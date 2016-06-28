/*
 * Copyright (c) 2014 Actuate Corporation
 */

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
 *         &lt;element ref="{}permission-rule" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"permissionRule"})
@XmlRootElement(name = "permission-rules")
public class PermissionRules {

	@XmlElement(name = "permission-rule")
	protected List<PermissionRule> permissionRule;

	/**
	 * Gets the value of the permissionRule property.
	 * <p/>
	 * <p/>
	 * This accessor method returns a reference to the live list,
	 * not a snapshot. Therefore any modification you make to the
	 * returned list will be present inside the JAXB object.
	 * This is why there is not a <CODE>set</CODE> method for the permissionRule property.
	 * <p/>
	 * <p/>
	 * For example, to add a new item, do as follows:
	 * <pre>
	 *    getPermissionRule().add(newItem);
	 * </pre>
	 * <p/>
	 * <p/>
	 * <p/>
	 * Objects of the following type(s) are allowed in the list
	 * {@link PermissionRule }
	 */
	public List<PermissionRule> getPermissionRule() {
		if (permissionRule == null) {
			permissionRule = new ArrayList<PermissionRule>();
		}
		return this.permissionRule;
	}

}
