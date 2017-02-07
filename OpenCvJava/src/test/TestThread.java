package test;

public class TestThread {

    private static int cp(int i, int percent) {
        return Math.round((i * percent) / 100);
    }

    public static void main(String[] args) {
        int crop = 600, resize = 200;
        int x = 70, y = 17, w = 72, h = 156;
        int percent = 100 - (100 / (crop / resize));
        int nx = cp(x, percent), ny = cp(y, percent), nw = cp(w, percent), nh = cp(h, percent);
        System.out.println("nx:" + nx + ", ny:" + ny + ", nw:" + nw + ", nh:" + nh);
    }
}
