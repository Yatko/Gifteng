package com.venefica.dao;

import com.venefica.model.SpamMark;
import java.util.Date;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of {@link SpamMarkDao} interface.
 *
 * @author Sviatoslav Grebenchukov
 */
@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class SpamMarkDaoImpl extends DaoBase<SpamMark> implements SpamMarkDao {

    @Override
    public Long save(SpamMark mark) {
        mark.setMarkedAt(new Date());
        return saveEntity(mark);
    }

    @Override
    public void delete(SpamMark mark) {
        deleteEntity(mark);
    }
}
