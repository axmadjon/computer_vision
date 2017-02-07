package uz.greenwhite.vision.java;

import org.opencv.core.Rect;

public class RectDetect {

    public final int x, y, crop, resize;
    public final Rect[] rects;

    public RectDetect(int x, int y, int crop, int resize, Rect[] rects) {
        this.x = x;
        this.y = y;
        this.crop = crop;
        this.resize = resize;
        this.rects = rects;
    }
}
