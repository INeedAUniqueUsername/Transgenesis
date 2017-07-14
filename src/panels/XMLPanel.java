package panels;

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
import javax.swing.SwingConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import designType.subElements.SubElementType;
import mod.ExtensionFactory.Extensions;
import mod.TranscendenceMod;
import net.miginfocom.layout.CC;
import net.miginfocom.swing.MigLayout;
import window.FrameOld;
import window.Loader;
import window.Window;
import window.Window.Fonts;
import xml.DesignAttribute;
import xml.DesignElementOld;
import static window.Window.Fonts.*;
public class XMLPanel extends JPanel {
	public static final int SCREEN_WIDTH;
	public static final int SCREEN_HEIGHT;
	static {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		SCREEN_WIDTH = (int) (screenSize.getWidth());
		SCREEN_HEIGHT = (int) (screenSize.getHeight() * 0.96);
	}
	public final JFrame frame;
	private final DefaultTreeModel elementTreeModel;
	private final JTree elementTree;
	private final JScrollPane elementTreePane;
	
	private final List<TranscendenceMod> mods;
	private DesignElementOld selected;
	
	public final JTextField nameField;
	public final JLabel documentation;
	public final JPanel labelPanel;
	public final JPanel fieldPanel;
	public final JPanel subElementPanel;
	public final JTextArea textArea;
	public final DefaultMutableTreeNode origin;
	//JPanel subelementPanel;
	public XMLPanel(FrameOld frame) {
		this.frame = frame;
		//String dir = "C:\\Users\\Alex\\Desktop\\Transcendence Multiverse\\ParseTest\\Test.xml";
		String dir = "C:\\Users\\Alex\\Desktop\\Transcendence Multiverse\\TransGenesis Test";
		//String dir = JOptionPane.showInputDialog("Specify mod directory");
		mods = Loader.loadAllMods(new File(dir));
		origin = new DefaultMutableTreeNode(new DesignElementOld(dir));
		for(TranscendenceMod tm : mods) {
			if(tm == null) {
				System.out.println("Null extension found");
			} else {
				origin.add(tm.toTreeNode());
			}
		}
		
		DefaultTreeCellRenderer elementTreeCellRenderer = new DefaultTreeCellRenderer() {
			
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
			    DesignElementOld element = (DesignElementOld) ((DefaultMutableTreeNode) value).getUserObject();
			    
			    return this;
			}
		};
		
