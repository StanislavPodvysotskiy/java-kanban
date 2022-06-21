package managers;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

public interface TaskManager {

    void addTask(Task task);

    void addEpicTask(Epic task);

    void addSubtask(Subtask task);

    String getTaskById(int id);

    String getEpicById(int id);

    String getSubtaskById(int id);

    void showAllTasks();

    String getStatusById(int id);

    String setStatusById(int id, String status);

    void removeTaskById(int id);

    void removeEpicById(int id);

    void removeSubtaskById(int id);

    void removeAllTasks();

    void getEpicSubtasks(int id);

    Status getEpicStatus(int id);

}