package pl.bmaraszek.transforms;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class TurnCW implements I_Transform {

	@Override
	public BufferedImage transform(BufferedImage bi) {
		AffineTransform tx = AffineTransform.getRotateInstance(Math.PI / 2);
		tx.translate(0, -bi.getHeight(null));
		AffineTransformOp op1 = new AffineTransformOp(tx,
				AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		return op1.filter(bi, null);
	}

}
