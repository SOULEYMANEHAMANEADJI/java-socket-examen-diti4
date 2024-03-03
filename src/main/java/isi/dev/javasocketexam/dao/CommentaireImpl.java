package isi.dev.javasocketexam.dao;

import isi.dev.javasocketexam.config.HibernateUtil;
import isi.dev.javasocketexam.entities.Commentaire;
import isi.dev.javasocketexam.entities.Membre;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class CommentaireImpl implements ICommentaire{

    private Session session;
    private Transaction transaction;


    public CommentaireImpl () {
        session = HibernateUtil.getSessionFactory().openSession();
    }

    @Override
    public int create(Commentaire commentaire) {
        int ok = 0;
        try {
            transaction = session.beginTransaction();
            session.save(commentaire);
            transaction.commit();
            ok = 1;
        }catch (Exception e){
            e.printStackTrace();
        }
        return ok;
    }

    @Override
    public List<Commentaire> getAll() {
        return null;
    }
    @Override
    public List<Commentaire> getAllWithMembre() {
        List<Commentaire> commentaires = null;
        try {
            transaction = session.beginTransaction();
            String hql = "SELECT c FROM Commentaire c JOIN FETCH c.membre";
            commentaires = session.createQuery(hql, Commentaire.class).getResultList();
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return commentaires;
    }
}
