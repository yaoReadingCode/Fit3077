package view;

import java.awt.BorderLayout;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
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

/**
 * Creates the Bar chart based on the dataset passed to it
 * @author sriram
 *
 */

public class BarChart extends GraphAbstractionWindow {

	private JPanel contentPane;
	

	/**
	 * Create the frame.
	 * ref-> https://www.youtube.com/watch?v=RUDrjqywD1g
	 */
	public BarChart(String windowName) {
		// setting window name
		super(windowName);
		//setting frame size 
		this.setSize(1600, 1000);
		// setting the bounds
		setBounds(100, 100, 450, 300);
		
		//Setting panel properties
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.LIGHT_GRAY);
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		panel.add(graphPanel);
		graphPanel.setLayout(new BoxLayout(graphPanel, BoxLayout.X_AXIS));

		
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
		
		// creates a Bar chart with the passed data set
		JFreeChart jchart= ChartFactory.createBarChart(title, xAxis, yAxis, dataSet, PlotOrientation.VERTICAL,true,true,false);

		CategoryPlot plot= jchart.getCategoryPlot();
		
		CategoryAxis axis = jchart.getCategoryPlot().getDomainAxis();
		//displays the x axis names in 45 degree angles so that the names of the patient dont occupy a lot of spce vertically
		axis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
        
		BarRenderer renderer= (BarRenderer)plot.getRenderer();
		DecimalFormat decimalFormat= new DecimalFormat("##.##");
		
		// ref -> http://www.jfree.org/forum/viewtopic.php?t=27979
		// to display the values of the bar chart on top of each bar
		renderer.setSeriesItemLabelGenerator(0, new StandardCategoryItemLabelGenerator());
		renderer.setSeriesItemLabelsVisible(1, true);
		renderer.setBaseItemLabelsVisible(true);
		renderer.setBaseSeriesVisible(true);
		jchart.getCategoryPlot().setRenderer(renderer);
		
		// setting color
		plot.setRangeGridlinePaint(Color.black); 
		// creating a chart frame
		ChartFrame chartFrm= new ChartFrame("Patient Monitor",jchart,true);
		//turning visiblity to false 
		chartFrm.setVisible(false);
		//setting the size of the frame 
		chartFrm.setSize(800,800);
		
		ChartPanel chartPanel= new ChartPanel(jchart);
		
		graphPanel.removeAll();
		//adding the chart panel to the graph panel
		graphPanel.add(chartPanel);
		//update the UI 
		graphPanel.updateUI();

	}

}
