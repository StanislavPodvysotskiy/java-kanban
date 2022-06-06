import java.util.ArrayList;

public class Epic extends Task {
    //список хранит ID всех подзадач для эпика
    private final ArrayList<Integer> subtaskIds = new ArrayList<>();

    public Epic(String name) {
        super(name);
    }

    public ArrayList<Integer> getSubtaskIds() {
        return subtaskIds;
    }

}