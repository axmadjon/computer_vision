import org.opencv.calib3d.Calib3d;
import org.opencv.core.*;
import org.opencv.features2d.*;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by pc on 09.02.2017.
 */


public class TestMain {

    static {

        try {
            System.setProperty("java.library.path", "E:\\install\\computer_vision\\opencv\\build\\java\\x64\\");

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
        Mat mat = Highgui.imread("E:\\z_result\\found_object.jpg", Highgui.IMREAD_GRAYSCALE);
        Core.flip(mat, mat, 1);

        Mat logo = Highgui.imread("E:\\z_result\\pepsi_logo2.jpg", Highgui.IMREAD_GRAYSCALE);
        if (logo.empty()) System.out.println("logo is empty");
        Size size = logo.size();
        Imgproc.resize(logo, logo, new Size(size.width / 5, size.height / 5));

        FeatureDetector detector = FeatureDetector.create(FeatureDetector.SIFT);
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

        Mat matchoutput = new Mat(logo.rows() * 2, logo.cols() * 2, Highgui.CV_LOAD_IMAGE_COLOR);
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

            Core.line(mat, new Point(scene_corners.get(0, 0)), new Point(scene_corners.get(1, 0)), new Scalar(0, 255, 0), 4);
            Core.line(mat, new Point(scene_corners.get(1, 0)), new Point(scene_corners.get(2, 0)), new Scalar(0, 255, 0), 4);
            Core.line(mat, new Point(scene_corners.get(2, 0)), new Point(scene_corners.get(3, 0)), new Scalar(0, 255, 0), 4);
            Core.line(mat, new Point(scene_corners.get(3, 0)), new Point(scene_corners.get(0, 0)), new Scalar(0, 255, 0), 4);
        } else {
            System.out.println("not found " + goodMatchesList.size());
        }
        Imshow.show(mat);

    }
}
