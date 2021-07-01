package dev.nicholaskoldys.matso.dao;

import dev.nicholaskoldys.matso.model.User;

import java.util.List;

public interface UserDao extends DaoBase<User> {

    public User getByName(String name);

    @Override
    public User getById(int id);

    @Override
    public List<User> getAll();

    List<User> reportAll();

    @Override
    public boolean insert(User item);

    public boolean insert(String name, String password);

    @Override
    public boolean insertAll(List<User> items);

    @Override
    public boolean update(User item);

    public boolean updateName(User item, String name);

    public boolean updatePassword(User iten, String password);

    @Override
    public boolean delete(User item);

    public boolean loginAttempt(String name, String password);

    public boolean loginUser(String name, String password);

    public boolean logoutAttempt();
}
