package uz.greenwhite.thread;

public abstract class TaskListener {

    public abstract void complete();

    public void completeTask(Job job) {
    }
}
