package mod;

import java.awt.event.ActionEvent;
import static window.Window.Fonts.*;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
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
	private File path;
	public TranscendenceMod(String name) {
		super(name);
		unids = new UNIDManager();
		path = null;
	}
	public String getXMLOutput() {
		return
				"<?xml version=\"1.0\" encoding=\"utf-8\"?>" + "\n" +
				"<!DOCTYPE" + getName() + "\n" +
				"[" + "\n" +
				unids.getXMLOutput() + "\n" +
				"]>" + "\n" +
				super.getXMLOutput() +
				unids.getXMLMetaData()
				;
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
	public void save() {
		System.out.println("File saved!");
		path.delete();
		try {
			FileWriter fw = new FileWriter(path);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(getXMLOutput());
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
