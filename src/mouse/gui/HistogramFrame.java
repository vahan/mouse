package mouse.gui;

import java.awt.Color;
import java.awt.Font;
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
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.Layer;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.TextAnchor;


import mouse.dataProcessing.DataProcessor;
import mouse.dbTableRows.DbTableRow;
import mouse.dbTableRows.MeetingResultRow;
import mouse.dbTableRows.StayResultRow;
import mouse.postgresql.MeetingResults;
import mouse.postgresql.StayResults;

public class HistogramFrame extends JFrame implements Runnable {

	private JTextArea log;
	private JPanel meetingsPanel;
	private JPanel staysPanel;
	private final int intervalsNumber;

	private DataProcessor processor;

	/**
	 * 
	 */
	private static final long serialVersionUID = 8110266043635101267L;

	public HistogramFrame(JTextArea log, DataProcessor processor,
			int intervalsNumber) {
		super();
		this.log = log;
		this.processor = processor;
		this.intervalsNumber = intervalsNumber;
		init();
	}

	private void init() {
		meetingsPanel = new JPanel();
		getContentPane().add(meetingsPanel);
		staysPanel = new JPanel();
		getContentPane().add(staysPanel);
	}

	public int getIntervalSize() {
		return intervalsNumber;
	}

	private void drawHistogram(JPanel parentPanel, IntervalXYDataset dataset) {
		JFreeChart chart = createChart(dataset);
		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		parentPanel.add(chartPanel);
	}

	/**
	 * Creates a sample dataset.
	 * 
	 * @return A sample dataset.
	 */
	private IntervalXYDataset meetingsDataset() {
		final XYSeries series = new XYSeries("Data");
		MeetingResults meetingResults = processor.getPsqlManager()
				.getMeetingResults();
		DbTableRow[] rows = meetingResults.getTableModels();
		HashMap<Long, Integer> histData = new HashMap<Long, Integer>();
		long min = meetingResults.getMinDuration();
		long max = meetingResults.getMaxDuration();
		long h = (max - min) / intervalsNumber;

		for (int i = 0; i < rows.length; ++i) {
			MeetingResultRow meetingRow = (MeetingResultRow) rows[i];
			long dur = meetingRow.getDuration();
			long interval = min
					+ (Math.min(Math.max(dur / h - 1, 0), intervalsNumber - 1))
					* h;
			if (histData.containsKey(interval)) {
				histData.put(interval, histData.get(interval) + 1);
			} else {
				histData.put(interval, 1);
			}
		}

		for (Long interval : histData.keySet()) {
			series.add(interval, histData.get(interval));
		}

		final XYSeriesCollection dataset = new XYSeriesCollection(series);
		return dataset;
	}

	private IntervalXYDataset specialStaysDataset() {
		final XYSeries series = new XYSeries("Data");
		StayResults stayResults = processor.getPsqlManager().getStayResults();
		DbTableRow[] rows = stayResults.getTableModels();
		HashMap<Long, Integer> histData = new HashMap<Long, Integer>();
		long min = processor.getSettings().getMinBoxTime();
		long max = processor.getSettings().getMaxBoxTime();
		long h = (max - min) / intervalsNumber;

		for (int i = 0; i < rows.length; ++i) {
			StayResultRow stayRow = (StayResultRow) rows[i];
			long dur = stayRow.getDuration();
			long interval = min
					+ (Math.min(Math.max(dur / h - 1, 0), intervalsNumber - 1))
					* h;
			if (histData.containsKey(interval)) {
				histData.put(interval, histData.get(interval) + 1);
			} else {
				histData.put(interval, 1);
			}
		}

		for (Long interval : histData.keySet()) {
			series.add(interval, histData.get(interval));
		}

		final XYSeriesCollection dataset = new XYSeriesCollection(series);
		return dataset;
	}

	/**
	 * Creates a sample chart.
	 * 
	 * @param dataset
	 *            the dataset.
	 * 
	 * @return A sample chart.
	 */
	private JFreeChart createChart(IntervalXYDataset dataset) {
		final JFreeChart chart = ChartFactory.createHistogram("Histogram",
				"Durations", "Number of occurancies", dataset,
				PlotOrientation.VERTICAL, true, true, false);
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

		drawHistogram(meetingsPanel, meetingsDataset());
		drawHistogram(staysPanel, specialStaysDataset());
		pack();
		setVisible(true);

		log.append("Histogram was generated");
	}

}
