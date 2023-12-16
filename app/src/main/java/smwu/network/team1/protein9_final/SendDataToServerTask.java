package smwu.network.team1.protein9_final;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SendDataToServerTask extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... params) {
        if (params.length == 0) {
            return null;  // 매개변수가 제공되지 않음
        }

        String messageType = params[0];

        if ("SONG_REQUEST".equals(messageType) && params.length == 3) {
            // 서버에 노래 요청
            return sendDataToServer("SONG_REQUEST:" + params[1] + "," + params[2]);
        } else if ("REQUEST_PLAYLIST".equals(messageType)) {
            // 서버에 재생 목록 요청
            return sendDataToServer("REQUEST_PLAYLIST");
        }

        return null;  // 잘못된 매개변수
    }

    private String sendDataToServer(String message) {
        try {
            // 서버에 연결
            Socket socket = new Socket("172.30.1.88", 8888);

            // 서버로 데이터 전송
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println(message);
            writer.flush();

            // 서버로부터 응답 받아오기
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = reader.readLine();

            // 소켓 및 출력 스트림 닫기
            writer.close();
            reader.close();
            socket.close();

            return response;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
