package pl.bmaraszek.transforms;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class FlipVertical implements I_Transform {

	@Override
	public BufferedImage transform(BufferedImage bi) {
		AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
		tx.translate(0, -bi.getHeight(null));
		AffineTransformOp op = new AffineTransformOp(tx,
				AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		return op.filter(bi, null);
	}

}
