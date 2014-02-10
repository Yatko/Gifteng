package com.venefica.dao;

import com.venefica.model.Viewer;
import java.util.Date;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of {@link ViewerDao} interface.
 *
 * @author gyuszi
 */
@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class ViewerDaoImpl extends DaoBase<Viewer> implements ViewerDao {

    @Override
    public Long save(Viewer viewer) {
        viewer.setViewedAt(new Date());
        return saveEntity(viewer);
    }
}
