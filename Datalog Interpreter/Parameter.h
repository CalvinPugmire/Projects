#ifndef PARAMETER_H
#define PARAMETER_H
#include <iostream>


class Parameter
{
private:
    std::string p;

public:
    Parameter(std::string inputParameter);
    std::string GetP();
    void ToString();
};


#endif //PARAMETER_H
