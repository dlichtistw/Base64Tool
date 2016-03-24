/*
 * This file is part of the Base64Tool project.
 * 
 * Copyright (c) 2016, David Lichti
 * All rights reserved.
 * 
 * The Base64Tool is published under the BSD-2-Clause license.
 */

package de.dlichti.base64tool;

import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.dlichti.base64tool.crc.CRC;

public class Base64Coder {	
	public final Base64Encoding encoding;
	
	public Base64Coder () {
		this (Base64Encoding.BASE_64);
	}
	public Base64Coder (Base64Encoding encoding) {
		this.encoding = encoding;
	}

	/**
	 * Encode the specified {@code String} using the encoding of the encoder instance.
	 * 
	 * @param clearText		the {@code String} to encode
	 * @return				the encoded text
	 */
	public String encode (byte[] clearData) {
		return Base64Coder.encode(clearData, this.encoding);
	}

	/**
	 * Attempt to decode the specified {@code String} assuming it is encoded using the encoding of the encoder instance.
	 * 
	 * @param encodedData	the {@code String} to decode
	 * @return				the decoded data
	 * @throws Base64Exception 
	 */
	public byte[] decode (String encodedData) throws Base64Exception {
		return Base64Coder.decode(encodedData, this.encoding);
	}
	
	protected static final BigInteger SIX_BIT_MASK = new BigInteger("111111", 2);
	protected static String encode (byte[] clearData, Base64Encoding encoding) {
		final char[] b64String = new char[4 * ((clearData.length + 2) / 3)];
		
		BigInteger bi;
		final byte[] sbString = new byte[3];
		int padding = 0;
		int bIndex = 0;
		int b64Index = 0;
		while (bIndex < clearData.length) {
			do {
				if (bIndex < clearData.length) {
					sbString[bIndex % 3] = clearData[bIndex++];
				} else {
					sbString[bIndex % 3] = 0;
					padding++;
					bIndex++;
				}
			} while (bIndex % 3 != 0);
			
			bi = new BigInteger(sbString);
			
			for (int i = 3; i >= 0; i--) {
				b64String[b64Index + i] = encoding.getChar(bi.and(SIX_BIT_MASK).intValue());
				bi = bi.shiftRight(6);
			}
			b64Index += 4;
		}
		
		for (int i = 1; i <= padding; i++) {
			b64String[b64String.length - i] = '=';
		}
		
		return String.copyValueOf(b64String);
	}
	
	protected static final Pattern TRIM_PATTERN = Pattern.compile("\\s+");
	protected static final BigInteger EIGHT_BIT_MASK = new BigInteger("11111111", 2);
	protected static byte[] decode (String encodedData, Base64Encoding encoding) throws Base64Exception {
		final Matcher trimmer = TRIM_PATTERN.matcher(encodedData);
		final String trimmedData = trimmer.replaceAll("");
		
		final char[] b64String = trimmedData.toCharArray();
		byte[] bString = new byte[3 * (b64String.length / 4)];
		
		int b64Index = 0;
		int bIndex = 0;
		BigInteger buffer = BigInteger.ZERO;
		byte[] byteBuffer;
		char character;
		int padding = 0;
		while (b64Index < b64String.length) {
			if (padding > 0) throw new Base64Exception("Unexpected data found after regular end of Base64 string.", encoding);
			
			try {
				do {
					character = b64String[b64Index++];
					buffer = buffer.shiftLeft(6);
					if (character == '=') {
						padding++;
					} else {
						buffer = buffer.or(BigInteger.valueOf(encoding.getValue(character)));
					}
				} while (b64Index % 4 != 0);
			} catch (IndexOutOfBoundsException indexEx) {
				throw new Base64Exception(String.format("The encoded data ended unexpectedly after %d characters.", b64Index - 1));
			}
			
			byteBuffer = CRC.bigIntToByteArray(buffer);
			for (int i = 0; i < byteBuffer.length; i++) {
				bString[bIndex + i] = byteBuffer[i];
			}
			bIndex += byteBuffer.length;
			
			buffer = BigInteger.ZERO;
		}
		
		final byte[] tString = new byte[bString.length - padding];
		for (int i = 0; i < tString.length; i++) tString[i] = bString[i];
		
		return tString;
	}
	
	public String getName () {
		return this.encoding.name;
	}
}
