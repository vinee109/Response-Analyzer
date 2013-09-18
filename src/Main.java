
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/*
		WriteExcel test = new WriteExcel();
	    test.setOutputFile("lars.xls");
	    try {
			test.write();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    System.out
	        .println("Please check the result file under c:/temp/lars.xls ");
	        */
	    /*
	    ReadExcel testread = new ReadExcel();
	    testread.setInputFile("Course_Evaluation_Responses.xls");
	    Analyze analyze = null;
	    try {
			analyze = new Analyze(testread.read());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    analyze.categorize();
	    */
		FileNavigator f = new FileNavigator();
	    f.createAndShowGUI();
	}

}
