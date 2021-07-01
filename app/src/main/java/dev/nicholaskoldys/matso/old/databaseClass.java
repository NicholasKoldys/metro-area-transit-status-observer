package dev.nicholaskoldys.matso.old;

//import matsoapplication.database.DBConnection;
//
//public class databaseClass {
//private BusServiceReportDaoImpl busServiceReportDao;
//this.busServiceReportDao = BusServiceReportDao();
//
//    public BusServiceReportDaoImpl BusServiceReportDao(){
//        if (busServiceReportDao == null) {
//            synchronized (DBConnection.class) {
//                if(busServiceReportDao == null) {
//                    busServiceReportDao = BusServiceReportDaoImpl.getInstance();
//                }
//            }
//        }
//        if(busServiceReportDao != null) {
//            return busServiceReportDao;
//        } else {
//            System.out.println("BusServiceReport Database Object failed to be created");
//            return null;
//        }
//    }
//
//
//
//private RouteTimeStatsReportDaoImpl routeTimeStatsReportDao;
//        this.routeTimeStatsReportDao = RouteTimeStatsReportDao();
//
//
//public RouteTimeStatsReportDaoImpl RouteTimeStatsReportDao() {
//        if (routeTimeStatsReportDao == null) {
//synchronized (DBConnection.class) {
//        if(routeTimeStatsReportDao == null) {
//        routeTimeStatsReportDao = RouteTimeStatsReportDaoImpl.getInstance();
//        }
//        }
//        }
//        if(routeTimeStatsReportDao != null) {
//        return routeTimeStatsReportDao;
//        } else {
//        System.out.println("CustomerCapacityReport Database Object failed to be created");
//        return null;
//        }
//        }
//}
