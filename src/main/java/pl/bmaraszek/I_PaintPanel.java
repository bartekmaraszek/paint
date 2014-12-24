package pl.bmaraszek;

import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public interface I_PaintPanel extends ActionListener {

	public BufferedImage getImage();
	public void restart();
	public void setImage(BufferedImage bi);
	public void clearImage(BufferedImage bi);
}
