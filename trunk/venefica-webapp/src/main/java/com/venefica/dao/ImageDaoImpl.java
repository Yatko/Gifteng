package com.venefica.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.venefica.model.Image;


/**
 * Implementation of {@link ImageDao} interface.
 * 
 * @author Sviatoslav Grebenchukov
 */
@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class ImageDaoImpl extends DaoBase implements ImageDao {

	@Override
	public Long save(Image image) {
		return saveEntity(image);
	}

	@Override
	public Image get(Long imageId) {
		return (Image) getEntity(Image.class, imageId);
	}

	@Override
	public void delete(Image image) {
		deleteEntity(image);
	}
}
