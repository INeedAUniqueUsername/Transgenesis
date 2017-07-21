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
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
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
import window.Frame;
import window.Loader;
import window.Window;
import window.Window.Fonts;
import xml.DesignAttribute;
import xml.DesignElement;
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
	
	public static boolean showErrors = false;
	
	//private final List<TranscendenceMod> mods;
	private static DesignElement selected;
	private static TranscendenceMod selectedExtension;
	
	public final JTextField nameField;
	public final JLabel documentation;
	public final JPanel labelPanel;
	public final JPanel fieldPanel;
	public final JPanel subElementPanel;
	public final JTextArea textArea;
	private static DefaultMutableTreeNode origin;
	private boolean saved;
	//JPanel subelementPanel;
	public XMLPanel(Frame frame) {
		this.frame = frame;
		JOptionPane.showMessageDialog(this, createTextArea(
				"TransGenesis: Transcendence XML Editor\n" +
				"By 0xABCDEF/Archcannon\n\n" +
				"Notes\n" +
				"-Please use an isolated copy of the source code in case of unknown bugs.\n" +
				"-Select a File or Folder to load extensions.\n" +
				"-Loading may take a while depending on how many files you are loading.\n" +
				"-In order to load successfully, files must contain well-formed XML code.\n" +
				"-If an extension has unloaded dependencies or modules, then not all of its\n" +
				" internal or external types will be recognized by TransGenesis. For optimal\n" +
				" functionality, please have all dependencies and modules loaded.\n"
				, false));
		//String dir = "C:\\Users\\Alex\\Desktop\\Transcendence Multiverse\\ParseTest\\Test.xml";
		//String dir = "C:\\Users\\Alex\\Desktop\\Transcendence Multiverse\\TransGenesis Test";
		JFileChooser j = new JFileChooser();
		j.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		j.setCurrentDirectory(new File(System.getProperty("user.dir")));
		File f = null;
		if(j.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			//String dir = JOptionPane.showInputDialog("Specify mod directory");
			f = j.getSelectedFile();
		} else {
			f = new File("");
		}
		//origin = new DefaultMutableTreeNode(f.getAbsolutePath());
		origin = new DefaultMutableTreeNode("TransGenesis");
		/*
		mods = Loader.loadAllMods(f);
		for(TranscendenceMod tm : mods) {
			if(tm == null) {
				System.out.println("Null extension found");
			} else {
				origin.add(tm.toTreeNode());
			}
		}
		*/
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
			    Object obj = ((DefaultMutableTreeNode) value).getUserObject();
			    if(obj instanceof DesignElement) {
			    	DesignElement element = (DesignElement) obj;
			    }
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
	    		selectNode(node);
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
		loadExtensions(f, false);
		elementTree.expandRow(0);
		bindNonModuleExtensions(getExtensions());
		resetLayout();
	}
	public static List<TranscendenceMod> getExtensions() {
		List<TranscendenceMod> result = new LinkedList<>();
		for(int i = 0; i < origin.getChildCount(); i++) {
			result.add((TranscendenceMod) ((DefaultMutableTreeNode) origin.getChildAt(i)).getUserObject()); 
		}
		return result;
	}
	public TranscendenceMod getExtension(DefaultMutableTreeNode node) {
		//It's possible that the selected extension is in the node itself
		DefaultMutableTreeNode parent = node;
		while(parent != null) {
			System.out.println("Looking for parent Extension");
			Object parentObj = parent.getUserObject();
			if(parentObj instanceof TranscendenceMod) {
				System.out.println("Found parent Extension");
				return (TranscendenceMod) parentObj;
			} else {
				parent = (DefaultMutableTreeNode) parent.getParent();
			}
		}
		System.out.println();
		return null;
	}
	//Get all the Types defined by the selected extension
	public static Map<String, DesignElement> getExtensionTypeMap() {
		if(selectedExtension != null) {
			//This only updates when save is pressed
			//selectedExtension.updateTypeBindings();
			return selectedExtension.getTypeMap();
		}
		return new TreeMap<String, DesignElement>();
	}
	/*
	public static Map<String, DesignElementOld> getExtensionExternalTypes() {
		
	}
	*/
	public void resetLayout() {
		removeAll();
		setLayout(new MigLayout());
		
		//nameField.setText("Element name");
		nameField.setEditable(false);
		labelPanel.removeAll();
		//labelPanel.add(createLabel("Attribute name"));
		fieldPanel.removeAll();
		//fieldPanel.add(createLabel("Attribute value"));
		subElementPanel.removeAll();
		//subElementPanel.add(createLabel("Subelements"));
		//textArea.setText("Element text");
		textArea.setEditable(false);
		
		
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
				save();
			}
		});
		
		JButton xmlButton = new JButton("Generate XML");
		xmlButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(selected != null) {
					save();
					JTextArea ta = new JTextArea(selected.getXMLOutput());
					ta.setFont(Medium.f);
					ta.setTabSize(4);
					ta.setEditable(false);
					JScrollPane pane = createScrollPane(ta);
					pane.setPreferredSize(getSize());
					JOptionPane.showMessageDialog(frame, pane);
				}
			}
		});
		xmlButton.setFont(Large.f);
		xmlButton.setAlignmentX(LEFT_ALIGNMENT);
		
		JButton loadExtension = createJButton("Load Extension/Folder");
		loadExtension.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() == loadExtension) {
					if(selected != null) {
						setData(selected);
					}
					JFileChooser j = new JFileChooser();
					j.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
					j.setCurrentDirectory(new File(System.getProperty("user.dir")));
					if(j.showOpenDialog(XMLPanel.this) == JFileChooser.APPROVE_OPTION) {
						loadExtensions(j.getSelectedFile(), true);
					}
				}
			}
		});
		add(loadExtension, new CC().x("0%").y("0%").width("25%").height("3%"));
		
		int yPercent = 3;
		for(Extensions extension : new Extensions[] {
				Extensions.TranscendenceAdventure,
				Extensions.TranscendenceExtension,
				Extensions.TranscendenceLibrary,
				Extensions.TranscendenceModule}) {
			JButton b = createJButton("New " + extension.name());
			b.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					if(e.getSource() == b) {
						if(selected != null) {
							setData(selected);
						}
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
						selectedExtension = null;
					} else {
						DesignElement parentElement = (DesignElement) parent.getUserObject();
						parentElement.getSubElements().remove(selected);
						if(parent.getChildCount() > 1) {
							selectNode(((DefaultMutableTreeNode) parent.getChildAt(1)));
						} else if(parent.getUserObject() instanceof DesignElement) {
							selectNode(parent);
						}
					}
					elementTreeModel.removeNodeFromParent(node);
				}
			}
		});
		deleteButton.setEnabled(!(selected == null));
		
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
				.minHeight("30%").maxHeight("30%")
				);
	}
	public static void showWarningPane(String warnings) {
		if(!showErrors) {
			return;
		}
		if(warnings.split("\n").length < 2) {
			JOptionPane.showMessageDialog(null, XMLPanel.createTextArea(warnings + "\nNo Warnings", false));
		} else {
			JOptionPane.showMessageDialog(null, XMLPanel.createTextArea(warnings, false));
		}
	}
	private void loadExtensions(File f, boolean bindTypesWhenDone) {
		List<TranscendenceMod> loaded = getExtensions();
		List<TranscendenceMod> mods = Loader.loadAllMods(f);
		String message = "Loading Extensions from " + f.getAbsolutePath();
		for(TranscendenceMod m : mods) {
			if(loaded.contains(m)) {
				message += ("\nNote: Extension " + m.getPath() + " already loaded");
			} else if(m == null || m.getName().equals("TranscendenceError")) {
				message += ("\nFailure: Extension " + m.getPath() + " could not be loaded");
			} else {
				message += ("\nSuccess: Extension " + m.getPath() + " loaded");
				elementTreeModel.insertNodeInto(m.toTreeNode(), origin, 0);
				/*
				if(m.hasSubElement("Library") && JOptionPane.showConfirmDialog(null, "This Extension depends on other Libraries. Load them now?", "Load Dependencies", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
				}
				*/
				//If the file is not the same as the Extension path, then we can assume that the file is the directory that contains the Extension. We can assume that an Extension and its modules have the same directory.
				if(m.hasSubElement("Module") && f.equals(m.getPath()) && JOptionPane.showConfirmDialog(null, createLabel("Note: Extension " + m.getPath() + " has Modules. Load them now?"), "Load Modules", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					List<File> paths = new LinkedList<>();
					for(DesignElement e : m.getSubElementsByName("Module")) {
						paths.add(new File(m.getModulePath(e.getAttributeByName("filename").getValue())));
					}
					loadModules(paths);
				}
			}
		}
		if(bindTypesWhenDone) {
			boolean showErrorsPrevious = showErrors;
			showErrors = false;
			bindNonModuleExtensions(getExtensions());
			showErrors = showErrorsPrevious;
		}
		
		JOptionPane.showMessageDialog(this, createScrollPane(createTextArea(message, false)));
	}
	private void loadModules(List<File> files) {
		List<TranscendenceMod> loaded = getExtensions();
		List<TranscendenceMod> mods = new LinkedList<>();
		String message = "";
		for(File f : files) {
			mods.add(Loader.processMod(f));
			message += "\nLoading Module from " + f.getAbsolutePath();
		}
		
		for(TranscendenceMod m : mods) {
			if(loaded.contains(m)) {
				message += ("\nNote: Module " + m.getPath() + " already loaded");
			} else if(m == null || m.getName().equals("TranscendenceError")) {
				message += ("\nFailure: Module " + m.getPath() + " could not be loaded");
			} else {
				message += ("\nSuccess: Module " + m.getPath() + " loaded");
				elementTreeModel.insertNodeInto(m.toTreeNode(), origin, 0);
			}
		}
		JOptionPane.showMessageDialog(this, createScrollPane(createTextArea(message, false)));
	}
	public void bindNonModuleExtensions(List<TranscendenceMod> mods) {
		for(TranscendenceMod mod : mods) {
			//Do not bind on TranscendenceModules because we will have their parents bind for them
			if(!mod.getName().equals("TranscendenceModule")) {
				mod.updateTypeBindings();
			}
		}
	}
	public boolean directoryContainsFile(File directory, File f) {
		File parent = f;
		while(parent != null) {
			if(parent.equals(f)) {
				return true;
			}
			parent = parent.getParentFile();
		}
		return false;
	}
	private void save() {
		boolean previous = showErrors;
		showErrors = false;
		if(selected != null) {
			setData(selected);
		}
		if(selectedExtension != null) {
			selectedExtension.updateTypeBindings();
			selectedExtension.save();
		}
		showErrors = previous;
		saved = true;
	}
	public static DesignElement getSelected() {
		return selected;
	}
	public void selectNode(DefaultMutableTreeNode node) {
		Object obj = node.getUserObject();
		if(obj instanceof DesignElement) {
			selectElement((DesignElement) obj);
			selectedExtension = getExtension(node);
		}
		else {
			selectElement(null);
			selectedExtension = null;
		}
	}
	public void selectElement(DesignElement e) {
		Dimension size = getSize();
		selected = e;
		setPreferredSize(size);
		resetLayout();
		if(e != null) {
			System.out.println("Initialize from element: " + e.getName());
			showErrors = false;
			e.initializeFrame(this);
			showErrors = true;
		}
		
		frame.pack();
		repaint();
	}
	public void removeSelf() {
		frame.remove(this);
	}
	public void setData(DesignElement e) {
		saved = false;
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
	public void updateTreeText(DesignElement se) {
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
	public void addElement(DesignElement se)
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
		result.setMaximumSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		result.getVerticalScrollBar().setUnitIncrement(16);
		return result;
	}
	/*
	public static JScrollPane createScrollPane(JComponent c, boolean vertical, boolean horizontal) {
		JScrollPane result = new JScrollPane(c,
				vertical ? JScrollPane.VERTICAL_SCROLLBAR_ALWAYS : JScrollPane.VERTICAL_SCROLLBAR_NEVER,
				horizontal ? JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS : JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
				);
		if(!horizontal) {
			c.setMaximumSize(new Dimension(result.getMaximumSize().width, c.getMaximumSize().height));
		}
		result.setMaximumSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		result.getVerticalScrollBar().setUnitIncrement(16);
		return result;
	}
	*/
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
