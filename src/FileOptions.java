import java.io.File;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileView;


public class FileOptions {
	final static String SAVE_DIR = "Analyzer_Sessions";
	final static String BACKUP_DIR = "Backup";
	JFileChooser fc;
	final static File SAVE_FILE_DIR = new File(SAVE_DIR);
	
	public FileOptions(){
		fc = new JFileChooser(SAVE_FILE_DIR);
		/*
		fc.setFileView(new FileView(){
			@Override
			public Boolean isTraversable(File f){
				return SAVE_FILE_DIR.equals(f);
			}
		});
		*/
	}
	public void write(Map<String, ArrayList<String>> map, Map<String, Integer> counts, File file){
		try{
			FileWriter fw = new FileWriter(file);
			DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			fw.write("Session ran: "+ dateFormat.format(new Date()) + "\r\n");
			fw.write("\r\n");
			String [] keys = map.keySet().toArray(new String [map.keySet().size()]);
			for (String entry: keys){
				ArrayList<String> responses = map.get(entry);
				fw.write("***" + entry + "\r\n");
				for ( int i = 0; i < responses.size(); i++){
					fw.write(i+1 + ") " + responses.get(i) + "\r\n");
				}
			}
			fw.write("||" + "\r\n");
			String [] countKeys = counts.keySet().toArray(new String [counts.keySet().size()]);
			for (String entry: countKeys){
				fw.write(entry + ": " + counts.get(entry) + "\r\n");
			}
			fw.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void parseFile(File file){
		Map<String, ArrayList<String>> retMap = new HashMap<String, ArrayList<String>>();
		Scanner input;
		ArrayList<String> responses = new ArrayList<String>();
		ArrayList<String> counts = new ArrayList<String>();
		try{
			input = new Scanner(file);
			input.nextLine();
			input.nextLine();
			boolean countSection = false;
			//System.out.println(input.hasNextLine() && !countSection);
			while (input.hasNextLine() && !countSection){
				String line = input.nextLine();
				if (line.contains("||") && !countSection)
					countSection = true;
				if (!countSection){	
					responses.add(line);
				}
			}
			while(input.hasNextLine()){
				String line = input.nextLine();
				counts.add(line);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		// parse counts
		Map<String, Integer> countMap = new HashMap<String, Integer>();
		for (int i = 0; i < counts.size(); i++){
			String item = counts.get(i);
			int index = item.indexOf(":");
			String key = item.substring(0, index);
			int value = Integer.parseInt(item.substring(index+2));
			countMap.put(key, value);
		}
		CountWindow counter = new CountWindow();
		counter.setCounts(countMap);
		
		//parse responses
		Map<String, ArrayList<String>> responseMap = new HashMap<String, ArrayList<String>>();
		String currentCat = new String();
		for (int i = 0; i < responses.size(); i++){
			String item = responses.get(i);
			if (item.contains("***")){
				currentCat = item.substring(3);
				responseMap.put(currentCat, new ArrayList<String>());
			}else{
				int index = item.indexOf(")");
				String response = item.substring(index+1);
				responseMap.get(currentCat).add(response);
			}
		}
		CommentWindow window = new CommentWindow(responseMap, counter, false, file.getName());
		window.createAndShowGUI();
	}
	
	public void backup(Map<String, ArrayList<String>> map, CountWindow count){
		DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy_HH-mm-ss");
		File file = new File(SAVE_DIR + "/" + BACKUP_DIR + "/" + dateFormat.format(new Date()));
		write(map, count.getCounts(), file);
	}
	
	public int save(Map<String, ArrayList<String>> map, CountWindow count){
		File dir = new File(SAVE_DIR);
		File backup = new File(SAVE_DIR + "/" + BACKUP_DIR);
		if (!dir.isDirectory()){
			dir.mkdir();
			backup.mkdir();
			//System.out.println("Dir was created");
		}
		else
			//System.out.println("Dir already exists");
		try{
			int returnVal = fc.showSaveDialog(new JPanel());
			if (returnVal == JFileChooser.APPROVE_OPTION){
				File saveFile = fc.getSelectedFile();
				if (!saveFile.isFile()){
					write(map, count.getCounts(), saveFile);
					saveFile.setReadOnly();
				}
				else{
					int reply = JOptionPane.showConfirmDialog(null, "A session under this name already exists. Are you sure you want to overwrite it?", "Overwrite", JOptionPane.YES_NO_OPTION);
					if (reply == JOptionPane.YES_OPTION){
						saveFile.delete();
						write(map, count.getCounts(), saveFile);
					}
				}
				return 1;
			}
			/*
			File save = new File(SAVE_DIR +"/save");
			save.setReadOnly();
			FileWriter fw = new FileWriter(save);
			fw.write(map.toString());
			fw.close();
			*/
		}catch(Exception e){
			e.printStackTrace();
		}
		return 0;
	}
	
	public void open(){
		File saveDir = new File(SAVE_DIR);
		if(!saveDir.isDirectory() || saveDir.listFiles().length == 1){
			JOptionPane.showMessageDialog(new JFrame("Error"),
				    "There are no saved sessions",
				    "Error", JOptionPane.ERROR_MESSAGE);
		}
		else{
			//System.out.println("Directory available");
			int returnVal = fc.showOpenDialog(new JPanel());
			if (returnVal == JFileChooser.APPROVE_OPTION){
				parseFile(fc.getSelectedFile());
			}
		}
	}
	
	public void delete(){
		int returnVal = fc.showDialog(new JPanel(), "Delete");
		if (returnVal == JFileChooser.APPROVE_OPTION){
			File selectedFile = fc.getSelectedFile();
			int reply = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this file?", "Delete", JOptionPane.YES_NO_OPTION);
			if (reply == JOptionPane.YES_OPTION)
				selectedFile.delete();
		}
	}
}
