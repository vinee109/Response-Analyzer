import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;


public class CheckListWindow implements ActionListener{
	JFrame frame = new JFrame("Pick the comments you want to show up on your report");
	JPanel mainPanel = new JPanel();
	Map<String, Integer> counts;
	Map<String, HashMap<String, ArrayList<String>>> data;
	Map<String, HashMap<String, ArrayList<String>>> examples = new HashMap<String, HashMap<String, ArrayList<String>>>();
	Map<String, String> exampleKey = new HashMap<String, String>();
	JButton genButton = new JButton("Generate Report");
	ArrayList<JCheckBox> checkList = new ArrayList<JCheckBox>();
	private CountWindow countWindow;
	String [] order = new String [4];
	
	public CheckListWindow(Map<String, Integer> c, Map<String, HashMap<String, ArrayList<String>>> d, CountWindow co){
		counts = c;
		data = d;
		genButton.addActionListener(this);
		countWindow = co;
	}
	
	public String [] topTwo(String sign){
		ArrayList<String> categories = new ArrayList<String>(counts.keySet());
		ArrayList<String> compare = new ArrayList<String>();
		for ( int i = 0; i < categories.size(); i++ ){
			if (categories.get(i).contains(sign))
				compare.add(categories.get(i));
		}
		String [] ret = new String [2];
		for ( int x = 0; x < 2; x++ ){
			int largest = 0;
			for (int i = 1; i < compare.size(); i++){
				if ( counts.get(compare.get(i)) > counts.get(compare.get(largest)))
					largest = i;
			}
			ret[x] = compare.get(largest);
			compare.remove(largest);
		}
		return ret;
	}
	public void createAndShowGUI(){
		addComponentsToPanel();
		frame.add(mainPanel);
		//frame.setSize(700, 700);
		JScrollPane jsp = new JScrollPane(mainPanel);
		frame.add(jsp);
		frame.setSize(700, 700);
		frame.setVisible(true);
	}
	
	public void addComponentsToPanel(){
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		// puts the categories from Categorizer class into a list;
		for ( int a = 0; a < 2; a++ ){
			String ending = "+";
			if (a == 1)
				ending = "-";
			String [] categories = topTwo(ending);
			// other categories
			for ( int i = 0; i < categories.length; i++){
				JLabel category = new JLabel(categories[i], JLabel.LEFT);
				category.setFont(new Font(category.getFont().getName(), category.getFont().getStyle(), 18));
				//panel containing the category name
				JPanel categoryPanel = new JPanel();
				categoryPanel.add(category);
				mainPanel.add(categoryPanel, BorderLayout.WEST);
				
				ArrayList<String> subcategories = new ArrayList<String>(data.get(categories[i]).keySet());
				for ( int k = 0; k < subcategories.size(); k++ ){
					ArrayList<String> responses = data.get(categories[i]).get(subcategories.get(k));
					//examples.get(categories[i]).put(subcategories.get(k), responses);
					if ( responses.size() > 0){
						JPanel subcategoryPanel = new JPanel();
						JLabel subcategory = new JLabel(subcategories.get(k));
						subcategory.setFont(new Font(subcategory.getFont().getName(), subcategory.getFont().getStyle(), 14));
						subcategoryPanel.add(subcategory);
						mainPanel.add(subcategoryPanel, BorderLayout.WEST);
						
						// one panel with all the checklists for each subcategory
						JPanel panel = new JPanel();
						panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
						for ( int x = 0; x < responses.size(); x++ ){
							JCheckBox checkBox = new JCheckBox(responses.get(x));
							exampleKey.put(responses.get(x), category.getText() + " " + subcategory.getText()); 
							checkList.add(checkBox);
							panel.add(checkBox);
							//mainPanel.add(checkBox);
						}
						
						//panel used to place all the checklists left aligned
						JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
						leftPanel.add(panel);
						mainPanel.add(leftPanel);
					}
				}
			}
		}
		// panel for the button so it is centered
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(genButton);
		mainPanel.add(buttonPanel, BorderLayout.WEST);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == genButton){
			checks();
			// fix if the second largest has none in it
			order[0] = topTwo("+")[0];
			order[1] = topTwo("+")[1];
			order[2] = topTwo("-")[0];
			order[3] = topTwo("-")[1];
			//System.out.println(examples);
			Report report = new Report(examples, countWindow.getCounts(), order);
			frame.setVisible(false);
			report.createReport();
		}
		
	}
	
	public void checks(){
		for ( int i = 0; i < checkList.size(); i++ ){
			if ( checkList.get(i).isSelected() ){
				String comment = checkList.get(i).getText();
				String category = new String();
				String sub = new String();
				String key = exampleKey.get(comment);
				if ( key.contains("+") ){
					category = key.substring(0, key.indexOf("+") + 1);
					sub = key.substring(key.indexOf("+") + 2);
				}
				else if (key.contains("-") ){
					category = key.substring(0, key.indexOf("-") + 1);
					sub = key.substring(key.indexOf("-") + 2);
				}
			
				if ( examples.get(category) == null)
					examples.put(category, new HashMap<String, ArrayList<String>>());
				if ( examples.get(category).get(sub) == null)
					examples.get(category).put(sub, new ArrayList<String>());
				examples.get(category).get(sub).add(comment);
			}
		}
	}
}


