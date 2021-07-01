package dev.nicholaskoldys.matso.model;

public class BusChart {

    private int routeId;
    private String routeNum;
    private int busId;
    private String busNum;

    public BusChart(int routeId, String routeNum, int busId, String busNum) {
        this.routeId = routeId;
        this.routeNum = routeNum;
        this.busId = busId;
        this.busNum = busNum;
    }

    public int getRouteId() {
        return routeId;
    }

    public String getRouteNum() {
        return routeNum;
    }

    public int getBusId() {
        return busId;
    }

    public String getBusNum() {
        return busNum;
    }
}
