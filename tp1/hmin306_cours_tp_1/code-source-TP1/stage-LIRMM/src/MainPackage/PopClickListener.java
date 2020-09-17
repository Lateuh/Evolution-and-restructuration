package MainPackage;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

class PopClickListener extends MouseAdapter {
	private PanneauTempsReel component;
	private ArrayList<String> liste;
	
	public PopClickListener(PanneauTempsReel component, ArrayList<String> liste) {
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
        PopUpDemo menu = new PopUpDemo(component, liste);
        menu.show(e.getComponent(), e.getX(), e.getY());
    }

	public void setListe(ArrayList<String> liste) {
		this.liste = liste;
	}
}