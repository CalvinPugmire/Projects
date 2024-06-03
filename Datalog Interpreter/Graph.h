#ifndef GRAPH_H
#define GRAPH_H
#include "DataLogProgram.h"
#include <map>
#include <set>
#include <vector>

class Graph {
private:
    std::map<int,std::set<int>> dependencyGraph;
    std::map<int,std::set<int>> reverseDependencyGraph;
    std::map<int,bool> visitedNodes;
    std::vector<int> postOrder;
    std::vector<std::set<int>> SCCs;
    std::set<int> SCC;
public:
    Graph();
    void BuildDG(std::vector<Rule*> inputRules);
    void BuildRDG(std::vector<Rule*> inputRules);
    void DFSForestPO();
    void DepthFirstSearchPO(int inputNode);
    void DFSForestSCC();
    void DepthFirstSearchSCC(int inputNode, int inputPos);
    std::vector<std::set<int>> GetSCCs();
    void PrintDG();
    void PrintRDG();
    void PrintPO();
    void PrintSCCs();
    void PrintSCC(int inputSCCIndex);
};


#endif //GRAPH_H
