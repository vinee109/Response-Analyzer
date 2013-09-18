import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import word.api.interfaces.IDocument;
import word.w2004.Document2004;
import word.w2004.elements.BreakLine;
import word.w2004.elements.Image;
import word.w2004.elements.Paragraph;
import word.w2004.elements.ParagraphPiece;


public class Report {
	private Map<String, HashMap<String, ArrayList<String>>> responseData;
	private Map<String, Integer> countData;
	private String [] order;
	String fileName = "";
	
	public Report(Map<String, HashMap<String, ArrayList<String>>> r, Map<String, Integer> c, String [] o){
		responseData = r;
		countData = c;
		order = o;
	}
	
	public void createReport(){
		try{
			IDocument myDoc = new Document2004();
			
			// adding picture header
			BufferedImage img = ImageIO.read(getClass().getResource("/logo.gif"));
			ImageIO.write(img, "gif", new File("logo.gif"));
			myDoc.getHeader().addEle(Image.from_FULL_LOCAL_PATHL("logo.gif").setHeight("69").setWidth("450").create());
			new File("logo.gif").delete();
			
			// beginning
			myDoc.addEle(Paragraph.withPieces(ParagraphPiece.with("MID COURSE EVALUATION SUMMARY").withStyle().bold().fontSize("16").create()));
			myDoc.addEle(Paragraph.withPieces(ParagraphPiece.with("Semester: ").create()));
			myDoc.addEle(Paragraph.withPieces(ParagraphPiece.with("Instructor: ").create()));
			myDoc.addEle(Paragraph.withPieces(ParagraphPiece.with("Course: ").create()));
			myDoc.addEle(Paragraph.withPieces(ParagraphPiece.with("Title: ").create()));
			myDoc.addEle(Paragraph.withPieces(ParagraphPiece.with("Responses/Enrollment: ").create()));
			myDoc.addEle(Paragraph.withPieces(ParagraphPiece.with("% Responding: ").create()));
			myDoc.addEle(BreakLine.times(1).create());
			// counts
			myDoc.addEle(Paragraph.withPieces(ParagraphPiece.with("I. Analysis of Class Based Comments").withStyle().bold().fontSize("16").create()));
			for ( int j = 2; j < 6; j++ ){
				myDoc.addEle(Paragraph.withPieces(ParagraphPiece.with(Analyze.questions[j]).withStyle().bold().create()));
				String [] options = null;
				switch(j){
				case 2: options = CountWindow.question_pace_options;
					break;
				case 3: options = CountWindow.question_course_options;
					break;
				case 4: options = CountWindow.question_useful_options;
					break;
				case 5: options = CountWindow.question_hw_options;
					break;
				}
				// number of options in that question is three
				int sum = 0;
				for (int i = 0; i < 4; i++){
					String text = new String();
					if (i == 3){
						text = "Total: " + sum;
					}
					else{
						sum += countData.get(options[i]);
						text += options[i] + ": " + countData.get(options[i]);
					}
					myDoc.addEle(Paragraph.withPieces(ParagraphPiece.with(text).create()));
				}
				/*
				PieChart demo = new PieChart(Analyze.questions[j], countData, options);
				demo.createAndShowGUI();
				*/
				//demo.makeImage();
				//demo.createImage();
				//myDoc.addEle(Image.from_FULL_LOCAL_PATHL("chart.jpeg").setHeight("400").setWidth("400").create());
				myDoc.addEle(BreakLine.times(1).create());
			}
			
			// responses
			myDoc.addEle(Paragraph.withPieces(ParagraphPiece.with("II. Sample of Student Responses").withStyle().bold().fontSize("16").create()));
			for ( int a = 0; a < order.length; a++ ){
				if ( a == 0 ){
					myDoc.addEle(Paragraph.withPieces(ParagraphPiece.with("Most Frequently Mentioned Themes in Strengths").withStyle().italic().fontSize("16").create()));
					myDoc.addEle(BreakLine.times(1).create());
				}
				else if ( a == 2){
					myDoc.addEle(Paragraph.withPieces(ParagraphPiece.with("Most Frequently Mentioned Themes in Opportunities").withStyle().italic().fontSize("16").create()));
					myDoc.addEle(BreakLine.times(1).create());
				}
				myDoc.addEle(Paragraph.withPieces(ParagraphPiece.with(order[a]).withStyle().bold().fontSize("14").create()));
				ArrayList<String> subcats = new ArrayList<String>(responseData.get(order[a]).keySet());
				for ( int i = 0; i < subcats.size(); i++ ){
					myDoc.addEle(Paragraph.withPieces(ParagraphPiece.with(subcats.get(i)).withStyle().bold().fontSize("12").create()));
					ArrayList<String> responses = responseData.get(order[a]).get(subcats.get(i));
					for ( int x = 0; x < responses.size(); x++){
						myDoc.addEle(Paragraph.withPieces(ParagraphPiece.with(responses.get(x)).withStyle().italic().fontSize("12").create()));
					}
				}
				myDoc.addEle(BreakLine.times(1).create());
			}
			new WriteWord().createLocalDocument(myDoc.getContent(), "report.doc");
			JFrame frame = new JFrame();
			JOptionPane.showMessageDialog(frame,
				    "Your report has been created it. It is called report.doc",
				    "Success!", JOptionPane.INFORMATION_MESSAGE);
		}
		catch(NullPointerException e){
			JFrame frame = new JFrame();
			JOptionPane.showMessageDialog(frame,
				    "Please check that you have done all of the following:\n1. Make sure you have selected at least one comment from each category." +
				    " \n2. You have not left the report word document open, if you need it, save it under a different name.",
				    "Error",
				    JOptionPane.ERROR_MESSAGE);
			//e.printStackTrace();
			System.out.println(e.getMessage());
		}
		catch(Exception e){
			JFrame frame = new JFrame();
			JOptionPane.showMessageDialog(frame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
	
	public void saveWindow(){
		// parent component of the dialog
		JFrame parentFrame = new JFrame();
		 
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Specify a file to save");    
		 
		int userSelection = fileChooser.showSaveDialog(parentFrame);
		FileNameExtensionFilter xmlfilter = new FileNameExtensionFilter(
                "xls files (*.xls)", "xls");
        fileChooser.setFileFilter(xmlfilter);
		 
		if (userSelection == JFileChooser.APPROVE_OPTION) {
		    File fileToSave = fileChooser.getSelectedFile();
		    //return fileToSave.getAbsolutePath();
		    fileName = fileToSave.getAbsolutePath();
		    System.out.println("Save as file: " + fileToSave.getAbsolutePath());
		}
	}
}
