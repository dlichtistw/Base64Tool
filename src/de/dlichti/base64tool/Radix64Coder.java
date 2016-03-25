/*
 * This file is part of the Base64Tool project.
 * 
 * Copyright (c) 2016, David Lichti
 * All rights reserved.
 * 
 * The Base64Tool is published under the BSD-2-Clause license.
 */

package de.dlichti.base64tool;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.dlichti.base64tool.crc.CRC;

public class Radix64Coder extends Base64CoderMIME {
	protected static final CRC CRC24 = new CRC(0xC3267D);
	
	@Override
	public String encode (byte[] data) {
		byte[] checksum = new byte[3];
		if (data != null && data.length > 0) checksum = longToByteArray(CRC24.checksum(data));
		
		return String.format("%s\r\n=%s", super.encode(data), super.encode(checksum));
	}
	
	protected final static Pattern CHECKSUM_PATTERN = Pattern.compile("(.*)^=(.+)$", Pattern.MULTILINE | Pattern.DOTALL);
	@Override
	public byte[] decode (String encodedData) throws Base64Exception {
		final Matcher checksumFinder = CHECKSUM_PATTERN.matcher(encodedData);
		long checksum = 0;
		if (checksumFinder.find()) {
			checksum = byteArrayToLong(super.decode(checksumFinder.group(2)));
		} else {
			throw new Radix64Exception("Checksum is missing.");
		}
		
		final byte[] decoded = super.decode(checksumFinder.group(1));
		
		if (checksum == CRC24.checksum(decoded)) {
			return decoded;
		} else {
			throw new Radix64Exception("Checksum error");
		}
	}
	
	protected static byte[] longToByteArray (long ln) {
		final byte[] bcs = new byte[(64 - Long.numberOfLeadingZeros(ln) + 7) / 8];
		for (int i = bcs.length - 1; i >= 0; i--) {
			bcs[i] = (byte) (ln & 0b11111111);
			ln = ln >>> 8;
		}
		return bcs;
	}
	protected static long byteArrayToLong (byte[] byteArray) {
		long ln = 0;
		for (int i = 0; i < byteArray.length; i++) {
			ln = ln << 8;
			ln |= Byte.toUnsignedInt(byteArray[i]);
		}
		return ln;
	}
	
	@Override
	public String getName () {
		return "Radix64";
	}
}
