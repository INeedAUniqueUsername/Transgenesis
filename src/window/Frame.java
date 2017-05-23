package window;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import designType.DesignElement;
import designType.OverlayType;
import designType.Power;
import mod.TranscendenceMod;
import xml.Attribute;

public class Frame extends JFrame {
	
	public static final int SCREEN_WIDTH;
	public static final int SCREEN_HEIGHT;
	static {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		SCREEN_WIDTH = (int) screenSize.getWidth();
		SCREEN_HEIGHT = (int) screenSize.getHeight();
	}
	DefaultTreeModel elementTreeModel;
	JTree elementTree;
	JScrollPane elementTreePane;
	DefaultTreeCellRenderer elementTreeCellRenderer;
	
	List<TranscendenceMod> mods;
	DesignElement selected;
	
	
	JPanel labelPanel;
	JPanel fieldPanel;
	JTextArea text;
	JButton applyButton;
	//JPanel subelementPanel;
	public Frame() {
		
		setTitle("TransGenesis");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		panel.setPreferredSize(new Dimension(SCREEN_WIDTH, (int) (SCREEN_HEIGHT * 0.9)));
		add(panel);
		
		JPanel leftPanel = new JPanel();
		leftPanel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		rightPanel.setPreferredSize(new Dimension(SCREEN_WIDTH/4, (int) (SCREEN_HEIGHT * 0.9)));
		panel.add(leftPanel);
		panel.add(rightPanel);
		
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
	    		DefaultMutableTreeNode node = (DefaultMutableTreeNode)
	    				elementTree.getLastSelectedPathComponent();
	    		DesignElement element = (DesignElement) node.getUserObject();
	    		selectElement(element);
	    	}
	    });
	    elementTreePane = new JScrollPane(elementTree);
	    elementTreePane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	    elementTreePane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		leftPanel.add(elementTreePane);
		
		JPanel attributePanel = new JPanel();
		attributePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		labelPanel = new JPanel();
		fieldPanel = new JPanel();
		attributePanel.add(labelPanel);
		attributePanel.add(fieldPanel);
		rightPanel.add(attributePanel);
		
		text = new JTextArea();
		text.setFont(Window.FONT_LARGE);
		applyButton = new JButton();
		applyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				setAttributes(selected);
			}
		});
		applyButton.setFont(Window.FONT_LARGE);
		applyButton.setText("Apply");
		rightPanel.add(text);
		rightPanel.add(applyButton);
		
		
		pack();
		setVisible(true);
	}
	public void selectElement(DesignElement e) {
		System.out.println("Initialize from element: " + e.getName());
		selected = e;
		labelPanel.removeAll();
		fieldPanel.removeAll();
		labelPanel.setLayout(new GridLayout(20, 1));
		fieldPanel.setLayout(new GridLayout(20, 1));
		
		labelPanel.setPreferredSize(new Dimension(200, SCREEN_HEIGHT/3));
		fieldPanel.setPreferredSize(new Dimension(SCREEN_WIDTH/4, SCREEN_HEIGHT/3));
		for(Attribute a : e.getAttributes()) {
			labelPanel.add(createLabel(a.getName() + "="));
			JComponent inputField = a.getValueType().getInputField(a.getValue());
			fieldPanel.add(inputField);
		}
		text.setText(e.getText());
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
		repaint();
	}
	public void setAttributes(DesignElement e) {
		List<Attribute> attributes = e.getAttributes();
		Component[] fields = fieldPanel.getComponents();
		for(int i = 0; i < attributes.size(); i++) {
			String value = "";
			Component field = fields[i];
			if(field instanceof JTextArea) {
				value = ((JTextArea) field).getText();
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
	public void expandTreeNodes()
	{
		for(int i = 0; i < elementTree.getRowCount(); i++){
	        elementTree.expandRow(i);
	    }
	}
}
