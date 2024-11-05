package application;
import java.sql.*;
import java.util.Scanner;

public class mainApp {

	// mysql connection string
	private static String connectionString = "jdbc:mysql://localhost:3306/new_database1?useSSL=false&allowPublicKeyRetrieval=true";
	private static String username = "new_username1";
	private static String password = "new_password1";
		
	public static void main(String[] args) {
		EnsureTableExists();
		Scanner scanner = new Scanner(System.in);
		boolean keepRunning = true;
		
		
		
		while (keepRunning)
		{
			System.out.println("Choose an operation: ");
			System.out.println("1.Create a document");
			System.out.println("2.Read documents");
			System.out.println("3.Update a document");
			System.out.println("4.Delete a document");
			System.out.println("5.Exit");
			String choice = scanner.nextLine();
			
			switch (choice)
			{
			case "1":
				application.CRUD.CreateRecord(scanner);
				break;
			case "2":
				application.CRUD.ReadRecord(scanner);
				break;
			case "3":
				application.CRUD.UpdateRecord(scanner);
				break;
			case "4":
				application.CRUD.DeleteRecord(scanner);
				break;
			case "5":
				keepRunning = false;
				break;
			default:
				System.out.println("Please enter a valid choice");
			}
		}
		
		scanner.close();
	}
	
	private static void EnsureTableExists() 
	{
		try(Connection conn = DriverManager.getConnection(connectionString, username, password))
		{
			String createTableQuery = """
										CREATE TABLE IF NOT EXISTS Homework4(
										id INT AUTO_INCREMENT PRIMARY KEY,
										name VARCHAR(50) NOT NULL,
										email VARCHAR(100) NOT NULL,
										phone VARCHAR(20) NOT NULL,
										address VARCHAR(255) NOT NULL,
										created_at TIMESTAMP NOT NULL
										)""";
			
			Statement stmt = conn.createStatement();
			stmt.execute(createTableQuery);
			//System.out.println("Table is verified to exist");
			
		}
		catch (SQLException ex)
		{
			ex.printStackTrace();
		}
	}

}
