package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import mouse.postgresql.Settings;

import dataProcessing.DataProcessor;
import dataProcessing.XmlReader;


public class ButtonsPanel extends JPanel implements ActionListener {

	private JButton openButton, importButton, histButton, settingsButton;
	private JFileChooser fc;
	private JTextArea log;
	private File sourceFile = null;
	private File settingsFile = null;
	
	DataProcessor processor = null;
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1495818979583613856L;

	
	public ButtonsPanel(JTextArea log) {
		super(new BorderLayout());
		this.log = log;
		init();
	}
	
	public DataProcessor getProcessor() {
		return processor;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		//Handle open button action.
		if (e.getSource() == openButton) {
			int returnVal = fc.showOpenDialog(ButtonsPanel.this);
 			if (returnVal == JFileChooser.APPROVE_OPTION) {
				sourceFile = fc.getSelectedFile();
				//This is where a real application would open the file.
				log.append("Chosen source file: " + sourceFile.getName() + "\n");
			} else {
				log.append("Open command cancelled by user\n");
			}
			log.setCaretPosition(log.getDocument().getLength());
 		//Handle save button action.
		} else if (e.getSource() == importButton) {
			if (sourceFile == null)
				return;
			log.append("Importing: " + sourceFile.getName() + "\n");
			run(sourceFile.getPath());
		} else if (e.getSource() == histButton) {
			if (settingsFile == null) {
				JOptionPane.showMessageDialog(getParent(), "First import settings.");
				return;
			}
			if (processor == null) {
				JOptionPane.showMessageDialog(getParent(), "First import the data.");
				return;
			}
			log.append("Generating the histogram...\n");
			
			HistogramFrame histFrame = new HistogramFrame(log, processor);
			histFrame.run();
		} else if (e.getSource() == settingsButton) {
			int returnVal = fc.showOpenDialog(ButtonsPanel.this);
 			if (returnVal == JFileChooser.APPROVE_OPTION) {
 				settingsFile = fc.getSelectedFile();
				//This is where a real application would open the file.
				log.append("Opening: " + settingsFile.getName() + ".");
			} else {
				log.append("Open command cancelled by user.\n");
			}
			log.setCaretPosition(log.getDocument().getLength());
		}

	}
	
	private void run(String inputFileName) {
		if (settingsFile == null) {
			System.out.println("No Settings file was given");
			return;
		}
		
		XmlReader reader = new XmlReader();
		Settings settings = reader.importSettingsFromXml(settingsFile.getPath());
		processor = new DataProcessor(inputFileName, settings);
		if (!processor.getPsqlManager().connect()) {
			System.out.println("Could not connect to the DB at " + processor.getPsqlManager().getSettings().getUrl());
			return;
		}
		if (!processor.process()) {
			System.out.println("An error accurred! Please check the above error messages");
			return;
		}
		
		System.out.println("The data was successfully read, processed and stored in DB");
	}
	
	private void init() {
		
		fc = new JFileChooser();
		
		settingsButton = new JButton("Choose Settings File");
		settingsButton.addActionListener(this);
		
		openButton = new JButton("Choose Data File");
		openButton.addActionListener(this);
		
		importButton = new JButton("Import");
		importButton.addActionListener(this);
		
		histButton = new JButton("Draw Histogram");
		histButton.addActionListener(this);
		
		//For layout purposes, put the buttons in a separate panel
		JPanel buttonPanel = new JPanel(); //use FlowLayout
		buttonPanel.add(settingsButton);
		buttonPanel.add(openButton);
		buttonPanel.add(importButton);
		buttonPanel.add(histButton);
		
		//Add the buttons and the log to this panel.
		add(buttonPanel, BorderLayout.PAGE_START);
	}
	

}
