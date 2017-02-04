package crop_resize;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import java.io.File;

import static org.opencv.core.CvType.CV_8UC3;

public class ResizePositiveImages {

    static {
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
        File f = new File("E:\\z_objects\\");
        for (File file : f.listFiles()) {
            String path = file.getAbsolutePath();
            Mat img = Highgui.imread(path, Highgui.CV_LOAD_IMAGE_UNCHANGED);
            System.out.println("path=" + path);
            if (!img.empty()) {
                Size size = img.size();
                if (size.width > 500 || size.height > 500) {
                    Imgproc.GaussianBlur(img, img, new Size(15, 15), 0);
                } else if (size.width > 200 || size.height > 200) {
                    Imgproc.GaussianBlur(img, img, new Size(5, 5), 0);
                }
                Imgproc.resize(img, img, new Size(30, 100));
                writeImage(img);

            } else {
                System.out.println("img is empty (" + file.getAbsolutePath() + ")");
            }
        }
    }

    private static int id = 338;

    private static void writeImage(Mat img) {
        Mat imgBmp = new Mat();
        img.convertTo(imgBmp, CV_8UC3);
        Highgui.imwrite("E:\\z_opencv_test\\good\\IMG_" + (id++) + ".png", imgBmp);
    }
}
