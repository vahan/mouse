package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import mouse.DataProcessor;
import mouse.postgresql.MeetingResults;

public class HistogramFrame extends JFrame implements ActionListener, Runnable {

	private JTextArea log;
	private JPanel histPanel;
	
	private int intervalSize = 10;
	
	private DataProcessor processor;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8110266043635101267L;
	
	
	public HistogramFrame(JTextArea log, DataProcessor processor) {
		super();
		this.log = log;
		this.processor = processor;
		init();
	}
	
	private void init() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		histPanel = new JPanel();
		getContentPane().add(histPanel);
	}

	public int getIntervalSize() {
		return intervalSize;
	}
	
	
	public void setIntervalSize(int intervalSize) {
		this.intervalSize = intervalSize;
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
	private void drawHistogram(JPanel parentPanel) {
		MeetingResults meetingResults = processor.getPsqlManager().getMeetingResults();
		HashMap<Long, Integer> data = meetingResults.histData(intervalSize);
		
		
	}

	@Override
	public void run() {
		setTitle("Histogram");
		setSize(1000, 400);
		setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
		
		drawHistogram(histPanel);
		pack();
		setVisible(true);
		
	}

}
