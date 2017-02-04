package uz.greenwhite.vision;

import com.sun.istack.internal.NotNull;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;

public class DetectUtil {

    private DetectUtil() {
    }

    private static boolean pushRightStep(@NotNull Wrapper<Integer> x,
                                         @NotNull Wrapper<Boolean> widthExit,
                                         @NotNull Wrapper<Boolean> stepDown,
                                         int width, int imgWidth,
                                         int rightPadding, int rightStep) {
        if ((x.val + rightPadding) > imgWidth) {
            if ((x.val + width) > imgWidth) {
                widthExit.val = true;
                return false;
            }
            x.val = (x.val - imgWidth);
            stepDown.val = true;
            widthExit.val = true;
        } else {
            x.val += rightStep;
            widthExit.val = false;
        }
        return true;
    }

    private static boolean pushDownStep(@NotNull Wrapper<Boolean> stepDown,
                                        @NotNull Wrapper<Boolean> heightExit,
                                        @NotNull Wrapper<Integer> x,
                                        @NotNull Wrapper<Integer> y,
                                        int height, int imgHeight,
                                        int downPadding, int downStep) {
        if (stepDown.val) {
            if ((y.val + downPadding) > imgHeight) {
                if ((y.val + height) > imgHeight) {
                    heightExit.val = true;
                    return false;
                }
                y.val += (y.val - imgHeight);
                heightExit.val = true;
            } else {
                y.val += downStep;
                x.val = 0;
            }
            stepDown.val = false;
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

            while (!widthExit.val || !heightExit.val) {
                MatOfRect detect = new MatOfRect();

                Mat img = CvUtil.cropImage(src, x.val, y.val, width, height);

                CvUtil.detectMultiScale(cascade, detect, img, cSize, cSize);
                if (!detect.empty()) {
                    if (file != null) {
                        file.mkdirs();
                        file = null;
                    }
                    CvUtil.rectangleDetect(img, detect);
                    String filename = dstPath + "IMG_" + x.val + "_" + y.val + ".jpg";
                    Highgui.imwrite(filename, img);
                }
                pushRightStep(x, widthExit, stepDown, width, (int) size.width, rightPadding, rightStep);
                pushDownStep(stepDown, heightExit, x, y, height, (int) size.height, downPadding, downStep);
            }
        }
    }
}
