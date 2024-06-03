#include "Tuple.h"

Tuple::Tuple(std::vector<std::string> inputAttributeValues) {
    attributeValues = inputAttributeValues;
}

std::vector<std::string> Tuple::GetAttributeValues() const {
    return attributeValues;
}