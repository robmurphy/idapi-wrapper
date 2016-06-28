/*
 * Copyright (c) 2014 Actuate Corporation
 */

package com.actuate.aces.idapi.actions.model;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for anonymous complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}include-filters"/>
 *         &lt;element ref="{}exclude-filters"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"includeFilters", "excludeFilters"})
@XmlRootElement(name = "filters")
public class Filters {

	@XmlElement(name = "include-filters", required = true)
	protected IncludeFilters includeFilters;
	@XmlElement(name = "exclude-filters", required = true)
	protected ExcludeFilters excludeFilters;

	/**
	 * Gets the value of the includeFilters property.
	 *
	 * @return possible object is
	 *         {@link IncludeFilters }
	 */
	public IncludeFilters getIncludeFilters() {
		return includeFilters;
	}

	/**
	 * Sets the value of the includeFilters property.
	 *
	 * @param value allowed object is
	 *              {@link IncludeFilters }
	 */
	public void setIncludeFilters(IncludeFilters value) {
		this.includeFilters = value;
	}

	/**
	 * Gets the value of the excludeFilters property.
	 *
	 * @return possible object is
	 *         {@link ExcludeFilters }
	 */
	public ExcludeFilters getExcludeFilters() {
		return excludeFilters;
	}

	/**
	 * Sets the value of the excludeFilters property.
	 *
	 * @param value allowed object is
	 *              {@link ExcludeFilters }
	 */
	public void setExcludeFilters(ExcludeFilters value) {
		this.excludeFilters = value;
	}

}
