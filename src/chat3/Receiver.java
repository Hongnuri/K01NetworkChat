package chat3;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

// 서버가 보내는 Echo 메세지를 읽어오는 쓰레드 클래스
public class Receiver extends Thread {
	
	Socket socket;
	BufferedReader in = null;
	
	// Client 가 접속시 생성한 Socket 객체를 생성자에서 매개변수로 받음.
	public Receiver (Socket socket) {
		this.socket = socket;
		
		/*
		Socket 객체를 기반으로 input 스트림을 생성한다.
		*/
		try {
			in = new BufferedReader (new
			InputStreamReader(this.socket.getInputStream()));
		}
		catch (Exception e) {
			System.out.println("예외 1 : " + e);
		}
	}
	/*
	Thread 에서 main() 의 역할을 하는 메소드로 직접 호출하면 안되고 , 반드시 start() 를 통해
	간접적으로 호출해야 쓰레드가 생성 된다.
	*/
	@Override
	public void run() {
		while(in != null) {
			try {
				System.out.println("Thread Receive : " + in.readLine());
				// 쓰레드가 하는 역할은 in.readLine 을 통해 서버가 보낸 내용을 읽어온다.
			}
			catch (Exception e) {
				/*
				클라이언트가 접속을 종료 할 경우 SocketException 이 발생 되면서 
				무한루프에 빠지게 된다.
				*/
				System.out.println("예외 2 : " + e);
			}
		}
		try {
			in.close();
		}
		catch (Exception e) {
			System.out.println("예외 3 : " + e);
		}
	}
}