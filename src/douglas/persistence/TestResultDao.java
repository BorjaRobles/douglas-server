package douglas.persistence;

import douglas.domain.Test;
import douglas.domain.TestResult;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;
import java.util.List;

@Repository
public class TestResultDao {

    private SessionFactory sessionFactory;

    @Autowired
    public TestResultDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<TestResult> all() {
        Session session = sessionFactory.getCurrentSession();

        List<TestResult> testResults = session.createQuery("from TestResult").list();

        return testResults;
    }

    @Transactional
    public TestResult findById(String id) {
        Long longId = Long.parseLong(id);
        Session session = sessionFactory.getCurrentSession();
        TestResult testResult = session.get(TestResult.class, longId);
        if (testResult == null) {
            throw new PersistenceException(String.format("Unknown id '%s' for TestResult", id));
        }
        return testResult;
    }

    public List<TestResult> allTestResultsByTest(String testId) {
        String hql = "from TestResult where test = :testId";

        Session session = sessionFactory.getCurrentSession();
        Query<TestResult> query = session.createQuery(hql, TestResult.class).setParameter("testId", testId);

        List<TestResult> res = query.getResultList();

        return res;
    }

    @Transactional
    public void save(TestResult testResult) {
        Session session = sessionFactory.getCurrentSession();
        session.save(testResult);
    }

    @Transactional
    public void update(TestResult testResult) {
        Session session = sessionFactory.getCurrentSession();
        session.update(testResult);
    }

    @Transactional
    public void delete(TestResult testResult) {
        Session session = sessionFactory.getCurrentSession();
        session.delete(testResult);
    }
}
