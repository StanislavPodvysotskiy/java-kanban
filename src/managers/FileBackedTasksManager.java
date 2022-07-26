package managers;

import tasks.*;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class FileBackedTasksManager extends InMemoryTaskManager {

    private final String path;
    private String stringToWrite;
    private String fileHead;
    private boolean isFirst = true;

    public FileBackedTasksManager(String path) {
        this.path = path;
    }

    @Override
    public HistoryManager getHistoryManager() {
        return super.historyManager;
    }

    public String getPath() {
        return path;
    }

    public void save() throws ManagerSaveException {
        if (isFirst) {
            try (FileWriter fileWriter = new FileWriter(path)) {
                fileWriter.write(fileHead);
                isFirst = false;
            } catch (IOException e) {
                System.out.println(e.getMessage());
            } catch (ManagerSaveException e) {
                System.out.println("Произошла ошибка во время чтения файла.");
            }
        }
        try (FileWriter fileWriter = new FileWriter(path, true)) {
            fileWriter.write(stringToWrite);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (ManagerSaveException e) {
            System.out.println("Произошла ошибка во время чтения файла.");
        }
    }

    public String toString(Task task) {
        return task.getId() + "," +
                task.getType() + "," +
                task.getName() + "," +
                task.getStatus() + "," +
                task.getDescription() + "\r\n";
    }

    public String toString(Epic epic) {
        return epic.getId() + "," +
                epic.getType() + "," +
                epic.getName() + "," +
                epic.getStatus() + "," +
                epic.getDescription() + "\r\n";
    }

    public String toString(Subtask subtask) {
        return subtask.getId() + "," +
                subtask.getType() + "," +
                subtask.getName() + "," +
                subtask.getStatus() + "," +
                subtask.getDescription() + "," +
                subtask.getEpicId() + "\r\n";
    }

    public static String toString(HistoryManager manager) {
        StringBuilder history = new StringBuilder();
        history.append(" " + "\r\n");
        for (int i = 0; i < manager.getHistory().size(); i++) {
            Task task = manager.getHistory().get(i);
            if (i < manager.getHistory().size() - 1) {
                history.append(task.getId()).append(",");
            } else {
                history.append(task.getId());
            }
        }
        return history.toString();
    }

    public void writeHistory(String history) {
        stringToWrite = history;
        try {
            save();
        } catch (ManagerSaveException e) {
            e.printStackTrace();
        }
    }

    public void readLines(String data) {
        String[] lines = data.split("\r?\n");
        fileHead = lines[0] + "\r\n";
        for (int i = 1; i < lines.length; i++) {
            String line = lines[i];
            if (!line.isBlank()) {
                String[] taskLine = line.split(",");
                switch (taskLine[1]) {
                    case "TASK":
                        Task task = fromString(line);
                        addTask(task);
                        break;
                    case "EPIC":
                        Epic epic = epicFromString(line);
                        addEpicTask(epic);
                        break;
                    case "SUBTASK":
                        Subtask subtask = subtaskFromString(line);
                        addSubtask(subtask);
                        break;
                }
            } else {
                if (!lines[i+1].isBlank() && i+1 < lines.length) {
                    List<Integer> tasksIds = historyFromString(lines[i + 1]);
                    restoreHistory(tasksIds);
                }
                return;
            }
        }
    }

    public Task fromString(String value) {
        String[] taskLine = value.split(",");
        Task task = new Task(taskLine[2]);
        task.setId(Integer.parseInt(taskLine[0]));
        task.setDescription(taskLine[4]);
        task.setStatus(Status.valueOf(taskLine[3]));
        task.setType(TaskTypes.valueOf(taskLine[1]));
        return task;
    }

    public Epic epicFromString(String value) {
        String[] taskLine = value.split(",");
        Epic epic = new Epic(taskLine[2]);
        epic.setId(Integer.parseInt(taskLine[0]));
        epic.setDescription(taskLine[4]);
        epic.setStatus(Status.valueOf(taskLine[3]));
        epic.setType(TaskTypes.valueOf(taskLine[1]));
        return epic;
    }

    public Subtask subtaskFromString(String value) {
        String[] taskLine = value.split(",");
        Subtask subtask = new Subtask(taskLine[2], Integer.parseInt(taskLine[5]));
        subtask.setId(Integer.parseInt(taskLine[0]));
        subtask.setDescription(taskLine[4]);
        subtask.setStatus(Status.valueOf(taskLine[3]));
        subtask.setType(TaskTypes.valueOf(taskLine[1]));
        return subtask;
    }

    public static List<Integer> historyFromString(String value) {
        List<Integer> ids = new ArrayList<>();
        String[] idLine = value.split(",");
        for (String id : idLine) {
            ids.add(Integer.parseInt(id));
        }
        return ids;
    }

    public void restoreHistory(List<Integer> tasksIds) {
        for (int id : tasksIds) {
            if (super.tasks.containsKey(id)) {
                super.getTaskById(id);
            }
            if (super.epics.containsKey(id)) {
                super.getEpicById(id);
            }
            if (super.subtasks.containsKey(id)) {
                super.getSubtaskById(id);
            }
        }
    }

    private String readFile(String path) {
        try {
            return Files.readString(Path.of(path));
        } catch (IOException e) {
            System.out.println("Невозможно прочитать файл.");
            return null;
        }
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        stringToWrite = toString(task);
        try {
            save();
        } catch (ManagerSaveException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addEpicTask(Epic task) {
        super.addEpicTask(task);
        stringToWrite = toString(task);
        try {
            save();
        } catch (ManagerSaveException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addSubtask(Subtask task) {
        super.addSubtask(task);
        stringToWrite = toString(task);
        try {
            save();
        } catch (ManagerSaveException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Task getTaskById(int id) {
        if (tasks.containsKey(id)) {
            historyManager.add(tasks.get(id));
            stringToWrite = toString(tasks.get(id));
            return tasks.get(id);
        }
        return null;
    }

    @Override
    public Epic getEpicById(int id) {
        if (epics.containsKey(id)) {
            historyManager.add(epics.get(id));
            stringToWrite = toString(epics.get(id));
            return epics.get(id);
        }
        return null;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        if (subtasks.containsKey(id)) {
            historyManager.add(subtasks.get(id));
            stringToWrite = toString(subtasks.get(id));
            return subtasks.get(id);
        }
        return null;
    }

    public static void main(String[] args) {

        System.out.println("\nЗагружаем программу");
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager("resources/data.csv");
        System.out.println("\nСчитываем файл");
        String data = fileBackedTasksManager.readFile(fileBackedTasksManager.getPath());
        System.out.println("\nВосстанавливаем данные и историю просмотров");
        fileBackedTasksManager.readLines(data);

        System.out.println("\nПроверяем задачи");
        fileBackedTasksManager.showAllTasks();
        System.out.println("\nПроверяем историю");
        System.out.println(fileBackedTasksManager.getHistoryManager().getHistory());

        System.out.println("\nДобавляем задачу");
        Task task = new Task("Task2");
        task.setDescription("Test6");
        fileBackedTasksManager.addTask(task);

        System.out.println("\nПроверяем что задача добавлена");
        fileBackedTasksManager.showAllTasks();
        System.out.println("\nВызываем задачу");
        System.out.println(fileBackedTasksManager.getTaskById(task.getId()));
        System.out.println("\nПроверяем что задача добавлена в историю");
        System.out.println(fileBackedTasksManager.getHistoryManager().getHistory());

        System.out.println("\nСохраняем историю перед выходом");
        fileBackedTasksManager.writeHistory(toString(fileBackedTasksManager.getHistoryManager()));

    }

}
