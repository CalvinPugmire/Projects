#include "Header.h"

Header::Header(std::vector <std::string> inputAttributeNames) {
    attributeNames = inputAttributeNames;
}

std::vector<std::string> Header::GetAttributeNames() const {
    return attributeNames;
}