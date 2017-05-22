package mod;

import java.util.HashMap;

import javax.swing.tree.DefaultMutableTreeNode;

import xml.Element;
import xml.IElement;

public class TranscendenceExtension extends TranscendenceMod {
	HashMap<String, String> unid_index;
	public DefaultMutableTreeNode toTreeNode() {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(this);
		for (IElement e : getSubElements()) {
			node.add(e.toTreeNode());
		}
		return node;
	}
}
