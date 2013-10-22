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
}