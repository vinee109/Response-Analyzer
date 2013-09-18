import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import word.api.interfaces.IDocument;
import word.w2004.Document2004;
import word.w2004.elements.BreakLine;
import word.w2004.elements.Paragraph;
import word.w2004.elements.ParagraphPiece;

/***
 * This class represents the window in which this program will open up from
 * @author Vineet Jain
 *
 */
public class CommentWindow implements ActionListener{
	JFrame frame = new JFrame("Results: ");
	JPanel mainPanel = new JPanel();
	MenuBar menuBar = new MenuBar(this, true);
	Map<String, ArrayList<String>> displayData;
	Map<String, HashMap<String, ArrayList<String>>> data = new HashMap<String, HashMap<String, ArrayList<String>>>();
	ArrayList<JComboBox> cbList = new ArrayList<JComboBox>();
	ArrayList<JTextArea> textList = new ArrayList<JTextArea>();
	String [] comboBoxItems;
	JButton editButton = new JButton("Edit");
	JButton doneButton = new JButton("Done");
	JButton genButton = new JButton("Generate Report");
	JButton countButton = new JButton("View Counts");
	JButton responseButton = new JButton("Export Categories");
	Map<String, Integer> counts = new HashMap<String, Integer>();
	private CountWindow counter;
	boolean edited;
	IDocument myDoc = new Document2004();
	
	public CommentWindow(Map<String, ArrayList<String>> d, CountWindow c, boolean e, String str){
		frame = new JFrame(str);
		edited = e;
		displayData = d;
		counter = c;
		convertToMap(displayData);
		// initializes comboBoxItems
		ArrayList<String> items = new ArrayList<String>();
		ArrayList<String> categories = makeCategoriesList();
		for ( int i = 0; i < categories.size(); i++ ){
			String item = categories.get(i);
			//System.out.println(data.get(item).keySet());
			ArrayList<String> subcategories = new ArrayList<String>(data.get(item).keySet());
			for ( int s = 0; s < subcategories.size(); s++){
				items.add( item + " " + subcategories.get(s));
			}	
		}
		items.add(Categorizer.categories[Categorizer.categories.length - 1]);
		//System.out.println(items);
		comboBoxItems = items.toArray(new String [items.size()]);
		getCounts();
	}
	
	public CommentWindow(){
	}
	// counts for the categories and number of comments
	public void getCounts(){
		ArrayList<String> categories = new ArrayList<String>(data.keySet());
		for ( int i = 0; i < categories.size(); i++){
			int sum = 0;
			ArrayList<String> subcategories = new ArrayList<String>(data.get(categories.get(i)).keySet());
			for ( int j = 0; j < subcategories.size(); j++ ){
				sum += data.get(categories.get(i)).get(subcategories.get(j)).size();
			}
			counts.put(categories.get(i), sum);
		}
	}
	
	public boolean isEmpty(String category){
		ArrayList<String> subcategories = new ArrayList<String>(data.get(category).keySet());
		for ( int i = 0; i < subcategories.size(); i++ ){
			if (data.get(category).get(subcategories.get(i)).size() > 0)
				return false;
		}
		return true;
	}
	
	public ArrayList<String> makeCategoriesList(){
		ArrayList<String> categories = new ArrayList<String>();
		for ( int i = 0; i < 2; i++ ){
			String sign = "+";
			if ( i == 1)
				sign = "-";
			for ( int j = 0; j < Categorizer.categories.length - 1; j++){
				categories.add(Categorizer.categories[j] + sign);
			}
		}
		return categories;
	}
	
	public int getSignTotal(String sign){
		int total = 0;
		ArrayList<String> categories = new ArrayList<String>(counts.keySet());
		for ( int i = 0; i < categories.size(); i++ ){
			if ( categories.get(i).contains(sign) )
				total += counts.get(categories.get(i));
		}
		return total;
	}
	
	//true if postitive, false if negative
	public boolean isPos(String cat){
		return cat.contains("+");
	}

