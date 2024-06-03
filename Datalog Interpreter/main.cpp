#include <fstream>
#include <vector>
#include "Database.h"
#include "Interpreter.h"
#include "Lexer.h"

using namespace std;

int main(int argc, char** argv) {
    try {
        // open file
        std::string input;
        ifstream passedInFile;
        passedInFile.open(argv[1]);
        while(!passedInFile.eof()) {
            std::string s;
            getline(passedInFile, s);
            input += s + "\n";
        }

        Lexer* lexer = new Lexer(input);

        std::vector<Token*> lexerTokens = lexer->GetTokens();

        std::vector<Token*> parserTokens;
        for (unsigned int i = 0; i < lexerTokens.size(); i++) {
            if (lexerTokens.at(i)->GetType() != "COMMENT") {
                parserTokens.push_back(lexerTokens.at(i));
            }
        }

        Parser* parser = new Parser(parserTokens);

        DataLogProgram* datalogprogram = new DataLogProgram(parser);

        Database database = Database();

        [[maybe_unused]] Interpreter interpreter = Interpreter(datalogprogram, database);

        delete lexer;
        delete parser;
        delete datalogprogram;
    }
    catch (int exception) {
        if (exception == 1) {
        }
    }

    return 0;
}