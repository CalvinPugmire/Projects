//A Note regarding 'using namespace std':
// The author of this code prefers to not have the line 'using namespace std;'
// Instead, you will see 'std::' in front of string, cout, and other identifiers
// You could put 'using namespace std;' at the top of your code and not need 'std::' in front of these words,
// but many people consider it better practice to not have 'using namespace std;'

#include <iostream>
#include <string>
using namespace std;

//TODO: Include your class files here
#include "ShoppingCart.h"
#include "ItemToPurchase.h"

std::string GetUserString(const std::string& prompt);
double GetUserDouble(const std::string& prompt);
int GetUserInt(const std::string& prompt);


int main() {
    std::string customerName = GetUserString("Enter Customer's Name: ");
    std::string todayDate = GetUserString("Enter Today's Date: ");

    ShoppingCart newCart(customerName, todayDate);

    std::string userMenuChoice = "none";
    bool continueMenuLoop = true;
    while (continueMenuLoop) {
        userMenuChoice = GetUserString("Enter option: ");

        if (userMenuChoice == "add") {
            std::string itemName = GetUserString("Enter the item name: ");
            std::string itemDescription = GetUserString("Enter the item description: ");
            double itemPrice = GetUserDouble("Enter the item price: ");
            int itemQuantity = GetUserInt("Enter the item quantity: ");

            ItemToPurchase newItem (itemName, itemDescription, itemPrice, itemQuantity);
            newCart.AddItem(newItem);
        }
        else if (userMenuChoice == "remove") {
            std::string nameOfItemToRemove = GetUserString("Enter name of the item to remove: ");

            newCart.RemoveItem(nameOfItemToRemove);
        }
        else if (userMenuChoice == "change") {
            std::string nameOfItemToChange = GetUserString("Enter the item name: ");
            int newItemQuantity = GetUserInt("Enter the new quantity: ");

            newCart.UpdateQuantity(nameOfItemToChange, newItemQuantity);
        }
        else if (userMenuChoice == "descriptions") {
            newCart.PrintCartDescription();
        }
        else if (userMenuChoice == "cart") {
            newCart.PrintCartTotal();
        }
        else if (userMenuChoice == "options") {
            std::cout << "MENU" << std::endl
                << "add - Add item to cart" << std::endl
                << "remove - Remove item from cart" << std::endl
                << "change - Change item quantity" << std::endl
                << "descriptions - Print the items' descriptions" << std::endl
                << "cart - Print the shopping cart" << std::endl
                << "options - Print the options menu" << std::endl
                << "quit - Quit" << std::endl << std::endl;
        }
        else if (userMenuChoice == "quit") {
            std::cout << "Goodbye." << std::endl;
            continueMenuLoop = false;
        }
        else {
            std::cout << "MENU" << std::endl
                << "add - Add item to cart" << std::endl
                << "remove - Remove item from cart" << std::endl
                << "change - Change item quantity" << std::endl
                << "descriptions - Print the items' descriptions" << std::endl
                << "cart - Print the shopping cart" << std::endl
                << "options - Print the options menu" << std::endl
                << "quit - Quit" << std::endl << std::endl;
        }
    }

    return 0;
}


std::string GetUserString(const std::string& prompt) {
    std::string userAnswer = "none";

    std::cout << prompt;
    std::getline(std::cin, userAnswer);
    std::cout << std::endl;
    return userAnswer;
}
double GetUserDouble(const std::string& prompt) {
    double userAnswer = 0.0;

    std::cout << prompt;
    std::cin >> userAnswer;
    std::cin.ignore();
    std::cout << std::endl;
    return userAnswer;
}
int GetUserInt(const std::string& prompt) {
    int userAnswer = 0;

    std::cout << prompt;
    std::cin >> userAnswer;
    std::cin.ignore();
    std::cout << std::endl;
    return userAnswer;
}