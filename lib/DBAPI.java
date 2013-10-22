package lib;
import java.sql.*;

public class DBAPI {

    final String jdbcURL
    = "jdbc:oracle:thin:@ora.csc.ncsu.edu:1521:orcl";

    Connection conn;

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
        }
        catch(Throwable err) {
            conn = null;
        }
        return conn != null;
    }

    public boolean dropTables() {
        System.out.println("Tables dropped. (not really)");
        //insert SQL to drop all related tables
        return true;
    }

    public boolean initTables() {
        System.out.println("Tables initialized with sample data. (not really)");
        //insert SQL to initialize all sample data tables
      
        /*create table alerts(a_id varchar(20),patient_id varchar(10), Oid varchar(10), description varchar(100), primary key(a_id), foreign key(patient_id) references patient_info)

        create table HAS_HF(hf_id varchar(20), patient_id varchar(10), on_date DATE, PRIMARY KEY(hf_id,patient_id), FOREIGN KEY(patient_id) REFERENCES PATIENT_INFO)


        create table Observations(OId varchar (5), Type varchar (40), Date_of_observation date, time_of_observation timestamp, AdditionalInfo varchar(100), primary key (OId))
         */
               
        return true;
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
