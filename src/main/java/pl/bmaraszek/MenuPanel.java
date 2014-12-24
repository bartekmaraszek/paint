package pl.bmaraszek;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

public class MenuPanel extends JPanel implements I_MenuPanel {

	private final I_Painter p;
	private static final Color bgColor = new Color(98, 145, 199);
	private static final Dimension MIN_SIZE = new Dimension(150, 25);
	private static final Dimension MAX_SIZE = new Dimension(150, 25);
	private ButtonGroup toggle = new ButtonGroup();
	private ActionType toggledButton;

	private static final long serialVersionUID = 1L;

	public MenuPanel(I_Painter p) {
		this.p = p;
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(Box.createRigidArea(new Dimension(0, 10)));

		this.add(makeButton(ButtonType.TOGGLE, ActionType.FREEHAND, "Pencil", null));
		this.add(makeButton(ButtonType.TOGGLE, ActionType.LINE, "Line", null));
		this.add(makeButton(ButtonType.TOGGLE, ActionType.RECTANGLE, "Rectangle", null));
		this.add(makeButton(ButtonType.TOGGLE, ActionType.ELLIPSE, "Ellipse", null));
		this.add(makeButton(ButtonType.TOGGLE, ActionType.ERASER, "Eraser", null));
		this.add(makeButton(ButtonType.TOGGLE, ActionType.FILL, "Fill", null));
		this.add(Box.createRigidArea(new Dimension(0, 10)));
		this.add(makeButton(ButtonType.NORMAL, ActionType.COLOR, "Color", null));
		this.add(Box.createRigidArea(new Dimension(0, 10)));
		this.add(makeButton(ButtonType.NORMAL, ActionType.TURN_CW, "Turn CW", null));
		this.add(makeButton(ButtonType.NORMAL, ActionType.TURN_CCW, "Turn CCW", null));
		this.add(makeButton(ButtonType.NORMAL, ActionType.TURN_180, "Turn 180", null));
		this.add(makeButton(ButtonType.NORMAL, ActionType.FLIP_HORIZONTAL, "Flip horizontally",
				null));
		this.add(makeButton(ButtonType.NORMAL, ActionType.FLIP_VERTICAL, "Flip vertically", null));
		this.add(Box.createRigidArea(new Dimension(0, 10)));
		this.add(makeButton(ButtonType.NORMAL, ActionType.UNDO, "Undo", null));

		this.setBackground(bgColor);
		this.setBorder(BorderFactory.createMatteBorder(0, 15, 0, 15, bgColor));
	}

	@Override
	/**
	 * @return An ActionType enum value to indicate which button is toggled.
	 */
	public ActionType getPaintMode() {
		if(toggledButton == null){
			return ActionType.NONE;
		}
		return toggledButton;
	}

	/**
	 * A factory method that makes a button for this panel.
	 * 
	 * @param type
	 *            Type of the Button. Possible values are listed in ButtonType enum defined in
	 *            I_MenuPanel interface.
	 * @param action
	 *            Action Command. Used to identify which button was pressed. Possible values are
	 *            listed in ActionType enum in I_MenuPanel interface.
	 * @param text
	 *            Text to be placed on the button
	 * @param icon
	 *            Icon to be placed on the button.
	 * @return A button implementing AbstractButton interface.
	 */
	private AbstractButton makeButton(ButtonType type, ActionType action, String text, Icon icon) {
		AbstractButton b;

		switch (type) {
		case TOGGLE:
			b = new JToggleButton(text, icon);
			toggle.add(b);
			b.addActionListener(this);
			break;
		case NORMAL:
		default:
			b = new JButton(text, icon);
			b.addActionListener(p.getPaintPanel());
			break;
		}

		b.setMaximumSize(MIN_SIZE);
		b.setMaximumSize(MAX_SIZE);
		b.setActionCommand(action.name());

		return b;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		toggledButton = ActionType.valueOf(e.getActionCommand());
	}
}
