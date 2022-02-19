import javafx.application.Application;

public class ugaHacks7Driver {

    public static void main(String[] args) {
        try {
            Application.launch(atm.class, args); //this will start the javafx applicaiton
        } catch (Exception e) {
            System.err.println(e.getMessage()); //print any errors without crashing the program for debugging
        }
    }
}