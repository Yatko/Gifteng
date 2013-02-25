package com.venefica.dao;

import com.venefica.model.Category;
import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of {@link CategoryDao} interface.
 *
 * @author Sviatoslav Grebenchukov
 */
@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class CategoryDaoImpl extends DaoBase<Category> implements CategoryDao {

    @Override
    @SuppressWarnings("unchecked")
    public List<Category> getSubcategories(Long parentCategoryId) {
        List<Category> categories;

        if (parentCategoryId == null) {
            categories = createQuery("from Category c where c.parent is null")
                    .list();
        } else {
            categories = createQuery("from Category c where c.parent.id = :parentId")
                    .setParameter("parentId", parentCategoryId)
                    .list();
        }

        return categories;
    }

    @Override
    public Category findByName(String name) {
        List<Category> categories = createQuery("from Category c where c.name = :name")
                .setParameter("name", name)
                .list();

        return categories.isEmpty() ? null : categories.get(0);
    }

    @Override
    public Long save(Category category) {
        return saveEntity(category);
    }

    @Override
    public boolean hasCategories() {
        Long numCategories = (Long) createQuery("select count(*) from Category").uniqueResult();

        return numCategories > 0;
    }

    @Override
    public Category get(Long categoryId) {
        if (categoryId == null) {
            throw new NullPointerException("categoriId");
        }
        return getEntity(categoryId);
    }
}
