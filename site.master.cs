using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;



using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Data;


using System.Configuration;
using System.Data.SqlClient;

namespace ALDP
{
    public partial class SiteMaster : MasterPage
    {
        protected void seccionClose_Click(object sender, EventArgs e)
        {
            Session["UserId"] = "";
            Session["UserRole"] = "";
            Session["UserName"] = "";
            Page.Response.Redirect("Default.aspx");
        }

        protected void Page_Load(object sender, EventArgs e)
        {
            var user = new UserRole();
            var loggedUserName = "";
            var loggedUserFulName = "";

            if (Session["UserId"] != null && Session["UserId"] !="")
            {
                if (topRibban != null)
                {
                    topRibban.Visible = true;
                    SessionName.InnerHtml = Session["UserId"].ToString();
                }
                loggedUserName = Session["UserId"].ToString();
                user = new UserRole
                {
                    Role = Session["UserRole"].ToString(),
                    UserFullName = Session["UserName"].ToString(),
                };
            }
            else
            {
                if (topRibban != null)
                {
                    topRibban.Visible = false;
                }     


                loggedUserName = HttpContext.Current.Request.LogonUserIdentity.Name;
                loggedUserName = loggedUserFulName= loggedUserName.Replace(@"SAMSUNG-AUSTIN\", "");
                Response.Write(loggedUserName);


                var adminController = new AdminAPI.AdminAPIController();
                user = adminController.GetUserRole(loggedUserName);
            }
            //System.Diagnostics.Debug.WriteLine("user role: " + user.Role);
            //System.Diagnostics.Debug.WriteLine("NT ID: " + user.UserId);
            hdnfldrole.Value = user != null && user.Role != null ? user.Role : "DefaultUser";
            hdnflduserFullName.Value = user != null && user.UserFullName != null ? user.UserFullName : "DefaultUser";

            if (hdnflduser != null)
                hdnflduser.Value = loggedUserName;


            var pageName = this.Page.Request.FilePath;
            
            string[] Admin = { "default.aspx", "Default", "Dashboard", "SchedulerAdmin", "History", "PocSeatAllocation", "CohortSchedule", "Attendance", "Scheduler", "AttendanceForNonAdmin", "Resources", "SchedulerDashboard", "ViewAllTickets", "settings" };
            string[] POC = { "default.aspx", "Default", "Dashboard", "Scheduler", "Resources", "SchedulerDashboard", "AttendanceForNonAdmin", "ViewAllTickets" };
            string[] DefaultUser = { "default.aspx", "Default", "Dashboard" };

            if (user != null && user.Role == "Admin")
            {
            }
            else if (user != null && user.Role == "POC")
            {
                var check = Array.FindIndex(POC, s => s.ToLower().Equals(pageName.Replace("/HarithaTestSite/", "").Replace("/", "").ToLower()));
                if (check == -1)
                {
                    if (!pageName.ToLower().Contains("error"))
                    {
                        Error_Log(new Exception("Page Not Found"), loggedUserFulName, loggedUserName, pageName);
                        //Response.Redirect("Error.aspx",false);
                        Response.Cache.SetCacheability(HttpCacheability.NoCache);
                        Response.Redirect("Error.aspx");
                        //Server.Transfer("Error.aspx",true);
                    }
                }
            }
            else
            {
                var check = Array.FindIndex(DefaultUser, s => s.ToLower().Equals(pageName.Replace("/HarithaTestSite/", "").Replace("/", "").ToLower()));
                if (check == -1)
                {
                    if (!pageName.ToLower().Contains("error"))
                    {
                        Response.Redirect("Error.aspx", false);
                    }
                }
            }
        }

        public void Error_Log(Exception exc, string loggedUserId,string loggedUserName, string page)
        {

            string connectionString = ConfigurationManager.ConnectionStrings["innovation"].ConnectionString;
            string ret = "";
            try
            {
                DateTime dateValue = DateTime.Now;
                string sqlFormattedDate = dateValue.ToString("yyyy-MM-dd HH:MM");
                //string query = " Update tbl_ALDP_Schedule_Program set [Status]='"+obj.Status+ "' ,[Comments]='" + obj.Comments + "' Where Id=" + obj.Id + "";
                string query = " insert into [dbo].[tbl_ALDP_ErrorLog] values ('" + loggedUserId + "','" + loggedUserName + "','" +page.Replace("/","") + "','" + exc.Message + "','" + (exc.StackTrace != null ? exc.StackTrace : "") + "','"+ sqlFormattedDate + "')";

                // create connection and command
                using (SqlConnection cn = new SqlConnection(connectionString))
                using (SqlCommand cmd = new SqlCommand(query, cn))
                {
                    // define parameters and their values
                    //cmd.Parameters.Add("@UserID", SqlDbType.NVarChar, 50).Value = loggedUserName;
                    //cmd.Parameters.Add("@UserName", SqlDbType.VarChar, 50).Value = loggedUserName;
                    //cmd.Parameters.Add("@Page", SqlDbType.VarChar, 50).Value = page;
                    //cmd.Parameters.Add("@Message", SqlDbType.VarChar, 50).Value = exc.Message;
                    //cmd.Parameters.Add("@StackTrace", SqlDbType.VarChar, 50).Value = exc.StackTrace != null ? exc.StackTrace : "";

                    // open connection, execute INSERT, close connection
                    cn.Open();
                    ret = cmd.CommandText;
                    cmd.ExecuteNonQuery();
                    cn.Close();
                }
            }
            catch (Exception ex)
            {

            }
        }
    }
}
