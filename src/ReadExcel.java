import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class ReadExcel {
	private String inputFile;		// string version of the directory
	private String keyInputFile;
	private boolean isCounted;
	
	  public void setInputFile(String inputFile) {
	    this.inputFile = inputFile;
	  }
	  
	  public void setKeyInputFile(String inputFile){
		  this.keyInputFile = inputFile;
	  }
	  public boolean preCounted(){
		  return isCounted;
	  }
	  public Map<String, ArrayList<String>> read(boolean tf) throws IOException  {
		  this.isCounted = false;
		  Map<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
		  File inputWorkbook = new File(inputFile);
		  Workbook w;
		    try {
		      w = Workbook.getWorkbook(inputWorkbook);
		      // Get the first sheet
		      Sheet sheet = w.getSheet(0);
		      if (sheet.getCell(0,0).getContents().equals("Initial Report")){
		    	  this.isCounted = true;
		    	  map.put("counts", new ArrayList<String>());
		    	  Map<String, Integer> questionIndex = new HashMap<String, Integer>();
		    	  for (int i = 0; i < sheet.getRows(); i++){
		    		  for (int x = 0; x < Analyze.questions.length; x++){
		    			  String contents = sheet.getCell(0, i).getContents();
		    			  if (contents.contains(Analyze.questions[x])){
		    				  questionIndex.put(Analyze.questions[x], i);
		    			  }
		    		  }
		    	  }
		    	  for (int i = 0; i < 2; i++){
		    		  int start = questionIndex.get(Analyze.questions[i]);
		    		  start += 2;
		    		  ArrayList<String> responses = new ArrayList<String>();
		    		  while ( sheet.getCell(0, start).getType() != CellType.EMPTY ){
		    			  responses.add(sheet.getCell(0, start).getContents());
		    			  start +=1;
		    		  }
		    		  map.put(Analyze.questions[i].toLowerCase(), responses);
		    	  }
		    	  if(tf){
			    	  // Change this after wards to 2
			    	  for (int i = 2; i < Analyze.questions.length; i++){
			    		  int start = questionIndex.get(Analyze.questions[i]);
			    		  start += 2;
			    		  for(int x = 0; x < 3; x++){
			    			  String str = sheet.getCell(1, start + x).getContents() + "-" + sheet.getCell(3, start + x).getContents();
			    			  map.get("counts").add(str);
			    		  }
			    	  }
		    	  }
		    	  
		    	 
		      }
		      else{
			      ArrayList<String> questions = new ArrayList<String>();
			      // Loop over all the columns and lines
			      	for (int j = 0; j < sheet.getColumns(); j++) {
			      		ArrayList<String> responses = new ArrayList<String>();
			      		for (int i = 0; i < sheet.getRows(); i++) {
			      			Cell cell = sheet.getCell(j, i);
			      				if ( i == 0 ){
			      					System.out.println(cell.getContents().toLowerCase());
			      					questions.add(cell.getContents().toLowerCase());
			      				}
			      				else{
			      					responses.add(cell.getContents());
			      				}
			      		}
			      		map.put(questions.get(j), responses);
	      			}
		      	}
		    } catch (BiffException e) {
		      e.printStackTrace();
		    } catch (Exception e){
		    	JFrame frame = new JFrame();
		    	JOptionPane.showMessageDialog(frame, "Whoops! Something went wrong:\n" + e.getCause(), "Error", JOptionPane.ERROR_MESSAGE);
		    }
			return map;
	  }
	  
	  public Map<String, ArrayList<String>> readKeywords() throws IOException{
		  //System.out.println(keyInputFile);
		  File inputWorkbook = new File(keyInputFile);
		  Workbook w;
		  Map<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();  
		    try {
		      w = Workbook.getWorkbook(inputWorkbook);
		      // Get the first sheet
		      Sheet sheet = w.getSheet(0);
		      // Loop over first 10 column and lines
		      String category = new String();
		      for (int j = 0; j < sheet.getColumns(); j++) {
		    	  ArrayList<String> words = new ArrayList<String>();
		    	  for (int i = 0; i < sheet.getRows(); i++) {
		    		  Cell cell = sheet.getCell(j, i);
		    		  CellType type = cell.getType();
		    		  if (type != CellType.EMPTY){
		    			  if( i == 0 )
		    				  category = cell.getContents();
		    			  else
		    				  words.add(cell.getContents());
		    		  }
		    	  }
		    	  map.put(category, words);
		      }
		    } catch (BiffException e) {
		      e.printStackTrace();
		    }
		    return map;
	  }
	  
	  public Map<String, HashMap<String, ArrayList<String>>> readSubCategories() throws IOException{
		  File inputWorkbook = new File(keyInputFile);
		  Workbook w;
		  Map<String, HashMap<String, ArrayList<String>>> map = new HashMap<String, HashMap<String, ArrayList<String>>>();  
		    try {
		      w = Workbook.getWorkbook(inputWorkbook);
		      // Get the first sheet
		      Sheet sheet = w.getSheet(0);
		      // Loop over first 10 column and lines
		      String category = new String();
		      for (int j = 0; j < sheet.getColumns(); j++) {
		    	  Cell cell = sheet.getCell(j, 0);
		    	  CellType type = cell.getType();
		    	  if (type != CellType.EMPTY){
		    		  category = cell.getContents();
		    		  if ( map.get(category) == null )
		    			  map.put(category, new HashMap<String, ArrayList<String>>());
		    	  }
		      }
		     for ( int j = 0; j < sheet.getColumns(); j++){
		    	 Cell cell = sheet.getCell(j, 1);
		    	 int catIndex = j;
		    	 while ( sheet.getCell(catIndex, 0).getType() == CellType.EMPTY){
		    		 catIndex--;
		    	 }
		    	 category = sheet.getCell(catIndex, 0).getContents();
		    	 String subcategory = cell.getContents();
		    	 if ( cell.getType() != CellType.EMPTY){
		    		 map.get(category).put(subcategory, new ArrayList<String>());
		    	 }
		     }
		     
		     for ( int c = 0; c < sheet.getColumns(); c++){
		    	 for ( int r = 2; r < sheet.getRows(); r++){
		    		 Cell cell = sheet.getCell(c, r);
		    		 int catIndex = c;
		    		 while ( sheet.getCell(catIndex, 0).getType() == CellType.EMPTY){
		    			 catIndex--;
		    		 }
		    		 if (cell.getType() != CellType.EMPTY){
		    			 map.get(sheet.getCell(catIndex, 0).getContents()).get(sheet.getCell(c, 1).getContents()).add(cell.getContents());
		    		 }
		    	 }
		     }
		    } catch (BiffException e) {
		      e.printStackTrace();
		    }
		    return map;
	  }
}
