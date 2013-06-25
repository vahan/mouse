package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import dataProcessing.DataProcessor;


public class LogsPanel extends javax.swing.JPanel implements ActionListener {

	private JComboBox<String> logsCombo;
	
	private JButton deleteButton;
	
	public LogsPanel(DataProcessor processor) {
		super();
		
		draw();
	}
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6273694776583387120L;

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}
	
	public void setProcessor(DataProcessor processor) {
		String[] logImportedAts = processor.getPsqlManager().getLogEntries();
		logsCombo.removeAllItems();
		for (String item : logImportedAts) {
			logsCombo.addItem(item);
		}
		logsCombo.setVisible(true);
		logsCombo.updateUI();
	}
	
	
	
	private void draw() {
		logsCombo = new JComboBox<String>();
		logsCombo.addActionListener(this);
		
		deleteButton = new JButton("Delete the Log");
		deleteButton.addActionListener(this);
		
		JPanel logsPanel = new JPanel();
		logsPanel.add(logsCombo);
		logsPanel.add(deleteButton);
		
		add(logsPanel, BorderLayout.PAGE_START);
		
		
		
	}

}
