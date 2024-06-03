#include <iostream>
#include <iomanip>
#include <fstream>
using namespace std;



int main () {
    cout << "Hotplate simulator" << endl;
    cout << endl;



    cout << "Printing the initial plate values..." << endl;

    double initialValues[10][10];

    for (int i = 0; i < 10; ++i) {
        for (int j = 0; j < 10; ++j) {
            if ((i == 0 && j != 0 && j != 9) || (i == 9 && j != 0 && j != 9)) {
                initialValues[i][j]= 100;
            }
            else {
                initialValues[i][j]= 0;
            }
        }
    }

    for (int i = 0; i < 10; ++i) {
        for (int j = 0; j < 10; ++j) {
            if (j < 9) {
                cout << setw(9)<< fixed << setprecision(3)<<initialValues[i][j] << ",";
            }
            else {
                cout << setw(9)<< fixed << setprecision(3)<<initialValues[i][j];
            }
        }
        cout << endl;
    }
    cout << endl;



    cout << "Printing plate after one iteration..." << endl;

    double newValues[10][10];

    for (int i = 0; i < 10; ++i) {
        for (int j = 0; j < 10; ++j) {
            if (i > 0 && i < 9 && j > 0 && j < 9) {
                newValues[i][j] = (initialValues[i-1][j] + initialValues[i][j-1] + initialValues[i+1][j] + initialValues[i][j+1])/4;
            }
            else {
                newValues[i][j]= initialValues[i][j];
            }
        }
    }


    for (int i = 0; i < 10; ++i) {
        for (int j = 0; j < 10; ++j) {
            if (j < 9) {
                cout << setw(9) << fixed << setprecision(3) << newValues[i][j] << ",";
            }
            else {
                cout << setw(9) << fixed << setprecision(3) << newValues[i][j];
            }
        }
        cout << endl;
    }
    cout << endl;



    cout << "Printing final plate... " << endl;

    bool isLess = true;

    while (isLess) {
        for (int i = 0; i < 10; ++i) {
            for (int j = 0; j < 10; ++j) {
                initialValues[i][j] = newValues[i][j];
            }
        }

        for (int i = 0; i < 10; ++i) {
            for (int j = 0; j < 10; ++j) {
                if (i > 0 && i < 9 && j > 0 && j < 9) {
                    newValues[i][j] = (initialValues[i-1][j] + initialValues[i][j-1] + initialValues[i+1][j] + initialValues[i][j+1])/4;
                }
                else {
                    newValues[i][j]= initialValues[i][j];
                }
            }
        }

        for (int i = 0; i < 10; ++i) {
            for (int j = 0; j < 10; ++j) {
                if (newValues[i][j] - initialValues[i][j] > 0.1) {
                    isLess = false;
                }
            }
        }
    }

    for (int i = 0; i < 10; ++i) {
        for (int j = 0; j < 10; ++j) {
            if (j < 9) {
                cout << setw(9) << fixed << setprecision(3) << newValues[i][j] << ",";
            }
            else {
                cout << setw(9) << fixed << setprecision(3) << newValues[i][j];
            }
        }
        cout << endl;
    }
    cout << endl;



    cout << "Writing final plate to \"Hotplate.csv\"..." << endl;

    ofstream Hotplate;
    Hotplate.open( "Hotplate.csv");

    if(!Hotplate.is_open()) {
        cout << "Could not open Hotplate.csv" << endl;
        return 1;
    }

    for (int i = 0; i < 10; ++i) {
        for (int j = 0; j < 10; ++j) {
            if (j < 9) {
                Hotplate << setw(9) << fixed << setprecision(3) << newValues[i][j] << ",";
            }
            else {
                Hotplate << setw(9) << fixed << setprecision(3) << newValues[i][j];
            }
        }
        Hotplate << endl;
    }
    Hotplate.close();
    cout << endl;



    cout << "Printing input plate after 3 updates..." << endl;

    ifstream Inputplate;
    Inputplate.open( "Inputplate.txt");

    if(!Inputplate.is_open()) {
        cout << "Could not open Inputplate.txt" << endl;
        return 1;
    }

    for (int i = 0; i < 10; ++i) {
        for (int j = 0; j < 10; ++j) {
            Inputplate >> newValues[i][j];
        }
    }
    Inputplate.close();

    for (int o = 0; o < 3; o++) {
        for (int i = 0; i < 10; ++i) {
            for (int j = 0; j < 10; ++j) {
                initialValues[i][j] = newValues[i][j];
            }
        }

        for (int i = 0; i < 10; ++i) {
            for (int j = 0; j < 10; ++j) {
                if (i > 0 && i < 9 && j > 0 && j < 9) {
                    newValues[i][j] = (initialValues[i-1][j] + initialValues[i][j-1] + initialValues[i+1][j] + initialValues[i][j+1])/4;
                }
                else {
                    newValues[i][j]= initialValues[i][j];
                }
            }
        }

        for (int i = 0; i < 10; ++i) {
            for (int j = 0; j < 10; ++j) {
                if (newValues[i][j] - initialValues[i][j] > 0.1) {
                    isLess = false;
                }
            }
        }
    }

    for (int i = 0; i < 10; ++i) {
        for (int j = 0; j < 10; ++j) {
            if (j < 9) {
                cout << setw(9) << fixed << setprecision(3) << newValues[i][j] << ",";
            }
            else {
                cout << setw(9) << fixed << setprecision(3) << newValues[i][j];
            }
        }
        cout << endl;
    }
    cout << endl;



    return 0;
}