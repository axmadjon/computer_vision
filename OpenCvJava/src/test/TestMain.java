package test;

import org.opencv.calib3d.Calib3d;
import org.opencv.core.*;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;
import uz.greenwhite.vision.Util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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

    private static boolean checkRadius(List<Rect> rects, Rect rect, Mat orgImage) {
        for (Rect lastRects : rects) {
            if (!Util.checkRectRadius(lastRects, rect)) {
                rects.clear();
                return false;
            }
        }
        if (rects.size() <= 10) {
            detect(new Mat(orgImage, rect));
            return true;
        }
        rects.add(rect);
        return false;

    }

    public static void main(String[] args) {
        //detect(null);
        String lbp = "e:\\install\\computer_vision\\opencv_2\\opencv\\build\\etc\\lbpcascades\\lbpcascade_frontalcatface.xml";
        String haar = "e:\\install\\computer_vision\\opencv_2\\opencv\\build\\etc\\haarcascades\\haarcascade_frontalcatface.xml";
        String myCascade = "e:\\gws_project\\opncv_train_cascade\\lbp_cascade\\cascade.xml";
        CascadeClassifier cascade = new CascadeClassifier(myCascade);

        CascadeClassifier cascadeLogo = new CascadeClassifier("e:\\logo_test\\haarcascade\\cascade.xml");
        Imshow img = new Imshow("img");
        //Imshow grayImg = new Imshow("grayIg");

        VideoCapture video = new VideoCapture();

        MatOfRect detect = new MatOfRect();
        if (video.open(0)) {
            int videoSize = 600;
            int padding = 2;
            video.set(Videoio.CAP_PROP_FRAME_WIDTH, videoSize);
            video.set(Videoio.CAP_PROP_FRAME_HEIGHT, videoSize);
            int i = 0;
            Mat vidImg = new Mat();
            Mat gray = new Mat();
            ArrayList<Rect> lastRects = new ArrayList<>();
            while (i < 2000) {
                if (video.read(vidImg)) {
                    // Core.flip(vidImg, vidImg, 1);

                    Imgproc.resize(vidImg, gray, new Size(vidImg.width() / padding, vidImg.height() / padding));
                    Imgproc.cvtColor(gray, gray, Imgproc.COLOR_RGB2GRAY);
                    cascade.detectMultiScale(gray, detect);

                    Rect[] foundRects = detect.toArray();
                    if (foundRects.length > 0) {
                        for (Rect r : foundRects) {
                            int x = r.x * padding, y = r.y * padding;
                            int w = r.width * padding, h = r.height * padding;

                            Rect rect = new Rect(x, y, w, h);
                            if (detectLogo(rect, vidImg, cascadeLogo)) {
                                Imgproc.rectangle(vidImg, rect.tl(), rect.br(), new Scalar(0, 0, 255));
                            }
                            //Imgproc.rectangle(gray, r.tl(), r.br(), new Scalar(0, 255, 0));

                           /* if (checkRadius(lastRects, rect, vidImg)) {
                                video.release();
                                return;
                            }*/
                        }
                    }
                    img.showImage(vidImg);
                    //grayImg.showImage(gray);
                }
                i++;
                System.out.println("i is " + i);
            }
            video.release();
            System.out.println("success open video");
        } else {
            System.out.println("can't open video");
        }

       /* if (img.Window.isActive()) {
            img.close();
        }*/
    }

    private static boolean detectLogo(Rect rect, Mat mat, CascadeClassifier cascadeLogo) {
        Mat clone = mat.clone();
        Mat obj = new Mat(clone, rect);
        MatOfRect detect = new MatOfRect();
        cascadeLogo.detectMultiScale(obj, detect);
        if (!detect.empty()) {
            Imgproc.putText(mat, "PEPSI", rect.tl(), Imgproc.CC_STAT_AREA, 1, new Scalar(0, 255, 0));
            return true;
        }
        return false;
    }

    private static void detect(Mat mat) {
        mat = Imgcodecs.imread("E:\\z_result\\found_object.jpg", Imgcodecs.IMREAD_GRAYSCALE);
        Core.flip(mat, mat, 1);

        Mat logo = Imgcodecs.imread("E:\\z_result\\pepsi_logo.jpg", Imgcodecs.IMREAD_GRAYSCALE);
        if (logo.empty()) System.out.println("logo is empty");
        Size size = logo.size();
        Imgproc.resize(logo, logo, new Size(size.width / 5, size.height / 5));

        FeatureDetector detector = FeatureDetector.create(FeatureDetector.FAST);
        DescriptorExtractor extractor = DescriptorExtractor.create(DescriptorExtractor.SIFT);

        MatOfKeyPoint objectKeyPoints = new MatOfKeyPoint();
        MatOfKeyPoint objectDescriptors = new MatOfKeyPoint();
        detector.detect(logo, objectKeyPoints);
        extractor.compute(logo, objectKeyPoints, objectDescriptors);

        MatOfKeyPoint sceneKeyPoints = new MatOfKeyPoint();
        MatOfKeyPoint sceneDescriptors = new MatOfKeyPoint();
        detector.detect(mat, sceneKeyPoints);
        extractor.compute(mat, sceneKeyPoints, sceneDescriptors);

        List<MatOfDMatch> matches = new LinkedList<>();
        DescriptorMatcher descriptorMatcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE);
        descriptorMatcher.knnMatch(objectDescriptors, sceneDescriptors, matches, 2);

        LinkedList<DMatch> goodMatchesList = new LinkedList<>();

        Scalar newKeypointColor = new Scalar(255, 0, 0);
        Features2d.drawKeypoints(mat, sceneKeyPoints, mat, newKeypointColor, 0);

        Mat matchoutput = new Mat(logo.rows() * 2, logo.cols() * 2, Imgcodecs.CV_LOAD_IMAGE_COLOR);
        Features2d.drawKeypoints(logo, sceneKeyPoints, matchoutput, newKeypointColor, 0);
        Imshow.show(matchoutput);

        float nndrRatio = 0.7f;

        for (MatOfDMatch matofDMatch : matches) {
            DMatch[] dmatcharray = matofDMatch.toArray();
            DMatch m1 = dmatcharray[0];
            DMatch m2 = dmatcharray[1];

            if (m1.distance <= m2.distance * nndrRatio) {
                goodMatchesList.addLast(m1);
            }
        }

        if (goodMatchesList.size() >= 10) {
            System.out.println("found");
            List<KeyPoint> objKeypointlist = objectKeyPoints.toList();
            List<KeyPoint> scnKeypointlist = sceneKeyPoints.toList();

            LinkedList<Point> objectPoints = new LinkedList<>();
            LinkedList<Point> scenePoints = new LinkedList<>();

            for (DMatch aGoodMatchesList : goodMatchesList) {
                objectPoints.addLast(objKeypointlist.get(aGoodMatchesList.queryIdx).pt);
                scenePoints.addLast(scnKeypointlist.get(aGoodMatchesList.trainIdx).pt);
            }

            MatOfPoint2f objMatOfPoint2f = new MatOfPoint2f();
            objMatOfPoint2f.fromList(objectPoints);
            MatOfPoint2f scnMatOfPoint2f = new MatOfPoint2f();
            scnMatOfPoint2f.fromList(scenePoints);

            Mat homography = Calib3d.findHomography(objMatOfPoint2f, scnMatOfPoint2f, Calib3d.RANSAC, 3);

            Mat obj_corners = new Mat(4, 1, CvType.CV_32FC2);
            Mat scene_corners = new Mat(4, 1, CvType.CV_32FC2);

            obj_corners.put(0, 0, 0, 0);
            obj_corners.put(1, 0, logo.cols(), 0);
            obj_corners.put(2, 0, logo.cols(), logo.rows());
            obj_corners.put(3, 0, 0, logo.rows());

            Core.perspectiveTransform(obj_corners, scene_corners, homography);

            Imgproc.line(mat, new Point(scene_corners.get(0, 0)), new Point(scene_corners.get(1, 0)), new Scalar(0, 255, 0), 4);
            Imgproc.line(mat, new Point(scene_corners.get(1, 0)), new Point(scene_corners.get(2, 0)), new Scalar(0, 255, 0), 4);
            Imgproc.line(mat, new Point(scene_corners.get(2, 0)), new Point(scene_corners.get(3, 0)), new Scalar(0, 255, 0), 4);
            Imgproc.line(mat, new Point(scene_corners.get(3, 0)), new Point(scene_corners.get(0, 0)), new Scalar(0, 255, 0), 4);
        } else {
            System.out.println("not found " + goodMatchesList.size());
        }
        Imshow.show(mat);
    }
}
