package pl.bmaraszek;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.LinkedList;

import javax.swing.JColorChooser;
import javax.swing.JPanel;

import pl.bmaraszek.I_MenuPanel.ActionType;
import pl.bmaraszek.TransforFactory.TransformType;

public class PaintPanel extends JPanel implements I_PaintPanel, ActionListener, MouseListener,
		MouseMotionListener {

	private static final long serialVersionUID = 1L;
	private static final Dimension DEFAULT_IMAGE_SIZE = new Dimension(400, 400);
	private static final int HISTORY_SIZE = 3;

	private Point start = new Point(0, 0);
	private Point end = new Point(0, 0);
	private boolean mouseUp, dragging;
	private Color color = new Color(0, 0, 0);
	private LinkedList<BufferedImage> history = new LinkedList<BufferedImage>();
	/*
	 * For drawing freehand:
	 */
	private Point dot[] = new Point[1000];
	private int dots = 0;

	private final I_Painter p;
	private BufferedImage image;

	public void clearImage(BufferedImage bi) {
		for (int i = 0; i < DEFAULT_IMAGE_SIZE.width; ++i) {
			for (int j = 0; j < DEFAULT_IMAGE_SIZE.height; ++j) {
				bi.setRGB(i, j, Color.WHITE.getRGB());
			}
		}
	}

	public PaintPanel(I_Painter p) {
		this.p = p;
		restart();

		Cursor hourglassCursor = new Cursor(Cursor.CROSSHAIR_CURSOR);
		setCursor(hourglassCursor);

		addMouseListener(this);
		addMouseMotionListener(this);
	}

	public void restart() {
		image = new BufferedImage(DEFAULT_IMAGE_SIZE.width, DEFAULT_IMAGE_SIZE.height,
				BufferedImage.TYPE_INT_RGB);
		clearImage(image);
		clearCoordinates();
		repaint();
	}

	private static BufferedImage deepCopy(BufferedImage bi) {
		ColorModel cm = bi.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = bi.copyData(null);
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D gImage;

		if (!dragging) {
			gImage = (Graphics2D) image.getGraphics();
		} else {
			gImage = (Graphics2D) g;
			g.drawImage(image, 0, 0, this);
			gImage.clip(new Rectangle2D.Double(0, 0, image.getWidth(), image.getHeight()));
		}

		if (color != null) {
			gImage.setColor(color);
		} else {
			gImage.setColor(new Color(0, 0, 0));
		}

		if (mouseUp || dragging) {
			Point tempStart, tempEnd;

			tempStart = new Point(Math.min(end.x, start.x), Math.min(end.y, start.y));
			tempEnd = new Point(Math.max(end.x, start.x), Math.max(end.y, start.y));

			tempStart = new Point(Math.min(tempStart.x, image.getWidth()), Math.min(tempStart.y,
					image.getHeight()));
			tempEnd = new Point(Math.min(tempEnd.x, image.getWidth()), Math.min(tempEnd.y,
					image.getHeight()));

			int width = tempEnd.x - tempStart.x;
			int height = tempEnd.y - tempStart.y;

			ActionType s = p.getMenuPanel().getPaintMode();

			switch (s) {
			case LINE:
				Line2D.Double drawLine = new Line2D.Double(start.x, start.y, end.x, end.y);
				gImage.draw(drawLine);
				break;
			case RECTANGLE:
				Rectangle2D.Double rectangle = new Rectangle2D.Double(tempStart.x, tempStart.y,
						width, height);
				gImage.draw(rectangle);
				break;
			case ELLIPSE:
				Ellipse2D.Double ellipse = new Ellipse2D.Double(tempStart.x, tempStart.y, width,
						height);
				gImage.draw(ellipse);
				break;
			case ERASER:
				// TODO: implement proper eraser
				gImage.setPaint(Color.WHITE);
				Rectangle2D.Double erase = new Rectangle2D.Double(tempStart.x, tempStart.y, width,
						height);
				gImage.fill(erase);
				break;
			case FILL:
				floodFill(this.getMousePosition(), gImage.getColor().getRGB());
				break;
			case FREEHAND:
				Line2D.Double freeDrawLine;
				for (int loop_index = 0; loop_index < dots - 1; loop_index++) {
					if (dragging) {
						freeDrawLine = new Line2D.Double(dot[loop_index].x, dot[loop_index].y,
								dot[loop_index + 1].x, dot[loop_index + 1].y);
					} else {
						freeDrawLine = new Line2D.Double(dot[loop_index].x, dot[loop_index].y,
								dot[loop_index + 1].x, dot[loop_index + 1].y);
					}
					gImage.draw(freeDrawLine);
				}
				if (!dragging) {
					dots = 0;
				}
				break;
			default:
				;
				break;
			}
		}

		if (!dragging) {
			g.drawImage(image, 0, 0, this);
		}
	}

	/**
	 * 
	 * @param clicked
	 *            - Coordinates of pixel clicked by the user.
	 * @param newColor
	 *            - Color used for filling the closed area.
	 * 
	 *            floodFill is implemented using queue, because using recursion caused Stack
	 *            Overflow error. The function doesn't need target color specified - It assumes that
	 *            the color of the pixel clicked is the target color.
	 */
	private void floodFill(Point clicked, int newColor) {
		if ((clicked == null) || (clicked.x > this.getImage().getWidth())
				|| (clicked.y > this.getImage().getHeight())) {
			return;
		}
		int oldColor = this.getImage().getRGB(clicked.x, clicked.y);
		if (oldColor == newColor) {
			return;
		}
		LinkedList<Point> q = new LinkedList<Point>();
		Point n = null;
		q.addLast(clicked);
		while (!q.isEmpty()) {
			n = q.removeLast();
			if (this.getImage().getRGB(n.x, n.y) == oldColor) {
				this.getImage().setRGB(n.x, n.y, newColor);

				if (n.x + 1 < this.getImage().getWidth()) {
					q.addLast(new Point(n.x + 1, n.y));
				}
				if (n.x - 1 > 0) {
					q.addLast(new Point(n.x - 1, n.y));
				}
				if (n.y + 1 < this.getImage().getHeight()) {
					q.addLast(new Point(n.x, n.y + 1));
				}
				if (n.y - 1 > 0) {
					q.addLast(new Point(n.x, n.y - 1));
				}
			}
		}
		return;
	}

	@Override
	public BufferedImage getImage() {
		return image;
	}

	@Override
	public void setImage(BufferedImage bi) {
		this.image = bi;
		repaint();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		dragging = true;
		if (new Rectangle(0, 0, image.getWidth(), image.getHeight()).contains(e.getX(), e.getY())) {
			if (p.getMenuPanel().getPaintMode() == ActionType.FREEHAND) {
				dot[dots] = new Point(e.getX(), e.getY());
				dots++;
			}
		}
		if (new Rectangle(0, 0, image.getWidth(), image.getHeight()).contains(start.x, start.y)) {
			end = new Point(e.getX(), e.getY());
			repaint();
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {

		mouseUp = false;
		start = new Point(e.getX(), e.getY());
		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (p.getMenuPanel().getPaintMode() != ActionType.NONE) {
			end = new Point(e.getX(), e.getY());
			mouseUp = true;
			dragging = false;
			Rectangle r = new Rectangle(0, 0, this.getImage().getWidth(), this.getImage()
					.getHeight());
			if (r.contains(start) && r.contains(end)) {
				addToHistory();
			}
			repaint();
		}
	}

	private void addToHistory() {
		while (history.size() > HISTORY_SIZE) {
			history.removeLast();
		}
		history.addFirst(deepCopy(image));
	}

	private void clearCoordinates() {
		start.x = -20;
		start.y = -20;
		end.x = -20;
		end.y = -20;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		switch (ActionType.valueOf(e.getActionCommand())) {
		case COLOR:
			color = JColorChooser.showDialog(this, "Select your color", Color.black);
			break;
		case FLIP_HORIZONTAL:
			addToHistory();
			image = TransforFactory.getTransform(TransformType.FLIP_HORIZONTAL).transform(
					image);
			break;
		case FLIP_VERTICAL:
			addToHistory();
			image = TransforFactory.getTransform(TransformType.FLIP_VERTICAL).transform(
					image);
			break;
		case TURN_CW:
			addToHistory();
			image = TransforFactory.getTransform(TransformType.TURN_CW).transform(image);
			break;
		case TURN_CCW:
			addToHistory();
			image = TransforFactory.getTransform(TransformType.TURN_CCW).transform(image);
			break;
		case TURN_180:
			addToHistory();
			image = TransforFactory.getTransform(TransformType.TURN_180).transform(image);
			break;
		case UNDO:
			this.setImage(history.pop());
			break;
		default:
			break;
		}

		clearCoordinates();
		repaint();
	}
}
