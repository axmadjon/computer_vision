package test;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.lang.reflect.Field;

/**
 * Created by pc on 12.02.2017.
 */
public class DetectLogo {


    static {

        try {
            System.setProperty("java.library.path", "e:\\install\\computer_vision\\opencv_2\\opencv\\build\\java\\x64\\");

            Field fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");
            fieldSysPath.setAccessible(true);
            fieldSysPath.set(null, null);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) {
        Mat imread = Imgcodecs.imread("E:\\image\\opencv\\bottle\\pepsiCo\\IMG_20161230_131018.jpg");
        //Core.flip(imread, imread, 1);
        Size s = imread.size();
        Imgproc.resize(imread, imread, new Size(s.width / 6, s.height / 6));
        CascadeClassifier cascade = new CascadeClassifier("e:\\logo_test\\haarcascade\\cascade.xml");

        MatOfRect detect = new MatOfRect();

        Mat gray = new Mat();
        Imgproc.cvtColor(imread, gray, Imgproc.COLOR_RGB2GRAY);
        cascade.detectMultiScale(gray, detect);

        Rect[] rects = detect.toArray();
        if (rects.length > 0) {
            for (Rect r : rects) {
                Imgproc.rectangle(imread, r.tl(), r.br(), new Scalar(0, 255, 0));
            }
        }

        Imshow.show(imread);
    }
}
