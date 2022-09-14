package Util;

public class Encryption {
	
	public static String Encrypt(String text) {
		String encrypted = "";
		for (int i = 0; i < text.length(); i++) {
			encrypted += (char)(text.charAt(i) + 1);
		}
		return encrypted;
	}

	public static String Decrypt(String text) {
		String decrypted = "";
		for (int i = 0; i < text.length(); i++) {
			decrypted += (char)(text.charAt(i) - 1);
		}
		return decrypted;
	}
}
