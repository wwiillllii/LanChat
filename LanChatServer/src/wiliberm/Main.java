package wiliberm;

public class Main {

	public static void main(String[] args) {
		System.out.println("server!");
		new ChatServer();
		/**
		 * Server server = new Server(); try { server.addListener(new
		 * ServerListener(server)); server.bind(6789, 25565); server.start();
		 * while (true) ; } catch (IOException e) { e.printStackTrace(); }
		 * finally { server.close(); }
		 */
	}

}
