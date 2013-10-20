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
    
    
    public void viewAlerts(String uname)
    {
    	//query for displaying observations based on the pid(uname)
    	
    }
    
    public String authLogin(String uname, String password) throws SQLException
    {
    	   	
    	String PATIENT_ID="",HP_ID="",type="";
    	
    	String query_patient="SELECT PATIENT_ID from PATIENT_INFO WHERE PATIENT_ID='"+uname+"' AND PASSWORD='"+password+"'";
		String query_hp="SELECT HP_ID from HP_INFO WHERE HP_ID='"+uname+"' AND PASSWORD='"+password+"'";

    	
    	Statement stmt = conn.createStatement();
    	try {
    	    
    	     ResultSet rs_p = stmt.executeQuery(query_patient);
    	     if (!rs_p.next()) 
    	     {
    	    ResultSet rs_hp = stmt.executeQuery(query_hp);
    	    	if(!rs_hp.next())
    	    	{
    	    		type="Enter valid username and password.";
    	    	}
    	    	
    	    	else
    	    	{
    	    		type="HP";
    	    	}
    	     }
    	     
    	     else
    	    	 	type="patient";
    	     
    	     
    	 } catch (SQLException e ) {
    	     
    	 }
    	
    	return type;
    }
    	
    	
   
    	
}

