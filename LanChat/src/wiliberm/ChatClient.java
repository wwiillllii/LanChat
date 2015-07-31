package wiliberm;

import java.io.IOException;
import java.net.InetAddress;

import wiliberm.gui.Display;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;

public class ChatClient extends Client {

	private Display display = null;

	public ChatClient() {
		super();
		Kryo kryo = getKryo();
		kryo.register(PacketIds.class);
		kryo.register(Packet.class);
		InetAddress address = discoverHost(25565, 5000);
		if (address == null) {
			System.out.println("could not connect to server!");
			System.exit(-1);
		}
		System.out.println(address.toString());
		try {
			start();
			connect(5000, address, 6789, 25565);
			display = new Display(this);
			addListener(new ClientListener(display, this));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
