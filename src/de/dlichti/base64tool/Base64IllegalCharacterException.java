/*
 * This file is part of the Base64Tool project.
 * 
 * Copyright (c) 2016, David Lichti
 * All rights reserved.
 * 
 * The Base64Tool is published under the BSD-2-Clause license.
 */

package de.dlichti.base64tool;

public class Base64IllegalCharacterException extends Base64Exception {
	private static final long serialVersionUID = 1L;

	public final char character;
	
	public Base64IllegalCharacterException (Base64Encoding encoding, char character) {
		super (String.format("The character '%c' is not allowed for %s.", character, encoding.name));
		
		this.character = character;
	}
}
