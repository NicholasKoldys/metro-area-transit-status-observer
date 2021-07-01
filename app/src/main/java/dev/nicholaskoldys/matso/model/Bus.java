package dev.nicholaskoldys.matso.model;

import dev.nicholaskoldys.matso.utility.Constants;

import java.time.LocalDateTime;

public class Bus {

    private int busId;
    private String busNum;
    private int capacity;
    private int routeId;
    private String routeNum;
    private Enum<Constants.MaintenanceState> maintenanceState;
    private LocalDateTime lastBreakdown;
    private int active;
    private LocalDateTime createDate;
    private String createUserName;
    private LocalDateTime lastUpdateBy;
    private String updateUserName;


    public Bus(int busId, String busNum, int capacity, int routeId, String routeNum, Enum<Constants.MaintenanceState> maintenanceState, LocalDateTime lastBreakdown) {
        this.busId = busId;
        this.busNum = busNum;
        this.capacity = capacity;
        this.routeId = routeId;
        this.routeNum = routeNum;
        this.maintenanceState = maintenanceState;
        this.lastBreakdown = lastBreakdown;
    }

    public Bus(String busNum, int capacity, int routeId, String routeNum, Enum<Constants.MaintenanceState> maintenanceState, LocalDateTime lastBreakdown) {
        this.busNum = busNum;
        this.capacity = capacity;
        this.routeId = routeId;
        this.routeNum = routeNum;
        this.maintenanceState = maintenanceState;
        this.lastBreakdown = lastBreakdown;
    }

    public Bus(int busId, String busNum, int capacity, int routeId, String routeNum, Enum<Constants.MaintenanceState> maintenanceState, LocalDateTime lastBreakdown, int active, LocalDateTime createDate, String createUserName, LocalDateTime lastUpdateBy, String updateUserName) {
        this.busId = busId;
        this.busNum = busNum;
        this.capacity = capacity;
        this.routeId = routeId;
        this.routeNum = routeNum;
        this.maintenanceState = maintenanceState;
        this.lastBreakdown = lastBreakdown;
        this.active = active;
        this.createDate = createDate;
        this.createUserName = createUserName;
        this.lastUpdateBy = lastUpdateBy;
        this.updateUserName = updateUserName;
    }

    public int getBusId() {
        return busId;
    }

    public void setBusId(int busId) {
        this.busId = busId;
    }

    public String getBusNum() {
        return busNum;
    }

    public void setBusNum(String busNum) {
        this.busNum = busNum;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public String getRouteNum() {
        if(routeNum == null) {
           return null;
        }
        return routeNum;
    }

    public void setRouteNum(String routeNum) {
        this.routeNum = routeNum;
    }

    public Enum<Constants.MaintenanceState> getMaintenanceState() {
        return maintenanceState;
    }

    public void setMaintenanceState(Enum<Constants.MaintenanceState> maintenanceState) {
        this.maintenanceState = maintenanceState;
    }

    public LocalDateTime getLastBreakdown() {
        if(lastBreakdown == null) {
            return null;
        }
        return lastBreakdown;
    }

    public int getActive() {
        return active;
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

    public void setLastBreakdown(LocalDateTime lastBreakdown) {
        this.lastBreakdown = lastBreakdown;
    }

    public Bus copy() {
        Bus copy = new Bus(
                this.getBusId(),
                this.getBusNum(),
                this.getCapacity(),
                this.getRouteId(),
                this.getRouteNum(),
                this.getMaintenanceState(),
                this.getLastBreakdown()
        );
        return copy;
    }
}
