package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import mouse.DataProcessor;

public class ButtonsPanel extends JPanel implements ActionListener {

	private JButton openButton, saveButton, histButton;
	private JFileChooser fc;
	private JTextArea log;
	private File file = null;
	
	DataProcessor processor;
	
	
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
				file = fc.getSelectedFile();
				//This is where a real application would open the file.
				log.append("Opening: " + file.getName() + ".");
			} else {
				log.append("Open command cancelled by user.\n");
			}
			log.setCaretPosition(log.getDocument().getLength());
 		//Handle save button action.
		} else if (e.getSource() == saveButton) {
			if (file == null)
				return;
			log.append("Saving: " + file.getName() + ".\n");
			run(file.getName());
			log.setCaretPosition(log.getDocument().getLength());
		} else if (e.getSource() == histButton) {
			if (processor == null) {
				JOptionPane.showMessageDialog(getParent(), "First import the data.");
				return;
			}
			log.append("Generating the histogram...\n");
			
			HistogramFrame histFrame = new HistogramFrame(log, processor);
			histFrame.run();
		}

	}
	
	private void run(String inputFileName) {
		//Read the configuration data from the console
		//TODO: change the config reading from a file (XML?)
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String username;
		String password;
		String host;
		String port;
		String dbName;
		//try {
			System.out.println("Enter the input CSV file name");
			inputFileName = "data.csv";// br.readLine().trim();
			System.out.println("Enter the DB username");
			username = "vahan"; //br.readLine().trim();
			System.out.println("Enter the DB password");
			password = "123"; //br.readLine().trim();
			System.out.println("Enter the DB host name");
			host = "localhost"; //br.readLine().trim();
			System.out.println("Enter the DB port");
			port = "5432"; //br.readLine().trim();
			System.out.println("Enter the DB name");
			dbName = "mousedb"; //br.readLine().trim();
		/*} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}*/
		
		processor = new DataProcessor(inputFileName, username, password, host, port, dbName);
		if (!processor.getPsqlManager().connect(host, port, dbName)) {
			System.out.println("Could not connect to the DB at " + processor.getPsqlManager().getUrl());
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
		openButton = new JButton("Open a File");
		openButton.addActionListener(this);
		
		saveButton = new JButton("Import");
		saveButton.addActionListener(this);
		
		histButton = new JButton("Draw histogram");
		histButton.addActionListener(this);
		
		//For layout purposes, put the buttons in a separate panel
		JPanel buttonPanel = new JPanel(); //use FlowLayout
		buttonPanel.add(openButton);
		buttonPanel.add(saveButton);
		buttonPanel.add(histButton);
		
		//Add the buttons and the log to this panel.
		add(buttonPanel, BorderLayout.PAGE_START);
	}
	

}
