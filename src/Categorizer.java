import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/***
 * 
 * @author Vineet Jain
 *
 */
public class Categorizer {
	public Map<String, ArrayList<String>> keywords = new HashMap<String, ArrayList<String>>();
	public Map<String, HashMap<String, ArrayList<String>>> keys = new HashMap<String, HashMap<String, ArrayList<String>>>();
	final static String [] categories = {"Preparation", "Clarity and Communication", "Student Involvement", 
	                              "Relationship to Students", "Course Structure", "Miscellaneous"};
	//public Map<String, String []> subcategories = new HashMap<String, String []>();
	// appended to the end of the main category to represent a strength or an opportunity
	final static String STRENGTH = "+";
	final static String OPP = "-";
	private String keyPath;
	
	public Categorizer(String k){
		//System.out.println("Constructor: " + k);
		keyPath = k;
		initialize();
	}
	// intializes the subcategories map as well as the keywords map
	public void initialize(){
		//keywords
		ReadExcel reader = new ReadExcel();
		//System.out.println( "initialize: " + keyPath);
		reader.setKeyInputFile(keyPath);
		try {
			keys = reader.readSubCategories();
			keywords = reader.readKeywords();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
	
	public ArrayList<String> determineCategory(String comment, String sign){
		ArrayList<String> ret = new ArrayList<String>();
		for ( int i = 0; i < categories.length - 1; i++ ){
			ArrayList<String> subcategories = new ArrayList<String>(keys.get(categories[i] + sign).keySet());
			for ( int x = 0; x < subcategories.size(); x++){
				for ( int j = 0; j < keys.get(categories[i] + sign).get(subcategories.get(x)).size(); j++){
					String word = keys.get(categories[i] + sign).get(subcategories.get(x)).get(j);
					if ( comment.toLowerCase().contains(word.toLowerCase()) ){
						String cat = categories[i] + sign + " " + subcategories.get(x);
						if (!ret.contains(cat))
							ret.add(cat);
					}
				}
			}
		}
		if ( ret.size() == 0)
			ret.add(categories[categories.length - 1]);
		return ret;
	}
	
}
