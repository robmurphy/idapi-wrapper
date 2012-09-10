package com.actuate.aces.idapi.actions.model;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the com.actuate.aces.idapi.actions.model package.
 * <p>An ObjectFactory allows you to programatically
 * construct new instances of the Java representation
 * for XML content. The Java representation of XML
 * content can consist of schema derived interfaces
 * and classes representing the binding of schema
 * type definitions, element declarations and model
 * groups.  Factory methods for each of these are
 * provided in this class.
 */
@XmlRegistry
public class ObjectFactory {

	private final static QName _IncludeFilter_QNAME = new QName("", "include-filter");
	private final static QName _ExcludeFilter_QNAME = new QName("", "exclude-filter");

	/**
	 * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.actuate.aces.idapi.actions.model
	 */
	public ObjectFactory() {
	}

	/**
	 * Create an instance of {@link Permissions }
	 */
	public Permissions createPermissions() {
		return new Permissions();
	}

	/**
	 * Create an instance of {@link VolumeUploadModel }
	 */
	public VolumeUploadModel createVolumeUploadModel() {
		return new VolumeUploadModel();
	}

	/**
	 * Create an instance of {@link IncludeFilters }
	 */
	public IncludeFilters createIncludeFilters() {
		return new IncludeFilters();
	}

	/**
	 * Create an instance of {@link FolderRules }
	 */
	public FolderRules createFolderRules() {
		return new FolderRules();
	}

	/**
	 * Create an instance of {@link FolderRule }
	 */
	public FolderRule createFolderRule() {
		return new FolderRule();
	}

	/**
	 * Create an instance of {@link Permission }
	 */
	public Permission createPermission() {
		return new Permission();
	}

	/**
	 * Create an instance of {@link ExcludeFilters }
	 */
	public ExcludeFilters createExcludeFilters() {
		return new ExcludeFilters();
	}

	/**
	 * Create an instance of {@link Filters }
	 */
	public Filters createFilters() {
		return new Filters();
	}

	/**
	 * Create an instance of {@link FileRule }
	 */
	public FileRule createFileRule() {
		return new FileRule();
	}

	/**
	 * Create an instance of {@link PermissionRules }
	 */
	public PermissionRules createPermissionRules() {
		return new PermissionRules();
	}

	/**
	 * Create an instance of {@link FileRules }
	 */
	public FileRules createFileRules() {
		return new FileRules();
	}

	/**
	 * Create an instance of {@link PermissionRule }
	 */
	public PermissionRule createPermissionRule() {
		return new PermissionRule();
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
	 */
	@XmlElementDecl(namespace = "", name = "include-filter")
	public JAXBElement<String> createIncludeFilter(String value) {
		return new JAXBElement<String>(_IncludeFilter_QNAME, String.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
	 */
	@XmlElementDecl(namespace = "", name = "exclude-filter")
	public JAXBElement<String> createExcludeFilter(String value) {
		return new JAXBElement<String>(_ExcludeFilter_QNAME, String.class, null, value);
	}

}
