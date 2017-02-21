package uz.greenwhite.vision;

import org.opencv.core.*;
import org.opencv.highgui.Highgui;
import org.opencv.objdetect.CascadeClassifier;
import uz.greenwhite.vision.common.Wrapper;

import java.util.ArrayList;

class Detect {

    @SuppressWarnings("ResultOfMethodCallIgnored")
    static void detectStepByStep(CascadeClassifier cascade,
                                 ArrayList<RectDetect> detects,
                                 Mat src, int size) {
        Detect detect = new Detect(cascade, detects, src, size, 200);
        detect.execute();
    }

    //------------------------------------------------------------------------------------------------------------------

    private final CascadeClassifier cascade;
    private final ArrayList<RectDetect> detects;
    private final Mat src;
    private final int size, resize;

    private Detect(CascadeClassifier cascade, ArrayList<RectDetect> detects, Mat src, int size, int resize) {
        this.cascade = cascade;
        this.detects = detects;
        this.src = src;
        this.size = size;
        this.resize = resize;
    }

    public static int id = 0;

    private void execute() {
        Size s = src.size();

        final Wrapper<Boolean> stepDown = Wrapper.ofBool(), widthExit = Wrapper.ofBool(), heightExit = Wrapper.ofBool();
        int downStep = (this.size / 2), rightStep = (this.size / 2);
        Wrapper<Integer> y = Wrapper.ofInt(), x = Wrapper.ofInt();
        final int rightPadding = (rightStep + this.size), downPadding = (downStep + this.size);

        while (!widthExit.value || !heightExit.value) {
            MatOfRect detect = new MatOfRect();

            Mat img = CvUtil.cropImage(src, x.value, y.value, this.size);

            CvUtil.detectMultiScale(cascade, detect, img, resize);

            if (!detect.empty()) {
                Rect[] rects = detect.toArray();
                detects.add(new RectDetect(x.value, y.value, this.size, resize, rects));
                Scalar scalar = new Scalar(0, 255, 0);
                for (Rect r : rects) {
                    Core.rectangle(img, r.tl(), r.br(), scalar);
                }
            }
            Highgui.imwrite("E:\\z_result\\test_result\\img_" + (id++) + "_"
                            + img.hashCode()
                            + "_"
                            + x.value + "_" + y.value
                            + "_" + resize + "_" + this.size + "_" + detect.hashCode() + "_" + (id++) + ".jpg",
                    img);

            DetectUtil.pushRightStep(x, widthExit, stepDown, this.size, (int) s.width, rightPadding, rightStep);
            DetectUtil.pushDownStep(stepDown, heightExit, x, y, this.size, (int) s.height, downPadding, downStep);
        }
    }
}
