package main.managers;

import main.interfaces.HistoryManager;
import main.interfaces.TaskManager;

public class Managers {

    public static HTTPTaskManager getDefault(String path) {
        return new HTTPTaskManager(path);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static FileBackedTaskManager getDefaultFileBackedTaskManager(String path) {
        return new FileBackedTaskManager(path);
    }
}
