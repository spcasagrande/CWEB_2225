package application;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;
import java.util.Scanner;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class CRUD {
	
	static void CreateDocument(MongoCollection<Document> collection, Scanner scanner)
	{
		String uniqueID = UUID.randomUUID().toString();
		Date created_at = new Date();
		String[] inputs = getValidInput.getValidInputs();
		
		Document document = new Document("id", uniqueID)
				.append("name", inputs[0])
				.append("email", inputs[1])
				.append("phone", inputs[2])
				.append("address", inputs[3])
				.append("created_at", created_at);
		
		collection.insertOne(document);
		System.out.println("Information inserted successfully\n");
	}
	
	static void ReadDocument(MongoCollection<Document> collection, Scanner scanner)
	{
		boolean keepRunning = true;
		List<Document> documents = null;
		
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
				String name;
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
				Bson nameFilter = Filters.eq("name", name);
				documents = collection.find(nameFilter).into(new java.util.ArrayList<>());
				keepRunning = false;
				break;
			case "2":
				String email;
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
				Bson emailFilter = Filters.eq("email", email);
				documents = collection.find(emailFilter).into(new java.util.ArrayList<>());
				keepRunning = false;
				break;
			case "3":
				String phone;
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
				Bson phoneFilter = Filters.eq("phone", phone);
				documents = collection.find(phoneFilter).into(new java.util.ArrayList<>());
				keepRunning = false;
				break;
			case "4":
				String address;
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
				Bson addressFilter = Filters.eq("address", address);
				documents = collection.find(addressFilter).into(new java.util.ArrayList<>());
				keepRunning = false;
				break;
			case "5":
				documents = collection.find().into(new java.util.ArrayList<>());
				keepRunning = false;
				break;
			default:
				System.out.println("Invalid choice. Try again");
				break;
			}
		}
		
		System.out.println();
		
		if (documents.isEmpty())
		{
			System.out.println("No records found");
			return;
		}

		// print headers
		System.out.printf("%-45s %-15s %-20s %-15s %-20s %-25s%n", "ID", "Name", "Email", "Phone", "Address", "Created At");
		System.out.println(new String(new char[145]).replace("\0", "-"));
		
		// print rows
		for (Document doc : documents)
		{
			String id = doc.containsKey("id") ? doc.getString("id"): "N/A";
			String name = doc.containsKey("name") ? doc.getString("name"): "N/A";
			String email = doc.containsKey("email") ? doc.getString("email"): "N/A";
			String phone = doc.containsKey("phone") ? doc.getString("phone"): "N/A";
			String address = doc.containsKey("address") ? doc.getString("address"): "N/A";
			String created_at = doc.containsKey("created_at") ? doc.getDate("created_at").toString(): "N/A";
			System.out.printf("%-45s %-15s %-20s %-15s %-20s %-12s%n", id, name, email, phone, address, created_at);
		}
		
		System.out.println();
	}
	
	static void UpdateDocument(MongoCollection<Document> collection, Scanner scanner)
	{
		String id = null;
		boolean keepRunning = true;
		while (keepRunning)
		{
			System.out.println("\nDo you wish to search for the ID of the document you wish to update? (y/n): ");
			String choice = scanner.nextLine().toLowerCase();
			
			switch (choice)
			{
			case "y":
				ReadDocument(collection, scanner);
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
			id = scanner.nextLine();
			
			if (id.isEmpty() == false)
			{
				break;
			}
			else
			{
				System.out.println("ID cannot be empty");
			}
		}
		
		Bson filter = Filters.eq("id", id);
		List<Document> documents = collection.find(filter).into(new java.util.ArrayList<>());
		
		if (documents.isEmpty())
		{
			System.out.println("No document found with that ID\n");
			return;
		}
		else
		{
			System.out.println("Please enter new values for the document");
			String[] inputs = getValidInput.getValidInputs();
			Bson updates = Updates.combine(
					Updates.set("name", inputs[0]),
					Updates.set("email", inputs[1]),
					Updates.set("phone", inputs[2]),
					Updates.set("address", inputs[3])
				);
			long modifiedCount = collection.updateOne(filter, updates).getModifiedCount();
			
			if (modifiedCount > 0) 
			{
				System.out.println("Document updated successfully\n");
			}
			
			else
			{
				System.out.println("Document could not be updated\n");
			}
		}
	}
	
	static void DeleteDocument(MongoCollection<Document> collection, Scanner scanner)
	{
		String id = null;
		System.out.println("\nDo you wish to search for the ID of the document you wish to delete? (y/n): ");
		String choice = scanner.nextLine();
		
		switch (choice.toLowerCase())
		{
		case "y":
			ReadDocument(collection, scanner);
			break;
		case "n":
			break;
		default:
			System.out.println("Please enter a valid choice");
		}
		
		while (true)
		{
			System.out.println("\nEnter ID of document to be deleted: ");
			id = scanner.nextLine();
			
			if (id.isEmpty() == false)
			{
				break;
			}
			else
			{
				System.out.println("ID cannot be empty");
			}
		}
		
		Bson filter = Filters.eq("id", id);
		List<Document> documents = collection.find(filter).into(new java.util.ArrayList<>());
		
		if (documents == null)
		{
			System.out.println("No document found with that ID\n");
			return;
		}
		else
		{
			long deletedCount = collection.deleteOne(filter).getDeletedCount();
			
			if (deletedCount > 0)
			{
				System.out.println("Document deleted successfully");
			}
			else
			{
				System.out.println("Document was not able to be deleted");
			}
		}
	}
}
