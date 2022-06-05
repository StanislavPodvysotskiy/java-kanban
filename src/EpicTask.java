import java.util.ArrayList;

public class EpicTask extends Task {
    private ArrayList<Integer> subTasksIds = new ArrayList<>(); //список хранит ID всех подзадач для эпика

    public EpicTask(String nameOfTask) {
        super(nameOfTask);
    }

    public ArrayList<Integer> getSubTasksIds() {
        return subTasksIds;
    }

}