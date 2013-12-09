package com.venefica.model;

import java.util.LinkedHashSet;
import java.util.Set;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;

/**
 * Describes an advertisement category. Subcategories are also supported.
 *
 * @author Sviatoslav Grebenchukov
 */
@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Access(AccessType.PROPERTY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @ForeignKey(name = "category_parent_fk")
    private Category parent;
    
    @OneToMany(mappedBy = "parent")
    @OrderBy
    private Set<Category> subcategories;
    
    @Column(nullable = false)
    @Index(name = "idx_name")
    private String name;
    
    // private BigDecimal fixedPrice;
    // private String fixedCurrency;
    
    @Index(name = "idx_hidden")
    private boolean hidden;

    public Category() {
        subcategories = new LinkedHashSet<Category>();
    }

    public Category(Category parent, String name) {
        this.parent = parent;
        this.name = name;
        subcategories = new LinkedHashSet<Category>();
    }

    public Long getId() {
        return id;
    }

    @SuppressWarnings("unused")
    private void setId(Long id) {
        this.id = id;
    }

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public Set<Category> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(Set<Category> subcategories) {
        this.subcategories = subcategories;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }
}
