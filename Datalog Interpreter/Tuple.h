#ifndef TUPLE_H
#define TUPLE_H
#include <iostream>
#include <vector>


class Tuple {
private:
    std::vector <std::string> attributeValues;

public:
    Tuple(){};
    Tuple (std::vector<std::string> inputAttributeValues);

    std::vector <std::string> GetAttributeValues () const;

    bool operator< (const Tuple& inputTuple) const {
        return this->attributeValues < inputTuple.GetAttributeValues();
    };
};


#endif //TUPLE_H
