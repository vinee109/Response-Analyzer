import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;


public class PieChart{
	String question;
	private Map<String, Integer> counts;
	String [] options;

    public PieChart(String t, Map<String, Integer> c, String [] o) {
        question = t;
        counts = c;
        options = o;
    }

    private PieDataset createDataset() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        for (int i = 0; i < options.length; i++){
        	dataset.setValue(options[i], new Double(counts.get(options[i])));
        }
        return dataset;        
    }
    
    private JFreeChart createChart(PieDataset dataset) {
        
        JFreeChart chart = ChartFactory.createPieChart(
            question,             // chart title
            dataset,             // data
            true,               // include legend
            true,
            false
        );
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        plot.setNoDataMessage("No data available");
        plot.setCircular(true);
        plot.setLabelGap(0.02);
        return chart;    
    }
    public void makeImage(int width, int height) throws IOException{
    	JFreeChart chart = createChart(createDataset());
    	ChartUtilities.saveChartAsJPEG(new File("chart.jpg"), chart, 100, 100);
    	//chart.setTitle(new TextTitle(question));
    	//System.out.println(chart.getTitle().getText());
    	
    }
	/*
    public void createImage(){
    	BufferedImage objBufferedImage = createChart(createDataset()).createBufferedImage(300,300);
    	ByteArrayOutputStream bas = new ByteArrayOutputStream();
        try {
            ImageIO.write(objBufferedImage, "gif", bas);
        } catch (IOException e) {
            e.printStackTrace();
        }
    	byte[] byteArray=bas.toByteArray();
    	InputStream in = new ByteArrayInputStream(byteArray);
    	BufferedImage image;
		try {
			image = ImageIO.read(in);
			File outputfile = new File("chart.gif");
	    	ImageIO.write(image, "gif", outputfile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   	
    }
    */

    public JPanel createDemoPanel() {
        JFreeChart chart = createChart(createDataset());
        return new ChartPanel(chart);
    }
    
    public void createAndShowGUI(){
    	JFrame frame = new JFrame();
    	frame.setContentPane(createDemoPanel());
    	frame.pack();
    	frame.setVisible(true);
    }
}
