package face;
/**
 * Reference:
 * https://github.com/sarxos/webcam-capture/tree/master/webcam-capture-examples/webcam-capture-detect-face
 * http://openimaj.org/
 */

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.apache.xmlgraphics.image.loader.cache.ImageKey;
import org.openimaj.image.FImage;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.processing.face.detection.DetectedFace;
import org.openimaj.image.processing.face.detection.HaarCascadeDetector;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.List;

import java.util.ArrayList;
import javax.swing.JFrame;

public class FaceDetector {

    private static final long serialVersionUID = 1L;

    private static final HaarCascadeDetector detector = new HaarCascadeDetector();
    private Webcam webcam = null;
    private BufferedImage img = null;
    private List<DetectedFace> faces = null;

    public FaceDetector() {

        webcam = Webcam.getDefault();
        webcam.setViewSize(WebcamResolution.VGA.getSize());
        webcam.open(true);
        img = webcam.getImage();
        webcam.close();
    }

   // public static ImagePanel[][] imageKeeper = new ImagePanel[][];


    //will return an array list of images of peoples faces, will return null if nobody is found
    public ArrayList<Image> detectFace(){
        ArrayList<Image> imageKeeper = new ArrayList<Image>();
        JFrame fr=new JFrame("Discovered Faces");
        faces = detector.detectFaces(ImageUtilities.createFImage(img));
        if (faces == null) {
            System.out.println("No faces found in the captured image");
            return null;
        }

        Iterator<DetectedFace> dfi = faces.iterator();
        while (dfi.hasNext()) {
            DetectedFace face = dfi.next();
            FImage image1 = face.getFacePatch();
            Image image = SwingFXUtils.toFXImage(ImageUtilities.createBufferedImage(image1), null);
            imageKeeper.add(image);

        }

        return imageKeeper;
        //printImageKeeper();
    }


    public void printImageKeeper() {
       System.out.println("x  people are watching");
    }
}