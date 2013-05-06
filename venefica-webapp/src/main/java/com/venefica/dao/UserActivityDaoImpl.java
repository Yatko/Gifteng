package com.venefica.dao;

import com.venefica.model.UserActivity;
import java.util.Date;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of {@link UserActivityDao} interface.
 *
 * @author gyuszi
 *
 */
@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class UserActivityDaoImpl extends DaoBase<UserActivity> implements UserActivityDao {

    @Override
    public Long save(UserActivity activity) {
        activity.setDateTime(new Date());
        return saveEntity(activity);
    }
}
