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

public class Base64CoderMIME extends PaddedBase64Coder {
	protected static final Pattern BREAK_PATTERN = Pattern.compile(".{76}(?!$)");

	public Base64CoderMIME () {
		super (Base64Encoding.BASE_64, '=');
	}
	
	@Override
	public String encode (byte[] data) {
		return BREAK_PATTERN.matcher(super.encode(data)).replaceAll("$0\r\n");
	}
	
	public String getName () {
		return "MIME";
	}
}
