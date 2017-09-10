package panels;
import static java.lang.System.*;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
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
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import designType.subElements.ElementType;
import elementSearch.IElementCriterion;
import elementSearch.PartialNameCriterion;
import elementSearch.PartialUNIDCriterion;
import mod.ExtensionFactory.Extensions;
import mod.TranscendenceMod;
import net.miginfocom.layout.CC;
import net.miginfocom.swing.MigLayout;
import window.Loader;
import window.Window;
import window.Window.Fonts;
import xml.DesignAttribute;
import xml.DesignElement;
import static window.Window.Fonts.*;
public class XMLPanel extends JPanel {
	public static XMLPanel instance;
	
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
	private final DefaultMutableTreeNode elementTreeOrigin;
	private final JScrollPane elementTreePane;
	
	//private final DefaultTreeModel searchTreeModel;
	//private final JTree searchTree;
	private final DefaultMutableTreeNode searchTreeOrigin;
	//private final JScrollPane searchTreePane;
	
	//private final List<TranscendenceMod> mods;
	private DesignElement selected;
	private TranscendenceMod selectedExtension;
	
	public final JTextField nameField;
	public final JTextField extensionField;
	public final JPanel actionsPanel;
	public final JLabel documentation;
	public final JPanel labelPanel;
	public final JPanel fieldPanel;
	public final JPanel subElementPanel;
	public final JTextArea textArea;
	
	public final JTextField searchBar;
	public final JButton bindButton, saveButton, xmlButton;
	//JPanel subelementPanel;
	public XMLPanel(JFrame frame) {
		instance = this;
		System.out.println("TransGenesis Running");
		this.frame = frame;
		elementTreeOrigin = new DefaultMutableTreeNode("TransGenesis");
		DefaultTreeCellRenderer elementTreeCellRenderer = new DefaultTreeCellRenderer() {
			final Color defaultBackgroundSelectionColor = getBackgroundSelectionColor();
			final Color defaultBackgroundNonSelectionColor = getBackgroundNonSelectionColor();
			final Color defaultTextSelectionColor = getTextSelectionColor();
			final Color defaultTextNonSelectionColor = getTextNonSelectionColor();
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
				/*
				Object obj = ((DefaultMutableTreeNode) value).getUserObject();
				if(obj instanceof TranscendenceMod) {
					boolean unbound = ((TranscendenceMod) obj).isUnbound();
					boolean unsaved = ((TranscendenceMod) obj).isUnsaved();
					this.setBackgroundNonSelectionColor(
							unbound && unsaved ? new Color(255, 128, 0) :
							unbound ? new Color(255, 255, 0) :
							unsaved ? new Color(255, 0, 0) :
							defaultBackgroundNonSelectionColor
							);
				} else {
					this.setBackgroundNonSelectionColor(defaultBackgroundNonSelectionColor);
				}
				*/
				return this;
			}
		};
		
		elementTreeModel = new DefaultTreeModel(elementTreeOrigin);
		elementTree = new JTree(elementTreeModel);
		elementTree.getSelectionModel().setSelectionMode
			(TreeSelectionModel.SINGLE_TREE_SELECTION);
		elementTree.setAlignmentX(Component.CENTER_ALIGNMENT);
		elementTree.setFont(Fonts.Medium.f);
		elementTree.setShowsRootHandles(true);
		elementTree.setCellRenderer(elementTreeCellRenderer);
		elementTree.expandRow(0);
		elementTree.setSelectionPath(new TreePath(elementTreeOrigin));
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
		
