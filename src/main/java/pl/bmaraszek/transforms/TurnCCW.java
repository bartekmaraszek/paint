package pl.bmaraszek.transforms;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class TurnCCW implements I_Transform {

	@Override
	public BufferedImage transform(BufferedImage bi) {
		AffineTransform tx = AffineTransform.getRotateInstance(-Math.PI / 2);
		tx.translate(-bi.getWidth(), 0);
		AffineTransformOp op2 = new AffineTransformOp(tx,
				AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		return op2.filter(bi, null);
	}

}