		elementTreeModel = new DefaultTreeModel(origin);
		elementTree = new JTree(elementTreeModel);
	    elementTree.getSelectionModel().setSelectionMode
	    	(TreeSelectionModel.SINGLE_TREE_SELECTION);
	    elementTree.setAlignmentX(Component.CENTER_ALIGNMENT);
	    elementTree.setFont(Fonts.Medium.f);
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
	    			setData(selected);
	    		}
	    		DefaultMutableTreeNode node = (DefaultMutableTreeNode)
	    				elementTree.getLastSelectedPathComponent();
	    		DesignElementOld element = (DesignElementOld) node.getUserObject();
	    		selectElement(element);
	    	}
	    });
	    elementTreePane = createScrollPane(elementTree);
	    //elementTreePane.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		
	    nameField = new JTextField("Element name here");
	    nameField.setFont(Title.f);
	    nameField.setEditable(false);
	    
		documentation = new JLabel("Documentation Here");
		documentation.setFont(Medium.f);
		documentation.setVerticalTextPosition(SwingConstants.TOP);
		
		labelPanel = new JPanel();
		fieldPanel = new JPanel();
		labelPanel.setLayout(new GridLayout(0, 1));
		fieldPanel.setLayout(new GridLayout(0, 1));
		
		subElementPanel = new JPanel();
		subElementPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		subElementPanel.setLayout(new GridLayout(0, 1));
		
		textArea = new JTextArea();
		textArea.setTabSize(4);
		textArea.setFont(Medium.f);
		textArea.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2 && !e.isConsumed()) {
                    e.consume();
                    System.out.println("Double Click");
				}
			}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}
		});
		
		resetLayout();
	}
	public void resetLayout() {
		removeAll();
		setLayout(new MigLayout());
		
		JPanel attributePanel = new JPanel();
		attributePanel.setLayout(new GridLayout(0, 2));
		attributePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		attributePanel.add(labelPanel);
		attributePanel.add(fieldPanel);
		
		JScrollPane textPanel = createScrollPane(textArea);
		textPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		JButton saveButton = createJButton("Save");
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(selected != null) {
					setData(selected);
					DefaultMutableTreeNode node = getNode(selected);
					DefaultMutableTreeNode parent = node;
					TranscendenceMod mod = null;
					while(parent != null && mod == null) {
						DesignElementOld parentElement = (DesignElementOld) parent.getUserObject();
						if(parentElement instanceof TranscendenceMod) {
							mod = (TranscendenceMod) parentElement;
							mod.save();
							break;
						} else {
							parent = (DefaultMutableTreeNode) node.getParent();
						}
					}
				}
			}
		});
		
		JButton xmlButton = new JButton("Generate XML");
		xmlButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(selected != null) {
					setData(selected);
					JTextArea ta = new JTextArea(selected.getXMLOutput());
					ta.setFont(Medium.f);
					ta.setTabSize(4);
					ta.setEditable(false);
					JScrollPane pane = createScrollPane(ta);
					pane.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
					JOptionPane.showMessageDialog(frame, pane);
				}
			}
		});
		xmlButton.setFont(Large.f);
		xmlButton.setAlignmentX(LEFT_ALIGNMENT);
		
		int yPercent = 0;
		for(Extensions extension : Extensions.values()) {
			JButton b = createJButton("New " + extension.name());
			b.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					if(e.getSource() == b) {
						TranscendenceMod mod = extension.get();
						mod.setPath(new File(JOptionPane.showInputDialog("Specify file path")));
						DefaultMutableTreeNode node = new DefaultMutableTreeNode(mod);
						elementTreeModel.insertNodeInto(node, origin, 0);
						elementTree.setSelectionPath(new TreePath(elementTreeModel.getPathToRoot(node)));
					}
				}
				
			});
			add(b, new CC().x("0%").y(yPercent + "%").width("25%").height("3%"));
			yPercent += 3;
		}
		
		JButton deleteButton = createJButton("Delete Element");
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(arg0.getSource() == deleteButton && selected != null) {
					DefaultMutableTreeNode node = getNode(selected),
							parent = (DefaultMutableTreeNode) node.getParent();
					if(parent == null) {
						selected = null;
					} else {
						DesignElementOld parentElement = (DesignElementOld) parent.getUserObject();
						parentElement.getSubElements().remove(selected);
						if(parent.getChildCount() > 1) {
							selectElement((DesignElementOld) ((DefaultMutableTreeNode) parent.getChildAt(1)).getUserObject());
						} else if(parent.getUserObject() instanceof DesignElementOld) {
							selectElement((DesignElementOld) parent.getUserObject());
						}
					}
					elementTreeModel.removeNodeFromParent(node);
				}
			}
			
		});
		
		add(elementTreePane,
				new CC()
				.x("0%")
				.y("15%")
				.minWidth("25%").maxWidth("25%")
				.minHeight("75%").maxHeight("75%")
				);
		add(xmlButton,
				new CC()
				.x("0%")
				.y("90%")
				.minWidth("25%").maxWidth("25%")
				.minHeight("5%").maxHeight("5%")
				);
		add(saveButton,
				new CC()
				.x("0%")
				.y("95%")
				.minWidth("25%").maxWidth("25%")
				.minHeight("5%").maxHeight("5%")
				);
		add(createScrollPane(nameField), new CC()
				.x("25%")
				.y("0%")
				.minWidth("50%").maxWidth("50%")
				.minHeight("5%").maxHeight("5%")
				);
		add(deleteButton, new CC()
				.x("90%")
				.y("0%")
				.width("10%")
				.height("5%")
				);
		add(createScrollPane(documentation), new CC()
				.x("25%")
				.y("10%")
				.minWidth("75%").maxWidth("75%")
				.minHeight("15%").maxHeight("15%")
				);
		add(createScrollPane(attributePanel),
				new CC()
				.x("25%")
				.y("25%")
				.minWidth("55%").maxWidth("55%")
				.maxHeight("45%")
				);
		add(createScrollPane(subElementPanel),
				new CC()
				.x("80%")
				.y("25%")
				.minWidth("20%").maxWidth("20%")
				.maxHeight("45%")
				);
		textPanel.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent arg0) {
				if(arg0.getSource() == textPanel) {
					System.out.println("Clicked");
				}
			}
			public void mouseEntered(MouseEvent arg0) {}
			public void mouseExited(MouseEvent arg0) {}
			public void mousePressed(MouseEvent arg0) {}
			public void mouseReleased(MouseEvent arg0) {}
		});
		add(textPanel,
				new CC()
				.x("25%")
				.y("70%")
				.minWidth("75%").maxWidth("75%")
				.minHeight("30%").maxHeight("80%")
				);
	}
	public void selectElement(DesignElementOld e) {
		System.out.println("Initialize from element: " + e.getName());
		Dimension size = getSize();
		selected = e;
		setPreferredSize(size);
		resetLayout();
		e.initializeFrame(this);
		frame.pack();
		repaint();
	}
	public void removeSelf() {
		frame.remove(this);
	}
	public void setData(DesignElementOld e) {
		
		e.setName(nameField.getText());
		List<DesignAttribute> attributes = e.getAttributes();
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
		e.setText(textArea.getText());
		updateTreeText(e);
	}
	public void updateTreeText(DesignElementOld se) {
		elementTreeModel.nodeChanged(getNode(se));
	}
	
	public DefaultMutableTreeNode getNode(DesignElementOld element)
	{
		DefaultMutableTreeNode theNode = null;
		for (Enumeration<DefaultMutableTreeNode> e = (Enumeration<DefaultMutableTreeNode>) ((DefaultMutableTreeNode) elementTreeModel.getRoot()).depthFirstEnumeration(); e.hasMoreElements() && theNode == null;) {
		    DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.nextElement();
		    if (node.getUserObject().equals(element)) {
		        theNode = node;
		        break;
		    }
		}
		if(theNode == null) {
			System.out.println("Could not find node");
		} else {
			System.out.println("Found Node: " + theNode.toString());
		}
		return theNode;
	}
	public void expandTreeNodes() {
		for(int i = 0; i < elementTree.getRowCount(); i++){
	        elementTree.expandRow(i);
	    }
	}
	public void addElement(DesignElementOld se)
	{
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(se), parent = (DefaultMutableTreeNode) elementTree.getLastSelectedPathComponent();
		elementTreeModel.insertNodeInto(node, parent, 0);
	    int rowIndex = parent.getIndex(node);
	    elementTree.expandRow(rowIndex);
	    System.out.println("Tree Path (Create): " + new TreePath(elementTreeModel.getPathToRoot(node)));
	    elementTree.setSelectionPath(new TreePath(elementTreeModel.getPathToRoot(node)));
	}
	public static JScrollPane createScrollPane(JComponent c) {
		JScrollPane result = new JScrollPane(c, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		result.getVerticalScrollBar().setUnitIncrement(16);
		return result;
	}
	
	public static JLabel createLabel(String text) {
		JLabel result = new JLabel(text);
		result.setFont(Medium.f);
		return result;
	}
	public static JTextField createTextField(String text, boolean editable) {
		JTextField result = new JTextField(text);
		result.setFont(Medium.f);
		result.setEditable(editable);
		return result;
	}
	public static JTextArea createTextArea(String text, boolean editable) {
		JTextArea result = new JTextArea(text);
		result.setFont(Medium.f);
		result.setEditable(editable);
		return result;
	}
	public static JButton createJButton(String text) {
		JButton result = new JButton(text);
		result.setFont(Medium.f);
		return result;
	}
}
