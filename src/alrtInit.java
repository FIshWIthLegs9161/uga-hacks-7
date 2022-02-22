

import face.FaceDetector;
import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ToolBar;
import javafx.scene.text.Font;
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
import java.util.ArrayList;

import com.google.gson.JsonParser;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * This class represents a custom compenent for gallery app. The component contains everything
 * excluding the menubar containing file and help.
 **/
public class alrtInit extends VBox {

    ImageView bannerFrame;
    HBox contentBox;
    HBox loadBox;

    //these nodes represent the content
    VBox configBox;
    HBox configSpec;
    Label specPrompt;
    TextField specCount;
    HBox configTime;
    Label timePrompt;
    TextField timeCount;
    Text countdown;
    HBox countdownBox;
    TextField number;
    Label numPrompt;
    HBox configNum;

    VBox goBox;
    ImageView mascot;
    Button toggle;

    //these nodes represent the progress bar
    Text timer;
    ProgressBar loadBar;
    Alert timeUp; //time is up display this alert


    boolean privacy;
    Timeline timeline;
    int stopwatch;
    int timeInterval; //rate of scanning in seconds
    int duration; //how long session, by default -1 menaing indefinite
    int userNum;
    String userPhone;

    /**
     * This is the constructor for the gallery tab which is a component which contains all needed
     * nodes for project 4 excluding the menu bar containging exit and about.
     **/
    public alrtInit() {
        super(); //call the super constructor of VBox

        this.userNum = 1; //1 by default
        this.timeInterval = 1; //rate of scanning, should be a constant tbh
        this.stopwatch = 0;
        this.duration = -1; //by default negative, meanign indefinite
        this.userPhone = "+15719696224";

        this.privacy = false;

        Image placeholder = new Image("file:resources/Banner.png");
        Image placeholder1 = new Image("file:resources/dogOpen.png");
        this.contentBox = new HBox();
        this.loadBox = new HBox();

        this.configBox = new VBox();

        this.configSpec = new HBox();
        this.specPrompt = new Label("Spectators: ");
        this.specCount = new TextField("1");

        this.configTime = new HBox();
        this.timePrompt = new Label("Session Time (min): ");
        this.timeCount = new TextField("30");

        this.configNum = new HBox();
        this.numPrompt = new Label("Phone Number: ");
        this.number = new TextField("+15719696224");

        this.countdownBox = new HBox();
        this.countdown = new Text("minutes : seconds");

        this.goBox = new VBox();
        this.mascot = new ImageView();
        this.toggle = new Button("WatchDog Off");
        this.timer = new Text("Loading...");
        this.loadBar = new ProgressBar(1);


        this.mascot = new ImageView(placeholder1);
        this.bannerFrame = new ImageView(placeholder);
        bannerFrame.setPreserveRatio(true);
        bannerFrame.setFitWidth(500);
        mascot.setPreserveRatio(true);
        mascot.setFitHeight(160);

        this.goBox.setAlignment(Pos.CENTER);
        goBox.setPadding(new Insets(.1, .0, .1, .0));
        //toggle.setPadding(new Insets(0,0,0,0));

        timer.setTextAlignment(TextAlignment.RIGHT);

        configSpec.setAlignment(Pos.CENTER);
        configTime.setAlignment(Pos.CENTER);
        configBox.setPadding(new Insets(20, 1.5, .25, 1.5));
        configTime.setPadding(new Insets(20, 1.5, 5, 1.5));
        configSpec.setPadding(new Insets(20, 1.5, 5, 1.5));
        configNum.setPadding(new Insets(20, 1.5, 5, 1.5));

        configSpec.getChildren().addAll(specPrompt, specCount);
        configTime.getChildren().addAll(timePrompt, timeCount);
        configNum.getChildren().addAll(numPrompt, number);

        countdownBox.getChildren().add(countdown);
        goBox.getChildren().addAll(mascot, toggle);
        configBox.getChildren().addAll(configSpec, configTime,configNum, countdownBox);
        toggle.setOnAction(this::privacyToggle);

        countdown.setTextAlignment(TextAlignment.CENTER);
        countdown.setFont(new Font(20));
        countdownBox.setAlignment(Pos.CENTER);

        loadBox.getChildren().addAll(loadBar, timer);
        contentBox.getChildren().addAll(configBox, goBox);
        this.getChildren().addAll(bannerFrame, contentBox); //when reimplementing in the future add the loadbox here

        this.timeUp = new Alert(AlertType.WARNING);
        timeUp.setContentText("Time is up! WatchDog letting you know :)");
        timeUp.setTitle("Time is Up!");

        EventHandler<ActionEvent> handler = event -> {
            faceScan();
            stopwatch = stopwatch + timeInterval;
            countdown.setText(getTime());
        };
        timelineStart(handler); //start the timeline shuffling
    }

