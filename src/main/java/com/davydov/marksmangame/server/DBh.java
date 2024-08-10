package com.davydov.marksmangame.server;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class DBh implements DB {
    SessionFactory sessionFactory = HibernateSF.getSessionFactory();

    @Override
    public ArrayList<Leader> getAllRecords() {
        ArrayList<Leader> res = new ArrayList<>();
        List<Leader> records = (List<Leader>)sessionFactory.openSession().
                createQuery("FROM Leader obj ORDER BY obj.playerWins DESC").list();
        res.addAll(records);
        return res;
    }

    @Override
    public void addOrUpdateRec(Leader rec) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        session.saveOrUpdate(rec);
        tx.commit();
        session.close();
    }
}
