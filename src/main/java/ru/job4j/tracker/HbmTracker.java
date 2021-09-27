package ru.job4j.tracker;

import org.hibernate.*;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.List;


public class HbmTracker implements Store, AutoCloseable {

    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private final SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();

    @Override
    public Item add(Item item) {
        Session session = sf.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(item); // item.setId(1); item.setName("Vasya");
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return item;
    }
    @Override
    public boolean replace(String id, Item item) {
        Session session = sf.openSession();
        Transaction transaction = null;
        Boolean result;
        try {
            int itemId = Integer.parseInt(id);
            transaction = session.beginTransaction();
            item = session.get(Item.class, itemId); item.setId(itemId);
            session.update(item);
            transaction.commit();
            result = true;
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            result = false;
            e.printStackTrace();
        } finally {
            session.close();
        }
        return result;
    }

    @Override
    public boolean delete(String id) {
        Session session = sf.openSession();
        Transaction transaction = null;
        Boolean result;
        try {
            int itemId = Integer.parseInt(id);
            transaction = session.beginTransaction();
            Item item = session.get(Item.class, itemId);
            if (item != null) {
                session.delete(item);
                transaction.commit();
                result = true;
            } else {
                result = false;
            }
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            result = false;
            e.printStackTrace();
        } finally {
            session.close();
        }
        return result;
    }

    @Override
    public List<Item> findAll() {
        Session session = sf.openSession();
        Transaction transaction = null;
        List<Item> lItems = null;
        try {
            transaction = session.beginTransaction();
            lItems = session.createQuery("from items").list();
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return lItems;
    }

    @Override
    public List<Item> findByName(String key) {
        Session session = sf.openSession();
        Transaction transaction = null;
        List<Item> lItems = null;
        try {
            transaction = session.beginTransaction();
            lItems = session.createQuery("from item where name =: name").setParameter("name", key).list();
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return lItems;
    }

    @Override
    public Item findById(String id) {
        Session session = sf.openSession();
        Transaction transaction = null;
        Item item = null;
        try {
            int itemId = Integer.parseInt(id);
            transaction = session.beginTransaction();
            item = session.get(Item.class, itemId);
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return item;
    }

    @Override
    public void close() throws Exception {
        StandardServiceRegistryBuilder.destroy(registry);
    }
}