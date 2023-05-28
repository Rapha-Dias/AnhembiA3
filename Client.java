import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class Client {
    private static final String HOST = "localhost";
    private static final int PORT = 8080;

    private String usuario;

    public static void main(String[] args) {
        Client client = new Client();
        client.initGame();
        client.run();
    }

    private void initGame() {
        try {
            System.out.println("====================== PAR OU IMPAR ======================");
            System.out.print("\nAntes de começar, por favor, diga-nos seu nome de usuário: ");
            usuario = new BufferedReader(new InputStreamReader(System.in)).readLine();

            System.out.println("\n\t============== BEM VINDO(A)" + usuario.toUpperCase() + " ==============");
            System.out.println("\n\t   Algumas informações que você precisa saber");

            System.out.println("\n==============================================================");
            System.out.println("\n\t  A3 ANHEMBI FOI DESENVOLVIDO POR: ");
            System.out.println("\n\t  João Henrique de Souza Mó     RA 125111345893");
            System.out.println("\t    Igor Cesar Sanches Silva      RA 125111368951");
            System.out.println("\t    Murillo Cesar Bispo Ferreira  RA 125111370656");
            System.out.println("\t    Nathasha Lohanne Kirdeikas    RA 125111374719");
            System.out.println("\t    Raphael Ferreira Dias         RA 125111347434");
            System.out.println("\t    Davi Oliveira dos Santos      RA 125111350411");
            System.out.println("\n==============================================================");
            System.out.println("\n\t     APERTE 'ENTER' PARA CONTINUAR ");
            new BufferedReader(new InputStreamReader(System.in)).readLine();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erro ao ler entrada do usuário: " + e.getMessage());
        }
    }