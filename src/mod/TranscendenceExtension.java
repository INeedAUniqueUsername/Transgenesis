package mod;

import java.util.TreeMap;

import javax.swing.tree.DefaultMutableTreeNode;

import designType.DesignElement;

public class TranscendenceExtension extends TranscendenceMod {
	public TranscendenceExtension() {
		super("TranscendenceExtension");
		// TODO Auto-generated constructor stub
	}
	TreeMap<String, String> unid_index;
	public DefaultMutableTreeNode toTreeNode() {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(this);
		for (DesignElement e : getSubElements()) {
			node.add(e.toTreeNode());
		}
		return node;
	}
}
