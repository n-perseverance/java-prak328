package msu.cmc.webprak.DAO;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

@Transactional
public abstract class CommonDAO<T, ID extends Serializable> {

    @Autowired
    private SessionFactory sessionFactory;

    private final Class<T> entityClass;

    public CommonDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    public T getById(ID id) {
        return getCurrentSession().get(entityClass, id);
    }

    public List<T> getAll() {
        return getCurrentSession().createQuery("from " + entityClass.getSimpleName(), entityClass).list();
    }

    public void save(T entity) {
        getCurrentSession().persist(entity);
        getCurrentSession().flush();
    }

    public T update(T entity) {
        return getCurrentSession().merge(entity);
    }

    public void delete(T entity) {
        getCurrentSession().delete(entity);
    }

    public void deleteById(ID id) {
        T entity = getById(id);
        if (entity != null) {
            delete(entity);
        }
    }
}