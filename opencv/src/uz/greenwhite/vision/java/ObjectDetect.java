package uz.greenwhite.vision.java;

import com.sun.javafx.beans.annotations.NonNull;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.objdetect.CascadeClassifier;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class ObjectDetect {

    private static void checkParameter(String cascadePath, String imgPath) throws NullPointerException {
        if (cascadePath == null || cascadePath.length() == 0)
            throw new NullPointerException("cascade path null or empty");

        if (imgPath == null || imgPath.length() == 0) throw new NullPointerException("image path null or empty");
    }

    public static void detect(@NonNull ArrayList<Rect> dst, @NonNull String cascade, @NonNull String img)
            throws NullPointerException, FileNotFoundException {
        checkParameter(cascade, img);

        Mat src = Imgcodecs.imread(img, Imgcodecs.CV_LOAD_IMAGE_COLOR);
        if (src.empty()) throw new FileNotFoundException(String.format("image not found [%s]", img));

        CascadeClassifier classifier = new CascadeClassifier(cascade);
        if (classifier.empty()) throw new FileNotFoundException(String.format("cascade not found [%s]", cascade));

        ArrayList<RectDetect> detects = new ArrayList<>();

        Size size = src.size();
        long count = Math.round(Math.min(size.width, size.height) / 200);

        for (int i = 0; i < count; i++) {
            DetectUtil.detectStepByStep(classifier, detects, src, (i + 1) * 200);
        }

        make(detects, dst);
    }

    //TODO rename method name
    private static void make(ArrayList<RectDetect> src, ArrayList<Rect> dst) {

    }

}
