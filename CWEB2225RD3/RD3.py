import mysql.connector
from mysql.connector import Error
import re

#MySQL connection
connection_params = {
    'host': 'localhost',
    'database': 'new_database1',
    'user': "new_username1",
    'password': 'new_password1'
}

def validate_name(name):
    if name:
        return True
    else:
        return False

def validate_email(email):
    regex = re.compile(r'([A-Za-z0-9]+[.-_])*[A-Za-z0-9]+@[A-Za-z0-9-]+(\.[A-Z|a-z]{2,})+')
    if re.fullmatch(regex, email):
        return True
    else:
        return False

def validate_phone(phone):
    if phone.isdigit() and (17 > len(phone) > 9):
        return True
    else:
        return False

def validate_address(address):
    if address:
        return True
    else:
        return False

def get_valid_input():
    # get the name
    while True:
        name = input("\nEnter name: ")
        if validate_name(name):
            break
        else:
            print("Name cannot be empty")

    # get the email
    while True:
        email = input("\nEnter email: ")
        if validate_email(email):
            break
        else:
            print("Please enter a valid email")

    # get the phone
    while True:
        phone = input("\nEnter phone number (digits only): ")
        if validate_phone(phone):
            break
        else:
            print("Please enter a valid phone number")

    # get the address
    while True:
        address = input("\nEnter address: ")
        if validate_address(address):
            break
        else:
            print("Address cannot be empty")

    return [name, email, phone, address]

def ensure_table_exists():
    try:
        with mysql.connector.connect(**connection_params) as conn:
            cursor = conn.cursor()
            create_table_query = """ 
            CREATE TABLE IF NOT EXISTS RD3(
			id INT AUTO_INCREMENT PRIMARY KEY,
			name VARCHAR(50) NOT NULL,
			email VARCHAR(100) NOT NULL,
			phone VARCHAR(20) NOT NULL,
			address VARCHAR(255) NOT NULL,
			created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
			);
			"""
            cursor.execute(create_table_query)
            conn.commit()
            #print("Table ensured to exist")
    except Error as e:
        print("Error: ", e)

def create_record():
    print("")
    inputs = get_valid_input()

    try:
        with mysql.connector.connect(**connection_params) as conn:
            cursor = conn.cursor()
            query = "INSERT INTO RD3 (name, email, phone, address) VALUES (%s, %s, %s, %s)"
            cursor.execute(query, (inputs[0], inputs[1], inputs[2], inputs[3]))
            conn.commit()
            print("\nRecord successfully inserted\n")

    except Error as e:
        print("Error: ", e)

def read_record():
    while True:
        print("How do you wish to search?")
        print("1. Name")
        print("2. Email")
        print("3. Phone")
        print("4. Address")
        print("5. Print all documents")
        choice = input("Answer: ")

        if choice == "1":
            while True:
                name = input("Enter name: ")
                if validate_name(name):
                    break
                else:
                    print("Name cannot be empty")
            query = "SELECT * FROM RD3 WHERE name='" + name + "'"
            break
        elif choice == "2":
            while True:
                email = input("Enter email: ")
                if validate_email(email):
                    break
                else:
                    print("Please enter a valid email format")
            query = "SELECT * FROM RD3 WHERE email='" + email + "'"
            break
        elif choice == "3":
            while True:
                phone = input("Enter phone number (digits only): ")
                if validate_phone(phone):
                    break
                else:
                    print("Please enter a valid phone number")
            query = "SELECT * FROM RD3 WHERE phone='" + phone + "'"
            break
        elif choice == "4":
            while True:
                address = input("Enter address: ")
                if validate_address(address):
                    break
                else:
                    print("Address cannot be empty")
            query = "SELECT * FROM RD3 WHERE address='" + address + "'"
            break
        elif choice == "5":
            query = "SELECT * FROM RD3"
            break
        else:
            print("Invalid choice. Please choose one of the listed options\n")

    try:
        with mysql.connector.connect(**connection_params) as conn:
            cursor = conn.cursor()
            cursor.execute(query)
            rows = cursor.fetchall()

            # print header
            print("")
            print(f"{'ID':<5}{'Name':<15} {'Email':<20} {'Phone':<15} {'Address':<20} {'Created At':<26}")
            print("-" * 100)

            # print rows
            for row in rows:
                created_at = row[5].strftime("%Y-%m-%d %H:%M:%S")
                print(f"{row[0]:<4} {row[1]:<15} {row[2]:<20} {row[3]:<15} {row[4]:<20} {created_at:<26}")

            print()

    except Error as e:
        print("Error: ", e)

