//1. What design principles does this code violate?
    //Single Responsibility Principle.
//2. Refactor the code to improve its design.

class Employee {
    //…
    private Date employmentStartDate;
    private Date employmentEndDate;
    private int getTotalYearsOfService(Date startDate, Date endDate) { … }
    private int getMonthsInLastPosition(Date startDate, Date endDate) { … }
}

class RetirementCalculator {
    private Employee employee;
    public RetirementCalculator(Employee emp) {
        this.employee = emp;
    }
    public float calculateRetirement(Date payPeriodStart, Date payPeriodEnd) { … }
    ...  
}
