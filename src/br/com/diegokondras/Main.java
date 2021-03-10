package br.com.diegokondras;

import java.util.Scanner;

import javax.crypto.BadPaddingException;

public class Main {

	public static void main(String[] args) {
		Controller controller = new Controller();
		String option = "";
		while(true) {
			System.out.println("1 Cadastro\n2 Entrar\n3 Sair");
			Scanner scanner = new Scanner(System.in);
			option = scanner.nextLine();
			if(option.equals("3"))
				break;
			cadastroLogin(option, controller);
		}
	}
	
	private static void cadastroLogin(String option, Controller controller) {
		Scanner scanner = new Scanner(System.in);
		if(option.equals("1")) {
			System.out.println("Identificador:\n");
			String identificador = scanner.nextLine();
			
			if(controller.usuarioDisponivel(identificador)) {
				System.out.println("Senha:\n");
				String senha = scanner.nextLine();
				try {
					controller.cadastraUsuario(identificador, senha);
					System.out.println("Usu�rio foi cadastrado com sucesso");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else {
				System.out.println("Erro: usu�rio j� cadastrado");
			}
		}
		else if(option.equals("2")) {
				System.out.println("Identificador:\n");
				String identificador = scanner.nextLine();
				if(controller.usuarioDisponivel(identificador) == false) {
					System.out.println("Senha:\n");
					String senha = scanner.nextLine();
					try {
						if(controller.entra(identificador, senha)) {
							System.out.println("Voc� est� autenticado");
						}
						else {
							System.out.println("Erro ao autenticar usu�rio");
						}
						
					} catch (BadPaddingException e) {
						System.out.println("Senha inv�lida");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				else {
					System.out.println("Erro: usu�rio n�o est� cadastrado");
				}
		}
		else {
			System.out.println("Op��o inv�lida");
		}
		
	}

}
