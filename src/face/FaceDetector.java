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
import org.openimaj.image.FImage;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.processing.face.detection.DetectedFace;
import org.openimaj.image.processing.face.detection.HaarCascadeDetector;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import java.util.ArrayList;
import javax.swing.JFrame;

public class FaceDetector extends JFrame {

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
        ImagePanel panel = new ImagePanel(img);
        panel.setPreferredSize(WebcamResolution.VGA.getSize());
        add(panel);

        setTitle("Face Recognizer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

   // public static ImagePanel[][] imageKeeper = new ImagePanel[][];
    static ArrayList<Image> imageKeeper = new ArrayList<Image>();
    int size = 0;

    public void detectFace(){
        JFrame fr=new JFrame("Discovered Faces");
        faces = detector.detectFaces(ImageUtilities.createFImage(img));
        if (faces == null) {
            System.out.println("No faces found in the captured image");
            return;
        }

        Iterator<DetectedFace> dfi = faces.iterator();
        while (dfi.hasNext()) {
            DetectedFace face = dfi.next();
            FImage image1 = face.getFacePatch();
            ImagePanel p = new ImagePanel(ImageUtilities.createBufferedImage(image1));
            Image image = SwingFXUtils.toFXImage(ImageUtilities.createBufferedImage(image1), null);
            imageKeeper.add(image);

            fr.add(p);
        }

        fr.setLayout(new FlowLayout(0));
        fr.setSize(500,500);
        fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fr.setVisible(true);

        size = imageKeeper.size();
        printImageKeeper();
    }

    public void printImageKeeper() {
       System.out.println(size + " people are watching");
    }

    public static void main(String[] args) throws IOException {
           new FaceDetector().detectFace();
       }
}