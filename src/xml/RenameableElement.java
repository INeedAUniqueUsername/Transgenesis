package xml;

import panels.XMLPanel;
import window.FrameOld;

public class RenameableElement extends DesignElementOld {

	public RenameableElement(String name) {
		super(name);
	}
	public void initializeFrame(XMLPanel panel) {
		super.initializeFrame(panel);
		panel.nameField.setEditable(true);
		
	}
}
