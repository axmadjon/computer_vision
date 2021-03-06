package uz.greenwhite.vision.java;

import com.sun.istack.internal.NotNull;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Size;
import org.opencv.objdetect.CascadeClassifier;
import uz.greenwhite.vision.common.Wrapper;

import java.util.ArrayList;

public class DetectUtil {

    private DetectUtil() {
    }

    private static boolean pushRightStep(@NotNull Wrapper<Integer> x,
                                         @NotNull Wrapper<Boolean> widthExit,
                                         @NotNull Wrapper<Boolean> stepDown,
                                         int width, int imgWidth,
                                         int rightPadding, int rightStep) {
        if ((x.value + rightPadding) > imgWidth) {
            if ((x.value + width) > imgWidth) {
                widthExit.value = true;
                return false;
            }
            x.value = (x.value - imgWidth);
            stepDown.value = true;
            widthExit.value = true;
        } else {
            x.value += rightStep;
            widthExit.value = false;
        }
        return true;
    }

    private static boolean pushDownStep(@NotNull Wrapper<Boolean> stepDown,
                                        @NotNull Wrapper<Boolean> heightExit,
                                        @NotNull Wrapper<Integer> x,
                                        @NotNull Wrapper<Integer> y,
                                        int height, int imgHeight,
                                        int downPadding, int downStep) {
        if (stepDown.value) {
            if ((y.value + downPadding) > imgHeight) {
                if ((y.value + height) > imgHeight) {
                    heightExit.value = true;
                    return false;
                }
                y.value += (y.value - imgHeight);
                heightExit.value = true;
            } else {
                y.value += downStep;
                x.value = 0;
            }
            stepDown.value = false;
        }
        return false;
    }

    //------------------------------------------------------------------------------------------------------------------

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void detectStepByStep(CascadeClassifier cascade,
                                        ArrayList<RectDetect> detects,
                                        Mat src, int size) {
        for (int i = 0; i < 4; i++) {
            int resize = (i + 1) * 100;

            if (size < resize) return;

            Size s = src.size();
            if (Math.max(s.width, s.height) < size) {
                return;
            }

            final Wrapper<Boolean> stepDown = Wrapper.ofBool(), widthExit = Wrapper.ofBool(), heightExit = Wrapper.ofBool();
            int downStep = (size / 3), rightStep = (size / 3);
            Wrapper<Integer> y = Wrapper.ofInt(), x = Wrapper.ofInt();
            final int rightPadding = (rightStep + size), downPadding = (downStep + size);

            while (!widthExit.value || !heightExit.value) {
                MatOfRect detect = new MatOfRect();

                Mat img = CvUtil.cropImage(src, x.value, y.value, size);

                CvUtil.detectMultiScale(cascade, detect, img, resize);

                if (!detect.empty()) {
                    detects.add(new RectDetect(x.value, y.value, size, resize, detect.toArray()));
                }

                pushRightStep(x, widthExit, stepDown, size, (int) s.width, rightPadding, rightStep);
                pushDownStep(stepDown, heightExit, x, y, size, (int) s.height, downPadding, downStep);
            }
        }
    }
}
