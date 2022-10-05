package test.managers;

import main.managers.FileBackedTasksManager;
import main.managers.HistoryManager;
import main.tasks.Subtask;
import main.tasks.Task;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest {

    private String toString(HistoryManager manager) {
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

    @Test
    void shouldRestoreHistory() {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager("resources/test1.csv");
        String data = fileBackedTasksManager.readFile(fileBackedTasksManager.getPath());
        fileBackedTasksManager.readLines(data);
        fileBackedTasksManager.writeHistory(toString(fileBackedTasksManager.getHistoryManager()));
        assertEquals("ID 1 Название Task1\n", fileBackedTasksManager.showAllTasks());
    }

    @Test
    void shouldBeEmpty() {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager("resources/test2.csv");
        String data = fileBackedTasksManager.readFile(fileBackedTasksManager.getPath());
        fileBackedTasksManager.readLines(data);
        String test = fileBackedTasksManager.showAllTasks();
        assertEquals("Задач пока нет", test);
    }

    @Test
    void shouldBeEmptyHistory() {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager("resources/test3.csv");
        String data = fileBackedTasksManager.readFile(fileBackedTasksManager.getPath());
        fileBackedTasksManager.readLines(data);
        List<Task> test = fileBackedTasksManager.getHistoryManager().getHistory();
        assertNull(test);
    }

    @Test
    void shouldBeWithoutSubtasks() {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager("resources/test4.csv");
        String data = fileBackedTasksManager.readFile(fileBackedTasksManager.getPath());
        fileBackedTasksManager.readLines(data);
        List<Subtask> test = fileBackedTasksManager.getEpicById(1).getSubtasks();
        assertEquals(0, test.size());
    }

}