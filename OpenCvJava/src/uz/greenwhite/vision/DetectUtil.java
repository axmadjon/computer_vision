package uz.greenwhite.vision;

import com.sun.istack.internal.NotNull;
import uz.greenwhite.vision.common.Wrapper;

public class DetectUtil {

    private DetectUtil() {
    }

    static boolean pushRightStep(@NotNull Wrapper<Integer> x,
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

    static boolean pushDownStep(@NotNull Wrapper<Boolean> stepDown,
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
}
