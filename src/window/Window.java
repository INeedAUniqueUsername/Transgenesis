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
import javax.swing.WindowConstants;
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
	public static enum Fonts {
		Title(new Font(FONT, Font.BOLD, 26)),		//32
		Large(new Font(FONT, Font.BOLD, 22)),		//24
		Medium(new Font(FONT, Font.PLAIN, 18));		//20
		public final Font f;
		Fonts(Font f) {
			this.f = f;
		}
	}
	Frame frame;
	String[] args;
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
		new Window(args);
	}
	
	public Window(String... args){
		this.args = args;
		run();
	}

	@Override
	public void run() {
		SwingUtilities.invokeLater(() -> {
			frame = new Frame();
		});
		
	}
	
	
	
	class Frame extends JFrame {
		ConsoleDialog console;
		
		public Frame() {
			setVisible(true);
			console = new ConsoleDialog();		
			console.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

			System.setOut(console.getPrintStream());
			
			setTitle("TransGenesis");
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setExtendedState(getExtendedState() | MAXIMIZED_BOTH);
			console.setSize(getSize());
			console.setLocation(0, 128);
			console.setSize(getWidth(), getHeight());
			console.requestFocus();
			XMLPanel xmlPanel = new XMLPanel(this);
			xmlPanel.initialize(args);
			//xmlPanel.initialize("path:C:\\Users\\Alex\\workspace\\TransGenesis\\Testing Area\\Transcendence_Source\\StarsOfThePilgrim.xml");
			add(xmlPanel);
			pack();
			setExtendedState(getExtendedState() | MAXIMIZED_BOTH);
		}
		class ConsoleDialog extends JDialog {
			PrintStream stream;
			public ConsoleDialog() {
				super((JFrame) null, "TransGenesis Console", false);
				//setExtendedState(JFrame.MAXIMIZED_BOTH);
				JTextArea consoleText = XMLPanel.createTextArea("", false);
				add(XMLPanel.createScrollPane(consoleText));
				pack();
				setMaximumSize(Frame.this.getSize());
				setVisible(true);
				stream = new PrintStream(new TextAreaOutputStream(consoleText));
			}
			public PrintStream getPrintStream() {
				return stream;
			}
		}
		
		//Source Question: https://stackoverflow.com/questions/9776465/how-to-visualize-console-java-in-jframe-jpanel
		//Source Answer: https://stackoverflow.com/a/9776819
		public class TextAreaOutputStream extends OutputStream {
			PrintStream eclipse = System.out;
			private final JTextArea textArea;
			private final StringBuilder sb = new StringBuilder();
			public TextAreaOutputStream(final JTextArea textArea) {
				this.textArea = textArea;
			}
			public void flush() {}
			public void close() {}
			public void write(int b) throws IOException {
				eclipse.write(b);
				if (b == '\r')
					return;
				if (b == '\n') {
					final String text = new SimpleDateFormat("hh:mm:ss").format(new Date()) + "> " + sb.toString() + "\n";
					/*
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {   
						}
					});
					*/
					textArea.append(text);
					textArea.repaint();
					sb.setLength(0);
					return;
				}
				sb.append((char) b);
			}
		}
	}
}
