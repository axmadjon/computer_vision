package test;

import org.opencv.core.*;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import uz.greenwhite.vision.CvUtil;
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

    private static void filter(Mat src) {
        int rows = src.rows();
        int cols = src.cols();
        for (int j = 0; j < rows; j++) {
            for (int k = 0; k < cols; k++) {
                double[] c = src.get(j, k);
                double gray = c[0];
                gray = gray < 20 ? (gray * 2) : gray;
                /*if (rows - 1 != j && cols - 1 != k) {
                    double upGray = src.get(j + 1, k + 1)[0];
                    double result = gray - upGray;
                    if (result > -100 || result < 100) {
                        gray = Math.min(gray, upGray);
                    }
                }*/
                c[0] = gray;//< 20 ? (gray * 2) : gray;
                // c[0] = (gray >= 190 ? 255 : gray);
                src.put(j, k, c);
            }
        }
    }

    public static void main(String[] args) {
//        File f = new File("E:\\z_hog_train\\good\\");
//        StringBuilder sb = new StringBuilder();
//        for (File file : f.listFiles()) {
//            String path = file.getAbsolutePath();
//            Mat imread = Highgui.imread(path);
//            Size size = imread.size();
//            sb.append(path).append(" 1 0 0 ")
//                    .append((int)size.width).append(" ").append((int)size.height).append("\n");
//        }
//        System.out.println(sb.toString());
//        if (true) return;
/*
        Mat img = Highgui.imread("E:\\train_object\\object.jpg", Highgui.IMREAD_GRAYSCALE);
//        Imgproc.blur(img, img, new Size(15, 15));
//        resizeByMaximum(img, img, 100);
        Size size = img.size();
        Mat clone = img.clone();
        filter(clone);
        Highgui.imwrite("E:\\train_object\\IMG_" + size + ".jpg", clone);
        for (int i = 1; i < 10; i++) {
            Mat resize = new Mat();
            resizeByMaximum(img, resize, (i * 10));
            size = resize.size();
            filter(resize);
            Highgui.imwrite("E:\\train_object\\IMG_" + size + ".jpg", resize);
            System.out.println(resize.dump());
            System.out.println("++++++++++++++++++++++++++++++++++++++++");
        }

        if (true) return;*/
//        String cascadePath = "e:\\z_hog_train\\hogcascade\\cascade.xml";
//        String path = "E:\\image\\opencv\\bottle\\find_object\\20161229_181302.jpg";
//        CascadeClassifier cascadeLogo = new CascadeClassifier("e:\\logo_test\\hogcascade\\cascade.xml");
//
//        /*Mat image = Highgui.imread("E:\\z_result\\result.jpg");
////        Core.transpose(image, image);
////        Core.flip(image, image, 1);
//        Mat m = new Mat();
//        Imgproc.cvtColor(image, m, Imgproc.COLOR_RGB2GRAY);
//        MatOfRect detect = new MatOfRect();
//        CascadeClassifier cascade = new CascadeClassifier(cascadePath);
//        cascade.detectMultiScale(m, detect);
//        Rect[] rects = detect.toArray();
//        Scalar scalars = new Scalar(0, 255, 0);
//        for (Rect r : rects) {
//            Core.rectangle(image, r.tl(), r.br(), scalars);
//        }
//        Highgui.imwrite("E:\\z_result\\new_result_image_cascade2.jpg", image);
//
//
//        if (true) return;*/
//
//        ArrayList<DetectOfRect> result = new ArrayList<>();
//        long start = System.nanoTime();
//        try {
//            result = ObjectDetect.detect(cascadePath, path);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        Mat imread = Highgui.imread(path);
//        CvUtil.resizeByMaximum(imread, imread, 1000);
////        Core.transpose(imread, imread);
////        Core.flip(imread, imread, 1);
//
//        int i = 0;
//        Mat cloneImage = imread.clone();
//        Mat newImg = imread.clone();
//        for (DetectOfRect r : result) {
//            Scalar scalar = new Scalar(255, 0, 0);
//            if (detectLogo(r.rect, imread, cascadeLogo)) {
//                Core.rectangle(imread, r.rect.tl(), r.rect.br(), scalar);
//            }
//            Core.rectangle(cloneImage, r.rect.tl(), r.rect.br(), scalar);
//          /*  Mat cloneImg = imread.clone();
//            for (Rect rect : r.list) {
//                Scalar scalar = new Scalar(0, 255, 0);
//                Core.rectangle(cloneImg, rect.tl(), rect.br(), scalar);
//                /*Core.putText(imread, "Object " + r.list.size(),
//                        r.rect.tl(), Core.FONT_HERSHEY_PLAIN, 1, scalar);*/
//            /*}
//            Highgui.imwrite("E:\\z_result\\found_img\\IMG_" + (i++) + ".jpg", cloneImg);*/
//
//        }
//        Highgui.imwrite("E:\\z_result\\result.jpg", imread);
//        Highgui.imwrite("E:\\z_result\\result_cloneImage.jpg", cloneImage);

        Mat imread1 = Highgui.imread("E:\\z_result\\asd.jpg");
        Mat canny = new Mat();
        CvUtil.resizeByMaximum(imread1, imread1, 200);
        Imgproc.Canny(imread1, canny, 100, 100);
        for (int i = 0; i < imread1.cols(); i++) {
            for (int j = 0; j < imread1.rows(); j++) {
                double val = canny.get(j, i)[0];
                if (val == 255) {
                    imread1.put(j, i, 0, 0, 0);
                }
            }
        }

        Highgui.imwrite("E:\\z_result\\result_cloneImage_canny.jpg", canny);
        Highgui.imwrite("E:\\z_result\\result_cloneImage_canny2.jpg", imread1);

//        System.out.println("end:" + (System.nanoTime() - start));
    }


    private static boolean detectLogo(Rect rect, Mat mat, CascadeClassifier cascadeLogo) {
        Mat clone = mat.clone();
        Mat obj = new Mat(clone, rect);
        CvUtil.resizeByMaximum(obj, obj, 200);
        MatOfRect detect = new MatOfRect();
        cascadeLogo.detectMultiScale(obj, detect);
        if (!detect.empty()) {
            Core.putText(mat, "PEPSI", rect.tl(), Core.FONT_HERSHEY_COMPLEX, 1, new Scalar(0, 255, 0));
            return true;
        }
        return false;
    }
}
