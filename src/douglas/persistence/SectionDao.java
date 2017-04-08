package douglas.persistence;

import douglas.domain.Section;
import douglas.domain.Test;
import douglas.testrunner.TestQueue;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.PersistenceException;
import java.util.List;

@Repository
public class SectionDao {

    private SessionFactory sessionFactory;
    private TestQueue testQueue;

    @Autowired
    public SectionDao(SessionFactory sessionFactory, TestQueue testQueue) {
        this.sessionFactory = sessionFactory;
        this.testQueue = testQueue;
    }

    public Section findById(String id) {
        Session session = sessionFactory.getCurrentSession();
        Section section = session.get(Section.class, id);
        if (section == null) {
            throw new PersistenceException(String.format("Unknown id '%s' for Section", id));
        }
        return section;
    }

    // Add tests to queue
    public void run(String sectionId) {
        Long longId = Long.parseLong(sectionId);

        String hql = "from Test where section = :sectionId";

        Session session = sessionFactory.getCurrentSession();
        Query<Test> query = session.createQuery(hql, Test.class).setParameter("sectionId", longId);

        List<Test> tests = query.getResultList();

        for(Test test : tests) {
            Hibernate.initialize(test.getTestSteps());
        }

        testQueue.addAll(tests);
    }

    public List<Section> allSectionsByProductId(String productId) {
        Long longId = Long.parseLong(productId);
        String hql = "from Section where product = :productId";

        Session session = sessionFactory.getCurrentSession();
        Query<Section> query = session.createQuery(hql, Section.class).setParameter("productId", longId);

        List<Section> res = query.getResultList();

        for(Section section : res) {
            Hibernate.initialize(section.getTests());
        }

        return res;
    }

    public void save(Section section) {
        Session session = sessionFactory.getCurrentSession();
        session.save(section);
    }

    public void update(Section section) {
        Session session = sessionFactory.getCurrentSession();
        session.update(section);
    }

    public void delete(Section section) {
        Session session = sessionFactory.getCurrentSession();
        session.delete(section);
    }

}
