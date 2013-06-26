package gui;

import java.awt.BorderLayout;
import java.awt.Insets;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import dataProcessing.DataProcessor;

public class MainWindow extends JFrame implements Runnable {

	private JTextArea log;
	
	private ButtonsPanel buttonsPanel;
	
	private LogsPanel deleteLogsPanel;
	
	private static MainWindow window = null;
	
	DataProcessor processor = null;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2740437090361841747L;
	
	
	private MainWindow() {
		super();
		
	}
	
	
	public static MainWindow getInstance() {
		if (window == null) {
			window = new MainWindow();
		}
		return window;
	}
	
	public DataProcessor getProcessor() {
		return processor;
	}
	
	public void setProcessor(DataProcessor processor) {
		this.processor = processor;
		deleteLogsPanel.setProcessor(processor);
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

		buttonsPanel = new ButtonsPanel(log);
		deleteLogsPanel = new LogsPanel();
		//Add content to the window.
		getContentPane().add(buttonsPanel);
		getContentPane().add(logScrollPane, BorderLayout.CENTER);
		getContentPane().add(deleteLogsPanel);

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
