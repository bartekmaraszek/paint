package pl.bmaraszek;

import java.awt.Dimension;
import java.awt.event.ActionListener;

public interface I_MenuPanel extends ActionListener {

	public static enum ButtonType {
		TOGGLE, NORMAL
	}

	public static enum ActionType {
		NONE, FREEHAND, LINE, ELLIPSE, RECTANGLE, ERASER, FILL, COLOR, TURN_CW, TURN_CCW, TURN_180, FLIP_HORIZONTAL, FLIP_VERTICAL, UNDO
	}

	public ActionType getPaintMode();

	public Dimension getSize();
}
