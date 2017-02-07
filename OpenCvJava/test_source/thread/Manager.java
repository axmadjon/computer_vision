package uz.greenwhite.thread;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

class Manager {

    private final ConcurrentHashMap<Integer, Job> runnables = new ConcurrentHashMap<>();
    private final ArrayList<Job> jobs;
    private final TaskListener listener;

    Manager(ArrayList<Job> jobs, TaskListener listener) {
        this.jobs = jobs;
        this.listener = listener;
    }

    void run() {
        for (Job job : jobs) {
            int taskId = JobManager.sequence.incrementAndGet();
            runnables.put(taskId, job);
            JobManager.execute.execute(new Task(taskId, job, this));
        }
    }

    void completeThread(int taskId) {
        listener.completeTask(runnables.get(taskId));
        runnables.remove(taskId);
        if (runnables.isEmpty()) listener.complete();
    }
}
