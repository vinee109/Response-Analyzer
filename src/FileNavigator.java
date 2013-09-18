import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * This class represents the panel in which users will use to decide which file is a key and what not
 * @author Vineet Jain
 *
 */
public class FileNavigator extends JPanel implements ActionListener {
	JFileChooser fc;
	JButton openButton, saveButton, analyzeButton;
	JCheckBox checkBox;
    JTextArea log;
    MenuBar menuBar = new MenuBar(this, false);
    static private final String newline = "\n";
    private String inputPath = null;
    private String keyDirectory = null;
    
	public FileNavigator(){
		//Create the log first, because the action listeners
        //need to refer to it.
        log = new JTextArea(5,20);
        log.setMargin(new Insets(5,5,5,5));
        log.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(log);
        
		//Create new FileChooser
		fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		FileNameExtensionFilter xmlfilter = new FileNameExtensionFilter(
                "xls files (*.xls)", "xls");
        fc.setFileFilter(xmlfilter);
		
		openButton = new JButton("Responses");
		openButton.addActionListener(this);
		
		saveButton = new JButton("Keywords");
		saveButton.addActionListener(this);
		
		analyzeButton = new JButton("Analyze");
		analyzeButton.addActionListener(this);
		
		checkBox = new JCheckBox("Analyze only Responses");
		//For layout purposes, put the buttons in a separate panel
        JPanel buttonPanel = new JPanel(); //use FlowLayout
        buttonPanel.add(openButton);
        buttonPanel.add(saveButton);
        
        JPanel endPanel = new JPanel();
        JPanel panel = new JPanel();
        endPanel.setLayout(new BoxLayout(endPanel, BoxLayout.Y_AXIS));
        panel.add(analyzeButton);
        endPanel.add(panel);
        JPanel panel1 = new JPanel();
        panel1.add(checkBox, BorderLayout.WEST);
        endPanel.add(panel1);
        
        //Add the buttons and the log to this panel.
        this.setLayout(new FlowLayout());
        add(buttonPanel, BorderLayout.PAGE_START);
        add(logScrollPane, BorderLayout.CENTER);
		add(endPanel, BorderLayout.CENTER);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//response input file
        if (e.getSource() == openButton) {
        	fc.setDialogTitle("Responses:");
            int returnVal = fc.showOpenDialog(FileNavigator.this);
 
            if (returnVal == JFileChooser.APPROVE_OPTION) {
            	// select the input file for the responses
                File file = fc.getSelectedFile();
                inputPath = file.getPath();
                //Displays the file name selected
                log.append("Response input file: " + file.getName() + "." + newline);
            } else {
                log.append("Response cancelled by user." + newline);
            }
            log.setCaretPosition(log.getDocument().getLength());
 
        //keyword file
        } else if (e.getSource() == saveButton) {
        	fc.setDialogTitle("Keywords:");
            int returnVal = fc.showOpenDialog(FileNavigator.this);
            
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                // select the keyword file for the responses
            	File file = fc.getSelectedFile();
                keyDirectory = file.getPath();
                //Displays the file name selected
                log.append("Keyword file: " + file.getName() + "." + newline);
            } else {
                log.append("Keyword cancelled by user." + newline);
            }
            log.setCaretPosition(log.getDocument().getLength());
        
        } else if( e.getSource() == analyzeButton){
        	//System.out.println("FileNavigator: " + keyDirectory);
        	if ( inputPath != null & keyDirectory != null){
        	ReadExcel testread = new ReadExcel();
    	    testread.setInputFile(inputPath);
    	    Analyze analyze = null;
    	    try {
    			analyze = new Analyze(testread.read(!checkBox.isSelected()), testread.preCounted());
    		} catch (IOException e1) {
    			// TODO Auto-generated catch block
    			e1.printStackTrace();
    		}
    	    analyze.categorize(keyDirectory);
        	}
        	else{
        		JFrame frame = new JFrame();
    			JOptionPane.showMessageDialog(frame,
    				    "Please check to make sure you have inputted a file for both the responses and keywords.",
    				    "Error",
    				    JOptionPane.ERROR_MESSAGE);
        	}
        } else if (e.getSource() == menuBar.open){
        	new FileOptions().open();
        } else if (e.getSource() == menuBar.delete)
        	new FileOptions().delete();
		
	}
	/**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    public void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Select the Files:");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setJMenuBar(menuBar);
        //Add content to the window.
        frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        frame.add(new FileNavigator());
        JLabel label = new JLabel("Created by Vineet Jain");
        label.setAlignmentX(CENTER_ALIGNMENT);
        frame.add(label);
 
        //Display the window.
        frame.setSize(300, 290);
        frame.setVisible(true);
    }
}
