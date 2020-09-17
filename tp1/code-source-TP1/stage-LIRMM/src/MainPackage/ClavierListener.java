package MainPackage;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ClavierListener implements KeyListener {

	public void keyPressed(KeyEvent event) {
		// TODO Auto-generated method stub
		if(!isNumeric(event.getKeyChar()))
			event.getKeyChar();	
	}

	public void keyReleased(KeyEvent event) {
		// TODO Auto-generated method stub
	}

	public void keyTyped(KeyEvent event) {
		// TODO Auto-generated method stub
		
	}
	
	private boolean isNumeric(char carac){
	    try {
	      Integer.parseInt(String.valueOf(carac));
	    }
	    catch (NumberFormatException e) {
	      return false;            
	    }
	    return true;
	}

}
