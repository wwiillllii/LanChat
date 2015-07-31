package wiliberm.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import wiliberm.ChatServer;

public class ServerControlPane extends JPanel implements ActionListener {
	private static final long serialVersionUID = 7219023411951776616L;

	private JPopupMenu nameMenu = new JPopupMenu();
	private JMenuItem kick = new JMenuItem("kick");
	private JMenuItem ban = new JMenuItem("ban");

	private ChatServer server = null;

	private final int size;
	private JLabel[][] labels;
	
	private JLabel selectedLabel = null;

	public ServerControlPane(int maxPlayers, ChatServer server) {
		this.server = server;

		kick.addActionListener(this);
		ban.addActionListener(this);

		nameMenu.add(kick);
		nameMenu.add(ban);

		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		int size = (int) Math.sqrt(maxPlayers);
		if (size * (size + 1) < maxPlayers)
			size++;
		this.size = size;

		c.weightx = 1.0 / (size + 1);
		c.weighty = 1.0 / (size + 2);
		c.fill = GridBagConstraints.BOTH;

		c.weightx = 1.0 / (size + 2);
		c.gridwidth = 1;
		labels = new JLabel[size][size + 1];
		for (int y = 0; y < size; y++)
			for (int x = 0; x <= size; x++) {
				labels[y][x] = new JLabel();// "test " + x + " " + y
				labels[y][x].setFont(Display.defaultFont);
				labels[y][x].addMouseListener(new PopupListener(labels[y][x]));
				c.gridx = x;
				c.gridy = y + 1;
				add(labels[y][x], c);
			}
	}

	public void add(String name) {
		for (int y = 0; y < size; y++)
			for (int x = 0; x <= size; x++) {
				JLabel label = labels[y][x];
				if (label.getText().equals("")) {
					label.setText(name);
					System.out.println(label.getText());
					return;
				}
			}
	}

	public void remove(String name) {
		for (int y = 0; y < size; y++)
			for (int x = 0; x <= size; x++) {
				JLabel label = labels[y][x];
				if (label.getText().equals(name)) {
					label.setText("");
					return;
				}
			}
	}

	private class PopupListener extends MouseAdapter {
		JLabel label = null;

		public PopupListener(JLabel label) {
			this.label = label;
		}

		public void mousePressed(MouseEvent e) {
			maybeShowPopup(e);
		}

		public void mouseReleased(MouseEvent e) {
			maybeShowPopup(e);
		}

		private void maybeShowPopup(MouseEvent e) {
			if (e.isPopupTrigger()) {
				selectedLabel = (JLabel) e.getSource();
				nameMenu.show(label, e.getX(), e.getY());
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JComponent source = (JComponent) e.getSource();
		if (source == kick)
			server.kick(selectedLabel.getText());
		else if (source == ban)
			server.ban(selectedLabel.getText());
	}
}
