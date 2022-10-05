package main;

import main.managers.Managers;
import main.managers.TaskManager;
import main.tasks.Task;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();

        Task task1 = new Task("task1");
        task1.setDescription("test");
        task1.setStartTime("03.10.2022 15:00");
        taskManager.addTask(task1);

        Task task2 = new Task("task2");
        task2.setStartTime("03.10.2022 15:00");
        task2.setDescription("test");
        taskManager.addTask(task2);

        Task task3 = new Task("task3");
        task3.setDescription("test");
        task3.setStartTime("03.10.2022 19:00");
        taskManager.addTask(task3);

        System.out.println(taskManager.showAllTasks());
    }

}