package douglas.persistence;

import douglas.domain.Product;
import douglas.domain.Section;
import douglas.domain.Test;
import douglas.testrunner.TestExecution;
import douglas.testrunner.TestQueue;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Repository
public class ProductDao {

    private SessionFactory sessionFactory;
    private TestQueue testQueue;

    @Autowired
    public ProductDao(SessionFactory sessionFactory, TestQueue testQueue) {
        this.sessionFactory = sessionFactory;
        this.testQueue = testQueue;
    }

    @Transactional
    public Product findById(String id) {
        id = id.toLowerCase();
        Long longId = Long.parseLong(id);
        Session session = sessionFactory.getCurrentSession();
        Product product = session.get(Product.class, longId);
        if (product == null) {
            throw new PersistenceException(String.format("Unknown id '%s' for Product", id));
        }
        Hibernate.initialize(product.getSections());

        return product;
    }

    // Add tests to queue and insert divider into queue
    @Transactional
    public void run(String id) {
        List<Test> tests = allTestsInProduct(id);

        testQueue.addAll(tests);
        TestExecution execution = new TestExecution();
        execution.setProductId(id);
        testQueue.add(execution);
    }

    @Transactional
    public List<Test> allTestsInProduct(String id) {
        Product product = findById(id);
        Collection<Long> sectionIds = new ArrayList<Long>();
        for(Section section : product.getSections()) {
            sectionIds.add(section.getId());
        }

        String hql = "from Test where section IN (:sectionIds)";

        Session session = sessionFactory.getCurrentSession();
        Query<Test> query = session.createQuery(hql, Test.class).setParameter("sectionIds", sectionIds);

        return query.getResultList();
    }


    public List<Product> all() {
        Session session = sessionFactory.getCurrentSession();

        List<Product> products = session.createQuery("from Product").list();

        for(Product product : products) {
            Hibernate.initialize(product.getSections());
        }

        return products;
    }

    public void save(Product product) {
        Session session = sessionFactory.getCurrentSession();
        session.save(product);
    }

    public void update(Product product) {
        Session session = sessionFactory.getCurrentSession();
        session.update(product);
    }

    public void delete(Product product) {
        Session session = sessionFactory.getCurrentSession();
        session.delete(product);
    }
}
