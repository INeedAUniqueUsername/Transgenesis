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
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.TreeMap;
import java.util.function.Function;

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
	private TranscendenceMod parent;
	private TypeManager types;
	Map<String, DesignElement> typeMap;
	private File path;
	//Do not acquire additional dependencies from our dependencies
	//Does not include Modules
	private List<TranscendenceMod> dependencies;
	private List<TranscendenceMod> modules;
	HashCodeContainer codes;
	ChangeDetector changes = new ChangeDetector(this);
	public TranscendenceMod(String name) {
		super(name);
		parent = null;
		types = new TypeManager();
		typeMap = new TreeMap<String, DesignElement>();
		path = null;
		dependencies = new LinkedList<>();
		modules = new LinkedList<>();
		codes = new HashCodeContainer();
	}
	public List<TranscendenceMod> collapseModuleChain() {
		ArrayList<TranscendenceMod> allModules = new ArrayList<>();
		for(TranscendenceMod module : modules) {
			allModules.addAll(module.collapseModuleChain());
		}
		return allModules;
	}
	//To allow overwriting dependency types without recursively binding all the extensions, we should take a list of Types from dependencies
	//If we're a TranscendenceModule, then have our parent extension bind types for us
	public void updateTypeBindingsWithModules() {
		out.println(getConsoleMessage("[General] Recursive Type Binding requested"));
		updateTypeBindings();
		for(TranscendenceMod module : modules) {
			module.updateTypeBindingsWithModules(typeMap);
		}
		System.out.println("[General] Recursive Type Binding complete");
		codes.setLastBindCode(getBindCode());
	}
	public void updateTypeBindingsWithModules(Map<String, DesignElement> parentMap) {
		out.println(getConsoleMessage("[General] Recursive Type Binding continued"));
		typeMap = new TreeMap<String, DesignElement>(parentMap);
		bindAccessibleTypes(typeMap);
		for(TranscendenceMod module : modules) {
			module.updateTypeBindingsWithModules(typeMap);
		}
		System.out.println("[General] Recursive Type Binding partially complete");
		codes.setLastBindCode(getBindCode());
	}
	public void updateTypeBindings() {
		out.println(getConsoleMessage("[General] Type Binding requested"));
		//Check if anything changed since the last binding. If not, then we don't update.
		if(!isUnbound()) {
			out.println(getConsoleMessage("[Warning] Type binding skipped; no changes"));
			return;
		}
		String consoleName = getName() + path.getPath();
		
		typeMap.clear();
		BindParent: if(getName().equals("TranscendenceModule")) {
			//We should have our parent extension handle this
			if(parent == null) {
				out.println(getConsoleMessage("[Warning] Parent Type Binding canceled; Parent Extension unknown"));
				break BindParent;
			}
			out.println(getConsoleMessage("[General] Type Binding requested from Parent Extension"));
			
			//We only update Parent Extension Type Bindings if there are unbound changes
			if(parent.isUnbound()) {
				parent.updateTypeBindings();
			} else {
				out.println(getConsoleMessage("[Warning] Parent Type Binding skipped; no changes"));
			}
			
			out.println(getConsoleMessage("[Success] Parent Type Binding complete; copying Types"));
			//Inherit types from our Parent Extension; we will not automatically receive them when the Parent Extension is updated
			//We make a copy of the type map from the Parent Extension since we don't want it to inherit Types/Dependencies that are exclusive to us
			typeMap.putAll(parent.typeMap);
		}
		
		bindAccessibleTypes(typeMap);
		codes.setLastBindCode(getBindCode());
		out.println(getConsoleMessage("[Success] Type Binding complete"));
	}
	public void bindAccessibleTypes(Map<String, DesignElement> typeMap) {
		//Insert all of our own Types. This will allow dependencies to override them
		for(String s : types.bindAll().values()) {
			if(typeMap.containsKey(s)) {
				System.out.println(getConsoleMessage("[Failure] Duplicate UNID: " + s));
			} else {
				typeMap.put(s, null);
			}
		}
		//If we have a UNID of our own, bind it
		if(hasAttribute("unid")) {
			typeMap.put(getAttributeByName("unid").getValue(), this);
		}
		updateDependencies();
		bindDependencyTypes(typeMap);
		updateModules();
		bindInternalTypes(typeMap);
		bindModuleTypes(typeMap);
	}
	//Allow modules to take external entities
	//Note: If we are a Module, we do not inherit dependencies from the Parent Extension. We will just receive the Type Map, which already includes the Parent Extension's Dependency Types
	public void updateDependencies() {
		String consoleName = getName() + path.getPath();
		out.println(getConsoleMessage("[General] Updating Dependencies"));
		dependencies.clear();
		for(DesignElement sub : getSubElements()) {
			String subName = sub.getName();
			switch(subName) {
			case "Library":
				String library_unid = sub.getAttributeByName("unid").getValue();
				out.println(getConsoleMessage("[General] Looking for " + subName + " " + library_unid));
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
				out.println(getConsoleMessage("[General] Looking for " + subName + " " + libraryPath));
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
				
				if(!found) {
					out.println(getConsoleMessage("[Warning] " + subName + " " + libraryPath + " could not be found. It may be unloaded."));
				} else {
					out.println(getConsoleMessage("[Success] " + subName + " " + libraryPath + " found."));
				}
				break;
			}
		}
	}
	public void bindDependencyTypes(Map<String, DesignElement> typeMap) {
		//Avoid going into a circular dependency binding loop by binding only the internal types from each dependency
		//Note: Circular dependencies are not supported
		for(TranscendenceMod dependency : dependencies) {
			dependency.bindAsDependency(typeMap);
		}
	}
	public void bindAsDependency(Map<String, DesignElement> typeMap) {
		/*
		if(typeMap.get(getAttributeByName("unid")) == this) {
			return;
		}
		*/
		for(String s : types.bindAll().values()) {
			typeMap.put(s, null);
		}
		bindInternalTypes(typeMap);
		for(TranscendenceMod module : modules) {
			module.bindAsDependency(typeMap);
		}
		
		/*
		updateDependencies();
		for(TranscendenceMod dependency : dependencies) {
			dependency.bindAsDependency(typeMap);
		}
		*/
	}
	public void updateModules() {
		modules.clear();
		out.println(getConsoleMessage("[General] Updating Modules"));
		
		/*
		case "TranscendenceAdventure":
				case "CoreLibrary":
					String libraryPath = getModulePath(sub.getAttributeByName("filename").getValue());
					out.println(getConsoleMessage("[General] Looking for " + subName + " " + libraryPath));
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
					
					if(!found) {
						out.println(getConsoleMessage("[Warning] " + subName + " " + libraryPath + " could not be found. It may be unloaded."));
					} else {
						out.println(getConsoleMessage("[Success] " + subName + " " + libraryPath + " found."));
					}
					break;
		*/
		
		
		for(DesignElement e : getSubElements()) {
			switch(e.getName()) {
			case "Module":
				String moduleFilename = e.getAttributeByName("filename").getValue();
				String modulePath = getModulePath(moduleFilename);
				//Look for our module in the Extensions list
				out.println(getConsoleMessage("[General] Looking for Module " + modulePath + "."));
				boolean found = false;
				FindModule:
				for(TranscendenceMod m : XMLPanel.getInstance().getExtensions()) {
					if(m.path.getAbsolutePath().equals(modulePath)) {
						modules.add(m);
						m.parent = this;
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
				break;
			}
		}
	}
	//Bindings between Types and Designs are stored in the map
	//This only binds Types with Designs that are defined within THIS file; Module bindings happen later
	public void bindInternalTypes(Map<String, DesignElement> typeMap) {
		out.println(getConsoleMessage("[General] Binding Internal Designs"));
		//Include ourself
		/*
		if(this.hasAttribute("unid")) {
			typeMap.put(getAttributeByName("unid").getValue(), this);
		}
		*/
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
						} else if(typeMap.get(sub_type) == sub) {
							//Ignore if this element was bound earlier (such as during a Parent Type Binding).
						} else if(typeMap.get(sub_type).getName().equals(sub.getName())) {
							//If the UNID is bound to a Design with the same tag, then it's probably an override
							out.println(getConsoleMessage2(sub.getName(), String.format("%-15s %s", "[Warning] Override Type:", sub_type)));
						} else {
							out.println(getConsoleMessage2(sub.getName(), String.format("%-15s %s", "[Warning] Duplicate Type:", sub_type)));
						}
					} else {
						//Depending on the context, we will not be able to identify whether this is defining a completely nonexistent Type or overriding an external type
						out.println(getConsoleMessage2(sub.getName(), String.format("%-15s %-31s %s", "[Warning] Unknown UNID:", sub_type + ";", "It may be an override for an unloaded dependency or it may be nonexistent")));
					}
				} else {
					out.println(getConsoleMessage2(sub.getName(), "[Failure] Missing unid= attribute"));
					
				}
			}
		}
	}
	private void bindModuleTypes(Map<String, DesignElement> typeMap) {
		
		for(TranscendenceMod module : modules) {
			module.bindInternalTypes(typeMap);
			//Let sub-modules bind Types for us too
			module.updateModules();
			module.bindModuleTypes(typeMap);
		}
		/*
		//Let modules inherit a copy of our bindings
		for(TranscendenceMod module : modules) {
			System.out.println(getConsoleMessage("[General] Copying Type Bindings to Module " + module.getPath().getAbsolutePath()));
			module.typeMap = new TreeMap<String, DesignElement>(typeMap);
		}
		*/
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
		return getSaveCode() != codes.getLastSaveCode();
	}
	public int getLastBindCode() {
		return codes.getLastBindCode();
	}
	public int getLastSaveCode() {
		return codes.getLastSaveCode();
	}
	//Bind Code depends on changes with local/dependency Types and module Designs
	public int getBindCode() {
		changes.showChanges(this);
		changes.update(this);
		/*
		Function<TranscendenceMod, Collection<String>> getTypes = (TranscendenceMod extension) -> {
			return extension.types.bindAll().values();
			};
		*/
		//out.println(getConsoleMessage("[General] Bind Code: " + Objects.hash(parent == null ? null : getTypes.apply(parent), types.bindAll().values(), dependencies.stream().map(getTypes), modules.stream().map(getTypes))));	
		return Objects.hash(
				types,
				dependencies,//dependencies.stream().map(getTypes),
				modules
				/*
				collapseModuleChain().stream().map((TranscendenceMod module) -> {
					return module.getSubElements().hashCode();
				})
				*/
				);
		//return Objects.hash(parent, types, dependencies, modules);
	}
	//Save Code depends on changes to our own code 
	public int getSaveCode() {
		return Objects.hash(types, this.getAttributes(), this.getSubElements());
	}
	public void save() {
		if(!path.isFile()) {
			out.println(getConsoleMessage("[Failure] Invalid file path: " + path.getAbsolutePath()));
			return;
		}
		out.println(getConsoleMessage("[Success] File saved"));
		if(!isUnsaved()) {
			out.println(getConsoleMessage("[Warning] Saving skipped; no changes"));
			return;
		}
		codes.setLastSaveCode(getSaveCode());
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
	public int hashCode() {
		return Objects.hash(this.dependencies, this.modules, this.path, this.types);
	}
	class ChangeDetector {
		private TranscendenceMod parent;
		private TypeManager types;
		Map<String, DesignElement> typeMap;
		private File path;
		private List<TranscendenceMod> dependencies;
		private List<TranscendenceMod> modules;
		public ChangeDetector(TranscendenceMod extension) {
			update(extension);
		}
		public void update(TranscendenceMod extension) {
			parent = extension.parent;
			types = extension.types;
			typeMap = extension.typeMap;
			path = extension.path;
			dependencies = extension.dependencies;
			modules = extension.modules;
		}
		public void showChanges(TranscendenceMod extension) {
			if(parent != extension.parent) System.out.println(getConsoleMessage("Parent changed"));
			if(types != extension.types) System.out.println(getConsoleMessage("Types changed"));
			if(typeMap != extension.typeMap) System.out.println(getConsoleMessage("Type Map changed"));
			if(path != extension.path) System.out.println(getConsoleMessage("Path changed"));
			if(dependencies != extension.dependencies) System.out.println(getConsoleMessage("Dependencies changed"));
			if(modules != extension.modules) System.out.println(getConsoleMessage("Modules changed"));
		}
		public int hashCode() {
			return 0;
		}
	}
}
