package uz.greenwhite.vision.java;

import org.w3c.dom.css.Rect;

import java.util.ArrayList;

public interface OnDetectListener {
    void detect(ArrayList<Rect> detect);
}
