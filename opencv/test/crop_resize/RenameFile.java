package crop_resize;

import java.io.File;

public class RenameFile {
    public static void main(String[] args) {
        File f = new File("E:\\z_opencv_test2\\negative\\");
        int id = 0;
        for (File file : f.listFiles()) {
            file.renameTo(new File(file.getParentFile(), "IMG_" + (id++) + ".jpg"));
        }
    }
}
