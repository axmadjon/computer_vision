package uz.greenwhite.vision;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.objdetect.CascadeClassifier;

import java.lang.reflect.Field;

@SuppressWarnings("Duplicates")
public class TestCascade {

    static {

        try {
            System.setProperty("java.library.path", "E:\\install\\computer_vision\\opencv\\build\\java\\x64");

            Field fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");
            fieldSysPath.setAccessible(true);
            fieldSysPath.set(null, null);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        System.loadLibrary("opencv_java245");
    }


    private static void rotate90(Mat img) {
        Core.transpose(img, img);
        Core.flip(img, img, 1);
    }


    public static void main(String[] args) {
//        File f = new File("E:\\z_opencv_test2\\positive\\");
//        int id = 0;
//        for (File file : f.listFiles()) {
//            Mat img = Highgui.imread(file.getAbsolutePath(), Imgcodecs.CV_LOAD_IMAGE_UNCHANGED);
//            Mat jpg = new Mat();
//            img.convertTo(jpg, CvType.CV_8UC3);
//            for (int i = 0; i < jpg.cols(); i++) {
//                for (int j = 0; j < jpg.rows(); j++) {
//                    double[] rgpa = jpg.get(j, i);
//                    if (rgpa[3] == 0) {
//                        jpg.put(j, i, 254, 254, 254, 254);
//                    }
//                }
//            }
//            Highgui.imwrite("E:\\z_result\\positive\\img" + (id++) + ".jpg", jpg);
//        }

        String path = "E:\\image\\opencv\\bottle\\find_object\\20161229_181238.jpg";
        Mat img = Highgui.imread(path, Highgui.CV_LOAD_IMAGE_COLOR);

        rotate90(img);
        //Imgproc.resize(img, img, new Size(1000, 1000));

        String cascadePath = "e:\\z_opencv_test2\\hogcascade\\cascade.xml";
        CascadeClassifier cascade = new CascadeClassifier(cascadePath);

        int[] size = {200, 400, 600, 800};
        for (int s : size) {
            DetectUtil.detectStepByStep(cascade, img, "E:\\z_result\\test", s, s);
        }
//        MatOfRect detect = new MatOfRect();
//        cascade.detectMultiScale(img, detect);
//
//        CvUtil.rectangleDetect(img, detect);

        //Highgui.imwrite("E:\\z_result\\hog_test.jpg", img);
    }
}
