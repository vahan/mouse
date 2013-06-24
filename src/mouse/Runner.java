package mouse;

import java.lang.reflect.InvocationTargetException;

import gui.MainWindow;


/**
 * Contains the void main() method to run the program
 * @author vahan
 *
 */
public class Runner {
	
	public static MainWindow mainWindow;
	
	public static void main(String[] args) throws InvocationTargetException, InterruptedException {

		mainWindow = new MainWindow();
		javax.swing.SwingUtilities.invokeAndWait(mainWindow);
		
	}

}