		searchBar = createTextField("", true);
		searchBar.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void keyTyped(KeyEvent arg0) {
				SwingUtilities.invokeLater(() -> {
					String query = searchBar.getText();
					if(query.isEmpty()) {
						System.out.println("Exiting Search");
						elementTreeModel.setRoot(elementTreeOrigin);
					} else {
						//Switch to Search Mode and find matching elements
						IElementCriterion criterion = IElementCriterion.parseQuery(query);
						System.out.println("Entering Search");
						searchTreeOrigin.removeAllChildren();
						for(TranscendenceMod e : getExtensions()) {
							MutableTreeNode result = e.createSearchTree(criterion);
							if(result != null) {
								searchTreeOrigin.add(result);
							}
						}
						elementTreeModel.setRoot(searchTreeOrigin);
						
						System.out.println("Expanding Result Paths");
						//Expand tree nodes all the way up to before matching elements
						Enumeration<DefaultMutableTreeNode> e = searchTreeOrigin.depthFirstEnumeration();
						for(DefaultMutableTreeNode n = e.nextElement(); e.hasMoreElements(); n = e.nextElement()) {
							Object obj = n.getUserObject();
							if(obj instanceof DesignElement && criterion.elementMatches((DesignElement) obj)) {
								System.out.println("Expanding Result Path");
								elementTree.expandPath(new TreePath(n.getPath()).getParentPath());
							}
						}
					}
					searchBar.requestFocus();
				});
			}
			
		});
		
		searchTreeOrigin = new DefaultMutableTreeNode("Element Search");
		/*
		searchTreeModel = new DefaultTreeModel(searchTreeOrigin);
		searchTree = new JTree(searchTreeModel);
		searchTree.getSelectionModel().setSelectionMode
			(TreeSelectionModel.SINGLE_TREE_SELECTION);
		searchTree.setAlignmentX(Component.CENTER_ALIGNMENT);
		searchTree.setFont(Fonts.Medium.f);
		searchTree.setShowsRootHandles(true);
		searchTree.setCellRenderer(elementTreeCellRenderer);
		searchTree.expandRow(0);
		searchTree.setSelectionPath(new TreePath(searchTreeOrigin));
		searchTree.addTreeSelectionListener(new TreeSelectionListener() {
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
		searchTreePane = createScrollPane(searchTree);
		*/
		bindButton = createJButton("Bind Extension");
		bindButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				bind();
			}
		});
		bindButton.setFont(Fonts.Large.f);
		bindButton.setHorizontalAlignment(SwingConstants.LEFT);
		
		saveButton = createJButton("Save Extension");
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				save();
			}
		});
		saveButton.setFont(Fonts.Large.f);
		saveButton.setHorizontalAlignment(SwingConstants.LEFT);
		
		xmlButton = new JButton("Generate XML");
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
					pane.setMaximumSize(XMLPanel.this.getSize());
					JOptionPane.showMessageDialog(frame, pane);
					/*
					frame.remove(XMLPanel.this);
					JPanel panel = new JPanel() {{
						setMaximumSize(XMLPanel.this.getSize());
						add(pane);
						add(new JButton("Done") {{
							addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent arg0) {
									frame.removeAll();
									frame.add(XMLPanel.this);
									frame.pack();
								}
							});
						}});
					}};
					frame.add(panel);
					frame.pack();
					*/
				}
			}
		});
		xmlButton.setHorizontalAlignment(SwingConstants.LEFT);
		xmlButton.setFont(Large.f);
		xmlButton.setAlignmentX(LEFT_ALIGNMENT);
		
		
		
		
		
		nameField = createTextField("Transgenesis", false);
		nameField.setFont(Large.f);
		
		extensionField = createTextField(System.getProperty("user.dir"), false);
		
		actionsPanel = new JPanel();
		actionsPanel.setLayout(new BoxLayout(actionsPanel, BoxLayout.X_AXIS));
		
		
		documentation = new JLabel("Documentation.txt is unavailable.");
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
				/*
				if (e.getClickCount() == 2 && !e.isConsumed()) {
					e.consume();
					out.println("Double Click");
				}
				*/
			}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}
		});
	}
	public void initialize(String... args) {
		if(args.length == 0) {
			JLabel wait = new JLabel("TransGenesis is initializing");
			wait.setFont(new Font("Consolas", Font.PLAIN, 72));
			wait.setHorizontalAlignment(SwingConstants.CENTER);
			wait.setAlignmentX(JComponent.CENTER_ALIGNMENT);
			add(wait);
			packFrame();
			
			JOptionPane.showMessageDialog(XMLPanel.this, createTextArea(
					"TransGenesis: Transcendence XML Editor\n" +
					"By 0xABCDEF/Archcannon\n\n" +
					"Notes\n" +
					"-Press Enter to close Dialogs such as this one.\n" +
					"-Please use an isolated copy of the Transcendence source code in case of unknown bugs.\n" +
					"-Select a File or Folder to load extensions.\n" +
					"-Loading may take a while depending on how many files you are loading.\n" +
					"-In order to load successfully, files must have the .xml extension and contain\n" +
					" well-formed XML code.\n" +
					"-If an extension has unloaded dependencies or modules, then not all of its\n" +
					" internal or external types will be recognized by TransGenesis. For optimal\n" +
					" functionality, please have all dependencies and modules loaded.\n" +
					"-Design definitions are incomplete, so TransGenesis may not recognize all elements,\n" +
					" subelements, or attributes.\n" +
					"-Data about extension Type Entries/Ranges is stored as metadata in a .dat file."
					, false));
			
			File[] load = showFileChooser();
			
			if(load.length > 0) {
				for(File f : load) {
					loadExtensions(f, false);
				}
				JOptionPane.showMessageDialog(XMLPanel.this, createTextArea("TransGenesis will now prepare type bindings for all loaded extensions.", false));
			} else {
				JOptionPane.showMessageDialog(XMLPanel.this, createTextArea("TransGenesis will begin without any extensions loaded.", false));
			}
		} else {
			for(String arg : args) {
				String argName = arg.split(":")[0];
				if(argName.equals("path")) {
					loadExtensions(new File(arg.substring(argName.length() + 1)), false);
				}
			}
		}
		onInitializationComplete();
	}
	public void onInitializationComplete() {
		//Bind the extensions twice because some extensions have unbound dependencies when they bind for the first time
		bindNonModuleExtensions(getExtensions());
		bindNonModuleExtensions(getExtensions());
		
		elementTree.expandRow(0);
		resetLayout();
		packFrame();
		JOptionPane.showMessageDialog(XMLPanel.this, createTextArea("Initialization complete", false));
		requestFocus();
	}
	public void packFrame() {
		Dimension size = getSize();
		setPreferredSize(size);
		frame.pack();
		repaint();
	}
	public static XMLPanel getInstance() {
		return instance;
	}
	//https://coderanch.com/t/342116/java/set-font-JFileChooser
	public static void setComponentsFont(Component[] comp, Font font) {
		for(int x = 0; x < comp.length; x++) {
			if(comp[x] instanceof Container) setComponentsFont(((Container)comp[x]).getComponents(), font);
			try{comp[x].setFont(font);}
			catch(Exception e){}//do nothing
		}
	}
	public List<TranscendenceMod> getExtensions() {
		List<TranscendenceMod> result = new LinkedList<>();
		for(int i = 0; i < elementTreeOrigin.getChildCount(); i++) {
			result.add((TranscendenceMod) ((DefaultMutableTreeNode) elementTreeOrigin.getChildAt(i)).getUserObject()); 
		}
		return result;
	}
	public TranscendenceMod getExtension(DefaultMutableTreeNode node) {
		//It's possible that the selected extension is in the node itself
		DefaultMutableTreeNode parent = node;
		while(parent != null) {
			out.println("Looking for parent Extension");
			Object parentObj = parent.getUserObject();
			if(parentObj instanceof TranscendenceMod) {
				out.println("Found parent Extension");
				return (TranscendenceMod) parentObj;
			} else {
				parent = (DefaultMutableTreeNode) parent.getParent();
			}
		}
		out.println();
		return null;
	}
	//Get all the Types defined by the selected extension
	public Map<String, DesignElement> getExtensionTypeMap() {
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
		
		nameField.setText("TransGenesis");
		nameField.setEditable(false);
		extensionField.setText(System.getProperty("user.dir"));
		extensionField.setEditable(false);
		actionsPanel.removeAll();
		labelPanel.removeAll();
		fieldPanel.removeAll();
		subElementPanel.removeAll();
		textArea.setText("");
		textArea.setEditable(false);
		bindButton.setEnabled(false);
		saveButton.setEnabled(false);
		xmlButton.setEnabled(false);
		
		JPanel attributePanel = new JPanel();
		attributePanel.setLayout(new GridLayout(0, 2));
		attributePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		attributePanel.add(labelPanel);
		attributePanel.add(fieldPanel);
		
		JPanel combinedPanel = new JPanel();
		combinedPanel.setLayout(new MigLayout());
		combinedPanel.add(createScrollPane(attributePanel), new CC().width("75%").alignY("top"));
		combinedPanel.add(createScrollPane(subElementPanel), new CC().width("25%").alignY("top"));
		
		JScrollPane textPanel = createScrollPane(textArea);
		textPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		textPanel.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent arg0) {
				if(arg0.getSource() == textPanel) {
					out.println("Clicked");
				}
			}
			public void mouseEntered(MouseEvent arg0) {}
			public void mouseExited(MouseEvent arg0) {}
			public void mousePressed(MouseEvent arg0) {}
			public void mouseReleased(MouseEvent arg0) {}
		});
		
		
		JPanel extensionButtons = new JPanel();
		extensionButtons.setLayout(new GridLayout(5, 1));
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
						String path = JOptionPane.showInputDialog(createTextArea("Specify file path", false));
						if(path == null) {
							return;
						}
						try {
							File f = new File(path);
							if(f.isFile()) {
								JOptionPane.showMessageDialog(XMLPanel.this, createTextArea("[Warning] Overwriting existing file  " + path, false));
							}
							else if(!f.createNewFile()) {
								JOptionPane.showMessageDialog(XMLPanel.this, createTextArea("[Failure] Unable to create file " + path, false));
							}
							
							mod.setPath(f);
							DefaultMutableTreeNode node = new DefaultMutableTreeNode(mod);
							elementTreeModel.insertNodeInto(node, elementTreeOrigin, 0);
							elementTree.setSelectionPath(new TreePath(elementTreeModel.getPathToRoot(node)));
						}catch(Exception ex) {
							JOptionPane.showMessageDialog(XMLPanel.this, createTextArea("[Failure] Invalid file path " + path, false));
						}
					}
				}
			});
			b.setHorizontalAlignment(SwingConstants.LEFT);
			extensionButtons.add(b);
		}
		JButton loadExtension = createJButton("Load Extension/Folder");
		loadExtension.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() == loadExtension) {
					if(selected != null) {
						setData(selected);
					}
					File[] load = showFileChooser();
					for(File f : load) {
						loadExtensions(f, true);
					}
				}
			}
		});
		loadExtension.setHorizontalAlignment(SwingConstants.LEFT);
		extensionButtons.add(loadExtension);
		
		//25% width, 100% height
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new MigLayout());
		leftPanel.add(extensionButtons, new CC()
				.width("100%")
				.wrap());
		leftPanel.add(searchBar, new CC()
				.width("100%")
				.wrap());
		leftPanel.add(elementTreePane, new CC()
				.width("100%")
				.height("100%")
				.wrap());
		leftPanel.add(bindButton, new CC()
				.width("100%")
				.wrap()
				);
		leftPanel.add(saveButton, new CC()
				.width("100%")
				.wrap());
		leftPanel.add(xmlButton, new CC()
				.width("100%")
				.wrap());
		add(leftPanel, new CC().width("30%").height("100%"));
		
		//75% width, 100% height
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new MigLayout());
		rightPanel.add(createScrollPane(nameField), new CC()
				.width("100%")
				.height("5%")
				.wrap()
				);
		rightPanel.add(extensionField, new CC()
				.width("100%")
				.height("5%")
				.wrap()
				);
		rightPanel.add(actionsPanel, new CC()
				.width("100%")
				.height("5%")
				.wrap());
		
		rightPanel.add(createScrollPane(documentation), new CC()
				.width("100%")
				.height("15%")
				.wrap());
		rightPanel.add(combinedPanel, new CC().width("100%").height("45%").wrap());
		rightPanel.add(textPanel, new CC()
				.width("100%")
				.height("25%")
				.wrap());
		add(rightPanel, new CC().width("70%").height("100%"));
		
		
		/*
		add(extensionButtons,
				new CC()
				.x("0%")
				.y("0")
				.minWidth("25%")
				.maxWidth("25%")
				.minHeight("15%")
				.maxHeight("15%")
				);
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
		add(textPanel,
				new CC()
				.x("25%")
				.y("70%")
				.minWidth("75%").maxWidth("75%")
				.minHeight("30%").maxHeight("30%")
				);
		*/
	}
	public File[] showFileChooser() {
		JFileChooser j = new JFileChooser();
		j.setFileFilter(new FileNameExtensionFilter("XML files", "xml"));
		j.setMultiSelectionEnabled(true);
		if(getWidth() == 0 || getHeight() == 0) {
			j.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT - 48));
		} else {
			j.setPreferredSize(getSize());
		}
		
		j.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		setComponentsFont(j.getComponents(), Fonts.Medium.f);
		j.setCurrentDirectory(new File(System.getProperty("user.dir")));
		if(j.showOpenDialog(XMLPanel.this) == JFileChooser.APPROVE_OPTION) {
			return j.getSelectedFiles();
		}
		return new File[0];
	}
	private void loadExtensions(File f, boolean bindTypesWhenDone) {
		List<TranscendenceMod> loaded = getExtensions();
		out.println("Loading Extensions from " + f.getAbsolutePath());
		List<TranscendenceMod> mods = Loader.loadAllMods(f);
		out.println("Processing loaded Extensions");
		for(TranscendenceMod m : mods) {
			if(loaded.contains(m)) {
				out.println("Warning: Extension " + m.getPath() + " already loaded.");
			} else if(m == null) {
				out.println("Unknown error");
			} else if(m.getName().equals("TranscendenceError")) {
				out.println("Failure: Extension " + m.getPath() + " could not be loaded.");
			} else {
				out.println("Success: Extension " + m.getPath() + " loaded.");
				elementTreeModel.insertNodeInto(m.toTreeNode(), elementTreeOrigin, 0);
				/*
				if(m.hasSubElement("Library") && JOptionPane.showConfirmDialog(null, "This Extension depends on other Libraries. Load them now?", "Load Dependencies", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
				}
				*/
				//If the file is not the same as the Extension path, then we can assume that the file is the directory that contains the Extension. We can assume that an Extension and its modules have the same directory.
				if(m.hasSubElement("Module") && f.equals(m.getPath()) && JOptionPane.showConfirmDialog(null, createTextArea("Note: Extension " + m.getPath() + " has Modules. Load them now?", false), "Load Modules", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					List<File> paths = new LinkedList<>();
					for(DesignElement e : m.getSubElementsByName("Module")) {
						paths.add(new File(m.getModulePath(e.getAttributeByName("filename").getValue())));
					}
					loadModules(paths);
				}
			}
		}
		if(bindTypesWhenDone) {
			JOptionPane.showMessageDialog(this, createScrollPane(createTextArea("TransGenesis will now update type bindings for all loaded extensions.", false)));
			bindNonModuleExtensions(getExtensions());
		}
	}
	private void loadModules(List<File> files) {
		List<TranscendenceMod> loaded = getExtensions();
		List<TranscendenceMod> mods = new LinkedList<>();
		String message = "";
		for(File f : files) {
			mods.add(Loader.processMod(f));
			out.println("Loading Module from " + f.getAbsolutePath());
		}
		
		for(TranscendenceMod m : mods) {
			if(loaded.contains(m)) {
				out.println("Warning: Module " + m.getPath() + " already loaded");
			} else if(m == null || m.getName().equals("TranscendenceError")) {
				out.println("Failure: Module " + m.getPath() + " could not be loaded");
			} else {
				out.println("Success: Module " + m.getPath() + " loaded");
				elementTreeModel.insertNodeInto(m.toTreeNode(), elementTreeOrigin, 0);
			}
		}
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
	private void bind() {
		if(selected != null) {
			setData(selected);
		}
		if(selectedExtension != null) {
			selectedExtension.updateTypeBindings();
		}
	}
	private void save() {
		if(selected != null) {
			setData(selected);
		}
		if(selectedExtension != null) {
			selectedExtension.updateTypeBindings();
			selectedExtension.save();
		}
	}
	public DesignElement getSelected() {
		return selected;
	}
	public void selectNode(DefaultMutableTreeNode node) {
		Object obj;
		if(node != null && (obj = node.getUserObject()) instanceof DesignElement) {
			//Always set the extension first so that when the selected element gets initialized, it gets the correct type map. Otherwise, it will get one from the previously selected extension
			selectedExtension = getExtension(node);
			selectElement((DesignElement) obj);
		}
		else {
			selectedExtension = null;
			selectElement(null);
		}
	}
	public void selectElement(DesignElement e) {
		Dimension size = getSize();
		selected = e;
		setPreferredSize(size);
		resetLayout();
		if(e != null) {
			out.println("Initialize from element: " + e.getName());
			extensionField.setText(selectedExtension.getPath().getAbsolutePath());
			actionsPanel.add(getRemoveElementButton());
			if(e instanceof TranscendenceMod && ((TranscendenceMod) e).getPath().isFile()) {
				actionsPanel.add(getDeleteExtensionButton());
			}
			xmlButton.setEnabled(true);
			e.initializeFrame(this);
		}
		
		if(selectedExtension != null) {
			//bindButton.setEnabled(selectedExtension.isUnbound());
			//saveButton.setEnabled(selectedExtension.isUnsaved());
			bindButton.setEnabled(true);
			saveButton.setEnabled(true);
		}
		
		
		frame.pack();
		repaint();
	}
	public JButton getRemoveElementButton() {
		JButton removeElementButton = createJButton("Remove Element");
		removeElementButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				removeSelectedElement();
			}
		});
		return removeElementButton;
	}
	public JButton getDeleteExtensionButton() {
		JButton deleteExtensionButton = createJButton("Delete Extension");
		deleteExtensionButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String path = selectedExtension.getPath().getName();
				String input = JOptionPane.showInputDialog(createTextArea("[Warning] To delete this extension, type in the file name", false));
				if(input.equals(path)) {
					selectedExtension.getPath().delete();
					removeSelectedElement();
					JOptionPane.showMessageDialog(XMLPanel.this, createTextArea("[Success] Deleted extension file " + path, false));
				} else {
					JOptionPane.showMessageDialog(XMLPanel.this, createTextArea("[Failure] Incorrect input path " + input, false));
				}
			}
		});
		return deleteExtensionButton;
	}
	public void removeSelectedElement() {
		DefaultMutableTreeNode node = getNode(getSelected()),
				parent = (DefaultMutableTreeNode) node.getParent();
		Object parentObj;;
		elementTreeModel.removeNodeFromParent(node);
		if(parent == null/* || !((parentObj = parent.getUserObject()) instanceof DesignElement)*/) {
			selectElement(null);
			selectedExtension = null;
		} else if(!((parentObj = parent.getUserObject()) instanceof DesignElement)) {
			selectNode(parent);
		} else {
			System.out.println("DesignElement Parent Node found");
			DesignElement parentElement = (DesignElement) parentObj;
			parentElement.getSubElements().remove(getSelected());
			if(parent.getChildCount() > 1) {
				selectNode(((DefaultMutableTreeNode) parent.getChildAt(1)));
			} else if(parent.getUserObject() instanceof DesignElement) {
				selectNode(parent);
			} else {
				selectNode(parent);
			}
		}
	}
	public void removeSelf() {
		frame.remove(this);
	}
	public void setData(DesignElement e) {
		e.setName(nameField.getText());
		List<DesignAttribute> attributes = e.getAttributes();
		Component[] fields = fieldPanel.getComponents();
		for(int i = 0; i < attributes.size(); i++) {
			String value = "";
			Component field = fields[i+1];
			if(field instanceof JTextField) {
				value = ((JTextField) field).getText();
			} else if(field instanceof JComboBox) {
				value = (String) ((JComboBox) field).getSelectedItem();
			}
			/*
			else if(field instanceof JLabel) {
				value = (String) ((JLabel) field).getText();
			}
			*/
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
			out.println("Could not find node");
		} else {
			out.println("Found Node: " + theNode.toString());
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
		addElementChildren(se, node);
		out.println("Tree Path (Create): " + new TreePath(elementTreeModel.getPathToRoot(node)));
		elementTree.setSelectionPath(new TreePath(elementTreeModel.getPathToRoot(node)));
	}
	public void addElementChildren(DesignElement se, DefaultMutableTreeNode parentNode) {
		for(DesignElement sub : se.getSubElements()) {
			DefaultMutableTreeNode subNode = new DefaultMutableTreeNode(sub);
			elementTreeModel.insertNodeInto(subNode, parentNode, 0);
			elementTree.setSelectionPath(new TreePath(elementTreeModel.getPathToRoot(subNode)));
			addElementChildren(sub, subNode);
		}
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
