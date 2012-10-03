package com.venefica.service.dto;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import com.venefica.model.Category;


/**
 * Category data transfer object.
 * 
 * @author Sviatoslav Grebenchukov
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CategoryDto extends DtoBase {
	// out
	private Long id;
	// out
	private Long parentId;
	// out
	@XmlElementWrapper(name = "subcategories")
	@XmlElement(name = "category")
	private List<CategoryDto> subcategories;
	// out
	private String name;

	// required for JAX-WS
	public CategoryDto() {
		subcategories = new LinkedList<CategoryDto>();
	}

	public CategoryDto(Category category, boolean includeSubcategories) {
		subcategories = new LinkedList<CategoryDto>();

		this.id = category.getId();
		this.parentId = category.getParent() != null ? category.getParent().getId() : null;
		this.name = category.getName();

		if (includeSubcategories) {
			for (Category subcategory : category.getSubcategories()) {
				CategoryDto subcategoryDto = new CategoryDto(subcategory, true);
				subcategories.add(subcategoryDto);
			}
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
