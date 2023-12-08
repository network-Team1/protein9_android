import java.io.*;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class MyServer {
    public static ArrayList<PrintWriter> m_OutputList;

    public static void main(String[] args){
    	// PrintWriter를 저장할 객체
        m_OutputList = new ArrayList<PrintWriter>();	

        try{
        	// 포트 열기 -> 클라이언트 연결을 기다림
            ServerSocket s_socket = new ServerSocket(8545); 
            // 서버 연결
            System.out.println("서버가 준비되었습니다"); 			
            
            while(true){
            	// 클라이언트 연결 받아드리고 클라이언트가 통신할 소켓 생성
                Socket c_socket = s_socket.accept();		
                
                // 연결된 클라이언트 통신(=c_socket)을 관리할 쓰레드 생성
                ClientManagerThread c_thread = new ClientManagerThread();
                c_thread.setSocket(c_socket);

                // 각 클라이언트의 PrintWriter를 추가
                m_OutputList.add(new PrintWriter(c_socket.getOutputStream()));
                
                // 현재 연결된 클라이언트 수 출력
                System.out.println(m_OutputList.size());
                
                // 연결된 클라이언트 통신 관리 시작
                c_thread.start();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}