package uz.greenwhite.vision.test;

import org.opencv.core.Core;
import org.opencv.core.Rect;
import uz.greenwhite.vision.java.ObjectDetect;

import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class TestMain {
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

    public static void main(String[] args) {

    }
}
