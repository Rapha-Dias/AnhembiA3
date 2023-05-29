import java.io.*;
import java.net.*;
import java.util.Random;

public class Server {
    private static final int PORT = 8080;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is listening on port " + PORT);
            while (true) {
                new ClientHandler(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            System.err.println("Error in server: " + e.getMessage());
            System.exit(-1);
        }
    }

    static class ClientHandler extends Thread {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;

        ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                System.out.println("New client connected");
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
            } catch (IOException e) {
                System.err.println("Error in client handler: " + e.getMessage());
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    System.err.println("Error while closing socket: " + e.getMessage());
                }
            }
        }
    }
}
