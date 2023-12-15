package smwu.network.team1.protein9_final;

import android.app.Application;
import android.content.Intent;

import java.io.*;
import java.net.*;

public class MyApplication extends Application {

    private Socket socket;
    private PrintWriter sendWriter;
    private String ip = "172.30.1.13";
    private int port = 8888;

    @Override
    public void onCreate() {
        super.onCreate();
        connectToServer();
//        startServerConnection();
    }

    protected void connectToServer() {
        new Thread() {
            public void run() {
                try {
                    socket = new Socket(ip, port);
                    sendWriter = new PrintWriter(socket.getOutputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public Socket getSocket() {
        return socket;
    }

    public PrintWriter getPrintWriter() {
        return sendWriter;
    }

    private void startServerConnection() {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
