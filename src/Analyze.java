import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JOptionPane;


public class Analyze {
	static String [] questions = {"What one aspect of practice should the instructor absolutely continue in this course?",
			"With a realistic view as to what can be changed at this point in the semester, what one aspect should the instructor change in this course? How?",
			"I'd prefer the course to", "I'd prefer the pace of lectures to", "I'd find it useful to have", "The homework assignments so far"};
	private Map<String, ArrayList<String>> data;
	private Map<String, ArrayList<String>> categorizedData = new HashMap<String, ArrayList<String>>();
	private boolean iscounted;
	
	public Analyze( Map<String, ArrayList<String>> d, boolean count){
		data = d;
		iscounted = count;
	}
	
	public void categorize(String keyDirectory){
		//System.out.println("Categorize: " + keyDirectory);
		Categorizer cat = new Categorizer(keyDirectory);
		initialize(cat);
	    Extract extract = new Extract();
	    // list of responses underneath the positive questions
	    try{
	    ArrayList<String> responses = data.get(questions[0].toLowerCase());
	    System.out.println(data);
	    for (String response: responses){
	    	// each sentence in the response
	    	ArrayList<String> list = extract.splitSentences(response);
		    for (int i = 0; i < list.size(); i++){
		    	String phrase = list.get(i).trim();
		    	//returns an ArrayList of all the categories its in.
		    	ArrayList<String> selectedCategories = cat.determineCategory(phrase, Categorizer.STRENGTH);
		    	for (int catNumber = 0; catNumber < selectedCategories.size(); catNumber++){
		    		String category = selectedCategories.get(catNumber);
		    		categorizedData.get(category).add(phrase);
		    	}
		    	//System.out.println(phrase);		
		    	//System.out.println("Strengths: " + cat.determineCategory(phrase, Categorizer.STRENGTH));
		    }
	    }
	    responses = data.get(questions[1].toLowerCase());
	    for (String response: responses){
	    	ArrayList<String> list = extract.splitSentences(response);
	    	for (int i = 0; i < list.size(); i++){
	    		String phrase = list.get(i).trim();
	    		ArrayList<String> selectedCategories = cat.determineCategory(phrase, Categorizer.OPP);
		    	for (int catNumber = 0; catNumber < selectedCategories.size(); catNumber++){
		    		String category = selectedCategories.get(catNumber);
		    		categorizedData.get(category).add(phrase);
		    	}
	    		//System.out.println(phrase);
	    		//System.out.println("Opportunities: " + cat.determineCategory(phrase, Categorizer.OPP));
	    	}
	    }
	    //System.out.println(categorizedData);
	    displayWindow(categorizedData);
	    }
	    catch(NullPointerException e){
	    	JFrame frame = new JFrame();
			JOptionPane.showMessageDialog(frame,
				    "Please check that you have inputted the correct file for both the responses and the keywords",
				    "Error",
				    JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
	    }
	}
	
	private void initialize(Categorizer c) {
		ArrayList<String> categories = new ArrayList<String>(c.keys.keySet());
		for ( int i = 0; i < categories.size(); i++ ){
			ArrayList<String> subcategories = new ArrayList<String>(c.keys.get(categories.get(i)).keySet());
				for ( int j = 0; j < subcategories.size(); j++ )
					categorizedData.put( categories.get(i) + " " + subcategories.get(j), new ArrayList<String>());
			}
		categorizedData.put(Categorizer.categories[Categorizer.categories.length -1 ], new ArrayList<String>());
	}

	public void displayWindow(Map<String, ArrayList<String>> map){
		System.out.println(data);
		CountWindow counter = new CountWindow();
		if (!iscounted){
			ArrayList<String> values = data.get(questions[2].toLowerCase());
			System.out.println(questions[3].toLowerCase());
			values.addAll(data.get(questions[3].toLowerCase()));
			values.addAll(data.get(questions[4].toLowerCase()));
			values.addAll(data.get(questions[5].toLowerCase()));
			System.out.println(values);
			counter.count(values);
		}
		else{
			// add pre counted map with integer values for each option
			ArrayList<String> cts = data.get("counts");
			Map<String, Integer> counts = new HashMap<String, Integer>();
			for (int i = 0; i < CountWindow.question_pace_options.length; i++ )
				counts.put(CountWindow.question_pace_options[i], 0);
			for (int i = 0; i < CountWindow.question_course_options.length; i++ )
				counts.put(CountWindow.question_course_options[i], 0);
			for (int i = 0; i < CountWindow.question_useful_options.length; i++ )
				counts.put(CountWindow.question_useful_options[i], 0);
			for (int i = 0; i < CountWindow.question_hw_options.length; i++)
				counts.put(CountWindow.question_hw_options[i], 0);
			for (int i = 0; i < cts.size(); i++){
				String c = cts.get(i);
				int index = c.indexOf("-");
				String name = c.substring(0, index).toLowerCase();
				int count = Integer.parseInt(c.substring(index + 1));
				counts.remove(name);
				counts.put(name, count);
			}
			counter.setCounts(counts);
			//System.out.println(cts);
			//System.out.println(counts);
		}
		//System.out.println(map);
		CommentWindow window = new CommentWindow(map, counter, false, "Untitled");
		window.createAndShowGUI();
	}
}
