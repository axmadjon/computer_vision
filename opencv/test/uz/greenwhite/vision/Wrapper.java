package uz.greenwhite.vision;

public class Wrapper<V> {

    public static Wrapper<Boolean> ofBool() {
        Wrapper<Boolean> w = new Wrapper<>();
        w.val = false;
        return w;
    }


    public static Wrapper<Integer> ofInt() {
        Wrapper<Integer> w = new Wrapper<>();
        w.val = 0;
        return w;
    }

    public static <V> Wrapper<V> of(V val) {
        Wrapper<V> w = new Wrapper<>();
        w.val = val;
        return w;
    }

    public V val;
}
