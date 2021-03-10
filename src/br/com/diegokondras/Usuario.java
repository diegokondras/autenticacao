package br.com.diegokondras;

public class Usuario {
	private String identificador;
	private byte sal[];
	private String hash;

	public Usuario(String identificador, byte[] sal, String hash) {
		super();
		this.identificador = identificador;
		this.sal = sal;
		this.hash = hash;
	}

	public byte[] getSal() {
		return sal;
	}

	public String getHash() {
		return hash;
	}

}
