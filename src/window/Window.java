package window;

import java.awt.Font;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JTextField;

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
	FrameOld frame;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Window window = new Window();
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
		frame = new FrameOld();
	}

}
