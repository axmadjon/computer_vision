package uz.greenwhite.vision.common;

public class Wrapper<V> {

    public static Wrapper<Boolean> ofBool() {
        Wrapper<Boolean> w = new Wrapper<>();
        w.value = false;
        return w;
    }


    public static Wrapper<Integer> ofInt() {
        Wrapper<Integer> w = new Wrapper<>();
        w.value = 0;
        return w;
    }

    public static <V> Wrapper<V> of(V val) {
        Wrapper<V> w = new Wrapper<>();
        w.value = val;
        return w;
    }

    public V value;
}
