package ru.job4j.tracker.hql;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

public class HqlRun {
    public static void main(String[] args) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();
/*
            Candidate one = Candidate.of("Vasiliy", 2, 125200);
            Candidate two = Candidate.of("Petr", 1, 101000);
            Candidate three = Candidate.of("Alex", 5, 250000);

            session.save(one);
            session.save(two);
            session.save(three);
            session.getTransaction().commit(); */

            Query query = session.createQuery("from Candidate ");
            for (Object cd : query.list()) {
                System.out.println(cd);
            }

            Query selectByValue = session.createQuery("from Candidate c where c.id = 2");
            System.out.println(selectByValue.uniqueResult());

            Query selectById = session.createQuery("from Candidate c where c.id = 3");
            System.out.println(selectById.uniqueResult());

            Query updateByValue = session.createQuery(
                    "update Candidate s set s.salary = :newSalary, s.experience = :newExperience where s.id = :cId");
            updateByValue.setParameter("newSalary", 164000);
            updateByValue.setParameter("newExperience", 2);
            updateByValue.setParameter("cId", Long.valueOf(2));
            updateByValue.executeUpdate();

            Query selectByValues = session.createQuery("from Candidate c where c.id = 2");
            System.out.println(selectByValues.uniqueResult());

            session.createQuery("update Candidate s set s.salary = :newSalary, s.experience = :newExperience where s.id = :cId")
                    .setParameter("newSalary", 175000)
                    .setParameter("newExperience", 3)
                    .setParameter("cId", Long.valueOf(1))
                    .executeUpdate();

            Query updateById = session.createQuery("from Candidate c where c.id = :cId");
            updateById.setParameter("cId", Long.valueOf(1));
            System.out.println("Обновление 1:" + updateById.uniqueResult());

            session.createQuery("delete from Candidate s where s.id = :cId")
                    .setParameter("cId", Long.valueOf(1))
                    .executeUpdate();
            Query delete = session.createQuery("from Candidate");
            for (Object cd : delete.list()) {
                System.out.println(cd);
            }

            Query selectByName = session.createQuery("from Candidate c where c.name = :newName");
            selectByName.setParameter("newName", "Petr");
            System.out.println(selectByName.list());

            session.createQuery(
                    "insert into Candidate (name, experience, salary) "
                            + "select 'Vasiliy', s.experience + 1, 175000 "
                            + "from Candidate s where s.id = :fId")
                    .setParameter("fId", Long.valueOf(2))
                    .executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

}