/**
 * 
 */
package driver;


import controller.ControllerForPractitionerLogin;
import view.PractitionerLoginGUI;

/**
 * @author sriram
 *
 */
public class Driver {

	//6137039
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PractitionerLoginGUI view= new PractitionerLoginGUI();
		ControllerForPractitionerLogin controllerForPracLogin = new ControllerForPractitionerLogin(view);
	}

}
