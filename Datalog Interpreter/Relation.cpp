#include "Relation.h"
#include <map>

Relation::Relation() {

}

Relation::Relation(std::string inputName, Header inputHeader) {
    name = inputName;
    header = inputHeader;
}

Relation::Relation(std::string inputName, Header inputHeader, std::set<Tuple> inputTuples) {
    name = inputName;
    header = inputHeader;
    tuples = inputTuples;
}

void Relation::AddName(std::string inputName) {
    name = inputName;
}

void Relation::AddTuple(Tuple inputTuple) {
    tuples.insert(inputTuple);
}

std::string Relation::GetName() {
    return name;
}

Header Relation::GetHeader() {
    return header;
}

std::set<Tuple> Relation::GetTuples() {
    return tuples;
}

Relation Relation::Select(int index, std::string value) {
    std::set<Tuple> newSet;
    for (Tuple tuple : tuples) {
        if (tuple.GetAttributeValues().at(index) == value) {
            newSet.insert(tuple);
        }
    }
    Relation newRelation(name, header, newSet);
    return newRelation;
}

Relation Relation::Select(int index1, int index2) {
    std::set<Tuple> newSet;
    for (Tuple tuple : tuples) {
        if (tuple.GetAttributeValues().at(index1) == tuple.GetAttributeValues().at(index2)) {
            newSet.insert(tuple);
        }
    }
    Relation newRelation(name, header, newSet);
    return newRelation;
}

Relation Relation::Project(std::vector<int> indices) {
    std::vector<std::string> attributeNames;
    for (size_t i = 0; i < indices.size(); i++) {
        attributeNames.push_back(header.GetAttributeNames().at(indices.at(i)));
    }
    Header newHeader(attributeNames);
    std::set<Tuple> newSet;
    for (Tuple tuple : tuples) {
        std::vector<std::string> attributeValues;
        for (size_t i = 0; i < indices.size(); i++) {
            attributeValues.push_back(tuple.GetAttributeValues().at(indices.at(i)));
        }
        Tuple newTuple(attributeValues);
        newSet.insert(newTuple);
    }
    Relation newRelation(name, newHeader, newSet);
    return newRelation;
}

Relation Relation::Rename(std::vector<std::string> values) {
    std::vector<std::string> attributeNames;
    for (size_t i = 0; i < values.size(); i++) {
        attributeNames.push_back(values.at(i));
    }
    Header newHeader(attributeNames);
    Relation newRelation(name, newHeader, tuples);
    return newRelation;
}

void Relation::Onion(Relation* inputRelation) {
    for (const Tuple& inputTuple : inputRelation->GetTuples()) {
        if (tuples.insert(inputTuple).second) {
            std::cout << "  ";
            for (unsigned int i = 0; i < inputTuple.GetAttributeValues().size(); i++) {
                std::cout << header.GetAttributeNames().at(i) << "=" << inputTuple.GetAttributeValues().at(i);
                if (i < inputTuple.GetAttributeValues().size()-1) {
                    std::cout << ", ";
                }
            }
            std::cout << std::endl;
        }
    }
}

Relation Relation::Join(Relation* inputRelation) {
    std::vector<std::vector<unsigned int>> columnMatches;
    std::vector<std::string> attributeNames;
    for (unsigned int i = 0; i < header.GetAttributeNames().size(); i++) {
        attributeNames.push_back(header.GetAttributeNames().at(i));
        for (unsigned int j = 0; j < inputRelation->GetHeader().GetAttributeNames().size(); j++) {
            if (header.GetAttributeNames().at(i) == inputRelation->GetHeader().GetAttributeNames().at(j)) {
                columnMatches.push_back({i,j});
            }
        }
    }
    for (unsigned int i = 0; i < inputRelation->GetHeader().GetAttributeNames().size(); i++) {
        bool addName = true;
        for (unsigned int j = 0; j < attributeNames.size(); j++) {
            if (inputRelation->GetHeader().GetAttributeNames().at(i) == attributeNames.at(j)) {
                addName = false;
            }
        }
        if (addName) {
            attributeNames.push_back(inputRelation->GetHeader().GetAttributeNames().at(i));
        }
    }
    Header newHeader(attributeNames);

    std::vector<std::vector<unsigned int>> rowMatches;
    unsigned int i = 0;
    for (const Tuple& tuple : tuples) {
        unsigned int j = 0;
        for (const Tuple& inputTuple : inputRelation->GetTuples()) {
            bool rowMatch = true;
            if (!columnMatches.empty()) {
                for (unsigned int k = 0; k < columnMatches.size(); k++) {
                    if (!rowMatch || tuple.GetAttributeValues().at(columnMatches.at(k).at(0)) != inputTuple.GetAttributeValues().at(columnMatches.at(k).at(1))) {
                        rowMatch = false;
                    }
                }
                if (rowMatch) {
                    rowMatches.push_back({i,j});
                }
            } else {
                rowMatches.push_back({i,j});
            }
            j++;
        }
        i++;
    }

    std::set<Tuple> newSet;
    for (unsigned int i = 0; i < rowMatches.size(); i++) {
        std::vector<std::string> attributeValues;
        unsigned int j = 0;
        for (const Tuple& tuple : tuples) {
            if (j == rowMatches.at(i).at(0)) {
                for (unsigned int k = 0; k < tuple.GetAttributeValues().size(); k++) {
                    attributeValues.push_back(tuple.GetAttributeValues().at(k));
                }
            }
            j++;
        }
        j = 0;
        for (const Tuple& inputTuple : inputRelation->GetTuples()) {
            if (j == rowMatches.at(i).at(1)) {
                if (!columnMatches.empty()) {
                    for (unsigned int k = 0; k < inputTuple.GetAttributeValues().size(); k++) {
                        bool addValue = true;
                        for (unsigned int l = 0; l < columnMatches.size(); l++) {
                            if (k == columnMatches.at(l).at(1)) {
                                addValue = false;
                            }
                        }
                        if (addValue) {
                            attributeValues.push_back(inputTuple.GetAttributeValues().at(k));
                        }
                    }
                } else {
                    for (unsigned int k = 0; k < inputTuple.GetAttributeValues().size(); k++) {
                        attributeValues.push_back(inputTuple.GetAttributeValues().at(k));
                    }
                }
            }
            j++;
        }
        Tuple newTuple(attributeValues);
        newSet.insert(newTuple);
    }

    Relation newRelation(name, newHeader, newSet);
    return newRelation;
}
