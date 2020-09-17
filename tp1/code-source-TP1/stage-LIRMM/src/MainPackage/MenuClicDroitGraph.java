package MainPackage;

import java.util.ArrayList;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

class MenuClicDroitGraph extends JPopupMenu {
	private static final long serialVersionUID = 1L;
		
    public MenuClicDroitGraph(Graphique component, ArrayList<String> liste) {
    	for (int i = 0; i< liste.size(); i++) {
    		JMenuItem anItem;
    		String champ = liste.get(i);
    		anItem = new JMenuItem(champ);
            anItem.addActionListener(event -> component.setIndex(liste.indexOf(champ)));
            add(anItem);
    	}      
    }
}
