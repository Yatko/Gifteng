package com.venefica.dao;

import com.venefica.model.User;
import java.util.Date;
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
public class UserDaoImpl extends DaoBase<User> implements UserDao {

    @Override
    public User get(Long id) {
        return getEntity(id);
    }

    @Override
    public User findUserByName(String name) {
        List<User> users = createQuery("from " + getDomainClassName() + " u where u.name=:name")
                .setParameter("name", name)
                .list();

        return users.isEmpty() ? null : users.get(0);
    }

    @Override
    public User findUserByEmail(String email) {
        List<User> users = createQuery("from " + getDomainClassName() + " u where u.email=:email")
                .setParameter("email", email)
                .list();

        return users.isEmpty() ? null : users.get(0);
    }

    @Override
    public User findUserByPhoneNumber(String phoneNumber) {
        List<User> users = createQuery("from " + getDomainClassName() + " u where u.phoneNumber=:phoneNumber")
                .setParameter("phoneNumber", phoneNumber)
                .list();

        return users.isEmpty() ? null : users.get(0);
    }

    @Override
    public Long save(User user) {
        user.setJoinedAt(new Date());
        return saveEntity(user);
    }

    @Override
    public void update(User user) {
        updateEntity(user);
    }

    @Override
    public boolean removeByName(String name) {
        int numUserDeleted = createQuery("delete from " + getDomainClassName() + " u where u.name = :name")
                .setParameter("name", name)
                .executeUpdate();

        return numUserDeleted != 0;
    }

    @Override
    public boolean removeByEmail(String email) {
        int numUserDeleted = createQuery("delete from " + getDomainClassName() + " u where u.email = :email")
                .setParameter("email", email)
                .executeUpdate();

        return numUserDeleted != 0;
    }

    @Override
    public Long getMaxUserId() {
        Long maxId = (Long) createQuery("select max(u.id) from " + getDomainClassName() + " u").uniqueResult();
        return maxId != null ? maxId : 0;
    }
}
