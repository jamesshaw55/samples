package org.jamesshaw.samples.jfreechart;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleInsets;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Example of a time series chart that updates dynamically every second. You can provide your own data source in the acquireDataMethod.
 */
public class UpdatingTimeSeries {


    public static void main(String[] args) {

        UpdatingTimeSeries updatingTimeSeries = new UpdatingTimeSeries();
        updatingTimeSeries.run();

    }

    private void run() {


        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        // This is a little shortcut that results in the window being centred
        frame.setLocationRelativeTo(null);
        frame.setTitle("Updating time series JFreeChart");

        final TimeSeriesCollection dataset = new TimeSeriesCollection();
        JFreeChart chart = createChart(dataset);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setFillZoomRectangle(true);
        chartPanel.setMouseWheelEnabled(true);

        frame.getContentPane().add(chartPanel);

        frame.setVisible(true);

        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                // Wrap in invoke later as we'll be potentially updating the gui based on model changes
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        updateChartWithNewData(dataset);
                    }
                });
            }
        };

        timer.schedule(timerTask, 1000, 1000);


    }

    private JFreeChart createChart(XYDataset dataset) {

        JFreeChart chart = ChartFactory.createTimeSeriesChart("Updating time series chart", "Date", "Value", dataset, true, true, false);

        chart.setBackgroundPaint(Color.white);
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);

        XYItemRenderer r = plot.getRenderer();
        if (r instanceof XYLineAndShapeRenderer) {
            XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
            renderer.setBaseShapesVisible(true);
            renderer.setBaseShapesFilled(true);
            renderer.setDrawSeriesLineAsPath(true);
        }

        DateAxis axis = (DateAxis) plot.getDomainAxis();
        axis.setDateFormatOverride(new SimpleDateFormat("MMM-yyyy-dd HH:mm:ss"));

        return chart;

    }

    private void updateChartWithNewData(TimeSeriesCollection dataset) {

        String[] series = new String[]{"A", "B", "C"};

        // If you want to get your time from somewhere else, change this line
        long timeNow = System.currentTimeMillis();

        Random random = new Random();

        Date now = new Date(timeNow);

        for (String seriesKey : series) {
            double value = 10 * random.nextInt(10);
            TimeSeries dataSeries = dataset.getSeries(seriesKey);
            if (dataSeries == null) {
                dataSeries = new TimeSeries(seriesKey);
                dataset.addSeries(dataSeries);
            }


            dataSeries.add(new Second(now), value);
        }

    }

}
