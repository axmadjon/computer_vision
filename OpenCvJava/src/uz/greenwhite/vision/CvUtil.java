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


    public static void resizeByWidth(@NotNull Mat src, @NotNull Mat dst, int size) {
        if (size == 0) throw new RuntimeException("size == 0");
        Size s = src.size();
        if (s.width < size) return;

        double resizePercent = 100 - ((size * 100) / s.width);
        int h = (int) Math.round(s.height - ((s.height / 100) * resizePercent));
        Imgproc.resize(src, dst, new Size(size, h));
    }

    public static void resizeByHeight(@NotNull Mat src, @NotNull Mat dst, int size) {
        if (size == 0) throw new RuntimeException("size == 0");
        Size s = src.size();
        if (s.height < size) return;

        double resizePercent = 100 - ((size * 100) / s.height);

        int w = (int) Math.round(s.width - ((s.width / 100) * resizePercent));
        Imgproc.resize(src, dst, new Size(w, size));
    }

    public static void resizeByMaximum(@NotNull Mat src, @NotNull Mat dst, int size) {
        Size s = src.size();
        if (s.width > s.height) resizeByWidth(src, dst, size);
        else resizeByHeight(src, dst, size);
    }
}
