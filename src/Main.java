import managers.Managers;
import managers.TaskManager;
import tasks.Epic;
import tasks.Subtask;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();

        Epic epic1 = new Epic("E1");
        epic1.setDescription("эпик 1");
        taskManager.addEpicTask(epic1);

        Epic epic2 = new Epic("E2");
        epic2.setDescription("эпик 2");
        taskManager.addEpicTask(epic2);

        Subtask subtask1 = new Subtask("S1", epic1.getId());
        subtask1.setDescription("сабтаск 1");
        taskManager.addSubtask(subtask1);

        Subtask subtask2 = new Subtask("S2", epic1.getId());
        subtask2.setDescription("сабтаск 2");
        taskManager.addSubtask(subtask2);

        Subtask subtask3 = new Subtask("S3", epic1.getId());
        subtask3.setDescription("сабтаск 3");
        taskManager.addSubtask(subtask3);

        taskManager.getEpicById(epic1.getId());
        taskManager.getEpicById(epic2.getId());
        taskManager.getSubtaskById(subtask1.getId());
        taskManager.getSubtaskById(subtask2.getId());
        taskManager.getSubtaskById(subtask3.getId());

        System.out.println(taskManager.getHistoryManager().getHistory());

        taskManager.getEpicById(epic2.getId());
        taskManager.getSubtaskById(subtask2.getId());
        taskManager.getSubtaskById(subtask1.getId());
        taskManager.getEpicById(epic1.getId());

        System.out.println(taskManager.getHistoryManager().getHistory());

        taskManager.removeSubtaskById(subtask2.getId());

        System.out.println(taskManager.getHistoryManager().getHistory());

        taskManager.removeEpicById(epic1.getId());

        System.out.println(taskManager.getHistoryManager().getHistory());

    }

}