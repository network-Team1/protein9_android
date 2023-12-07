package smwu.network.team1.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import java.io.*;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private static final String SERVER_IP = "127.0.0.1"; // 서버 IP 주소
    private static final int SERVER_PORT = 9999; // 서버 포트 번호

    private ScrollView scrollView;
    private TextView chatTextView;
    private EditText editText;
    private Button sendButton;

    private Socket socket;
    private PrintWriter printWriter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scrollView = findViewById(R.id.scrollView1);
        chatTextView = findViewById(R.id.chatTextView1);
        editText = findViewById(R.id.editText1);
        sendButton = findViewById(R.id.sendButton1);

        connectToServer();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage(editText.getText().toString());
            }
        });

        // 서버로부터 메시지를 수신하여 UI 업데이트
        Thread receiveThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    while (true) {
                        final String receivedMessage = bufferedReader.readLine();
                        if (receivedMessage != null) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateChat(receivedMessage);
                                }
                            });
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        receiveThread.start();
    }

    private void connectToServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket(SERVER_IP, SERVER_PORT);
                    printWriter = new PrintWriter(socket.getOutputStream(), true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void sendMessage(final String message) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                printWriter.println(message);
            }
        }).start();
    }

    private void updateChat(String message) {
        chatTextView.append(message + "\n");
        scrollView.fullScroll(View.FOCUS_DOWN);
        editText.setText("");
    }


}