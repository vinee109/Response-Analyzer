/***
 * This class contains methods that will be used to maniuplate survey responses.
 * @author Vineet Jain
 */
import java.util.ArrayList;



public class Extract {
	
	public Extract(){
	}
	
	/*** Splits up a multiple sentence comment, or phrase comment into a list of multiple sentences
	 * broken up by where the period is. Returns an ArrayList<String> of all the sentences in that
	 * comment.
	***/
	public ArrayList<String> splitSentences(String str){
		ArrayList<String> ret = new ArrayList<String>();
		if( str.contains(".") == false)
			str += ".";
		while( str.contains(".")){
			int period_index = str.indexOf(".");
			ret.add(str.substring(0, period_index + 1));
			str = str.substring(period_index + 1); 
		}
		return ret;
	}
}
