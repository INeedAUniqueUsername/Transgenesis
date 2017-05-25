package mod;

import java.util.TreeMap;

import javax.swing.tree.DefaultMutableTreeNode;

import xml.Element;

public class TranscendenceExtension extends TranscendenceMod {
	public TranscendenceExtension() {
		super();
		// TODO Auto-generated constructor stub
	}
	TreeMap<String, String> unid_index;
	public DefaultMutableTreeNode toTreeNode() {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(this);
		for (Element e : getSubElements()) {
			node.add(e.toTreeNode());
		}
		return node;
	}
}
