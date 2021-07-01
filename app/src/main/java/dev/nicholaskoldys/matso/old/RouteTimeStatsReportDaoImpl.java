package dev.nicholaskoldys.matso.old;


//public class RouteTimeStatsReportDaoImpl implements DaoReportBase<RouteTimeStatsReport> {
//
//    public static final String TABLE_ROUTES = RouteDaoImpl.TABLE_ROUTES;
//    private static final String ROUTE_NUM_COLUMN = RouteDaoImpl.ROUTE_NUM_COLUMN;
//    private static final String ROUTE_TIMELENGTH_COLUMN = RouteDaoImpl.ROUTE_TIMELENGTH_COLUMN;
//    private static final String ROUTE_CURRENTDELAY_COLUMN = RouteDaoImpl.ROUTE_CURRENTDELAY_COLUMN;
//
//    private final String SELECT_ROUTES_TIMELENGTH_AND_CURRENTDELAY =
//            "SELECT " + ROUTE_NUM_COLUMN + ", "
//                    + ROUTE_TIMELENGTH_COLUMN + ", "
//                    + ROUTE_CURRENTDELAY_COLUMN
//                    + " FROM " + TABLE_ROUTES;
//
//
//    private static RouteTimeStatsReportDaoImpl INSTANCE = null;
//
//
//    private RouteTimeStatsReportDaoImpl() {}
//
//    /**
//     *
//     * @return
//     */
//    public static RouteTimeStatsReportDaoImpl getInstance() {
//
//        if (INSTANCE == null) {
//            synchronized (RouteTimeStatsReportDaoImpl.class) {
//                if(INSTANCE == null) {
//                    INSTANCE = new RouteTimeStatsReportDaoImpl();
//                }
//            }
//        }
//        if(INSTANCE != null) {
//            return INSTANCE;
//        } else {
//            System.out.println("RouteTimeStatsReportDaoImpl failed to create instance.");
//            return null;
//        }
//    }
//
//    @Override
//    public List<RouteTimeStatsReport> queryReport() {
//        try (PreparedStatement sqlStatement = DBConnection.getConnection().prepareStatement(SELECT_ROUTES_TIMELENGTH_AND_CURRENTDELAY)) {
//
//            ResultSet results = sqlStatement.executeQuery();
//            List<RouteTimeStatsReport> reportList = new ArrayList<>();
//
//            while(results.next()) {
//                RouteTimeStatsReport route = new RouteTimeStatsReport(
//                        results.getString(ROUTE_NUM_COLUMN),
//                        results.getTime(ROUTE_TIMELENGTH_COLUMN).toLocalTime(),
//                        results.getTime(ROUTE_CURRENTDELAY_COLUMN).toLocalTime()
//                );
//                reportList.add(route);
//            }
//            return reportList;
//
//        } catch (SQLException ex) {
//            System.out.println("Creating the Customer Capacity Per Route Report Failed");
//            ex.getStackTrace();
//        }
//        return null;
//    }
//}
