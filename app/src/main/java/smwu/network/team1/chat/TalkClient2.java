package smwu.network.team1.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class TalkClient2 {
    public static void main(String[] args) throws Exception {
        String serverIP = "172.30.1.88";
        int serverPort = 9000;

        Socket sock = new Socket(serverIP, serverPort);

        BufferedReader keyRead = new BufferedReader(new InputStreamReader(System.in));
        OutputStream ostream = sock.getOutputStream();
        PrintWriter pwrite = new PrintWriter(ostream, true);
        InputStream istream = sock.getInputStream();
        BufferedReader receivedRead = new BufferedReader(new InputStreamReader(istream));

        System.out.print("Start the chitchat, type and press Enter key");

        // 새로운 스레드를 만들어 서버에서 받은 메시지를 계속해서 처리
        Thread receiveThread = new Thread(() -> {
            try {
                String receiveMessage;
                while ((receiveMessage = receivedRead.readLine()) != null) {
                    System.out.println("Client>> Received Message from Server: " + receiveMessage);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        receiveThread.start();

        // 메시지를 입력하고 서버로 전송하는 부분은 그대로 유지
        String sendMessage;
        while (true) {
            System.out.println("Client>> Enter Sending Message : ");
            sendMessage = keyRead.readLine();
            pwrite.println(sendMessage);
            System.out.print("-> Over: Client Waiting");
            pwrite.flush();
        }
    }
}

