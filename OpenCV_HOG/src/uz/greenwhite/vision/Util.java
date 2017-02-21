package uz.greenwhite.vision;

import org.opencv.core.Rect;

public class Util {

    public static int rectRadius(Rect rect) {
        int min = Math.min(rect.width, rect.height);
        int max = Math.max(rect.width, rect.height);
        return Math.round((((max / 8) - (min / 8)) / 2));// + min
    }

    private static boolean checkPointRadius(int p1, int r1r, int p2, int r2r) {
        int p1Min = p1 - r1r, p1Max = p1 + r1r;
        int p2Min = p2 - r2r, p2Max = p2 + r2r;
        return (p1Min < p2 || p1Min < p2Max) && (p1Max > p2 || p1Max > p2Min);
    }

    public static boolean checkRectRadius(Rect r1, Rect r2) {
        int r1r = rectRadius(r1);
        int r2r = rectRadius(r2);
        return checkRectRadius(r1, r1r, r2, r2r);
    }

    public static boolean checkRectRadius(Rect r1, int r1r, Rect r2, int r2r) {
        //      rect.x with round check
        return checkPointRadius(r1.x, r1r, r2.x, r2r) &&
                //rect.y with round check
                checkPointRadius(r1.y, r1r, r2.y, r2r) &&
                //rect.width with round check
                checkPointRadius(r1.x + r1.width, r1r, r2.x + r2.width, r2r) &&
                //rect.height with round check
                checkPointRadius(r1.y + r1.height, r1r, r2.y + r2.height, r2r);
    }
}
