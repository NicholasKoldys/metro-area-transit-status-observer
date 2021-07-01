package dev.nicholaskoldys.matso.dao;

import dev.nicholaskoldys.matso.model.Bus;

import java.util.List;

public interface BusDao extends DaoBase<Bus> {

    @Override
    Bus getById(int id);

    @Override
    List<Bus> getAll();

    List<Bus> reportAll();

    @Override
    boolean insert(Bus item);

    @Override
    boolean insertAll(List<Bus> items);

    @Override
    boolean update(Bus item);

    @Override
    boolean delete(Bus item);
}
