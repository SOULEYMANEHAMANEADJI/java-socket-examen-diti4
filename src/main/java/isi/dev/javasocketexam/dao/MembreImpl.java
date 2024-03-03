package isi.dev.javasocketexam.dao;

import isi.dev.javasocketexam.config.HibernateUtil;
import isi.dev.javasocketexam.entities.Membre;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.TypedQuery;
import java.util.List;


public class MembreImpl implements IMembre{

    private Session session;
    private Transaction transaction;


    public MembreImpl () {
       session = HibernateUtil.getSessionFactory().openSession();
    }

    @Override
    public int create(Membre membre) {
        int ok = 0;
        try {
            transaction = session.beginTransaction();
            session.save(membre);
            transaction.commit();
            ok = 1;
        }catch (Exception e){
            e.printStackTrace();
        }
        return ok;
    }

    @Override
    public List<Membre> getAll() {
        return null;
    }


    @Override
    public Membre get(int id) {
        return session.get(Membre.class, id);
    }

    @Override
    public int update(Membre classe) {
        int ok = 0;
        try {
            transaction = session.beginTransaction();
            session.merge(classe);
            transaction.commit();
            ok = 1;
        }catch (Exception e){
            e.printStackTrace();
        }
        return ok;
    }

    @Override
    public int delete(int id) {
        int ok = 0;
        try {
            transaction = session.beginTransaction();
            session.delete(get(id));
            transaction.commit();
            ok = 1;
        }catch (Exception e){
            e.printStackTrace();
        }
        return ok;
    }

    @Override
    public Membre findByName(String name) {
            String jpql = "SELECT m FROM Membre m WHERE m.name = :name";
            TypedQuery<Membre> query = session.createQuery(jpql, Membre.class);
            query.setParameter("name", name);
            List<Membre> results = query.getResultList();
            if (!results.isEmpty()) {
                return results.get(0);
            }
            return null;
        }

    @Override
    public Membre findByEmail(String email) {
        String jpql = "SELECT m FROM Membre m WHERE m.email = :email";
        TypedQuery<Membre> query = session.createQuery(jpql, Membre.class);
        query.setParameter("email", email);
        List<Membre> results = query.getResultList();
        if (!results.isEmpty()) {
            return results.get(0);
        }
        return null;
    }

    @Override
    public Membre findByPhoneNo(String phoneNo) {
        String jpql = "SELECT m FROM Membre m WHERE m.phoneNo = :phoneNo";
        TypedQuery<Membre> query = session.createQuery(jpql, Membre.class);
        query.setParameter("phoneNo", phoneNo);
        List<Membre> results = query.getResultList();
        if (!results.isEmpty()) {
            return results.get(0);
        }
        return null;
    }
    @Override
    public Membre findNameAndPassword(String name, String password) {
        String queryString = "SELECT m FROM Membre m WHERE m.name = :name AND m.password = :password";
        TypedQuery<Membre> query = session.createQuery(queryString, Membre.class);
        query.setParameter("name", name);
        query.setParameter("password", password);
        List<Membre> membres = query.getResultList();
        return membres.isEmpty() ? null : membres.get(0);
    }

}
