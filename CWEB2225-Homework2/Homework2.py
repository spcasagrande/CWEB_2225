from pymongo import MongoClient
import re
import uuid
import datetime

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

def create_document(collection):
    print("")
    ids = str(uuid.uuid4())
    created_at = datetime.datetime.now()
    inputs = get_valid_input()

    document = {"id" : ids, "name" : inputs[0], "email" : inputs[1], "phone" : inputs[2], "address" : inputs[3], "created_at" : created_at}
    collection.insert_one(document)
    print("Document inserted successfully\n")

def read_document(collection):
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
            document = collection.find({"name": name})
            break
        elif choice == "2":
            while True:
                email = input("Enter email: ")
                if validate_email(email):
                    break
                else:
                    print("Please enter a valid email format")
            document = collection.find({"email": email})
            break
        elif choice == "3":
            while True:
                phone = input("Enter phone number (digits only): ")
                if validate_phone(phone):
                    break
                else:
                    print("Please enter a valid phone number")
            document = collection.find({"phone": phone})
            break
        elif choice == "4":
            while True:
                address = input("Enter address: ")
                if validate_address(address):
                    break
                else:
                    print("Address cannot be empty")
            document = collection.find({"address": address})
            break
        elif choice == "5":
            document = list(collection.find())
            break
        else:
            print("Invalid choice. Please choose one of the listed options\n")

    # print header
    print("")
    print(f"{'ID':<45}{'Name':<15} {'Email':<20} {'Phone':<15} {'Address':<20} {'Created At':<26}")
    print("-" * 150)

    # print rows
    for doc in document:
        ids = doc.get('id', 'N/A')
        name = doc.get('name', 'N/A')
        email = doc.get('email', 'N/A')
        phone = doc.get('phone', 'N/A')
        address = doc.get('address', 'N/A')
        created_at = str(doc.get('created_at', 'N/A'))
        print(f"{ids:<45}{name:<15} {email:<20} {phone:<15} {address:<20} {created_at:<26}")

    print("")

def update_document(collection):
    while True:
        choice = input("Do you wish to search for the ID of the document you wish to update? (y/n): ")
        if choice.lower() == "y":
            read_document(collection)
            break
        elif choice.lower() == "n":
            break
        else:
            print("Please enter a valid choice\n")

    while True:
        ids = input("\nEnter ID of document to be updated: ")
        if ids:
            break
        else:
            print("ID cannot be empty")

    document = collection.find_one({"id" : ids})

    if not document:
        print("No document found with that ID\n")
        return
    else:
        print("Please enter new values for the document")
        inputs = get_valid_input()
        newDocument = {"$set": {"name" : inputs[0], "email" : inputs[1], "phone" : inputs[2], "address" : inputs[3]}}
        filters = {"id": ids}
        collection.update_one(filters, newDocument)
        print("Document updated successfully\n")

def delete_document(collection):
    while True:
        choice = input("Do you wish to search for the ID of the document you wish to delete? (y/n): ")
        if choice.lower() == "y":
            read_document(collection)
            break
        elif choice.lower() == "n":
            break
        else:
            print("Please enter a valid choice\n")

    while True:
        ids = input("\nEnter ID of document to be deleted: ")
        if ids:
            break
        else:
            print("ID cannot be empty")

    document = collection.find_one({"id" : ids})

    if not document:
        print("No document found with that ID\n")
        return
    else:
        result = collection.delete_one({"id" : ids})

    # telling the user if it worked
    if result.deleted_count > 0:
        print("Document deleted successfully\n")
    else:
        print("Document was not deleted successfully\n")

def main():
    client = MongoClient("mongodb+srv://admin:123password@cluster0.la4np.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0")

    database = client["Homework2"]
    collection = database["testCollection"]

    while True:
        print("Choose an operation: ")
        print("1. Create a new document")
        print("2. Read documents")
        print("3. Update a document")
        print("4. Delete a document")
        print("5. Exit\n")

        choice = input("Enter your choice: ")

        if choice == "1":
            create_document(collection)
        elif choice == "2":
            read_document(collection)
        elif choice == "3":
            update_document(collection)
        elif choice == "4":
            delete_document(collection)
        elif choice == "5":
            break
        else:
            print("Invalid choice. Try again\n")

    client.close()

main()