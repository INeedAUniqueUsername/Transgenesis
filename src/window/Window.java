package window;

import java.awt.Font;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import mod.ExtensionFactory;
import panels.XMLPanel;

public class Window implements Runnable {
	/*
	Terms used in this program
		Name	Definition
		UNID	Number in hexadecimal or decimal format
		Type	XML entity bound to a UNID
		Design	XML element bound to a Type
	*/
	
	public static final boolean DEBUG = true;
	public static final String FONT = "Consolas";
	public enum Fonts {
		Title(new Font(FONT, Font.BOLD, 32)),
		Large(new Font(FONT, Font.BOLD, 24)),
		Medium(new Font(FONT, Font.PLAIN, 20));
		public final Font f;
		Fonts(Font f) {
			this.f = f;
		}
	}
	String file_name = "0.txt";
	File file;
	Writer writer;
	BufferedReader reader;
	Frame frame;
	static Window instance;
	
	public static void main(String[] args) {
		//javax.swing.UIManager.put("OptionPane.font", Fonts.Medium.f);
		UIManager.put("OptionPane.buttonFont", new FontUIResource(Fonts.Medium.f));
		
		/*
		try {
			FileWriter fw = new FileWriter("T_E_S_T.xml");
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(ExtensionFactory.Extensions.TranscendenceAdventure.get().toMinistryMarkdown(1));
			bw.close();
			fw.close();
		}catch(Exception e) {}
		/**/
		
		
		System.out.println("Run");
		if(args.length == 1 && args[0].equals("ErrorTest")) {
			try {
				((Integer) null).intValue();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		instance = new Window();
	}
	
	public Window(){
		run();
	}
	public void print()
	{
		try {
			for(String s: Files.readAllLines(Paths.get(file_name)))
			{
				System.out.println(s);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void write(String input)
	{
		   try {
			Files.write(Paths.get(file_name), input.getBytes("utf-16"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void delete()
	{
		try {
			Files.delete(Paths.get(file_name));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void close()
	{
		System.exit(0);
	}

	@Override
	public void run() {
		SwingUtilities.invokeLater(() -> {
			frame = new Frame();
		});
		
	}
}
