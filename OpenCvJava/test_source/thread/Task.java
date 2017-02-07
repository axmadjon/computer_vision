package uz.greenwhite.thread;

class Task implements Runnable {

    private final int taskId;
    private final Job job;
    private final Manager manager;

    Task(int taskId, Job job, Manager manager) {
        this.taskId = taskId;
        this.job = job;
        this.manager = manager;
    }

    @Override
    public void run() {
        try {
            this.job.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            manager.completeThread(taskId);
        }

    }
}
