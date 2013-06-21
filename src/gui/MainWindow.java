package gui;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class MainWindow extends JFrame {

	private String inputFileName;
	
	private final JFileChooser fc = new JFileChooser();
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2740437090361841747L;
	
	
	public MainWindow() {
		super("Mouse Experimenter");
		
		draw();
	}
	
	
	private void draw() {
		//Create and set up the window.
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Add content to the window.
		add(new FileChooserPanel());

		//Display the window.
		pack();
		setVisible(true);
		
	}

}
