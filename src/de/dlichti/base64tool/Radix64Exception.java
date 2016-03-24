/*
 * This file is part of the Base64Tool project.
 * 
 * Copyright (c) 2016, David Lichti
 * All rights reserved.
 * 
 * The Base64Tool is published under the BSD-2-Clause license.
 */

package de.dlichti.base64tool;

public class Radix64Exception extends Base64Exception {
	public Radix64Exception (String message) {
		super (message, Base64Encoding.BASE_64);
	}

	private static final long serialVersionUID = 1L;
}
