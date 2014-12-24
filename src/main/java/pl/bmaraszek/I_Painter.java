package pl.bmaraszek;

public interface I_Painter {
	
	public static enum MenuCommand{
		NEW, OPEN, SAVE, EXIT
	}
	
	public I_MenuPanel getMenuPanel();
	public I_PaintPanel getPaintPanel();
}
