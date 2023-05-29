// Server.java
import java.io.*;
import java.net.*;
import java.util.Random;

public class Server {
    private static final int PORT = 8080;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is listening on port " + PORT);
            while (true) {
                try (Socket socket = serverSocket.accept()) {
                    System.out.println("New client connected");
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    Random rand = new Random();
                    int scorePlayer = 0;
                    int scoreComputer = 0;

                    while (true) {
                        String input = in.readLine();
                        if (input == null || input.isEmpty()) {
                            break;
                        }
                        String[] parts = input.split(",");
                        String parity = parts[0];
                        int playerNumber = Integer.parseInt(parts[1]);
                        int computerNumber = rand.nextInt(6); 
                        int total = playerNumber + computerNumber;
                        boolean isTotalEven = total % 2 == 0;
                        boolean playerWins = (parity.equals("Par") && isTotalEven) || (parity.equals("Ímpar") && !isTotalEven);

                        if (playerWins) {
                            scorePlayer++;
                        } else {
                            scoreComputer++;
                        }

                        String result = playerWins ? "Você ganhou!" : "Você perdeu!";
                        out.println(result + ",Placar: " + scorePlayer + " - " + scoreComputer);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error in server: " + e.getMessage());
        }
    }
}
