package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import mouse.postgresql.MeetingResults;

public class HistogramPanel extends JPanel implements ActionListener {

	private JTextArea log;
	
	private JButton generateButton;
	
	private int intervalSize = 10;
	
	private MeetingResults meetingResults;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8110266043635101267L;
	
	
	public HistogramPanel(JTextArea log, MeetingResults meetingResults) {
		super(new BorderLayout());
		this.log = log;
		this.meetingResults = meetingResults;
		init();
	}
	
	public int getIntervalSize() {
		return intervalSize;
	}
	
	
	public void setIntervalSize(int intervalSize) {
		this.intervalSize = intervalSize;
	}
	
	
	private void init() {
		generateButton = new JButton("Draw histogram");
		generateButton.addActionListener(this);
		
		JPanel buttonPanel = new JPanel(); //use FlowLayout
		buttonPanel.add(generateButton);
		
		add(buttonPanel, BorderLayout.SOUTH);
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		//Handle open button action.
		if (e.getSource() == generateButton) {
			log.append("Generating the histogram...\n");
			
			JFrame histFrame = new JFrame("Histogram");
			JPanel histPanel = new JPanel();
			
			drawHistogram(histPanel);
			
			histFrame.getContentPane().add(histPanel);
			histFrame.pack();
			histFrame.setVisible(true);
			
			//TODO: generate and show the histogram
		}
		
	}
	
	
	private void drawHistogram(JPanel parentPanel) {
		HashMap<Long, Integer> data = meetingResults.histData(intervalSize);
		
		
	}

}
