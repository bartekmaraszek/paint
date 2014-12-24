package pl.bmaraszek;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class Painter extends JFrame implements I_Painter, ActionListener {

	private static final long serialVersionUID = 1L;
	/*
	 * Upper MenuBar:
	 */
	private JMenuBar menuBar;
	private JMenu fileMenu;
	
	private FileDialog dialog = new FileDialog(this);
	/*
	 * Default window and image size:
	 */
	private int imageWidth = 600;
	private int imageHeight = 450;
	private int windowWidth = 800;
	private int windowHeight = 600;
	/*
	 * menuPanel and paintPanel provide the basic functionality:
	 */
	private final I_PaintPanel paintPanel = new PaintPanel(this);
	private final I_MenuPanel menuPanel = new MenuPanel(this);
	

	private void setMenuBar() {
		menuBar = new JMenuBar();
		fileMenu = new JMenu("File");
		
		addJMenuItem(fileMenu, MenuCommand.NEW, "New", this);
		addJMenuItem(fileMenu, MenuCommand.OPEN, "Open...", this);
		addJMenuItem(fileMenu, MenuCommand.SAVE, "Save as...", this);
		addJMenuItem(fileMenu, MenuCommand.EXIT, "Exit", this);

		menuBar.add(fileMenu);
		setJMenuBar(menuBar);
	}
	
	private void addJMenuItem(JMenu target, MenuCommand actionCommand, String text, ActionListener actionListener){
		JMenuItem jmi = new JMenuItem(text);
		target.add(jmi);
		jmi.setActionCommand(actionCommand.name());
		jmi.addActionListener(actionListener);
	}

	public static void main(String[] args) {
		new Painter();
	}

	public Painter() {
		setLayout(new BorderLayout(0,0));
		this.add((Component) menuPanel, BorderLayout.LINE_START);
		this.add((Component) paintPanel, BorderLayout.CENTER);

		setMenuBar();

		dialog = new FileDialog(this, "File Dialog");

		setSize(windowWidth, windowHeight);

		setTitle("Java Paint");
		setVisible(true);

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}

	private void openFile() {
		try {
			dialog.setMode(FileDialog.LOAD);
			dialog.setVisible(true);

			if (dialog.getFile() != "") {
				File inputFile = new File(dialog.getDirectory() + dialog.getFile());
				paintPanel.setImage(ImageIO.read(inputFile));
				if (paintPanel.getImage() != null) {
					imageWidth = paintPanel.getImage().getWidth();
					imageHeight = paintPanel.getImage().getHeight();
					setSize(imageWidth + menuPanel.getSize().width + 25, imageHeight + 75);
					repaint();
				}
			}
		} catch (Exception exp) {
			System.out.println("Error while opening file: " + exp.getMessage());
		}
	}

	private void saveFile() {
		dialog.setMode(FileDialog.SAVE);
		dialog.setVisible(true);

		try {
			if (dialog.getFile() != "") {
				String outfile = dialog.getFile();
				File outputFile = new File(dialog.getDirectory() + outfile);
				ImageIO.write(paintPanel.getImage(), outfile.substring(outfile.length() - 3, outfile.length()), outputFile);
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals(MenuCommand.NEW.name())){
			paintPanel.restart();
		}else if(e.getActionCommand().equals(MenuCommand.OPEN.name())){
			openFile();
		}else if(e.getActionCommand().equals(MenuCommand.SAVE.name())){
			saveFile();
		}else if(e.getActionCommand().equals(MenuCommand.EXIT.name())){
			System.exit(0);
		}
	}

	@Override
	public I_MenuPanel getMenuPanel() {
		return menuPanel;
	}

	@Override
	public I_PaintPanel getPaintPanel() {
		return paintPanel;
	}
}
