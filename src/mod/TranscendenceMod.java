package mod;

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
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
	public TranscendenceMod(String name) {
		super(name);
		types = new TypeManager();
		typeMap = new TreeMap<String, DesignElement>();
		path = null;
		dependencies = new LinkedList<>();
		modules = new LinkedList<>();
	}
	//To allow overwriting dependency types without recursively binding all the extensions, we should take a list of Types from dependencies
	//If we're a TranscendenceModule, then have our parent extension bind types for us
	public void updateTypeBindings() {
		if(getName().equals("TranscendenceModule")) {
			System.out.println("Warning: Not binding Types for TranscendenceModule");
			//We should have our parent extension handle this
			if(dependencies.size() < 1) {
				System.out.println("Parent Extension unknown");
				return;
			}
			TranscendenceMod parent = dependencies.get(0);
			parent.updateTypeBindings();
			//Inherit typeMap from parent
			typeMap = parent.typeMap;
			return;
		}
		System.out.println("Type Binding requested");
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
	}
	//Allow modules to take external entities
		public void updateDependencies() {
			String warnings = getName() + ": Updating Dependencies";
			dependencies = new LinkedList<TranscendenceMod>();
			for(DesignElement sub : getSubElements()) {
				switch(sub.getName()) {
				case "Library":
					String library_unid = sub.getAttributeByName("unid").getValue();
					//Make sure that Library Types are defined in our TypeManager so that they always work in-game
					boolean found = false;
					FindLibrary:
					for(TranscendenceMod m : XMLPanel.getExtensions()) {
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
						warnings += "\n" + getName() + ": " + "Could not find Library " + library_unid + ". It may be unloaded.";
					}
					break;
				case "TranscendenceAdventure":
				case "CoreLibrary":
					String libraryPath = getModulePath(sub.getAttributeByName("filename").getValue());
					//Make sure that Library Types are defined in our TypeManager so that they always work in-game
					found = false;
					FindLibrary:
					for(TranscendenceMod m : XMLPanel.getExtensions()) {
						if(
								m.getName().equals(sub.getName()) &&
								m.getPath().getAbsolutePath().equals(libraryPath)) {
							dependencies.add(m);
							found = true;
							break FindLibrary;
						}
					}
					if(!found) {
						warnings += "\n" + getName() + ": " + "Could not find Library " + libraryPath + ". It may be unloaded.";
					}
					break;
				}
			}
			XMLPanel.showWarningPane(warnings);
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
		String warnings = getName() + ": " + "Updating Modules";
		for(DesignElement e : getSubElements()) {
			if(!e.getName().equals("Module")) {
				continue;
			}
			String moduleFilename = e.getAttributeByName("filename").getValue();
			String modulePath = getModulePath(moduleFilename);
			//Look for our module in the Extensions list
			boolean found = false;
			FindModule:
			for(TranscendenceMod m : XMLPanel.getExtensions()) {
				if(m.path.getAbsolutePath().equals(modulePath)) {
					modules.add(m);
					//We are the parent extension and we are the module's only dependency
					m.dependencies.clear();
					m.dependencies.add(this);
					found = true;
					break FindModule;
				}
			}
			if(!found) {
				warnings += "\n" + getName() + ": " + "Could not find Module " + modulePath + ". It may be unloaded.\n";
			}
			//Maybe we should automatically load the module if it is not loaded already
		}
		XMLPanel.showWarningPane(warnings);
	}
	//Bindings between Types and Designs are stored in the map
	//This only affects Designs that are defined WITHIN the file and Modules
	public void bindInternalDesigns(Map<String, DesignElement> typeMap) {
		String warnings = getName() + ": Binding Internal Designs";
		//warnings += typeMap.keySet().toString() + "\n";
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
							warnings += "\n" + sub.getName() + ": " + "Override Type: " + sub_type;
						} else {
							warnings += "\n" + sub.getName() + ": " + "Duplicate Type: " + sub_type;
						}
					} else {
						//Depending on the context, we will not be able to identify whether this is defining a completely nonexistent Type or overriding an external type
						warnings += "\n" + sub.getName() + ": " + "Unknown UNID: " + sub_type + ". It may be an override for an unloaded dependency or it may be nonexistent\n";
					}
				} else {
					warnings += "\n" + sub.getName() + ": " + "Missing unid= attribute";
				}
			}
			for(TranscendenceMod module : modules) {
				module.bindInternalDesigns(typeMap);
			}
		}
		XMLPanel.showWarningPane(warnings);
	}
	public String getModulePath(String moduleFilename) {
		String folder = path.getAbsolutePath().substring(0, path.getAbsolutePath().lastIndexOf(File.separator));
		String modulePath = folder + File.separator + moduleFilename;
		return modulePath;
	}
	public Map<String, DesignElement> getTypeMap() {
		System.out.println("Type Map requested");
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
		for(DesignAttribute a : new DesignAttribute[] {getAttributeByName("name"), getAttributeByName("UNID")}) {
			if(a == null) {
				continue;
			}
			String displayName = a.getValue();
			if(displayName != null && !displayName.equals("")) {
				return String.format("%-16s%s", name, displayName);
			}
		}
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
	public void save() {
		System.out.println("File saved!");
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
		manageUNIDs.setFont(Large.f);
		JLabel filePath = XMLPanel.createLabel(path.getAbsolutePath());
		panel.add(manageUNIDs, new CC()
				.x("75%")
				.y("0%")
				.width("15%")
				.height("5%")
				);
		panel.add(filePath, new CC()
				.x("25%")
				.y("5%")
				.minWidth("75%").maxWidth("75%")
				.minHeight("5%").maxHeight("5%")
				);
		super.initializeFrame(panel);
	}
}
