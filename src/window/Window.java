package window;

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

public class Window implements Runnable {
	String file_name = "0.txt";
	File file;
	Writer writer;
	BufferedReader reader;
	JFrame frame;
	
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void delete()
	{
		try {
			Files.delete(Paths.get(file_name));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void close()
	{
		System.exit(0);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		frame = new JFrame();
		frame.setTitle("TransGenesis");
		frame.setSize(1920, 1080);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		frame.pack();
		frame.setVisible(true);
	}

}
