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

public class Radix64Coder extends PaddedBase64Coder {
	protected static final CRC CRC24 = CRC.fromKoopman("C3267D");
	
	public Radix64Coder () {
		super (Base64Encoding.BASE_64);
	}
	
	@Override
	public String encode (byte[] data) {
		byte[] checksum = new byte[3];
		if (data != null && data.length > 0) checksum = CRC.bigIntToByteArray(CRC24.checksum(new BigInteger(data)));
		
		return String.format("%s\n=%s", super.encode(data), super.encode(checksum));
	}
	
	protected final static Pattern CHECKSUM_PATTERN = Pattern.compile("(.*)^=(.+)$", Pattern.MULTILINE | Pattern.DOTALL);
	@Override
	public byte[] decode (String encodedData) throws Base64Exception {
		final Matcher checksumFinder = CHECKSUM_PATTERN.matcher(encodedData);
		BigInteger checksum = null;
		if (checksumFinder.find()) {
			checksum = CRC.byteArrayToBigInt(super.decode(checksumFinder.group(2)));
		} else {
			throw new Radix64Exception("Checksum is missing.");
		}
		
		final byte[] decoded = super.decode(checksumFinder.group(1));
		
		if (CRC24.validate(new BigInteger(decoded), checksum)) {
			return decoded;
		} else {
			throw new Radix64Exception("Checksum error");
		}
	}
	
	@Override
	public String getName () {
		return "Radix64";
	}
}
