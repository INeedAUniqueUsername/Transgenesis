package xml;

import window.FrameOld;

public class RenameableElement extends DesignElementOld {

	public RenameableElement(String name) {
		super(name);
	}
	public void initializeFrame(FrameOld f) {
		f.textArea.setEditable(true);
	}
}
