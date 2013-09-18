/***
 * This class represents one response. It contains the question that it is answering as well as 
 *  the answer to that specific question
 *  @author Vineet Jain
 */
public class Response {
	private String question;	//question of the response
	private String response;	//the actual answer to the question
	
	public Response( String q, String r ){
		question = q;
		response = r;
	}
	
	public String getQuestion(){
		return question;
	}
	
	public String getResponse(){
		return response;
	}
}
