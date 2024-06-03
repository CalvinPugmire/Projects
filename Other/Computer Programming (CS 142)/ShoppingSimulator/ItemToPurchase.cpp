//
// Created by troop on 11/11/2020.
//

#include <iostream>
#include <iomanip>
using namespace std;

#include "ItemToPurchase.h"

ItemToPurchase::ItemToPurchase () {
    itemName = "none";
    itemDescription = "none";
    itemPrice = 0.0;
    itemQuantity = 0;
}

ItemToPurchase::ItemToPurchase (string entryName, string entryDescription, double entryPrice, int entryQuantity) {
    itemName = std::move(entryName);
    itemDescription = std::move(entryDescription);
    itemPrice = entryPrice;
    itemQuantity = entryQuantity;
}

void ItemToPurchase::SetName(string entryName) {
    itemName = std::move(entryName);
}

string ItemToPurchase::GetName() const {
    return itemName;
}

void ItemToPurchase::SetDescription(string entryDescription) {
    itemDescription = std::move(entryDescription);
}

string ItemToPurchase::GetDescription() const {
    return itemDescription;
}

void ItemToPurchase::SetPrice(double entryPrice) {
    itemPrice = entryPrice;
}

double ItemToPurchase::GetPrice() const {
    return itemPrice;
}

void ItemToPurchase::SetQuantity(int entryQuantity) {
    itemQuantity = entryQuantity;
}

int ItemToPurchase::GetQuantity () const {
    return itemQuantity;
}

void ItemToPurchase::PrintCost() const {
    cout << itemName << " " << itemQuantity << " " << " @ $" << fixed << showpoint << setprecision(2) << itemPrice << " = $" << fixed << showpoint << setprecision(2) << (itemQuantity * itemPrice) << endl;
}

void ItemToPurchase::PrintDescription() const {
    cout << itemName << ": " << itemDescription << endl;
}