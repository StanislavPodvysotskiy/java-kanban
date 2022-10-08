package main.interfaces;
import main.tasks.Epic;
import main.enums.Status;
import main.tasks.Subtask;
import main.tasks.Task;

import java.util.TreeSet;

public interface TaskManager {

    void addTask(Task task);

    void addEpicTask(Epic task);

    void addSubtask(Subtask task);

    Task getTaskById(int id);

    Epic getEpicById(int id);

    Subtask getSubtaskById(int id);

    String showAllTasks();

    String getStatusById(int id);

    String setStatusById(int id, String status);

    void removeTaskById(int id);

    void removeEpicById(int id);

    void removeSubtaskById(int id);

    void removeAllTasks();

    String getEpicSubtasks(int id);

    Status getEpicStatus(int id);

    HistoryManager getHistoryManager();

    TreeSet<Task> getPrioritizedTasks();

    TreeSet<Epic> getPrioritizedEpics();

}