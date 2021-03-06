package uz.greenwhite.vision.java;

import com.sun.istack.internal.NotNull;
import com.sun.javafx.beans.annotations.NonNull;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;

public class CvUtil {

    public static void detectMultiScale(@NotNull CascadeClassifier cascade,
                                        @NotNull MatOfRect dstDetect,
                                        @NotNull Mat src,
                                        int resize) {
        Size size = src.size();
        Mat gray = src.clone();
        Imgproc.cvtColor(src, gray, Imgproc.COLOR_RGB2GRAY);
        if (Math.max(size.width, size.height) > resize) {
            Imgproc.resize(gray, gray, new Size(resize, resize));
            Imgproc.resize(src, src, new Size(resize, resize));
        }
        Size min = new Size(resize / 6, resize / 6);
        Size max = new Size(resize, resize);
        cascade.detectMultiScale(gray, dstDetect, 1.1, 1, Objdetect.CASCADE_SCALE_IMAGE, min, max);
    }

    @NonNull
    public static Mat cropImage(@NonNull Mat src, int x, int y, int size) {
        return cropImage(src, x, y, size, size);
    }

    @NonNull
    public static Mat cropImage(@NonNull Mat src, int x, int y, int width, int height) {
        Mat result = new Mat(src, new Rect(x, y, width, height));
        Mat crop = new Mat();
        result.copyTo(crop);
        return crop;
    }
}
