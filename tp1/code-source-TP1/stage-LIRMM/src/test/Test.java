package test;

import java.io.IOException;
import java.text.ParseException;
import MainPackage.Controller;
import MainPackage.Day;
import MainPackage.OSvalidator;

public class Test {
	public static void main(String[] args) throws NumberFormatException, ParseException, IOException, InterruptedException {
		//verifier si n'hésésite une installation
		Controller control = null;
		if (OSvalidator.isWindows()) {///"../../"; //! pour linux : collectFolderPath = "/etc/consommation/";
			Controller.setFolderPath("../../");
			Day.setOS(false);
			control = new Controller(false);
		}
		if (OSvalidator.isUnix()) {
			Controller.setFolderPath("/etc/consommation/");
			Day.setOS(true);
			control = new Controller(true);
		}
    	while (true) {
    		control.updateVisualInfo();
    		Thread.sleep(10000);
    	}
    }
}

