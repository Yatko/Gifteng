package com.venefica.model;

import java.util.LinkedList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import org.hibernate.annotations.ForeignKey;
//import javax.persistence.SequenceGenerator;

/**
 * Describes an advertisement category. Subcategories are also supported.
 *
 * @author Sviatoslav Grebenchukov
 */
@Entity
@Table(name = "category")
//@SequenceGenerator(name = "cat_gen", sequenceName = "cat_seq", allocationSize = 1)
public class Category {

    @Id
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cat_gen")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @ManyToOne
    @ForeignKey(name = "category_parent_fk")
    private Category parent;
    
    @OneToMany(mappedBy = "parent")
    @OrderBy
    private List<Category> subcategories;
    
    @Column(nullable = false)
    private String name;
    
    // private BigDecimal fixedPrice;
    // private String fixedCurrency;
    
    private boolean hidden;

    public Category() {
        subcategories = new LinkedList<Category>();
    }

    public Category(Category parent, String name) {
        this.parent = parent;
        this.name = name;
        subcategories = new LinkedList<Category>();
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

    public List<Category> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(List<Category> subcategories) {
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
