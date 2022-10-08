package test.managers;

import main.managers.Managers;
import main.interfaces.TaskManager;
import main.tasks.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    TaskManager taskManager;

    @BeforeEach
    public void beforeEach() {
        taskManager = Managers.getDefault();
    }

    @Test
    void shouldBeNullWhenHistoryIsEmpty() {
        assertNull(taskManager.getHistoryManager().getHistory());
    }

    @Test
    void shouldReplaceDuplicate() {
        Task task1 = new Task("task1");
        task1.setDescription("test");
        task1.setStartTime("03.10.2022 15:00");
        taskManager.addTask(task1);
        int id1 = task1.getId();
        Task task2 = new Task("task2");
        task2.setDescription("test");
        task2.setStartTime("03.10.2022 17:00");
        taskManager.addTask(task2);
        int id2 = task2.getId();
        taskManager.getTaskById(id1);
        taskManager.getTaskById(id2);
        taskManager.getTaskById(id1);
        List<Task> test = taskManager.getHistoryManager().getHistory();
        assertEquals("task2", test.get(0).getName());
    }

    @Test
    void shouldRemoveFirst() {
        Task task1 = new Task("task1");
        task1.setDescription("test");
        task1.setStartTime("03.10.2022 15:00");
        taskManager.addTask(task1);
        int id1 = task1.getId();
        Task task2 = new Task("task2");
        task2.setStartTime("03.10.2022 17:00");
        task2.setDescription("test");
        taskManager.addTask(task2);
        int id2 = task2.getId();
        taskManager.getTaskById(id1);
        taskManager.getTaskById(id2);
        taskManager.removeTaskById(id1);
        List<Task> test = taskManager.getHistoryManager().getHistory();
        assertEquals("task2", test.get(0).getName());
    }

    @Test
    void shouldRemoveMiddle() {
        Task task1 = new Task("task1");
        task1.setDescription("test");
        task1.setStartTime("03.10.2022 15:00");
        taskManager.addTask(task1);
        int id1 = task1.getId();
        Task task2 = new Task("task2");
        task2.setDescription("test");
        task2.setStartTime("03.10.2022 17:00");
        taskManager.addTask(task2);
        int id2 = task2.getId();
        Task task3 = new Task("task3");
        task3.setDescription("test");
        task3.setStartTime("03.10.2022 19:00");
        taskManager.addTask(task3);
        int id3 = task3.getId();
        taskManager.getTaskById(id1);
        taskManager.getTaskById(id2);
        taskManager.getTaskById(id3);
        taskManager.removeTaskById(id2);
        List<Task> test = taskManager.getHistoryManager().getHistory();
        assertEquals("task3", test.get(1).getName());
    }

    @Test
    void shouldRemoveLast() {
        Task task1 = new Task("task1");
        task1.setDescription("test");
        task1.setStartTime("03.10.2022 15:00");
        taskManager.addTask(task1);
        int id1 = task1.getId();
        Task task2 = new Task("task2");
        task2.setStartTime("03.10.2022 17:00");
        task2.setDescription("test");
        taskManager.addTask(task2);
        int id2 = task2.getId();
        Task task3 = new Task("task3");
        task3.setDescription("test");
        task3.setStartTime("03.10.2022 19:00");
        taskManager.addTask(task3);
        int id3 = task3.getId();
        taskManager.getTaskById(id1);
        taskManager.getTaskById(id2);
        taskManager.getTaskById(id3);
        taskManager.removeTaskById(id3);
        List<Task> test = taskManager.getHistoryManager().getHistory();
        assertEquals("task2", test.get(1).getName());
    }

    @Test
    void shouldClearHistory() {
        Task task = new Task("task");
        task.setDescription("test");
        task.setStartTime("03.10.2022 15:00");
        taskManager.addTask(task);
        int id = task.getId();
        taskManager.getTaskById(id);
        taskManager.removeAllTasks();
        List<Task> test = taskManager.getHistoryManager().getHistory();
        assertNull(test);
    }
}