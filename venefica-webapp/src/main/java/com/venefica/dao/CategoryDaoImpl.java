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
public class CategoryDaoImpl extends DaoBase implements CategoryDao {

    @Override
    @SuppressWarnings("unchecked")
    public List<Category> getSubcategories(Long parentCategoryId) {
        List<Category> categories;

        if (parentCategoryId == null) {
            categories = (List<Category>) getCurrentSession().createQuery(
                    "from Category c where c.parent is null").list();
        } else {
            categories = (List<Category>) getCurrentSession()
                    .createQuery("from Category c where c.parent.id = :parentId")
                    .setParameter("parentId", parentCategoryId).list();
        }

        return categories;
    }

    @Override
    public Category findByName(String name) {
        List<?> categories = getCurrentSession()
                .createQuery("from Category c where c.name = :name").setParameter("name", name)
                .list();

        return categories.isEmpty() ? null : (Category) categories.get(0);
    }

    @Override
    public Long save(Category category) {
        return (Long) getCurrentSession().save(category);
    }

    @Override
    public boolean hasCategories() {
        Long numCategories = (Long) getCurrentSession()
                .createQuery("select count(*) from Category").uniqueResult();

        return numCategories > 0;
    }

    @Override
    public Category get(Long categoryId) {
        if (categoryId == null) {
            throw new NullPointerException("categoriId");
        }
        return (Category) getCurrentSession().get(Category.class, categoryId);
    }
}
