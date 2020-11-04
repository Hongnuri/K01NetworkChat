package chat6;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MultiServer {
		
	
	static ServerSocket serverSocket = null;
	static Socket socket = null;
	
	// 클라이언트 정보 저장을 위한 Map 컬렉션 생성
	Map<String, PrintWriter> clientMap;
	
	
	public MultiServer() {
		// 클라이언트의 이름과 출력스트림을 저장 할 HashMap 컬렉션 생성.
		clientMap = new HashMap<String, PrintWriter>();
		// HashMap 동기화 설정. 쓰레드가 사용자정보에 동시에 접근하는 것을 차단.
		Collections.synchronizedMap(clientMap);
	
	}
	
	public void init() {
		
		try {
			/*
			9999번 포트를 설정하여 서버객체를 생성하고 클라이언트의 접속을 대기한다.
			*/
			serverSocket = new ServerSocket(9999);
			System.out.println("서버가 시작되었습니다.");
			
			while(true) {
				socket = serverSocket.accept();
				
				System.out.println(socket.getInetAddress()+"(클라이언트)의"
						+ socket.getPort()+"포트를 통해"
						+ socket.getLocalAddress() + "(서버)의"
						+ socket.getLocalPort()+"포트로 연결 됨");
				
				/*
				클아이언트의 메세지를 모든 클라이언트에게 전달하기 위한 쓰레드 생성 및 시작이다. 
				한 명 당 하나씩의 쓰레드가 생성된다.
				*/
				Thread mst = new MultiServerT(socket);
				mst.start();
			}
		}	
		
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				serverSocket.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}		
	}
	
	public static void main(String[] args) {
		MultiServer ms = new MultiServer();
		ms.init();
	}
	
	// 접속 된 모든 클라이언트에게 메시지를 전달하는 메소드
	public void sendAllMsg(String name , String msg) 
	{
	// Map 에 저장 된 객체의 키 값 (접속자명) 을 먼저 얻어온다.
	Iterator<String> it = clientMap.keySet().iterator();
	
	// 저장 된 객체 (클라이언트) 의 갯수만큼 반복한다.
	while(it.hasNext()) {
		try {
			// 각 클라이언트의 PrintWriter 객체를 얻어 온다.
			PrintWriter it_out = (PrintWriter)
			clientMap.get(it.next());
			
			/*
			클라이언트에게 메시지를 전달한다.
			매개변수로 name 이 있는 경우와 없는 경우를 구분해서
			메세지를 전달하게 된다.
			*/
			if(name.equals("")) {
				// 접속 , 퇴장에서 사용되는 부분
				it_out.println(msg);
			} else {
				// 메세지를 보낼 때 , 사용되는 부분
				it_out.println("[" + name + "]:" + msg);
				}
			}
			catch (Exception e) {
				System.out.println("예외 : " + e);
				
			}
		}
	}
	
 	class MultiServerT extends Thread {
 		// 멤버변수
 		Socket socket;
 		PrintWriter out = null;
 		BufferedReader in = null;
		
 		// 생성자 : socket 을 기반으로 입출력 스트림 생성
		public MultiServerT (Socket socket) {
			this.socket = socket;
			
			try {
				out = new PrintWriter(this.socket.getOutputStream(),true);
				in = new BufferedReader(new
			InputStreamReader(this.socket.getInputStream()));
				
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		@Override
		// 클라이언트에게 서버의 메세지를 Echo 해준다.
		public void run() {
			
			String name = "";
			String s = "";
		
			try {
				// 클라이언트의 이름을 읽어와서 저장한다.
				name = in.readLine();
				/*
				방금 접속한 클라이언트를 제외한 나머지에게 사용자의 입장을 알려준다.
				*/
				sendAllMsg("", name + "님이 입장하셨습니다.");
				// 현재 접속자의 정보를 HashMap 에 저장한다. 
				clientMap.put(name,  out);
				
				// HashMap 에 저장 된 객체의 수로 접속자 수를 파악할 수 있다.
				System.out.println(name + "접속");
				System.out.println("현재 접속자 수는" + clientMap.size() + "명 입니다.");
				
				// 입력한 메세지는 모든 클라이언트에게 Echo 된다.
				while (in!= null) {
					s = in.readLine();
					if (s == null)
						break;
					
					System.out.println(name + " >> " + s);
					sendAllMsg(name , s);
				}
			}
			catch (Exception e) {
				System.out.println("예외 : " + e);
			}
			
			finally {
				/*
				클라이언트가 접속을 종료하면 Socket 예외가 발생하게 되어 finally 절로 집입하게 된다.
				이 때 , '대화명' 을 통해 정보를 삭제한다.
				*/
				clientMap.remove(name);
				sendAllMsg("", name + "님이 퇴장하셨습니다.");
				// 퇴장하는 클라이언트의 쓰레드명을 보여준다.
				System.out.println(name + "[" +
				Thread.currentThread().getName() + "] 퇴장 ");
				System.out.println("현재 접속자 수는" + clientMap.size() + "명 입니다.");
				
				try {
					in.close();
					out.close();
					socket.close();
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}