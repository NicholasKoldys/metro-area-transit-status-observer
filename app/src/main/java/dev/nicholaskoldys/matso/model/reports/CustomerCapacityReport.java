package dev.nicholaskoldys.matso.model.reports;

public class CustomerCapacityReport {

    private String routeNum;
    private int totalCapacity;
    private int customerAvg;

    public CustomerCapacityReport(String routeNum, int customerAvg,  int totalCapacity) {
        this.routeNum = routeNum;
        this.customerAvg = customerAvg;
        this.totalCapacity = totalCapacity;
    }

    public String getRouteNum() {
        return routeNum;
    }

    public int getTotalCapacity() {
        return totalCapacity;
    }

    public int getCustomerAvg() {
        return customerAvg;
    }
}
