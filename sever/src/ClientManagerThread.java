import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientManagerThread extends Thread{

	// 통신에 사용할 소켓
    private Socket m_socket;
    // 클라이언트 식별 번호
    private String m_ID;

    @Override
    public void run(){
        super.run();
        try{
        	// 클라이언트가 입력하는 값(=메시지)을 읽어와서,
            BufferedReader in = new BufferedReader(new InputStreamReader(m_socket.getInputStream()));
            // 그 메시지를 저장할 변수
            String text;
            // 무한루프라 함은 클라이언트의 아래의 (메세지를 읽고 다른 클라이언트로 전달하는) 행위를 반복
            while(true){
            	// 클라이언트가 입력하는 메시지 내용을 저장해서,
                text = in.readLine(); 
                if(text!=null) {
                	// 연결된 모든 클라이언트에게 메세지 전달함
                    for(int i=0;i<MyServer.m_OutputList.size();++i){
                    	// i번째 인덱스 PrintWriter 객체에 text를 전달
                        MyServer.m_OutputList.get(i).println(text);
                        // 버퍼 비우기
                        MyServer.m_OutputList.get(i).flush();
                    }
                }
            }


        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public void setSocket(Socket _socket){
        m_socket = _socket;
    }
}