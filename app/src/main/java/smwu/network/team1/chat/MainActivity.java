package smwu.network.team1.chat;

import android.os.AsyncTask;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.*;
import android.view.View;
import android.widget.*;
import java.io.*;
import java.net.Socket;

//public class MainActivity extends AppCompatActivity {
//
//    private static final String SERVER_IP = "127.0.0.1"; // 서버 IP 주소
//    private static final int SERVER_PORT = 9999; // 서버 포트 번호
//
//    private ScrollView scrollView;
//    private TextView chatTextView;
//    private EditText editText;
//    private Button sendButton;
//
//    private Socket socket;
//    private PrintWriter printWriter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        scrollView = findViewById(R.id.scrollView1);
//        chatTextView = findViewById(R.id.chatTextView1);
//        editText = findViewById(R.id.editText1);
//        sendButton = findViewById(R.id.sendButton1);
//
//        connectToServer();
//
//        sendButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                sendMessage(editText.getText().toString());
//            }
//        });
//
//        // 서버로부터 메시지를 수신하여 UI 업데이트
//        Thread receiveThread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//                    while (true) {
//                        final String receivedMessage = bufferedReader.readLine();
//                        if (receivedMessage != null) {
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    updateChat(receivedMessage);
//                                }
//                            });
//                        }
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        receiveThread.start();
//    }
//
//    private void connectToServer() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    socket = new Socket(SERVER_IP, SERVER_PORT);
//                    printWriter = new PrintWriter(socket.getOutputStream(), true);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }
//
//    private void sendMessage(final String message) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                printWriter.println(message);
//            }
//        }).start();
//    }
//
//    private void updateChat(String message) {
//        chatTextView.append(message + "\n");
//        scrollView.fullScroll(View.FOCUS_DOWN);
//        editText.setText("");
//    }
//
//
//}
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

        // AsyncTask를 사용하여 서버로부터 메시지를 수신하여 UI 업데이트
        new ReceiveMessagesTask().execute();
    }

    private void connectToServer() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    socket = new Socket(SERVER_IP, SERVER_PORT);
//                    printWriter = new PrintWriter(socket.getOutputStream(), true);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    Log.e("Thread-3", "Failed to initialize PrintWriter");
//                    return;
//                }
//                // printWriter이 null이 아닌지 확인하고 사용
//                if (printWriter != null) {
//                    printWriter.println("null이 아니다!!!");
//                }
//            }
//        }).start();
        try {
            socket = new Socket(SERVER_IP, SERVER_PORT);
            printWriter = new PrintWriter(socket.getOutputStream(), true);
            // printWriter이 null이 아닌지 확인하고 사용
            if (printWriter != null) {
                printWriter.println("null이 아니다!!!");
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Thread-3", "Failed to initialize PrintWriter");
        }
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

    // AsyncTask를 사용하여 백그라운드에서 메시지 수신
    private class ReceiveMessagesTask extends AsyncTask<Void, String, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                if (socket == null || !socket.isConnected()) {
                    // socket이 초기화되지 않았거나 연결이 종료된 경우
                    Log.e("ReceiveMessagesTask", "Socket is not initialized or connection is closed.");
                    return null;
                }
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String receivedMessage;
                while ((receivedMessage = bufferedReader.readLine()) != null) {
                    publishProgress(receivedMessage);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
//            updateChat(values[0]);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateChat(values[0]);
                }
            });
        }
    }
}
