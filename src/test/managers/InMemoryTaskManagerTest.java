package test.managers;

import main.managers.HistoryManager;
import main.managers.Managers;
import main.managers.TaskManager;
import main.tasks.Epic;
import main.tasks.Status;
import main.tasks.Subtask;
import main.tasks.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    private TaskManager taskManager;

    @BeforeEach
    public void beforeEach() {
        taskManager = Managers.getDefault();
    }

    @Test
    public void shouldCreateTask() {
        Task task = new Task("task");
        task.setDescription("test");
        task.setStartTime("03.10.2022 15:00");
        taskManager.addTask(task);
        int id = task.getId();
        Task test = taskManager.getTaskById(id);
        assertNotNull(test);
    }

    @Test
    public void shouldCreateEpicTask() {
        Epic epic = new Epic("epic");
        epic.setDescription("test");
        epic.setStartTime("03.10.2022 11:00");
        taskManager.addEpicTask(epic);
        int id = epic.getId();
        Epic test = taskManager.getEpicById(id);
        assertNotNull(test);
    }

    @Test
    public void shouldCreateSubtask() {
        Epic epic = new Epic("epic");
        epic.setDescription("test");
        epic.setStartTime("03.10.2022 15:00");
        taskManager.addEpicTask(epic);
        Subtask subtask = new Subtask("subtask", epic.getId());
        subtask.setDescription("test");
        taskManager.addSubtask(subtask);
        int id = subtask.getId();
        Subtask test = taskManager.getSubtaskById(id);
        assertNotNull(test);
    }

    @Test
    public void shouldDoNotAddTaskWithSameDateTime() {
        Task task1 = new Task("task1");
        task1.setDescription("test");
        task1.setStartTime("03.10.2022 15:00");
        taskManager.addTask(task1);

        Task task2 = new Task("task2");
        task2.setStartTime("03.10.2022 15:00");
        task2.setDescription("test");
        taskManager.addTask(task2);

        assertEquals("ID 1 Название task1\n", taskManager.showAllTasks());
    }

    @Test
    public void shouldGetTaskById() {
        Task task = new Task("task");
        task.setDescription("test");
        task.setStartTime("03.10.2022 15:00");
        taskManager.addTask(task);
        int id = task.getId();
        Task test = taskManager.getTaskById(id);
        assertNotNull(test);
    }

    @Test
    public void shouldGetEpicById() {
        Epic epic = new Epic("epic");
        epic.setDescription("test");
        epic.setStartTime("03.10.2022 15:00");
        taskManager.addEpicTask(epic);
        int id = epic.getId();
        Epic test = taskManager.getEpicById(id);
        assertNotNull(test);
    }

    @Test
    public void shouldGetSubtaskById() {
        Epic epic = new Epic("epic");
        epic.setDescription("test");
        epic.setStartTime("03.10.2022 15:00");
        taskManager.addEpicTask(epic);
        Subtask subtask = new Subtask("subtask", epic.getId());
        subtask.setDescription("test");
        taskManager.addSubtask(subtask);
        int id = subtask.getId();
        Subtask test = taskManager.getSubtaskById(id);
        assertNotNull(test);
    }

    @Test
    public void shouldBeNullIfWrongId() {
        Task test = taskManager.getTaskById(1);
        assertNull(test);
    }

    @Test
    public void shouldShowAllTasks() {
        Task task = new Task("task");
        task.setDescription("test");
        task.setStartTime("03.10.2022 15:00");
        taskManager.addTask(task);
        assertEquals("ID 1 Название task\n", taskManager.showAllTasks());
    }

    @Test
    public void shouldGiveMessageWhenNoTasks() {
        String test = taskManager.showAllTasks();
        assertEquals("Задач пока нет", test);
    }

    @Test
    public void shouldGetStatusNew() {
        Task task = new Task("task");
        task.setDescription("test");
        task.setStartTime("03.10.2022 15:00");
        taskManager.addTask(task);
        int id = task.getId();
        String status = taskManager.getStatusById(id);
        Assertions.assertEquals("NEW", status);
    }

    @Test
    public void shouldSetStatusDone() {
        Task task = new Task("task");
        task.setDescription("test");
        task.setStartTime("03.10.2022 15:00");
        taskManager.addTask(task);
        int id = task.getId();
        taskManager.setStatusById(id, "DONE");
        String status = taskManager.getStatusById(id);
        Assertions.assertEquals("DONE", status);
    }

    @Test
    public void shouldGiveMessageWhenWrongId() {
        String test = taskManager.setStatusById(1, "DONE");
        assertEquals("Такой задачи нет", test);
    }

    @Test
    public void shouldRemoveTaskById() {
        Task task = new Task("task");
        task.setDescription("test");
        task.setStartTime("03.10.2022 15:00");
        taskManager.addTask(task);
        int id = task.getId();
        taskManager.getTaskById(id);
        taskManager.removeTaskById(id);
        Task test = taskManager.getTaskById(id);
        assertNull(test);
    }

    @Test
    public void shouldRemoveEpicById() {
        Epic epic = new Epic("epic");
        epic.setDescription("test");
        epic.setStartTime("03.10.2022 15:00");
        taskManager.addEpicTask(epic);
        int id = epic.getId();
        taskManager.getEpicById(id);
        taskManager.removeEpicById(id);
        Epic test = taskManager.getEpicById(id);
        assertNull(test);
    }

    @Test
    public void shouldRemoveSubtaskById() {
        Epic epic = new Epic("epic");
        epic.setDescription("test");
        epic.setStartTime("03.10.2022 15:00");
        taskManager.addEpicTask(epic);
        Subtask subtask = new Subtask("subtask", epic.getId());
        subtask.setDescription("test");
        taskManager.addSubtask(subtask);
        int id = subtask.getId();
        taskManager.getSubtaskById(id);
        taskManager.removeSubtaskById(id);
        Subtask test = taskManager.getSubtaskById(id);
        assertNull(test);
    }

    @Test
    public void shouldRemoveAllTasks() {
        Task task = new Task("task");
        task.setDescription("test");
        task.setStartTime("03.10.2022 15:00");
        taskManager.addTask(task);
        int id = task.getId();
        taskManager.removeAllTasks();
        Task test = taskManager.getTaskById(id);
        assertNull(test);
    }

    @Test
    public void shouldGetEpicSubtasks() {
        Epic epic = new Epic("epic");
        epic.setDescription("test");
        epic.setStartTime("03.10.2022 15:00");
        taskManager.addEpicTask(epic);
        Subtask subtask = new Subtask("subtask", epic.getId());
        subtask.setDescription("test");
        taskManager.addSubtask(subtask);
        int id = epic.getId();
        assertEquals("Найдена подзадача ID 2", taskManager.getEpicSubtasks(id));
    }

    @Test
    public void shouldEpicStatusNew() {
        Epic epic = new Epic("epic");
        epic.setDescription("test");
        epic.setStartTime("03.10.2022 15:00");
        taskManager.addEpicTask(epic);
        Subtask subtask = new Subtask("subtask", epic.getId());
        subtask.setDescription("test");
        taskManager.addSubtask(subtask);
        int id = epic.getId();
        Status status = taskManager.getEpicStatus(id);
        Assertions.assertEquals(Status.NEW, status);
    }

    @Test
    public void shouldEpicStatusInProgressWithOneSubtask() {
        Epic epic = new Epic("epic");
        epic.setDescription("test");
        epic.setStartTime("03.10.2022 15:00");
        taskManager.addEpicTask(epic);
        Subtask subtask = new Subtask("subtask", epic.getId());
        subtask.setDescription("test");
        taskManager.addSubtask(subtask);
        subtask.setStatus(Status.IN_PROGRESS);
        int id = epic.getId();
        Status status = taskManager.getEpicStatus(id);
        Assertions.assertEquals(Status.IN_PROGRESS, status);
    }

    @Test
    public void shouldEpicStatusInProgressWithTwoSubtask() {
        Epic epic = new Epic("epic");
        epic.setDescription("test");
        epic.setStartTime("03.10.2022 15:00");
        taskManager.addEpicTask(epic);
        Subtask subtask1 = new Subtask("subtask1", epic.getId());
        subtask1.setDescription("test");
        subtask1.setStartTime("03.10.2022 16:00");
        Subtask subtask2 = new Subtask("subtask2", epic.getId());
        subtask2.setDescription("test");
        subtask2.setStartTime("03.10.2022 17:00");
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        subtask1.setStatus(Status.DONE);
        int id = epic.getId();
        Status status = taskManager.getEpicStatus(id);
        Assertions.assertEquals(Status.IN_PROGRESS, status);
    }

    @Test
    public void shouldEpicStatusDone() {
        Epic epic = new Epic("epic");
        epic.setDescription("test");
        epic.setStartTime("03.10.2022 15:00");
        taskManager.addEpicTask(epic);
        Subtask subtask = new Subtask("subtask", epic.getId());
        subtask.setDescription("test");
        taskManager.addSubtask(subtask);
        subtask.setStatus(Status.DONE);
        int id = epic.getId();
        Status status = taskManager.getEpicStatus(id);
        Assertions.assertEquals(Status.DONE, status);
    }

    @Test
    public void shouldCheckHistoryManagerNotNull() {
        Task task = new Task("task");
        task.setDescription("test");
        task.setStartTime("03.10.2022 15:00");
        taskManager.addTask(task);
        int id = task.getId();
        taskManager.getTaskById(id);
        HistoryManager history = taskManager.getHistoryManager();
        List<Task> list = history.getHistory();
        Task test = list.get(0);
        assertNotNull(test);
    }

    @Test
    public void shouldBeTask2BeforeTask1() {
        Task task1 = new Task("task1");
        task1.setDescription("test");
        task1.setStartTime("03.10.2022 15:00");
        task1.setDuration(2L);
        taskManager.addTask(task1);

        Task task2 = new Task("task2");
        task2.setDescription("test");
        task2.setStartTime("03.10.2022 13:00");
        task2.setDuration(1L);
        taskManager.addTask(task2);

        TreeSet<Task> set = taskManager.getPrioritizedTasks();
        Task test = set.first();
        assertEquals("ID 2 Название task2\n", test.toString());
    }

    @Test
    public void shouldGetEndTime1900() {
        Epic epic1 = new Epic("E1");
        epic1.setDescription("эпик 1");
        epic1.setStartTime("03.10.2022 13:00");
        taskManager.addEpicTask(epic1);

        Subtask subtask1 = new Subtask("S1", epic1.getId());
        subtask1.setDescription("сабтаск 1");
        subtask1.setStartTime("03.10.2022 13:00");
        subtask1.setDuration(3L);
        taskManager.addSubtask(subtask1);

        Subtask subtask2 = new Subtask("S2", epic1.getId());
        subtask2.setDescription("сабтаск 2");
        subtask2.setStartTime("03.10.2022 18:00");
        subtask2.setDuration(1L);
        taskManager.addSubtask(subtask2);

        assertEquals("03.10.2022 19:00", taskManager.getEpicById(epic1.getId()).getEndTime());
    }
}