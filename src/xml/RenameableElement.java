package xml;

import panels.XMLPanel;
import window.Frame;

public class RenameableElement extends DesignElement {

	public RenameableElement(String name) {
		super(name);
	}
	public void initializeFrame(XMLPanel panel) {
		super.initializeFrame(panel);
		panel.nameField.setEditable(true);
		
	}
}
