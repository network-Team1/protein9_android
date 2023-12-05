package smwu.network.team1.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class TalkServer2 {
    private static Map<String, PrintWriter> clientWriters = new HashMap<>();

    public static void main(String[] args) throws Exception {
        int serverPort = 9000;

        ServerSocket serverSocket = new ServerSocket(serverPort);
        System.out.println("Talk Server Ready for Chatting");

        int clientCount = 1;

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("New client connected");

            PrintWriter clientWriter = new PrintWriter(clientSocket.getOutputStream(), true);
            String clientId = "Client" + clientCount++;
            clientWriters.put(clientId, clientWriter);

            // 각 클라이언트에 대한 별도의 스레드 생성
            Thread clientThread = new Thread(() -> {
                try {
                    handleClient(clientSocket, clientId);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            clientThread.start();
        }
    }

    private static void handleClient(Socket clientSocket, String clientId) throws IOException {
        BufferedReader keyRead = new BufferedReader(new InputStreamReader(System.in));
        InputStream istream = clientSocket.getInputStream();
        BufferedReader receivedRead = new BufferedReader(new InputStreamReader(istream));

        String receiveMessage;
        System.out.println("Server>> Message Waiting");

        while (true) {
            if ((receiveMessage = receivedRead.readLine()) != null) {
                System.out.println("Server>> Received Message from " + clientId + ": " + receiveMessage);
                // 특정 클라이언트에게 메시지 전송
                sendDirectMessage(clientId, receiveMessage);
            }
        }
    }

    private static void sendDirectMessage(String clientId, String message) {
        for (Map.Entry<String, PrintWriter> entry : clientWriters.entrySet()) {
            String recipientId = entry.getKey();
            PrintWriter writer = entry.getValue();

            // 메시지를 보낸 클라이언트를 제외하고 특정 대화 상대에게 메시지 전송
            if (!recipientId.equals(clientId)) {
                writer.println("Server>> Message from " + clientId + ": " + message);
                writer.flush();
            }
        }
    }
}

