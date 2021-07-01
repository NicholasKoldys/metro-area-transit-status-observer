package dev.nicholaskoldys.matso.old;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

//public class repositoryClass {
//
//    private BusServiceReportDaoImpl busServiceReportDao;
//    private ObservableList<BusServiceReport> bsReportList = FXCollections.observableArrayList();
//
//
//    busServiceReportDao = db.BusServiceReportDao();
//    bsReportList.setAll(busServiceReportDao.queryReport());
//
//
//    public ObservableList<BusServiceReport> getBusServiceReportList() {
//        if(bsReportList == null || bsReportList.isEmpty()) {
//            setBusServiceReportList(busServiceReportDao.queryReport());
//        }
//        return bsReportList;
//    }
//
//        setBusServiceReportList(busServiceReportDao.queryReport());
//
//    public void setBusServiceReportList(List<BusServiceReport> reportList) {
//        this.bsReportList.setAll(reportList);
//    }
////
//
//private RouteTimeStatsReportDaoImpl routeTimeStatsReportDao;
//
//        routeTimeStatsReportDao = db.RouteTimeStatsReportDao();
//
//private ObservableList<RouteTimeStatsReport> rtsReportList = FXCollections.observableArrayList();
//        rtsReportList.setAll(routeTimeStatsReportDao.queryReport());
//
//
//
//public ObservableList<RouteTimeStatsReport> getRouteTimeStatsReportList() {
//        if(rtsReportList == null || rtsReportList.isEmpty()) {
//        setRouteTimeStatsReportList(routeTimeStatsReportDao.queryReport());
//        }
//        return rtsReportList;
//        }
//
//
//
//public void setRouteTimeStatsReportList(List<RouteTimeStatsReport> reportList) {
//        this.rtsReportList.setAll(reportList);
//        }
//
//        setRouteTimeStatsReportList(routeTimeStatsReportDao.queryReport());
//
//
//}
