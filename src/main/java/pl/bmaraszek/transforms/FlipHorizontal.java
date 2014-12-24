package pl.bmaraszek.transforms;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class FlipHorizontal implements I_Transform {

	@Override
	public BufferedImage transform(BufferedImage bi) {
		AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
		tx.translate(-bi.getWidth(null), 0);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		return op.filter(bi, null);
	}

}
