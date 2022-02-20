

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ToolBar;
import javafx.scene.text.Text;
import javafx.scene.control.Label;
import javafx.scene.layout.TilePane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.ProgressBar;
import javafx.application.Platform;
import javafx.scene.layout.Priority;
import javafx.geometry.Pos;
import java.net.URLEncoder;
import java.net.URL;
import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.io.InputStreamReader;
import com.google.gson.JsonParser;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * This class represents a custom compenent for gallery app. The component contains everything
 * excluding the menubar containing file and help.
 **/
public class alrtInit extends VBox {

    //these nodes represent the toolbar
    HBox toolbar;
    Button playPaws;
    TextField searchBar;
    Button updateImg;
    Label searchTxt;

    //these nodes represent the musicImgTiles
    TilePane musicTiles;
    ImageView[] tileFrame;
    Boolean shuffle;
    String[] lastSearch;
    Timeline timeline;

    //these nodes represent the progress bar
    HBox loadBox;
    Text loadMsg;
    ProgressBar loadBar;

    //thesenodes are for the alert
    Alert tileAlert;
    Alert urlAlert;
    Alert encodingAlert;
    Alert iOAlert;

    /**
     * This is the constructor for the gallery tab which is a component which contains all needed
     * nodes for project 4 excluding the menu bar containging exit and about.
     **/
    public alrtInit() {
        super(); //call the super constructor of VBox
        toolbar = new HBox(10);
        playPaws = new Button("Pause"); //by default play
        searchBar = new TextField("country");
        updateImg = new Button("Update Images");
        searchTxt = new Label("Search Query:"); //text does not change
        toolbar.setAlignment(Pos.CENTER);
        HBox.setHgrow(searchBar, Priority.ALWAYS); //make textfield dynamicly change size

        //check the syntax of these two lines in the labmda
        updateImg.setOnAction(this::imgUpdate); //give an action to button
        playPaws.setOnAction(this::imgToggle); //giving an action to button
        toolbar.getChildren().addAll(playPaws, searchTxt, searchBar, updateImg); //toolbar fill

        shuffle = true; //by default, music tiles will shuffle
        musicTiles = new TilePane(); //use a tile pane of image views
        musicTiles.setPrefColumns(5); //5 columns
        musicTiles.setPrefRows(4); //4rows
        tileFrame = new ImageView[20]; //20 total tiles
        for (int i = 0; i < 20; i++) { //add all tiles with same characteristics
            tileFrame[i] = new ImageView();
            tileFrame[i].setFitHeight(100);
            tileFrame[i].setFitWidth(100);
            musicTiles.getChildren().add(tileFrame[i]);    //populate tilepane element
        }
        loadBox = new HBox(); //hbox to hold bottom progress box
        loadMsg = new Text("Images provided courtesy of iTunes"); //dynamic text based on progress
        loadBar = new ProgressBar(); //progress bar will show img update

        //populating all alert types
        tileAlert = new Alert(AlertType.WARNING);
        tileAlert.setTitle("Tile Count Warning");
        tileAlert.setContentText("There are not enough songs for that query");
        tileAlert.setResizable(true);
        urlAlert = new Alert(AlertType.WARNING);
        urlAlert.setTitle("Malformed URL Exception");
        urlAlert.setContentText("A Malformed URL Exception Occurred: see API for more");
        urlAlert.setResizable(true);
        encodingAlert = new Alert(AlertType.WARNING);
        encodingAlert.setTitle("Unsupported Encoding Exception");
        encodingAlert.setContentText("An Unsupported Encoding Exception Ocurred: see API for more");
        encodingAlert.setResizable(true);
        iOAlert = new Alert(AlertType.WARNING);
        iOAlert.setTitle("IO Exception");
        iOAlert.setContentText("An IO Exception Ocurred: see API for more");
        iOAlert.setResizable(true);

        loadBox.getChildren().addAll(loadBar, loadMsg); //populate loadbox element
        this.getChildren().addAll(toolbar, musicTiles, loadBox);//add all subelements to this VBox

        String[] urls = getImgUrl(searchBar.getText());
        this.lastSearch = urls;
        loadUpdate(urls);
        EventHandler<ActionEvent> handler = event -> {
            imgShuffle();
        };
        timelineStart(handler); //start the timeline shuffling
    }



    //toolbar methods
    /**
     * This method shoudl toggle the new generation of random images wihtin the tile pane.
     * NOTE THAT THIS WILL BE ON BY DEFAULT AND CALL THE TIMELINE to function.
     *
     * @param e is an action event such that this method can be tied to a physical button
     **/
    public void imgToggle(ActionEvent e) {
        if (shuffle) {
            playPaws.setText("Play"); //change button text
            timeline.pause(); //pause timeline, 2 second delay
        } else {
            playPaws.setText("Pause");
            //imgShuffle();
            timeline.play(); //play timeline
        }
        shuffle = !shuffle; //toggle the shuffle bool
    }

    /**
     * This method will odtain needed urls of images and check there are enough.
     * This method gets a string of img urls, updates the last search array, and then places the
     * images within the tilepane but on a seperate thread which also updates the progress bar.
     * Simultaneously, this method also will give a tile alert if not enough query responses.
     *
     * @param e this action event allows the method to be tied to a physical button.
     **/
    private void imgUpdate(ActionEvent e) {
        Runnable threadUpdate = () -> { //create a runnable thread
            try {
                String[] urls = getImgUrl(searchBar.getText()); //moved url getter into runnable
                this.lastSearch = urls; //update last search results for shuffle method
                loadUpdate(urls); //update urls
            } catch (IndexOutOfBoundsException IOOBE) { //catch statement will trigger alert
                Platform.runLater(() -> tileAlert.showAndWait());
            }
        };

        timeline.pause(); //bydefault pause the timeline before downloading
        runThread(threadUpdate); //run previous thread
        if (shuffle) { //if it was shuffling, then toggle the timeline
            timeline.play();
        }
    }


