package mod;

import java.util.HashMap;
import java.util.TreeMap;

import javax.swing.tree.DefaultMutableTreeNode;

import xml.Element;
import xml.IElement;

public class TranscendenceExtension extends TranscendenceMod {
	TreeMap<String, String> unid_index;
	public DefaultMutableTreeNode toTreeNode() {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(this);
		for (IElement e : getSubElements()) {
			node.add(e.toTreeNode());
		}
		return node;
	}
}
