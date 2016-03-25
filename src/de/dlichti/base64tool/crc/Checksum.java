package de.dlichti.base64tool.crc;

public interface Checksum<E> {
	public E checksum (byte[] data);
}
