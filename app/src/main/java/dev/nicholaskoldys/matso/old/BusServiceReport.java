package dev.nicholaskoldys.matso.old;

//public class BusServiceReport {
//
//    private int count;
//    private Constants.MaintenanceState maintenanceState;
//
//    public BusServiceReport(int count, Constants.MaintenanceState maintenanceState) {
//        this.count = count;
//        this.maintenanceState = maintenanceState;
//    }
//
//    public int getCount() {
//        return count;
//    }
//
//    public void setCount(int count) {
//        this.count = count;
//    }
//
//    public Constants.MaintenanceState getMaintenanceState() {
//        return maintenanceState;
//    }
//
//    public void setMaintenanceState(Constants.MaintenanceState maintenanceState) {
//        this.maintenanceState = maintenanceState;
//    }

//Code for pie call
/*ObservableList<BusServiceReport> countList = FXCollections.observableArrayList();
        countList = repo.getBusServiceReportList();

        List<PieChart.Data> pieChartData = new ArrayList<>();

        if(!countList.isEmpty()) {
            for (BusServiceReport busCount : countList) {
                PieChart.Data newPiePiece = new PieChart.Data(busCount.getMaintenanceState().toString(), busCount.getCount());
                newPiePiece.setName(busCount.getCount() + " - " + busCount.getMaintenanceState().toString() + " buses");
                pieChartData.add(newPiePiece);
            }
            busMaintenanceStatusPieChart.getData().addAll(pieChartData);
        }*/

//}
