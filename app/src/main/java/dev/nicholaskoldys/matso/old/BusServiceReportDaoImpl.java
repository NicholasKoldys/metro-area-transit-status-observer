package dev.nicholaskoldys.matso.old;

//public class BusServiceReportDaoImpl implements DaoReportBase<BusServiceReport> {
//
//    public static final String TABLE_BUSES = DBConnection.DATABASE_NAME + ".buses";
//    private static final String BUS_ACTIVE_COLUMN = "active";
//    private static final String BUS_COUNT_COLUMN = "COUNT(*)";
//    private static final String BUS_MAINTENANCESTATE_COLUMN = "maintenanceState";
//
//    private final String SELECT_COUNT_PER_MAINTENANCESTATE =
//            "SELECT " + BUS_COUNT_COLUMN + ", "
//                      + BUS_MAINTENANCESTATE_COLUMN
//            + " FROM " + TABLE_BUSES
//            + " WHERE " + BUS_ACTIVE_COLUMN + " = 1"
//            + " GROUP BY " + BUS_MAINTENANCESTATE_COLUMN;
//
//
//    private static BusServiceReportDaoImpl INSTANCE = null;
//
//
//    private BusServiceReportDaoImpl() {}
//
//    /**
//     *
//     * @return
//     */
//    public static BusServiceReportDaoImpl getInstance() {
//
//        if (INSTANCE == null) {
//            synchronized (BusServiceReportDaoImpl.class) {
//                if(INSTANCE == null) {
//                    INSTANCE = new BusServiceReportDaoImpl();
//                }
//            }
//        }
//        if(INSTANCE != null) {
//            return INSTANCE;
//        } else {
//            System.out.println("BusServiceReportDaoImpl failed to create instance.");
//            return null;
//        }
//    }
//
//    @Override
//    public List<BusServiceReport> queryReport() {
//        try (PreparedStatement sqlStatement = DBConnection.getConnection().prepareStatement(SELECT_COUNT_PER_MAINTENANCESTATE)) {
//
//            ResultSet results = sqlStatement.executeQuery();
//            List<BusServiceReport> reportList = new ArrayList<>();
//
//            while(results.next()) {
//                BusServiceReport count = new BusServiceReport(
//                        results.getInt(BUS_COUNT_COLUMN),
//                        Constants.MaintenanceState.valueOf(results.getString(BUS_MAINTENANCESTATE_COLUMN))
//                );
//                reportList.add(count);
//            }
//            return reportList;
//
//        } catch (SQLException ex) {
//            System.out.println("Creating the Bus Maintenance Report Failed");
//            ex.getStackTrace();
//        }
//        return null;
//    }
//}
