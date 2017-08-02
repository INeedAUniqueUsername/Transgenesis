package window;

import java.awt.Font;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.File;
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
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

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
	public enum Fonts {
		Title(new Font("Consolas", Font.BOLD, 32)),
		Large(new Font("Consolas", Font.BOLD, 24)),
		Medium(new Font("Consolas", Font.PLAIN, 20));
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
	ConsoleFrame console;
	static Window instance;
	
	public static void main(String[] args) {
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
			console = new ConsoleFrame();
			frame = new Frame();
		});
		
	}
	class ConsoleFrame extends JFrame {
		JTextArea consoleText;
		public ConsoleFrame() {
			super("TransGenesis Console");
			setExtendedState(JFrame.MAXIMIZED_BOTH);
			consoleText = XMLPanel.createTextArea("", false);
			add(XMLPanel.createScrollPane(consoleText));
			pack();
			setVisible(true);
			System.setOut(new PrintStream(new TextAreaOutputStream(consoleText)));
		}
	}
	
	//Source Question: https://stackoverflow.com/questions/9776465/how-to-visualize-console-java-in-jframe-jpanel
	//Source Answer: https://stackoverflow.com/a/9776819
	public class TextAreaOutputStream extends OutputStream {

		   private final JTextArea textArea;
		   private final StringBuilder sb = new StringBuilder();
		   public TextAreaOutputStream(final JTextArea textArea) {
		      this.textArea = textArea;
		   }
		   public void flush() {}
		   public void close() {}
		   public void write(int b) throws IOException {
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
