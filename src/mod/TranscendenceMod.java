package mod;

import java.awt.event.ActionEvent;
import static window.Window.Fonts.*;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.TreeMap;

import javax.swing.JButton;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.TreeBidiMap;

import net.miginfocom.layout.CC;
import panels.UNIDManager;
import panels.XMLPanel;
import window.Window;
import xml.DesignAttribute;
import xml.DesignElementOld;
public class TranscendenceMod extends DesignElementOld {
	public final UNIDManager unids;
	private File path;
	public TranscendenceMod(String name) {
		super(name);
		unids = new UNIDManager();
		path = null;
	}
	public String getXMLOutput() {
		return
				"<?xml version=\"1.0\" encoding=\"utf-8\"?>" + "\n" +
				"<!DOCTYPE TranscendenceExtension" + "\n" +
				"[" + "\n" +
				unids.getXMLOutput() + "\n" +
				"]>" + "\n" +
				super.getXMLOutput();
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
		super.initializeFrame(panel);
		JButton button = new JButton("Manage UNIDs");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(arg0.getSource() == button) {
					panel.frame.add(unids.getPanel(panel));
					panel.removeSelf();
					panel.frame.pack();
				}
			}});
		button.setFont(Large.f);
		panel.add(button, new CC()
				.x("75%")
				.y("0%")
				.width("25%")
				.height("5%")
				);
	}
}
