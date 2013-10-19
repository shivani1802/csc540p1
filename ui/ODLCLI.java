package ui;
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
        System.out.print("Username (ie \"jsmith\"): ");
        String user = sc.next();
        String passwd = new String(System.console().readPassword("Password: "));
        
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
    }
