package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import mouse.TimeStamp;

import dataProcessing.DataProcessor;

public class LogsPanel extends javax.swing.JPanel implements ActionListener {

	private JComboBox<String> logsCombo;

	private JButton deleteButton;

	private String selectedLog = null;

	private DataProcessor processor;

	public LogsPanel() {
		super();
		draw();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -6273694776583387120L;

	@SuppressWarnings("unchecked")
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(logsCombo)) {
			selectedLog = (String) ((JComboBox<String>) e.getSource())
					.getSelectedItem();
		} else if (e.getSource().equals(deleteButton)) {
			int n = JOptionPane.showConfirmDialog(getParent(),
					"Delete all entries imported at " + selectedLog,
					"An Inane Question", JOptionPane.YES_NO_OPTION);
			if (n == JOptionPane.YES_OPTION) {
				TimeStamp importedAt;
				try {
					importedAt = new TimeStamp(selectedLog,
							TimeStamp.getDbFormat());
				} catch (ParseException e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(getParent(), e1.getMessage());
					return;
				}
				processor.getPsqlManager().deleteLogEntries(importedAt);
				updateComboList();
			}
		}

	}

	public void setProcessor(DataProcessor processor) {
		this.processor = processor;
		updateComboList();
	}

	private void updateComboList() {
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
