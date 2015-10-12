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
 *         &lt;element ref="{}filters"/>
 *         &lt;element ref="{}permission-rules"/>
 *         &lt;element ref="{}folder-rules"/>
 *         &lt;element ref="{}file-rules"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"filters", "permissionRules", "folderRules", "fileRules"})
@XmlRootElement(name = "volume-upload-model")
public class VolumeUploadModel {

	@XmlElement(required = true)
	protected Filters filters;
	@XmlElement(name = "permission-rules", required = true)
	protected PermissionRules permissionRules;
	@XmlElement(name = "folder-rules", required = true)
	protected FolderRules folderRules;
	@XmlElement(name = "file-rules", required = true)
	protected FileRules fileRules;

	/**
	 * Gets the value of the filters property.
	 *
	 * @return possible object is
	 *         {@link Filters }
	 */
	public Filters getFilters() {
		return filters;
	}

	/**
	 * Sets the value of the filters property.
	 *
	 * @param value allowed object is
	 *              {@link Filters }
	 */
	public void setFilters(Filters value) {
		this.filters = value;
	}

	/**
	 * Gets the value of the permissionRules property.
	 *
	 * @return possible object is
	 *         {@link PermissionRules }
	 */
	public PermissionRules getPermissionRules() {
		return permissionRules;
	}

	/**
	 * Sets the value of the permissionRules property.
	 *
	 * @param value allowed object is
	 *              {@link PermissionRules }
	 */
	public void setPermissionRules(PermissionRules value) {
		this.permissionRules = value;
	}

	/**
	 * Gets the value of the folderRules property.
	 *
	 * @return possible object is
	 *         {@link FolderRules }
	 */
	public FolderRules getFolderRules() {
		return folderRules;
	}

	/**
	 * Sets the value of the folderRules property.
	 *
	 * @param value allowed object is
	 *              {@link FolderRules }
	 */
	public void setFolderRules(FolderRules value) {
		this.folderRules = value;
	}

	/**
	 * Gets the value of the fileRules property.
	 *
	 * @return possible object is
	 *         {@link FileRules }
	 */
	public FileRules getFileRules() {
		return fileRules;
	}

	/**
	 * Sets the value of the fileRules property.
	 *
	 * @param value allowed object is
	 *              {@link FileRules }
	 */
	public void setFileRules(FileRules value) {
		this.fileRules = value;
	}

}
