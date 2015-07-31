package wiliberm.gui;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JFrame;

import wiliberm.ChatServer;

public class Display extends JFrame {
	private static final long serialVersionUID = 590659656551357195L;

	private ServerControlPane serverPane = null;

	private ChatServer server;
	public static final Font defaultFont = new Font(null, Font.PLAIN, 32);

	public Display(ChatServer server) {
		super("online chat server");
		this.server = server;
		serverPane = new ServerControlPane(20, this.server);
		setMinimumSize(new Dimension(640, 480));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setContentPane(serverPane);
		setVisible(true);
	}

	public void add(String name) {
		serverPane.add(name);
	}

	public void remove(String name) {
		serverPane.remove(name);
	}

	public static void finishQuiz() {

	}
}
