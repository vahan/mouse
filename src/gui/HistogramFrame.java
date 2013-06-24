package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.Layer;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.TextAnchor;

import mouse.DataProcessor;
import mouse.postgresql.MeetingResults;

public class HistogramFrame extends JFrame implements ActionListener, Runnable {

	private JTextArea log;
	private JPanel histPanel;
	
	private int intervalsNumber = 100;
	
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
		return intervalsNumber;
	}
	
	
	public void setIntervalSize(int intervalSize) {
		this.intervalsNumber = intervalSize;
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
	private void drawHistogram(JPanel parentPanel) {
		IntervalXYDataset dataset = createDataset();
		JFreeChart chart = createChart(dataset);
		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		getContentPane().add(chartPanel);
		
	}
	

	/**
	 * Creates a sample dataset.
	 * 
	 * @return A sample dataset.
	 */
	private IntervalXYDataset createDataset() {
		final XYSeries series = new XYSeries("Data");
		MeetingResults meetingResults = processor.getPsqlManager().getMeetingResults();
		HashMap<Long, Integer> data = meetingResults.histData(intervalsNumber);
		
		for (Long key : data.keySet()) {
			series.add(key, data.get(key));
		}
		
		final XYSeriesCollection dataset = new XYSeriesCollection(series);
		return dataset;
	}

	/**
	 * Creates a sample chart.
	 * 
	 * @param dataset  the dataset.
	 * 
	 * @return A sample chart.
	 */
	private JFreeChart createChart(IntervalXYDataset dataset) {
		final JFreeChart chart = ChartFactory.createXYBarChart(
			"XY Series Demo",
			"X", 
			false,
			"Y", 
			dataset,
			PlotOrientation.VERTICAL,
			true,
			true,
			false
		);
		XYPlot plot = (XYPlot) chart.getPlot();
		final IntervalMarker target = new IntervalMarker(400.0, 700.0);
		target.setLabel("Target Range");
		target.setLabelFont(new Font("SansSerif", Font.ITALIC, 11));
		target.setLabelAnchor(RectangleAnchor.LEFT);
		target.setLabelTextAnchor(TextAnchor.CENTER_LEFT);
		target.setPaint(new Color(222, 222, 255, 128));
		plot.addRangeMarker(target, Layer.BACKGROUND);
		return chart;
	}
	

	@Override
	public void run() {
		setTitle("Histogram");
		setSize(1000, 400);
		setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
		
		pack();
		setVisible(true);
		drawHistogram(histPanel);
	}

}