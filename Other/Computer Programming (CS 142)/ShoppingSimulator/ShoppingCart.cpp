//
// Created by troop on 11/13/2020.
//

#include <iostream>
#include <iomanip>
using namespace std;

#include "ShoppingCart.h"

ShoppingCart::ShoppingCart () {
    customerName = "none";
    cartDate = "January 1, 2016";
}

ShoppingCart::ShoppingCart (string entryCustomer, string entryDate) {
    customerName = entryCustomer;
    cartDate = entryDate;
}

string ShoppingCart::GetCustomer() {
    return customerName;
}

string ShoppingCart::GetDate() {
    return cartDate;
}

void ShoppingCart::AddItem(ItemToPurchase itemSingle) {
    int isInList = 0;
    for (unsigned int i = 0; i < itemList.size(); i++) {
        if (itemList.at(i).GetName() == itemSingle.GetName()) {
            isInList = 1;
        }
    }
    if (isInList == 0){
        itemList.push_back(itemSingle);
    }
    else {
        cout << "Item is already found in the cart. It will not be added." << endl;
    }
}

void ShoppingCart::RemoveItem(string itemSingle) {
    int isInList = -1;
    for (unsigned int i = 0; i < itemList.size(); i++) {
        if (itemList.at(i).GetName() == itemSingle) {
            isInList = i;
        }
    }
    if (isInList != -1){
        itemList.erase(itemList.begin()+isInList);
    }
    else {
        cout << "Item not found in cart. It will not be removed." << endl;
    }
}

void ShoppingCart::UpdateQuantity(string itemSingle, int newQuantity) {
    int isInList = -1;
    for (unsigned int i = 0; i < itemList.size(); i++) {
        if (itemList.at(i).GetName() == itemSingle) {
            isInList = i;
        }
    }
    if (isInList != -1){
        itemList.at(isInList).SetQuantity(newQuantity);
    }
    else {
        cout << "Item not found in cart. It will not be modified." << endl;
    }
}

int ShoppingCart::GetCartQuantity() {
    int totalQuantity = 0;
    for (unsigned int i = 0; i < itemList.size(); i++) {
        totalQuantity = totalQuantity + itemList.at(i).GetQuantity();
    }
    return totalQuantity;
}

double ShoppingCart::GetCartCost() {
    double totalCost = 0;
    for (unsigned int i = 0; i < itemList.size(); i++) {
        totalCost = totalCost + itemList.at(i).GetPrice();
    }
    return totalCost;
}

void ShoppingCart::PrintCartDescription() {
    cout << customerName << "'s Shopping Cart - " << cartDate << endl;
    if (itemList.size() > 0) {
        cout << endl;
        cout << "Item Descriptions" << endl;
        for (unsigned int i = 0; i < itemList.size(); i++) {
            cout << itemList.at(i).GetName() << ": " << itemList.at(i).GetDescription() << endl;
        }
        cout << endl;
    }
    else {
        cout <<  "Shopping cart is empty." << endl;
        cout << endl;
    }
}

void ShoppingCart::PrintCartTotal() {
    cout << customerName << "'s Shopping Cart - " << cartDate << endl;
    if (itemList.size() > 0) {
        int totalQuantity = 0;
        for (unsigned int i = 0; i < itemList.size(); i++) {
            totalQuantity = totalQuantity + itemList.at(i).GetQuantity();
        }
        double totalPrice = 0;
        cout << "Number of Items: " << totalQuantity << endl;
        cout << endl;
        for (unsigned int i = 0; i < itemList.size(); i++) {
            cout << itemList.at(i).GetName() << " " << itemList.at(i).GetQuantity() << " @ $" << fixed << showpoint << setprecision(2) << itemList.at(i).GetPrice() << " = $" << fixed << showpoint << setprecision(2) << (itemList.at(i).GetQuantity() * itemList.at(i).GetPrice()) << endl;
            totalPrice = totalPrice + (itemList.at(i).GetPrice() * itemList.at(i).GetQuantity());
        }
        cout << endl;
        cout << "Total: $" << fixed << showpoint << setprecision(2) << totalPrice << endl;
        cout << endl;
    }
    else {
        cout <<  "Shopping cart is empty." << endl;
        cout << endl;
    }
}
