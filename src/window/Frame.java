package window;

import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import designType.DesignType;
import designType.OverlayType;
import designType.Power;
import mod.TranscendenceMod;
import xml.Attribute;
import xml.Element;
import xml.IElement;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Entity;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;


public class Frame extends JFrame {
	
	DefaultTreeModel elementTreeModel;
	JTree elementTree;
	JScrollPane elementTreePane;
	DefaultTreeCellRenderer elementTreeCellRenderer;
	
	List<TranscendenceMod> mods;
	
	JPanel attributePanel;
	//JPanel subelementPanel;
	public Frame() {
		
		setTitle("TransGenesis");
		setSize(1920, 1080);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0, 2));
		add(panel);
		
		//mods = Loader.loadAllMods(new File("C:\\Users\\Alex\\Desktop\\Transcendence Multiverse\\Extensions"));
		DefaultMutableTreeNode origin = new DefaultMutableTreeNode();
		/*
		for(TranscendenceMod tm : mods) {
			origin.add(tm.toTreeNode());
		}
		*/
		origin.add(new Power().toTreeNode());
		origin.add(new OverlayType().toTreeNode());
		
		elementTreeCellRenderer = new DefaultTreeCellRenderer() {
			
			public Component getTreeCellRendererComponent(
		        JTree tree,
		        Object value,
		        boolean sel,
		        boolean expanded,
		        boolean leaf,
		        int row,
		        boolean hasFocus) {

			    super.getTreeCellRendererComponent(
			                    tree, value, sel,
			                    expanded, leaf, row,
			                    hasFocus);
			    Element element = (Element) ((DefaultMutableTreeNode) value).getUserObject();
			
			    return this;
			}
		};
		
		elementTreeModel = new DefaultTreeModel(origin);
		elementTree = new JTree(elementTreeModel);
	    elementTree.getSelectionModel().setSelectionMode
	    	(TreeSelectionModel.SINGLE_TREE_SELECTION);
	    elementTree.setAlignmentX(Component.CENTER_ALIGNMENT);
	    elementTree.setFont(Window.FONT_DEFAULT);
	    elementTree.setName("XML");
	    elementTree.setShowsRootHandles(true);
	    elementTree.setCellRenderer(elementTreeCellRenderer);
	    elementTree.expandRow(0);
	    elementTree.setSelectionPath(new TreePath(origin));
	    elementTree.addTreeSelectionListener(new TreeSelectionListener() {
	    	@Override
	    	public void valueChanged(TreeSelectionEvent arg0) {
	    		// TODO Auto-generated method stub
	    		DefaultMutableTreeNode node = (DefaultMutableTreeNode)
	    				elementTree.getLastSelectedPathComponent();
	    		Element element = (Element) node.getUserObject();
	    		initializeFromElement(element);
	    	}
	    });
	    elementTreePane = new JScrollPane(elementTree);
	    elementTreePane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		panel.add(elementTreePane);
		
		attributePanel = new JPanel();
		panel.add(attributePanel);
		
		initializeFromElement(new Power());
		//setAttributes(new Power());
		
		
		pack();
		setVisible(true);
	}
	public void initializeFromElement(IElement e) {
		System.out.println("Initialize from element: " + e.getName());
		attributePanel.removeAll();
		attributePanel.setLayout(new GridLayout(0, 2));
		for(Attribute a : e.getAttributes()) {
			attributePanel.add(createLabel(a.getName() + "="));
			attributePanel.add(a.getValueType().getInputField(a.getValue()));
		}
		/*
		subelementPanel.removeAll();
		subelementPanel.setLayout(new GridLayout(0, 1));
		Frame f = this;
		for(IElement sub : e.getSubElements()) {
			JButton j = new JButton(sub.getName());
			j.setFont(Window.FONT_DEFAULT);
			j.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					System.out.println("Button clicked");
					f.initializeFromElement(sub);
				}
				
			});
			subelementPanel.add(j);
		}
		*/
		pack();
	}
	public void setAttributes(Element e) {
		List<Attribute> attributes = e.getAttributes();
		Component[] fields = attributePanel.getComponents();
		for(int i = 0; i < attributes.size(); i += 1) {
			String value = "";
			Component field = fields[i];
			if(field instanceof JTextField) {
				value = ((JTextField) field).getText();
			} else if(field instanceof JComboBox) {
				value = (String) ((JComboBox) field).getSelectedItem();
			}
			attributes.get(i).setValue(value);
		}
	}
	public static JLabel createLabel(String text) {
		JLabel result = new JLabel();
		result.setFont(Window.FONT_DEFAULT);
		result.setText(text);
		return result;
	}
	
	public void updateTreeText(Element se)
	{
		elementTreeModel.nodeChanged(getNode(se));
	}
	
	public DefaultMutableTreeNode getNode(Element element)
	{
		DefaultMutableTreeNode theNode = null;
		for (Enumeration<DefaultMutableTreeNode> e = (Enumeration<DefaultMutableTreeNode>) ((DefaultMutableTreeNode) elementTreeModel.getRoot()).depthFirstEnumeration(); e.hasMoreElements() && theNode == null;) {
		    DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.nextElement();
		    if (node.getUserObject().equals(element)) {
		        theNode = node;
		        break;
		    }
		}
		System.out.println("Found Node: " + theNode.toString());
		System.out.println("Found Parent: " + theNode.getParent().toString());
		return theNode;
	}
	public void expandTreeNodes()
	{
		for(int i = 0; i < elementTree.getRowCount(); i++){
	        elementTree.expandRow(i);
	    }
	}
}
