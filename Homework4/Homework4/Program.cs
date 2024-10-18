using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;
using System.Xml.Linq;
using MySql.Data.MySqlClient;
using Org.BouncyCastle.Asn1;

namespace Homework4
{
	internal class Program
	{
		// mysql connection string
		private static string connectionString = "server=localhost;user=new_username2;database=homework4;port=3306;password=new_password2";

		static void Main(string[] args)
		{
			EnsureTableExists();
			bool keepRunning = true;
			while (keepRunning)
			{
				Console.WriteLine("Choose an operation");
				Console.WriteLine("1.Create a record");
				Console.WriteLine("2.Read records");
				Console.WriteLine("3.Update a record");
				Console.WriteLine("4.Delete a record");
				Console.WriteLine("5.Exit");
				var choice = Console.ReadLine();

				switch (choice)
				{
					case "1":
						CreateRecord();
						break;
					case "2":
						ReadRecord();
						break;
					case "3":
						UpdateRecord();
						break;
					case "4":
						DeleteRecord();
						break;
					case "5":
						keepRunning = false;
						break;
					default:
						Console.WriteLine("Please select a valid option");
						break;
				}
			}
		}

		static void EnsureTableExists()
		{
			using (MySqlConnection conn = new MySqlConnection(connectionString))
			{
				conn.Open();
				string createTableQuery = @"
				CREATE TABLE IF NOT EXISTS `H4_TABLE` (
				`id` INT AUTO_INCREMENT PRIMARY KEY,
				`name` VARCHAR(50) NOT NULL,
				`email` VARCHAR(100) NOT NULL,
				`phone` VARCHAR(20) NOT NULL,
				`address` VARCHAR(255) NOT NULL,
				`created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP()
				);";

				MySqlCommand cmd = new MySqlCommand(createTableQuery, conn);
				cmd.ExecuteNonQuery();
				//Console.WriteLine("Table 'H4_TABLE' exists");
			}
		}

		static bool ValidateName(string name)
		{
			if (string.IsNullOrEmpty(name))
			{
				return false;
			}
			else
			{
				return true;
			}
		}

		static bool ValidateEmail(string email)
		{
			Regex regex = new Regex(@"^([\w\.\-]+)@([\w\-]+)((\.(\w){2,3})+)$");
			Match match = regex.Match(email);
			if (match.Success)
				return true;
			else
				return false;
		}

		static bool ValidatePhone(string phone)
		{
			for (int i = 0; i < phone.Length; i++)
			{
				if (!Char.IsDigit(phone[i]))
				{
					return false;
				}
			}

			if ((15 > phone.Length) && (phone.Length > 9))
			{
				return true;
			}
			else
			{
				return false;
			}
		}

		static bool ValidateAddress(string address)
		{
			if (string.IsNullOrEmpty(address))
			{
				return false;
			}
			else
			{
				return true;
			}
		}

		static List<string> GetValidInput()
		{
			List<string> input = new List<string>();

			// get the name
			while (true)
			{
				Console.WriteLine("Enter name: ");
				string name = Console.ReadLine();
				if (ValidateName(name))
				{
					input.Add(name);
					break;
				}
				else
				{
					Console.WriteLine("Name cannot be empty");
				}
			}

			// get the email
			while (true)
			{
				Console.WriteLine("Enter email: ");
				string email = Console.ReadLine();
				if (ValidateEmail(email))
				{
					input.Add(email);
					break;
				}
				else
				{
					Console.WriteLine("Please enter a properly formated email address");
				}
			}

			// get the phone
			while (true)
			{
				Console.WriteLine("Enter phone (digits only): ");
				string phone = Console.ReadLine();
				if (ValidatePhone(phone))
				{
					input.Add(phone);
					break;
				}
				else
				{
					Console.WriteLine("Please enter a valid phone number");
				}
			}

			// get the address
			while (true)
			{
				Console.WriteLine("Enter address: ");
				string address = Console.ReadLine();
				if (ValidateAddress(address))
				{
					input.Add(address);
					break;
				}
				else
				{
					Console.WriteLine("Address cannot be empty");
				}
			}

			return input;
		}

		static void PrintFormattedDataTable(DataTable table)
		{
			// find the maximum width of each column
			int[] columnWidths = new int[table.Columns.Count];

			for (int i = 0; i < table.Columns.Count; i++)
			{
				columnWidths[i] = table.Columns[i].ColumnName.Length;

				foreach (DataRow row in table.Rows)
				{
					int length = row[i].ToString().Length;

					if (length > columnWidths[i])
					{
						columnWidths[i] = length;
					}
				}
			}

			Console.WriteLine();
			// print the headers with padding
			for (int i = 0; i < table.Columns.Count; i++)
			{
				Console.Write(table.Columns[i].ColumnName.PadRight(columnWidths[i] + 2));
			}
			Console.WriteLine();

			// print row seperator
			for (int i = 0; i < columnWidths.Length; i++)
			{
				Console.Write(new string('-', columnWidths[i] + 2));
			}
			Console.WriteLine();

			// print the rows with padding
			foreach (DataRow row in table.Rows)
			{
				for (int i = 0; i < table.Columns.Count; i++)
				{
					Console.Write(row[i].ToString().PadRight(columnWidths[i] + 2));
				}
				Console.WriteLine();
			}
			Console.WriteLine();
		}

		static void CreateRecord()
		{
			Console.WriteLine("");
			List<string> inputs = GetValidInput();
			Console.WriteLine("");

			// insert the record into mysql
			using (MySqlConnection conn = new MySqlConnection(connectionString))
			{
				conn.Open();
				string query = "INSERT INTO H4_TABLE (name, email, phone, address) VALUES (@name, @email, @phone, @address)";

				MySqlCommand cmd = new MySqlCommand(query, conn);
				cmd.Parameters.AddWithValue("@name", inputs[0]);
				cmd.Parameters.AddWithValue("@email", inputs[1]);
				cmd.Parameters.AddWithValue("@phone", inputs[2]);
				cmd.Parameters.AddWithValue("@address", inputs[3]);
				cmd.ExecuteNonQuery();

				Console.WriteLine("Record inserted successfully\n");
				conn.Close();
			}
		}

		static void ReadRecord()
		{
			using (MySqlConnection conn = new MySqlConnection(connectionString))
			{
				conn.Open();
				string name = null;
				string email = null;
				string phone = null;
				string address = null;
				string query = null;
				bool keepRunning = true;

				while (keepRunning)
				{
					Console.WriteLine("How do you wish to search?");
					Console.WriteLine("1. Name");
					Console.WriteLine("2. Email");
					Console.WriteLine("3. Phone");
					Console.WriteLine("4. Address");
					Console.WriteLine("5. Print all documents");
					string choice = Console.ReadLine();

					switch (choice)
					{
						case "1":
							while (true)
							{
								Console.WriteLine("Enter name: ");
								name = Console.ReadLine();
								if (ValidateName(name))
								{
									break;
								}
								else
								{
									Console.WriteLine("Name cannot be empty");
								}
							}
							query = "SELECT * from H4_TABLE WHERE name = @name";
							keepRunning = false;
							break;
						case "2":
							while (true)
							{
								Console.WriteLine("Enter email: ");
								email = Console.ReadLine();
								if (ValidateEmail(email))
								{
									break;
								}
								else
								{
									Console.WriteLine("Please enter a properly formated email address");
								}
							}
							query = "SELECT * from H4_TABLE WHERE email = @email";
							keepRunning = false;
							break;
						case "3":
							while (true)
							{
								Console.WriteLine("Enter phone (digits only): ");
								phone = Console.ReadLine();
								if (ValidatePhone(phone))
								{
									break;
								}
								else
								{
									Console.WriteLine("Please enter a valid phone number");
								}
							}
							query = "SELECT * from H4_TABLE WHERE phone = @phone";
							keepRunning = false;
							break;
						case "4":
							while (true)
							{
								Console.WriteLine("Enter address: ");
								address = Console.ReadLine();
								if (ValidateAddress(address))
								{
									break;
								}
								else
								{
									Console.WriteLine("Address cannot be empty");
								}
							}
							query = "SELECT * from H4_TABLE WHERE address = @address";
							keepRunning = false;
							break;
						case "5":
							query = "SELECT * from H4_TABLE";
							keepRunning = false;
							break;
						default:
							Console.WriteLine("Please select a valid choice");
							break;
					}
				}

				MySqlCommand cmd = new MySqlCommand(query, conn);
				cmd.Parameters.AddWithValue("@name", name);
				cmd.Parameters.AddWithValue("@email", email);
				cmd.Parameters.AddWithValue("@phone", phone);
				cmd.Parameters.AddWithValue("@address", address);
				MySqlDataReader reader = cmd.ExecuteReader();

				if (!reader.HasRows)
				{
					Console.WriteLine("\nNo records found\n");
					return;
				}

				// create a datatable to display records
				DataTable table = new DataTable();
				table.Columns.Add("ID");
				table.Columns.Add("Name");
				table.Columns.Add("Email");
				table.Columns.Add("Phone");
				table.Columns.Add("Address");
				table.Columns.Add("Created At");

				while (reader.Read())
				{
					DataRow row = table.NewRow();
					row["ID"] = reader["id"].ToString();
					row["Name"] = reader["name"].ToString();
					row["Email"] = reader["email"].ToString();
					row["Phone"] = reader["phone"].ToString();
					row["Address"] = reader["address"].ToString();
					row["Created At"] = reader["created_at"].ToString();

					table.Rows.Add(row);
				}

				PrintFormattedDataTable(table);
				reader.Close();
				conn.Close();
			}
		}

		static void UpdateRecord()
		{
			bool keepRunning = true;
			while (keepRunning)
			{
				Console.WriteLine("Do you want to search for the ID of the record to update? (y/n): ");
				string choice = Console.ReadLine().ToLower();

				switch (choice)
				{
					case "y":
						ReadRecord();
						keepRunning = false;
						break;
					case "n":
						keepRunning = false;
						break;
					default:
						Console.WriteLine("Please enter a valid choice");
						break;
				}
			}

			Console.WriteLine("");
			string id = null;
			while (true)
			{
				Console.WriteLine("Enter the ID of the person to be updated: ");
				id = Console.ReadLine();
				if (string.IsNullOrEmpty(id))
				{
					Console.WriteLine("ID cannot be empty");
				}
				else 
				{ 
					break; 
				}
			}

			using (MySqlConnection conn = new MySqlConnection(connectionString))
			{
				conn.Open();
				string query = "SELECT * FROM H4_TABLE WHERE id = @id";
				MySqlCommand cmd = new MySqlCommand(query, conn);
				cmd.Parameters.AddWithValue("@id", id);
				MySqlDataReader reader = cmd.ExecuteReader();

				if (!reader.HasRows)
				{
					Console.WriteLine($"\nNo record found with ID: {id}\n");
					return;
				}
				reader.Close();

				Console.WriteLine("");
				List<string> inputs = GetValidInput();
				Console.WriteLine("");

				string updateQuery = "UPDATE H4_TABLE SET name = @newName, email = @newEmail, phone = @newPhone, address = @newAddress WHERE id = @id";
				MySqlCommand updateCmd = new MySqlCommand(updateQuery, conn);
				updateCmd.Parameters.AddWithValue("@id", id);
				updateCmd.Parameters.AddWithValue("@newName", inputs[0]);
				updateCmd.Parameters.AddWithValue("@newEmail", inputs[1]);
				updateCmd.Parameters.AddWithValue("@newPhone", inputs[2]);
				updateCmd.Parameters.AddWithValue("@newAddress", inputs[3]);
				int result = updateCmd.ExecuteNonQuery();

				if (result == 0)
				{
					Console.WriteLine("\nRecord could not be updated\n");
				}
				else
				{
					Console.WriteLine("\nRecord updated successfully\n");
				}
			}
		}

		static void DeleteRecord()
		{
			bool keepRunning = true;
			while (keepRunning)
			{
				Console.WriteLine("Do you want to search for the ID of the record to delete? (y/n): ");
				string choice = Console.ReadLine().ToLower();

				switch (choice)
				{
					case "y":
						ReadRecord();
						keepRunning = false;
						break;
					case "n":
						keepRunning = false;
						break;
					default:
						Console.WriteLine("Please enter a valid choice");
						break;
				}
			}

			Console.WriteLine("");
			string id = null;
			while (true)
			{
				Console.WriteLine("Enter the ID of the person to be deleted: ");
				id = Console.ReadLine();
				if (string.IsNullOrEmpty(id))
				{
					Console.WriteLine("ID cannot be empty");
				}
				else
				{
					break;
				}
			}

			using (MySqlConnection conn = new MySqlConnection(connectionString))
			{
				conn.Open();
				string query = "DELETE FROM H4_TABLE WHERE id = @id";
				MySqlCommand cmd = new MySqlCommand(query, conn);
				cmd.Parameters.AddWithValue("@id", id);
				int result = cmd.ExecuteNonQuery();

				if (result == 0)
				{
					Console.WriteLine("\nRecord could not be deleted\n");
				}
				else
				{
					Console.WriteLine("\nRecord deleted successfully\n");
				}
			}
		}

	}
}
