package chat3;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiServer {
		
	// 멤버 변수
	static ServerSocket serverSocket = null;
	static Socket socket = null;
	static PrintWriter out = null;
	static BufferedReader in = null;
	static String s = ""; // 클라이언트의 메세지를 저장
	
	// 생성자
	public MultiServer() {
		
	
	}
	
	// 서버의 초기화를 담당 할 메소드
	public static void init() {
		
		// 클라이언트의 이름을 저장
		String name = ""; // 클라이언트의 이름을 저장
	
		try {
			
			// 클라이언트의 접속을 기다린다.
			serverSocket = new ServerSocket(9999);
			System.out.println("서버가 시작되었습니다.");
			
			// 접속 대기 중
			
			/*
			클라이언트가 접속요청을 하면 accept() 을 통해 허가한다.
			*/
			socket = serverSocket.accept();
			/*
			getInetAddress() : 소켓이 연결되어있는 원격 IP 주소를 얻어온다.
			getPort() : 원격 포트번호를 얻어온다.
			즉 , 클라이언트의 IP 와 포트번호를 얻어와서 출력한다.
			*/
			System.out.println(socket.getInetAddress() + ":" + socket.getPort());
			
			// 서버 -> 클라이언트 측으로 메세지를 전송하기 위한 스트림을 생성한다. (메세지를 보낼 준비)
			out = new PrintWriter(socket.getOutputStream(), true);
			// 클라이언트로부터 메세지를 받기 위한 스트림을 생성한다. (메세지를 받을 준비)
			in = new BufferedReader(new 
			InputStreamReader(socket.getInputStream()));
			
			/*
			클라이언트가 서버로 전송하는 최초의 메세지는 "대화명" 이므로 
			메세지를 읽은 후 , 변수에 저장하고 , 클라이언트쪽으로 Echo 해준다.
			*/
			if (in != null) {
				name = in.readLine(); // 클라이언트의 이름을 읽어서 저장
				System.out.println(name + "접속"); // 서버의 콘솔에 출력
				out.println(">" + name + "님이 접속했습니다."); // 클라이언트에게 Echo
			}
			
			/*
			두 번째 메세지부터는 실제 대화내용이므로 읽어와서 콘솔에 출력하고 , 동시에 클라이언트 측으로 Echo 해준다.
			*/
			while (in != null) {
				s = in.readLine();
				if(s==null) {
					break;
				}
				// 읽어 온 메세지를 서버의 콘솔에 출력한다.
				System.out.println(name + " ==>" + s);
				// 클라이언트에게 Echo 해준다.
				sendAllMsg(name, s);
				}
			System.out.println("Bye !");
		}
		
		catch (Exception e) {
			System.out.println("예외1 : " + e);
			//e.printStackTrace();
		}
		finally {
			try {
				// 입출력 스트림 종료 (자원해제)
				in.close();
				out.close();
				// 소켓 종료 (자원해제)
				socket.close();
				serverSocket.close();
			}
			catch (Exception e)
			{
				System.out.println("예외2 : " + e);
				//e.printStackTrace();
			}
		}		
	}
	// 서버가 클라이언트에게 메세지를 Echo 해주는 메소드
	public static void sendAllMsg(String name , String msg) {
		try {
			out.println("> " + name + "==>" + msg);
		}
		catch (Exception e) {
			System.out.println("예외 : " + e);
			}
		}
		// main() 은 프로그램의 출발점 역할만 담당한다.
		public static void main(String[] args) {
			init();
		}
	}