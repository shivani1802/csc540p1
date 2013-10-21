package ui;
import java.sql.SQLException;
import java.util.Scanner;

import lib.*;

/**
 * Description here.
 */

public class ODLCLI
{
    DBAPI api;

    public ODLCLI() 
    {
        preStart();
        startMenu();
    }

    public static void main(String [] args)
    {
        new ODLCLI();
    }

    public void preStart() {
        System.out.println("Please enter an oracle DB " + 
                            "username and password.\n");

        Scanner sc = new Scanner(System.in);
        //System.out.print("Username (ie \"jsmith\"): ");
       // String user = sc.next();
        String user="SSANGANE";
       
        
       // String passwd = new String(System.console().readPassword("Password: "));
       String passwd="200022497";
       
        
        api = new DBAPI();
        if (!api.authDB(user, passwd)) {
            System.out.println("\nError connecting to Oracle database. Please try again.\n");
            preStart();
        }
           promptReinit();
    }

    public void promptReinit() {
        System.out.print("\nWould you like to delete any existing tables and " + 
                    "re-initialize tables with the sample data? (y/n)");
        Scanner sc = new Scanner(System.in);
        String in = sc.next();
        switch (in) {
            case "y":
                api.dropTables();
                api.initTables();
                break;
            case "n":
                break;
            default:
                System.out.println("Please enter \'y\' or \'n\'.");
                promptReinit();
        }
    }

    public void startMenu()
    {
        Scanner in = new Scanner(System.in);
        System.out.println("\n\n\n\n===== Observations of Dailing Living -- Start Menu =====\n");
        System.out.println("Available Options:");
        System.out.println(" 1.  Login");
        System.out.println(" 2.  Create User");
        System.out.println(" 3.  Exit");
        System.out.print("\nInput: ");
        String input = in.next();
        switch (input) {
            case "1":
                login();
                break;
            case "2":
                createUser();
                break;
            case "3":
                terminate(0, "Exiting.");
            default:
                System.out.println("Invalid input. Please Try again.");
                startMenu();
        }
    }

    public void login() 
    {
    	String role="";
    	//Take username and password from the user
        Scanner s=new Scanner(System.in);
        String uname="", password="";
        System.out.println("Username");
        uname=s.next();
       
        System.out.println("Password");
        password=s.next();
               
        //call to DBAPI.authLogin for validation
        try{
       role=api.authLogin(uname,password);
        
        
        }
        
        catch(SQLException e )
        {
        	System.out.println("ERROR");
        }
        
  

        switch (role) {
            case "patient":
                patientMenu();
                break;
            case "HP":
            	healthProfMenu();
                break;
            default:
                System.out.println("Login incorrect, please try again.");
                login();
        }
    }

    public void createUser()
    {
        System.out.println("Create user screen not yet implemented, returning to start menu.");
        startMenu();
    }
    public void terminate(int status, String msg)
    {
        System.out.println(msg + "\n");
        System.exit(status);
    }
    
    public void patientMenu()
    {
    	
    	System.out.println("Select an option");
    	Scanner in=new Scanner(System.in);
    	
    	System.out.println("1. Enter Observations");
    	System.out.println("2. View Observations");
    	System.out.println("3. Add a new Observation Type");
    	System.out.println("4. View my Alerts");
    	System.out.println("5. Manage Health Friends");
    	System.out.println("6. Back");
    	String input = in.next();
    	
    	 switch (input) {
         case "1":
           //add functionality 
             break;
         case "2":
            //api.viewObs();
             break;
         case "3":
        	//add functionality
        	 break;
         case "4":
        	// api.viewAlerts();
        	 break;
         case "5":
        	 healthFriendsMenu();
        	 break;
         case "6":
        	 startMenu();
        	 break;
        	
         default:
             System.out.println("Invalid input. Please Try again.");
    	}

    }
    
    public void healthProfMenu()
    {
    	System.out.println("Show health prof menu");
    	//Menu for health professionals    	
    }
    
    
    public  void healthFriendsMenu()
	{
	System.out.println("Select an option");
	Scanner in=new Scanner(System.in);
	
	System.out.println("1. View existing Health Friends");
	System.out.println("2. Find a new Health Friend");
	System.out.println("3. Find a Health Friend at risk");
	System.out.println("4. Back");
	String input = in.next();
	
	 switch (input) {
     case "1":
       //add functionality 
         break;
     case "2":
      //add functionality 
         break;
     case "3":
    	//add functionality
    	 break;
     case "4":
    	 patientMenu();
    	 break;
     default:
         System.out.println("Invalid input. Please Try again.");
	

	}

}
    }
