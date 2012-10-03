package com.venefica.service.dto.builder;

import com.venefica.service.dto.DtoBase;

/**
 * Base DTO object builder.
 * 
 * @author Sviatoslav Grebenchukov
 * 
 * @param <M>
 *            model class
 * @param <D>
 *            dto class
 */
public abstract class DtoBuilderBase<M, D extends DtoBase> {
	protected M model;	

	public DtoBuilderBase(M model) {
		if (model == null)
			throw new NullPointerException("model");

		this.model = model;
	}
	
	public abstract D build();
}
