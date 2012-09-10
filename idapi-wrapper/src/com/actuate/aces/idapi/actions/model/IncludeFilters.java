package com.actuate.aces.idapi.actions.model;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


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
 *         &lt;element ref="{}include-filter" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"includeFilter"})
@XmlRootElement(name = "include-filters")
public class IncludeFilters {

	@XmlElement(name = "include-filter")
	protected List<String> includeFilter;

	/**
	 * Gets the value of the includeFilter property.
	 * <p/>
	 * <p/>
	 * This accessor method returns a reference to the live list,
	 * not a snapshot. Therefore any modification you make to the
	 * returned list will be present inside the JAXB object.
	 * This is why there is not a <CODE>set</CODE> method for the includeFilter property.
	 * <p/>
	 * <p/>
	 * For example, to add a new item, do as follows:
	 * <pre>
	 *    getIncludeFilter().add(newItem);
	 * </pre>
	 * <p/>
	 * <p/>
	 * <p/>
	 * Objects of the following type(s) are allowed in the list
	 * {@link String }
	 */
	public List<String> getIncludeFilter() {
		if (includeFilter == null) {
			includeFilter = new ArrayList<String>();
		}
		return this.includeFilter;
	}

}
