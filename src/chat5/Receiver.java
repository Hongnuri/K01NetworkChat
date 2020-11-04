package chat5;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;

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
			System.out.println("예외 > Receiver > 생성자 :" + e);
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
			catch (SocketException e) {
				/*
				클라이언트가 q 를 입력하여 접속을 종료하면 무한루프가 발생되므로
				탈출 할 수 있도록 별도의 catch 블럭을 추가하고 , break 를 걸어준다.
				*/
				System.out.println("SocketException 발생. 루프 탈출");
				break;
			}
			catch (Exception e) {
				
				System.out.println("예외 > Receiver > run1 " + e);
			}
		}
		try {
			in.close();
		}
		catch (Exception e) {
			System.out.println("예외 > Receiver > run2 " + e);
		}
	}
}