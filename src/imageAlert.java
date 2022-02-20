import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import java.net.URI;

import java.util.ArrayList;

public class imageAlert extends Alert {

    TilePane spectators;
    public static final String ACCOUNT_SID = System.getenv("AC0c9aab6167e5a8e51ed47e618756df69");
    public static final String AUTH_TOKEN = System.getenv("9f2f116dc55338c41d1fdd521acc6464");

    public imageAlert(ArrayList<Image> spectators, String phoneNum) {
        super(AlertType.INFORMATION);
        //calling the api
        String alertText = "Intruder Alert!, WatchDog found somebody snooping.";
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(
                new com.twilio.type.PhoneNumber(phoneNum),
                "MG1706077535a377978302207c8ea186b7",
                alertText)
                .create();

        System.out.println(message.getSid());

        HBox specFrames = new HBox();
        this.setTitle("Intruder Alert!");
        ButtonType close = new ButtonType("Close", ButtonBar.ButtonData.OK_DONE);
        this.setContentText("Found an Intruder!");

        for(Image spec: spectators) {
            ImageView specFrame = new ImageView(spec);
            specFrames.getChildren().add(specFrame);
            //imgView.setPreserveRatio(true);
            //imgView.setFitHeight(300);
        }
        this.setResizable(false);
        this.setGraphic(specFrames);
    }
}
