package wiliberm;

import java.io.IOException;
import java.util.Scanner;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

public class Test {

	public static void Main(String[] args) {
		System.out.println("run server?");
		try {
			if (new Scanner(System.in).nextBoolean()) {
				Server server = new Server();
				server.bind(6789);
				// server.getKryo().register(String.class);
				server.addListener(new Listener() {
					@Override
					public void connected(Connection connection) {
						super.connected(connection);
						System.out.println("client connected!");
						System.out.println(connection.sendTCP("hello!"));
					}

					public void received(Connection connection, Object object) {
						super.received(connection, object);
						object = (String) object;
						if (object instanceof String) {
							System.out.println(object);
						}
					}
				});
				server.start();
			} else {
				Client client = new Client();
				// client.getKryo().register(String.class);
				client.addListener(new Listener() {
					@Override
					public void received(Connection connection, Object object) {
						super.received(connection, object);
						object = (String) object;
						if (object instanceof String) {
							System.out.println(object);
							connection.sendTCP("echo: " + object);
						} else {
							System.out.println("shit");
							System.out.println(object.getClass().getName());
						}
					}
				});
				client.start();
				client.connect(5000, "localhost", 6789);
				while (client.isConnected())
					;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}