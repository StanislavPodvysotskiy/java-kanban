package tasks;
import java.util.ArrayList;

public class Epic extends Task {

    //список хранит подзадачи для эпика
    private final ArrayList<Subtask> subtasks = new ArrayList<>();

    public Epic(String name) {
        super(name);
        super.type = TaskTypes.EPIC;
    }

    public void addSubtask(Subtask subtask) {
        subtasks.add(subtask);
    }

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }

}