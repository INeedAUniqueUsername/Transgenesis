package window;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.Enumeration;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import net.miginfocom.layout.AC;
import net.miginfocom.layout.CC;
import net.miginfocom.swing.MigLayout;

import com.jcabi.xml.XMLDocument;

import designType.TypeFactory;
import designType.TypeFactory.Types;
import mod.ExtensionFactory;
import mod.TranscendenceMod;
import xml.Attribute;
import xml.DesignElement;

public class Frame extends JFrame {
	
	public static final int SCREEN_WIDTH;
	public static final int SCREEN_HEIGHT;
	static {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		SCREEN_WIDTH = (int) (screenSize.getWidth());
		SCREEN_HEIGHT = (int) (screenSize.getHeight() * 0.96);
	}
	private final DefaultTreeModel elementTreeModel;
	private final JTree elementTree;
	private final JScrollPane elementTreePane;
	private final DefaultTreeCellRenderer elementTreeCellRenderer;
	
	private final List<TranscendenceMod> mods;
	private DesignElement selected;
	
	JLabel documentation;
	private final JPanel labelPanel;
	private final JPanel fieldPanel;
	private final JPanel subElementPanel;
	private final JTextArea text;
	//private final JButton applyButton;
	private final JButton xmlButton;
	//JPanel subelementPanel;
	public Frame() {
		
		setTitle("TransGenesis");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//String dir = "C:\\Users\\Alex\\Desktop\\Transcendence Multiverse\\ParseTest\\Test.xml";
		String dir = "C:\\Users\\Alex\\Desktop\\Transcendence Multiverse\\Extensions";
		//String dir = JOptionPane.showInputDialog("Specify mod directory");
		mods = Loader.loadAllMods(new File(dir));
		DefaultMutableTreeNode origin = new DefaultMutableTreeNode(new DesignElement(dir));
		for(TranscendenceMod tm : mods) {
			if(tm == null) {
				System.out.println("Null extension found");
			} else {
				origin.add(tm.toTreeNode());
			}
		}
		origin.add(ExtensionFactory.Extensions.TranscendenceAdventure.create().toTreeNode());
		
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
			    DesignElement element = (DesignElement) ((DefaultMutableTreeNode) value).getUserObject();
			    
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
	    		DesignElement element = (DesignElement) node.getUserObject();
	    		selectElement(element);
	    	}
	    });
	    elementTreePane = createScrollPane(elementTree);
	    //elementTreePane.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		
		documentation = new JLabel("Documentation Here");
		documentation.setFont(Window.FONT_MEDIUM);
		documentation.setVerticalTextPosition(SwingConstants.TOP);
		JScrollPane documentationScroll = createScrollPane(documentation);
		
		JPanel attributePanel = new JPanel();
		attributePanel.setLayout(new GridLayout(0, 2));
		attributePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		labelPanel = new JPanel();
		fieldPanel = new JPanel();
		labelPanel.setLayout(new GridLayout(0, 1));
		fieldPanel.setLayout(new GridLayout(0, 1));
		attributePanel.add(labelPanel);
		attributePanel.add(fieldPanel);
		JScrollPane attributeScroll = createScrollPane(attributePanel);
		
		subElementPanel = new JPanel();
		subElementPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		subElementPanel.setLayout(new GridLayout(0, 1));
		JScrollPane subElementScroll = createScrollPane(subElementPanel);
		
		text = new JTextArea();
		text.setTabSize(4);
		text.setFont(Window.FONT_MEDIUM);
		text.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2 && !e.isConsumed()) {
                    e.consume();
                    System.out.println("Double Click");
				}
			}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseReleased(MouseEvent e) {}
		});
		JScrollPane textPanel = createScrollPane(text);
		textPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		/*
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
		*/
		xmlButton = new JButton("Generate XML");
		JFrame f = this;
		xmlButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(selected != null) {
					setAttributes(selected);
					JTextArea ta = new JTextArea(selected.getXML());
					ta.setFont(Window.FONT_MEDIUM);
					ta.setTabSize(4);
					ta.setEditable(false);
					JScrollPane pane = createScrollPane(ta);
					pane.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
					JOptionPane.showMessageDialog(f, pane);
				}
			}
		});
		xmlButton.setFont(Window.FONT_LARGE);
		xmlButton.setAlignmentX(LEFT_ALIGNMENT);
		
		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout());
		panel.add(elementTreePane,
				new CC()
				.x("0")
				.y("0")
				.minWidth("25%")
				.maxWidth("25%")
				.minHeight("95%")
				.maxHeight("95%")
				);
		panel.add(xmlButton,
				new CC()
				.x("0%")
				.y("95%")
				.minWidth("25%")
				.maxWidth("25%")
				.minHeight("5%")
				.maxHeight("5%")
				);
		panel.add(documentationScroll,
				new CC()
				.x("25%")
				.y("0")
				.minWidth("75%")
				.maxWidth("75%")
				.height("20%")
				
				);
		panel.add(attributeScroll,
				new CC()
				.x("25%")
				.y("20%")
				.minWidth("55%")
				.maxWidth("55%")
				.maxHeight("50%")
				);
		panel.add(subElementScroll,
				new CC()
				.x("80%")
				.y("20%")
				.minWidth("20%")
				.maxWidth("20%")
				.maxHeight("50%")
				);
		panel.add(textPanel,
				new CC()
				.x("25%")
				.y("70%")
				.minWidth("75%")
				.maxWidth("75%")
				.minHeight("30%")
				.maxHeight("30%")
				);
		add(panel);
		pack();
		setExtendedState(getExtendedState() | MAXIMIZED_BOTH);
		setVisible(true);
	}
	public static JScrollPane createScrollPane(JComponent c) {
		JScrollPane result = new JScrollPane(c, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		result.getVerticalScrollBar().setUnitIncrement(16);
		return result;
	}
	public void selectElement(DesignElement e) {
		System.out.println("Initialize from element: " + e.getName());
		Dimension size = getSize();
		int state = getExtendedState();
		selected = e;
		setPreferredSize(size);
		e.initializeFrame(this);
		pack();
		repaint();
	}
	public void setAttributes(DesignElement e) {
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
	
	public void updateTreeText(DesignElement se)
	{
		elementTreeModel.nodeChanged(getNode(se));
	}
	
	public DefaultMutableTreeNode getNode(DesignElement element)
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
	public void expandTreeNodes() {
		for(int i = 0; i < elementTree.getRowCount(); i++){
	        elementTree.expandRow(i);
	    }
	}
	public void addElement(DesignElement se)
	{
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(se), parent = (DefaultMutableTreeNode) elementTree.getLastSelectedPathComponent();
		elementTreeModel.insertNodeInto(node, parent, 0);
	    int rowIndex = parent.getIndex(node);
	    elementTree.expandRow(rowIndex);
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
