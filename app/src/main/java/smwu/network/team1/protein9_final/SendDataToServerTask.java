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
        if (params.length != 2) {
            // 적절한 파라미터가 전달되지 않았을 경우 예외 처리
            return "ADD_TO_PLAYLIST_FAILURE";
        }

        String musicName = params[0];
        String musicArtist = params[1];

        // 노래 신청 메시지 프로토콜 생성
        String songRequestMsg = "SONG_REQUEST:" + musicName + "," + musicArtist;

        // 서버로 데이터 전송 및 응답 받아오기
        return sendDataToServer(songRequestMsg);
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
