//1. What design principles does this code violate?
    //Simplicity.
//2. Refactor the code to improve its design.

if (income > 100000) {
    accept();
} else if (score > 700) {
    accept();
} else if ((income >= 40000) && (score > 500) && authorized) {
    accept();
} else {
    reject();
}