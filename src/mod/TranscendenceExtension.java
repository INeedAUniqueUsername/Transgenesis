package mod;

import java.util.TreeMap;

import javax.swing.tree.DefaultMutableTreeNode;

import designType.DesignElement;

public class TranscendenceExtension extends TranscendenceMod {
	TreeMap<String, String> unid_index;
	public DefaultMutableTreeNode toTreeNode() {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(this);
		for (DesignElement e : getSubElements()) {
			node.add(e.toTreeNode());
		}
		return node;
	}
}
