package wiliberm;

import java.net.InetSocketAddress;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class ChatConnection extends Connection {

	private InetSocketAddress lastConnection = null;
	private String name = "";
	private int score = 0;

	public ChatConnection() {
		// set lastConnection on connected event
		addListener(new Listener() {
			@Override
			public void connected(Connection connection) {
				lastConnection = getRemoteAddressTCP();
			}
		});
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public InetSocketAddress getLastConnection() {
		return lastConnection;
	}

	public void addScore(int add) {
		score += add;
	}

	public int getScore() {
		return score;
	}

}
