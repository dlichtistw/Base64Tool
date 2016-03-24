/*
 * This file is part of the Base64Tool project.
 * 
 * Copyright (c) 2016, David Lichti
 * All rights reserved.
 * 
 * The Base64Tool is published under the BSD-2-Clause license.
 */

package de.dlichti.base64tool;

public enum Base64Encoding {
	BASE_64 ("Base64", new char[]{
			'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
			'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
			'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'
	}),
	BASE_64_URL ("Base64url", new char[] {
			'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
			'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
			'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', '_'
	});
	
	/**
	 * This represents the display name of the encoding.
	 */
	public final String name;
	
	/**
	 * A 64 character long array defining how to map the integers from 0 to 63 to ASCII characters.
	 */
	protected final char[] mapping;
		
	private Base64Encoding (String name, char[] mapping) {
		this.name = name;
		this.mapping = mapping;
	}
	private Base64Encoding (String name, char[] mapping, char padding) {
		this.name = name;
		this.mapping = mapping;
	}
	
	public char getChar (int index) {
		if (index < 0 || index > 63) throw new IndexOutOfBoundsException("A Base64 encoding has base 64.");
		return this.mapping[index];
	}
	
	public int getValue (char character) throws Base64IllegalCharacterException {
		for (int i = 0; i < 64; i++) {
			if (this.mapping[i] == character) return i;
		}
		throw new Base64IllegalCharacterException(this, character);
	}
}
