package com.venefica.dao;

import com.venefica.model.Rating;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of {@link RatingDao} interface.
 *
 * @author Sviatoslav Grebenchukov
 *
 */
@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class RatingDaoImpl extends DaoBase<Rating> implements RatingDao {

    @Override
    public Long save(Rating rating) {
        return saveEntity(rating);
    }

    @Override
    public void delete(Rating rating) {
        deleteEntity(rating);
    }
}