package main;

import com.google.gson.Gson;
import main.managers.HTTPTaskManager;
import main.managers.Managers;
import main.servers.KVServer;
import main.tasks.Epic;
import main.tasks.Subtask;
import main.tasks.Task;

import java.io.IOException;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Gson gson = new Gson();
        new KVServer().start();
        HTTPTaskManager httpTaskManager = Managers.getDefault("http://localhost:8078");
        Task task1 = new Task("task1");
        task1.setDescription("test");
        task1.setStartTime("03.10.2022 15:00");
        task1.setDuration(1L);
        httpTaskManager.addTaskSuper(task1);
        int id1 = task1.getId();
        Task task2 = new Task("task2");
        task2.setDescription("test");
        task2.setStartTime("03.10.2022 17:00");
        task2.setDuration(2L);
        httpTaskManager.addTaskSuper(task2);
        int id2 = task2.getId();
        Epic epic1 = new Epic("epic");
        epic1.setDescription("test");
        epic1.setStartTime("03.10.2022 18:00");
        epic1.setDuration(0L);
        httpTaskManager.addEpicTaskSuper(epic1);
        int id3 = epic1.getId();
        Subtask subtask1 = new Subtask("subtask", epic1.getId());
        subtask1.setDescription("test");
        subtask1.setStartTime("03.10.2022 21:00");
        subtask1.setDuration(3L);
        httpTaskManager.addSubtaskSuper(subtask1);
        int id4 = subtask1.getId();
        httpTaskManager.getTaskById(id3);
        httpTaskManager.getTaskById(id1);
        httpTaskManager.getTaskById(id2);
        httpTaskManager.getTaskById(id4);
        System.out.println(httpTaskManager.getAllTasks());
        System.out.println(httpTaskManager.getHistoryManager().getHistory());
        String saveTasks = "";
        for (Map.Entry<Integer, Task> entry : httpTaskManager.getTasks().entrySet()) {
            Task task = entry.getValue();
            String save = gson.toJson(task) + "/";
            saveTasks += save;
        }
        String saveEpics = "";
        for (Map.Entry<Integer, Epic> entry : httpTaskManager.getEpics().entrySet()) {
            Epic epic = entry.getValue();
            String save = gson.toJson(epic) + "/";
            saveEpics += save;
        }
        String saveSubtasks = "";
        for (Map.Entry<Integer, Subtask> entry : httpTaskManager.getSubtasks().entrySet()) {
            Subtask subtask = entry.getValue();
            String save = gson.toJson(subtask) + "/";
            saveSubtasks += save;
        }
        String saveHistory = gson.toJson(httpTaskManager.getHistoryManager().getHistory().toString());
        String toSave = saveTasks + "%%" + saveEpics + "%%" + saveSubtasks + "%%" + saveHistory;
        httpTaskManager.save(toSave);
        httpTaskManager.removeAllTasks();
        System.out.println(httpTaskManager.getAllTasks());
        httpTaskManager = httpTaskManager.load();
        System.out.println(httpTaskManager.getAllTasks());
    }
}