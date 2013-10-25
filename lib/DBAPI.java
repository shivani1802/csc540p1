package lib;
import java.sql.*;
import java.util.Scanner;

public class DBAPI {

    final String jdbcURL
    = "jdbc:oracle:thin:@ora.csc.ncsu.edu:1521:orcl";

    Connection conn;
    Statement stmt = null;
    ResultSet rs = null;

    public DBAPI() {
    }


    public boolean authDB(String user, String passwd) {
        try {
            // Load the driver. This creates an instance of the driver
            // and calls the registerDriver method to make Oracle Thin
            // driver available to clients.

            Class.forName("oracle.jdbc.driver.OracleDriver");

            // Get a connection from the first driver in the
            // DriverManager list that recognizes the URL jdbcURL
            conn = DriverManager.getConnection(jdbcURL, user, passwd);
            stmt = conn.createStatement();
        }
        catch(Throwable err) {
            conn = null;
        }
        return conn != null;
    }

    public boolean dropTables() {
        System.out.println("Tables dropped. (not really)");
        try
        {
       stmt.executeUpdate("drop table Makes_Observation");
       stmt.executeUpdate("drop table Observations");
      stmt.executeUpdate("drop table Has_Illness");
      stmt.executeUpdate("drop table patient_info");
       stmt.executeUpdate("drop table Observation_Type");
        }
        catch(Throwable err) {
            conn = null;
            System.out.println("Not dropped table" + err);
        }
        //insert SQL to drop all related tables
        return true;
    }

    public boolean initTables() {
        System.out.println("Tables initialized with sample data. (not really)");
        try
        {
       // stmt = conn.createStatement();
       stmt.executeUpdate("CREATE TABLE Patient_Info " +
 			"(Patient_NAME VARCHAR(32), Address varchar (100), Age integer,Sex varchar(6),Patient_Id varchar (10), Password varchar(10), PublicStatus char(2), primary key (Patient_Id))" );
     //  System.out.println("inserted table Patient");
       stmt.executeUpdate("INSERT INTO Patient_Info " +
			   "VALUES ('Gary George', '2806 Confer Drive Raleigh NC 27606', 25 , 'Male','ggeorge','geo123','Y')");
       //System.out.println("inserted value Patient");
       stmt.executeUpdate("create table Observation_Type"+
"(Illness varchar (10),Type varchar (40), Category varchar(40), AdditionalInfo varchar (200), primary key (Illness, Type))");
       //System.out.println("created table oBS");
       stmt.executeUpdate("INSERT INTO Observation_Type (Illness, Type, Category, AdditionalInfo)" +
			   "VALUES ('HIV','Temprature','Physiological','Amount in Fahrenheit')");
      
      //System.out.println("inserted table oBS");
      
       stmt.executeUpdate("INSERT INTO Observation_Type (Illness, Type, Category, AdditionalInfo)" +
			   "VALUES ('General', 'Diet','Behavioral','What was consumed and Amount in sevings')");
       stmt.executeUpdate("INSERT INTO Observation_Type (Illness, Type, Category, AdditionalInfo)" +
			   "VALUES ('General', 'Weight','Physiological','Amount in Pounds')");
      
        stmt.executeUpdate("create table Has_Illness"+
        		"(Patient_Id varchar(10), Illness varchar (10),  foreign key(Patient_Id) references Patient_Info(Patient_Id))");
        
        stmt.executeUpdate("INSERT INTO Has_Illness (Patient_Id, Illness)" +
 			   "VALUES ('ggeorge', 'HIV')");
       
        stmt.executeUpdate("create table Observations"+
"(OId varchar(5), Type varchar(40), Date_of_observation varchar(20), time_of_observation varchar(20), AdditionalInfo varchar(100), primary key (OId))");
      //  System.out.println("not inserted table observations new");
       // stmt.executeUpdate("Insert into Observations (OId, Type, Date_of_observtion,time_of_observation,AdditionalInfo)"+
//"values ('O1', 'Diet', to_date('01-01-2011','dd-mm-yyyy'),to_date('18:12:02', 'HH24:MI:SS'),'What was consumed and amount in servings: egg 1, orange ½, toast 1, margarine 1')");
        stmt.executeUpdate("Insert into Observations (OId, Type, Date_of_observation,time_of_observation,AdditionalInfo)"+
        		"values ('O1', 'Diet','01-01-2011','18:12:02','What was consumed and amount in servings: egg 1, orange ½, toast 1, margarine 1')");
     
       
         stmt.executeUpdate("create table Makes_Observation" +
"(pId varchar(10), OId varchar(5), Date_of_record varchar(20), time_of_record varchar(20), foreign key (pId) references Patient_Info(Patient_Id), foreign key(OId) references Observations)");
        // System.out.println("created table makesobs");
         stmt.executeUpdate("Insert into Makes_Observation (pId, OId, Date_of_record ,time_of_record)"+
        		"values ('ggeorge', 'O1', '01-01-2011','18:12:02')");
        }

        catch(Throwable err) {
            conn = null;
            System.out.println("Not inserted" + err);
        }
        //insert SQL to initialize all sample data tables
      
        /*create table alerts(a_id varchar(20),patient_id varchar(10), Oid varchar(10), description varchar(100), primary key(a_id), foreign key(patient_id) references patient_info)

        create table HAS_HF(hf_id varchar(20), patient_id varchar(10), on_date DATE, PRIMARY KEY(hf_id,patient_id), FOREIGN KEY(patient_id) REFERENCES PATIENT_INFO)


        create table Observations(OId varchar (5), Type varchar (40), Date_of_observation date, time_of_observation timestamp, AdditionalInfo varchar(100), primary key (OId))
         */
               
        return true;
    }
    
