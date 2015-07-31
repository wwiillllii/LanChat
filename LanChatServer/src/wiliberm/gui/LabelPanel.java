package wiliberm.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class LabelPanel extends JPanel {
	private static final long serialVersionUID = -1423748032002782183L;

	private JLabel label = new JLabel("test");
	// This field doesn't seem to have a use:
	// private String text = "test";
	private boolean hasText = true;

	public LabelPanel() {
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		label.setFont(Display.defaultFont);
		c.fill = GridBagConstraints.BOTH;
		add(label, c);
	}

	public LabelPanel(String text) {
		this();
		setText(text);
	}

	public void setText(String text) {
		label.setText(text);
	}

	public void reColor() {
		if (hasText) {
			hasText = false;
			label.setForeground(getBackground());
		} else {
			hasText = true;
			label.setForeground(Color.BLACK);
		}
	}
}
