package com.venefica.dao;

import com.venefica.model.User;
import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of
 *
 * @{link {@link UserDao}.
 *
 * @author Sviatoslav Grebenchukov
 */
@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class UserDaoImpl extends DaoBase implements UserDao {

    @Override
    public User get(Long id) {
        return (User) getCurrentSession().get(User.class, id);
    }

    @Override
    public User findUserByName(String name) {
        @SuppressWarnings("rawtypes")
        List users = getCurrentSession().createQuery("from User where name=:name")
                .setParameter("name", name).list();

        return users.isEmpty() ? null : (User) users.get(0);
    }

    @Override
    public User findUserByEmail(String email) {
        @SuppressWarnings("rawtypes")
        List users = getCurrentSession().createQuery("from User where email=:email")
                .setParameter("email", email).list();

        return users.isEmpty() ? null : (User) users.get(0);
    }

    @Override
    public User findUserByPhoneNumber(String phoneNumber) {
        @SuppressWarnings("rawtypes")
        List users = getCurrentSession().createQuery("from User where phoneNumber=:phoneNumber")
                .setParameter("phoneNumber", phoneNumber).list();

        return users.isEmpty() ? null : (User) users.get(0);
    }

    @Override
    public Long save(User user) {
        return (Long) getCurrentSession().save(user);
    }

    @Override
    public void update(User user) {
        getCurrentSession().update(user);
    }

    @Override
    public boolean removeByName(String name) {
        int numUserDeleted = getCurrentSession()
                .createQuery("delete from User u where u.name = :name").setParameter("name", name)
                .executeUpdate();

        return numUserDeleted != 0;
    }

    @Override
    public boolean removeByEmail(String email) {
        int numUserDeleted = getCurrentSession()
                .createQuery("delete from User u where u.email = :email")
                .setParameter("email", email).executeUpdate();

        return numUserDeleted != 0;
    }

    @Override
    public Long getMaxUserId() {
        Long maxId = (Long) getCurrentSession().createQuery("select max(u.id) from User u")
                .uniqueResult();
        return maxId != null ? maxId : 0;
    }
}
