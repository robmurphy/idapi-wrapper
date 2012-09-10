package com.actuate.aces.idapi.actions.model;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for anonymous complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType>
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *       &lt;attribute name="user" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="rights" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="role" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"value"})
@XmlRootElement(name = "permission")
public class Permission {

	@XmlValue
	protected String value;
	@XmlAttribute
	protected String user;
	@XmlAttribute
	protected String rights;
	@XmlAttribute
	protected String role;

	/**
	 * Gets the value of the value property.
	 *
	 * @return possible object is
	 *         {@link String }
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Sets the value of the value property.
	 *
	 * @param value allowed object is
	 *              {@link String }
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * Gets the value of the user property.
	 *
	 * @return possible object is
	 *         {@link String }
	 */
	public String getUser() {
		return user;
	}

	/**
	 * Sets the value of the user property.
	 *
	 * @param value allowed object is
	 *              {@link String }
	 */
	public void setUser(String value) {
		this.user = value;
	}

	/**
	 * Gets the value of the rights property.
	 *
	 * @return possible object is
	 *         {@link String }
	 */
	public String getRights() {
		return rights;
	}

	/**
	 * Sets the value of the rights property.
	 *
	 * @param value allowed object is
	 *              {@link String }
	 */
	public void setRights(String value) {
		this.rights = value;
	}

	/**
	 * Gets the value of the role property.
	 *
	 * @return possible object is
	 *         {@link String }
	 */
	public String getRole() {
		return role;
	}

	/**
	 * Sets the value of the role property.
	 *
	 * @param value allowed object is
	 *              {@link String }
	 */
	public void setRole(String value) {
		this.role = value;
	}

}
