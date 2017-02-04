package crop_resize;

import java.io.File;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        StringBuffer sb = new StringBuffer();
        Arrays.stream(new File("E:\\z_opencv_test2\\positive\\").listFiles())
                .map(File::getAbsolutePath)
                .forEach((s) -> sb.append(s).append(" 1 0 0 30 100").append("\n"));
        System.out.println(sb.toString());
    }
}