    /**
     * This method shoudl toggle the new generation of random images wihtin the tile pane.
     * NOTE THAT THIS WILL BE ON BY DEFAULT AND CALL THE TIMELINE to function.
     *
     * @param e is an action event such that this method can be tied to a physical button
     **/
    public void privacyToggle(ActionEvent e) {
        privacy = !privacy;
        if (privacy)
        {
            toggle.setText("WatchDog On"); //change button text
            duration = getDuration();
            userNum = getUserNum();
            userPhone = getUserPhone();
            mascot.setImage(new Image ("file:resources/dogClosed.png"));
            privacyFilter();
            System.out.println("duration= " + duration + "\n" +"users= " + userNum + "\n" + "Phone#= " + userPhone);
        } else
        {
            toggle.setText("WatchDog Off");
            stopwatch = 0;
            mascot.setImage(new Image ("file:resources/dogOpen.png"));
            timeline.pause();
            countdown.setText("minutes : seconds");
        }
    }

    //this is the core privacy filter method which will start the timeline
    public void privacyFilter() {
        timeline.play();
    }

    /**
     * Starts the timeline which will be paused when toggled by button.
     * This code was taken fromt the project description, i dont fully get it lol.
     *
     * @param handler this handler defines the code which runs in 2 second intervals
     **/
    public void timelineStart(EventHandler<ActionEvent> handler) {
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(timeInterval), handler);
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(keyFrame);
        //timeline.play(); //start the timeline by default

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
     * Dr Barnes method from the project documentation.
     *
     * @param progress this final double represents the percent of loading complete in the bar
     **/
    private void setProgress(final double progress) {
        Platform.runLater(() -> loadBar.setProgress(progress));
    } // setProgress

    /**
     * This method will by jtied to the time line and be called in intervals to scan for faces
     */
    public void faceScan() {
        //if face scan detects a > num of users than the expected value or the  0< duration is  <= time elapsed
        //return either an alert that there is a new user or that time is up
        //FaceDetector scanner = new FaceDetector();
        //ArrayList<Image> specs = scanner.detectFace();

        if ((duration <= stopwatch)) {
            //Platform.runLater(() -> timeUp.showAndWait());
            Platform.runLater(() -> timeUp.showAndWait());
            toggle.fire();
        } //else if (specs.size() > userNum) {
            //Alert x = new imageAlert(specs, userPhone);
            // if retursnnull then set duration to - 10
            //x.showAndWait();
        //}
    }

    //returns the time elapsed in minutes and seconds
    public String getTime() {
        int seconds = stopwatch % 60;
        int minutes = stopwatch / 60;
        String rtn = minutes + " : " + seconds;
        return rtn;
    }

    //get time duration in seconds
    public int getDuration() {
        int rtn = Integer.parseInt(timeCount.getText());
        rtn *= 60;
        return rtn;

    }

    public int getUserNum() {
        return Integer.parseInt(specCount.getText());

    }

    public String getUserPhone() {
        return number.getText();
    }


}



