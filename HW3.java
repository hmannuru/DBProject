import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.util.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class HW3 {

    public HashSet<String> panelMainCategories= new HashSet<String>();
    public HashMap<String, Integer> panelSubCategories= new HashMap<String, Integer>();
    public HashMap<String, Integer> panelAttributes= new HashMap<String, Integer>();
    public HashMap<String, Integer> panelLocs = new HashMap<String, Integer>();
    public HashMap<String, Integer> panelDays = new HashMap<String, Integer>();
    public HashMap<String, Integer> panelFromtimes = new HashMap<String, Integer>();
    public HashMap<String, Integer> panelTotimes = new HashMap<String, Integer>();
    public HashMap<String, Integer> selectMainCategories= new HashMap<String, Integer>();
    public HashMap<String, Integer> selectSubCategories= new HashMap<String, Integer>();
    public HashMap<String, Integer> selectAttributes= new HashMap<String, Integer>();
    public HashSet<String> businessres = new HashSet<String>();
    public String selectLoc="";
    public String selectDay="";
    public String selectFromtime="";
    public String selectTotime="";
    public HashMap<String, JCheckBox> sccheckbox= new HashMap<String, JCheckBox>();
    public HashMap<String, JCheckBox> attrcheckbox= new HashMap<String, JCheckBox>();
    JPanel mcpanel;
    JPanel scpanel;
    JPanel attrpanel;
    JPanel locdayhrpanel;
    JPanel businesspanel;
    JTable bustable;
    JPanel searchpanel;
    JComboBox<String> loc;
    JComboBox<String> day;
    JComboBox<String> fromtime;
    JComboBox<String> totime;
    JFrame frame;
    boolean handleloc;
    boolean handleday;
    boolean handlefromtime;
    boolean handletotime;
    boolean andselected;
    boolean orselected;
    private long starttime;
    private long totaltime;
    
    private void defaultandor(){
    	andselected = false;
    	orselected = true;
    }
    
    private void disablecombos(){
    	handleloc = false;
    	handleday = false;
    	handlefromtime = false;
    	handletotime = false;
    }
    
    private void enablecombos(){
    	handleloc = true;
    	handleday = true;
    	handlefromtime = true;
    	handletotime = true;
    }
   
    private void reset_sc_panel(Connection connection) throws SQLException{
    	System.out.println("Reset SC panel");    	
    	sccheckbox.clear();
    	panelSubCategories.clear();
    	selectSubCategories.clear();
    	scpanel.removeAll();
    	//frame.repaint();
    	
    	StringBuilder auxsql= new StringBuilder();
    	auxsql.append("DELETE FROM AUX_CAT_SUBCAT aux").append("\n");          	  
    	PreparedStatement preparedStatement = connection.prepareStatement(auxsql.toString());
    	preparedStatement.executeQuery();
    	preparedStatement.close();
    	
    	StringBuilder aux1sql= new StringBuilder();
    	aux1sql.append("DELETE FROM AUX_BUS_USEDBY_SUBCAT aux").append("\n");          	  
    	preparedStatement = connection.prepareStatement(aux1sql.toString());
    	preparedStatement.executeQuery();
    	preparedStatement.close();
    }

    private void reset_attr_panel(Connection connection) throws SQLException{
    	System.out.println("Reset Attr panel");    	
    	attrcheckbox.clear();
    	panelAttributes.clear();
    	selectAttributes.clear();
    	attrpanel.removeAll();
    	//frame.repaint();

    	StringBuilder auxsql= new StringBuilder();
    	auxsql.append("DELETE FROM AUX_CAT_SUBCAT_ATTR aux").append("\n");          	  
    	PreparedStatement preparedStatement = connection.prepareStatement(auxsql.toString());
    	preparedStatement.executeQuery();
    	preparedStatement.close();

    	StringBuilder aux1sql= new StringBuilder();
    	aux1sql.append("DELETE FROM AUX_BUS_USEDBY_ATTR aux").append("\n");          	  
    	preparedStatement = connection.prepareStatement(aux1sql.toString());
    	preparedStatement.executeQuery();
    	preparedStatement.close();
    }
    
    private void reset_loc_panel(Connection connection) throws SQLException{
    	System.out.println("Reset Loc panel");
    	panelLocs.clear();
    	loc.removeAllItems();

    	StringBuilder auxsql= new StringBuilder();
    	auxsql.append("DELETE FROM AUX_CAT_SUBCAT_ATTR_LOC aux").append("\n");          	  
    	PreparedStatement preparedStatement = connection.prepareStatement(auxsql.toString());
    	preparedStatement.executeQuery();
    	preparedStatement.close();

    	StringBuilder aux1sql= new StringBuilder();
    	aux1sql.append("DELETE FROM AUX_BUS_USEDBY_LOC aux").append("\n");          	  
    	preparedStatement = connection.prepareStatement(aux1sql.toString());
    	preparedStatement.executeQuery();
    	preparedStatement.close();
    }

    private void reset_day_panel(Connection connection) throws SQLException{
    	System.out.println("Reset Day panel");
    	panelDays.clear();
    	day.removeAllItems();

    	StringBuilder auxsql= new StringBuilder();
    	auxsql.append("DELETE FROM AUX_CAT_SUBCAT_ATTR_LOC_DAY aux").append("\n");          	  
    	PreparedStatement preparedStatement = connection.prepareStatement(auxsql.toString());
    	preparedStatement.executeQuery();
    	preparedStatement.close();

    	StringBuilder aux1sql= new StringBuilder();
    	aux1sql.append("DELETE FROM AUX_BUS_USEDBY_DAY aux").append("\n");          	  
    	preparedStatement = connection.prepareStatement(aux1sql.toString());
    	preparedStatement.executeQuery();
    	preparedStatement.close();
    }

    private void reset_fromtime_panel(Connection connection) throws SQLException{
    	System.out.println("Reset Fromtime panel");
    	panelFromtimes.clear();
    	fromtime.removeAllItems();

    	StringBuilder auxsql= new StringBuilder();
    	auxsql.append("DELETE FROM AUX_MC_SC_AT_LOC_DAY_FROM aux").append("\n");          	  
    	PreparedStatement preparedStatement = connection.prepareStatement(auxsql.toString());
    	preparedStatement.executeQuery();
    	preparedStatement.close();

    	StringBuilder aux1sql= new StringBuilder();
    	aux1sql.append("DELETE FROM AUX_BUS_USEDBY_FROM aux").append("\n");          	  
    	preparedStatement = connection.prepareStatement(aux1sql.toString());
    	preparedStatement.executeQuery();
    	preparedStatement.close();
    }

    private void reset_totime_panel(Connection connection) throws SQLException{
    	System.out.println("Reset Totime panel");
    	panelTotimes.clear();
    	totime.removeAllItems();

    	StringBuilder auxsql= new StringBuilder();
    	auxsql.append("DELETE FROM AUX_MC_SC_AT_LOC_DAY_FROM_TO aux").append("\n");          	  
    	PreparedStatement preparedStatement = connection.prepareStatement(auxsql.toString());
    	preparedStatement.executeQuery();
    	preparedStatement.close();

    	StringBuilder aux1sql= new StringBuilder();
    	aux1sql.append("DELETE FROM AUX_BUS_USEDBY_TO aux").append("\n");          	  
    	preparedStatement = connection.prepareStatement(aux1sql.toString());
    	preparedStatement.executeQuery();
    	preparedStatement.close();
    }
    
    private void reset_business_search_table(Connection connection) throws SQLException{
    	System.out.println("Reset business search table");
    	businessres.clear();
    	
    	StringBuilder auxsql= new StringBuilder();
    	auxsql.append("DELETE FROM AUX_BUSINESS_RESULT aux").append("\n");          	  
    	PreparedStatement preparedStatement = connection.prepareStatement(auxsql.toString());
    	preparedStatement.executeQuery();
    	preparedStatement.close();    	
    }
    
    private void update_panels_for_mc(){
        try {			 
        	starttime = System.nanoTime();
	        Connection connection = null;
	        Class.forName("oracle.jdbc.driver.OracleDriver");
			 connection = DriverManager.getConnection(
				         "jdbc:oracle:thin:@localhost:1521:hmannuru", "scott", "tiger");
			 
			 /*Disable comboboxes listeners*/
			 disablecombos();
			 
			 /*Reset panels and AUX tables*/
			 reset_sc_panel(connection);
			 reset_attr_panel(connection);
			 reset_loc_panel(connection);
			 reset_day_panel(connection);
			 reset_fromtime_panel(connection);
			 reset_totime_panel(connection);
			 reset_business_search_table(connection);

			 if(selectMainCategories.isEmpty()){
				 connection.close();
				 enablecombos();
				 return;				 
			 }

			 Iterator<String> mci=selectMainCategories.keySet().iterator();
			 String bussql = "";
			 int z=0;
			 if(orselected) {
				 while(mci.hasNext()){
					 if(z == 0)						 
						 bussql = bussql+"SELECT business_id from category c where c.category='"+mci.next()+"'";
					 else
						 bussql = bussql+"UNION SELECT business_id from category c where c.category='"+mci.next()+"'";						 
					 z++;				 
				 }
			 }
			 else{
				 while(mci.hasNext()){
					 if(z == 0)						 
						 bussql = bussql+"SELECT business_id from category c where c.category='"+mci.next()+"'";
					 else
						 bussql = bussql+"INTERSECT SELECT business_id from category c where c.category='"+mci.next()+"'";						 
					 z++;				 
				 }
			 }

			 /*First find and fill business table for selected MCs*/
			 ResultSet busrs;
			 PreparedStatement p4 = connection.prepareStatement(bussql);
			 busrs= p4.executeQuery();
			 while (busrs.next()) {
				 String bid = busrs.getString(busrs.findColumn("business_id"));
				 businessres.add(bid);
				 
				 StringBuilder resultsql = new StringBuilder();
				 resultsql.append("INSERT INTO AUX_BUSINESS_RESULT (business_id) VALUES(?)");
				 PreparedStatement p3 = connection.prepareStatement(resultsql.toString());
				 p3.setString(1, bid);
				 p3.executeUpdate();
				 p3.close();

				 String tempsql = "INSERT INTO AUX_BUS_USEDBY_SUBCAT (business_id) VALUES(?)";
				 p3 = connection.prepareStatement(tempsql);
				 p3.setString(1, bid);
				 p3.executeUpdate();
				 p3.close();

				 tempsql = "INSERT INTO AUX_BUS_USEDBY_ATTR (business_id) VALUES(?)";
				 p3 = connection.prepareStatement(tempsql);
				 p3.setString(1, bid);
				 p3.executeUpdate();
				 p3.close();

				 tempsql = "INSERT INTO AUX_BUS_USEDBY_LOC (business_id) VALUES(?)";
				 p3 = connection.prepareStatement(tempsql);
				 p3.setString(1, bid);
				 p3.executeUpdate();
				 p3.close();

				 tempsql = "INSERT INTO AUX_BUS_USEDBY_DAY (business_id) VALUES(?)";
				 p3 = connection.prepareStatement(tempsql);
				 p3.setString(1, bid);
				 p3.executeUpdate();
				 p3.close();

				 tempsql = "INSERT INTO AUX_BUS_USEDBY_FROM (business_id) VALUES(?)";
				 p3 = connection.prepareStatement(tempsql);
				 p3.setString(1, bid);
				 p3.executeUpdate();
				 p3.close();

				 tempsql = "INSERT INTO AUX_BUS_USEDBY_TO (business_id) VALUES(?)";
				 p3 = connection.prepareStatement(tempsql);
				 p3.setString(1, bid);
				 p3.executeUpdate();
				 p3.close();
			 }
			 p4.close();

//			 /*Create index on business table*/
//			 String indsql = "create index business_result_index on AUX_BUSINESS_RESULT(business_id)";
//			 p4 = connection.prepareStatement(indsql);
//			 p4.executeUpdate();
//			 p4.close();
			 
			 /* Update SC panel based on filtered Businesses for selected MCs*/
			 StringBuilder scsql= new StringBuilder();
			 ResultSet scrs;
			 scsql.append("SELECT DISTINCT s.subcategory").append("\n")
	        	  .append("FROM subcategory s, AUX_BUSINESS_RESULT aux").append("\n")
	        	  .append("WHERE s.business_id=aux.business_id").append("\n")
	        	  .append("ORDER BY subcategory");
			PreparedStatement preparedStatement;
	        preparedStatement = connection.prepareStatement(scsql.toString());
	        scrs= preparedStatement.executeQuery();
	        
		    int x=0;
	       	//Iterator to go over the result set and populate them into the SubCategories panel as checkboxes
		    while (scrs.next()) {
		    	String scname = scrs.getString(scrs.findColumn("subcategory"));
		    	x++;
		    	System.out.println(x+": "+scname+"\n");

		    	/*Insert into AUX_CAT_SUBCAT table*/
		    	StringBuilder auxsql= new StringBuilder();
		    	auxsql.append("INSERT INTO AUX_CAT_SUBCAT (business_id, category, subcategory ) VALUES(?, ?, ?)");
		    	PreparedStatement p1 = connection.prepareStatement(auxsql.toString());
		    	p1.setString(1, "NA");
		    	p1.setString(2, "NA");
		    	p1.setString(3, scname);
		    	p1.executeUpdate();
		    	p1.close();
		    	
		    	if(panelSubCategories.containsKey(scname)){
		    		System.out.println("BAD "+scname+", already exists");
		    	}
		    	else{
		    		panelSubCategories.put(scname, 1);
		    		JCheckBox sc= new JCheckBox(scname);

		    		// adding action listeners to sub categories check boxes
		    		sc.addActionListener(new scActionListener());
		    		sccheckbox.put(scname, sc);
		    		scpanel.add(sc);
		    	}
		    }
		    preparedStatement.close();
		    if(orselected)
		    	System.out.println("Count of SC selected by ORing MCs = "+x+"\n");
		    else
		    	System.out.println("Count of SC selected by ANDing MCs = "+x+"\n");
	   	 		 
			 
			 /* Update Loc panel based on filtered Businesses for selected MCs*/
			 StringBuilder locsql= new StringBuilder();
			 ResultSet locrs;
			 locsql.append("SELECT DISTINCT b.city, b.state").append("\n")
	        	  .append("FROM business b, AUX_BUSINESS_RESULT aux").append("\n")
	        	  .append("WHERE b.business_id=aux.business_id").append("\n")
	        	  .append("ORDER BY b.city");

	        preparedStatement = connection.prepareStatement(locsql.toString());
	        locrs= preparedStatement.executeQuery();
	        
		    x=0;
	       	//Iterator to go over the result set and populate them into the Loc panel as combobox
		    while (locrs.next()) {
	           	 String city = locrs.getString(locrs.findColumn("city"));
	           	 String state = locrs.getString(locrs.findColumn("state"));
	           	 String locname = city+","+state;
	           	 x++;
	           	 System.out.println(x+": "+locname+"\n");
	 
	           	 /*Insert into AUX_CAT_SUBCAT_ATTR_LOC table*/
	           	 StringBuilder auxsql= new StringBuilder();
	           	 auxsql.append("INSERT INTO AUX_CAT_SUBCAT_ATTR_LOC (business_id, category, subcategory, att_value, loc) VALUES(?, ?, ?, ?, ?)");
	           	 PreparedStatement p1 = connection.prepareStatement(auxsql.toString());
	           	 p1.setString(1, "NA");
	           	 p1.setString(2, "NA");
	           	 p1.setString(3, "NA");
	           	 p1.setString(4, "NA");
	           	 p1.setString(5, locname);
	           	 p1.executeUpdate();
	           	 p1.close();
	           	 
	           	 if(panelLocs.containsKey(locname)){
			    		System.out.println("BAD "+locname+", already exists");
	           	 }
	           	 else{
	           		 panelLocs.put(locname, 1);
	           		 loc.addItem(locname);
	           	 }
		    }
		    preparedStatement.close();
		    if(orselected)
		    	System.out.println("Count of Locs selected by ORing MCs = "+x+"\n");
		    else
		    	System.out.println("Count of Locs selected by ANDing MCs = "+x+"\n");
	   	
		    
			 /* Update Day panel based on filtered Businesses for selected MCs*/
			 StringBuilder daysql= new StringBuilder();
			 ResultSet dayrs;
			 daysql.append("SELECT DISTINCT h.day, h.open, h.close").append("\n")
	        	  .append("FROM hours h, AUX_BUSINESS_RESULT aux").append("\n")
	        	  .append("WHERE h.business_id=aux.business_id").append("\n")
	        	  .append("ORDER BY h.day");
	        preparedStatement = connection.prepareStatement(daysql.toString());
	        dayrs= preparedStatement.executeQuery();
	        
		    x=0;
	       	//Iterator to go over the result set and populate them into the Loc panel as combobox
		    while (dayrs.next()) {
	           	 String dayname = dayrs.getString(dayrs.findColumn("day"));
	           	 String opentime = dayrs.getString(dayrs.findColumn("open"));
	           	 String closetime = dayrs.getString(dayrs.findColumn("close"));
	           	 x++;
	           	 System.out.println(x+": "+dayname+"\n");
	           	 System.out.println(x+": "+opentime+"\n");
	           	 System.out.println(x+": "+closetime+"\n");
	 
	           	 PreparedStatement p1;

	           	 if(!panelDays.containsKey(dayname)){
	           		 /*Insert into AUX_CAT_SUBCAT_ATTR_LOC_DAY table if not already inserted earlier*/
	           		 StringBuilder auxsql= new StringBuilder();
	           		 auxsql.append("INSERT INTO AUX_CAT_SUBCAT_ATTR_LOC_DAY (business_id, category, subcategory, att_value, loc, day) VALUES(?, ?, ?, ?, ?, ?)");
	           		 p1 = connection.prepareStatement(auxsql.toString());
	           		 p1.setString(1, "NA");
	           		 p1.setString(2, "NA");
	           		 p1.setString(3, "NA");
	           		 p1.setString(4, "NA");
	           		 p1.setString(5, "NA");
	           		 p1.setString(6, dayname);
	           		 p1.executeUpdate();
	           		 p1.close();
	           	 }

	           	 if(!panelFromtimes.containsKey(opentime)){
	           		 /*Insert into AUX_CAT_SUBCAT_ATTR_LOC_DAY_FROM table if not already inserted earlier*/
	           		 StringBuilder aux1sql= new StringBuilder();
	           		 aux1sql.append("INSERT INTO AUX_MC_SC_AT_LOC_DAY_FROM (business_id, category, subcategory, att_value, loc, day, open) VALUES(?, ?, ?, ?, ?, ?, ?)");
	           		 p1 = connection.prepareStatement(aux1sql.toString());
	           		 p1.setString(1, "NA");
	           		 p1.setString(2, "NA");
	           		 p1.setString(3, "NA");
	           		 p1.setString(4, "NA");
	           		 p1.setString(5, "NA");
	           		 p1.setString(6, "NA");
	           		 p1.setString(7, opentime);
	           		 p1.executeUpdate();
	           		 p1.close();
	           	 }

	           	 if(!panelTotimes.containsKey(closetime)){
	           		 /*Insert into AUX_CAT_SUBCAT_ATTR_LOC_DAY_FROM_TO table if not already inserted earlier*/
	           		 StringBuilder aux2sql= new StringBuilder();
	           		 aux2sql.append("INSERT INTO AUX_MC_SC_AT_LOC_DAY_FROM_TO (business_id, category, subcategory, att_value, loc, day, open, close) VALUES(?, ?, ?, ?, ?, ?, ?, ?)");
	           		 p1 = connection.prepareStatement(aux2sql.toString());
	           		 p1.setString(1, "NA");
	           		 p1.setString(2, "NA");
	           		 p1.setString(3, "NA");
	           		 p1.setString(4, "NA");
	           		 p1.setString(5, "NA");
	           		 p1.setString(6, "NA");
	           		 p1.setString(7, "NA");
	           		 p1.setString(8, closetime);
	           		 p1.executeUpdate();
	           		 p1.close();
	           	 }

	           	 /*Insert to Days combobox*/
	           	 if(panelDays.containsKey(dayname)){
			    	System.out.println("Duplicate Day: "+dayname);
			    	int count= panelDays.get(dayname);
			    	count= count+1;
			    	panelDays.replace(dayname, count);
	           	 }
       			 else{
       				 panelDays.put(dayname, 1);
       				 day.addItem(dayname);
       			 }
	           	 
	           	 /*Insert to From combobox*/
	           	 if(panelFromtimes.containsKey(opentime)){
	           		 System.out.println("Duplicate Fromtime: "+opentime);
	           		 int count= panelFromtimes.get(opentime);
	           		 count= count+1;
	           		 panelFromtimes.replace(opentime, count);
	           	 }
	           	 else{
	           		 panelFromtimes.put(opentime, 1);
	           		 fromtime.addItem(opentime);
	           	 }

	           	 /*Insert to To combobox*/
	           	 if(panelTotimes.containsKey(closetime)){
	           		 System.out.println("Duplicate Totime: "+closetime);
	           		 int count= panelTotimes.get(closetime);
	           		 count= count+1;
	           		 panelTotimes.replace(closetime, count);
	           	 }
	           	 else{
	           		 panelTotimes.put(closetime, 1);
	           		 totime.addItem(closetime);
	           	 }
		    }
		    preparedStatement.close();
		    if(orselected)
		    	System.out.println("Count of Hours selected by ORing MCs = "+x+"\n");
		    else
		    	System.out.println("Count of Hours selected by ANDing MCs = "+x+"\n");
			 
//			 /*Drop index on business table*/
//			 String inddropsql = "drop index business_result_index1";
//			 p4 = connection.prepareStatement(inddropsql);
//			 p4.executeUpdate();
//			 p4.close();

			 connection.close();
			 /*enable back combo listeners*/
			 enablecombos();
			 /*set AND/OR to default*/
			 defaultandor();
			 totaltime = System.nanoTime() - starttime;
			 System.out.println("MC: Time taken - "+(totaltime/1.0E09)+" seconds");
        }
        catch (ClassNotFoundException e1) {
        	// TODO Auto-generated catch block
        	e1.printStackTrace();
        } catch (SQLException e1) {
        	// TODO Auto-generated catch block
        	e1.printStackTrace();
        }
    }
    
    private void update_panels_for_sc(){
        try {
        	if(selectSubCategories.isEmpty()){
        		update_panels_for_mc();
        		return;
        	}

        	starttime = System.nanoTime();
        	Connection connection = null;
        	Class.forName("oracle.jdbc.driver.OracleDriver");
        	connection = DriverManager.getConnection(
        			"jdbc:oracle:thin:@localhost:1521:hmannuru", "scott", "tiger");

        	/*Disable comboboxes listeners*/
        	disablecombos();

        	/*Reset panels and AUX tables*/
        	reset_attr_panel(connection);
        	reset_loc_panel(connection);
        	reset_day_panel(connection);
        	reset_fromtime_panel(connection);
        	reset_totime_panel(connection);
        	reset_business_search_table(connection);

        	Iterator<String> sci=selectSubCategories.keySet().iterator();
        	String bussql = "";
        	int z=0;
        	if(orselected) {
        		while(sci.hasNext()){
        			if(z == 0)						 
        				bussql = bussql+"SELECT s.business_id from subcategory s, AUX_BUS_USEDBY_SUBCAT aux where s.business_id=aux.business_id and s.subcategory='"+sci.next()+"'";
        			else
        				bussql = bussql+"UNION SELECT s.business_id from subcategory s, AUX_BUS_USEDBY_SUBCAT aux where s.business_id=aux.business_id and s.subcategory='"+sci.next()+"'";						 
        			z++;				 
        		}
        	}
        	else{
        		while(sci.hasNext()){
        			if(z == 0)						 
        				bussql = bussql+"SELECT s.business_id from subcategory s, AUX_BUS_USEDBY_SUBCAT aux where s.business_id=aux.business_id and s.subcategory='"+sci.next()+"'";
        			else
        				bussql = bussql+"INTERSECT SELECT s.business_id from subcategory s, AUX_BUS_USEDBY_SUBCAT aux where s.business_id=aux.business_id and s.subcategory='"+sci.next()+"'";						 
        			z++;				 
        		}
        	}

        	/*First find and fill business table for selected SCs*/
        	ResultSet busrs;
        	PreparedStatement p4 = connection.prepareStatement(bussql);
        	busrs= p4.executeQuery();
        	while (busrs.next()) {
        		String bid = busrs.getString(busrs.findColumn("business_id"));
        		businessres.add(bid);
        		StringBuilder resultsql = new StringBuilder();
        		resultsql.append("INSERT INTO AUX_BUSINESS_RESULT (business_id) VALUES(?)");
        		PreparedStatement p3 = connection.prepareStatement(resultsql.toString());
        		p3.setString(1, bid);
        		p3.executeUpdate();
        		p3.close();

        		String tempsql = "INSERT INTO AUX_BUS_USEDBY_ATTR (business_id) VALUES(?)";
				 p3 = connection.prepareStatement(tempsql);
				 p3.setString(1, bid);
				 p3.executeUpdate();
				 p3.close();

				 tempsql = "INSERT INTO AUX_BUS_USEDBY_LOC (business_id) VALUES(?)";
				 p3 = connection.prepareStatement(tempsql);
				 p3.setString(1, bid);
				 p3.executeUpdate();
				 p3.close();

				 tempsql = "INSERT INTO AUX_BUS_USEDBY_DAY (business_id) VALUES(?)";
				 p3 = connection.prepareStatement(tempsql);
				 p3.setString(1, bid);
				 p3.executeUpdate();
				 p3.close();

				 tempsql = "INSERT INTO AUX_BUS_USEDBY_FROM (business_id) VALUES(?)";
				 p3 = connection.prepareStatement(tempsql);
				 p3.setString(1, bid);
				 p3.executeUpdate();
				 p3.close();

				 tempsql = "INSERT INTO AUX_BUS_USEDBY_TO (business_id) VALUES(?)";
				 p3 = connection.prepareStatement(tempsql);
				 p3.setString(1, bid);
				 p3.executeUpdate();
				 p3.close();
        	}
        	p4.close();

//			 /*Create index on business table*/
//			 String indsql = "create index business_result_index on AUX_BUSINESS_RESULT(business_id)";
//			 p4 = connection.prepareStatement(indsql);
//			 p4.executeUpdate();
//			 p4.close();
			 
        	
			 /* Update Attr panel based on filtered Businesses for selected SCs*/
			 StringBuilder attrsql= new StringBuilder();
			 ResultSet attrrs;
			 attrsql.append("SELECT DISTINCT att_value").append("\n")
	        	  .append("FROM attributes a, AUX_BUSINESS_RESULT aux").append("\n")
	        	  .append("WHERE a.business_id=aux.business_id").append("\n")
	        	  .append("ORDER BY att_value");
			PreparedStatement preparedStatement;
	        preparedStatement = connection.prepareStatement(attrsql.toString());
	        attrrs= preparedStatement.executeQuery();
	        
		    int x=0;
	       	//Iterator to go over the result set and populate them into the SubCategories panel as checkboxes
		    while (attrrs.next()) {
		    	String attrname = attrrs.getString(attrrs.findColumn("att_value"));
		    	x++;
		    	System.out.println(x+": "+attrname+"\n");

		    	if(panelAttributes.containsKey(attrname)){
		    		System.out.println("BAD "+attrname+", already exists");
		    	}
		    	else{
		    		panelAttributes.put(attrname, 1);
		    		JCheckBox sc= new JCheckBox(attrname);

		    		// adding action listeners to sub categories check boxes
		    		sc.addActionListener(new attrActionListener());
		    		attrcheckbox.put(attrname, sc);
		    		attrpanel.add(sc);
		    	}
		    }
		    preparedStatement.close();
		    if(orselected)
		    	System.out.println("Count of Attrs selected by ORing SCs = "+x+"\n");
		    else
		    	System.out.println("Count of Attrs selected by ANDing SCs = "+x+"\n");
	   	 		 
		    
			 /* Update Loc panel based on filtered Businesses for selected SCs*/
			 StringBuilder locsql= new StringBuilder();
			 ResultSet locrs;
			 locsql.append("SELECT DISTINCT b.city, b.state").append("\n")
	        	  .append("FROM business b, AUX_BUSINESS_RESULT aux").append("\n")
	        	  .append("WHERE b.business_id=aux.business_id").append("\n")
	        	  .append("ORDER BY b.city");

	        preparedStatement = connection.prepareStatement(locsql.toString());
	        locrs= preparedStatement.executeQuery();
	        
		    x=0;
	       	//Iterator to go over the result set and populate them into the Loc panel as combobox
		    while (locrs.next()) {
	           	 String city = locrs.getString(locrs.findColumn("city"));
	           	 String state = locrs.getString(locrs.findColumn("state"));
	           	 String locname = city+","+state;
	           	 x++;
	           	 System.out.println(x+": "+locname+"\n");
	 
	           	 /*Insert into AUX_CAT_SUBCAT_ATTR_LOC table*/
	           	 StringBuilder auxsql= new StringBuilder();
	           	 auxsql.append("INSERT INTO AUX_CAT_SUBCAT_ATTR_LOC (business_id, category, subcategory, att_value, loc) VALUES(?, ?, ?, ?, ?)");
	           	 PreparedStatement p1 = connection.prepareStatement(auxsql.toString());
	           	 p1.setString(1, "NA");
	           	 p1.setString(2, "NA");
	           	 p1.setString(3, "NA");
	           	 p1.setString(4, "NA");
	           	 p1.setString(5, locname);
	           	 p1.executeUpdate();
	           	 p1.close();
	           	 
	           	 if(panelLocs.containsKey(locname)){
			    		System.out.println("BAD "+locname+", already exists");
	           	 }
	           	 else{
	           		 panelLocs.put(locname, 1);
	           		 loc.addItem(locname);
	           	 }
		    }
		    preparedStatement.close();
		    if(orselected)
		    	System.out.println("Count of Locs selected by ORing SCs = "+x+"\n");
		    else
		    	System.out.println("Count of Locs selected by ANDing SCs = "+x+"\n");
	   	
		    
			 /* Update Day panel based on filtered Businesses for selected MCs*/
			 StringBuilder daysql= new StringBuilder();
			 ResultSet dayrs;
			 daysql.append("SELECT DISTINCT h.day, h.open, h.close").append("\n")
	        	  .append("FROM hours h, AUX_BUSINESS_RESULT aux").append("\n")
	        	  .append("WHERE h.business_id=aux.business_id").append("\n")
	        	  .append("ORDER BY h.day");
	        preparedStatement = connection.prepareStatement(daysql.toString());
	        dayrs= preparedStatement.executeQuery();
	        
		    x=0;
	       	//Iterator to go over the result set and populate them into the Loc panel as combobox
		    while (dayrs.next()) {
	           	 String dayname = dayrs.getString(dayrs.findColumn("day"));
	           	 String opentime = dayrs.getString(dayrs.findColumn("open"));
	           	 String closetime = dayrs.getString(dayrs.findColumn("close"));
	           	 x++;
	           	 System.out.println(x+": "+dayname+"\n");
	           	 System.out.println(x+": "+opentime+"\n");
	           	 System.out.println(x+": "+closetime+"\n");
	 
	           	 PreparedStatement p1;

	           	 if(!panelDays.containsKey(dayname)){
	           		 /*Insert into AUX_CAT_SUBCAT_ATTR_LOC_DAY table if not already inserted earlier*/
	           		 StringBuilder auxsql= new StringBuilder();
	           		 auxsql.append("INSERT INTO AUX_CAT_SUBCAT_ATTR_LOC_DAY (business_id, category, subcategory, att_value, loc, day) VALUES(?, ?, ?, ?, ?, ?)");
	           		 p1 = connection.prepareStatement(auxsql.toString());
	           		 p1.setString(1, "NA");
	           		 p1.setString(2, "NA");
	           		 p1.setString(3, "NA");
	           		 p1.setString(4, "NA");
	           		 p1.setString(5, "NA");
	           		 p1.setString(6, dayname);
	           		 p1.executeUpdate();
	           		 p1.close();
	           	 }

	           	 if(!panelFromtimes.containsKey(opentime)){
	           		 /*Insert into AUX_CAT_SUBCAT_ATTR_LOC_DAY_FROM table if not already inserted earlier*/
	           		 StringBuilder aux1sql= new StringBuilder();
	           		 aux1sql.append("INSERT INTO AUX_MC_SC_AT_LOC_DAY_FROM (business_id, category, subcategory, att_value, loc, day, open) VALUES(?, ?, ?, ?, ?, ?, ?)");
	           		 p1 = connection.prepareStatement(aux1sql.toString());
	           		 p1.setString(1, "NA");
	           		 p1.setString(2, "NA");
	           		 p1.setString(3, "NA");
	           		 p1.setString(4, "NA");
	           		 p1.setString(5, "NA");
	           		 p1.setString(6, "NA");
	           		 p1.setString(7, opentime);
	           		 p1.executeUpdate();
	           		 p1.close();
	           	 }

	           	 if(!panelTotimes.containsKey(closetime)){
	           		 /*Insert into AUX_CAT_SUBCAT_ATTR_LOC_DAY_FROM_TO table if not already inserted earlier*/
	           		 StringBuilder aux2sql= new StringBuilder();
	           		 aux2sql.append("INSERT INTO AUX_MC_SC_AT_LOC_DAY_FROM_TO (business_id, category, subcategory, att_value, loc, day, open, close) VALUES(?, ?, ?, ?, ?, ?, ?, ?)");
	           		 p1 = connection.prepareStatement(aux2sql.toString());
	           		 p1.setString(1, "NA");
	           		 p1.setString(2, "NA");
	           		 p1.setString(3, "NA");
	           		 p1.setString(4, "NA");
	           		 p1.setString(5, "NA");
	           		 p1.setString(6, "NA");
	           		 p1.setString(7, "NA");
	           		 p1.setString(8, closetime);
	           		 p1.executeUpdate();
	           		 p1.close();
	           	 }

	           	 /*Insert to Days combobox*/
	           	 if(panelDays.containsKey(dayname)){
			    	System.out.println("Duplicate Day: "+dayname);
			    	int count= panelDays.get(dayname);
			    	count= count+1;
			    	panelDays.replace(dayname, count);
	           	 }
      			 else{
      				 panelDays.put(dayname, 1);
      				 day.addItem(dayname);
      			 }
	           	 
	           	 /*Insert to From combobox*/
	           	 if(panelFromtimes.containsKey(opentime)){
	           		 System.out.println("Duplicate Fromtime: "+opentime);
	           		 int count= panelFromtimes.get(opentime);
	           		 count= count+1;
	           		 panelFromtimes.replace(opentime, count);
	           	 }
	           	 else{
	           		 panelFromtimes.put(opentime, 1);
	           		 fromtime.addItem(opentime);
	           	 }

	           	 /*Insert to To combobox*/
	           	 if(panelTotimes.containsKey(closetime)){
	           		 System.out.println("Duplicate Totime: "+closetime);
	           		 int count= panelTotimes.get(closetime);
	           		 count= count+1;
	           		 panelTotimes.replace(closetime, count);
	           	 }
	           	 else{
	           		 panelTotimes.put(closetime, 1);
	           		 totime.addItem(closetime);
	           	 }
		    }
		    preparedStatement.close();
		    if(orselected)
		    	System.out.println("Count of Hours selected by ORing SCs = "+x+"\n");
		    else
		    	System.out.println("Count of Hours selected by ANDing SCs = "+x+"\n");

//			 /*Drop index on business table*/
//			 String inddropsql = "drop index business_result_index";
//			 p4 = connection.prepareStatement(inddropsql);
//			 p4.executeUpdate();
//			 p4.close();

			 connection.close();
			 /*enable back combo listeners*/
			 enablecombos();
			 /*set AND/OR to default*/
			 defaultandor();
			 totaltime = System.nanoTime() - starttime;
			 System.out.println("SC: Time taken - "+(totaltime/1.0E09)+" seconds");
        }
        catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    }
    
    private void update_panels_for_attr(){
        try {
			 if(selectAttributes.isEmpty()){
				 update_panels_for_sc();
				 return;
			 }

			 starttime = System.nanoTime();
        	Connection connection = null;
        	Class.forName("oracle.jdbc.driver.OracleDriver");
        	connection = DriverManager.getConnection(
        			"jdbc:oracle:thin:@localhost:1521:hmannuru", "scott", "tiger");

        	/*Disable comboboxes listeners*/
        	disablecombos();

        	/*Reset panels and AUX tables*/
        	reset_loc_panel(connection);
        	reset_day_panel(connection);
        	reset_fromtime_panel(connection);
        	reset_totime_panel(connection);
        	reset_business_search_table(connection);

        	Iterator<String> atti=selectAttributes.keySet().iterator();
        	String bussql = "";
        	int z=0;
        	if(orselected) {
        		while(atti.hasNext()){
        			if(z == 0)						 
        				bussql = bussql+"SELECT a.business_id from attributes a, AUX_BUS_USEDBY_ATTR aux where a.business_id=aux.business_id and a.att_value='"+atti.next()+"'";
        			else
        				bussql = bussql+"UNION SELECT a.business_id from attributes a, AUX_BUS_USEDBY_ATTR aux where a.business_id=aux.business_id and a.att_value='"+atti.next()+"'";						 
        			z++;				 
        		}
        	}
        	else{
        		while(atti.hasNext()){
        			if(z == 0)						 
        				bussql = bussql+"SELECT a.business_id from attributes a, AUX_BUS_USEDBY_ATTR aux where a.business_id=aux.business_id and a.att_value='"+atti.next()+"'";
        			else
        				bussql = bussql+"INTERSECT SELECT a.business_id from attributes a, AUX_BUS_USEDBY_ATTR aux where a.business_id=aux.business_id and a.att_value='"+atti.next()+"'";						 
        			z++;				 
        		}
        	}

        	/*First find and fill business table for selected SCs*/
        	ResultSet busrs;
        	PreparedStatement p4 = connection.prepareStatement(bussql);
        	busrs= p4.executeQuery();
        	while (busrs.next()) {
        		String bid = busrs.getString(busrs.findColumn("business_id"));
        		businessres.add(bid);
        		StringBuilder resultsql = new StringBuilder();
        		resultsql.append("INSERT INTO AUX_BUSINESS_RESULT (business_id) VALUES(?)");
        		PreparedStatement p3 = connection.prepareStatement(resultsql.toString());
        		p3.setString(1, bid);
        		p3.executeUpdate();
        		p3.close();

        		String tempsql = "INSERT INTO AUX_BUS_USEDBY_LOC (business_id) VALUES(?)";
				 p3 = connection.prepareStatement(tempsql);
				 p3.setString(1, bid);
				 p3.executeUpdate();
				 p3.close();

				 tempsql = "INSERT INTO AUX_BUS_USEDBY_DAY (business_id) VALUES(?)";
				 p3 = connection.prepareStatement(tempsql);
				 p3.setString(1, bid);
				 p3.executeUpdate();
				 p3.close();

				 tempsql = "INSERT INTO AUX_BUS_USEDBY_FROM (business_id) VALUES(?)";
				 p3 = connection.prepareStatement(tempsql);
				 p3.setString(1, bid);
				 p3.executeUpdate();
				 p3.close();

				 tempsql = "INSERT INTO AUX_BUS_USEDBY_TO (business_id) VALUES(?)";
				 p3 = connection.prepareStatement(tempsql);
				 p3.setString(1, bid);
				 p3.executeUpdate();
				 p3.close();
        	}
        	p4.close();

//			 /*Create index on business table*/
//			 String indsql = "create index business_result_index on AUX_BUSINESS_RESULT(business_id)";
//			 p4 = connection.prepareStatement(indsql);
//			 p4.executeUpdate();
//			 p4.close();
			 
			 /* Update Loc panel based on filtered Businesses for selected SCs*/
			 StringBuilder locsql= new StringBuilder();
			 ResultSet locrs;
			 locsql.append("SELECT DISTINCT b.city, b.state").append("\n")
	        	  .append("FROM business b, AUX_BUSINESS_RESULT aux").append("\n")
	        	  .append("WHERE b.business_id=aux.business_id").append("\n")
	        	  .append("ORDER BY b.city");

	        PreparedStatement preparedStatement = connection.prepareStatement(locsql.toString());
	        locrs= preparedStatement.executeQuery();
	        
		    int x=0;
	       	//Iterator to go over the result set and populate them into the Loc panel as combobox
		    while (locrs.next()) {
	           	 String city = locrs.getString(locrs.findColumn("city"));
	           	 String state = locrs.getString(locrs.findColumn("state"));
	           	 String locname = city+","+state;
	           	 x++;
	           	 System.out.println(x+": "+locname+"\n");
	 
	           	 /*Insert into AUX_CAT_SUBCAT_ATTR_LOC table*/
	           	 StringBuilder auxsql= new StringBuilder();
	           	 auxsql.append("INSERT INTO AUX_CAT_SUBCAT_ATTR_LOC (business_id, category, subcategory, att_value, loc) VALUES(?, ?, ?, ?, ?)");
	           	 PreparedStatement p1 = connection.prepareStatement(auxsql.toString());
	           	 p1.setString(1, "NA");
	           	 p1.setString(2, "NA");
	           	 p1.setString(3, "NA");
	           	 p1.setString(4, "NA");
	           	 p1.setString(5, locname);
	           	 p1.executeUpdate();
	           	 p1.close();
	           	 
	           	 if(panelLocs.containsKey(locname)){
			    		System.out.println("BAD "+locname+", already exists");
	           	 }
	           	 else{
	           		 panelLocs.put(locname, 1);
	           		 loc.addItem(locname);
	           	 }
		    }
		    preparedStatement.close();
		    if(orselected)
		    	System.out.println("Count of Locs selected by ORing Attrs = "+x+"\n");
		    else
		    	System.out.println("Count of Locs selected by ANDing Attrs = "+x+"\n");
	   	
		    
			 /* Update Day panel based on filtered Businesses for selected Attrs*/
			 StringBuilder daysql= new StringBuilder();
			 ResultSet dayrs;
			 daysql.append("SELECT DISTINCT h.day, h.open, h.close").append("\n")
	        	  .append("FROM hours h, AUX_BUSINESS_RESULT aux").append("\n")
	        	  .append("WHERE h.business_id=aux.business_id").append("\n")
	        	  .append("ORDER BY h.day");
	        preparedStatement = connection.prepareStatement(daysql.toString());
	        dayrs= preparedStatement.executeQuery();
	        
		    x=0;
	       	//Iterator to go over the result set and populate them into the Loc panel as combobox
		    while (dayrs.next()) {
	           	 String dayname = dayrs.getString(dayrs.findColumn("day"));
	           	 String opentime = dayrs.getString(dayrs.findColumn("open"));
	           	 String closetime = dayrs.getString(dayrs.findColumn("close"));
	           	 x++;
	           	 System.out.println(x+": "+dayname+"\n");
	           	 System.out.println(x+": "+opentime+"\n");
	           	 System.out.println(x+": "+closetime+"\n");
	 
	           	 PreparedStatement p1;

	           	 if(!panelDays.containsKey(dayname)){
	           		 /*Insert into AUX_CAT_SUBCAT_ATTR_LOC_DAY table if not already inserted earlier*/
	           		 StringBuilder auxsql= new StringBuilder();
	           		 auxsql.append("INSERT INTO AUX_CAT_SUBCAT_ATTR_LOC_DAY (business_id, category, subcategory, att_value, loc, day) VALUES(?, ?, ?, ?, ?, ?)");
	           		 p1 = connection.prepareStatement(auxsql.toString());
	           		 p1.setString(1, "NA");
	           		 p1.setString(2, "NA");
	           		 p1.setString(3, "NA");
	           		 p1.setString(4, "NA");
	           		 p1.setString(5, "NA");
	           		 p1.setString(6, dayname);
	           		 p1.executeUpdate();
	           		 p1.close();
	           	 }

	           	 if(!panelFromtimes.containsKey(opentime)){
	           		 /*Insert into AUX_CAT_SUBCAT_ATTR_LOC_DAY_FROM table if not already inserted earlier*/
	           		 StringBuilder aux1sql= new StringBuilder();
	           		 aux1sql.append("INSERT INTO AUX_MC_SC_AT_LOC_DAY_FROM (business_id, category, subcategory, att_value, loc, day, open) VALUES(?, ?, ?, ?, ?, ?, ?)");
	           		 p1 = connection.prepareStatement(aux1sql.toString());
	           		 p1.setString(1, "NA");
	           		 p1.setString(2, "NA");
	           		 p1.setString(3, "NA");
	           		 p1.setString(4, "NA");
	           		 p1.setString(5, "NA");
	           		 p1.setString(6, "NA");
	           		 p1.setString(7, opentime);
	           		 p1.executeUpdate();
	           		 p1.close();
	           	 }

	           	 if(!panelTotimes.containsKey(closetime)){
	           		 /*Insert into AUX_CAT_SUBCAT_ATTR_LOC_DAY_FROM_TO table if not already inserted earlier*/
	           		 StringBuilder aux2sql= new StringBuilder();
	           		 aux2sql.append("INSERT INTO AUX_MC_SC_AT_LOC_DAY_FROM_TO (business_id, category, subcategory, att_value, loc, day, open, close) VALUES(?, ?, ?, ?, ?, ?, ?, ?)");
	           		 p1 = connection.prepareStatement(aux2sql.toString());
	           		 p1.setString(1, "NA");
	           		 p1.setString(2, "NA");
	           		 p1.setString(3, "NA");
	           		 p1.setString(4, "NA");
	           		 p1.setString(5, "NA");
	           		 p1.setString(6, "NA");
	           		 p1.setString(7, "NA");
	           		 p1.setString(8, closetime);
	           		 p1.executeUpdate();
	           		 p1.close();
	           	 }

	           	 /*Insert to Days combobox*/
	           	 if(panelDays.containsKey(dayname)){
			    	System.out.println("Duplicate Day: "+dayname);
			    	int count= panelDays.get(dayname);
			    	count= count+1;
			    	panelDays.replace(dayname, count);
	           	 }
     			 else{
     				 panelDays.put(dayname, 1);
     				 day.addItem(dayname);
     			 }
	           	 
	           	 /*Insert to From combobox*/
	           	 if(panelFromtimes.containsKey(opentime)){
	           		 System.out.println("Duplicate Fromtime: "+opentime);
	           		 int count= panelFromtimes.get(opentime);
	           		 count= count+1;
	           		 panelFromtimes.replace(opentime, count);
	           	 }
	           	 else{
	           		 panelFromtimes.put(opentime, 1);
	           		 fromtime.addItem(opentime);
	           	 }

	           	 /*Insert to To combobox*/
	           	 if(panelTotimes.containsKey(closetime)){
	           		 System.out.println("Duplicate Totime: "+closetime);
	           		 int count= panelTotimes.get(closetime);
	           		 count= count+1;
	           		 panelTotimes.replace(closetime, count);
	           	 }
	           	 else{
	           		 panelTotimes.put(closetime, 1);
	           		 totime.addItem(closetime);
	           	 }
		    }
		    preparedStatement.close();
		    if(orselected)
		    	System.out.println("Count of Hours selected by ORing Attrs = "+x+"\n");
		    else
		    	System.out.println("Count of Hours selected by ANDing Attrs = "+x+"\n");

//			 /*Drop index on business table*/
//			 String inddropsql = "drop index business_result_index";
//			 p4 = connection.prepareStatement(inddropsql);
//			 p4.executeUpdate();
//			 p4.close();

			 connection.close();
			 /*enable back combo listeners*/
			 enablecombos();
			 /*set AND/OR to default*/
			 defaultandor();
			 totaltime = System.nanoTime() - starttime;
			 System.out.println("Attr: Time taken - "+(totaltime/1.0E09)+" seconds");
       }
        catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}    	
    }
    
    private void update_panels_for_loc(){
        try {
			 starttime = System.nanoTime();
	        	Connection connection = null;
	        	Class.forName("oracle.jdbc.driver.OracleDriver");
	        	connection = DriverManager.getConnection(
	        			"jdbc:oracle:thin:@localhost:1521:hmannuru", "scott", "tiger");

	        	/*Disable comboboxes listeners*/
	        	disablecombos();

	        	/*Reset panels and AUX tables*/
	        	reset_day_panel(connection);
	        	reset_fromtime_panel(connection);
	        	reset_totime_panel(connection);
	        	reset_business_search_table(connection);

	        	String locname = selectLoc;
	        	String[] citystate = locname.split(",");
	        	String cityname = citystate[0];
	        	String statename = citystate[1];
	        	String bussql = "SELECT distinct b.business_id from business b, AUX_BUS_USEDBY_LOC aux where b.business_id=aux.business_id and b.city='"+cityname+"' and b.state='"+statename+"'";

	        	/*First find and fill business table for selected SCs*/
	        	ResultSet busrs;
	        	PreparedStatement p4 = connection.prepareStatement(bussql);
	        	busrs= p4.executeQuery();
	        	while (busrs.next()) {
	        		String bid = busrs.getString(busrs.findColumn("business_id"));
	        		businessres.add(bid);
	        		StringBuilder resultsql = new StringBuilder();
	        		resultsql.append("INSERT INTO AUX_BUSINESS_RESULT (business_id) VALUES(?)");
	        		PreparedStatement p3 = connection.prepareStatement(resultsql.toString());
	        		p3.setString(1, bid);
	        		p3.executeUpdate();
	        		p3.close();

					 String tempsql = "INSERT INTO AUX_BUS_USEDBY_DAY (business_id) VALUES(?)";
					 p3 = connection.prepareStatement(tempsql);
					 p3.setString(1, bid);
					 p3.executeUpdate();
					 p3.close();

					 tempsql = "INSERT INTO AUX_BUS_USEDBY_FROM (business_id) VALUES(?)";
					 p3 = connection.prepareStatement(tempsql);
					 p3.setString(1, bid);
					 p3.executeUpdate();
					 p3.close();

					 tempsql = "INSERT INTO AUX_BUS_USEDBY_TO (business_id) VALUES(?)";
					 p3 = connection.prepareStatement(tempsql);
					 p3.setString(1, bid);
					 p3.executeUpdate();
					 p3.close();
	        	}
	        	p4.close();

//				 /*Create index on business table*/
//				 String indsql = "create index business_result_index on AUX_BUSINESS_RESULT(business_id)";
//				 p4 = connection.prepareStatement(indsql);
//				 p4.executeUpdate();
//				 p4.close();
				 
				 /* Update Day panel based on filtered Businesses for selected Attrs*/
				 StringBuilder daysql= new StringBuilder();
				 ResultSet dayrs;
				 daysql.append("SELECT DISTINCT h.day, h.open, h.close").append("\n")
		        	  .append("FROM hours h, AUX_BUSINESS_RESULT aux").append("\n")
		        	  .append("WHERE h.business_id=aux.business_id").append("\n")
		        	  .append("ORDER BY h.day");
		        PreparedStatement preparedStatement = connection.prepareStatement(daysql.toString());
		        dayrs= preparedStatement.executeQuery();
		        
			    int x=0;
		       	//Iterator to go over the result set and populate them into the Loc panel as combobox
			    while (dayrs.next()) {
		           	 String dayname = dayrs.getString(dayrs.findColumn("day"));
		           	 String opentime = dayrs.getString(dayrs.findColumn("open"));
		           	 String closetime = dayrs.getString(dayrs.findColumn("close"));
		           	 x++;
		           	 System.out.println(x+": "+dayname+"\n");
		           	 System.out.println(x+": "+opentime+"\n");
		           	 System.out.println(x+": "+closetime+"\n");
		 
		           	 PreparedStatement p1;

		           	 if(!panelDays.containsKey(dayname)){
		           		 /*Insert into AUX_CAT_SUBCAT_ATTR_LOC_DAY table if not already inserted earlier*/
		           		 StringBuilder auxsql= new StringBuilder();
		           		 auxsql.append("INSERT INTO AUX_CAT_SUBCAT_ATTR_LOC_DAY (business_id, category, subcategory, att_value, loc, day) VALUES(?, ?, ?, ?, ?, ?)");
		           		 p1 = connection.prepareStatement(auxsql.toString());
		           		 p1.setString(1, "NA");
		           		 p1.setString(2, "NA");
		           		 p1.setString(3, "NA");
		           		 p1.setString(4, "NA");
		           		 p1.setString(5, "NA");
		           		 p1.setString(6, dayname);
		           		 p1.executeUpdate();
		           		 p1.close();
		           	 }

		           	 if(!panelFromtimes.containsKey(opentime)){
		           		 /*Insert into AUX_CAT_SUBCAT_ATTR_LOC_DAY_FROM table if not already inserted earlier*/
		           		 StringBuilder aux1sql= new StringBuilder();
		           		 aux1sql.append("INSERT INTO AUX_MC_SC_AT_LOC_DAY_FROM (business_id, category, subcategory, att_value, loc, day, open) VALUES(?, ?, ?, ?, ?, ?, ?)");
		           		 p1 = connection.prepareStatement(aux1sql.toString());
		           		 p1.setString(1, "NA");
		           		 p1.setString(2, "NA");
		           		 p1.setString(3, "NA");
		           		 p1.setString(4, "NA");
		           		 p1.setString(5, "NA");
		           		 p1.setString(6, "NA");
		           		 p1.setString(7, opentime);
		           		 p1.executeUpdate();
		           		 p1.close();
		           	 }

		           	 if(!panelTotimes.containsKey(closetime)){
		           		 /*Insert into AUX_CAT_SUBCAT_ATTR_LOC_DAY_FROM_TO table if not already inserted earlier*/
		           		 StringBuilder aux2sql= new StringBuilder();
		           		 aux2sql.append("INSERT INTO AUX_MC_SC_AT_LOC_DAY_FROM_TO (business_id, category, subcategory, att_value, loc, day, open, close) VALUES(?, ?, ?, ?, ?, ?, ?, ?)");
		           		 p1 = connection.prepareStatement(aux2sql.toString());
		           		 p1.setString(1, "NA");
		           		 p1.setString(2, "NA");
		           		 p1.setString(3, "NA");
		           		 p1.setString(4, "NA");
		           		 p1.setString(5, "NA");
		           		 p1.setString(6, "NA");
		           		 p1.setString(7, "NA");
		           		 p1.setString(8, closetime);
		           		 p1.executeUpdate();
		           		 p1.close();
		           	 }

		           	 /*Insert to Days combobox*/
		           	 if(panelDays.containsKey(dayname)){
				    	System.out.println("Duplicate Day: "+dayname);
				    	int count= panelDays.get(dayname);
				    	count= count+1;
				    	panelDays.replace(dayname, count);
		           	 }
	     			 else{
	     				 panelDays.put(dayname, 1);
	     				 day.addItem(dayname);
	     			 }
		           	 
		           	 /*Insert to From combobox*/
		           	 if(panelFromtimes.containsKey(opentime)){
		           		 System.out.println("Duplicate Fromtime: "+opentime);
		           		 int count= panelFromtimes.get(opentime);
		           		 count= count+1;
		           		 panelFromtimes.replace(opentime, count);
		           	 }
		           	 else{
		           		 panelFromtimes.put(opentime, 1);
		           		 fromtime.addItem(opentime);
		           	 }

		           	 /*Insert to To combobox*/
		           	 if(panelTotimes.containsKey(closetime)){
		           		 System.out.println("Duplicate Totime: "+closetime);
		           		 int count= panelTotimes.get(closetime);
		           		 count= count+1;
		           		 panelTotimes.replace(closetime, count);
		           	 }
		           	 else{
		           		 panelTotimes.put(closetime, 1);
		           		 totime.addItem(closetime);
		           	 }
			    }
			    preparedStatement.close();
			    if(orselected)
			    	System.out.println("Count of Hours selected by ORing Attrs = "+x+"\n");
			    else
			    	System.out.println("Count of Hours selected by ANDing Attrs = "+x+"\n");

//				 /*Drop index on business table*/
//				 String inddropsql = "drop index business_result_index";
//				 p4 = connection.prepareStatement(inddropsql);
//				 p4.executeUpdate();
//				 p4.close();

			 connection.close();
			 /*enable back combo listeners*/
			 enablecombos();
			 totaltime = System.nanoTime() - starttime;
			 System.out.println("Loc: Time taken - "+(totaltime/1.0E09)+" seconds");
        }
        catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}    	
    }

    private void update_panels_for_day(){
        try {
			 starttime = System.nanoTime();
	        	Connection connection = null;
	        	Class.forName("oracle.jdbc.driver.OracleDriver");
	        	connection = DriverManager.getConnection(
	        			"jdbc:oracle:thin:@localhost:1521:hmannuru", "scott", "tiger");

	        	/*Disable comboboxes listeners*/
	        	disablecombos();

	        	/*Reset panels and AUX tables*/
	        	reset_fromtime_panel(connection);
	        	reset_totime_panel(connection);
	        	reset_business_search_table(connection);

	        	String bussql = "SELECT distinct h.business_id from hours h, AUX_BUS_USEDBY_DAY aux where h.business_id=aux.business_id and h.day='"+selectDay+"'";

	        	/*First find and fill business table for selected Day*/
	        	ResultSet busrs;
	        	PreparedStatement p4 = connection.prepareStatement(bussql);
	        	busrs= p4.executeQuery();
	        	while (busrs.next()) {
	        		String bid = busrs.getString(busrs.findColumn("business_id"));
	        		businessres.add(bid);
	        		StringBuilder resultsql = new StringBuilder();
	        		resultsql.append("INSERT INTO AUX_BUSINESS_RESULT (business_id) VALUES(?)");
	        		PreparedStatement p3 = connection.prepareStatement(resultsql.toString());
	        		p3.setString(1, bid);
	        		p3.executeUpdate();
	        		p3.close();

					 String tempsql = "INSERT INTO AUX_BUS_USEDBY_FROM (business_id) VALUES(?)";
					 p3 = connection.prepareStatement(tempsql);
					 p3.setString(1, bid);
					 p3.executeUpdate();
					 p3.close();

					 tempsql = "INSERT INTO AUX_BUS_USEDBY_TO (business_id) VALUES(?)";
					 p3 = connection.prepareStatement(tempsql);
					 p3.setString(1, bid);
					 p3.executeUpdate();
					 p3.close();
	        	}
	        	p4.close();

//				 /*Create index on business table*/
//				 String indsql = "create index business_result_index on AUX_BUSINESS_RESULT(business_id)";
//				 p4 = connection.prepareStatement(indsql);
//				 p4.executeUpdate();
//				 p4.close();
				 
				 
				 /* Update From/To panel based on filtered Businesses for selected Days*/
				 StringBuilder fromsql= new StringBuilder();
				 ResultSet fromrs;
				 fromsql.append("SELECT DISTINCT h.day, h.open, h.close").append("\n")
		        	  .append("FROM hours h, AUX_BUSINESS_RESULT aux").append("\n")
		        	  .append("WHERE h.business_id=aux.business_id").append("\n")
		        	  .append("ORDER BY h.day");
		        PreparedStatement preparedStatement = connection.prepareStatement(fromsql.toString());
		        fromrs= preparedStatement.executeQuery();
		        
			    int x=0;
		       	//Iterator to go over the result set and populate them into the Loc panel as combobox
			    while (fromrs.next()) {
		           	 String dayname = fromrs.getString(fromrs.findColumn("day"));
		           	 System.out.println("Filling from/to for Day: "+dayname+", selectedday:"+selectDay);
		           	 if(dayname.equals(selectDay)){
		           		 String opentime = fromrs.getString(fromrs.findColumn("open"));
		           		 String closetime = fromrs.getString(fromrs.findColumn("close"));
		           		 x++;
		           		 System.out.println(x+": "+opentime+"\n");
		           		 System.out.println(x+": "+closetime+"\n");

		           		 /*Insert to From combobox*/
		           		 if(panelFromtimes.containsKey(opentime)){
		           			 System.out.println("Duplicate Fromtime: "+opentime);
		           			 int count= panelFromtimes.get(opentime);
		           			 count= count+1;
		           			 panelFromtimes.replace(opentime, count);
		           		 }
		           		 else{
		           			 panelFromtimes.put(opentime, 1);
		           			 fromtime.addItem(opentime);
		           		 }

		           		 /*Insert to To combobox*/
		           		 if(panelTotimes.containsKey(closetime)){
		           			 System.out.println("Duplicate Totime: "+closetime);
		           			 int count= panelTotimes.get(closetime);
		           			 count= count+1;
		           			 panelTotimes.replace(closetime, count);
		           		 }
		           		 else{
		           			 panelTotimes.put(closetime, 1);
		           			 totime.addItem(closetime);
		           		 }
		           	 }
			    }
			    preparedStatement.close();
			    if(orselected)
			    	System.out.println("Count of From/Totimes selected by ORing Attrs = "+x+"\n");
			    else
			    	System.out.println("Count of From/Totimes selected by ANDing Attrs = "+x+"\n");

//				 /*Drop index on business table*/
//				 String inddropsql = "drop index business_result_index";
//				 p4 = connection.prepareStatement(inddropsql);
//				 p4.executeUpdate();
//				 p4.close();

			    connection.close();
			 /*enable back combo listeners*/
			 enablecombos();
			 totaltime = System.nanoTime() - starttime;
			 System.out.println("Day: Time taken - "+(totaltime/1.0E09)+" seconds");
        }
        catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}    	
    }

    private void update_panels_for_fromtime() throws ParseException{
        try {
			 starttime = System.nanoTime();
	        	Connection connection = null;
	        	Class.forName("oracle.jdbc.driver.OracleDriver");
	        	connection = DriverManager.getConnection(
	        			"jdbc:oracle:thin:@localhost:1521:hmannuru", "scott", "tiger");

	        	/*Disable comboboxes listeners*/
	        	disablecombos();

	        	/*Reset panels and AUX tables*/
	        	reset_totime_panel(connection);
	        	reset_business_search_table(connection);

	        	String bussql = "";
	        	if(selectDay.isEmpty())
	        		bussql = "SELECT distinct h.business_id, h.open from hours h, AUX_BUS_USEDBY_FROM aux where h.business_id=aux.business_id";
//	        		bussql = "SELECT distinct h.business_id from hours h, AUX_BUS_USEDBY_FROM aux where h.business_id=aux.business_id and h.open='"+selectFromtime+"'";
	        	else
	        		bussql = "SELECT distinct h.business_id, h.open from hours h, AUX_BUS_USEDBY_FROM aux where h.business_id=aux.business_id and h.day='"+selectDay+"'";
//	        		bussql = "SELECT distinct h.business_id from hours h, AUX_BUS_USEDBY_FROM aux where h.business_id=aux.business_id and h.open='"+selectFromtime+"' and h.day='"+selectDay+"'";
	        	
	        	/*First find and fill business table for selected Day*/
	        	ResultSet busrs;
	        	PreparedStatement p4 = connection.prepareStatement(bussql);
	        	busrs= p4.executeQuery();
	        	while (busrs.next()) {
	        		String bid = busrs.getString(busrs.findColumn("business_id"));
	        		String opentime = busrs.getString(busrs.findColumn("open"))+":00";
	        		DateFormat sdf = new SimpleDateFormat("hh:mm:ss");
	        		Date date = (Date) sdf.parse(opentime);
	        		Date seldate = (Date) sdf.parse(selectFromtime+":00");
	        		businessres.add(bid);
	        		
	        		if(date.after(seldate) || date.equals(seldate)) {
	        		StringBuilder resultsql = new StringBuilder();
	        		resultsql.append("INSERT INTO AUX_BUSINESS_RESULT (business_id) VALUES(?)");
	        		PreparedStatement p3 = connection.prepareStatement(resultsql.toString());
	        		p3.setString(1, bid);
	        		p3.executeUpdate();
	        		p3.close();

					 String tempsql = "INSERT INTO AUX_BUS_USEDBY_TO (business_id) VALUES(?)";
					 p3 = connection.prepareStatement(tempsql);
					 p3.setString(1, bid);
					 p3.executeUpdate();
					 p3.close();
	        		}
	        	}
	        	p4.close();

//				 /*Create index on business table*/
//				 String indsql = "create index business_result_index on AUX_BUSINESS_RESULT(business_id)";
//				 p4 = connection.prepareStatement(indsql);
//				 p4.executeUpdate();
//				 p4.close();
				 
				 
				 /* Update From/To panel based on filtered Businesses for selected Days*/
				 StringBuilder fromsql= new StringBuilder();
				 ResultSet fromrs;
				 fromsql.append("SELECT DISTINCT h.day, h.open, h.close").append("\n")
		        	  .append("FROM hours h, AUX_BUSINESS_RESULT aux").append("\n")
		        	  .append("WHERE h.business_id=aux.business_id").append("\n")
		        	  .append("ORDER BY h.day");
		        PreparedStatement preparedStatement = connection.prepareStatement(fromsql.toString());
		        fromrs= preparedStatement.executeQuery();
		        
			    int x=0;
		       	//Iterator to go over the result set and populate them into the Loc panel as combobox
			    while (fromrs.next()) {
		           	 String dayname = fromrs.getString(fromrs.findColumn("day"));
		           	 String opentime = fromrs.getString(fromrs.findColumn("open"));
		        		String opendatetime = fromrs.getString(fromrs.findColumn("open"))+":00";
		        		DateFormat sdf = new SimpleDateFormat("hh:mm:ss");
		        		Date date = (Date) sdf.parse(opendatetime);
		        		Date seldate = (Date) sdf.parse(selectFromtime+":00");

		           	 boolean goahead = true;
		           	 if(!selectDay.isEmpty()){
		           		 if(!dayname.equals(selectDay))
		           			 goahead = false;
		           	 }
		           	 if(!selectFromtime.isEmpty()){
//		           		 if(!opentime.equals(selectFromtime))
//		           			 goahead = false;
		           		 if(!date.after(seldate) && !date.equals(seldate))
		           			 goahead=false;
		           	 }
		           	 
		           	 if(goahead){
		           		 String closetime = fromrs.getString(fromrs.findColumn("close"));
		           		 x++;
		           		 System.out.println(x+": "+closetime+"\n");

		           		 /*Insert to From combobox*/
		           		 if(panelFromtimes.containsKey(opentime)){
		           			 System.out.println("Duplicate Fromtime: "+opentime);
		           			 int count= panelFromtimes.get(opentime);
		           			 count= count+1;
		           			 panelFromtimes.replace(opentime, count);
		           		 }
		           		 else{
		           			 panelFromtimes.put(opentime, 1);
		           			 fromtime.addItem(opentime);
		           		 }

		           		 /*Insert to To combobox*/
		           		 if(panelTotimes.containsKey(closetime)){
		           			 System.out.println("Duplicate Totime: "+closetime);
		           			 int count= panelTotimes.get(closetime);
		           			 count= count+1;
		           			 panelTotimes.replace(closetime, count);
		           		 }
		           		 else{
		           			 panelTotimes.put(closetime, 1);
		           			 totime.addItem(closetime);
		           		 }
		           	 }
			    }
			    preparedStatement.close();
			    if(orselected)
			    	System.out.println("Count of Totimes selected by ORing Attrs = "+x+"\n");
			    else
			    	System.out.println("Count of Totimes selected by ANDing Attrs = "+x+"\n");

//				 /*Drop index on business table*/
//				 String inddropsql = "drop index business_result_index";
//				 p4 = connection.prepareStatement(inddropsql);
//				 p4.executeUpdate();
//				 p4.close();

			    connection.close();
			 /*enable back combo listeners*/
			 enablecombos();
			 totaltime = System.nanoTime() - starttime;
			 System.out.println("Fromtime: Time taken - "+(totaltime/1.0E09)+" seconds");
        }
        catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}    	
    }

    private void update_panels_for_totime() throws ParseException{
        try {
			 starttime = System.nanoTime();
	        	Connection connection = null;
	        	Class.forName("oracle.jdbc.driver.OracleDriver");
	        	connection = DriverManager.getConnection(
	        			"jdbc:oracle:thin:@localhost:1521:hmannuru", "scott", "tiger");

	        	/*Disable comboboxes listeners*/
	        	disablecombos();

	        	/*Reset panels and AUX tables*/
	        	reset_business_search_table(connection);

	           	 String bussql = "";
//	           	 if(!selectDay.isEmpty() && !selectFromtime.isEmpty()){
//	 	        	bussql = "SELECT distinct h.business_id from hours h, AUX_BUS_USEDBY_TO aux where h.business_id=aux.business_id and h.close='"+selectTotime+"' and h.day='"+selectDay+"' and h.open='"+selectFromtime+"'";
//	           	 }
//	           	 else if(selectDay.isEmpty() && !selectFromtime.isEmpty()){
//		 	        bussql = "SELECT distinct h.business_id from hours h, AUX_BUS_USEDBY_TO aux where h.business_id=aux.business_id and h.close='"+selectTotime+"' and h.open='"+selectFromtime+"'";	           		 
//	           	 }
//	           	 else if(!selectDay.isEmpty() && selectFromtime.isEmpty()){
//		 	        bussql = "SELECT distinct h.business_id from hours h, AUX_BUS_USEDBY_TO aux where h.business_id=aux.business_id and h.close='"+selectTotime+"' and h.day='"+selectDay+"'";	           		 
//	           	 }
//	           	 else{
//		 	        bussql = "SELECT distinct h.business_id from hours h, AUX_BUS_USEDBY_TO aux where h.business_id=aux.business_id and h.close='"+selectTotime+"'"; 	           		 
//	           	 }
	           	
	           	bussql = "SELECT distinct h.business_id, h.day, h.open, h.close from hours h, AUX_BUS_USEDBY_TO aux where h.business_id=aux.business_id"; 
	           	
	           	/*First find and fill business table for selected Day*/
	        	ResultSet busrs;
	        	PreparedStatement p4 = connection.prepareStatement(bussql);
	        	busrs= p4.executeQuery();
	        	while (busrs.next()) {
	        		String bid = busrs.getString(busrs.findColumn("business_id"));
	        		String dayname = busrs.getString(busrs.findColumn("day"));
	        		String opentime = busrs.getString(busrs.findColumn("open"))+":00";
	        		String closetime = busrs.getString(busrs.findColumn("close"))+":00";
	        		DateFormat sdf = new SimpleDateFormat("hh:mm:ss");
	        		Date opendate = (Date) sdf.parse(opentime);
	        		Date selopendate = (Date) sdf.parse(selectFromtime+":00");
	        		Date closedate = (Date) sdf.parse(closetime);
	        		Date selclosedate = (Date) sdf.parse(selectTotime+":00");
	        		
		           	 boolean goahead = true;
		           	 if(!selectDay.isEmpty()){
		           		 if(!dayname.equals(selectDay))
		           			 goahead = false;
		           	 }
		           	 
		           	 System.out.println("1:"+goahead);
		           	 if(!selectFromtime.isEmpty()){
		           		 if(!opendate.after(selopendate) && !opendate.equals(selopendate))
		           			 goahead=false;
		           	 }
		           	 System.out.println("2:"+goahead);
		           	 
	        		if(closedate.after(selclosedate)){
	        			goahead = false;
	        		}
		           	 System.out.println("3:"+goahead);

	        		if(goahead){
	        		businessres.add(bid);
	        		StringBuilder resultsql = new StringBuilder();
	        		resultsql.append("INSERT INTO AUX_BUSINESS_RESULT (business_id) VALUES(?)");
	        		PreparedStatement p3 = connection.prepareStatement(resultsql.toString());
	        		p3.setString(1, bid);
	        		p3.executeUpdate();
	        		p3.close();
	        		}
	        	}
	        	p4.close();

//				 /*Create index on business table*/
//				 String indsql = "create index business_result_index on AUX_BUSINESS_RESULT(business_id)";
//				 p4 = connection.prepareStatement(indsql);
//				 p4.executeUpdate();
//				 p4.close();
				 
			 connection.close();
			 /*enable back combo listeners*/
			 enablecombos();
        }
        catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}    	
    }

    private class mcbActionListener implements ActionListener {
        public void actionPerformed( ActionEvent e ){
        	JCheckBox cb = (JCheckBox) e.getSource();
        	 String mainCategory = cb.getText();
        	 
			 if(cb.isSelected())
				 selectMainCategories.put(mainCategory, 1);
			 else
				 selectMainCategories.remove(mainCategory, 1);

        	 update_panels_for_mc();
        }
    }
    
    private class scActionListener implements ActionListener {
        public void actionPerformed( ActionEvent e ){
        	JCheckBox sc = (JCheckBox) e.getSource();
        	 String subCategory = sc.getText();
  
			 if(sc.isSelected())
				 selectSubCategories.put(subCategory, 1);
			 else
				 selectSubCategories.remove(subCategory, 1);

			update_panels_for_sc(); 
        }
    }

    private class attrActionListener implements ActionListener {
        public void actionPerformed( ActionEvent e ){
        	JCheckBox attr = (JCheckBox) e.getSource();
        	 String attrname = attr.getText();

        	 if(attr.isSelected())
				 selectAttributes.put(attrname, 1);
			 else
				 selectAttributes.remove(attrname, 1);

        	 update_panels_for_attr();
        }
    }

    private class locActionListener implements ActionListener {
        public void actionPerformed( ActionEvent e ){
        	if(handleloc){
	        	if(loc.getSelectedItem() != null) {
	        		selectLoc = loc.getSelectedItem().toString();
	        	 
	        		update_panels_for_loc();
	        	}
        	}
        }
    }

    private class dayActionListener implements ActionListener {
        public void actionPerformed( ActionEvent e ){
        	if(handleday){
	        	if(day.getSelectedItem() != null){
	        		selectDay = day.getSelectedItem().toString();
	        	 
	        		update_panels_for_day();
	        	}
        	}
        }
    }
    
    private class fromActionListener implements ActionListener {
        public void actionPerformed( ActionEvent e ){
        	if(handlefromtime){
	        	if(fromtime.getSelectedItem() != null){
	        		selectFromtime = fromtime.getSelectedItem().toString();
	        	 
	        		try {
						update_panels_for_fromtime();
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	        	}
        	}
        }
    }

    private class toActionListener implements ActionListener {
        public void actionPerformed( ActionEvent e ){
        	if(handletotime){
	        	if(totime.getSelectedItem() != null){
	        		selectTotime = totime.getSelectedItem().toString();
	        	 
	        		try {
						update_panels_for_totime();
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	        	}
        	}
        }
    }

    private class andorActionListener implements ActionListener {
        public void actionPerformed( ActionEvent e ){
        	JButton button = (JButton) e.getSource();
        	if(button.getText() == "AND"){
        		andselected = true;
        		orselected = false;
        	}
        	if(button.getText() == "OR"){
        		orselected = true;
        		andselected = false;
        	}
        }
    }

    private class searchActionListener implements ActionListener {
    	public void actionPerformed(ActionEvent e){
    		frame.getContentPane().remove(bustable);
            Vector<Object> columnNames = new Vector<Object>();
            Vector<Object> data = new Vector<Object>();
            Vector<Object> revcolumnNames = new Vector<Object>();
            Vector<Object> revdata = new Vector<Object>();
            
            try
            {
                //  Connect to an Access Database
            	Connection connection = null;
                Class.forName("oracle.jdbc.driver.OracleDriver");        
                connection = DriverManager.getConnection(
                        "jdbc:oracle:thin:@localhost:1521:hmannuru", "scott", "tiger");

                //  Read data from a table
                String sql = "Select b.name,b.city,b.state,b.stars,b.business_id,tempc.noofcheckin,tempr.noofreviews"
                		+ " from business b, AUX_BUSINESS_RESULT a,"
                												+"(select COUNT(c.count) AS noofcheckin, c.business_id"
                												+ " from checkin c"
                												+ " group by(c.business_id))tempc,"
                												+ "(select COUNT(r.review_id) AS noofreviews,r.business_id"
                												+ " from review r"
                												+ " group by(r.business_id))tempr"
                		+ " where b.business_id=a.business_id and tempc.business_id=b.business_id and tempr.business_id=b.business_id"
                		+" order by b.name";
                
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery( sql );
                ResultSetMetaData md = rs.getMetaData();
                int columns = md.getColumnCount();

                //  Get column names
                for (int i = 1; i <= columns; i++)
                {
                    columnNames.addElement( md.getColumnName(i) );
                }

                //  Get row data
                while (rs.next())
                {
                    Vector<Object> row = new Vector<Object>(columns);
                    for (int i = 1; i <= columns; i++)
                    {
                        row.addElement( rs.getObject(i) );
                    }
                    data.addElement( row );
                }

                rs.close();
                stmt.close();
                connection.close();
            }
            catch(Exception e1)
            {
                System.out.println( e1 );
            }

            //  Create table with database data
            DefaultTableModel model = new DefaultTableModel(data, columnNames)
            {
    			private static final long serialVersionUID = 1L;
    			@SuppressWarnings("unchecked")
    			@Override
                public Class getColumnClass(int column)
                {
                    for (int row = 0; row < getRowCount(); row++)
                    {
                        Object o = getValueAt(row, column);
                        if (o != null)
                        {
                            return o.getClass();
                        }
                    }
                    return Object.class;
                }
            };

    		bustable = new JTable( model );
    		System.out.println("No of businesses: "+bustable.getModel().getRowCount());
    		bustable.addMouseListener(new MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
			        int rownum = bustable.rowAtPoint(e.getPoint());
			        int columnnum = bustable.columnAtPoint(e.getPoint());
			        if (rownum >= 0 && columnnum >= 0) {
			        	System.out.println("Row:"+rownum+", Column:"+columnnum);
			        	String bid = (String) model.getValueAt(rownum, 4);
			        	System.out.println("bid: "+bid);
			        	
			        	/*Get reviews for bid*/
			            try
			            {
			                //  Connect to an Access Database
			            	Connection connection = null;
			                Class.forName("oracle.jdbc.driver.OracleDriver");        
			                connection = DriverManager.getConnection(
			                        "jdbc:oracle:thin:@localhost:1521:hmannuru", "scott", "tiger");

			                //  Read data from a table
			                String sql = "Select r.business_id, r.date1, r.stars, r.text, u.name, r.useful"
			                		+ " from review r, yelp_user u"
			                		+ " where r.business_id='"+bid+"' and u.user_id=r.user_id";
			                
			                Statement stmt = connection.createStatement();
			                ResultSet rs = stmt.executeQuery( sql );
			                ResultSetMetaData md = rs.getMetaData();
			                int columns = md.getColumnCount();

			                //  Get column names
			                for (int i = 1; i <= columns; i++)
			                {
			                    revcolumnNames.addElement( md.getColumnName(i) );
			                }

			                //  Get row data
			                while (rs.next())
			                {
			                    Vector<Object> row = new Vector<Object>(columns);
			                    for (int i = 1; i <= columns; i++)
			                    {
			                    	row.addElement( rs.getString(i) );
			                    }
			                    revdata.addElement( row );
			                }

			                rs.close();
			                stmt.close();
			                connection.close();
			            }
			            catch(Exception e1)
			            {
			                System.out.println( e1 );
			            }
			            
			            //  Create table with database data
			            DefaultTableModel model1 = new DefaultTableModel(revdata, revcolumnNames)
			            {
			    			private static final long serialVersionUID = 1L;
			    			@SuppressWarnings("unchecked")
			    			@Override
			                public Class getColumnClass(int column)
			                {
			                    for (int row = 0; row < getRowCount(); row++)
			                    {
			                        Object o = getValueAt(row, column);
			                        if (o != null)
			                        {
			                            return o.getClass();
			                        }
			                    }
			                    return Object.class;
			                }
			            };

			    		JTable revtable = new JTable( model1 );
			    		TableColumn c = revtable.getColumnModel().getColumn(3);
			    		c.setPreferredWidth(1000);
			    		//JPanel revpanel = new JPanel();
			    		//revpanel.add(revtable);
			    		JFrame revframe = new JFrame();
			    		revframe.getContentPane().add(new JScrollPane(revtable));
			    		revframe.setBounds(200, 200, 1500, 700);
			    		revframe.setLayout(new GridLayout(0, 1));
			    		revframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			    		revframe.setVisible(true);
			    		revframe.pack();
			        }
			    }
			});    		
    		frame.getContentPane().add(new JScrollPane(bustable));
    	}
    }
    
    private void fillmc(){
    	panelMainCategories.add("Active Life");
    	panelMainCategories.add("Arts & Entertainment");
    	panelMainCategories.add("Automotive");
    	panelMainCategories.add("Beauty & Spas");
    	panelMainCategories.add("Car Rental");
    	panelMainCategories.add("Cafes");
    	panelMainCategories.add("Convenience Stores");
    	panelMainCategories.add("Dentists");
    	panelMainCategories.add("Doctors");
    	panelMainCategories.add("Drugstores");
    	panelMainCategories.add("Department Stores");
    	panelMainCategories.add("Education");
    	panelMainCategories.add("Event Planning & Services");
    	panelMainCategories.add("Flowers & Gifts");
    	panelMainCategories.add("Food");
    	panelMainCategories.add("Grocery");
    	panelMainCategories.add("Health & Medical");
    	panelMainCategories.add("Home Services");
    	panelMainCategories.add("Home & Garden");
    	panelMainCategories.add("Hospitals");
    	panelMainCategories.add("Hotels & Travel");
    	panelMainCategories.add("Hardware Stores");
    	panelMainCategories.add("Medical Centers");
    	panelMainCategories.add("Nurseries & Gardening");
    	panelMainCategories.add("Nightlife");
    	panelMainCategories.add("Restaurants");
    	panelMainCategories.add("Shopping");
    	panelMainCategories.add("Transportation");    	
    }
    
    
    
    public HW3() throws SQLException, Exception {
		// TODO Auto-generated method stub
		frame = new JFrame();
		frame.setBounds(200, 200, 1500, 700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new GridLayout(0, 6, 0, 0));
		
		//new Populate();
		int j=0;
				
		mcpanel = new JPanel(new GridLayout(0, 1));
		mcpanel.setBorder(new TitledBorder("Business Main Categories"));

		fillmc();
		Iterator<String> i = panelMainCategories.iterator();
		while(i.hasNext()){
			j++;
			String s = i.next();
			System.out.println(j+": "+s+"\n");
			JCheckBox cb = new JCheckBox(s);
			cb.addActionListener(new mcbActionListener());		
			mcpanel.add(cb);
		}

		scpanel = new JPanel();
		scpanel.setLayout(new GridLayout(0,1));
		scpanel.setBorder(new TitledBorder("Business Sub-Categories"));
		
		attrpanel = new JPanel();
		attrpanel.setLayout(new GridLayout(0, 1));
		attrpanel.setBorder(new TitledBorder("Business Attributes"));
		
		locdayhrpanel = new JPanel();
		locdayhrpanel.setLayout(new GridLayout(4, 1));
		loc = new JComboBox<String>();
		loc.addActionListener(new locActionListener());
		day = new JComboBox<String>();
		day.addActionListener(new dayActionListener());
		fromtime = new JComboBox<String>();
		fromtime.addActionListener(new fromActionListener());
		totime = new JComboBox<String>();
		totime.addActionListener(new toActionListener());
		locdayhrpanel.add(loc);
		locdayhrpanel.add(day);
		locdayhrpanel.add(fromtime);
		locdayhrpanel.add(totime);
		enablecombos();
		
		searchpanel = new JPanel(new GridLayout(0, 1));
		JPanel andpanel = new JPanel(new GridBagLayout());
		JButton andbutton = new JButton("AND");
		andbutton.addActionListener(new andorActionListener());
		andpanel.add(andbutton);
		JPanel orpanel = new JPanel(new GridBagLayout());
		JButton orbutton = new JButton("OR");
		orbutton.addActionListener(new andorActionListener());
		orpanel.add(orbutton);
		JPanel extrapanel = new JPanel(new GridBagLayout());
		JButton searchbutton = new JButton("Search");
		searchbutton.addActionListener(new searchActionListener());
		extrapanel.add(searchbutton);
		searchpanel.add(andpanel);
		searchpanel.add(orpanel);
		searchpanel.add(extrapanel);
		defaultandor();
		
		businesspanel = new JPanel();
		businesspanel.setBorder(new TitledBorder("List of Businesses"));
		businesspanel.setLayout(new GridLayout(0,1));
		bustable = new JTable();

		frame.getContentPane().add(new JScrollPane(mcpanel));
		frame.getContentPane().add(new JScrollPane(scpanel));
		frame.getContentPane().add(new JScrollPane(attrpanel));
		frame.getContentPane().add(locdayhrpanel);
		frame.getContentPane().add(searchpanel);
		frame.getContentPane().add(bustable);
		frame.pack();
	}
	
	public static void main(String[] args) throws SQLException, Exception {
		HW3 h = new HW3();
		h.frame.setVisible(true);
	}

}
