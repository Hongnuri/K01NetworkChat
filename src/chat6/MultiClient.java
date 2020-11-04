package chat6;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class MultiClient {

	public static void main(String[] args) {
		
		// 클라이언트의 접속자명을 입력한다.
		System.out.print("이름을 입력하세요 : ");
		Scanner scanner = new Scanner(System.in);
		String s_name = scanner.nextLine();
		
		PrintWriter out = null;
		// 서버의 메세지를 읽어오는 기능을 Receiver 클래스로 옮긴다.
		// BufferedReader in = null;
		
		try {
			/*
			접속 할 IP 주소 , c:\bin>java chat3.MultiClient
			위와 같이 접속하면 로컬이 아닌 내가 원하는 서버로 접속할 수 있다. 
			*/
			String ServerIP = "localhost";
			// 실행시 매개변수로 IP주소를 전달한다면 해당주소로 설정한다.
			if(args.length > 0) {
				ServerIP = args[0];
			}
			// IP 주소와 포트를 기반으로 Socket 객체를 생성하여 서버에 접속요청을 한다.
			Socket socket = new Socket(ServerIP , 9999);
			System.out.println("서버와 연결 되었습니다.");
			
			// 서버에서 보내는 메세지를 읽어 올 Receiver 쓰레드 객체 생성 및 시작
			Thread receiver = new Receiver(socket);
			// setDaemon(true) 선언이 없으므로 독립 쓰레드로 생성된다.
			receiver.start();
			/*
			InputStreamReader / getOutputStreamReader 는
			바이트스트림과 문자스트림의 상호변환을 제공하는 입출력스트림이다.
			바이트를 읽어서 지정 된 문자인코딩에 따라 문자로 변환하는데 사용된다.
			서버는 받는 것 뿐만아니라 주기도 한다.
			*/
			Thread sender = new Sender(socket , s_name);
			sender.start();
			
		}
		
		catch (Exception e) {
			System.out.println("예외발생[MultiClient]" + e);
			
		}
	}
}