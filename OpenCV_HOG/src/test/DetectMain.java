package test;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import uz.greenwhite.vision.DetectOfRect;
import uz.greenwhite.vision.ObjectDetect;

import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class DetectMain {
    static {

        try {
            System.setProperty("java.library.path", "e:\\install\\computer_vision\\opencv\\build\\java\\x64\\");

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
        String cascadePath = "e:\\gws_project\\opncv_train_cascade\\hog_cascade\\cascade.xml";
        String path = "E:\\image\\opencv\\bottle\\find_object\\20161229_181245.jpg";

        ArrayList<DetectOfRect> result = new ArrayList<>();
        long start = System.nanoTime();
        try {
            result = ObjectDetect.detect(cascadePath, path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Mat imread = Highgui.imread(path);
        Core.transpose(imread, imread);
        Core.flip(imread, imread, 1);

        for (DetectOfRect r : result) {
            //if (r.list.size() <= 10) continue;
            Scalar scalar = new Scalar(0, 100 + r.list.size(), 0);
            Core.rectangle(imread, r.rect.tl(), r.rect.br(), scalar);
            Core.putText(imread, "Object " + r.list.size(),
                    r.rect.tl(), Core.FONT_HERSHEY_PLAIN, 1, scalar);
        }
        Highgui.imwrite("E:\\z_result\\result.jpg", imread);

        System.out.println("end:" + (System.nanoTime() - start));
    }
}
