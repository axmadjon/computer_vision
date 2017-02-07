package uz.greenwhite.vision;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import uz.greenwhite.vision.common.Wrapper;

import java.util.ArrayList;

public class Detect {

    @SuppressWarnings("ResultOfMethodCallIgnored")
    static void detectStepByStep(CascadeClassifier cascade,
                                 ArrayList<RectDetect> detects,
                                 Mat src, int size) {
        ArrayList<Detect> result = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            int resize = (i + 1) * 200;

            if (size < resize) return;

            Size s = src.size();
            if (Math.max(s.width, s.height) < size) {
                return;
            }

            result.add(new Detect(cascade, detects, src, size, resize));
        }

        result.parallelStream().forEach(Detect::execute);
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
                /*TODO Scalar scalar = new Scalar(0, 255, 0);
                for (Rect r : rects) {
                    Imgproc.rectangle(img, r.tl(), r.br(), scalar);
                }
                Imgcodecs.imwrite("E:\\z_result\\test_result\\img_"
                                + img.hashCode()
                                + "_"
                                + x.value + "_" + y.value
                                + "_" + resize + "_" + this.size + "_" + rects.length + ".jpg",
                        img);*/
            }

            DetectUtil.pushRightStep(x, widthExit, stepDown, this.size, (int) s.width, rightPadding, rightStep);
            DetectUtil.pushDownStep(stepDown, heightExit, x, y, this.size, (int) s.height, downPadding, downStep);
        }
    }
}
