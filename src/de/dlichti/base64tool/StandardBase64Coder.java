package de.dlichti.base64tool;

public class StandardBase64Coder extends PaddedBase64Coder {

	public StandardBase64Coder () {
		super (Base64Encoding.BASE_64, '=');
	}
	
	public String getName () {
		return "Standard Base64";
	}
}
