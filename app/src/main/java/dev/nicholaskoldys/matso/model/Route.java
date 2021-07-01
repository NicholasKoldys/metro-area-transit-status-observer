package dev.nicholaskoldys.matso.model;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class Route {

    private int routeId;
    private String routeNum;
    private String startPoint;
    private String endPoint;
    private int stopsTotal;
    private LocalTime timeLength;
    private float distance = 0.0F;
    private LocalTime currentDelay;
    private int customersAvg;
    private LocalDateTime createDate;
    private String createUserName;
    private LocalDateTime lastUpdateBy;
    private String updateUserName;


    public Route(int routeId, String routeNum, String startPoint, String endPoint, int stopsTotal, LocalTime timeLength, float distance, LocalTime currentDelay, int customersAvg) {
        this.routeId = routeId;
        this.routeNum = routeNum;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.stopsTotal = stopsTotal;
        this.timeLength = timeLength;
        this.distance = distance;
        this.currentDelay = currentDelay;
        this.customersAvg = customersAvg;
    }

    public Route(String routeNum, String startPoint, String endPoint, int stopsTotal, LocalTime timeLength, float distance, LocalTime currentDelay, int customersAvg) {
        this.routeNum = routeNum;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.stopsTotal = stopsTotal;
        this.timeLength = timeLength;
        this.distance = distance;
        this.currentDelay = currentDelay;
        this.customersAvg = customersAvg;
    }

    public Route(int routeId, String routeNum, String startPoint, String endPoint, int stopsTotal, LocalTime timeLength, float distance, LocalTime currentDelay, int customersAvg, LocalDateTime createDate, String createUserName, LocalDateTime lastUpdateBy, String updateUserName) {
        this.routeId = routeId;
        this.routeNum = routeNum;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.stopsTotal = stopsTotal;
        this.timeLength = timeLength;
        this.distance = distance;
        this.currentDelay = currentDelay;
        this.customersAvg = customersAvg;
        this.createDate = createDate;
        this.createUserName = createUserName;
        this.lastUpdateBy = lastUpdateBy;
        this.updateUserName = updateUserName;
    }

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public String getRouteNum() {
        return routeNum;
    }

    public void setRouteNum(String routeNum) {
        this.routeNum = routeNum;
    }

    public String getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public int getStopsTotal() {
        return stopsTotal;
    }

    public void setStopsTotal(int stopsTotal) {
        this.stopsTotal = stopsTotal;
    }

    public LocalTime getTimeLength() {
        return timeLength;
    }

    public void setTimeLength(LocalTime timeLength) {
        this.timeLength = timeLength;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public LocalTime getCurrentDelay() {
        return currentDelay;
    }

    public void setCurrentDelay(LocalTime currentDelay) {
        this.currentDelay = currentDelay;
    }

    public int getCustomersAvg() {
        return customersAvg;
    }

    public void setCustomersAvg(int customersAvg) {
        this.customersAvg = customersAvg;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public LocalDateTime getLastUpdateBy() {
        return lastUpdateBy;
    }

    public String getUpdateUserName() {
        return updateUserName;
    }

    public Route copy() {
        Route copy = new Route(
                this.getRouteId(),
                this.getRouteNum(),
                this.getStartPoint(),
                this.getEndPoint(),
                this.getStopsTotal(),
                this.getTimeLength(),
                this.getDistance(),
                this.getCurrentDelay(),
                this.getCustomersAvg()
        );
        return copy;
    }
}
