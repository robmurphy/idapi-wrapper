/*
 * Copyright (c) 2014 Actuate Corporation
 */

package com.actuate.aces.idapi.control;

public class ActuateException extends Exception {

	public ActuateException() {
		super();
	}

	public ActuateException(String message) {
		super(message);
	}

	public ActuateException(String message, Throwable cause) {
		super(message, cause);
	}

	public ActuateException(Throwable cause) {
		super(cause);
	}
}