    public void observationMenu(String patientId)
    {
    	int i=1;
    	        Scanner sc= new Scanner(System.in);
    	
      	try
    	{
    		
    		
    	rs = stmt.executeQuery("select type from Observation_Type where Illness ='General' or Illness in ( SELECT Illness FROM Has_Illness where Patient_Id= '"+ patientId +"')");
    //	System.out.println(patientId);
    	// String typeObv1 = rs.getString("type");
		  //  System.out.println(i+". " + typeObv1);
    	while (rs.next()) {
		    String typeObv = rs.getString("type");
		    System.out.println(i+". " + typeObv);
		    i++;
		}
    	}
      	 catch(Throwable err) {
             conn = null;
             System.out.println("querry nt executed" + err);
             
             }
    }
      	public void enterObservation( String patientId,String Obs_Type,String obsDate,String obsTime)
      	{
      	//	System.out.println("hello");
      		int j=0;
      		String Obs_Data=null;
      		Scanner sc= new Scanner(System.in);
          try
       {
    	rs = stmt.executeQuery("select AdditionalInfo from Observation_Type where Type ='"+Obs_Type+"' and Illness ='General' or Illness in ( SELECT Illness FROM Has_Illness where Patient_Id= '"+ patientId +"')");
        	 // rs = stmt.executeQuery("select AdditionalInfo from Observation_Type where Type ='Diet' and Illness ='General' or Illness in ( SELECT Illness FROM Has_Illness where Patient_Id= 'ggeorge')");
    	while (rs.next()) {
		    String AdditionalInfo = rs.getString("AdditionalInfo");
		  String [] data= AdditionalInfo.split(",");
		  
		  while(j!=data.length)
		  {
			  System.out.println("Enter "+ data[j]);
			  Obs_Data+= ""+ sc.next();
			  j++;			  
		  }
		  stmt.executeUpdate("Insert into Observations "+
"values ('O2','"+Obs_Type+"','"+ obsDate +"','"+ obsTime + "','"+ Obs_Data +"')");
		 // System.out.println(patientId);
		  
		    // do we need a trigger instead of insert i makes here ??
		  
		 //If no trigger how to enter corresponding oid..that would be an issue 
		  
		  stmt.executeUpdate("Insert into Makes_Observation (pId, OId, Date_of_record ,time_of_record)"+
	        		"values ('"+patientId+"', 'O1', '01-01-2011','18:12:02')"); // here date and time are supposed to be system date and time as i trigger below but for the time being we have this
		  
		  /*
		   create or replace Trigger MakesObservationInsert
		   After Inser on Observations
		   Referencing new row as NewRow
		   insert into Makes_Observaton (pId, OId, Date_of_record ,time_of_record)
		   values (patient_Id, NewRow.OId, TO_CHAR(SYSDATE,'dd-Mon-yyy'),TO_CHAR(SYSDATE,' hh:mi:ss PM'))
		   */
		}
    	
    	}
    	  catch(Throwable err) {
              conn = null;
              System.out.println("querry nt executed" + err);
              
              }
    	 }