    /**
     * This method will obtain image urls via json to gson and will be threaded.
     * When this method was not threaded, it appeared to cause some lag.
     *
     * @param searchTxt this is the search term taken directly from the searchbar
     * @return String[] which contains all valid image urls from itunes api
     **/
    public String[] getImgUrl(String searchTxt) {
        try {
            String jsonTxt = URLEncoder.encode(searchTxt, "UTF-8");
            String jsonUrl = "https://itunes.apple.com/search?term=" +
                    jsonTxt + "&limit=200&media=music";

            //code taken from project documentation, credit to barnes/cotterl
            URL url = new URL(jsonUrl);
            System.out.println(url);
            InputStreamReader reader = new InputStreamReader(url.openStream());
            JsonElement je = JsonParser.parseReader(reader);
            JsonObject root = je.getAsJsonObject();
            JsonArray results = root.getAsJsonArray("results");

            //remove any elemetns without pictures
            for (JsonElement x: results) {
                JsonObject tempO = x.getAsJsonObject();
                if (tempO.get("artworkUrl100").isJsonNull()) {
                    results.remove(x);
                }
            }

            //results is an array of allt eh resutls of the query
            String[] rtn = new String[results.size()];

            for (int i = 0; i < rtn.length; i++) {
                JsonObject temp = results.get(i).getAsJsonObject(); //can omit this line i think
                rtn[i]  = temp.get("artworkUrl100").getAsString();
            }
            return rtn;

            //handle all the exceptions
        } catch (UnsupportedEncodingException e) {
            //System.out.println("UEE");
            Platform.runLater(() -> encodingAlert.showAndWait());
        } catch (MalformedURLException e) {
            //System.out.println("MUE");
            Platform.runLater(() -> urlAlert.showAndWait());
        } catch (IOException e) {
            // System.out.println("IOException occurred");
            Platform.runLater(() -> iOAlert.showAndWait());
            // tileAlert.showAndWait();
        }
        return this.lastSearch;
        //if query is unsuccessful, then use the last successful query
    }



    /**
     * This method when called will randomize a music tile using math.random.
     **/
    public void imgShuffle() {
        if (this.lastSearch.length > 21) { //only shuffles when more than 20 elements
            Image newImg = new Image(this.lastSearch[(int) (Math.random() * lastSearch.length)]);
            tileFrame[(int) (Math.random() * 20)].setImage(newImg);
        }
    }

    /**
     * Starts the timeline which will be paused when toggled by button.
     * This code was taken fromt the project description, i dont fully get it lol.
     *
     * @param handler this handler defines the code which runs in 2 second intervals
     **/
    public void timelineStart(EventHandler<ActionEvent> handler) {
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(2), handler);
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play(); //start the timeline by default

    }

    /**
     * Given several string urls, this method will populate the images with the urls defined.
     * This method has been replaced by load update and is NO LONGER USED IN MY PROGRAM.
     * THIS WAS A TEST METHOD.
     *
     * @param imgUrls this string[] contains valid image urls from the itunes api
     **/
    public void imgFill(String[] imgUrls) {
        for (int i = 0; i < 20 ; i++) {
            String tempUrl = imgUrls[i];
            Image tempImg = new Image(tempUrl);
            //implicitly using a lmbda expression of type runnable with platform ? maybe
            tileFrame[i].setImage(tempImg);
        }
    }


    /**
     * Dr Barnes method from the project documentation.
     *
     * @param progress this final double represents the percent of loading complete in the bar
     **/
    private void setProgress(final double progress) {
        Platform.runLater(() -> loadBar.setProgress(progress));
    } // setProgress


    //load box methods
    /**
     * This method will update the load bar and download imgs to show progress as well as the label.
     * when this method is called, it should be on another thread. This is the heart of my program,
     * if something with the img update doesnt work, check here first.
     *
     * @param imgUrls a string[] of VALID image urls, taken from the iTUNES API.
     **/
    public void loadUpdate(String[] imgUrls) {

        setProgress(0); //set progress to 0
        loadMsg.setText("Downloading Images..."); //change text dynamically
        Image[] tilesImg = new Image[20]; //create new image [] to store downloads
        for (int i = 0; i < 20; i++) { //traverse each string and DL imagges
            String tempUrl = imgUrls[i];
            tilesImg[i] = new Image(tempUrl);
            setProgress(1.0 * i / 20); //increment progress bar
        }

        for (int i = 0; i < 20; i++) { //traverse the tileframes and set an img into each
            tileFrame[i].setImage(tilesImg[i]);
        }

        setProgress(1); //set progress to 1
        loadMsg.setText("Images courtesy of iTunes"); //reset dynamic text

    }


    //general methods
    /**
     * This method is used as a helper method to run any threads which are defined by
     * a lambda expression which extends runnable.
     *
     * @param x tis refers to some class which extends runnable and is therefore- runnable lol
     **/
    public static void runThread(Runnable x) {
        Thread t = new Thread(x);
        t.setDaemon(false);
        t.start();
    }


    /**
     * This was a test method I had used
     public String[] testImgFill() {
     //testigncode here
     String[] test = new String[30];
     this.lastSearch = test;
     for (int i = 0; i < 20; i++) {
     if (i % 2 == 0) {
     test[i] = "";
     } else {
     test[i] = "";
     }
     }
     return test;
     //end of test
     **/
}