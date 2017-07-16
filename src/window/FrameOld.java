package window;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

import net.miginfocom.layout.AC;
import net.miginfocom.layout.CC;
import net.miginfocom.swing.MigLayout;
import panels.XMLPanel;
import designType.TypeFactory;
import mod.ExtensionFactory;
import mod.TranscendenceMod;
import mod.ExtensionFactory.Extensions;
import xml.DesignAttribute;
import xml.DesignElement;
import xml.DesignElementOld;

public class FrameOld extends JFrame {
	
	
	public FrameOld() {
		/*
		try {
			FileWriter fw = new FileWriter("XML Definitions.txt");
			BufferedWriter bw = new BufferedWriter(fw);
			DesignElementOld hierarchy = new DesignElementOld("Hierarchy");
			hierarchy.addSubElements(
					
					ExtensionFactory.Extensions.TranscendenceExtension.get(),
					ExtensionFactory.Extensions.TranscendenceLibrary.get(),
					ExtensionFactory.Extensions.TranscendenceAdventure.get(),
					ExtensionFactory.Extensions.TranscendenceModule.get()
					);
			bw.write(hierarchy.getXMLDefinition());
			bw.close();
			fw.close();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			
		}
		
		/*
		try {
			//https://stackoverflow.com/questions/5640334/how-do-i-preserve-line-breaks-when-using-jsoup-to-convert-html-to-plain-text
			//https://stackoverflow.com/questions/11154145/jsoup-how-to-extract-this-text
			Document d = Jsoup.connect("http://wiki.kronosaur.com/modding/xml/effecttype").get();
			d.outputSettings(new Document.OutputSettings().prettyPrint(false));//makes html() preserve linebreaks and spacing
		    d.select("br").append("\\n");
		    d.select("p").prepend("\\n\\n");
		    String result = Jsoup.clean(d.html().replaceAll("\\\\n", "\n"), "", Whitelist.none(), new Document.OutputSettings().prettyPrint(false));
		    System.out.println(result.replaceAll("\n+", "\n"));
			System.exit(0);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			FileWriter fw = new FileWriter("XML Hierarchy.txt");
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(ExtensionFactory.Extensions.TranscendenceAdventure.get().toMinistryMarkdown());
			bw.close();
			fw.close();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			
		}
		*/
		setTitle("TransGenesis");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		add(new XMLPanel(this));
		pack();
		setExtendedState(getExtendedState() | MAXIMIZED_BOTH);
		setVisible(true);
	}
	
}
