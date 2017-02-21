package test;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

public class Test {

    final protected static char[] hexArray = "0123456789abcdef".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static String calcSHA(byte b[]) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(b);
        return bytesToHex(md.digest());
    }

    public static void main(String[] args) throws Exception {
        for (int i = 0; i <= 9999; i++) {
            String password = String.valueOf(i);
            System.out.println(password + " " + calcSHA(password.getBytes("utf-8")));
        }

//        File f = new File("E:\\z_hog_train\\good\\");
//        StringBuilder sb = new StringBuilder();
//        Size size = new Size(30, 100);
//        for (File file : f.listFiles()) {
//            String path = file.getAbsolutePath();
//            Mat imread = Highgui.imread(path);
//            Imgproc.resize(imread, imread, size);
//            Highgui.imwrite(path, imread);
//            sb.append(path).append(" 1 0 0 30 100\n");
//        }
//        System.out.println(sb.toString());
//        if (true) return;

//        File f = new File("E:\\z_hog_train\\img\\");
//        StringBuilder sb = new StringBuilder();
//        for (File file : f.listFiles()) {
//            sb.append(file.getAbsolutePath()).append("\n");
//        }
//        System.out.println(sb.toString());

//        File f = new File("E:\\z_hog_train\\img\\");
//        int id = 0;
//        for (File file : f.listFiles()) {
//            file.renameTo(new File(file.getParentFile(), "IMG_" + (id++) + ".jpg"));
//        }
    }

}