    	public void displayObservation(String patientId)
      	{
      		int i=0;
      		
          try
       {
    	rs = stmt.executeQuery("select * from Observations o,makes_observation m where o.oid=m.oid and m.pid='"+patientId+"'");  
    	
    	while (rs.next()) {
		    
		    System.out.println(i+". " + rs.getString("type"));
		    System.out.println(rs.getString("Oid"));
		    System.out.println(rs.getString("date_of_observation"));
		    System.out.println(rs.getString("time_of_observation"));
		    System.out.println(rs.getString("AdditionalInfo"));
		    i++;
		}    	
    	}
    	  catch(Throwable err) {
              conn = null;
              System.out.println("querry nt executed" + err);
              
              }
          }

    public boolean close(Connection conn) {
        if(conn != null) {
            try {
                conn.close();
            }
            catch(Throwable whatever) {
                return false;
            }
        }
        return true;
    }

    public boolean close(Statement st) {
        if(st != null) {
            try {
                st.close();
            }
            catch(Throwable whatever) {
                return false;
            }
        }
        return true;
    }

    public boolean close(ResultSet rs) {
        if(rs != null) {
            try {
                rs.close();
            }
            catch(Throwable whatever) {
                return false;
            }
        }
        return true;
    }
<<<<<<< HEAD
}
=======
    
    public void viewObs(String uname)
    {
 	   //query for displaying observations based on the pid(uname)
    }
    
    
    public void viewMyAlerts(String uname)
    {
    	//query for displaying alerts based on the pid(uname)
    	
    }
    
    
    public String authLogin(String uname, String password) throws SQLException
    {   	
    	String type="";
    	String query_patient="SELECT PATIENT_ID from PATIENT_INFO WHERE PATIENT_ID='"+uname+"' AND PASSWORD='"+password+"'";
		String query_hp="SELECT HP_ID from HP_INFO WHERE HP_ID='"+uname+"' AND PASSWORD='"+password+"'";

    	Statement stmt = conn.createStatement();
    	try {
    	     ResultSet rs_p = stmt.executeQuery(query_patient);
    	     if (!rs_p.next()) 
    	     {
    	     ResultSet rs_hp = stmt.executeQuery(query_hp);
    	    	if(!rs_hp.next())
    	    		type="Enter valid username and password.";
    	    	
    	    	else
    	    		type="HP";
    	     }
    	     else
    	    	 	type="patient";
    	     
    	 	} catch (SQLException e ) 
    	 		{}
    	return type;
    }
    	
    	
    public void viewHF(String uname) throws SQLException
    {
    	String query_hf="SELECT DISTINCT patient_name, p.patient_id"
    					+ " FROM PATIENT_INFO p, HAS_HF h "
    					+ "WHERE h.patient_id='"+uname+"' AND h.hf_id=p.patient_id";
    	
    	Statement stmt = conn.createStatement();
    	
    	try {	
    		ResultSet rs_hf = stmt.executeQuery(query_hf);
    		int count=1;
    		System.out.println("\n\n\nYOUR HEALTHFRIENDS\n");
    	     while (rs_hf.next()) 
    	     	{
    	         String name = rs_hf.getString("patient_name");
    	         String id = rs_hf.getString("patient_id");
    	         System.out.println("\n"+count+". "+id+"\t"+name);
    	         count++;
    	     	}
    	     
    	 	} catch (SQLException e ) 
    	 		{}
   
    }
    	
    	 public boolean findNewHF(String uname) throws SQLException
    	    {	
    		 	boolean existnewfriend=true;
    	    	String query_hf="SELECT p2.PATIENT_ID, p2.PATIENT_NAME "
    	    			+ "FROM PATIENT_INFO p1, PATIENT_INFO p2 "
    	    			+ "WHERE p1.patient_id='"+uname+"' AND p1.class=p2.class and p1.patient_id<>p2.patient_id "
    	    			+ "MINUS (select p.patient_id, p.patient_name from patient_info p,has_hf h "
    	    			+ "where p.patient_id=h.hf_id and h.patient_id='"+uname+"')";
    	    	
    	    	Statement stmt = conn.createStatement();
    	    
    	    	try {
    	    		ResultSet rs_hf = stmt.executeQuery(query_hf);
    	    		if(!rs_hf.next())
    	    		{
    	    			System.out.println("No new friends to add\n\n");
    	    			existnewfriend=false;
    	    		}
    	    		
    	    		else{
    	    		rs_hf = stmt.executeQuery(query_hf);
    	    			int count=1;
    	    			
        	    		System.out.println("\n\n\nADD HEALTHFRIENDS\n\nPATIENT ID \tPATIENT NAME");
        	    		
    	    	     while (rs_hf.next()) {
    	    	         String id = rs_hf.getString("patient_id");
    	    	         String name=rs_hf.getString("patient_name");
    	    	         System.out.println("\n"+count+". "+id+"\t"+name);
    	    	         count++;
    	    	    
    	    		}
    	    		}
    	    	 } catch (SQLException e ) {
    	    	     
    	    	 }
    	    	
    	    	return existnewfriend;
    	    }
    	 
    	 
    	 
    	 public void addNewHF(String uname, String addFriend) throws SQLException
 	    {
    		 Statement stmt = conn.createStatement();
    		 
    		 	//check if the entered id is valid 	
    		String query_check="select patient_name "
    							+ "from patient_info "
    							+ "where patient_id='"+addFriend+"'";
    		ResultSet rs_check = stmt.executeQuery(query_check);
    		if(!rs_check.next())
    			System.out.println("\nEnter a valid Patient ID.");
    		
    		else{
    		String friendName=rs_check.getString("patient_name");
    		
    			//insert into table if id entered exists in the patient table
    		String query_hf="insert into HAS_HF values('"+addFriend+"', '"+uname+"', sysdate)";
    		try{
    		stmt.executeUpdate(query_hf);
    		System.out.println(friendName+ " and you are now friends.\n\n");
    		}
    		catch(SQLException e)
    		{}
 	    		}
 	    }
    	 
    	 public boolean viewRiskHF(String uname) throws SQLException
    	 {	
    		 boolean atRisk=true;
    		 Statement stmt = conn.createStatement();
    		 String query_riskHF="select patient_name, p.patient_id "
 				+ "from patient_info p, alerts a "
 				+ "where p.patient_id=a.patient_id and a.patient_id<>'"+uname+"' "
 				+ "group by p.patient_name, p.patient_id "
 				+ "having count(a_id)>2";
 		
    		 ResultSet rs_riskHF = stmt.executeQuery(query_riskHF);
 		
    		 if(!rs_riskHF.next())
    		 {
    			 System.out.println("\n\nNo health friends at risk currently.\n");
    			 atRisk=false;
    			 rs_riskHF.close();
    		 }	
 			
    		 else{
    			 rs_riskHF = stmt.executeQuery(query_riskHF);
    			 int count=1;
    			 System.out.println("\n\n\nLIST OF HEALTH FRIENDS AT RISK\n\n");
		
    			 while (rs_riskHF.next()) {
    	 
    	
    				 String name=rs_riskHF.getString("patient_name");
    				 String id=rs_riskHF.getString("patient_id");
    				 System.out.println("\n"+count+". "+id+"\t"+name);
    				 count++;
    			 	}
 		 		}
     
    		 return atRisk;
    	 }
    	 
    	 
    	 
    	 public void msgRiskHF(String uname, String riskFriend)
    	 {
    		 //create message table to store msg
    		 //store msg_id,from (uname), to (riskFriend)
    		 System.out.println("Msg "+ riskFriend);
    	 }
    	 

    	
    	 public void viewHFAlerts(String uname) throws SQLException
    	    {
    	    	//query for displaying friend's alerts
    		 String query_hf="SELECT a_id, description"
 					+ " FROM alerts "
 					+ "WHERE patient_id='"+uname+"'";
 	
    		 Statement stmt = conn.createStatement();
 	
    		 try {	
    			 ResultSet rs_hf = stmt.executeQuery(query_hf);
    			 int count=1;
    			 System.out.println("\n\n\nHealth Friend's Alerts\n");
    			 while (rs_hf.next()) 
    			 {
    				 String a_id = rs_hf.getString("a_id");
    				 String desc = rs_hf.getString("description");
 	         
    				 System.out.println("\n"+count+". "+a_id+"\t"+desc);
    				 count++;
    			 }
 	     
    		 	} catch (SQLException e ) 
    		 		{}

    	    }
    	 
    	 
    	 public void viewHFobs(String uname)
    	 {
    		//Show Health friend's Observations from observation table 
    		 System.out.println("No observations for now..have to check observations table");
    	 }
}		
>>>>>>> origin/master
