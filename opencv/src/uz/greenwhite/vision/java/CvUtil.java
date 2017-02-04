package uz.greenwhite.vision.java;

import com.sun.istack.internal.NotNull;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;

public class CvUtil {

    public static void detectMultiScale(@NotNull CascadeClassifier cascade,
                                        @NotNull MatOfRect dstDetect,
                                        @NotNull Mat src,
                                        int width, int height) {
        Size size = src.size();
        Mat gray = src.clone();
        Imgproc.cvtColor(src, gray, Imgproc.COLOR_RGB2GRAY);
        if (size.width > width && size.height > height) {
            Imgproc.resize(gray, gray, new Size(width, height));
            Imgproc.resize(src, src, new Size(width, height));
        }
        cascade.detectMultiScale(gray, dstDetect, 1.1, 1, Objdetect.CASCADE_SCALE_IMAGE,
                new Size(width / 6, height / 6), new Size(width, height));
    }

    @NotNull
    public static Mat cropImage(@NotNull Mat src, int x, int y, int width, int height) {
        Mat result = new Mat(src, new Rect(x, y, width, height));
        Mat crop = new Mat();
        result.copyTo(crop);
        return crop;
    }

    public static void rectangleDetect(@NotNull Mat src, @NotNull MatOfRect detect) {
        if (detect.empty()) return;
        rectangleDetect(src, detect, new Scalar(0, 255, 0));
    }

    public static void rectangleDetect(@NotNull Mat src, @NotNull MatOfRect detect, @NotNull Scalar scalar) {
        long bestFoundObjects = 0;
        long neutralObjects = 0;
        Size size = src.size();
        for (Rect rect : detect.toArray()) {
            if (Math.max(rect.width, rect.height) < (Math.max(size.width, size.height)) / 2) {
                scalar = new Scalar(255, 0, 0);
                neutralObjects++;
            } else {
                bestFoundObjects++;
            }
            Imgproc.rectangle(src, rect.tl(), rect.br(), scalar);
        }
        System.out.println("Found Objects BEST:" + bestFoundObjects + ", Neutral:" + neutralObjects);
    }
}
