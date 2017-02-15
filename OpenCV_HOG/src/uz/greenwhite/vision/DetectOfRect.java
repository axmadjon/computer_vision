package uz.greenwhite.vision;

import org.opencv.core.Rect;

import java.util.ArrayList;

public class DetectOfRect {

    public final Rect rect;
    public final ArrayList<Rect> list;

    public DetectOfRect(Rect rect, ArrayList<Rect> list) {
        this.rect = rect;
        this.list = list;
    }
}
