package ui;
import java.util.Scanner;
import DBAPI.DBAPI;
//import lib.*;


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
        System.out.println("Username (ie \"jsmith\"): ");
        String user = sc.next();
        System.out.print("Password: ");
        String passwd = sc.next();
       //p String passwd = new String(System.console().readPassword("Password: "));
        
        api = new DBAPI();
        if (!api.authDB(user, passwd)) {
            System.out.println("\nError connecting to Oracle database. Please try again.\n");
            preStart();
        }
        promptReinit();
        obsevationMenuPatient();
       
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
        System.out.println("Not yet implemented - should call DBAPI.authLogin " +
                            "and then call appropriate (patient/healthProfessional)" +
                            " class to continue execution.");
        startMenu();
        /*
        //do some stuff to validate user/pass and determine role
        //(maybe have separate login class/module and call it here?)
        System.out.println("role of user is defaulted to patient for now");
        String role = "patient";

        switch (role) {
            case "patient":
                patientMenu();
                break;
            case "physician":
            //falls through
            case "nurse":
                healthProfMenu();
                break;
            default:
                System.out.println("Login incorrect, please try again.");
                login();
        }*/
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
    
    public void obsevationMenuPatient()
    {
while(true)
{
        Scanner in = new Scanner(System.in);
        System.out.println("\n\n\n\n===== Observations of Dailing Living -- Observation Menu =====\n");
        System.out.println("Available Options:");
        System.out.println(" 1.  Enter Observations");
        System.out.println(" 2.  View Observations");
        System.out.println(" 3.  Add a new Observation Type");
        System.out.println(" 4.  View my Alerts");
        System.out.println(" 5  Manage HealthFriends");
        System.out.println(" 6.  Back");
        System.out.print("\nInput: ");
        String input = in.next();
        switch (input) {
            case "1":
                recordObservation();
                break;
            case "2":
                viewObservation("ggeorge");
                break;
            case "3":
            	addObservationType();
                break;
            case "4":
                createUser();//write alert function name here
                break;
            case "5":
                login();//manage health friends
                break;
            case "6":
                startMenu();;
            default:
                System.out.println("Invalid input. Please Try again.");
                obsevationMenuPatient();
        }
       }
    }
    public void recordObservation()
    {
    	String obsType,obsDate,obsTime;
    	Scanner sc = new Scanner(System.in);
    	System.out.println("Enter observations for the following available types based on your Illness :");
    	api.observationMenu("ggeorge");
    	System.out.print("Enter your type of Observation : ");
    	obsType= sc.next();
    	System.out.println(obsType +":\nEnter :");
    	System.out.println("Enter Date of Observation in mm/dd/yyyy format :");
    	obsDate= sc.next();
    	System.out.println("Enter Time of Observation in HH:mm:ss format :");
    	obsTime= sc.next();
    	api.enterObservation("ggeorge",obsType,obsDate,obsTime);
    	
    }
    public void viewObservation(String patientId)
    {
    	api.displayObservation(patientId);
    }
    public void addObservationType()
    {
    }
}