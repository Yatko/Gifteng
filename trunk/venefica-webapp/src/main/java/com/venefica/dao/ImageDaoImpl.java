package com.venefica.dao;

import com.venefica.model.Image;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of {@link ImageDao} interface.
 *
 * @author Sviatoslav Grebenchukov
 */
@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class ImageDaoImpl extends DaoBase<Image> implements ImageDao {

    @Override
    public Long save(Image image) {
        return saveEntity(image);
    }

    @Override
    public Image get(Long imageId) {
        return getEntity(imageId);
    }

    @Override
    public void delete(Image image) {
        deleteEntity(image);
    }
}
