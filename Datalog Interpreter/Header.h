#ifndef HEADER_H
#define HEADER_H
#include <iostream>
#include <vector>


class Header {
private:
    std::vector <std::string> attributeNames;
public:
    Header(){};
    Header(std::vector<std::string> inputAttributeNames);

    std::vector<std::string> GetAttributeNames() const;
};


#endif //HEADER_H
