package uz.greenwhite.vision;

import com.sun.istack.internal.NotNull;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.objdetect.CascadeClassifier;

import java.io.FileNotFoundException;
import java.util.*;

public class ObjectDetect {

    private static void checkParameter(String cascadePath, String imgPath) throws NullPointerException {
        if (cascadePath == null || cascadePath.length() == 0)
            throw new NullPointerException("cascade path null or empty");

        if (imgPath == null || imgPath.length() == 0) throw new NullPointerException("image path null or empty");
    }

    public static ArrayList<Rect> detect(@NotNull String cascade, @NotNull String img)
            throws NullPointerException, FileNotFoundException {
        checkParameter(cascade, img);

        Mat src = Imgcodecs.imread(img, Imgcodecs.CV_LOAD_IMAGE_COLOR);
        if (src.empty()) throw new FileNotFoundException(String.format("image not found [%s]", img));

        CascadeClassifier classifier = new CascadeClassifier(cascade);
        if (classifier.empty()) throw new FileNotFoundException(String.format("cascade not found [%s]", cascade));

        ArrayList<RectDetect> detects = new ArrayList<>();

        Size size = src.size();
        long count = Math.round(Math.min(size.width, size.height) / 200);

        long start = System.nanoTime();

        for (int i = 2; i < count; i++) {
            Detect.detectStepByStep(classifier, detects, src, (i + 1) * 200);
        }
        System.out.println("end" + (System.nanoTime() - start));
        return make(detects);
    }

    //TODO rename method name
    private static ArrayList<Rect> make(ArrayList<RectDetect> src) {
        Set<Rect> rects = new HashSet<>();
        for (RectDetect detect : src) {
            int i = detect.crop / detect.resize;
            ArrayList<Rect> r1 = new ArrayList<>();
            for (Rect rect : detect.rects) {
                int x = (rect.x * i) + detect.x;
                int y = (rect.y * i) + detect.y;
                int w = rect.width * i;
                int h = rect.height * i;
                if (checkResult(r1, x, y, w, h)) {
                    Rect r = new Rect(x, y, w, h);
                    rects.add(r);
                    r1.add(r);
                }
            }
        }
        Map<Integer, ArrayList<Rect>> map = new HashMap<>();
        for (Rect r : rects) {
            for (Rect r2 : rects) {
                if (!r.equals(r2)) {
                    if (Util.checkRectRadius(r, r2)) {

                    }
                }
            }
        }

        ArrayList<Rect> resultRects = new ArrayList<>();
        resultRects.addAll(rects);

        System.out.println("rectSize:" + rects.size());
        System.out.println("dstSize:" + resultRects.size());

        return resultRects;
    }

    //TODO  rename method name
    private static boolean checkResult(ArrayList<Rect> r, int x, int y, int w, int h) {
        Rect r1 = new Rect(x, y, w, h);
        for (Rect r2 : r) {
            if (r2.x < x && r2.width > w &&
                    r2.y > y && r2.height > h ||
                    Util.checkRectRadius(r2, r1)) {
                return false;
            }
        }
        return true;
    }
}
