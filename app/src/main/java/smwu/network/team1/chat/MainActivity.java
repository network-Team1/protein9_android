package smwu.network.team1.chat;

//import android.os.AsyncTask;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.os.Bundle;
//import android.util.*;
//import android.view.View;
//import android.widget.*;
//import java.io.*;
//import java.net.Socket;
//
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
////        connectToServer();
//        new ConnectToServerTask().execute();
//
//        sendButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                sendMessage(editText.getText().toString());
//            }
//        });
//
//        // AsyncTask를 사용하여 서버로부터 메시지를 수신하여 UI 업데이트
//        new ReceiveMessagesTask().execute();
//    }
//
//    private void connectToServer() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    // 소켓 초기화
//                    socket = new Socket(SERVER_IP, SERVER_PORT);
//
//                    // PrintWriter 초기화
//                    printWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
//
//                    // 연결 메시지 전송
//                    printWriter.println("Hello Server!");
//
//                    // 아래 코드는 굳이 필요하지 않을 수 있습니다.
//                    // printWriter.flush();
//
//                    // 연결이 성공적으로 이루어지면, UI 업데이트 등을 수행할 수 있습니다.
//                    // 예를 들어, 연결 성공 메시지를 UI에 표시하는 등의 작업을 수행할 수 있습니다.
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    Log.e("Thread-3", "Failed to initialize PrintWriter");
//                    // 연결이 실패했을 때 예외 처리 코드 추가
//                }
//            }
//        }).start();
//    }
//
//    private class ConnectToServerTask extends AsyncTask<Void, Void, Boolean> {
//        @Override
//        protected Boolean doInBackground(Void... voids) {
//            try {
//                socket = new Socket(SERVER_IP, SERVER_PORT);
//                printWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
//                // 연결 메시지 전송
//                printWriter.println("Hello Server!");
//                return true;
//            } catch (IOException e) {
//                e.printStackTrace();
//                Log.e("ConnectToServerTask", "Failed to initialize PrintWriter");
//                // 연결이 실패했을 때 예외 처리 코드 추가
//                return false;
//            }
//        }
//
//        @Override
//        protected void onPostExecute(Boolean connected) {
//            if (connected) {
//                // 연결이 성공적으로 이루어지면, UI 업데이트 등을 수행할 수 있습니다.
//                // 예를 들어, 연결 성공 메시지를 UI에 표시하는 등의 작업을 수행할 수 있습니다.
//            } else {
//                // 연결 실패에 대한 처리
//            }
//        }
//    }
//
//
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
//    // AsyncTask를 사용하여 백그라운드에서 메시지 수신
//    private class ReceiveMessagesTask extends AsyncTask<Void, String, Void> {
//        @Override
//        protected Void doInBackground(Void... voids) {
//            try {
//                if (socket == null || !socket.isConnected()) {
//                    Log.e("ReceiveMessagesTask", "Socket is not initialized or connection is closed.");
//                    return null;
//                }
//                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//                String receivedMessage;
//                while ((receivedMessage = bufferedReader.readLine()) != null) {
//                    publishProgress(receivedMessage);
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onProgressUpdate(String... values) {
//            super.onProgressUpdate(values);
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    updateChat(values[0]);
//                }
//            });
//        }
//    }
//
//}

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private TextView chatTextView;
    private EditText messageEditText;
    private PrintWriter clientWriter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chatTextView = findViewById(R.id.chatTextView);
        messageEditText = findViewById(R.id.messageEditText);

        // 서버에 연결
        ConnectToServerTask connectTask = new ConnectToServerTask();
        connectTask.execute();
    }

    public void sendMessage(View view) {
        // 메시지를 입력하고 서버로 전송
        String message = messageEditText.getText().toString();
        SendMessageTask sendMessageTask = new SendMessageTask();
        sendMessageTask.execute(message);

        // 화면에 메시지 표시
        chatTextView.append("Client>> " + message + "\n");
        messageEditText.setText("");
    }

    private class ConnectToServerTask extends AsyncTask<Void, String, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                String serverIP = "172.30.1.88";
                int serverPort = 9000;

                Socket socket = new Socket(serverIP, serverPort);
                clientWriter = new PrintWriter(socket.getOutputStream(), true);

                // 서버에서 받은 메시지 처리
                BufferedReader receivedRead = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                while (true) {
                    String receivedMessage = receivedRead.readLine();
                    if (receivedMessage != null) {
                        publishProgress(receivedMessage);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            // 받은 메시지를 화면에 표시
            chatTextView.append("서버>> " + values[0] + "\n");
        }
    }

    private class SendMessageTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... messages) {
            // 서버로 메시지 전송
            clientWriter.println(messages[0]);
            return null;
        }
    }
}
