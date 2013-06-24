package gui;

import java.awt.BorderLayout;
import java.awt.Insets;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class MainWindow extends JFrame implements Runnable {

	private JTextArea log;
	
	private FileChooserPanel fileChooserPanel;
	
	private HistogramPanel histPanel;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2740437090361841747L;
	
	
	public MainWindow() {
		super();
		
	}
	
	
	private void draw() {
		//Create and set up the window.
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Create the log first, because the action listeners
		//need to refer to it.
		log = new JTextArea(5,20);
		log.setMargin(new Insets(5,5,5,5));
		log.setEditable(false);
		JScrollPane logScrollPane = new JScrollPane(log);

		fileChooserPanel = new FileChooserPanel(log);
		histPanel = new HistogramPanel(log, fileChooserPanel.processor.getPsqlManager().getMeetingResults());
		//Add content to the window.
		getContentPane().add(fileChooserPanel);
		getContentPane().add(histPanel);
		getContentPane().add(logScrollPane, BorderLayout.CENTER);

		//Display the window.
		pack();
		setVisible(true);
		
	}


	@Override
	public void run() {
		setTitle("Mouse Experimenter");
		setSize(1000, 400);
		setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		draw();
		pack();
		setVisible(true);
	}

}
