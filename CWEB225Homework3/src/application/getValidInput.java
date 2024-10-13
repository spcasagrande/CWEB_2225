package application;
import java.util.Scanner;

public class getValidInput {

	public static String[] getValidInputs()
	{
		Scanner scanner = new Scanner(System.in);
		String[] list = {"", "", "", ""};
		
		String name;
		String email;
		String phone;
		String address;
		
		// getting the name
		while (true)
		{
			System.out.print("\nEnter name: ");
			name = scanner.nextLine();
			if (validations.validations.validate_name(name) == true)
			{
				list[0] = name;
				break;
			}
			else
			{
				System.out.println("Name cannot be empty");
			}
		}
		
		// getting the email
		while (true)
		{
			System.out.print("\nEnter email: ");
			email = scanner.nextLine();
			if (validations.validations.validate_email(email) == true)
			{
				list[1] = email;
				break;
			}
			else
			{
				System.out.println("Please enter a properly formated email address");
			}
		}
		
		// getting the phone
		while (true)
		{
			System.out.print("\nEnter phone (digits only): ");
			phone = scanner.nextLine();
			if (validations.validations.validate_phone(phone) == true)
			{
				list[2] = phone;
				break;
			}
			else
			{
				System.out.println("Please enter a valid phone number");
			}
		}
		
		// getting the address
		while (true)
		{
			System.out.print("\nEnter address: ");
			address = scanner.nextLine();
			if (validations.validations.validate_address(address) == true)
			{
				list[3] = address;
				break;
			}
			else
			{
				System.out.println("Address cannot be empty");
			}
		}
		
		scanner.close();
		return list;
	}
}
