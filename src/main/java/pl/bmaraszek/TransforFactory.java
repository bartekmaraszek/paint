package pl.bmaraszek;

import pl.bmaraszek.transforms.*;

public class TransforFactory {

	public enum TransformType {
		TURN_CW, TURN_CCW, TURN_180, FLIP_HORIZONTAL, FLIP_VERTICAL
	}

	private final static I_Transform turnCW = new TurnCW();
	private final static I_Transform turnCCW = new TurnCCW();
	private final static I_Transform turn180 = new Turn180();
	private final static I_Transform flipH = new FlipHorizontal();
	private final static I_Transform flipV = new FlipVertical();

	/**
	 * 
	 * @param t
	 *            Type of TransformCommand to create.
	 * @return A static copy of object implementing I_TransformCommand interface.
	 */
	public static I_Transform getTransform(TransformType t) {
		switch (t) {
		case TURN_CW:
			return turnCW;
		case TURN_CCW:
			return turnCCW;
		case TURN_180:
			return turn180;
		case FLIP_HORIZONTAL:
			return flipH;
		case FLIP_VERTICAL:
			return flipV;
		default:
			return turnCW;
		}
	}
}
