package br.com.diegokondras;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.HashMap;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;       
import javax.crypto.SecretKey;  

public class Controller {
	
	
	HashMap<String, Usuario> usuarios = new HashMap<>();
	
	public boolean usuarioDisponivel(String identificador) {
		if(usuarios.containsKey(identificador))
			return false;
		return true;
	}
	
	public byte[] geraSal() {
	    SecureRandom random = new SecureRandom();
	    byte bytes[] = new byte[20];
	    random.nextBytes(bytes);
	    return bytes;
	}

	public static String criptografa(String strToEncrypt, byte[] sal) throws Exception {
		byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		IvParameterSpec ivSpec = new IvParameterSpec(iv);
		SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
		KeySpec keySpec = new PBEKeySpec(strToEncrypt.toCharArray(), sal, 65536, 256);
		SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);
		SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getEncoded(), "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivSpec);
		return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));
	}
	
	  
	public static String descriptografa(String senha, String strToDecrypt, byte[] sal) throws Exception {
		byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		IvParameterSpec ivSpec = new IvParameterSpec(iv);
		SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
		KeySpec keySpec = new PBEKeySpec(senha.toCharArray(), sal, 65536, 256);
		SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);
		SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getEncoded(), "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
		cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivSpec);
		return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
	}
	
	
	public void cadastraUsuario(String identificador, String senha) throws Exception {
		byte sal[] = geraSal();
		String hash = criptografa((senha.concat(sal.toString())), sal);
		Usuario usuario = new Usuario(identificador, sal, hash);
		usuarios.put(identificador, usuario);
	}

	public boolean entra(String identificador, String senha) throws Exception {
		Usuario usuario = usuarios.get(identificador);
		String senhaSal = senha.concat(usuario.getSal().toString());
		String decSenha = descriptografa(senhaSal, usuario.getHash(), usuario.getSal());
		//System.out.println(decSenha);
		if(senhaSal.equals(decSenha))
			return true;
		return false;
	}
}
