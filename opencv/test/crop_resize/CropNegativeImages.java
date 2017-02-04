package crop_resize;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import java.io.File;

public class CropNegativeImages {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    //------------------------------------------------------------------------------------------------------------------
    private static int sequence = 6091;
    private static String fileName = "";

    public static void main(String[] args) {

        File f = new File("E:\\image\\opencv\\bottle\\bottle_negative\\");

        for (File file : f.listFiles()) {
            Mat imread = Highgui.imread((fileName = file.getAbsolutePath()));
            if (!imread.empty()) {
                Imgproc.GaussianBlur(imread, imread, new Size(5, 5), 0);
                run(imread);
            }else{
                System.out.println(String.format("imread is empty (%s)", fileName));
            }
        }
    }

    private static void run(Mat img) {

        Size size = img.size();
        Mat clone = img.clone();

        if (size.width >= 1000 && size.height >= 1000) {
            bigImage(img, size);

        } else if (size.width >= 1000) {
            widthBigImage(img, size);

        } else if (size.height >= 1000) {
            heightBigImage(img, size);

        } else if (size.height > 500 || size.width > 500) {
            final int with = (int) (size.width / 2), height = (int) (size.height / 2);
            int x = 0, y = 0;

            for (int i = 0; i < 4; i++) {
                if (i == 1) x = with;
                else if (i == 2) {
                    y = height;
                    x = 0;
                } else if (i == 3) x = with;


                Mat result = new Mat(img, new Rect(x, y, with, height));
                writeImage(result);
                transposeWithFlip(result);
            }
        }
        writeImage(clone);
        transposeWithFlip(clone);
    }
    //------------------------------------------------------------------------------------------------------------------

    private static void bigImage(Mat img, Size size) {
        final int with = (int) (size.width / 3), height = (int) (size.height / 3);
        int x = 0, y = 0;
        for (int i = 0; i < 9; i++) {
            switch (i) {
                case 1:
                case 4:
                case 7:
                    x = with;
                    break;
                case 2:
                case 5:
                case 8:
                    x += with;
                    break;
                case 3:
                    x = 0;
                    y = height;
                    break;
                case 6:
                    x = 0;
                    y += height;
                    break;
            }

            Mat result = new Mat(img, new Rect(x, y, with, height));
            writeImage(result);
            transposeWithFlip(result);
        }
    }

    private static void widthBigImage(Mat img, Size size) {
        final int with = (int) (size.width / 3), height = (int) (size.height / 2);
        int x = 0, y = 0;

        for (int i = 0; i < 6; i++) {
            switch (i) {
                case 1:
                case 2:
                    x = (with * i);
                    break;
                case 3:
                    x = 0;
                    y = height;
                    break;
                case 4:
                    x = with;
                    break;
                case 5:
                    x += with;
                    break;
            }

            Mat result = new Mat(img, new Rect(x, y, with, height));
            writeImage(result);
            transposeWithFlip(result);
        }
    }

    private static void heightBigImage(Mat img, Size size) {
        final int with = (int) (size.width / 2), height = (int) (size.height / 3);
        int x = 0, y = 0;

        for (int i = 0; i < 6; i++) {
            switch (i) {
                case 1:
                case 3:
                case 5:
                    x = with;
                    break;
                case 2:
                    x = 0;
                    y = height;
                    break;
                case 4:
                    x = 0;
                    y += height;
                    break;
            }
            Mat result = new Mat(img, new Rect(x, y, with, height));
            writeImage(result);
            transposeWithFlip(result);
        }
    }

    //------------------------------------------------------------------------------------------------------------------

    private static void writeImage(Mat img) {
        if (!img.empty()) {
            Highgui.imwrite("E:\\z_opencv_test2\\negative\\IMG_" + (sequence++) + ".jpg", img);
        } else {
            System.out.println(String.format("img is empty (%s)", fileName));
        }
    }

    private static void transposeWithFlip(Mat img) {
        Core.flip(img, img, 1);
        writeImage(img);
       /* for (int i = 0; i < 2; i++) {
            Core.transpose(img, img);
            writeImage(img);
            Core.flip(img, img, 1);
            writeImage(img);
        }*/
    }
}
