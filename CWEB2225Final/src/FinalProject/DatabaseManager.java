package FinalProject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.SQLException;

public class DatabaseManager {
	
	public static void main(String[] args) {
		
		String url = "jdbc:mysql://localhost:3306/";
		String user = "new_username1";
		String password = "new_password1";
		
		try(Connection db = DriverManager.getConnection(url, user, password);
				Statement stmt = db.createStatement())
		{
			// create database
			stmt.execute("DROP DATABASE IF EXISTS CWEB2225Final");
			stmt.execute("CREATE DATABASE CWEB2225Final");
			stmt.execute("USE CWEB2225Final");
			stmt.execute("""
	                CREATE TABLE Customers (
	                    customer_id INT PRIMARY KEY AUTO_INCREMENT,
	                    name VARCHAR(100) NOT NULL,
	                    email VARCHAR(100) NOT NULL,
	                    phone VARCHAR(15) NOT NULL,
	                    address VARCHAR(255),
	                    registration_date DATE NOT NULL
	                );
	            """);
			stmt.execute("""
	                CREATE TABLE Categories (
	                    category_id INT PRIMARY KEY AUTO_INCREMENT,
	                    category_name VARCHAR(100) NOT NULL
	                );
	            """);
			stmt.execute("""
	                CREATE TABLE Suppliers (
	                    supplier_id INT PRIMARY KEY AUTO_INCREMENT,
	                    supplier_name VARCHAR(100) NOT NULL,
	                    contact_email VARCHAR(100) NOT NULL,
	                    contact_phone VARCHAR(15) NOT NULL,
	                    address VARCHAR(255)
	                );
	            """);
			stmt.execute("""
	                CREATE TABLE Orders (
	                    order_id INT PRIMARY KEY AUTO_INCREMENT,
						customer_id INT NOT NULL,
	                    order_date DATE NOT NULL,
	                    total_amount DECIMAL(10,2) NOT NULL,
	                    FOREIGN KEY (customer_id) REFERENCES Customers(customer_id)
	                );
	            """);
			stmt.execute("""
	                CREATE TABLE Payments (
	                    payment_id INT PRIMARY KEY AUTO_INCREMENT,
						order_id INT NOT NULL,
	                    payment_date DATE NOT NULL,
	                    payment_method VARCHAR(50) NOT NULL,
	                    amount DECIMAL(10,2) NOT NULL,
	                    FOREIGN KEY (order_id) REFERENCES Orders(order_id)
	                );
	            """);
			stmt.execute("""
	                CREATE TABLE Products (
	                    product_id INT PRIMARY KEY AUTO_INCREMENT,
	                    product_name VARCHAR(100) NOT NULL,
						category_id INT NOT NULL,
	                    price DECIMAL(10,2) NOT NULL,
	                    stock_quantity INT NOT NULL,
	                    supplier_id INT NOT NULL,
	                    FOREIGN KEY (category_id) REFERENCES Categories(category_id),
	                    FOREIGN KEY (supplier_id) REFERENCES Suppliers(supplier_id)
	                );
	            """);
			stmt.execute("""
	                CREATE TABLE OrderDetails (
	                    order_detail_id INT PRIMARY KEY AUTO_INCREMENT,
						order_id INT NOT NULL,
	                    product_id INT NOT NULL,
	                    quantity INT NOT NULL,
	                    price_at_purchase DECIMAL(10,2) NOT NULL,
	                    FOREIGN KEY (order_id) REFERENCES Orders(order_id),
	                    FOREIGN KEY (product_id) REFERENCES Products(product_id)
	                );
	            """);
			
			// insert sample data into customer table
			String customerInsert = "INSERT INTO Customers (name, email, phone, address, registration_date) VALUES (?, ?, ?, ?, ?);";
			try(PreparedStatement customerStmt = db.prepareStatement(customerInsert))
			{
				Object[][] customerData = {
						{"Alice Brown", "alice@example.com", "1234567890", "123 Elm Street", "2024-01-15"},
						{"Bob Smith", "bob@example.com", "1234567891", "456 Maple Avenue", "2024-02-20"},
						{"Charlie Davis", "charlie@example.com", "1234567892", "789 Oak Lane", "2024-03-10"},
						{"Diana Green", "diana@example.com", "1234567893", "101 Pine Road", "2024-01-25"},
						{"Ethan White", "ethan@example.com", "1234567894", "202 Cedar Street", "2024-03-15"},
						{"Fiona Black", "fiona@example.com", "1234567895", "303 Birch Avenue", "2024-02-10"},
						{"George Blue", "george@example.com", "1234567896", "404 Walnut Drive", "2024-03-20"},
						{"Hannah Gold", "hannah@example.com", "1234567897", "505 Chestnut Lane", "2024-01-05"},
						{"Ian Silver", "ian@example.com", "1234567898", "606 Ash Street", "2024-02-25"},
						{"Julia Violet", "julia@example.com", "1234567899", "707 Spruce Road", "2024-03-05"}
				};
				
				for(Object[] customer : customerData)
				{
					customerStmt.setString(1, (String) customer[0]);
					customerStmt.setString(2, (String) customer[1]);
					customerStmt.setString(3, (String) customer[2]);
					customerStmt.setString(4, (String) customer[3]);
					customerStmt.setDate(5, java.sql.Date.valueOf((String) customer[4]));
					customerStmt.addBatch();
				}
				
				customerStmt.executeBatch();
			}
			
			// insert sample data into supplier table
			String supplierInsert = "INSERT INTO Suppliers (supplier_name, contact_email, contact_phone, address) VALUES (?, ?, ?, ?);";
			try(PreparedStatement supplierStmt = db.prepareStatement(supplierInsert))
			{
				Object[][] supplierData = {
						{"Tech Supplies Inc.", "contact@techsupplies.com", "9876543210", "1 Tech Park"},
						{"Home Essentials Co.", "support@homeessentials.com", "9876543211", "2 Home Street"},
						{"Office Depot", "sales@officedepot.com", "9876543212", "3 Office Lane"},
						{"Green Gadgets", "info@greengadgets.com", "9876543213", "4 Gadget Avenue"},
						{"Smart Electronics", "support@smartelectronics.com", "9876543214", "5 Smart Road"},
						{"Kitchen Wonders", "hello@kitchenwonders.com", "9876543215", "6 Kitchen Street"},
						{"Furniture Mart", "contact@furnituremart.com", "9876543216", "7 Furniture Way"},
						{"Book Haven", "sales@bookhaven.com", "9876543217", "8 Book Alley"},
						{"Fashion World", "info@fashionworld.com", "9876543218", "9 Fashion Lane"},
						{"Toy Universe", "support@toyuni.com", "9876543219", "10 Toy Street"}
				};
				
				for(Object[] supplier : supplierData)
				{
					supplierStmt.setString(1, (String) supplier[0]);
					supplierStmt.setString(2, (String) supplier[1]);
					supplierStmt.setString(3, (String) supplier[2]);
					supplierStmt.setString(4, (String) supplier[3]);
					supplierStmt.addBatch();
				}
				
				supplierStmt.executeBatch();
			}
			
			// insert sample data into category table
			String categoryInsert = "INSERT INTO Categories (category_name) VALUES (?);";
			try(PreparedStatement categoryStmt = db.prepareStatement(categoryInsert))
			{
				Object[][] categoryData = {
						{"Electronics"},
						{"Home Goods"},
						{"Books"},
						{"Furniture"},
						{"Fashion"},
						{"Toys"},
						{"Kitchenware"},
						{"Office Supplies"},
						{"Outdoor"},
						{"Fitness"}
				};
				
				for(Object[] category : categoryData)
				{
					categoryStmt.setString(1, (String) category[0]);
					categoryStmt.addBatch();
				}
				
				categoryStmt.executeBatch();
			}
			
			// insert sample data into products table
			String productInsert = "INSERT INTO Products (product_name, category_id, price, stock_quantity, supplier_id) VALUES (?, ?, ?, ?, ?);";
			try(PreparedStatement productStmt = db.prepareStatement(productInsert))
			{
				Object[][] productData = {
						{"Smartphone", 1, 699.99, 50, 1},
						{"Laptop", 1, 1199.99, 30, 1},
						{"Microwave Oven", 2, 299.99, 20, 2},
						{"Fiction Book", 3, 19.99, 100, 8},
						{"Office Chair", 4, 99.99, 40, 3},
						{"T-shirt", 5, 15.99, 150, 9},
						{"Toy Car", 6, 12.99, 200, 10},
						{"Blender", 7, 49.99, 60, 6},
						{"Printer", 8, 89.99, 25, 3},
						{"Camping Tent", 9, 199.99, 15, 4}
				};
				
				for(Object[] product : productData)
				{
					productStmt.setString(1, (String) product[0]);
					productStmt.setInt(2, (Integer) product[1]);
					productStmt.setDouble(3, (Double) product[2]);
					productStmt.setInt(4, (Integer) product[3]);
					productStmt.setInt(5, (Integer) product[4]);
					productStmt.addBatch();
				}
				
				productStmt.executeBatch();
			}
			
			// insert sample data into orders table
			String orderInsert = "INSERT INTO Orders (customer_id, order_date, total_amount) VALUES (?, ?, ?);";
			try(PreparedStatement orderStmt = db.prepareStatement(orderInsert))
			{
				Object[][] orderData = {
						{1, "2024-04-01", 719.98},
						{2, "2024-04-05", 299.99},
						{3, "2024-04-10", 1299.99},
						{4, "2024-04-15", 15.99},
						{5, "2024-04-20", 99.99},
						{6, "2024-04-25", 49.99},
						{7, "2024-04-30", 199.99},
						{8, "2024-05-01", 119.99},
						{9, "2024-05-05", 299.99},
						{10, "2024-05-10", 12.99}
				};
				
				for(Object[] order : orderData)
				{
					orderStmt.setInt(1, (Integer) order[0]);
					orderStmt.setString(2, (String) order[1]);
					orderStmt.setDouble(3, (Double) order[2]);
					orderStmt.addBatch();
				}
				
				orderStmt.executeBatch();
			}
			
			// insert sample data into order details table
			String orderDetailInsert = "INSERT INTO OrderDetails (order_id, product_id, quantity, price_at_purchase) VALUES (?, ?, ?, ?);";
			try(PreparedStatement orderDetailStmt = db.prepareStatement(orderDetailInsert))
			{
				Object[][] orderDetailData = {
						{1, 1, 1, 699.99},
						{1, 4, 1, 19.99},
						{2, 3, 1, 299.99},
						{3, 2, 1, 1299.99},
						{4, 6, 1, 15.99},
						{5, 5, 1, 99.99},
						{6, 8, 1, 49.99},
						{7, 10, 1, 199.99},
						{8, 9, 1, 119.99},
						{9, 3, 1, 299.99}
				};
				
				for(Object[] orderDetail : orderDetailData)
				{
					orderDetailStmt.setInt(1, (Integer) orderDetail[0]);
					orderDetailStmt.setInt(2, (Integer) orderDetail[1]);
					orderDetailStmt.setInt(3, (Integer) orderDetail[2]);
					orderDetailStmt.setDouble(4, (Double) orderDetail[3]);
					orderDetailStmt.addBatch();
				}
				
				orderDetailStmt.executeBatch();
			}
			
			// insert sample data into payments table
			String paymentInsert = "INSERT INTO Payments (order_id, payment_date, payment_method, amount) VALUES (?, ?, ?, ?);";
			try(PreparedStatement paymentStmt = db.prepareStatement(paymentInsert))
			{
				Object[][] paymentData = {
						{1, "2024-04-02", "Credit Card", 719.98},
						{2, "2024-04-06", "PayPal", 299.99},
						{3, "2024-04-11", "Credit Card", 1299.99},
						{4, "2024-04-16", "Cash", 15.99},
						{5, "2024-04-21", "Credit Card", 99.99},
						{6, "2024-04-26", "PayPal", 49.99},
						{7, "2024-05-01", "Credit Card", 199.99},
						{8, "2024-05-02", "Debit Card", 119.99},
						{9, "2024-05-06", "PayPal", 299.99},
						{10, "2024-05-11", "Credit Card", 12.99}
				};
				
				for(Object[] payment : paymentData)
				{
					paymentStmt.setInt(1, (Integer) payment[0]);
					paymentStmt.setString(2, (String) payment[1]);
					paymentStmt.setString(3, (String) payment[2]);
					paymentStmt.setDouble(4, (Double) payment[3]);
					paymentStmt.addBatch();
				}
				
				paymentStmt.executeBatch();
			}
			
		}
		catch (SQLException e) 
		{
			System.out.println("Error: " + e.getMessage());
		}
	}
}
