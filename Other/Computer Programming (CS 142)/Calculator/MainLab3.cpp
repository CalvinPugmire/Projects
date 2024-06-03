#include <iostream>
#include <iomanip>
#include <cmath>
using namespace std;

//place function prototypes here...
double CalcRectanglePerimeter(double height, double width);
void PrintRectanglePerimeter(double height, double width);
void DoubleRectanglePerimeter(double& height, double& width);
double CalcCircumferenceOfCircle(double radius);
double CalcAreaOfCircle (double radius);
double CalcVolumeOfSphere (double radius);
void Swap(double& valueA, double& valueB);
void Swap(int& valueA, int& valueB);

const double PI = 3.14;


//main function
int main(){
    cout << "Getting this line to print earns you points!\n";

    double height = 0;
    double width = 0;
    double radius = 0;

    cin >> height >> width >> radius;

    PrintRectanglePerimeter(height, width);
    cout << CalcRectanglePerimeter(height, width) << std::endl;
    cout << "... about to double height and width...\n";
    DoubleRectanglePerimeter(height, width);
    PrintRectanglePerimeter(height, width);

    return 0;
}

double CalcRectanglePerimeter(double height, double width) {
    return (2 * height) + (2 * width);
}

void PrintRectanglePerimeter(double height, double width) {
    double perimeter = CalcRectanglePerimeter(height,width);
    cout << fixed << setprecision(1);
    cout << "A rectangle with height " << height << " and width " << width << " has a perimeter of " << perimeter << "." << endl;
}

void DoubleRectanglePerimeter(double& height, double& width) {
    height = height * 2;
    width = width * 2;
}

double CalcCircumferenceOfCircle(double radius) {
    return radius * 2 * PI;
}

double CalcAreaOfCircle (double radius) {
    return radius * radius * PI;
}

double CalcVolumeOfSphere (double radius) {
    return (4.0 / 3.0) * PI * radius * radius * radius;
}

void Swap(double& valueA, double& valueB) {
    double holder = valueA;
    valueA = valueB;
    valueB = holder;
}

void Swap(int& valueA, int& valueB) {
    int holder = valueA;
    valueA = valueB;
    valueB = holder;
}