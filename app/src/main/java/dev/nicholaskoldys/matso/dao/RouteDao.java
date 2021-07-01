package dev.nicholaskoldys.matso.dao;

import dev.nicholaskoldys.matso.model.BusChart;
import dev.nicholaskoldys.matso.model.Route;

import java.util.List;

public interface RouteDao extends DaoBase<Route> {

    @Override
    Route getById(int id);

    @Override
    List<Route> getAll();

    List<Route> reportAll();

    List<BusChart> getChart();

    @Override
    boolean insert(Route item);

    @Override
    boolean insertAll(List<Route> items);

    @Override
    boolean update(Route item);

    @Override
    boolean delete(Route item);
}
