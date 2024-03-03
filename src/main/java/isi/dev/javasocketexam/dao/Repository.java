package isi.dev.javasocketexam.dao;

import java.util.List;

public interface Repository<T>{
    public int create(T t);
    public List<T> getAll();
    public T get(int id);
    public int update(T t);
    public int delete(int id);
    public T findByName(String name);
    public T findByEmail(String email);
    public T findByPhoneNo(String phoneNo);
    public T findNameAndPassword(String name, String password);
}
