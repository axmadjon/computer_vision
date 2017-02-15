package uz.greenwhite.vision;

import com.sun.istack.internal.NotNull;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.objdetect.CascadeClassifier;

import java.io.FileNotFoundException;
import java.util.*;

public class ObjectDetect {

    private static void checkParameter(String cascadePath, String imgPath) throws NullPointerException {
        if (cascadePath == null || cascadePath.length() == 0)
            throw new NullPointerException("cascade path null or empty");

        if (imgPath == null || imgPath.length() == 0) throw new NullPointerException("image path null or empty");
    }

    public static ArrayList<DetectOfRect> detect(@NotNull String cascade, @NotNull String img)
            throws NullPointerException, FileNotFoundException {
        checkParameter(cascade, img);

        Mat src = Highgui.imread(img, Highgui.CV_LOAD_IMAGE_COLOR);
        Core.transpose(src,src);
        Core.flip(src,src,1);
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
    private static ArrayList<DetectOfRect> make(ArrayList<RectDetect> src) {
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

        Map<Rect, ArrayList<Rect>> map = new HashMap<>();
        for (Rect found : rects) {
            if (found.area() == 0) continue;
            boolean contains = false;

            for (Map.Entry<Rect, ArrayList<Rect>> r2 : map.entrySet()) {
                ArrayList<Rect> value = r2.getValue();
                int size = value.size();
                for (int i = 0; i < size; i++) {
                    if (Util.checkRectRadius(found, value.get(i))) {
                        contains = true;
                        value.add(found);
                        r2.getKey().set(calculateCenter(value));
                    }
                }
            }
            if (!contains) {
                Rect clone = found.clone();
                ArrayList<Rect> result = new ArrayList<>();
                result.add(clone);
                map.put(clone, result);
            }
        }

        ArrayList<DetectOfRect> result = new ArrayList<>();
        for (Map.Entry<Rect, ArrayList<Rect>> entry : map.entrySet()) {
            result.add(new DetectOfRect(entry.getKey(), entry.getValue()));
        }

        return result;
    }

    private static double[] calculateCenter(ArrayList<Rect> rects) {
        int minX = -1, maxX = -1, minY = -1, maxY = -1;
        int minW = -1, maxW = -1, minH = -1, maxH = -1;
        for (Rect r : rects) {
            minX = minX < 0 ? r.x : Math.min(minX, r.x);
            maxX = maxX < 0 ? r.x : Math.max(maxX, r.x);

            minY = minY < 0 ? r.y : Math.min(minY, r.y);
            maxY = maxY < 0 ? r.y : Math.max(maxY, r.y);

            minW = minW < 0 ? r.width : Math.min(minW, r.width);
            maxW = maxW < 0 ? r.width : Math.max(maxW, r.width);

            minH = minH < 0 ? r.height : Math.min(minH, r.height);
            maxH = maxH < 0 ? r.height : Math.max(maxH, r.height);

        }
        return new double[]{
                minX + ((maxX - minX) / 2),
                minY + ((maxY - minY) / 2),
                minW + ((maxW - minW) / 2),
                minH + ((maxH - minH) / 2)};
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
