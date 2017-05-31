package window;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Enumeration;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.jcabi.xml.XMLDocument;

import mod.ExtensionFactory;
import mod.TranscendenceMod;
import xml.Attribute;
import xml.Element;

public class Frame extends JFrame {
	
	public static final int SCREEN_WIDTH;
	public static final int SCREEN_HEIGHT;
	static {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		SCREEN_WIDTH = (int) (screenSize.getWidth() * 0.95);
		SCREEN_HEIGHT = (int) (screenSize.getHeight() * 0.90);
	}
	private final DefaultTreeModel elementTreeModel;
	private final JTree elementTree;
	private final JScrollPane elementTreePane;
	private final DefaultTreeCellRenderer elementTreeCellRenderer;
	
	private final List<TranscendenceMod> mods;
	private Element selected;
	
	JLabel documentation;
	private final JPanel labelPanel;
	private final JPanel fieldPanel;
	private final JPanel subElementPanel;
	private final JTextArea text;
	private final JButton applyButton;
	private final JButton xmlButton;
	//JPanel subelementPanel;
	public Frame() {
		
		setTitle("TransGenesis");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new JPanel();
		panel.setAlignmentY(JPanel.TOP_ALIGNMENT);
		//panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		add(panel);
		
		JPanel leftPanel = new JPanel();
		leftPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		leftPanel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		JPanel rightPanel = new JPanel();
		rightPanel.setAlignmentX(JPanel.CENTER_ALIGNMENT);
		rightPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		
		int rightPanel_width = (int) (SCREEN_WIDTH * 0.75);
		int rightPanel_height = SCREEN_HEIGHT;
		
		rightPanel.setPreferredSize(new Dimension(rightPanel_width, rightPanel_height));
		
		String dir = "C:\\Users\\Alex\\Desktop\\Transcendence Multiverse\\ParseTest\\Test.xml";
		mods = Loader.loadAllMods(new File(dir));
		DefaultMutableTreeNode origin = new DefaultMutableTreeNode(new Element(dir));
		for(TranscendenceMod tm : mods) {
			if(tm == null) {
				System.out.println("Null extension found");
			} else {
				origin.add(tm.toTreeNode());
			}
		}
		
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
	    elementTree.setFont(Window.FONT_MEDIUM);
	    elementTree.setName("XML");
	    elementTree.setShowsRootHandles(true);
	    elementTree.setCellRenderer(elementTreeCellRenderer);
	    elementTree.expandRow(0);
	    elementTree.setSelectionPath(new TreePath(origin));
	    elementTree.addTreeSelectionListener(new TreeSelectionListener() {
	    	@Override
	    	public void valueChanged(TreeSelectionEvent arg0) {
	    		// TODO Auto-generated method stub
	    		if(selected != null) {
	    			setAttributes(selected);
	    		}
	    		DefaultMutableTreeNode node = (DefaultMutableTreeNode)
	    				elementTree.getLastSelectedPathComponent();
	    		Element element = (Element) node.getUserObject();
	    		selectElement(element);
	    	}
	    });
	    elementTreePane = new JScrollPane(elementTree);
	    elementTreePane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	    elementTreePane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	    elementTreePane.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		leftPanel.add(elementTreePane);
		
		documentation = new JLabel("Documentation Here");
		documentation.setFont(Window.FONT_MEDIUM);
		rightPanel.add(documentation);
		
		JPanel elementPanel = new JPanel();
		elementPanel.setLayout(new BoxLayout(elementPanel, BoxLayout.X_AXIS));
		JPanel attributePanel = new JPanel();
		attributePanel.setLayout(new GridLayout(0, 2));
		attributePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		labelPanel = new JPanel();
		fieldPanel = new JPanel();
		labelPanel.setLayout(new GridLayout(0, 1));
		fieldPanel.setLayout(new GridLayout(0, 1));
		attributePanel.add(labelPanel);
		attributePanel.add(fieldPanel);
		JScrollPane attributePanelScroll = new JScrollPane(attributePanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		elementPanel.add(attributePanelScroll);
		
		subElementPanel = new JPanel();
		subElementPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		subElementPanel.setLayout(new GridLayout(0, 1));
		JScrollPane subElementPanelScroll = new JScrollPane(subElementPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		elementPanel.add(subElementPanelScroll);
		
		rightPanel.add(elementPanel);
		
		text = new JTextArea();
		text.setTabSize(4);
		text.setFont(Window.FONT_MEDIUM);
		JScrollPane textPanel = new JScrollPane(text, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		textPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		rightPanel.add(textPanel);
		
		applyButton = new JButton();
		applyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(selected != null) {
					setAttributes(selected);
				}
			}
		});
		applyButton.setFont(Window.FONT_LARGE);
		applyButton.setText("Apply");
		applyButton.setAlignmentX(LEFT_ALIGNMENT);
		rightPanel.add(applyButton);
		
		xmlButton = new JButton("Generate XML");
		xmlButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(selected != null) {
					JTextArea ta = new JTextArea(selected.getXML());
					ta.setFont(Window.FONT_MEDIUM);
					ta.setTabSize(4);
					ta.setEditable(false);
					JScrollPane pane = new JScrollPane(ta, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
					pane.setMaximumSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
					JOptionPane.showMessageDialog(null, pane);
				}
			}
			
		});
		xmlButton.setFont(Window.FONT_LARGE);
		xmlButton.setAlignmentX(LEFT_ALIGNMENT);
		rightPanel.add(xmlButton);
		
		panel.add(leftPanel);
		panel.add(rightPanel);
		setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		panel.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		leftPanel.setPreferredSize(new Dimension((int) (SCREEN_WIDTH * 0.2), SCREEN_HEIGHT));
		elementTreePane.setPreferredSize(new Dimension((int) (SCREEN_WIDTH * 0.2), SCREEN_HEIGHT));
		
		attributePanel.setMaximumSize(	new Dimension((int) (rightPanel_width * 0.75), (int) (rightPanel_height * 0.25)));
		attributePanelScroll.setMaximumSize(attributePanel.getMaximumSize());
		labelPanel.setMaximumSize(		new Dimension((int) (rightPanel_width * 0.75), (int) (rightPanel_height * 0.25)));
		fieldPanel.setMaximumSize(		new Dimension((int) (rightPanel_width * 0.75), (int) (rightPanel_height * 0.25)));
		
		subElementPanel.setMaximumSize(new Dimension((int) (rightPanel_width * 0.25), (int) (rightPanel_height * 0.25)));
		subElementPanelScroll.setMaximumSize(subElementPanel.getMaximumSize());
		
		elementPanel.setMaximumSize(	new Dimension(rightPanel_width, (int) (rightPanel_height * 0.25)));
		
		textPanel.setMinimumSize(new Dimension((int) (SCREEN_WIDTH * 0.75), (int) (SCREEN_HEIGHT * 0.5)));
		pack();
		setVisible(true);
	}
	public void selectElement(Element e) {
		System.out.println("Initialize from element: " + e.getName());
		selected = e;
		e.initializeFrame(this);
		pack();
		repaint();
	}
	public void setAttributes(Element e) {
		List<Attribute> attributes = e.getAttributes();
		Component[] fields = fieldPanel.getComponents();
		for(int i = 0; i < attributes.size(); i++) {
			String value = "";
			Component field = fields[i];
			if(field instanceof JTextField) {
				value = ((JTextField) field).getText();
			} else if(field instanceof JComboBox) {
				value = (String) ((JComboBox) field).getSelectedItem();
			}
			attributes.get(i).setValue(value);
		}
		e.setText(text.getText());
	}
	public static JLabel createLabel(String text) {
		JLabel result = new JLabel();
		result.setFont(Window.FONT_MEDIUM);
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
	public void addElement(Element se)
	{
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(se);
		elementTreeModel.insertNodeInto(node, (DefaultMutableTreeNode) elementTree.getLastSelectedPathComponent(), 0);
	    expandTreeNodes();
	    System.out.println("Tree Path (Create): " + new TreePath(elementTreeModel.getPathToRoot(node)));
	    elementTree.setSelectionPath(new TreePath(elementTreeModel.getPathToRoot(node)));
	}
	public JPanel getAttributeLabelPanel() {
		return labelPanel;
	}
	public JPanel getAttributeFieldPanel() {
		return fieldPanel;
	}
	public JPanel getSubElementPanel() {
		return subElementPanel;
	}
	public JTextArea getTextArea() {
		return text;
	}
}
