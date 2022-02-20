

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

    VBox goBox;
    ImageView mascot;
    Button toggle;

    //these nodes represent the progress bar
    Text timer;
    ProgressBar loadBar;

    /**
     * This is the constructor for the gallery tab which is a component which contains all needed
     * nodes for project 4 excluding the menu bar containging exit and about.
     **/
    public alrtInit() {
        super(); //call the super constructor of VBox
        Image placeholder = new Image("file:resources/Banner.png");
        Image placeholder1 = new Image("file:resources/dog.png");
        this.contentBox = new HBox();
        this.loadBox = new HBox();

        this.configBox = new VBox();

        this.configSpec = new HBox();
        this.specPrompt = new Label("Enter # Spectators: ");
        this.specCount = new TextField("spec x");

        this.configTime = new HBox();
        this.timePrompt = new Label("Please enter duration: ");
        this.timeCount = new TextField("time x");

        this.countdownBox = new HBox();
        this.countdown = new Text("00:00 left");

        this.goBox = new VBox();
        this.mascot = new ImageView();
        this.toggle = new Button("Alert Go!");
        this.timer = new Text("Loading...");
        this.loadBar = new ProgressBar(1);


        this.mascot = new ImageView(placeholder1);
        this.bannerFrame = new ImageView(placeholder);
        bannerFrame.setPreserveRatio(true);
        bannerFrame.setFitWidth(500);
        mascot.setPreserveRatio(true);
        mascot.setFitHeight(160);

        this.goBox.setAlignment(Pos.CENTER);
        goBox.setPadding(new Insets(.1,.0,.1,.0));
        //toggle.setPadding(new Insets(0,0,0,0));

        timer.setTextAlignment(TextAlignment.RIGHT);

        configSpec.setAlignment(Pos.CENTER);
        configTime.setAlignment(Pos.CENTER);
        configBox.setPadding(new Insets(20,1.5,.25,1.5));
        configTime.setPadding(new Insets(20,1.5,5,1.5));
        configSpec.setPadding(new Insets(20,1.5,5,1.5));

        configSpec.getChildren().addAll(specPrompt, specCount);
        configTime.getChildren().addAll(timePrompt, timeCount);

        countdownBox.getChildren().add(countdown);
        goBox.getChildren().addAll(mascot, toggle);
        configBox.getChildren().addAll(configSpec, configTime, countdownBox);

        countdown.setTextAlignment(TextAlignment.CENTER);
        countdown.setFont(new Font(20));
        countdownBox.setAlignment(Pos.CENTER);

        loadBox.getChildren().addAll(loadBar, timer);
        contentBox.getChildren().addAll(configBox, goBox);
        this.getChildren().addAll(bannerFrame, contentBox, loadBox);


        
    }



}