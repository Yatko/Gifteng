package com.venefica.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.venefica.model.SpamMark;


/**
 * Implementation of {@link SpamMarkDao} interface.
 * 
 * @author Sviatoslav Grebenchukov
 */
@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class SpamMarkDaoImpl extends DaoBase implements SpamMarkDao {

	@Override
	public Long save(SpamMark mark) {
		return saveEntity(mark);
	}

	@Override
	public void delete(SpamMark mark) {
		deleteEntity(mark);
	}

}
