import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;


public class WriteWord {
	public void createLocalDocument(String myDoc, String fileName) {
        //Property prop = new Property("");
		/*
        Properties prop = new Properties();
        String tmpDocs = "";
        try {            
            prop.load(new FileInputStream("build.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        tmpDocs = (String) prop.get("tmp.docs.dir");
        
        //System.out.println(tmpDocs);
        //"/home/leonardo/Desktop/Java2word_allInOne.doc"
        */
        File fileObj = new File(fileName);

        PrintWriter writer = null;
        try {
            writer = new PrintWriter(fileObj);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String myWord = myDoc;

        writer.println(myWord);
        writer.close();
    }
}
