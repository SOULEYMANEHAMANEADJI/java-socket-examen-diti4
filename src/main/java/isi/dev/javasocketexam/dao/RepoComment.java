package isi.dev.javasocketexam.dao;

import java.util.List;

public interface RepoComment <T>{
    public int create(T t);
    public List<T> getAll();
    List<T> getAllWithMembre();
}
