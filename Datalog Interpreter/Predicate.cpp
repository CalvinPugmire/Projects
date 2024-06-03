#include "Predicate.h"

Predicate::Predicate(std::string inputType, std::string inputID) {
    type = inputType;
    id = inputID;
}

Predicate::~Predicate() {
    for (unsigned int i = 0; i < parameters.size(); i++) {
        delete parameters.at(i);
    }
    parameters.clear();
}

void Predicate::AddParameter(std::string inputParameterString) {
    Parameter* parameter = new Parameter(inputParameterString);
    parameters.push_back(parameter);
}

std::string Predicate::GetType() {
    return type;
}

std::string Predicate::GetID() {
    return id;
}

std::vector<std::string> Predicate::GetParameters() {
    std::vector<std::string> outputParameters;
    for (unsigned int i = 0; i < parameters.size(); i++) {
        outputParameters.push_back(parameters.at(i)->GetP());
    }
    return outputParameters;
}

void Predicate::ToString() {
    if (type == "SCHEME" || type == "RULE") {
        std::cout << id << "(";
        for (unsigned int i = 0; i < parameters.size(); i++) {
            parameters.at(i)->ToString();
            if (i < parameters.size()-1) {
                std::cout << ",";
            }
        }
        std::cout << ")";
    }
    else if (type == "FACT") {
        std::cout << id << "(";
        for (unsigned int i = 0; i < parameters.size(); i++) {
            parameters.at(i)->ToString();
            if (i < parameters.size()-1) {
                std::cout << ",";
            }
        }
        std::cout << ")" << ".";
    }
    else if (type == "QUERY") {
        std::cout << id << "(";
        for (unsigned int i = 0; i < parameters.size(); i++) {
            parameters.at(i)->ToString();
            if (i < parameters.size()-1) {
                std::cout << ",";
            }
        }
        std::cout << ")" << "?";
    }
}
