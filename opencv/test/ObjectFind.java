import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import uz.greenwhite.vision.DetectUtil;

import java.lang.reflect.Field;

@SuppressWarnings("Duplicates")
public class ObjectFind {
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

    private static boolean isSuited(Size size, int width, int height) {
        return size.width < width || size.height < height;
    }

    private static Mat resize(Mat img, int width, int height) {
        Mat mat = new Mat();
        Size size = img.size();
        if (!isSuited(size, width, height)) {
            do {
                size.set(new double[]{size.width - 100, size.height - 100});
            } while (!isSuited(size, width, height));

            if (size.width < width) {
                int r = (int) (width - size.width);

                size.width = (size.width + r);
                size.height = (size.height + r);
            } else if (size.height < height) {
                int r = (int) (height - size.height);

                size.width = (size.width + r);
                size.height = (size.height + r);
            }
            if (1000 < width || 1000 < height) {
                Imgproc.GaussianBlur(img, img, new Size(5, 5), 0);
            }
            Imgproc.resize(img, mat, size);
        }
        return mat.empty() ? img : mat;
    }

//  public static void main(String[] args) {
//       Mat mat = Highgui.imread("E:\\z_opencv_test\\pe.jpg");
//
//      MatOfKeyPoint keypoints = new MatOfKeyPoint();
//      FeatureDetector surf = FeatureDetector.create(FeatureDetector.SURF);
//      surf.detect(mat, keypoints);
//
//      Highgui.imwrite("E:\\z_opencv_test\\test3.jpg", mat);
//  }


    private static void rotate90(Mat img) {
        Core.transpose(img, img);
        Core.flip(img, img, 1);
    }


    public static void main(String[] args) {
//        String path2 = "E:\\image\\opencv\\bottle\\find_object\\20161229_181232.jpg";
//        Mat img2 = Imgcodecs.imread(path2, Imgcodecs.CV_LOAD_IMAGE_COLOR);
//        img2 = resize(img2, 100, 100);
//        Imshow ui = new Imshow("Hello");
//        ui.showImage(img2);

//        Imshow ui = new Imshow("Hello");
//        VideoCapture capture = new VideoCapture();
//        capture.open("E:\\qvz.mp4");
//        Mat mat = new Mat();
//        if (capture.isOpened()) {
//            while (capture.read(mat)) {
//                ui.showImage(mat);
//            }
//
//            System.out.println("exit in while loop");
//        } else {
//            System.out.println("Can't open camera");
//        }
//        capture.release();


        String path = "E:\\image\\opencv\\bottle\\find_object\\20161229_181238.jpg";
        Mat img = Highgui.imread(path, Highgui.CV_LOAD_IMAGE_COLOR);

        if (img.empty()) System.out.println("img is empty");
        rotate90(img);
        Imgproc.GaussianBlur(img, img, new Size(3, 3), 0, 0);

        String cascadePath = "E:\\gws_project\\opncv_train_cascade\\hog_cascade\\cascade.xml";
//        String cascadePath = "e:\\z_opencv_test2\\hogcascade\\cascade.xml";
        CascadeClassifier cascade = new CascadeClassifier(cascadePath);
        String dstPath = "E:\\z_result\\test";

        Size size = img.size();
        double min = Math.min(size.width, size.height);
        double count = min / 200;

        for (int i = 0; i < count; i++) {
            int cropSize = (i + 1) * 200;
            DetectUtil.detectStepByStep(cascade, img, dstPath, cropSize, cropSize);
            System.out.println("end:" + cropSize);

        }
/*        int[] boundingBox = {200, 400, 600, 800, 1000, 1200, 1400, 1600, 1800, 2000};
        for (int bb : boundingBox) {
            DetectUtil.detectStepByStep(cascade, img, dstPath, bb, bb);
            System.out.println("end:" + bb);
        }
*/
    }
}
