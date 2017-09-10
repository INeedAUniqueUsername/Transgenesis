package mod;

import static java.lang.System.out;
import java.awt.event.ActionEvent;
import static window.Window.Fonts.*;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.TreeBidiMap;
import org.w3c.dom.Document;

import net.miginfocom.layout.CC;
import panels.TypeManager;
import panels.XMLPanel;
import window.Window;
import xml.DesignAttribute;
import xml.DesignElement;
public class TranscendenceMod extends DesignElement {
	//This is a placeholder in the typeMap to make sure that an unbound Type exists in it, because containsKey returns false with an actual null value
	/*
	DesignElementOld DESIGN_NULL = new DesignElementOld() {
		public boolean equals(Object o) {
			return this == o;
		}
	};
	*/
	private TypeManager types;
	Map<String, DesignElement> typeMap;
	private File path;
	//Do not acquire additional dependencies from our dependencies
	//Does not include Modules
	private List<TranscendenceMod> dependencies;
	private List<TranscendenceMod> modules;
	HashCodeContainer codes;
	public TranscendenceMod(String name) {
		super(name);
		types = new TypeManager();
		typeMap = new TreeMap<String, DesignElement>();
		path = null;
		dependencies = new LinkedList<>();
		modules = new LinkedList<>();
		codes = new HashCodeContainer();
	}
	//To allow overwriting dependency types without recursively binding all the extensions, we should take a list of Types from dependencies
	//If we're a TranscendenceModule, then have our parent extension bind types for us
	public void updateTypeBindings() {
		out.println(getConsoleMessage("[General] Type Binding requested"));
		int bindingCode = getBindCode();
		//Check if anything changed since the last binding. If not, then we don't update.
		if(bindingCode == codes.getLastBindCode()) {
			out.println(getConsoleMessage("[Warning] Type binding skipped; no changes"));
			return;
		}
		codes.setLastBindCode(bindingCode);
		String consoleName = getName() + path.getPath();
		if(getName().equals("TranscendenceModule")) {
			//We should have our parent extension handle this
			if(dependencies.size() < 1) {
				out.println(getConsoleMessage("[Failure] Type Binding failed; Parent Extension unknown"));
				return;
			}
			out.println(getConsoleMessage("[General] Type Binding requested from Parent Extension"));
			TranscendenceMod parent = dependencies.get(0);
			parent.updateTypeBindings();
			//Inherit typeMap from parent
			typeMap = parent.typeMap;
			return;
		}
		out.println(getConsoleMessage("[General] Type Binding initiated"));
		typeMap = new TreeMap<>();
		//First, insert all of our own Types. This will allow dependencies to override them
		for(String s : types.bindAll().values()) {
			typeMap.put(s, null);
		}
		//If we have a UNID of our own, bind it
		if(hasAttribute("unid")) {
			typeMap.put(getAttributeByName("unid").getValue(), this);
		}
		updateDependencies();
		bindDependencyDesigns(typeMap);
		updateModules();
		bindInternalDesigns(typeMap);
		out.println(getConsoleMessage("[Success] Type Binding complete"));
	}
	//Allow modules to take external entities
		public void updateDependencies() {
			String consoleName = getName() + path.getPath();
			out.println(getConsoleMessage("[General] Updating Dependencies"));
			dependencies = new LinkedList<TranscendenceMod>();
			for(DesignElement sub : getSubElements()) {
				switch(sub.getName()) {
				case "Library":
					String library_unid = sub.getAttributeByName("unid").getValue();
					//Make sure that Library Types are defined in our TypeManager so that they always work in-game
					boolean found = false;
					FindLibrary:
					for(TranscendenceMod m : XMLPanel.getInstance().getExtensions()) {
						if(
								m.getName().equals("TranscendenceLibrary") ||
								m.getName().equals("CoreLibrary") &&
								m.getAttributeByName("unid").getValue().equals(library_unid)) {
							dependencies.add(m);
							found = true;
							break FindLibrary;
						}
					}
					if(!found) {
						out.println(getConsoleMessage("[Warning] Library " + library_unid + " could not be found. It may be unloaded."));
					} else {
						out.println(getConsoleMessage("[Success] Library " + library_unid + " found."));
					}
					break;
				case "TranscendenceAdventure":
				case "CoreLibrary":
					String libraryPath = getModulePath(sub.getAttributeByName("filename").getValue());
					//Make sure that Library Types are defined in our TypeManager so that they always work in-game
					found = false;
					FindLibrary:
					for(TranscendenceMod m : XMLPanel.getInstance().getExtensions()) {
						if(
								m.getName().equals(sub.getName()) &&
								m.getPath().getAbsolutePath().equals(libraryPath)) {
							dependencies.add(m);
							found = true;
							break FindLibrary;
						}
					}
					String name = sub.getName();
					if(!found) {
						out.println(getConsoleMessage("[Warning] " + name + " " + libraryPath + " could not be found. It may be unloaded."));
					} else {
						out.println(getConsoleMessage("[Success] " + name + " " + libraryPath + " found."));
					}
					break;
				}
			}
		}
	public void bindDependencyDesigns(Map<String, DesignElement> typeMap) {
		//Avoid going into a circular dependency binding loop by binding only the internal types from each dependency
		//Note: Circular dependencies are not supported
		for(TranscendenceMod dependency : dependencies) {
			for(String s : dependency.types.bindAll().values()) {
				typeMap.put(s, null);
			}
			dependency.bindInternalDesigns(typeMap);
		}
	}
	public void updateModules() {
		modules.clear();
		out.println(getConsoleMessage("[General] Finding Modules"));
		for(DesignElement e : getSubElements()) {
			if(!e.getName().equals("Module")) {
				continue;
			}
			String moduleFilename = e.getAttributeByName("filename").getValue();
			String modulePath = getModulePath(moduleFilename);
			//Look for our module in the Extensions list
			out.println(getConsoleMessage("[General] Finding Module " + modulePath + "."));
			boolean found = false;
			FindModule:
			for(TranscendenceMod m : XMLPanel.getInstance().getExtensions()) {
				if(m.path.getAbsolutePath().equals(modulePath)) {
					//A parent extension handles binding for all of its modules
					m.codes.setLastBindCode(m.getBindCode());
					modules.add(m);
					//We are the parent extension and we are the module's only dependency
					m.dependencies.clear();
					m.dependencies.add(this);
					found = true;
					break FindModule;
				}
			}
			if(!found) {
				out.println(getConsoleMessage("[Warning] Module " + modulePath + " could not be found. It may be unloaded."));
			} else {
				out.println(getConsoleMessage("[Success] Module " + modulePath + " found."));
			}
			//Maybe we should automatically load the module if it is not loaded already
		}
	}
	//Bindings between Types and Designs are stored in the map
	//This only affects Designs that are defined WITHIN the file and Modules
	public void bindInternalDesigns(Map<String, DesignElement> typeMap) {
		out.println(getConsoleMessage("[General] Binding Internal Designs"));
		for(DesignElement sub : getSubElements()) {
			//We already handled Library types as dependencies
			if(!sub.getName().equals("Library") && sub.hasAttribute("unid")) {
				String sub_type = sub.getAttributeByName("unid").getValue();
				//Check if the element has been assigned a UNID
				if(!(sub_type == null || sub_type.isEmpty())) {
					//Check if the UNID has been defined by the extension
					//WARNING: THE TYPE SPECIFIED IN THE ATTRIBUTE WILL NOT MATCH BECAUSE IT IS AN XML ENTITY. REMOVE THE AMPERSAND AND SEMICOLON.
					sub_type = sub_type.replaceFirst("&", "").replaceFirst(";", "");
					if(typeMap.containsKey(sub_type)) {
						//Check if the UNID has not already been bound to a Design
						if(typeMap.get(sub_type) == null) {
							typeMap.put(sub_type, sub);
						} else if(typeMap.get(sub_type).getName().equals(sub.getName())) {
							//If the UNID is bound to a Design with the same tag, then it's probably an override
							out.println(getConsoleMessage2(sub.getName(), String.format("%-15s %s", "[Warning] Override Type:", sub_type)));
						} else {
							out.println(getConsoleMessage2(sub.getName(), String.format("%-15s %s", "[Warning] Duplicate Type:", sub_type)));
						}
					} else {
						//Depending on the context, we will not be able to identify whether this is defining a completely nonexistent Type or overriding an external type
						out.println(getConsoleMessage2(sub.getName(), String.format("%-15s %-31s %s", "[Failure] Unknown UNID:", sub_type + ";", "It may be an override for an unloaded dependency or it may be nonexistent")));
					}
				} else {
					out.println(getConsoleMessage2(sub.getName(), "[Failure] Missing unid= attribute"));
					
				}
			}
		}
		for(TranscendenceMod module : modules) {
			module.bindInternalDesigns(typeMap);
		}
	}
	public String getConsoleName() {
		return String.format("%-21s %s", getName(), " (" + path.getName() + ")");
	}
	public String getConsoleMessage(String message) {
		return String.format("%-53s - %s", getConsoleName(), message);
	}
	public String getConsoleMessage2(String subName, String message) {
		return String.format("%-72s - %s", getConsoleMessage(subName), message);
	}
	public String getModulePath(String moduleFilename) {
		String folder = path.getAbsolutePath().substring(0, path.getAbsolutePath().lastIndexOf(File.separator));
		String modulePath = folder + File.separator + moduleFilename;
		return modulePath;
	}
	public Map<String, DesignElement> getTypeMap() {
		out.println(getConsoleMessage("[General] Type Map requested"));
		return typeMap;
	}
	public String getXMLOutput() {
		return
				"<?xml version=\"1.0\" encoding=\"utf-8\"?>" + "\n" +
				"<!DOCTYPE" + " " + getName() + "\n" +
				"[" + "\n" +
				types.getXMLOutput() + "\n" +
				"]>" + "\n" +
				super.getXMLOutput()
				;
	}
	public String getXMLMetaData() {
		return
				"<?xml version=\"1.0\" encoding=\"utf-8\"?>" + "\n" +
				types.getXMLMetaData();
	}
	public String getDisplayName() {
		String name = getName().replaceFirst("Transcendence", "");
		/*
		for(DesignAttribute a : new DesignAttribute[] {getAttributeByName("name"), getAttributeByName("UNID")}) {
			if(a == null) {
				continue;
			}
			String displayName = a.getValue();
			if(displayName != null && !displayName.equals("")) {
				return String.format("%-16s%s", name, displayName);
			}
		}
		*/
		if(path != null) {
			return String.format("%-16s%s", name, path.getName());
		}
		return super.getDisplayName();
	}
	public void setUNIDs(TypeManager unids) {
		this.types = unids;
	}
	public TypeManager getTypeManager() {
		return types;
	}
	public void setPath(File absolutePath) {
		// TODO Auto-generated method stub
		path = absolutePath;
	}
	public File getPath() {
		return path;
	}
	public boolean isUnbound() {
		return getBindCode() != codes.getLastBindCode();
	}
	public boolean isUnsaved() {
		return hashCode() != codes.getLastSaveCode();
	}
	public int getBindCode() {
		return Objects.hash(types, dependencies, modules);
	}
	public void save() {
		if(!path.isFile()) {
			out.println(getConsoleMessage("[Failure] Invalid file path: " + path.getAbsolutePath()));
			return;
		}
		out.println(getConsoleMessage("[Success] File saved"));
		int lastSaveCode = hashCode();
		if(lastSaveCode == codes.getLastSaveCode()) {
			out.println(getConsoleMessage("[Warning] Saving skipped; no changes"));
			return;
		}
		codes.setLastSaveCode(lastSaveCode);
		File path_metadata = new File(path.getAbsolutePath() + ".dat");
		path.delete();
		path_metadata.delete();
		try {
			FileWriter fw = new FileWriter(path);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(getXMLOutput());
			bw.close();
			fw.close();
			
			//Do not write a metadata file for bindings if there are none
			if(types.bindAll().size() > 0) {
				fw = new FileWriter(path_metadata);
				bw = new BufferedWriter(fw);
				bw.write(getXMLMetaData());
				bw.close();
				fw.close();
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			
		}
		//Save all Modules
		for(TranscendenceMod m : modules) {
			m.save();
		}
	}
	
	public void initializeFrame(XMLPanel panel) {
		super.initializeFrame(panel);
		JButton manageUNIDs = new JButton("Manage UNIDs");
		manageUNIDs.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(arg0.getSource() == manageUNIDs) {
					panel.frame.remove(panel);
					types.setEditor(panel);
					types.refreshFrame();
					panel.frame.pack();
				}
			}});
		manageUNIDs.setFont(Medium.f);
		panel.actionsPanel.add(manageUNIDs);
		
		
		JButton viewTypeBindingsButton = new JButton("View Type Bindings");
		viewTypeBindingsButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() == viewTypeBindingsButton) {
					JScrollPane scrollPane = XMLPanel.createScrollPane(XMLPanel.createTextArea(getTypeMapText(), false));
					scrollPane.setMaximumSize(panel.getSize());
					JOptionPane.showMessageDialog(panel, scrollPane);
				}
			}
			
		});
		viewTypeBindingsButton.setFont(Medium.f);
		panel.actionsPanel.add(viewTypeBindingsButton);
	}
	public String getTypeMapText() {
		String result = "";
		for(Entry<String, DesignElement> e : typeMap.entrySet()) {
			DesignElement design = e.getValue();
			result += String.format(
					"%-32s%s",
					e.getKey(),
					design == null ? "" : design.getName()
					) + "\n";
		}
		return result;
	}
	public String toString() {
		return String.format("%-42s%-3s%-3s", super.toString(), (isUnbound() ? "[B]" : ""), (isUnsaved() ? "[S]" : ""));
	}
}
