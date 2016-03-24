/*
 * This file is part of the Base64Tool project.
 * 
 * Copyright (c) 2016, David Lichti
 * All rights reserved.
 * 
 * The Base64Tool is published under the BSD-2-Clause license.
 */

package de.dlichti.base64tool.crc;

import java.math.BigInteger;

public class CRC {
	protected final BigInteger polynomial;
	
	public CRC (BigInteger poly) {
		CRC.checkPositive(poly);
		if (!poly.testBit(0)) throw new IllegalArgumentException("The lowest bit of a CRC polynomial must be 1");
		
		this.polynomial = poly;
	}
	
	public BigInteger checksum (BigInteger message) {
		CRC.checkPositive(message, false);
		
		final BigInteger pMessage = message.shiftLeft(this.polynomial.bitLength() - 1);
		return CRC.cycle(pMessage, this.polynomial);
	}
	public boolean validate (BigInteger message, BigInteger checksum) {
		CRC.checkPositive(message, false);
		if (this.polynomial.bitLength() - 1 < checksum.bitLength()) throw new IllegalArgumentException(String.format("The checksum's length (%d, args) does not correspond to the polynomial's length (%d).", checksum.bitLength(), this.polynomial.bitLength()));
		
		final BigInteger pMessage = message.shiftLeft(this.polynomial.bitLength() - 1).or(checksum);
		return CRC.cycle(pMessage, this.polynomial).equals(BigInteger.ZERO);
	}
	
	public static byte[] bigIntToByteArray (BigInteger bigInt) {
		byte[] bChecksum = new byte[3];
		for (int i = 2; i >= 0; i--) {
			bChecksum[i] = bigInt.byteValue();
			bigInt = bigInt.shiftRight(8);
		}
		return bChecksum;
	}

	public static BigInteger byteArrayToBigInt (byte[] byteArray) {
		BigInteger bigInt = BigInteger.ZERO;
		for (int i = 0; i < byteArray.length; i++) {
			bigInt = bigInt.shiftLeft(8);
			bigInt = bigInt.or(BigInteger.valueOf(Byte.toUnsignedInt(byteArray[i])));
		}
		return bigInt;
	}

	protected static BigInteger cycle (BigInteger bi, BigInteger poly) {
		BigInteger remainder = bi;
		final int rl = remainder.bitLength();
		
		final int cl = poly.bitLength() - 1;
		BigInteger sPoly = poly.shiftLeft(rl - (cl + 1));
		
		for (int i = rl - 1; i >= cl; i--) {
			if (remainder.testBit(i)) {
				remainder = remainder.xor(sPoly);
			}
			sPoly = sPoly.shiftRight(1);
		}
		
		return remainder;
	}
	
	/**
	 * Constructs a CRC checksum algorithm using the polynomial corresponding to the string representation according to the Koopman notation.
	 * That is the hexadecimal representation of the coefficients of the binary polynomial, omitting the lowest coefficient (which is always 1).
	 * E.g.: 1x^5 + 1x^4 + 0x^3 + 1x^2 + 0x^1 +1x^0 <-> 110101 <-> 11010 <-> 1A
	 * 
	 * @param rep	A {@code String} representing the binary CRC polynomial in Koopman notation.
	 * @return		A {@code CRC} object using the specified polynomial.
	 */
	public static CRC fromKoopman (String rep) {
		return new CRC(new BigInteger(rep, 16).shiftLeft(1).setBit(0));
	}
	
	protected static void checkPositive (BigInteger bi, boolean strict) {
		if (bi.signum() < (strict ? 1 : 0)) throw new IllegalArgumentException("BigInteger representations must be positive.");
	}
	protected static void checkPositive (BigInteger bi) {
		CRC.checkPositive(bi, true);
	}
}
