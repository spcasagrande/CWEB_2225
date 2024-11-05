package application;
import java.sql.*;
import java.util.Scanner;
import java.util.Date;

public class CRUD {
	
	// mysql connection string
	private static String connectionString = "jdbc:mysql://localhost:3306/new_database1?useSSL=false&allowPublicKeyRetrieval=true";
	private static String username = "new_username1";
	private static String password = "new_password1";
	
	static void CreateRecord(Scanner scanner) 
	{
		Date date = new Date();
		Timestamp created_at = new Timestamp(date.getTime());
		String[] inputs = getValidInput.getValidInputs();
		
		// adding it to the database
		try(Connection conn = DriverManager.getConnection(connectionString, username, password))
		{
			String query = "INSERT INTO Homework4 (name, email, phone, address, created_at) VALUES (?, ?, ?, ?, ?)";
			
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, inputs[0]);
			stmt.setString(2, inputs[1]);
			stmt.setString(3, inputs[2]);
			stmt.setString(4, inputs[3]);
			stmt.setTimestamp(5, created_at);
			stmt.executeUpdate();
			
			System.out.println("Information inserted successfully\n");
		}
		catch (SQLException ex)
		{
			ex.printStackTrace();
		}
	}
	
	static void ReadRecord(Scanner scanner) 
	{
		try(Connection conn = DriverManager.getConnection(connectionString, username, password))
		{
			boolean keepRunning = true;
			String query = "";
			String name;
			String email;
			String phone;
			String address;
			
			while (keepRunning)
			{
				System.out.println("\nHow do you wish to search?");
				System.out.println("1. Name");
				System.out.println("2. Email");
				System.out.println("3. Phone");
				System.out.println("4. Address");
				System.out.println("5. Print all documents");
				String choice = scanner.nextLine();
				
				switch (choice)
				{
				case "1":
					while (true)
					{
						System.out.print("\nEnter name: ");
						name = scanner.nextLine();
						if (validations.validations.validate_name(name) == true)
						{
							break;
						}
						else
						{
							System.out.println("Name cannot be empty");
						}
					}
					query = ("SELECT * FROM Homework4 WHERE name='" + name + "'");
					keepRunning = false;
					break;
				case "2":
					while (true)
					{
						System.out.print("\nEnter email: ");
						email = scanner.nextLine();
						if (validations.validations.validate_email(email) == true)
						{
							break;
						}
						else
						{
							System.out.print("Please enter a valid email format");
						}
					}
					query = "SELECT * FROM Homework4 WHERE email='" + email + "'";
					keepRunning = false;
					break;
				case "3":
					while (true)
					{
						System.out.print("\nEnter phone number (digits only): ");
						phone = scanner.nextLine();
						if (validations.validations.validate_phone(phone) == true)
						{
							break;
						}
						else
						{
							System.out.println("Please enter a valid phone number");
						}
					}
					query = "SELECT * FROM Homework4 WHERE phone='" + phone + "'";
					keepRunning = false;
					break;
				case "4":
					while (true)
					{
						System.out.print("\nEnter address: ");
						address = scanner.nextLine();
						if (validations.validations.validate_address(address) == true)
						{
							break;
						}
						else
						{
							System.out.println("Address cannot be empty");
						}
					}
					query = "SELECT * FROM Homework4 WHERE address='" + address + "'";
					keepRunning = false;
					break;
				case "5":
					query = "SELECT * FROM Homework4";
					keepRunning = false;
					break;
				default:
					System.out.println("Invalid choice. Try again");
					break;
				}
			}
			
			System.out.println();
			
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			System.out.println();
			// print header
			System.out.printf("%-10s %-15s %-20s %-15s %-20s %-25s%n", "ID", "Name", "Email", "Phone", "Address", "Created At");
			System.out.println(new String(new char[110]).replace("\0", "-"));
			
			// print record
			while (rs.next())
			{
				System.out.printf("%-10s %-15s %-20s %-15s %-20s %-25s%n", rs.getInt("id"), rs.getString("name"), rs.getString("email"), rs.getString("phone"), rs.getString("address"), rs.getTimestamp("created_at"));
			}
			System.out.println();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	static void UpdateRecord(Scanner scanner) 
	{
		String userid = "";
		int id = 0;
		boolean keepRunning = true;
		while (keepRunning)
		{
			System.out.println("\nDo you wish to search for the ID of the document you wish to update? (y/n): ");
			String choice = scanner.nextLine().toLowerCase();
			
			switch (choice)
			{
			case "y":
				ReadRecord(scanner);
				keepRunning = false;
				break;
			case "n":
				keepRunning = false;
				break;
			default:
				System.out.println("Please enter a valid choice");
				break;
			}
		}
		
		while (true)
		{
			System.out.println("\nEnter ID of document to be updated: ");
			userid = scanner.nextLine();
			
			if (!userid.isEmpty())
			{
				try
				{
					id = Integer.parseInt(userid);
					break;
				}
				catch (NumberFormatException e)
				{
					System.out.println("ID must be a number");
				}
			}
			else
			{
				System.out.println("ID cannot be empty");
			}
		}
		
		try(Connection conn = DriverManager.getConnection(connectionString, username, password))
		{
			String selectQuery = "SELECT * FROM Homework4 WHERE id='" + id + "'";
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(selectQuery);
		
			if (!rs.next())
			{
				System.out.println("No records found\n");
				return;
			}
			
			System.out.println("Please enter new values for the document");
			String[] inputs = getValidInput.getValidInputs();
			
			String updateQuery = "UPDATE Homework4 SET name = ?, email = ?, phone = ?, address = ? WHERE id = ?";
			PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
			updateStmt.setString(1, inputs[0]);
			updateStmt.setString(2, inputs[1]);
			updateStmt.setString(3, inputs[2]);
			updateStmt.setString(4, inputs[3]);
			updateStmt.setInt(5, id);
			int result = updateStmt.executeUpdate();
			
			if (result > 0)
			{
				System.out.println("Information updated successfully\n");
			}
			else
			{
				System.out.println("Information was not updated successfully\n");
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		
	}
	
	static void DeleteRecord(Scanner scanner) 
	{
		String userid = null;
		int id = 0;
		boolean keepRunning = true;
		while (keepRunning)
		{
			System.out.println("\nDo you wish to search for the ID of the document you wish to delete? (y/n): ");
			String choice = scanner.nextLine().toLowerCase();
			
			switch (choice)
			{
			case "y":
				ReadRecord(scanner);
				keepRunning = false;
				break;
			case "n":
				keepRunning = false;
				break;
			default:
				System.out.println("Please enter a valid choice");
				break;
			}
		}
		
		while (true)
		{
			System.out.println("\nEnter ID of document to be deleted: ");
			userid = scanner.nextLine();
			
			if (!userid.isEmpty())
			{
				try
				{
					id = Integer.parseInt(userid);
					break;
				}
				catch (NumberFormatException e)
				{
					System.out.println("ID must be a number");
				}
			}
			else
			{
				System.out.println("ID cannot be empty");
			}
		}
		
		try(Connection conn = DriverManager.getConnection(connectionString, username, password))
		{
			String query = "DELETE FROM Homework4 WHERE id = ?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, id);
			
			int result = stmt.executeUpdate();
			
			if (result > 0)
			{
				System.out.println("Information deleted successfully\n");
			}
			else
			{
				System.out.println("Information was not deleted successfully\n");
			}
		}
		catch (Exception ex) 
		{
			ex.printStackTrace();
		}
	}
}
