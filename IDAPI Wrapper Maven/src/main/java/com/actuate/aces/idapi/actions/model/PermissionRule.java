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
 *         &lt;element ref="{}permissions"/>
 *       &lt;/sequence>
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"permissions"})
@XmlRootElement(name = "permission-rule")
public class PermissionRule {

	@XmlElement(required = true)
	protected Permissions permissions;
	@XmlAttribute
	protected String name;

	/**
	 * Gets the value of the permissions property.
	 *
	 * @return possible object is
	 *         {@link Permissions }
	 */
	public Permissions getPermissions() {
		return permissions;
	}

	/**
	 * Sets the value of the permissions property.
	 *
	 * @param value allowed object is
	 *              {@link Permissions }
	 */
	public void setPermissions(Permissions value) {
		this.permissions = value;
	}

	/**
	 * Gets the value of the name property.
	 *
	 * @return possible object is
	 *         {@link String }
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the value of the name property.
	 *
	 * @param value allowed object is
	 *              {@link String }
	 */
	public void setName(String value) {
		this.name = value;
	}

}
