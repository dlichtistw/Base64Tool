/*
 * This file is part of the Base64Tool project.
 * 
 * Copyright (c) 2016, David Lichti
 * All rights reserved.
 * 
 * The Base64Tool is published under the BSD-2-Clause license.
 */

package de.dlichti.base64tool;

public abstract class BaseCoder<E> {
	/**
	 * Encode the specified {@code String} using the encoding of the encoder instance.
	 * 
	 * @param data		the {@code byte[]} to encode
	 * @return			the encoded text
	 */
	public abstract E encode (byte[] data);
	
	/**
	 * Attempt to decode the specified {@code String} assuming it is encoded using the encoding of the encoder instance.
	 * 
	 * @param encodedData	the {@code String} to decode
	 * @return				the decoded data
	 * @throws BaseCoderException 
	 */
	public abstract byte[] decode (E encodedData) throws BaseCoderException;
	
	public abstract String getName ();
}
