import java.util.ArrayList;
import java.util.List;

class InMemoryHistoryManager implements HistoryManager {

    private static final List<Task> TASKS_HISTORY = new ArrayList<>();

    @Override
    public void add(Task task) {
        if (TASKS_HISTORY.size() < 10) {
            TASKS_HISTORY.add(task);
        } else {
            TASKS_HISTORY.remove(0);
            TASKS_HISTORY.add(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        if (TASKS_HISTORY.isEmpty()) {
            System.out.println("Вы пока не просматривали задачи");
            return null;
        }
        return TASKS_HISTORY;
    }

}
