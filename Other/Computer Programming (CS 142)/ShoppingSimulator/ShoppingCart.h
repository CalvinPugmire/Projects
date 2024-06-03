//
// Created by troop on 11/13/2020.
//

#ifndef SHOPPINGCART_H
#define SHOPPINGCART_H

#include <vector>
using namespace std;

#include "ItemToPurchase.h"


class ShoppingCart {
public:
    ShoppingCart();
    ShoppingCart(string, string);
    string GetCustomer();
    string GetDate();
    void AddItem(ItemToPurchase);
    void RemoveItem(string);
    void UpdateQuantity(string, int);
    int GetCartQuantity();
    double GetCartCost();
    void PrintCartDescription();
    void PrintCartTotal();
private:
    string customerName = "none";
    string cartDate = "none";
    vector <ItemToPurchase> itemList;
};


#endif //SHOPPINGCART_H
