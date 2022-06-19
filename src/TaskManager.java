public interface TaskManager {

    void addTask(Task task);

    void addEpicTask(Epic task);

    void addSubTask(Subtask task);

    String getById(int id);

    void showAllTasks();

    String getStatusById(int id);

    String setStatusById(int id, String status);

    void removeTaskById(int id);

    void removeAllTasks();

    void getEpicSubtasks(int id);

    Status getEpicStatus(int id);

}