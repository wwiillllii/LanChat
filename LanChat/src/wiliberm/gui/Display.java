package wiliberm.gui;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;
import static javax.swing.SpringLayout.EAST;
import static javax.swing.SpringLayout.NORTH;
import static javax.swing.SpringLayout.SOUTH;
import static javax.swing.SpringLayout.WEST;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SpringLayout;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import wiliberm.ChatClient;
import wiliberm.Packet;
import static wiliberm.PacketIds.*;

public class Display extends JPanel implements KeyListener, ActionListener {
	private static final long serialVersionUID = 590659656551357195L;

	private JTextArea log = null;
	private JScrollPane logPane = null;
	private JTextArea input = null;
	private JScrollPane inputPane = null;
	private JButton send = null;

	private JList<String> connections = null;
	private DefaultListModel<String> listModel = null;
	private JScrollPane listPane = null;

	private JFrame frame = null;

	private ChatClient client = null;

	public Display(ChatClient client) {
		this.client = client;

		frame = new JFrame("LanChat (version: 1.1)");
		frame.setContentPane(this);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setMinimumSize(new Dimension(320, 240));
		frame.setLocationRelativeTo(null);
		init();
		// set L&F to Nimbus
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
		}
		frame.setVisible(true);
		input.requestFocusInWindow();
		String name = JOptionPane.showInputDialog(frame, "what is your name? ",
				"name", JOptionPane.QUESTION_MESSAGE);
		client.sendTCP(new Packet(NAME, name));
	}

	private void init() {
		SpringLayout l = new SpringLayout();
		setLayout(l);

		listModel = new DefaultListModel<String>();
		connections = new JList<String>(listModel);
		connections.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		connections.setSelectedIndex(-1);
		connections.setEnabled(false);
		listPane = new JScrollPane(connections);

		log = new JTextArea();
		log.setEditable(false);
		log.setLineWrap(true);
		log.setWrapStyleWord(true);
		logPane = new JScrollPane(log);
		logPane.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
		logPane.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS);
		logPane.getVerticalScrollBar().addAdjustmentListener(
				new AdjustmentListener() {

					@Override
					public void adjustmentValueChanged(AdjustmentEvent e) {
						if (!e.getValueIsAdjusting())
							e.getAdjustable().setValue(
									e.getAdjustable().getMaximum());
					}
				});

		input = new JTextArea();
		input.addKeyListener(this);
		inputPane = new JScrollPane(input);
		inputPane.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
		send = new JButton("send");

		l.putConstraint(EAST, send, -5, EAST, this);
		l.putConstraint(WEST, send, -5 - 128, EAST, this);
		l.putConstraint(SOUTH, send, -5, SOUTH, this);
		l.putConstraint(NORTH, send, -5 - 64, SOUTH, this);
		add(send);

		l.putConstraint(EAST, inputPane, -5, WEST, send);
		l.putConstraint(NORTH, inputPane, 0, NORTH, send);
		l.putConstraint(SOUTH, inputPane, -5, SOUTH, this);
		l.putConstraint(WEST, inputPane, 5, WEST, this);
		add(inputPane);

		l.putConstraint(NORTH, listPane, 5, NORTH, this);
		l.putConstraint(SOUTH, listPane, -5, NORTH, send);
		l.putConstraint(EAST, listPane, -5, EAST, this);
		l.putConstraint(WEST, listPane, 0, WEST, send);
		add(listPane);

		l.putConstraint(NORTH, logPane, 5, NORTH, this);
		l.putConstraint(SOUTH, logPane, -5, NORTH, inputPane);
		l.putConstraint(EAST, logPane, -5, WEST, listPane);
		l.putConstraint(WEST, logPane, 5, WEST, this);
		add(logPane);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			if (e.isShiftDown()) {
				input.setText(input.getText() + "\n");
				return;
			}
			send();
			e.consume();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		send();
	}

	private void send() {
		String text = input.getText().trim();
		if (text == null || text.equals(""))
			return;
		client.sendTCP(new Packet(MESSAGE, text));
		input.setText("");
	}

	public void append(String text) {
		if (!text.endsWith("\n"))
			text += "\n";
		log.append(text);
	}

	public void connected(String name, boolean announce) {
		if (listModel.contains(name))
			return;
		listModel.addElement(name);
		if (announce)
			append(name + " has connected!");
	}

	public void disconnected(String name) {
		if (!listModel.contains(name))
			return;
		while (listModel.removeElement(name))
			;
		append(name + " has disconnected!");
	}

	@Override
	public void setEnabled(boolean enabled) {
		input.setEnabled(enabled);
		send.setEnabled(false);
	}
}
