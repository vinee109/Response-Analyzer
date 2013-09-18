import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import word.api.interfaces.IDocument;
import word.w2004.Document2004;
import word.w2004.elements.Paragraph;
import word.w2004.elements.ParagraphPiece;


public class Export {
	private Map<String, HashMap<String, ArrayList<String>>> data;
	
	public Export(Map<String, HashMap<String, ArrayList<String>>> d){
		data = d;
	}
	
	public void createDoc(){
		IDocument myDoc = new Document2004();
		String [] categories = data.keySet().toArray(new String [data.keySet().size()]);
		for (int i = 0; i < categories.length; i++){
			myDoc.addEle(Paragraph.withPieces(ParagraphPiece.with(categories[i]).withStyle().bold().fontSize("16").create()));
		}
		new WriteWord().createLocalDocument(myDoc.getContent(), "export.doc");
	}
}
