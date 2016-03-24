/*
 * This file is part of the Base64Tool project.
 * 
 * Copyright (c) 2016, David Lichti
 * All rights reserved.
 * 
 * The Base64Tool is published under the BSD-2-Clause license.
 */

package de.dlichti.base64tool;

public class Base64Exception extends Exception {
	private static final long serialVersionUID = 1L;
	
	public final Base64Encoding encoding;
	
	public Base64Exception () {
		this.encoding = null;
	}
	public Base64Exception (Base64Encoding encoding) {
		this.encoding = encoding;
	}
	
	public Base64Exception (String message, Base64Encoding encoding) {
		super (message);
		this.encoding = encoding;
	}
	public Base64Exception (String message) {
		this (message, null);
	}
}
