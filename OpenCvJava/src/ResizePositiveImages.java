import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.lang.reflect.Field;

public class ResizePositiveImages {

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

//    private static boolean isSuited(Size size) {
//        int minSize = 100;
//        return size.width < minSize && size.height < minSize;
//    }
//
//    private static void resize(Mat img) {
//        Size size = img.size();
//        if (!isSuited(size)) {
//            do {
//                size.set(new double[]{size.width / 2, size.height / 2});
//            } while (!isSuited(size));
//            Imgproc.resize(img, img, size);
//        }
//    }

    public static void main(String[] args) {
        File f = new File("E:\\logo_test\\good\\");
        for (File file : f.listFiles()) {
            if (file.isDirectory())continue;
            String path = file.getAbsolutePath();
            Mat img = Imgcodecs.imread(path);
            if (!img.empty()) {
                Imgproc.resize(img, img, new Size(50, 50));
                writeImage(img);

            } else {
                System.out.println("img is empty (" + file.getAbsolutePath() + ")");
            }
        }
    }

    private static int id = 1;

    private static void writeImage(Mat img) {
        /*Mat imgBmp = new Mat();
        img.convertTo(imgBmp, CV_8UC3);*/
        String path = "E:\\logo_test\\good\\CV_IMG_" + (id++) + ".jpg";
        Imgcodecs.imwrite(path, img);
        System.out.println(path+" 1 0 0 50 50");
    }
}
