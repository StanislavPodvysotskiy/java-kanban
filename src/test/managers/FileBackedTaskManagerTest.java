package test.managers;

import main.managers.FileBackedTaskManager;
import main.interfaces.HistoryManager;
import main.tasks.Subtask;
import main.tasks.Task;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {

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
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager("resources/test1.csv");
        String data = fileBackedTaskManager.readFile(fileBackedTaskManager.getPath());
        fileBackedTaskManager.readLines(data);
        fileBackedTaskManager.writeHistory(toString(fileBackedTaskManager.getHistoryManager()));
        assertEquals("ID 1 Название Task1\n", fileBackedTaskManager.getAllTasks());
    }

    @Test
    void shouldBeEmpty() {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager("resources/test2.csv");
        String data = fileBackedTaskManager.readFile(fileBackedTaskManager.getPath());
        fileBackedTaskManager.readLines(data);
        String test = fileBackedTaskManager.getAllTasks();
        assertEquals("Задач пока нет", test);
    }

    @Test
    void shouldBeEmptyHistory() {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager("resources/test3.csv");
        String data = fileBackedTaskManager.readFile(fileBackedTaskManager.getPath());
        fileBackedTaskManager.readLines(data);
        List<Task> test = fileBackedTaskManager.getHistoryManager().getHistory();
        assertNull(test);
    }

    @Test
    void shouldBeWithoutSubtasks() {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager("resources/test4.csv");
        String data = fileBackedTaskManager.readFile(fileBackedTaskManager.getPath());
        fileBackedTaskManager.readLines(data);
        List<Subtask> test = fileBackedTaskManager.getEpicById(1).getSubtasks();
        assertEquals(0, test.size());
    }

}