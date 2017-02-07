package test;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import uz.greenwhite.vision.ObjectDetect;

import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class TestMain {

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
        String cascadePath = "e:\\gws_project\\opncv_train_cascade\\haarcascade2\\cascade.xml";
        String path = "E:\\image\\opencv\\bottle\\find_object\\20161229_181238.jpg";

        ArrayList<Rect> result = new ArrayList<>();
        long start = System.nanoTime();
        try {
            result = ObjectDetect.detect(cascadePath, path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Mat imread = Imgcodecs.imread(path);
        Scalar scalar = new Scalar(0, 255, 0);
        for (Rect r : result) {
            Imgproc.rectangle(imread, r.tl(), r.br(), scalar);
        }
        Imgcodecs.imwrite("E:\\z_result\\result.jpg", imread);

        System.out.println("end:" + (System.nanoTime() - start));
    }
}
