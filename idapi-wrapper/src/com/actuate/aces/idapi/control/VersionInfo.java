/*
 * Copyright (c) 2014 Actuate Corporation
 */

package com.actuate.aces.idapi.control;

public class VersionInfo {

	// corresponds to NAMESPACE version # used
	public static final int IDAPI_NAMESPACE_VERSION = 11;

	// corresponds to Actuate version of libraries
	public static final int ACTUATE_MAJOR_VERSION = 23;

	// corresponds to Actuate service pack version of libraries
	public static final int ACTUATE_MINOR_VERSION = 0;

	// corresponds to major version of this wrapper
	public static final int WRAPPER_MAJOR_VERSION = 3;

	// corresponds to minor version of this wrapper
	public static final int WRAPPER_MINOR_VERSION = 0;

	// corresponds to sub version of this wrapper
	public static final int WRAPPER_SUB_VERSION = 0;

	// indicates wether the current release version is a production or beta/alpha release
	public static final boolean PRODUCTION_RELEASE = false;


	public static int getNamespaceVersion() {
		return IDAPI_NAMESPACE_VERSION;
	}

	public static int getActuateMajorVersion() {
		return ACTUATE_MAJOR_VERSION;
	}

	public static int getActuateMinorVersion() {
		return ACTUATE_MINOR_VERSION;
	}

	public static int getWrapperMajorVersion() {
		return WRAPPER_MAJOR_VERSION;
	}

	public static int getWrapperMinorVersion() {
		return WRAPPER_MINOR_VERSION;
	}

	public static int getWrapperSubVersion() {
		return WRAPPER_SUB_VERSION;
	}

	public static boolean isProductionRelease() {
		return PRODUCTION_RELEASE;
	}

	public static String getVersionString() {
		String productionTag = "";
		if (!isProductionRelease())
			productionTag = " - Pre Production";

		if (getWrapperSubVersion() == 0)
			return ACTUATE_MAJOR_VERSION + "." + ACTUATE_MINOR_VERSION + "." + WRAPPER_MAJOR_VERSION + "." + WRAPPER_MINOR_VERSION + productionTag;
		else
			return ACTUATE_MAJOR_VERSION + "." + ACTUATE_MINOR_VERSION + "." + WRAPPER_MAJOR_VERSION + "." + WRAPPER_MINOR_VERSION + "." + WRAPPER_SUB_VERSION + productionTag;

	}
}
