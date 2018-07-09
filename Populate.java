import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Populate {
    public HashSet<String> mainCategoriesHash= new HashSet<String>();
    private final int count = 0;

	public void jsonobj_recursive(JSONObject attr, String s, String bid, Connection c) throws SQLException{
        Iterator x = (Iterator) attr.keySet().iterator();
        int k=0;
        
        while (x.hasNext()){
        	k++;
            String key = (String) x.next();
            if(attr.get(key) instanceof JSONObject) {
            	if(s.isEmpty()) {
            		//System.out.println(k+": "+bid+" - "+key+"_"+attr.get(key)+" - Yes\n");
            		jsonobj_recursive((JSONObject)attr.get(key), key, bid, c);
            	}
            	else {
            		//System.out.println(k+": "+bid+" - "+s+"_"+key+"_"+attr.get(key)+" - Yes\n");
            		jsonobj_recursive((JSONObject)attr.get(key), s+"_"+key, bid, c);
            	}
            }
            else {
            	if(s.isEmpty()) {
                	//System.out.println(k+": "+bid+" - "+key+"_"+attr.get(key)+" - No\n");
                	PreparedStatement preparedStatement = null;
                    String sql = "INSERT INTO attributes (business_id, att_value) VALUES (?, ?)";
                    preparedStatement = c.prepareStatement(sql);
                    preparedStatement.setString(1, bid);
                    preparedStatement.setString(2, key+"_"+attr.get(key));
                    preparedStatement.executeUpdate();
                    preparedStatement.close();
            	}
                else {
                	//System.out.println(k+": "+bid+" - "+s+"_"+key+"_"+attr.get(key)+" - No\n");
                	PreparedStatement preparedStatement = null;
                    String sql = "INSERT INTO attributes (business_id, att_value) VALUES (?, ?)";
                    preparedStatement = c.prepareStatement(sql);
                    preparedStatement.setString(1, bid);
                    preparedStatement.setString(2, s+"_"+key+"_"+attr.get(key));
                    preparedStatement.executeUpdate();
                    preparedStatement.close();
                }
            }
        }		
	}

	public void read_checkin(Connection connection) throws SQLException {
        String checkinfile= "C:\\Users\\harit\\Documents\\Quarter_4\\DB\\Assignment_3\\YelpDataset\\YelpDataset\\yelp_checkin.json";

        String line = null;
        JSONObject obj;
        try {
	        // Always wrap FileReader in BufferedReader.
	        BufferedReader cbufferedReader = new BufferedReader(new FileReader(checkinfile));
	        int i=0;

	        while((line = cbufferedReader.readLine()) != null) {
	            obj = (JSONObject) new JSONParser().parse(line);

	            String b_id=(String)obj.get("business_id");
	            System.out.println(" bid:"+ b_id+ "\n");
	            JSONObject checkininfo=(JSONObject)obj.get("checkin_info");
	            Iterator r= (Iterator)checkininfo.keySet().iterator();
	            while(r.hasNext()){
	            	String hr_day= (String)r.next();
	            	long count= (long)checkininfo.get(hr_day);
	            	int cnt = Integer.parseInt(Long.toString(count));
	            	String[] parts = hr_day.split("-");
	            	int hr_start = Integer.parseInt(parts[0]);
	            	int hr_end= hr_start+1;
	            	int day = Integer.parseInt(parts[1]);
	            	
	            	System.out.println(" hr_Start:" +hr_start +" hr_end:"+hr_end + " Day:" +day+" Count:"+cnt+"\n");

	            	PreparedStatement preparedStatement = null;
		            String sql = "INSERT INTO checkin (business_id, from_time, to_time, day_checkin, count) VALUES (?, ?, ?, ?, ?)";
		            preparedStatement = connection.prepareStatement(sql);
	                preparedStatement.setString(1, b_id);
	                preparedStatement.setInt(2, hr_start);
	                preparedStatement.setInt(3, hr_end);
	                preparedStatement.setInt(4, day);
	                preparedStatement.setInt(5, cnt);
	                preparedStatement.executeUpdate();
	                preparedStatement.close();
	            }
	            
                i++;
                System.out.println(i+"\n");
                if(count > 0) {
                	if(i==count)
                		break;
                }
	        }
	              
	        cbufferedReader.close();
        }
        catch(FileNotFoundException ex) {
        	System.out.println("Unable to open file '" + checkinfile + "'");                
        }
        catch(IOException ex) {
        	System.out.println("Error reading file '" + checkinfile + "'");                  
        } catch (ParseException e) {
        	// TODO Auto-generated catch block
        	e.printStackTrace();
        }
	}
	
	
	public void read_users(Connection connection) throws SQLException {
        String userfile= "C:\\Users\\harit\\Documents\\Quarter_4\\DB\\Assignment_3\\YelpDataset\\YelpDataset\\yelp_user.json";

        String line = null;
        JSONObject obj;
        try {
	        // Always wrap FileReader in BufferedReader.
	        BufferedReader bufferedReader = new BufferedReader(new FileReader(userfile));
	        int i=0;

	        while((line = bufferedReader.readLine()) != null) {
	            obj = (JSONObject) new JSONParser().parse(line);

	            String u_id=(String)obj.get("user_id");
	            System.out.println(" uid:"+ u_id+ "\n");
	            String name=(String)obj.get("name");
	            String sql = "INSERT INTO yelp_user (user_id, name) VALUES (?, ?)";
	            PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, u_id);
                preparedStatement.setString(2, name);
                preparedStatement.executeUpdate();
                preparedStatement.close();
                
                i++;
                System.out.println(i+"\n");
                if(count > 0) {
                	if(i==count)
                		break;
                }
	        }
        
	        bufferedReader.close();
        }
        catch(FileNotFoundException ex) {
        	System.out.println("Unable to open file '" + userfile + "'");                
        }
        catch(IOException ex) {
        	System.out.println("Error reading file '" + userfile + "'");                  
        } catch (ParseException e) {
        	// TODO Auto-generated catch block
        	e.printStackTrace();
        }
	}
	
	
	public void read_reviews(Connection connection) throws SQLException {
        String reviewfile= "C:\\Users\\harit\\Documents\\Quarter_4\\DB\\Assignment_3\\YelpDataset\\YelpDataset\\yelp_review.json";

        String line = null;
        JSONObject obj;
        try {
	        // Always wrap FileReader in BufferedReader.
	        BufferedReader bufferedReader = new BufferedReader(new FileReader(reviewfile));
	        int i=0;

	        while((line = bufferedReader.readLine()) != null) {
	            obj = (JSONObject) new JSONParser().parse(line);

	            String r_id=(String)obj.get("review_id");
	            System.out.println(" rid:"+ r_id+ "\n");
	            int stars=Integer.parseInt(obj.get("stars").toString());
	            //System.out.println(" rid:"+ r_id+ "\n");
	            String date1=(String)obj.get("date");
	            String text=(String)obj.get("text");
	            JSONObject votes=(JSONObject)obj.get("votes");
	            int useful=Integer.parseInt(votes.get("useful").toString());
	            String business_id=(String)obj.get("business_id");
	            String user_id=(String)obj.get("user_id");

	            String sql = "INSERT INTO review (review_id, stars, date1, text, useful, business_id, user_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
	            PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, r_id);
                preparedStatement.setLong(2, stars);
                preparedStatement.setString(3, date1);
                preparedStatement.setString(4, text);
                preparedStatement.setLong(5, useful);
                preparedStatement.setString(6, business_id);
                preparedStatement.setString(7, user_id);
                preparedStatement.executeUpdate();
                preparedStatement.close();
                
                i++;
                System.out.println(i+"\n");
                if(count > 0) {
                	if(i==count)
                		break;
                }
	        }        
	        bufferedReader.close();
        }
        catch(FileNotFoundException ex) {
        	System.out.println("Unable to open file '" + reviewfile + "'");                
        }
        catch(IOException ex) {
        	System.out.println("Error reading file '" + reviewfile + "'");                  
        } catch (ParseException e) {
        	// TODO Auto-generated catch block
        	e.printStackTrace();
        }
	}

	public void read_business(Connection connection) throws SQLException{
	    String line = null;
	    JSONObject obj;
	    String businessfile = "C:\\Users\\harit\\Documents\\Quarter_4\\DB\\Assignment_3\\YelpDataset\\YelpDataset\\yelp_business.json";

	    try {
	        FileReader fileReader = new FileReader(businessfile);
	        BufferedReader bufferedReader = new BufferedReader(fileReader);

	        int i=0;
	        	        
	        while((line = bufferedReader.readLine()) != null) {
	        	
	            obj = (JSONObject) new JSONParser().parse(line);
		        PreparedStatement preparedStatement = null;	        
	            String sql;
	            String business_id= (String)obj.get("business_id");
	            String full_address= (String)obj.get("full_address");
	            boolean open= (boolean)obj.get("open");
	            String city= (String)obj.get("city");
	            long review_count= (long)obj.get("review_count");
	            String name= (String)obj.get("name");
	            String state= (String)obj.get("state");
	            double stars= (double)obj.get("stars");
	            sql = "INSERT INTO business (business_id, full_address, open, city, review_count, name, state, stars) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	            preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, business_id);
                preparedStatement.setString(2, full_address);
                preparedStatement.setBoolean(3, open);
                preparedStatement.setString(4, city);
                preparedStatement.setLong(5, review_count);
                preparedStatement.setString(6, name);
                preparedStatement.setString(7, state);
                preparedStatement.setDouble(8, stars);
                preparedStatement.executeUpdate();
                preparedStatement.close();
                
                
//              parsing and inserting into hours table
                
	            JSONObject hrs=(JSONObject)obj.get("hours");
	            String day;
	            JSONObject mdayobj=(JSONObject)hrs.get("Monday");
	            if(mdayobj!=null){
	            	day= "Monday";
	            	String ctime = (String)mdayobj.get("close");
	            	String otime = (String)mdayobj.get("open");

	            	//System.out.println("Monday: "+ "Close: "+ (String)dayobj.get("close")+ "open"+(String)dayobj.get("open") + "\n");
	            	String sql1 = "INSERT INTO hours (business_id, day, open, close) VALUES (?, ?, ?, ?)";
	    	        preparedStatement = connection.prepareStatement(sql1);
	                preparedStatement.setString(1, business_id);
	                preparedStatement.setString(2, day);
	                preparedStatement.setString(3, otime);
	                preparedStatement.setString(4, ctime);
	                preparedStatement.executeUpdate();
	                preparedStatement.close();
	            }
            	
	            JSONObject tdayobj=(JSONObject)hrs.get("Tuesday");
	            if(tdayobj!=null){
	            	day= "Tuesday";
	            	String ctime = (String)tdayobj.get("close");
	            	String otime = (String)tdayobj.get("open");

	            	//System.out.println("Monday: "+ "Close: "+ (String)dayobj.get("close")+ "open"+(String)dayobj.get("open") + "\n");
	            	String sql1 = "INSERT INTO hours (business_id, day, open, close) VALUES (?, ?, ?, ?)";
	    	        preparedStatement = connection.prepareStatement(sql1);
	                preparedStatement.setString(1, business_id);
	                preparedStatement.setString(2, day);
	                preparedStatement.setString(3, otime);
	                preparedStatement.setString(4, ctime);
	                preparedStatement.executeUpdate();
	                preparedStatement.close();
	            }
	            	
	            JSONObject wdayobj=(JSONObject)hrs.get("Wednesday");
	            if(wdayobj!=null){
	            	day= "Wednesday";
	            	String ctime = (String)wdayobj.get("close");
	            	String otime = (String)wdayobj.get("open");

	            	//System.out.println("Monday: "+ "Close: "+ (String)dayobj.get("close")+ "open"+(String)dayobj.get("open") + "\n");
	            	String sql1 = "INSERT INTO hours (business_id, day, open, close) VALUES (?, ?, ?, ?)";
	    	        preparedStatement = connection.prepareStatement(sql1);
	                preparedStatement.setString(1, business_id);
	                preparedStatement.setString(2, day);
	                preparedStatement.setString(3, otime);
	                preparedStatement.setString(4, ctime);
	                preparedStatement.executeUpdate();
	                preparedStatement.close();
	            }
	            	
	            JSONObject thdayobj=(JSONObject)hrs.get("Thursday");
	            if(thdayobj!=null){
	            	day= "Thursday";
	            	String ctime = (String)thdayobj.get("close");
	            	String otime = (String)thdayobj.get("open");

	            	//System.out.println("Monday: "+ "Close: "+ (String)dayobj.get("close")+ "open"+(String)dayobj.get("open") + "\n");
	            	String sql1 = "INSERT INTO hours (business_id, day, open, close) VALUES (?, ?, ?, ?)";
	    	        preparedStatement = connection.prepareStatement(sql1);
	                preparedStatement.setString(1, business_id);
	                preparedStatement.setString(2, day);
	                preparedStatement.setString(3, otime);
	                preparedStatement.setString(4, ctime);
	                preparedStatement.executeUpdate();
	                preparedStatement.close();
	            }
	            	
	            JSONObject fdayobj=(JSONObject)hrs.get("Friday");
	            if(fdayobj!=null){
	            	day= "Friday";
	            	String ctime = (String)fdayobj.get("close");
	            	String otime = (String)fdayobj.get("open");

	            	//System.out.println("Monday: "+ "Close: "+ (String)dayobj.get("close")+ "open"+(String)dayobj.get("open") + "\n");
	            	String sql1 = "INSERT INTO hours (business_id, day, open, close) VALUES (?, ?, ?, ?)";
	    	        preparedStatement = connection.prepareStatement(sql1);
	                preparedStatement.setString(1, business_id);
	                preparedStatement.setString(2, day);
	                preparedStatement.setString(3, otime);
	                preparedStatement.setString(4, ctime);
	                preparedStatement.executeUpdate();
	                preparedStatement.close();
	            }
	            	
	            JSONObject sadayobj=(JSONObject)hrs.get("Saturday");
	            if(sadayobj!=null){
	            	day= "Saturday";
	            	String ctime = (String)sadayobj.get("close");
	            	String otime = (String)sadayobj.get("open");

	            	//System.out.println("Monday: "+ "Close: "+ (String)dayobj.get("close")+ "open"+(String)dayobj.get("open") + "\n");
	            	String sql1 = "INSERT INTO hours (business_id, day, open, close) VALUES (?, ?, ?, ?)";
	    	        preparedStatement = connection.prepareStatement(sql1);
	                preparedStatement.setString(1, business_id);
	                preparedStatement.setString(2, day);
	                preparedStatement.setString(3, otime);
	                preparedStatement.setString(4, ctime);
	                preparedStatement.executeUpdate();
	                preparedStatement.close();
	            }
	            	
	            JSONObject sdayobj=(JSONObject)hrs.get("Sunday");
	            if(sdayobj!=null){
	            	day= "Sunday";
	            	String ctime = (String)sdayobj.get("close");
	            	String otime = (String)sdayobj.get("open");

	            	//System.out.println("Monday: "+ "Close: "+ (String)dayobj.get("close")+ "open"+(String)dayobj.get("open") + "\n");
	            	String sql1 = "INSERT INTO hours (business_id, day, open, close) VALUES (?, ?, ?, ?)";
	    	        preparedStatement = connection.prepareStatement(sql1);
	                preparedStatement.setString(1, business_id);
	                preparedStatement.setString(2, day);
	                preparedStatement.setString(3, otime);
	                preparedStatement.setString(4, ctime);
	                preparedStatement.executeUpdate();
	                preparedStatement.close();
	            }  
	            
//	            parsing and inserting into category table	            
	            
	            List<category> mainCategories = new ArrayList();
	            List<subcategory> subCategories = new ArrayList(); 
	            JSONArray arr = (JSONArray)obj.get("categories");
	            for (int j = 0; j < arr.size(); j++) {
                    String category = (String) arr.get(j);
                    //System.out.println("Read: "+category+"\n");
                    if (mainCategoriesHash.contains(category)) {
                        mainCategories.add(new category(business_id, category));
                    }
                    else {
                        subCategories.add(new subcategory(business_id, category));
                    }
                }
                //System.out.println("Insert data into MainCategory table...");
                sql = "INSERT INTO category (business_id, category) VALUES (?, ?)";
                preparedStatement = connection.prepareStatement(sql);
                for (category m: mainCategories) {
                	//System.out.println("Inserting: "+m.business_id+", "+m.mainCategory+"\n");
                	preparedStatement.setString(1, m.business_id);
                    preparedStatement.setString(2, m.mainCategory);
                    preparedStatement.executeUpdate();
                }
                preparedStatement.close();
                
                //System.out.println("Insert data into SubCategory table...");
                sql = "INSERT INTO subcategory (business_id, subCategory) VALUES (?, ?)";
                preparedStatement = connection.prepareStatement(sql);
                for (subcategory s: subCategories) {
                	preparedStatement.setString(1, s.business_id);
                	preparedStatement.setString(2, s.subcategory);
                	preparedStatement.executeUpdate();
                }
                preparedStatement.close();
                
                /* Parsing attributes */
                jsonobj_recursive((JSONObject)obj.get("attributes"), "", business_id, connection);
                
                i++;
                System.out.println(i+"\n");
                if(count > 0) {
                	if(i==count)
                		break;
                }
	        }
	        bufferedReader.close();
	    }
       
		 catch(FileNotFoundException ex) {
			 System.out.println("Unable to open file '" + businessfile + "'");                
		 }
		 catch(IOException ex) {
		     System.out.println("Error reading file '" + businessfile + "'");                  
		 } catch (ParseException e) {
		     e.printStackTrace();
		 }	        
	}
	
	public Populate() throws Exception, SQLException {
			    
	    Connection connection = null;
        Class.forName("oracle.jdbc.driver.OracleDriver");        
        connection = DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1521:hmannuru", "scott", "tiger");

        mainCategoriesHash.add("Active Life");
        mainCategoriesHash.add("Arts & Entertainment");
        mainCategoriesHash.add("Automotive");
        mainCategoriesHash.add("Car Rental");
        mainCategoriesHash.add("Cafes");
        mainCategoriesHash.add("Beauty & Spas");
        mainCategoriesHash.add("Convenience Stores");
        mainCategoriesHash.add("Dentists");
        mainCategoriesHash.add("Doctors");
        mainCategoriesHash.add("Drugstores");
        mainCategoriesHash.add("Department Stores");
        mainCategoriesHash.add("Education");
        mainCategoriesHash.add("Event Planning & Services");
        mainCategoriesHash.add("Flowers & Gifts");
        mainCategoriesHash.add("Food");
        mainCategoriesHash.add("Health & Medical");
        mainCategoriesHash.add("Home Services");
        mainCategoriesHash.add("Home & Garden");
        mainCategoriesHash.add("Hospitals");
        mainCategoriesHash.add("Hotels & Travel");
        mainCategoriesHash.add("Hardware Stores");
        mainCategoriesHash.add("Grocery");
        mainCategoriesHash.add("Medical Centers");
        mainCategoriesHash.add("Nurseries & Gardening");
        mainCategoriesHash.add("Nightlife");
        mainCategoriesHash.add("Restaurants");
        mainCategoriesHash.add("Shopping");
        mainCategoriesHash.add("Transportation");

        //starting to read business file
        read_business(connection);
        
        //starting to read checkin file	        	        
        read_checkin(connection);

        //starting to read Users file	        
        read_users(connection);

        //starting to read Reviews file	        
        read_reviews(connection);

        connection.close();

	}
	
	public static void main(String[] args) throws SQLException, Exception {
		new Populate();
	}
}
