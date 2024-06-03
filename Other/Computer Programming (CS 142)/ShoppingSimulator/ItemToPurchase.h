#include <utility>

//
// Created by troop on 11/11/2020.
//

#ifndef ITEMTOPURCHASE_H
#define ITEMTOPURCHASE_H

#include <vector>
using namespace std;

class ItemToPurchase {
public:
    ItemToPurchase();
    ItemToPurchase(string entryName, string entryDescription, double entryPrice, int entryQuantity);
    void SetName(string entryName);
    string GetName() const;
    void SetDescription(string entryDescription);
    string GetDescription() const;
    void SetPrice(double entryPrice);
    double GetPrice() const;
    void SetQuantity(int entryQuantity);
    int GetQuantity() const;
    void PrintCost() const;
    void PrintDescription() const;
private:
    string itemName = "none";
    string itemDescription = "none";
    double itemPrice = 0.0;
    int itemQuantity = 0;
};


#endif //ITEMTOPURCHASE_H
