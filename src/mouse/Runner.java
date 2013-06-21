package mouse;

import gui.MainWindow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Contains the void main() method to run the program
 * @author vahan
 *
 */
public class Runner {
	
	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				//Turn off metal's use of bold fonts
				UIManager.put("swing.boldMetal", Boolean.FALSE); 
				new MainWindow();
			}
		});
		
	}

}
