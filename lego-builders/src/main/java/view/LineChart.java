package view;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;



import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

import model.FHIRPatient;
import model.Observation;
import model.Tuple;

/**
 * Creates the line chart based on the dataset passed to it
 * @author sriram
 *
 */

public class LineChart extends GraphAbstractionWindow {
	
	/**
	 * Creates the frame
	 * ref-> https://www.youtube.com/watch?v=RUDrjqywD1g
	 */
	public LineChart(String windowName) {
		super(windowName);
		// adding the graph panel to the frame
		this.add(graphPanel);
		// setting the layout of the panel
		graphPanel.setLayout(new FlowLayout());
		
		// adding a scroll bar 
		JScrollPane pane = new JScrollPane(graphPanel,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.setContentPane(pane);
		
	}
	
	/**
	 * Helps to render the chart using the given data set
	 * @param dataSet set of data that needs to be plotted 
	 * @param title title of the chart
	 * @param xAxis x axis of the chart
	 * @param yAxis y axis of the chart
	 */
	@Override
	public void renderChart(DefaultCategoryDataset dataSet, String title,String xAxis, String yAxis) {
		
		// creates a line chart
		JFreeChart jchart= ChartFactory.createLineChart(title,xAxis, yAxis, dataSet, PlotOrientation.VERTICAL,true,true,false);
		CategoryPlot plot= jchart.getCategoryPlot();
		// setting color
		plot.setRangeGridlinePaint(Color.black); 
		// creating a chart frame
		ChartFrame chartFrm= new ChartFrame("",jchart,true);
		//turning visiblity to false 
		chartFrm.setVisible(false);
		//setting the size of the frame 
		chartFrm.setSize(800,800);
		ChartPanel chartPanel= new ChartPanel(jchart);
		//adding the chart panel to the graph panel
		graphPanel.add(chartPanel);
		//update the UI 
		graphPanel.updateUI();

	}
	/**
	 * 
	 * @return the panel on which the graph is rendered on 
	 */
	public JPanel getGraphPanel() {
		return graphPanel;
	}

}
