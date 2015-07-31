package wiliberm;

import java.io.IOException;

import wiliberm.gui.Display;
import static wiliberm.PacketIds.*;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class ClientListener extends Listener {

	private final Display display;
	private final ChatClient client;

	public ClientListener(Display display, ChatClient client) {
		this.display = display;
		this.client = client;
	}

	@Override
	public void connected(Connection connection) {
		super.connected(connection);
		System.out.println("connected!");
	}

	@Override
	public void received(Connection connection, Object object) {
		super.received(connection, object);
		if (!(object instanceof Packet))
			return;
		Packet packet = (Packet) object;
		String text = packet.getText();
		PacketIds type = packet.getId();
		switch (type) {
		case DISCONNECT:
			display.append(text);
			display.setEnabled(false);
			connection.close();
		case MESSAGE:
			display.append(text);
			break;
		case NAME_CONNECTED:
			display.connected(text, true);
			break;
		case NAME_DISCONNECTED:
			display.disconnected(text);
			break;
		case NAME_LIST:
			System.out.println("got a name list:");
			System.out.println(text.replace("\n", ","));
			for (String name : text.split("\n"))
				display.connected(name, false);
			break;
		default:
			System.err.println("unknown packet id: " + type);
			System.exit(-1);
		}
	}
}
