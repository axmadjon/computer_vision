package uz.greenwhite.vision.java;

import com.sun.istack.internal.NotNull;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.objdetect.CascadeClassifier;
import uz.greenwhite.vision.common.Wrapper;

import java.io.File;

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
    public static void detectStepByStep(@NotNull CascadeClassifier cascade,
                                        @NotNull Mat src,
                                        @NotNull String dstPath,
                                        int width, int height) {
        if (src.empty()) return;

        for (int i = 0; i < 2; i++) {
            int cSize = (i + 1) * 200;

            if (width < cSize || height < cSize) return;

            if (dstPath == null || dstPath.trim().length() <= 0) throw new RuntimeException("path dst images is empty");

            Size size = src.size();
            if (size.width < width || size.height < height) {
                return;
            }
            if (!dstPath.endsWith("\\")) dstPath = dstPath + "\\";

            File file = new File(dstPath = (String.format("%sIMG_%d_%dx%d\\", dstPath, cSize, width, height)));
            if (!dstPath.endsWith("\\")) dstPath = dstPath + "\\";

            final Wrapper<Boolean> stepDown = Wrapper.ofBool(), widthExit = Wrapper.ofBool(), heightExit = Wrapper.ofBool();
            int downStep = (width / 3), rightStep = (height / 3);
            Wrapper<Integer> y = Wrapper.ofInt(), x = Wrapper.ofInt();
            final int rightPadding = (rightStep + width), downPadding = (downStep + height);

            while (!widthExit.value || !heightExit.value) {
                MatOfRect detect = new MatOfRect();

                Mat img = CvUtil.cropImage(src, x.value, y.value, width, height);

                CvUtil.detectMultiScale(cascade, detect, img, cSize, cSize);
                if (!detect.empty()) {
                    if (file != null) {
                        file.mkdirs();
                        file = null;
                    }
                    CvUtil.rectangleDetect(img, detect);
                    String filename = dstPath + "IMG_" + x.value + "_" + y.value + ".jpg";
                    Imgcodecs.imwrite(filename, img);
                }
                pushRightStep(x, widthExit, stepDown, width, (int) size.width, rightPadding, rightStep);
                pushDownStep(stepDown, heightExit, x, y, height, (int) size.height, downPadding, downStep);
            }
        }
    }
}
