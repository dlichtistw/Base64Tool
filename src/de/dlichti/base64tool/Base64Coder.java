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

public class Base64Coder {
	protected static final Pattern TRIM_PATTERN = Pattern.compile("\\s+");
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
	 * @param data		the {@code byte[]} to encode
	 * @return			the encoded text
	 */
	public String encode (byte[] data) {
		final char[] b64String = getCharArrayFor(data.length);
		encode(data, b64String, this.encoding);
		return String.copyValueOf(b64String);
	}
	
	/**
	 * Takes binary data and encodes it into a given {@code char} array according to the specified {@code Base64Encoding}.
	 * It is assumed that the {@code char} array is large enough to hold all the encoded data.
	 * The method returns the capacity which has been used to store the encoded data.
	 * The data itself can be found in the {@code char[]} given as parameter.
	 * 
	 * @param data			a {@code byte[]} holding the binary data
	 * @param b64Data		the {@code char[]} to use for the encoded data
	 * @param encoding		an instance of {@code Base64Encoding} used to translate from binary to Base64
	 * @return				the number of character that have been stored in the character array
	 */
	protected static int encode (byte[] data, char[] b64Data, Base64Encoding encoding) {
		final int datLen = data.length;
		
		int datInd = 0; // Index in the byte array representing the plain data
		int b64Ind = 0; // Index in the character array representing the Base64 encoded data
		int bitBuf = 0; // Bit buffer to operate on. Data is loaded from the right and consumed from the left.
		int bufLen = 0; // How many bits are currently loaded into the buffer
		
		boolean done = false;
		while (!done) {
			while (bufLen <= 24) { // load data
				if (datInd == datLen) {
					done = true;
					break;
				}
//				System.out.println(String.format("Buffer: %32s <- %8s", Integer.toBinaryString(bitBuf), Integer.toBinaryString(Byte.toUnsignedInt(data[datInd]))));
				
				bitBuf |= Byte.toUnsignedInt(data[datInd++]) << (24 - bufLen);
				bufLen += 8;
			}
			
//			System.out.println(String.format("Buffer: %32s", Integer.toBinaryString(bitBuf)));

			while (bufLen >= 6) { // store data
//				System.out.println(String.format("Buffer: %32s -> %8s", Integer.toBinaryString(bitBuf), Integer.toBinaryString((bitBuf >>> 26) & 63)));

				// extract the left most bits
				assert b64Ind < b64Data.length;
				b64Data[b64Ind++] = encoding.getChar((bitBuf >>> 26) & 0b111111);
				
				// remove stored bits
				bitBuf = bitBuf << 6;
				bufLen -= 6;
			}
		}
		
		if (bufLen > 0) { // empty buffer
//			System.out.println(String.format("Buffer: %32s -> %8s", Integer.toBinaryString(bitBuf), Integer.toBinaryString((bitBuf >>> 26) & 63)));
			
			assert b64Ind < b64Data.length;
			b64Data[b64Ind++] = encoding.getChar((bitBuf >>> 26) & 0b111111);
			
			bitBuf = bitBuf << 6;
			bufLen = 0;
		}
		
		return b64Ind;
	}
	
	/**
	 * Attempt to decode the specified {@code String} assuming it is encoded using the encoding of the encoder instance.
	 * 
	 * @param encodedData	the {@code String} to decode
	 * @return				the decoded data
	 * @throws Base64Exception 
	 */
	public byte[] decode (String encodedData) throws Base64Exception {
		final char[] trimmedB64 = TRIM_PATTERN.matcher(encodedData).replaceAll("").toCharArray();
		final byte[] data = getByteArrayFor(trimmedB64.length);
		Base64Coder.decode(trimmedB64, data, this.encoding);
		return data;
	}
	protected static int decode (char[] b64Data, byte[] data, Base64Encoding encoding) throws Base64Exception {
		final int b64Len = b64Data.length;
		
		int datInd = 0; // Index in the byte array representing the plain data
		int b64Ind = 0; // Index in the character array representing the Base64 encoded data
		int bitBuf = 0; // Bit buffer to operate on. Data is loaded from the right and consumed from the left.
		int bufLen = 0; // How many bits are currently loaded into the buffer
		
		boolean done = false;
		while (!done) {
			while (bufLen <= 26) { // load data
				if (b64Ind == b64Len) {
					done = true;
					break;
				}
//				System.out.println(String.format("Buffer: %32s <- %8s", Integer.toBinaryString(bitBuf), Integer.toBinaryString(encoding.getValue(b64Data[b64Ind]))));
				
				bitBuf |= encoding.getValue(b64Data[b64Ind++]) << (26 - bufLen);
				bufLen += 6;
			}
			
//			System.out.println(String.format("Buffer: %32s", Integer.toBinaryString(bitBuf)));

			while (bufLen >= 8) { // store data
//				System.out.println(String.format("Buffer: %32s -> %8s", Integer.toBinaryString(bitBuf), Integer.toBinaryString((bitBuf >>> 24) & 255)));

				// extract the left most bits
				assert datInd < data.length;
				data[datInd++] = (byte) ((bitBuf >>> 24) & 0b11111111);
				
				// remove stored bits
				bitBuf = bitBuf << 8;
				bufLen -= 8;
			}
		}
		
		return datInd;
	}
	
	/**
	 * Creates and returns a {@code char[]} which is large enough to hold a Base64 encoding of data with the specified number of bytes.
	 * It is the minimal number of units of size 6 (a Base64 digit) needed to store at least the content of {@code dataLen} units of size 8 (a {@code byte}).
	 * 
	 * @param dataLength	The length of the original data in bytes
	 * @return				A {@code char[]} suitable for Base64 conversion of the specified amount of data
	 */
	protected static char[] getCharArrayFor (int dataLength) {
		return new char[((8 * dataLength) + 5) / 6];
	}
	protected static byte[] getByteArrayFor (int b64Length) {
		return new byte[(6 * b64Length) / 8];
	}
	
	public String getName () {
		return this.encoding.name;
	}
}
