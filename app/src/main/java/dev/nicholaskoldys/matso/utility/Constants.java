package dev.nicholaskoldys.matso.utility;

import dev.nicholaskoldys.matso.database.DBConnection;

public class Constants {


    /**
     * Default Scene - Application Start Scene.
     */
    public static final SceneView APP_START_SCENE = SceneView.LOGIN_SCENE;


    /**
     * Red color used after warning in Manage Scene TextFields.
     */
    public static final String INCORRECT_COLOR = "ffe5e3";


    /**
     * TODO TEMPORARY - Don't use in production environment.
     * Must be All CAPITAL
     */
    public static final String[] WHITELIST_ADMIN = {"NKOLDYS"};


    /**
     *  Current Available Reports for Report ChoiceBox.
     *      - used for showing Text Before Selection.
     *      - or set a report to be loaded on default.
     */
    public static final String[] AVAILABLE_REPORTS = {
            "Please Select a Report...",
            "Buses",
            "Routes",
            "Bus Chart",
            "Routes Below Capacity",
            "Routes Above Capacity",
            "Users"
    };


    /**
     *  Bus Maintenance State Values.
     */
    public enum MaintenanceState {
        OK,
        SERVICE,
        OUT_OF_ORDER
    }


    /**
     *  SceneView names environment variables.
     */
    public enum SceneView {
        /* toString needs to be human readable for the title and reflect the files name without '.fxml' */
        MENU_BAR {
            @Override
            public String toString() {
                return "MenuBar";
            }
        },
        LOGIN_SCENE {
            @Override
            public String toString() {
                return "Login";
            }
        },
        DASHBOARD_SCENE {
            @Override
            public String toString() {
                return "Dashboard";
            }
        },
        BUSES_SCENE {
            @Override
            public String toString() {
                return "Bus Manager";
            }
        },
        ROUTES_SCENE {
            @Override
            public String toString() {
                return "Route Manager";
            }
        },
        REPORTS_SCENE {
            @Override
            public String toString() {
                return "Reports";
            }
        },
        TEST {
            @Override
            public String toString() {
                return "Test";
            }
        };
    }


    /*This method would be more useful with @IntDef({}) but its not included*/
    /*@Retention(RetentionPolicy.SOURCE)
    @Inherited
    public @interface SceneView {
        int LOGIN_SCENE_VIEW = 1;
        int DASHBOARD_SCENE_VIEW = 2;


        public String getSceneViewString() {
            switch ( this ) {
                case LOGIN_SCENE:
                    return "LoginScene.fxml";
                case DASHBOARD_SCENE:
                    return "DashboardScene.fxml";
                case TEST:
                    return "test.fxml";
            }
            return "";
        }
    }*/

    /**
     * This is here because it can change with different databases.
     */
    public static class EnvironmentVariables {
        // MYSQL = current_timestamp()
        // HSQLDB = CURRENT_TIMESTAMP
        public static final String CURRENT_TIMESTAMP_METHOD = "CURRENT_TIMESTAMP";

        // MYSQL = INT(11)
        // HSQLDB = INT
        public static final String COMMON_INT = "INT";

        // MYSQL = TINYINT(1)
        // HSQLDB = TINYINT
        public static final String SINGLE_INT = "TINYINT";

        // MYSQL = SHA1("")
        // HSQLDB =  (VALUES(0));
        public static String ENCRYPTION_METHOD(String encryptedString) {
            //return "SHA(" + encryptedString + ")";
            //return "(SELECT CRYPT_KEY('AES', null) FROM (" + encryptedString + "))";
            return encryptedString;
        }
    }

    public static class TableConstants {

        public static final String STRUCTURE_SCHEMA = DBConnection.DATABASE_NAME + "_SCHEMA";

        // TODO make check for all table names, dont just blast the structure schema

        public static final String TABLE_USER = STRUCTURE_SCHEMA + ".user";
        public static final String USER_ID = "userId";
        public static final String USER_NAME = "userName";
        public static final String USER_PASSWORD = "password";
        public static final String USER_ACTIVE = "active";

        public static final String USER_LOGGEDIN = "loggedIn";
        public static final String USER_LASTLOGIN = "lastLogin";
        public static final String USER_ATTEMPT = "attempt";

        public static final String TABLE_BUSES = STRUCTURE_SCHEMA + ".buses";
        public static final String BUS_ID = "busId";
        public static final String BUS_NUM = "busNum";
        public static final String BUS_ACTIVE = "active";
        public static final String BUS_CAPACITY = "capacity";
        public static final String BUS_ROUTEID = "routeId";
        public static final String BUS_MAINTENANCESTATE = "maintenanceState";
        public static final String BUS_LASTBREAKDOWN = "lastBreakdown";

        public static final String TABLE_ROUTES = STRUCTURE_SCHEMA + ".routes";
        public static final String ROUTE_ID = "routeId";
        public static final String ROUTE_NUM = "routeNum";
        public static final String ROUTE_STARTPOINT = "startPoint";
        public static final String ROUTE_ENDPOINT = "endPoint";
        public static final String ROUTE_STOPSTOTAL = "stopsTotal";
        public static final String ROUTE_TIMELENGTH = "timeLength";
        public static final String ROUTE_DISTANCE = "distance";
        public static final String ROUTE_CURRENTDELAY = "currentDelay";
        public static final String ROUTE_CUSTOMERSAVG = "customersAvg";

        public static final String CREATE_DATE = "createDate";
        public static final String CREATED_BY = "createdBy";
        public static final String LAST_UPDATE = "lastUpdate";
        public static final String LAST_UPDATE_BY = "lastUpdateBy";

        public static final String TABLE_BUSCHART = STRUCTURE_SCHEMA + ".busChart";
        public static final String BUSCHART_ID = "busChartId";

        public static final String TRIGGER_INS_BUS = TABLE_BUSCHART + "INS_BUS";
        public static final String TRIGGER_UPD_BUS = TABLE_BUSCHART + "UPD_BUS";
        public static final String TRIGGER_INS_ROUTE = TABLE_BUSCHART + "INS_ROUTE";
    }
}
