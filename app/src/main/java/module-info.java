module dev.nicholaskoldys.matso.app {
    
    requires java.sql;
    requires javafx.controls;
    requires javafx.fxml;

    opens dev.nicholaskoldys.matso.controller to javafx.fxml;
    opens dev.nicholaskoldys.matso.model to javafx.base;
    opens dev.nicholaskoldys.matso.model.reports to javafx.base;

    exports dev.nicholaskoldys.matso.utility to javafx.fxml;
    exports dev.nicholaskoldys.matso;
}