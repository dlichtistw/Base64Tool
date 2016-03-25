/*
 * This file is part of the Base64Tool project.
 * 
 * Copyright (c) 2016, David Lichti
 * All rights reserved.
 * 
 * The Base64Tool is published under the BSD-2-Clause license.
 */

package de.dlichti.base64tool;

public abstract class BaseCoderException extends Exception {
	private static final long serialVersionUID = 1L;

	public BaseCoderException () {
		super();
	}

	public BaseCoderException (String message) {
		super(message);
	}

	public BaseCoderException (Throwable cause) {
		super(cause);
	}

	public BaseCoderException (String message, Throwable cause) {
		super(message, cause);
	}

	public BaseCoderException (String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
