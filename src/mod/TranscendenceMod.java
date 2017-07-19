package mod;

import java.awt.event.ActionEvent;
import static window.Window.Fonts.*;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JButton;
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
import panels.UNIDManager;
import panels.XMLPanel;
import window.Window;
import xml.DesignAttribute;
import xml.DesignElementOld;
public class TranscendenceMod extends DesignElementOld {
	public UNIDManager unids;
	Map<String, DesignElementOld> typeMap;
	private File path;
	List<DesignElementOld> dependencies; //If we are a module, this includes our parent extension
	public TranscendenceMod(String name) {
		super(name);
		unids = new UNIDManager();
		typeMap = new TreeMap<String, DesignElementOld>();
		path = null;
	}
	public void updateTypeBindings() {
		System.out.println("Rebinding Types");
		typeMap = new TreeMap<>();
		bindTypes(typeMap);
	}
	
	//Allow modules to take external entities
	
	//Bindings between Types and Designs are stored in the map
	public void bindTypes(Map<String, DesignElementOld> typeMap) {
		
		//If we have a UNID of our own, bind it
		if(hasAttribute("unid")) {
			typeMap.put(getAttributeByName("unid").getValue(), this);
		}
		
		for(String s : unids.bindAll().values()) {
			typeMap.put(s, null);
		}
		String warnings = "Warnings\n";
		for(DesignElementOld sub : getSubElements()) {
			if(sub.hasAttribute("unid")) {
				String sub_type = sub.getAttributeByName("unid").getValue();
				//Check if the element has been assigned a UNID
				if(!(sub_type == null || sub_type.isEmpty())) {
					//Check if the UNID has been defined by the extension
					if(typeMap.containsKey(sub_type)) {
						//Check if the UNID has not already been bound to an element
						if(typeMap.get(sub_type) == null) {
							typeMap.put(sub_type, sub);
						} else {
							warnings += sub.getName() + ": " + "Duplicate Type: " + sub_type + "\n";
						}
					} else {
						warnings += sub.getName() + ": " + "Unknown UNID: " + sub_type + "\n";
					}
				} else {
					warnings += sub.getName() + ": " + "Missing unid= attribute" + "\n";
				}
			} else if(sub.getName().equals("Module")) {
				//We may be looking at a module. Use it to bind types.
				String moduleFileName = sub.getAttributeByName("filename").getValue();
				String folder = path.getAbsolutePath().substring(0, path.getAbsolutePath().lastIndexOf(File.separator));
				String modulePath = folder + File.separator + moduleFileName;
				System.out.println("Looking for Module at " + moduleFileName);
				//Look for our module in the Extensions list
				boolean found = false;
				FindModule:
				for(TranscendenceMod m : XMLPanel.getExtensions()) {
					if(m.path.getAbsolutePath().equals(modulePath)) {
						System.out.println("Binding Types from Module at " + modulePath);
						m.bindTypes(typeMap);
						found = true;
						break FindModule;
					}
				}
				warnings += getName() + ": " + "Load module at " + modulePath + "\n";
				//Maybe we should automatically load the module if it is not loaded already
			}
		}
		JOptionPane.showMessageDialog(null, warnings);
	}
	public Map<String, DesignElementOld> getTypeMap() {
		return typeMap;
	}
	public String getXMLOutput() {
		return
				"<?xml version=\"1.0\" encoding=\"utf-8\"?>" + "\n" +
				"<!DOCTYPE" + " " + getName() + "\n" +
				"[" + "\n" +
				unids.getXMLOutput() + "\n" +
				"]>" + "\n" +
				super.getXMLOutput()
				;
	}
	public String getXMLMetaData() {
		return
				"<?xml version=\"1.0\" encoding=\"utf-8\"?>" + "\n" +
				unids.getXMLMetaData();
	}
	public String getDisplayName() {
		for(DesignAttribute a : new DesignAttribute[] {getAttributeByName("name"), getAttributeByName("UNID")}) {
			if(a == null) {
				continue;
			}
			String name = a.getValue();
			if(name != null && !name.equals("")) {
				return name;
			}
		}
		if(path != null) {
			return path.getName();
		}
		return super.getDisplayName();
	}
	public void setUNIDs(UNIDManager unids) {
		this.unids = unids;
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
			
			fw = new FileWriter(path_metadata);
			bw = new BufferedWriter(fw);
			bw.write(getXMLMetaData());
			bw.close();
			fw.close();
			
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			
		}
	}
	
	public void initializeFrame(XMLPanel panel) {
		JButton manageUNIDs = new JButton("Manage UNIDs");
		manageUNIDs.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(arg0.getSource() == manageUNIDs) {
					panel.frame.remove(panel);
					unids.setEditor(panel);
					unids.refreshFrame();
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
