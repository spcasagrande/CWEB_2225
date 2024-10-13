package application;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import java.util.Scanner;

public class mainApp {
	
	public static void main(String[] args)
	{
		// create a MongoClient instance
		MongoClient client = MongoClients.create("mongodb+srv://admin:123password@cluster0.la4np.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0");
		
		// access the database and collection
		MongoDatabase database = client.getDatabase("Homework3");
		MongoCollection<Document> collection = database.getCollection("testCollection");
		
		Scanner scanner = new Scanner(System.in);
		
		boolean keepRunning = true;

		while (keepRunning)
		{
			System.out.println("Choose an operation: ");
			System.out.println("1. Create a document");
			System.out.println("2. Read a document");
			System.out.println("3. Update a document");
			System.out.println("4. Delete a document");
			System.out.println("5. Exit");

			String choice = scanner.nextLine();

			switch (choice)
			{
				case "1":
					application.CRUD.CreateDocument(collection, scanner);
					break;
				case "2":
					application.CRUD.ReadDocument(collection, scanner);
					break;
				case "3":
					application.CRUD.UpdateDocument(collection, scanner);
					break;
				case "4":
					application.CRUD.DeleteDocument(collection, scanner);
					break;
				case "5":
					keepRunning = false;
					break;
				default:
					System.out.println("Invalid choice. Try again");
					break;
			}
		}
		scanner.close();
		client.close();
	}
}
