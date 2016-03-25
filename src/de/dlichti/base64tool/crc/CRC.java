/*
 * This file is part of the Base64Tool project.
 * 
 * Copyright (c) 2016, David Lichti
 * All rights reserved.
 * 
 * The Base64Tool is published under the BSD-2-Clause license.
 */

package de.dlichti.base64tool.crc;

public class CRC implements Checksum<Long> {
	public final Long polynomial; // in Koopman's notation, i.e. without the least significant bit
	public final int polyOrder;
	
	public CRC (long poly) {
		this (new Long(poly));
	}
	public CRC (Long poly) {
		this.polynomial = poly;
		this.polyOrder = 64 - Long.numberOfLeadingZeros(poly);
	}
	
	public Long checksum (byte[] data) {
		final int datLen = data.length * 8;
		
		Long bitBuf = (long) 0;
		int datInd = 0;
		while (datInd < datLen + this.polyOrder - 1) {
			bitBuf ^= datInd < datLen ? ((data[datInd / 8] >>> (7 - (datInd % 8))) & 1) : 0;
			
//			System.out.print(String.format("Buffer: %5s", Long.toBinaryString(bitBuf)));
			
			if ((bitBuf & Long.highestOneBit(this.polynomial)) == 0) {
				bitBuf = bitBuf << 1;
			} else {
				bitBuf ^= this.polynomial;
				bitBuf = bitBuf << 1;
				bitBuf |= 1;
			}
			datInd++;
			
//			System.out.println(String.format(" -> %5s", Long.toBinaryString(bitBuf)));
		}
		
		return bitBuf;
	}
}
