package douglas.persistence;

import douglas.domain.Test;
import douglas.testrunner.TestQueue;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;
import java.util.List;

@Repository
public class TestDao {

    private SessionFactory sessionFactory;
    private TestQueue testQueue;

    @Autowired
    public TestDao(SessionFactory sessionFactory, TestQueue testQueue) {
        this.sessionFactory = sessionFactory;
        this.testQueue = testQueue;
    }

    @Transactional
    public Test findById(String id) {
        Long longId = Long.parseLong(id);
        Session session = sessionFactory.getCurrentSession();
        Test test = session.get(Test.class, longId);
        if (test == null) {
            throw new PersistenceException(String.format("Unknown id '%s' for Test", id));
        }

        Hibernate.initialize(test.getTestResults());

        return test;
    }

    public List<Test> all() {
        Session session = sessionFactory.getCurrentSession();

        List<Test> tests = session.createQuery("from Test").list();

        return tests;
    }

    public List<Test> allTestsBySection(String sectionId) {
        String hql = "from Test where section = :sectionId";

        Session session = sessionFactory.getCurrentSession();
        Query<Test> query = session.createQuery(hql, Test.class).setParameter("sectionId", sectionId);

        List<Test> res = query.getResultList();

        return res;
    }

    @Transactional
    public void save(Test test) {
        Session session = sessionFactory.getCurrentSession();
        session.save(test);
    }

    @Transactional
    public void update(Test test) {
        Session session = sessionFactory.getCurrentSession();
        session.update(test);
    }

    @Transactional
    public void delete(Test test) {
        Session session = sessionFactory.getCurrentSession();
        session.delete(test);
    }

    // Add test to queue
    @Transactional
    public void run(String testId) {
        Long longId = Long.parseLong(testId);
        Session session = sessionFactory.getCurrentSession();
        Test test = session.get(Test.class, longId);
        if (test == null) {
            throw new PersistenceException(String.format("Unknown id '%s' for Test", testId));
        }

        testQueue.add(test);
    }

}
