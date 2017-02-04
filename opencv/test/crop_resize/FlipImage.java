package crop_resize;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

import java.io.File;

import static org.opencv.core.CvType.CV_8UC3;

public class FlipImage {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static int id = 388;

    public static void main(String[] args) {
        File f = new File("E:\\z_opencv_test\\positive\\");
        for (File file : f.listFiles()) {
            Mat img = Highgui.imread(file.getAbsolutePath(), Highgui.CV_LOAD_IMAGE_UNCHANGED);
            Core.flip(img, img, 1);
            writeImage(img);
        }
    }

    private static void writeImage(Mat img) {
        Mat imgBmp = new Mat();
        img.convertTo(imgBmp, CV_8UC3);
        Highgui.imwrite("E:\\z_opencv_test\\good\\IMG_" + (id++) + ".png", imgBmp);
    }
}
