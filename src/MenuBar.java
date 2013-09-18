import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;


public class MenuBar extends JMenuBar{
	JMenu menu = new JMenu("Session Options");
	JMenuItem save = new JMenuItem("Save Current Session");
	JMenuItem open = new JMenuItem("Open Previous Session");
	JMenuItem delete = new JMenuItem("Delete a Saved Session");
	
	public MenuBar(ActionListener a, boolean option){
		menu = new JMenu("Session Options");
		save.addActionListener(a);
		open.addActionListener(a);
		delete.addActionListener(a);
		menu.add(open);
		if (option)
			menu.add(save);
		menu.add(delete);
		this.add(menu);
	}
}
