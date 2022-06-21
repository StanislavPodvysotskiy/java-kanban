package tasks;
import java.util.ArrayList;

public class Epic extends Task {

    //список хранит подзадачи для эпика
    private final ArrayList<Subtask> subtaskIds = new ArrayList<>();

    public Epic(String name) {
        super(name);
    }

    public void addSubtask(Subtask subtask) {
        subtaskIds.add(subtask);
    }

    public ArrayList<Subtask> getSubtaskIds() {
        return subtaskIds;
    }

}