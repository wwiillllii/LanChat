package wiliberm;

import java.net.InetSocketAddress;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import static wiliberm.PacketIds.*;

public class ServerListener extends Listener {

	private final ChatServer server;

	public ServerListener(ChatServer server) {
		this.server = server;
	}

	@Override
	public void connected(Connection connection) {
		super.connected(connection);
		if (server.isBanned(connection.getRemoteAddressTCP().getHostString())) {
			connection.sendTCP(new Packet(DISCONNECT,
					"you are banned from this server!"));
			connection.close();
			return;
		}
		System.out.println("client connected: "
				+ connection.getRemoteAddressTCP().getHostString());
		System.out.println("client id: " + connection.getID());
		connection.sendTCP(new Packet(MESSAGE,
				"Welcome to the local chat server!"));
	}

	@Override
	public void disconnected(Connection c) {
		ChatConnection connection = (ChatConnection) c;
		String name = connection.getName();
		if (name.equals("")) {
			try {
				String ip = connection.getRemoteAddressTCP().getHostString();
				System.out.println(ip + " disconnected!");
				server.sendToAllTCP(new Packet(MESSAGE, ip + " disconnected!"));
			} catch (NullPointerException e) {
			}
			return;
		}
		server.remove(name);
		System.out.println(name + " disconnected!");
		server.sendToAllTCP(new Packet(NAME_DISCONNECTED, name));
	}

	@Override
	public void idle(Connection connection) {
		super.idle(connection);
	}

	@Override
	public void received(Connection c, Object object) {
		ChatConnection connection = (ChatConnection) c;

		InetSocketAddress address = connection.getRemoteAddressTCP();
		String host = address.getHostString();
		String name = connection.getName();

		if (!(object instanceof Packet))
			return;
		Packet packet = (Packet) object;
		String text = packet.getText();
		PacketIds type = packet.getId();
		if (connection.getName().equals("") && type != NAME) {
			connection.sendTCP(new Packet(DISCONNECT,
					"you must set your name first!"));
			connection.close();
			return;
		}
		switch (type) {
		case MESSAGE:
			if (connection.getName().equals("")) {
				connection.sendTCP(new Packet(DISCONNECT,
						"You don't have a name!"));
				connection.close();
				return;
			}
			if (text.equals(""))
				return;
			System.out.println(name + ": " + text);
			server.sendToAllTCP(new Packet(MESSAGE, name + ": " + text));
			break;
		case NAME:
			text = text.trim();
			if (!name.equals("")) {
				System.err.println(name + " from " + host
						+ " tried to set name more then once!");
				connection.sendTCP(new Packet(MESSAGE,
						"You can't set your name more than once!"));
				connection.close();
				return;
			}
			String names = "";
			for (ChatConnection other : server.getConnections()) {
				if (other.getName().equals(text)) {
					String otherAddress = other.getRemoteAddressTCP()
							.getHostString();
					System.err.println(host + " and " + otherAddress
							+ " tried to use the same name: " + text + "!");
					connection.sendTCP(new Packet(DISCONNECT,
							"This name is already in use by " + otherAddress
									+ " !"));
					connection.close();
					return;
				} else {
					names += other.getName() + "\n";
				}
			}
			names = names.trim();
			if (name.contains("\n")) {
				System.err.println(name + " from " + host
						+ " tried to set name with a \\n!");
				connection.sendTCP(new Packet(MESSAGE,
						"You can't have a name with a \\n!"));
				connection.close();
				return;
			}
			connection.setName(text);
			server.add(text);
			server.sendToAllTCP(new Packet(NAME_CONNECTED, text));
			connection.sendTCP(new Packet(NAME_LIST, names));
			System.out.println(host + " has connected with the name " + text);
		default:
			break;
		}
	}
}
