import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;

import java.util.ArrayList;

public class imageAlert extends Alert {

    TilePane spectators;

    public imageAlert(ArrayList<Image> spectators) {
        super(AlertType.INFORMATION);

        this.setTitle("Intruder Alert!");
        ButtonType close = new ButtonType("Close", ButtonBar.ButtonData.OK_DONE);
        this.setContentText("Tahsin Nabi, tjn92948@uga.edu, Version 1.00");
        //Image aboutImg = new Image("https://scontent-atl3-1.xx.fbcdn.net/v/t1.6435-9/72687449_2519325251446909_5658522496542965760_n.jpg?_nc_cat=107&ccb=1-5&_nc_sid=09cbfe&_nc_ohc=KDvpmWbpk8cAX9msVcH&_nc_ht=scontent-atl3-1.xx&oh=97dfbd9aa31c78381893b05a8800f1bb&oe=61C0C9F7");
        //ImageView imgView = new ImageView(aboutImg);
        imgView.setPreserveRatio(true);
        imgView.setFitHeight(300);
        this.setGraphic(imgView);
        this.setResizable(true);
    }
}
