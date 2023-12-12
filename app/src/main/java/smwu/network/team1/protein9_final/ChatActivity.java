package smwu.network.team1.protein9_final;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatActivity extends AppCompatActivity {

    private Handler mHandler;
    Socket socket;
    PrintWriter sendWriter;
    BufferedReader receiveReader;
    private String ip = "172.30.1.88";
    private int port = 8888;

    TextView textView;
    String UserID;
    Button chatbutton;
    TextView chatView;
    EditText message;
    String sendmsg;
    String read;

    @Override
    protected void onStop() {
        super.onStop();
        try {
            sendWriter.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mHandler = new Handler();
        textView = (TextView) findViewById(R.id.chat_sendTo_tv);
        chatView = (TextView) findViewById(R.id.chat_textview_tv);
        message = (EditText) findViewById(R.id.chat_message_et);
        chatbutton = (Button) findViewById(R.id.chat_chatting_btn);
//        textView.setText("chatting App"); // pt 선생님

        new Thread() {
            public void run() {
                try {
                    //InetAddress serverAddr = InetAddress.getByName(ip);
                    socket = new Socket(ip, port);
                    sendWriter = new PrintWriter(socket.getOutputStream());
                    receiveReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    UserID = receiveReader.readLine();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // 받아온 ID를 텍스트뷰에 표시
                            textView.setText("채팅방");
                        }
                    });

                    while (true) {
                        read = receiveReader.readLine();

                        System.out.println("TTTTTTTT" + read);
                        if (read != null) {
//                            mHandler.post(new msgUpdate(read));
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    // 메시지를 표시하는 TextView
                                    TextView chatView = findViewById(R.id.chat_textview_tv);

                                    chatView.append("*** [ " + read + "님이 접속하셨습니다 ] ***\n");

                                }
                            });
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        chatbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendmsg = message.getText().toString();
                Toast.makeText(getApplicationContext(), sendmsg, Toast.LENGTH_SHORT);
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            if (sendWriter != null) {
                                sendWriter.println(UserID + " : " + sendmsg);
                                sendWriter.flush();
                                message.setText("");
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(ChatActivity.this, "서버에 연결되어 있지 않습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });
    }

    class msgUpdate implements Runnable{
        private String msg;
        public msgUpdate(String str) {this.msg=str;}

        @Override
        public void run() {
            chatView.setText(chatView.getText().toString()+msg+"\n");
        }
    }
}