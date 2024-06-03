#ifndef PREDICATE_H
#define PREDICATE_H
#include <vector>
#include "Parameter.h"


class Predicate
{
private:
    std::string type;
    std::string id;
    std::vector<Parameter*> parameters;

public:
    Predicate(std::string inputType, std::string inputID);
    ~Predicate();
    void AddParameter(std::string inputParameterString);
    std::string GetType();
    std::string GetID();
    std::vector<std::string> GetParameters();
    void ToString();
};


#endif //PREDICATE_H
