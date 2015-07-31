package wiliberm;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import wiliberm.gui.Display;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;

public class ChatServer extends Server {

	private Display display;
	private List<String> banned = new ArrayList<String>();

	public ChatServer() {
		searchLan();
		Kryo kryo = getKryo();
		kryo.register(PacketIds.class);
		kryo.register(Packet.class);
		display = new Display(this);
		try {
			addListener(new ServerListener(this));
			start();
			bind(6789, 25565);
			while (true)
				;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}

	private void searchLan() {
		Client client = new Client();
		Kryo kryo = client.getKryo();
		kryo.register(PacketIds.class);
		kryo.register(Packet.class);
		InetAddress address = client.discoverHost(25565, 5000);
		if (address != null) {
			System.err.println("there is another server on the lan!");
			System.err.println("address: " + address.getHostAddress());
			client.close();
			close();
			System.exit(0);
		}
	}

	@Override
	protected ChatConnection newConnection() {
		return new ChatConnection();
	}

	public void add(String name) {
		display.add(name);
	}

	public void remove(String name) {
		display.remove(name);
	}

	@Override
	public ChatConnection[] getConnections() {
		Connection[] connections = super.getConnections();
		ChatConnection[] newConnections = new ChatConnection[connections.length];
		for (int i = 0; i < connections.length; i++)
			newConnections[i] = (ChatConnection) connections[i];
		return newConnections;
	}

	public void kick(String name) {
		for (ChatConnection c : getConnections()) {
			if (c.getName().equals(name)) {
				sendToAllTCP(new Packet(PacketIds.MESSAGE, "Admin kicked "
						+ name + "!"));
				c.sendTCP(new Packet(PacketIds.DISCONNECT, "you are kicked!"));
				c.close();
			}
		}
	}

	public void ban(String name) {
		for (ChatConnection c : getConnections()) {
			if (c.getName().equals(name)) {
				sendToAllTCP(new Packet(PacketIds.MESSAGE, "Admin banned "
						+ c.getName()));
				banned.add(c.getRemoteAddressTCP().getHostString());
				c.sendTCP(new Packet(PacketIds.DISCONNECT, "you are banned!"));
				c.close();
			}
		}
	}

	public boolean isBanned(String IP) {
		return banned.contains(IP);
	}
}
