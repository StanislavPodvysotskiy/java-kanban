package main.managers;

import main.enums.Status;
import main.enums.TaskTypes;
import main.interfaces.HistoryManager;
import main.tasks.*;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final String path;
    private String dataToWriteToFile; //в поле хранятся данные для записи

    public FileBackedTaskManager(String path) {
        this.path = path;
    }

    @Override
    public HistoryManager getHistoryManager() {
        return super.historyManager;
    }

    public String getPath() {
        return path;
    }

    //метод стирает содержимое файла и записывает заголовок
    private void writeHead() {
        try (FileWriter fileWriter = new FileWriter(path)) {
            fileWriter.write("id,type,name,status,description,epic,datetime,duration\r\n");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    //метод сохраняет данные в конец файла
    //далее в файл дописываются задачи и история просмотров
    //по условию данный метод должен быть без параметров
    private void save() throws ManagerSaveException {
        try (FileWriter fileWriter = new FileWriter(path, true)) {
            fileWriter.write(dataToWriteToFile);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (ManagerSaveException e) {
            System.out.println("Произошла ошибка во время чтения файла.");
        }
    }

    //метод приобразует задачу в строку
    private String toString(Task task) {
        return task.getId() + "," +
                task.getType() + "," +
                task.getName() + "," +
                task.getStatus() + "," +
                task.getDescription() + ",," +
                task.getStartTime().format(Task.DATE_TIME_FORMATTER) + "," +
                task.getDuration().toHours() + "\r\n";
    }

    //метод приобразует эпик задачу в строку
    private String toString(Epic epic) {
        return epic.getId() + "," +
                epic.getType() + "," +
                epic.getName() + "," +
                epic.getStatus() + "," +
                epic.getDescription() + ",," +
                epic.getStartTime().format(Task.DATE_TIME_FORMATTER) + "," +
                epic.getDuration().toHours() + "\r\n";
    }

    //метод приобразует подзадачу в строку
    private String toString(Subtask subtask) {
        return subtask.getId() + "," +
                subtask.getType() + "," +
                subtask.getName() + "," +
                subtask.getStatus() + "," +
                subtask.getDescription() + "," +
                subtask.getEpicId() + "," +
                subtask.getStartTime().format(Task.DATE_TIME_FORMATTER) + "," +
                subtask.getDuration().toHours() + "\r\n";
    }

    //метод записывает пустую строку, далее получает из списка задачи,
    //далее из задач получает их ID и добавляет в строку
    //получаем пустую строку и последовательность ID соответствующую истории вызовов задач
    public String toString(HistoryManager manager) {
        StringBuilder history = new StringBuilder();
        history.append("\r\n");
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

    //метод получает историю в виде строки и передает ее для записи в файл
    public void writeHistory(String history) {
        dataToWriteToFile = history;
        try {
            save();
        } catch (ManagerSaveException e) {
            e.printStackTrace();
        }
    }

    //метод получает в качестве параметра строку с содержимым файла
    //разбиваем строку и получаем отдельные задачи и историю
    //проверяем у каждой задачи поле Type, в зависимости от значения поля передаем строку в нужный метод
    //продолжаем повторять до тех пор пока не найдем пустую строку, за ней будет история просмотров
    //выделяем строку с историей и преобразуем ее в список ID задач
    //передаем список в метод востанавливающий историю просмотров задач
    public void readLines(String data) {
        writeHead();
        String[] lines = data.split("\r?\n");
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
                if (!lines[i + 1].isBlank() && i + 1 < lines.length) {
                    List<Integer> tasksIds = historyFromString(lines[i + 1]);
                    restoreHistory(tasksIds);
                }
                return;
            }
        }
    }

    //метод приобразует строку в экземпляр объекта Task
    private Task fromString(String value) {
        String[] taskLine = value.split(",");
        Task task = new Task(taskLine[2]);
        task.setId(Integer.parseInt(taskLine[0]));
        task.setDescription(taskLine[4]);
        task.setStatus(Status.valueOf(taskLine[3]));
        task.setType(TaskTypes.valueOf(taskLine[1]));
        task.setStartTime(taskLine[6]);
        task.setDuration(Long.parseLong(taskLine[7]));
        return task;
    }

    //метод приобразует строку в экземпляр объекта Epic
    private Epic epicFromString(String value) {
        String[] taskLine = value.split(",");
        Epic epic = new Epic(taskLine[2]);
        epic.setId(Integer.parseInt(taskLine[0]));
        epic.setDescription(taskLine[4]);
        epic.setStatus(Status.valueOf(taskLine[3]));
        epic.setType(TaskTypes.valueOf(taskLine[1]));
        epic.setStartTime(taskLine[6]);
        epic.setDuration(Long.parseLong(taskLine[7]));
        return epic;
    }

    //метод приобразует строку в экземпляр объекта Subtask
    private Subtask subtaskFromString(String value) {
        String[] taskLine = value.split(",");
        Subtask subtask = new Subtask(taskLine[2], Integer.parseInt(taskLine[5]));
        subtask.setId(Integer.parseInt(taskLine[0]));
        subtask.setDescription(taskLine[4]);
        subtask.setStatus(Status.valueOf(taskLine[3]));
        subtask.setType(TaskTypes.valueOf(taskLine[1]));
        subtask.setStartTime(taskLine[6]);
        subtask.setDuration(Long.parseLong(taskLine[7]));
        return subtask;
    }

    //метод преобразует строку с ID в список с ID
    private static List<Integer> historyFromString(String value) {
        List<Integer> ids = new ArrayList<>();
        String[] idLine = value.split(",");
        for (String id : idLine) {
            ids.add(Integer.parseInt(id));
        }
        return ids;
    }

    //метод востанавливает историю просмотров
    //для восстановления просмотра используется вызов родительского метода,
    //в котором реализована логика добавления в историю
    private void restoreHistory(List<Integer> tasksIds) {
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

    //метод считывает файл и преобразует его в строку
    public String readFile(String path) {
        try {
            return Files.readString(Path.of(path));
        } catch (IOException e) {
            System.out.println("Невозможно прочитать файл.");
            return null;
        }
    }

    //метод выполняет подительский метод, и сохраняет в переменную данные о задаче для записи в файл
    @Override
    public void addTask(Task task) {
        int sizeBefore = super.tasks.size();
        super.addTask(task);
        int sizeAfter = super.tasks.size();
        if (sizeAfter > sizeBefore) {
            dataToWriteToFile = toString(task);
        } else {
            return;
        }
        try {
            save();
        } catch (ManagerSaveException e) {
            e.printStackTrace();
        }
    }

    //метод выполняет подительский метод, и сохраняет в переменную данные об эпик задаче для записи в файл
    @Override
    public void addEpicTask(Epic task) {
        int sizeBefore = super.epics.size();
        super.addEpicTask(task);
        int sizeAfter = super.epics.size();
        if (sizeAfter > sizeBefore) {
            dataToWriteToFile = toString(task);
        } else {
            return;
        }
        try {
            save();
        } catch (ManagerSaveException e) {
            e.printStackTrace();
        }
    }

    //метод выполняет подительский метод, и сохраняет в переменную данные о подзадаче для записи в файл
    @Override
    public void addSubtask(Subtask task) {
        int sizeBefore = super.subtasks.size();
        super.addSubtask(task);
        int sizeAfter = super.subtasks.size();
        if (sizeAfter > sizeBefore) {
            dataToWriteToFile = toString(task);
        } else {
            return;
        }
        try {
            save();
        } catch (ManagerSaveException e) {
            e.printStackTrace();
        }
    }

    public void addTaskSuper(Task task) {
        super.addTask(task);
    }

    public void addEpicTaskSuper(Epic task) {
        super.addEpicTask(task);
    }

    public void addSubtaskSuper(Subtask task) {
        super.addSubtask(task);
    }

    public static void main(String[] args) {

        System.out.println("\nЗагружаем программу");
        FileBackedTaskManager fileBackedTaskManager = Managers.getDefaultFileBackedTaskManager("resources/data.csv");
        System.out.println("\nСчитываем файл");
        String data = fileBackedTaskManager.readFile(fileBackedTaskManager.getPath());
        System.out.println("\nВосстанавливаем данные и историю просмотров");
        fileBackedTaskManager.readLines(data);

        System.out.println("\nПроверяем задачи");
        System.out.println(fileBackedTaskManager.getAllTasks());
        System.out.println("\nПроверяем историю");
        System.out.println(fileBackedTaskManager.getHistoryManager().getHistory());

        System.out.println("\nПытаемся добавить задачу с одинаковыс DateTime");
        Task task = new Task("Task2");
        task.setDescription("Test6");
        task.setStartTime("03.10.2022 13:00");
        task.setDuration(1L);
        fileBackedTaskManager.addTask(task);

        System.out.println("Вычисляем время завершения эпик задачи");
        System.out.println(fileBackedTaskManager.getEpicById(2).getEndTime());

        System.out.println("\nСохраняем историю перед выходом");
        fileBackedTaskManager.writeHistory(fileBackedTaskManager.toString(fileBackedTaskManager.getHistoryManager()));
    }

}
