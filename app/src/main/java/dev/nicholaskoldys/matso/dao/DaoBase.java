package dev.nicholaskoldys.matso.dao;

import java.util.List;

/**
 * Create Read Update Delete
 * @param <T>
 */
public interface DaoBase<T> {

    T getById(int id);

    List<T> getAll();

    boolean insert(T item);

    boolean insertAll(List<T> items);

    boolean update(T item);

    boolean delete(T item);
}