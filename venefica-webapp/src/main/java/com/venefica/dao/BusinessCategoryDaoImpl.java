package com.venefica.dao;

import com.venefica.model.BusinessCategory;
import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of {@link BusinessCategoryDao} interface.
 *
 * @author gyuszi
 */
@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class BusinessCategoryDaoImpl extends DaoBase<BusinessCategory> implements BusinessCategoryDao {

    @Override
    @SuppressWarnings("unchecked")
    public List<BusinessCategory> getCategories() {
        List<BusinessCategory> categories = createQuery(""
                + "from " + getDomainClassName() + " c where "
                + "c.hidden = false"
                + "")
                .list();
        return categories;
    }

    @Override
    public BusinessCategory findByName(String name) {
        List<BusinessCategory> categories = createQuery(""
                + "from " + getDomainClassName() + " c where "
                + "c.name = :name"
                + "")
                .setParameter("name", name)
                .list();
        return categories.isEmpty() ? null : categories.get(0);
    }

    @Override
    public Long save(BusinessCategory category) {
        return saveEntity(category);
    }

    @Override
    public boolean hasCategories() {
        Long numCategories = (Long) createQuery(""
                + "select count(c.id) from " + getDomainClassName() + " c where "
                + "c.hidden = false"
                + "").uniqueResult();
        return numCategories > 0;
    }

    @Override
    public BusinessCategory get(Long categoryId) {
        if (categoryId == null) {
            throw new NullPointerException("categoriId");
        }
        return getEntity(categoryId);
    }
}
