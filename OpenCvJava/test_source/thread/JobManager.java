package uz.greenwhite.thread;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;


@SuppressWarnings("unused")
public class JobManager {

    static final ExecutorService execute = Executors.newCachedThreadPool();
    static final AtomicInteger sequence = new AtomicInteger();
    static final ConcurrentHashMap<Integer, Job> jobs = new ConcurrentHashMap<>();

    public static void execute(ArrayList<Job> jobs, TaskListener listener) {
        Manager manager = new Manager(jobs, listener);
        manager.run();
    }
}
