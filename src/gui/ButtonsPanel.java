package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import mouse.Settings;
import mouse.TimeStamp;

import dataProcessing.DataProcessor;
import dataProcessing.XmlReader;

public class ButtonsPanel extends JPanel implements ActionListener, Observer {

	private JButton openButton, boxDataButton, importButton, histButton,
			settingsButton, resetButton;
	private JFileChooser fc;
	private JTextArea log;
	private File sourceFile = null;
	private File settingsFile = null;
	private String boxDataFileName = null;

	/**
	 * 
	 */
	private static final long serialVersionUID = -1495818979583613856L;

	public ButtonsPanel(JTextArea log) {
		super(new BorderLayout());
		this.log = log;
		init();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// Handle open button action.
		if (e.getSource() == openButton) {
			int returnVal = fc.showOpenDialog(ButtonsPanel.this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				sourceFile = fc.getSelectedFile();
				// This is where a real application would open the file.
				log.append("Chosen source file: " + sourceFile.getName() + "\n");
			} else {
				log.append("Open command cancelled by user\n");
			}
			log.setCaretPosition(log.getDocument().getLength());
		} else if (e.getSource() == boxDataButton) {
			int returnVal = fc.showOpenDialog(ButtonsPanel.this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				boxDataFileName = fc.getSelectedFile().getPath();
				// This is where a real application would open the file.
				log.append("Chosen source file: " + boxDataFileName + "\n");
			} else {
				log.append("Open command cancelled by user\n");
			}
			log.setCaretPosition(log.getDocument().getLength());
		} else if (e.getSource() == importButton) {
			if (sourceFile == null || boxDataFileName == null)
				return;
			log.append("Importing: " + sourceFile.getName() + "\n");
			try {
				run(sourceFile.getPath(), boxDataFileName, false);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				JOptionPane.showMessageDialog(this, "dafuq did just happenn?");
			}
		} else if (e.getSource() == histButton) {
			if (settingsFile == null) {
				JOptionPane.showMessageDialog(getParent(),
						"First import settings.");
				return;
			}
			if (MainWindow.getInstance().getProcessor() == null) {
				JOptionPane.showMessageDialog(getParent(),
						"First import the data.");
				return;
			}
			log.append("Generating the histogram...\n");
			XmlReader reader = new XmlReader();
			Settings settings = reader.importSettingsFromXml(settingsFile
					.getPath());
			if (settings == null)
				return;
			HistogramFrame histFrame = new HistogramFrame(log, MainWindow
					.getInstance().getProcessor(),
					settings.getIntervalsNumber());
			histFrame.run();
		} else if (e.getSource() == settingsButton) {
			int returnVal = fc.showOpenDialog(ButtonsPanel.this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				settingsFile = fc.getSelectedFile();
				// This is where a real application would open the file.
				log.append("Opening: " + settingsFile.getName() + ".");
			} else {
				log.append("Open command cancelled by user.\n");
			}
			log.setCaretPosition(log.getDocument().getLength());
		} else if (e.getSource() == resetButton) {
			int confirm = JOptionPane.showConfirmDialog(getParent(),
					"Are you sure, you want to reset the entire Database!?",
					"An Inane Question", JOptionPane.YES_NO_OPTION);
			if (confirm == JOptionPane.YES_OPTION) {
				if (sourceFile == null || boxDataFileName == null)
					return;
				log.append("Reseting the DB and importing: "
						+ sourceFile.getName() + "\n");
				try {
					run(sourceFile.getPath(), boxDataFileName, true);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(this,
							"dafuq did just happenn?");
					e1.printStackTrace();
				}
			}
		}

	}

	private void run(String inputFileName, String boxDataFileName, boolean reset)
			throws InterruptedException {
		if (settingsFile == null) {
			JOptionPane.showMessageDialog(this, "No Settings file was given");
			return;
		}

		XmlReader reader = new XmlReader();
		Settings settings = reader
				.importSettingsFromXml(settingsFile.getPath());
		if (settings == null)
			return;

		TimeStamp.setDateFormats(settings.getCsvDateFormat(),
				settings.getDbDateFormat());

		DataProcessor processor = DataProcessor.getInstance();
		processor.init(inputFileName, boxDataFileName, settings, reset);
		processor.addObserver(this);
		if (!processor.getPsqlManager().connect()) {
			JOptionPane
					.showMessageDialog(this, "Could not connect to the DB at "
							+ processor.getPsqlManager().getSettings().getUrl());
			return;
		}
		// TODO Doesn't update the log continuously, but rather show the entire
		// log after process() is done.
		Thread thr = new Thread(processor);
		thr.start();
		try {
			thr.join();
		} catch (InterruptedException e) {
			JOptionPane.showMessageDialog(this, "dafuq did just happen?");
			e.printStackTrace();
			return;
		}
		// TODO is supposed to fix the previous todo, but doesn't!
		/*
		 * while (!processor.isFinished()) { synchronized
		 * (processor.getMessage()) { try { this.wait(); } catch
		 * (InterruptedException e1) { JOptionPane.showMessageDialog(this,
		 * "dafuq did just happenn?"); } } if (!processor.getSuccess()) {
		 * JOptionPane.showMessageDialog(this,
		 * "An error accurred! Please check the above error messages\n");
		 * return; } }
		 */
		MainWindow.getInstance().setProcessor(processor);
		if (!processor.getSuccess()) {
			log.append("The processing has failed!");
			return;
		}
		log.append("Like a sir\n");
		JOptionPane.showMessageDialog(this,
				"The data was successfully read, processed and stored in DB\n");
	}

	private void init() {

		fc = new JFileChooser();

		settingsButton = new JButton("Choose Settings File");
		settingsButton.addActionListener(this);

		openButton = new JButton("Choose Data File");
		openButton.addActionListener(this);

		boxDataButton = new JButton("Choose Boxes Data File");
		boxDataButton.addActionListener(this);

		importButton = new JButton("Import");
		importButton.addActionListener(this);

		histButton = new JButton("Draw Histogram");
		histButton.addActionListener(this);

		resetButton = new JButton("Reset the DB and Import");
		resetButton.addActionListener(this);

		// For layout purposes, put the buttons in a separate panel
		JPanel buttonPanel = new JPanel(); // use FlowLayout
		buttonPanel.add(settingsButton);
		buttonPanel.add(openButton);
		buttonPanel.add(boxDataButton);
		buttonPanel.add(importButton);
		buttonPanel.add(histButton);
		buttonPanel.add(resetButton);

		// Add the buttons and the log to this panel.
		add(buttonPanel, BorderLayout.PAGE_START);
	}

	@Override
	public void update(Observable o, Object arg) {
		DataProcessor processor = (DataProcessor) o;
		log.append(processor.getMessage() + "\n");

		log.setVisible(true);
		log.updateUI();

	}

}
