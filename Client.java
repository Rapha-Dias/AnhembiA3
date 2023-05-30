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

    private void run() {
        try {
            Socket socket = new Socket(HOST, PORT);
            System.out.println("Conectado ao servidor");
            GameGUI game = new GameGUI(socket);
            game.setVisible(true);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erro ao conectar ao servidor: " + e.getMessage());
        }
    }

    static class GameGUI extends JFrame {
        private BufferedReader in;
        private PrintWriter out;
        private JTextField inputField;
        private JButton playButton;
        private JLabel statusLabel;
        private JLabel scoreLabel;
        private JComboBox<String> paritySelector;

        public GameGUI(Socket socket) {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Erro ao configurar fluxos de entrada/saída: " + e.getMessage());
            }

            setTitle("Par ou Ímpar");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(500, 400);  // Tamanho ajustado
            setLayout(new BorderLayout(10, 10));

            JPanel topPanel = new JPanel();
            topPanel.setLayout(new FlowLayout());
            topPanel.add(new JLabel("Selecione Par ou Ímpar:"));
            paritySelector = new JComboBox<>(new String[] { "Par", "Ímpar" });
            topPanel.add(paritySelector);
            topPanel.add(new JLabel("Digite um número:"));
            inputField = new JTextField(10);
            topPanel.add(inputField);
            add(topPanel, BorderLayout.NORTH);

            JPanel buttonPanel = new JPanel(new FlowLayout());  // Painel para os botões
            playButton = new JButton("Jogar");
            buttonPanel.add(playButton);
            JButton exitButton = new JButton("Sair");
            buttonPanel.add(exitButton);
            add(buttonPanel, BorderLayout.CENTER);

            JPanel centerPanel = new JPanel(new BorderLayout());
            statusLabel = new JLabel("", SwingConstants.CENTER);
            statusLabel.setFont(new Font("Arial", Font.BOLD, 16));
            centerPanel.add(statusLabel, BorderLayout.CENTER);

            JPanel scorePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            scoreLabel = new JLabel("Placar: 0 - 0");
            scoreLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            scorePanel.add(scoreLabel);
            centerPanel.add(scorePanel, BorderLayout.SOUTH);

            add(centerPanel, BorderLayout.SOUTH);

            playButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String input = inputField.getText();
                    if (!input.matches("\\d+")) {
                        JOptionPane.showMessageDialog(null, "Por favor, digite um número válido.");
                        return;
                    }
                    String parity = (String) paritySelector.getSelectedItem();
                    out.println(parity + "," + input);
                    try {
                        String response = in.readLine();
                        String[] parts = response.split(",");
                        statusLabel.setText(parts[0] + " " + parts[1]);
                        scoreLabel.setText(parts[2]);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Erro ao ler do servidor: " + ex.getMessage());
                    }
                }
            });

            exitButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        socket.close();
                        System.exit(0);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Erro ao fechar o socket: " + ex.getMessage());
                    }
                }
            });

            getRootPane().setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        }
    }
}