	public void addComponentsToPanel(){
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		JPanel panel1 = new JPanel();
		panel1.setLayout(new BoxLayout(panel1, BoxLayout.X_AXIS));
		// puts the categories from Categorizer class into a list;
		ArrayList<String> categories = makeCategoriesList();
		// other categories
		for ( int i = 0; i < categories.size(); i++){
			if ( !isEmpty(categories.get(i)) ){
				String ending = new String();
				if ( isPos(categories.get(i)) )
					ending = "+";
				else
					ending = "-";
				//
				myDoc.addEle(Paragraph.withPieces(ParagraphPiece.with(categories.get(i)).withStyle().bold().fontSize("16").create()));
				//
				JLabel category = new JLabel(categories.get(i) + " (" + counts.get(categories.get(i)) + " out of " + getSignTotal(ending) + ")", JLabel.LEFT);
				category.setFont(new Font(category.getFont().getName(), category.getFont().getStyle(), 18));
				JPanel categoryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
				categoryPanel.add(category);
				mainPanel.add(categoryPanel, BorderLayout.WEST);
				
				ArrayList<String> subcategories = new ArrayList<String>(data.get(categories.get(i)).keySet());
				for ( int k = 0; k < subcategories.size(); k++ ){
					ArrayList<String> responses = data.get(categories.get(i)).get(subcategories.get(k));
					if ( responses.size() > 0){
						//
						myDoc.addEle(Paragraph.withPieces(ParagraphPiece.with(subcategories.get(k)).withStyle().bold().fontSize("14").create()));
						//
						JPanel subcategoryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
						JLabel subcategory = new JLabel(subcategories.get(k));
						subcategoryPanel.add(subcategory);
						mainPanel.add(subcategoryPanel, BorderLayout.WEST);
						for ( int x = 0; x < responses.size(); x++ ){
							myDoc.addEle(Paragraph.withPieces(ParagraphPiece.with(responses.get(x)).withStyle().fontSize("12").create()));
							JPanel panel = new JPanel();
							panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
							JTextArea text = new JTextArea(1,1);
							text.setLineWrap(true);
							textList.add(text);
							text.setText(responses.get(x));
							text.setEditable(false);
							JComboBox cb = new JComboBox(comboBoxItems);
							cbList.add(cb);
							cb.setVisible(false);
							cb.setSelectedItem(categories.get(i) + " " + subcategories.get(k));
							panel.add(text);
							panel.add(cb);
							mainPanel.add(panel);
						}
					}
				}
				//
				myDoc.addEle(BreakLine.times(1).create());
				//
			}
		}
		//misc category
		String miscCat = Categorizer.categories[Categorizer.categories.length - 1];
		//
		myDoc.addEle(Paragraph.withPieces(ParagraphPiece.with(miscCat).withStyle().bold().fontSize("16").create()));
		//
		JPanel miscPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel miscLabel = new JLabel(miscCat);
		miscPanel.add(miscLabel);
		miscLabel.setFont(new Font(miscLabel.getFont().getName(), miscLabel.getFont().getStyle(), 18));
		mainPanel.add(miscPanel, BorderLayout.WEST);
		ArrayList<String> responses = displayData.get(miscCat);
		for ( int i = 0; i < responses.size(); i++ ){
			myDoc.addEle(Paragraph.withPieces(ParagraphPiece.with(responses.get(i)).withStyle().fontSize("12").create()));
			JPanel panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
			JTextArea text = new JTextArea(1,1);
			text.setLineWrap(true);
			textList.add(text);
			text.setText(responses.get(i));
			text.setEditable(false);
			JComboBox cb = new JComboBox(comboBoxItems);
			cbList.add(cb);
			cb.setVisible(false);
			cb.setSelectedItem(miscCat);
			panel.add(text);
			panel.add(cb);
			mainPanel.add(panel);
		}
		
		// add done button and edit button
				JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
				panel.add(genButton);
				panel.add(doneButton);
				doneButton.setVisible(false);
				panel.add(editButton);
				panel.add(countButton);
				panel.add(responseButton);
				doneButton.addActionListener(this);
				editButton.addActionListener(this);
				genButton.addActionListener(this);
				countButton.addActionListener(this);
				responseButton.addActionListener(this);
				mainPanel.add(panel);
		
	}
	public void createAndShowGUI(){
		//System.out.println(displayData);
		addComponentsToPanel();
		if (edited && !frame.getTitle().contains("*"))
			frame.setTitle("*" + frame.getTitle());
		frame.setJMenuBar(menuBar);
		frame.add(mainPanel);
		frame.setSize(700, 700);
		JScrollPane jsp = new JScrollPane(mainPanel);
		frame.add(jsp);
		//frame.pack();
		frame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == editButton && !frame.getTitle().contains("*") ){
			frame.setTitle("*" + frame.getTitle());
			for (int i = 0; i < cbList.size(); i++){
				cbList.get(i).setVisible(true);
			}
			edited = true;
			doneButton.setVisible(true);
			genButton.setVisible(false);
			frame.setSize(900,700);	
		}
		else if (event.getSource() == doneButton){
			frame.dispose();
			makeNewMap();
			new FileOptions().backup(displayData, counter);
			CommentWindow window = new CommentWindow(displayData, counter, edited, frame.getTitle());
			window.createAndShowGUI();
		}
		else if (event.getSource() == genButton){
			CheckListWindow check = new CheckListWindow(counts, data, counter);
			check.createAndShowGUI();
		}
		else if (event.getSource() == countButton){
			counter.createAndShowGUI();
		}
		else if (event.getSource() == responseButton){
			new WriteWord().createLocalDocument(myDoc.getContent(), "export.doc");
			JFrame frame = new JFrame();
			JOptionPane.showMessageDialog(frame,
				    "Your data has been exported. It is in a word document called export.doc",
				    "Success!", JOptionPane.INFORMATION_MESSAGE);
		}
		else if (event.getSource() == menuBar.save){
			if (doneButton.isVisible()){
				JOptionPane.showMessageDialog(frame,
					    "You are in the middle of editing. Please click Done first.",
					    "Wait", JOptionPane.INFORMATION_MESSAGE);
			}
			else{
				String title = frame.getTitle();
				int x = new FileOptions().save(displayData, counter);
				if ( x == 1 && title.contains("*"))
					frame.setTitle(title.substring(1));
					
			}
		}
		else if (event.getSource() == menuBar.open){
			if (edited){
				int reply = JOptionPane.showConfirmDialog(null, "You have not saved your changes. Any unsaved changes will be lost. Are you sure you want to continue?",
						"Continue?", JOptionPane.YES_NO_OPTION);
				if (reply == JOptionPane.YES_OPTION){
					frame.setVisible(false);
					new FileOptions().open();
				}
			}
			else{
				frame.setVisible(false);
				new FileOptions().open();
			}
		} else if (event.getSource() == menuBar.delete)
        	new FileOptions().delete();
	}
	
	//called after the data is changed in the edit menu
	public void makeNewMap(){
		Map<String, ArrayList<String>> newMap = new HashMap<String, ArrayList<String>>();
		ArrayList<String> categories = makeCategoriesList();
		for (int x = 0; x < categories.size(); x++ ){
			ArrayList<String> subcats = new ArrayList<String>(data.get(categories.get(x)).keySet());
			for ( int j = 0; j < subcats.size(); j++ ){
				newMap.put(categories.get(x) + " " + subcats.get(j), new ArrayList<String>());
			}
		}
		newMap.put(Categorizer.categories[Categorizer.categories.length - 1], new ArrayList<String>());
		for (int i = 0; i < cbList.size(); i++){
			JComboBox cb = cbList.get(i);
			JTextArea text = textList.get(i);
			newMap.get(cb.getSelectedItem()).add(text.getText());
		}
		displayData = newMap;
	}
	
	public void convertToMap(Map<String, ArrayList<String>> d){
		ArrayList<String> keys = new ArrayList<String>(displayData.keySet());
		for ( int i = 0; i < keys.size(); i++ ){
			String current = keys.get(i);
			String category = new String();
			String subcategory = new String();
			if (current.contains("+")){
				int index = current.indexOf("+");
				category = current.substring(0, index + 1);
				subcategory = current.substring(index + 2);
				if ( data.get(category) == null )
					data.put(category, new HashMap<String, ArrayList<String>>());
				data.get(category).put(subcategory, d.get(current));
			}
			else if ( current.contains("-")){
				int index = current.indexOf("-");
				category = current.substring(0, index + 1);
				subcategory = current.substring(index + 2);
				if ( data.get(category) == null )
					data.put(category, new HashMap<String, ArrayList<String>>());
				data.get(category).put(subcategory, d.get(current));
			}
		}
	}
	
}