def update_record():
    while True:
        choice = input("Do you wish to search for the ID of the document you wish to update? (y/n): ")
        if choice.lower() == "y":
            read_record()
            break
        elif choice.lower() == "n":
            break
        else:
            print("Please enter a valid choice\n")

    while True:
        try:
            record_id = int(input("Enter the ID of the record to update: ").strip())
            if record_id > 0:
                break
            else:
                print("Please enter a valid ID")
        except ValueError:
            print("Please enter a valid ID")

    try:
        with mysql.connector.connect(**connection_params) as conn:
            cursor = conn.cursor()
            select_query = "SELECT * FROM RD3 WHERE id = %s"
            cursor.execute(select_query, (record_id,))
            rows = cursor.fetchall()

            if not rows:
               print("No record found for ID: ", record_id)
               return
            else:
                print("Please enter new values for the document")
                inputs = get_valid_input()

                try:
                    update_query = "UPDATE RD3 SET name = %s, email = %s, phone = %s, address = %s WHERE id = %s"
                    cursor.execute(update_query, (inputs[0], inputs[1], inputs[2], inputs[3], record_id))
                    conn.commit()

                    if cursor.rowcount > 0:
                        print("\nRecord successfully updated\n")
                    else:
                        print("\nNo records updated\n")

                except Error as e:
                    print("Error: ", e)

    except Error as e:
        print("Error: ", e)

def delete_record():
    while True:
        choice = input("Do you wish to search for the ID of the document you wish to delete? (y/n): ")
        if choice.lower() == "y":
            read_record()
            break
        elif choice.lower() == "n":
            break
        else:
            print("Please enter a valid choice\n")

    while True:
        try:
            record_id = int(input("Enter the ID of the record to delete: ").strip())
            if record_id > 0:
                break
            else:
                print("Please enter a valid ID")
        except ValueError:
            print("Please enter a valid ID")

    try:
        with mysql.connector.connect(**connection_params) as conn:
            cursor = conn.cursor()
            select_query = "SELECT * FROM RD3 WHERE id = %s"
            cursor.execute(select_query, (record_id,))
            rows = cursor.fetchall()

            if not rows:
               print("No record found for ID: ", record_id)
               return
            else:
                cursor = conn.cursor()
                select_query = "SELECT * FROM RD3 WHERE id = %s"
                cursor.execute(select_query, (record_id,))
                rows = cursor.fetchall()

                if not rows:
                    print("No record found for ID: ", record_id)
                    return

                try:
                    delete_query = "DELETE FROM RD3 WHERE id = %s"
                    cursor.execute(delete_query, (record_id,))
                    conn.commit()

                    if cursor.rowcount > 0:
                        print("\nRecord successfully deleted\n")
                    else:
                        print("\nNo records deleted\n")

                except Error as e:
                    print("Error: ", e)

    except Error as e:
        print("Error: ", e)

def main():
    ensure_table_exists()

    while True:
        print("Choose an operation: ")
        print("1. Create a record")
        print("2. Read all records")
        print("3. Update a record")
        print("4. Delete a record")
        print("5. Exit\n")

        choice = input("Enter your choice: ")

        if choice == "1":
            create_record()
        elif choice == "2":
            read_record()
        elif choice == "3":
            update_record()
        elif choice == "4":
            delete_record()
        elif choice == "5":
            print("\nExiting the program...")
            break
        else:
            print("Invalid choice. Try again\n")

main()