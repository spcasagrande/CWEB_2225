package FinalProject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QueryExecutor {
	
	// database connection
	private static final String HOST = "localhost";
	private static final String USER = "new_username1";
	private static final String PASSWORD = "new_password1";
	private static final String DATABASE = "CWEB2225Final";
	private static final String URL = "jdbc:mysql://" + HOST + "/" + DATABASE;
	
	public static void executeQuery(String query, String title)
	{
		try(Connection conn = DriverManager.getConnection(URL, USER, PASSWORD))
		{
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			// get metadata
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			
			//get column name
			String[] columnNames = new String[columnCount];
			int[] columnWidths = new int[columnCount];
			
			for(int i = 1; i <= columnCount; i++)
			{
				columnNames[i - 1] = rsmd.getColumnLabel(i);
				columnWidths[i - 1] = columnNames[i - 1].length();
			}
			
			// read data and adjust column widths
			List<String[]> rows = new ArrayList<>();
			
			while(rs.next())
			{
				String[] row = new String[columnCount];
				
				for(int i = 1; i <= columnCount; i++)
				{
					String value = rs.getString(i);
					
					if(value == null)
					{
						value = "NULL";
					}
					
					row[i - 1] = value;
					
					if(value.length() > columnWidths[i - 1])
					{
						columnWidths[i - 1] = value.length();
					}
				}
				
				rows.add(row);
			}
			
			// print the title
			System.out.println("\n" + title);
			
			// print table header
			printSeparator(columnWidths);
			printRow(columnNames, columnWidths);
			printSeparator(columnWidths);
			
			// print data rows
			for(String[] row : rows)
			{
				printRow(row, columnWidths);
				printSeparator(columnWidths);
			}
			
			rs.close();
			stmt.close();
		}
		catch (SQLException e) 
		{
			System.out.println("Error: " + e.getMessage());
		}
	}
	
	private static void printSeparator(int[] columnWidths)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("+");
		
		for(int width : columnWidths)
		{
			for(int i = 0; i < width + 2; i++)
			{
				sb.append("-");
			}
			
			sb.append("+");
		}
		
		System.out.println(sb.toString());
	}
	
	private static void printRow(String[] row, int[] columnWidths)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("|");
		
		for(int i = 0; i < row.length; i++)
		{
			String format = "%-" + columnWidths[i] + "s";
			sb.append(String.format(format, row[i]));
			sb.append("  |");
		}
		
		System.out.println(sb.toString());
	}

	public static void main(String[] args) {
		
		// define queries
		String query_1 = "SELECT Customers.name, Payments.amount, Payments.payment_date from Customers INNER JOIN Orders ON Customers.customer_id = Orders.customer_id INNER JOIN Payments ON Orders.order_id = Payments.order_id;";
		String query_2 = "SELECT * FROM Customers WHERE registration_date BETWEEN '2024-01-01' AND '2024-12-31';";
		String query_3 = "SELECT Orders.order_id, Orders.order_date, Orders.total_amount, Payments.payment_method, Payments.amount from Orders INNER JOIN Payments ON Orders.order_id = Payments.order_id;";
		String query_4 = "SELECT product_name, price FROM Products WHERE price BETWEEN 10 AND 100;";
		String query_5 = "SELECT Products.product_name, Categories.category_name FROM Products INNER JOIN Categories ON Products.category_id = Categories.category_id;";
		String query_6 = "SELECT SUM(stock_quantity) AS total_stock FROM Products;";
		String query_7 = "SELECT DISTINCT Categories.category_name, SUM(Products.stock_quantity * Products.price) AS total_stock_value FROM Products INNER JOIN Categories ON Products.category_id = Categories.category_id GROUP BY Products.category_id ORDER BY total_stock_value DESC;";
		String query_8 = "SELECT DISTINCT Categories.category_name, 2024 as year, SUM(OrderDetails.quantity * OrderDetails.price_at_purchase) AS annual_revenue FROM OrderDetails INNER JOIN Products ON OrderDetails.product_id = Products.product_id INNER JOIN Categories ON Products.category_id = Categories.category_id INNER JOIN Orders ON OrderDetails.order_id = Orders.order_id WHERE Orders.order_date BETWEEN '2024-01-01' AND '2024-12-31' GROUP BY Categories.category_name ORDER BY Categories.category_name;";
		String query_9 = "SELECT Customers.customer_id, Customers.name, SUM(Orders.total_amount) AS total_spending, COUNT(Orders.customer_id) AS total_orders, ROUND(AVG(Orders.total_amount), 2) AS avg_order_value, ROW_NUMBER() OVER ( ORDER BY SUM(Orders.total_amount) DESC) loyality_rank FROM Customers INNER JOIN Orders ON Customers.customer_id = Orders.customer_id GROUP BY Customers.customer_id ORDER BY total_spending DESC;";
		String query_10 = "SELECT Customers.customer_id, Customers.name, Orders.order_date AS last_order_date, DATEDIFF(CURRENT_DATE(), Orders.order_date) AS days_since_last_order FROM Customers INNER JOIN Orders ON Customers.customer_id = Orders.order_id WHERE Orders.order_date < '2024-05-15' ORDER BY days_since_last_order DESC;";
		String query_11 = "SELECT product_name, stock_quantity, 60 AS stock_needed, IF((stock_quantity < 60), (60-stock_quantity), 0) AS reorder_quantity FROM Products;";
		String query_12 = "SELECT DISTINCT (SELECT Products.product_name FROM Products INNER JOIN OrderDetails ON Products.product_id = OrderDetails.product_id WHERE OrderDetails.order_id IN (SELECT OrderDetails.order_id FROM OrderDetails GROUP BY OrderDetails.order_id HAVING COUNT(*) > 1) LIMIT 1) AS product1, (SELECT Products.product_name FROM Products INNER JOIN OrderDetails ON Products.product_id = OrderDetails.product_id WHERE OrderDetails.order_id IN (SELECT OrderDetails.order_id FROM OrderDetails GROUP BY OrderDetails.order_id HAVING COUNT(*) > 1) LIMIT 1, 1) AS product2, (SELECT OrderDetails.order_id FROM OrderDetails GROUP BY OrderDetails.order_id HAVING COUNT(*) > 1) AS times_bought_together FROM Products INNER JOIN OrderDetails ON Products.product_id = OrderDetails.product_id;";
		String query_13 = "SELECT product_name, stock_quantity, 1 AS avg_daily_sales, 7 AS reorder_point, IF((1 * 7) > stock_quantity, 'Insufficent Stock', 'Sufficient Stock') AS stock_status FROM Products;";
		String query_14 = "SELECT Suppliers.supplier_name, Categories.category_name, SUM(OrderDetails.price_at_purchase) AS total_revenue FROM Suppliers INNER JOIN Products ON Suppliers.supplier_id = Products.supplier_id INNER JOIN Categories ON Products.category_id = Categories.category_id INNER JOIN OrderDetails ON Products.product_id = OrderDetails.product_id GROUP BY Categories.category_name, Suppliers.supplier_name;";
		String query_15 = "SELECT Customers.name AS customer1, t.s as customer2, Products.product_name FROM Customers INNER JOIN Orders ON Customers.customer_id = Orders.customer_id INNER JOIN OrderDetails ON Orders.order_id = OrderDetails.order_id INNER JOIN Products ON OrderDetails.product_id = Products.product_id CROSS JOIN (SELECT Customers.name AS s FROM Customers) t  WHERE NOT Customers.name = 'Alice Brown' LIMIT 10;";
		String query_16 = "SELECT Categories.category_name, ROUND(SUM(price_at_purchase) * 100 / (SELECT SUM(price_at_purchase) AS s FROM OrderDetails), 2) AS percentage_contribution FROM OrderDetails INNER JOIN Products ON Products.product_id = OrderDetails.product_id INNER JOIN Categories ON Products.category_id = Categories.category_id GROUP BY Categories.category_name ORDER BY percentage_contribution DESC;";
		String query_17 = "SELECT DISTINCT payment_method, COUNT(payment_method) AS usage_count FROM Payments GROUP BY payment_method ORDER BY usage_count DESC;";
		String query_18 = "SELECT Products.product_name, Categories.category_name, COUNT(OrderDetails.product_id) AS total_quantity_sold, SUM(OrderDetails.price_at_purchase) AS total_revenue FROM OrderDetails INNER JOIN Products ON Products.product_id = OrderDetails.product_id INNER JOIN Categories ON Products.category_id = Categories.category_id GROUP BY OrderDetails.product_id ORDER BY total_revenue DESC;";
		String query_19 = "SELECT Suppliers.supplier_name, Products.product_name, Products.price FROM Suppliers INNER JOIN Products ON Suppliers.supplier_id = Products.supplier_id ORDER BY Products.price LIMIT 1;";
		String query_20 = "SELECT Categories.category_name, SUM(OrderDetails.price_at_purchase) AS total_revenue FROM Products INNER JOIN Categories ON Products.category_id = Categories.category_id INNER JOIN OrderDetails ON Products.product_id = OrderDetails.product_id GROUP BY Categories.category_name ORDER BY total_revenue DESC LIMIT 1;";
		
		// execute query
		executeQuery(query_1, "Query #1: List payments by customers in alphabetical order");
		executeQuery(query_2, "Query #2: Find customers who registered within the past year");
		executeQuery(query_3, "Query #3: Display orders along with payment details");
		executeQuery(query_4, "Query #4: List products with a price between $10 and $100");
		executeQuery(query_5, "Query #5: Show products along with their category names");
		executeQuery(query_6, "Query #6: Calculate the total quantity of all products in stock");
		executeQuery(query_7, "Query #7: Calculate the total value of unsold stock for each category");
		executeQuery(query_8, "Query #8: Analyze annual revenue for each product category over the past 1 years");
		executeQuery(query_9, "Query #9: Rank customers based on total spending, average order value, and number of orders");
		executeQuery(query_10, "Query #10: Identify customers who haven't placed orders in the last 6 months and are at risk of churn");
		executeQuery(query_11, "Query #11: Calculate how much stock needs to be reordered to meet demand for the next 60 days");
		executeQuery(query_12, "Query #12: Identify product pairs frequently purchased together in the same order");
		executeQuery(query_13, "Query #13: Calculate the reorder point for each product based on sales trends and lead time (e.g., 7 days)");
		executeQuery(query_14, "Query #14: Determine how much revenue each supplier contributes to a specific product category");
		executeQuery(query_15, "Query #15: Find 10 customers who placed orders for the same products");
		executeQuery(query_16, "Query #16: Show each category's percentage contribution to the overall sales revenue");
		executeQuery(query_17, "Query #17: Determine the most popular payment method based on the number of payments");
		executeQuery(query_18, "Query #18: Create a sales report showing each product, its category, total quantity sold, and revenue generated");
		executeQuery(query_19, "Query #19: List suppliers who provide products with the minimum price in the database");
		executeQuery(query_20, "Query #20: Determine which category has generated the highest total revenue");

	}

}
