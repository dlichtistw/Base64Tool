/*
 * This file is part of the Base64Tool project.
 * 
 * Copyright (c) 2016, David Lichti
 * All rights reserved.
 * 
 * The Base64Tool is published under the BSD-2-Clause license.
 */

package de.dlichti.base64tool;

import java.util.regex.Pattern;

public class PaddedBase64Coder extends Base64Coder {
	protected char padding;
	
	public PaddedBase64Coder () {
		this (Base64Encoding.BASE_64);
	}
	public PaddedBase64Coder (Base64Encoding encoding) {
		this (encoding, '=');
	}
	public PaddedBase64Coder (Base64Encoding encoding, char padding) {
		super (encoding);
		
		this.padding = padding;
	}
	
	@Override
	/**
	 * {@inheritDoc}
	 */
	public String encode (byte[] data) {
		final char[] b64String = getCharArrayFor(data.length);
		int b64Len = encode(data, b64String, this.encoding);
		
		while (b64Len < b64String.length) b64String[b64Len++] = this.padding;
		
		return String.copyValueOf(b64String);
	}
	
	protected final static Pattern PADDING_PATTERN = Pattern.compile("(=\\s*){0,2}$");
	@Override
	/**
	 * {@inheritDoc}
	 */
	public byte[] decode (String b64Data) throws Base64Exception {
		return super.decode(PADDING_PATTERN.matcher(b64Data).replaceFirst(""));
	}
	
	/**
	 * Creates and returns a {@code char[]} which is large enough to hold a Base64 encoding of data with the specified number of bytes.
	 * It is large enough to store at least the content of {@code dataLen} units of size 8 (a {@code byte}) in units of size 6 (a base64 digit) and a multiple of 24 (the least common multiple of 6 and 8).
	 * 
	 * @param dataLength	The length of the original data in bytes
	 * @return				A {@code char[]} suitable for Base64 conversion of the specified amount of data
	 */
	protected static char[] getCharArrayFor (int dataLength) {
		return new char[4 * (((dataLength) + 2) / 3)];
	}
}
