import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;


public class CountWindow {
	final static String [] question_pace_options = {"go slower", "remain the same", "go faster"};
	final static String [] question_course_options = {"have less structure", "stay about the same", "have more structure"};
	final static String [] question_useful_options = {"more discussion in general", "the same amount of discussion", "less discussion in general"};
	final static String [] question_hw_options = {"are too many", "are just right", "are too few"};
	private Map<String, Integer> counts = new HashMap<String, Integer>();
	
	public CountWindow(){
		for (int i = 0; i < question_pace_options.length; i++ )
			counts.put(question_pace_options[i].toLowerCase(), 0);
		for (int i = 0; i < question_course_options.length; i++ )
			counts.put(question_course_options[i].toLowerCase(), 0);
		for (int i = 0; i < question_useful_options.length; i++ )
			counts.put(question_useful_options[i].toLowerCase(), 0);
		for (int i = 0; i < question_hw_options.length; i++)
			counts.put(question_hw_options[i].toLowerCase(), 0);
	}
	
	public void setCounts(Map<String, Integer> c){
		counts = c;
	}
	public Map<String, Integer> getCounts(){
		return counts;
		
	}
	
	public void count(ArrayList<String> values){
		System.out.println(counts);
		for (int i = 0; i < values.size(); i++){
			System.out.println(values.get(i).toLowerCase());
			int x = counts.get(values.get(i).toLowerCase());
			x++;
			counts.put(values.get(i).toLowerCase(), x);
		}
	}

	public void createAndShowGUI() {
		//System.out.println(counts);
		JFrame frame = new JFrame("Counts to MC Questions");
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		for ( int j = 2; j < 6; j++ ){
			panel.add(new JLabel(Analyze.questions[j]), BorderLayout.WEST);
			String [] options = null;
			switch(j){
			case 2: options = question_pace_options;
				break;
			case 3: options = question_course_options;
				break;
			case 4: options = question_useful_options;
				break;
			case 5: options = question_hw_options;
				break;
			}
			// number of options in that question is three
			int sum = 0;
			for (int i = 0; i < 4; i++){
				JTextArea text = new JTextArea();
				text.setEditable(false);
				if (i == 3){
					text.setText("Total: " + sum);
				}
				else{
					sum += counts.get(options[i]);
					text.setText(options[i] + ": " + counts.get(options[i]));
				}
				panel.add(text);
				
			}
		}
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
		/*
		PieChart demo = new PieChart("Demo");
		demo.pack();
		demo.setVisible(true);
		*/
	}
}