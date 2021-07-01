package dev.nicholaskoldys.matso.utility;

import dev.nicholaskoldys.matso.database.DBConnection;
import dev.nicholaskoldys.matso.database.Database;
import dev.nicholaskoldys.matso.database.Repository;
import dev.nicholaskoldys.matso.model.Route;
import dev.nicholaskoldys.matso.dao.implementation.RouteDaoImpl;
import dev.nicholaskoldys.matso.model.User;

import java.sql.Connection;

public class UnitTests {

    static Repository repository;
    static Connection databaseConnection;
    static Database database;


    public static void runDatabaseConnectionTest() {

        databaseConnection = DBConnection.getConnection();
        if(databaseConnection != null) {


            RouteDaoImpl routeDao = database.RouteDao();
            if(database.RouteDao() != null) {

                for (Route route : routeDao.getAll()) {
                    System.out.println("route name: " + route.getRouteNum());
                    System.out.println("route customerAvg: " + route.getCustomersAvg());
                }

                repository = Repository.getInstance();

                if(repository != null) {
                    User user = repository.getCurrentUser();
                    System.out.println("The current user is " + user.getUserName());
                    System.out.println("The user last login was " + user.getLastLogin().toString());

                } else {
                    System.out.println("Repository ran into an issue.");
                }
            } else {
                System.out.println("Database ran into an issue.");
            }
        }
    }
}
