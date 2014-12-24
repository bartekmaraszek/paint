package pl.bmaraszek.transforms;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class Turn180 implements I_Transform {

	@Override
	public BufferedImage transform(BufferedImage bi) {
		AffineTransform tx = AffineTransform.getRotateInstance(-Math.PI);
		tx.translate(-bi.getWidth(), -bi.getHeight());
		AffineTransformOp op3 = new AffineTransformOp(tx,
				AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		return op3.filter(bi, null);
	}

}
