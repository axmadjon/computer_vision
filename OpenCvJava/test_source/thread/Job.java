package uz.greenwhite.thread;


@FunctionalInterface
public interface Job {
    void execute() throws Exception;
}
