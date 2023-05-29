import javax.swing.*;
import java.awt.*;
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
        System.out.println("====================== PAR OU IMPAR ======================");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("\nAntes de começar, por favor, diga-nos seu nome de usuário: ");
        try {
            usuario = reader.readLine();
        } catch (IOException e) {
            System.err.println("Erro ao ler entrada do usuário: " + e.getMessage());
            System.exit(-1);
        }

        System.out.printf("\n\t============== BEM VINDO(A) %s ==============\n", usuario.toUpperCase());
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
        try {
            reader.readLine();
        } catch (IOException e) {
            System.err.println("Erro ao ler entrada do usuário: " + e.getMessage());
            System.exit(-1);
        }
    }

    private void run() {
        try (Socket socket = new Socket(HOST, PORT)) {
            System.out.println("Conectado ao servidor");
            GameGUI game = new GameGUI(socket);
            game.setVisible(true);
        } catch (IOException e) {
            System.err.println("Erro ao conectar ao servidor: " + e.getMessage());
            System.exit(-1);
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
                System.err.println("Erro ao configurar fluxos de entrada/saída: " + e.getMessage());
                System.exit(-1);
            }

            setTitle("Par ou Ímpar");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(350, 200);
            setLayout(new BorderLayout(10, 10));

            JPanel topPanel = new JPanel();
            topPanel.setLayout(new FlowLayout());
            topPanel.add(new JLabel("Selecione Par ou Ímpar:"));
            paritySelector = new JComboBox<>(new String[] { "Par", "Ímpar" });
            topPanel.add(paritySelector);
            topPanel.add(new JLabel("Digite um número:"));
            inputField = new JTextField(10);
            topPanel.add(inputField);
            playButton = new JButton("Jogar");
            topPanel.add(playButton);
            add(topPanel, BorderLayout.NORTH);

            JPanel centerPanel = new JPanel(new BorderLayout());
            statusLabel = new JLabel("", SwingConstants.CENTER);
            centerPanel.add(statusLabel, BorderLayout.CENTER);

            JPanel scorePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            scoreLabel = new JLabel("Placar: 0 - 0");
            scorePanel.add(scoreLabel);
            centerPanel.add(scorePanel, BorderLayout.SOUTH);

            add(centerPanel, BorderLayout.CENTER);

            playButton.addActionListener(e -> {
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
                    statusLabel.setText(parts[0]);
                    scoreLabel.setText(parts[1]);
                } catch (IOException ex) {
                    System.err.println("Erro ao ler do servidor: " + ex.getMessage());
                    System.exit(-1);
                }
            });

            getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        }
    }
}
