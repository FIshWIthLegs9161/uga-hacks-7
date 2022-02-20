
import javafx.application.Application;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

/**
 * Represents an iTunes GalleryApp which allows for song queries and has a dynamic graphics system.
 */
public class alrt extends Application {


    Alert alert;

    /** {@inheritdoc}
     *
     * This is the start method for the gallery app class.
     * @param stage it takes in teh stage created by javafx, stage will become populated with scene
     */
    @Override
    public void start(Stage stage) {
        VBox pane = new VBox();
        Scene scene = new Scene(pane, 502, 520);
        stage.setMaxWidth(502);
        stage.setMaxHeight(520);
        stage.setTitle("GalleryApp!");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
        //^^ appropriate sizing for the galleryapp

        //this block of code will setup the menubar
        MenuBar menuBar = new MenuBar();
        Menu fTab = new Menu("File");
        Menu hTab = new Menu("Help");
        MenuItem exit = new MenuItem("Exit");
        MenuItem about = new MenuItem("About");
        fTab.getItems().add(exit);
        hTab.getItems().add(about);
        menuBar.getMenus().addAll(fTab, hTab);

        //this block of code will setup the about me section
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About Tahsin Nabi");
        ButtonType close = new ButtonType("Close", ButtonData.OK_DONE);
        alert.setContentText("Tahsin Nabi, tjn92948@uga.edu, Version 1.00");
        Image aboutImg = new Image("https://scontent-atl3-1.xx.fbcdn.net/v/t1.6435-9/72687449_2519325251446909_5658522496542965760_n.jpg?_nc_cat=107&ccb=1-5&_nc_sid=09cbfe&_nc_ohc=KDvpmWbpk8cAX9msVcH&_nc_ht=scontent-atl3-1.xx&oh=97dfbd9aa31c78381893b05a8800f1bb&oe=61C0C9F7");
        ImageView imgView = new ImageView(aboutImg);
        imgView.setPreserveRatio(true);
        imgView.setFitHeight(300);
        alert.setGraphic(imgView);
        alert.setResizable(true);
        //alert.showAndWait();

        //when about is pressed, pop an alert
        about.setOnAction(this::aboutAlert);
        //awhen file is pressed, exit the program
        exit.setOnAction(this::exitApp);

        VBox mBar = new VBox(menuBar); //place menu bar into vbox
        VBox gTab = new alrtInit(); //create the gallery tab

        pane.getChildren().addAll(mBar, gTab); //populate the pane
    } // start


    /**
     * This method will pop up an about me alert.
     *
     * @param e this actionevent will allow for the method to connect to a physical menuitem
     **/
    public void aboutAlert(ActionEvent e) {
        alert.showAndWait();
    }

    /**
     * This method will forcefully close the application.
     *
     * @param e this actionevent will allow for the method to be connected to a physical menuitem
     **/
    public void exitApp(ActionEvent e) {
        System.exit(0);
    }


} // GalleryApp
