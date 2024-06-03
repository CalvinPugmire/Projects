#include "Parameter.h"

Parameter::Parameter(std::string inputParameter) {
    p = inputParameter;
}

std::string Parameter::GetP() {
    return p;
};

void Parameter::ToString() {
    std::cout << p;
}