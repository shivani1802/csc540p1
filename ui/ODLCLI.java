import java.util.Scanner;

/**
 * An incredibly simplistic, imcomplete implementation of the ODL command line interface.
 */

public class ODLCLI
{
	public ODLCLI() 
	{
		startMenu();
	}
	
	public static void main(String [] args)
	{
		new ODLCLI();
	}
	
	public void startMenu()
	{
		Scanner in = new Scanner(System.in);
		System.out.println("\n===== Observations of Dailing Living -- Start Menu =====\n");
		System.out.println("Available Options:");
		System.out.println(" 1.  Login");
		System.out.println(" 2.  Create User");
		System.out.println(" 3.  Exit");
		System.out.print("\nInput: ");
		String input = in.nextLine();
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
				reprompt(input);
				startMenu();
		}
	}
	
	public void login()
	{
		System.out.println("Login screen not yet implemented, returning to start menu.");
		startMenu();
	}
	public void createUser()
	{
		System.out.println("Create user screen not yet implemented, returning to start menu.");
		startMenu();
	}	
	
	public void reprompt(String invInput)
	{
		System.out.println("Invalid input \"" + invInput + "\"\nPlease try again.");
	}
	
	public void terminate(int status, String msg)
	{
		System.out.println(msg + "\n");
		System.exit(status);
	}
}