package uz.greenwhite.vision;

import com.sun.istack.internal.NotNull;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;

public class CvUtil {

    private static final Object TAG = new Object();

    private static void detectMultiScale(CascadeClassifier cascade, Mat img, MatOfRect dst, Size min, Size max) {
        synchronized (TAG) {
            cascade.detectMultiScale(img, dst, 1.1, 1, Objdetect.CASCADE_SCALE_IMAGE, min, max);
        }
    }

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
        Size min = new Size(resize / 4, resize / 4);
        Size max = new Size(resize, resize);
        detectMultiScale(cascade, gray, dstDetect, min, max);
    }

    @NotNull
    public static Mat cropImage(@NotNull Mat src, int x, int y, int size) {
        return cropImage(src, x, y, size, size);
    }

    @NotNull
    public static Mat cropImage(@NotNull Mat src, int x, int y, int width, int height) {
        Mat result = new Mat(src, new Rect(x, y, width, height));
        Mat crop = new Mat();
        result.copyTo(crop);
        return crop;
    }
}
