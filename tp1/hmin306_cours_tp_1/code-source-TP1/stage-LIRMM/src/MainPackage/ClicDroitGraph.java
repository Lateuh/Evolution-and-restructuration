package MainPackage;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

class ClicDroitGraph extends MouseAdapter {
	private Graphique component;
	private ArrayList<String> liste;
	
	public ClicDroitGraph(Graphique component, ArrayList<String> liste) {
		super();
		this.component = component;
		this.liste = liste;
	}
	
    public void mousePressed(MouseEvent e) {
        if (e.isPopupTrigger())
            doPop(e);
    }

    public void mouseReleased(MouseEvent e) {
        if (e.isPopupTrigger() && e.getButton() == 3)
            doPop(e);
    }

    private void doPop(MouseEvent e) {
    	MenuClicDroitGraph menu = new MenuClicDroitGraph(component, liste);
        menu.show(e.getComponent(), e.getX(), e.getY());
    }

	public void setListe(ArrayList<String> liste) {
		this.liste = liste;
	}
}