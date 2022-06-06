public class Subtask extends Task {

    //в этой переменной хранится ID эпика, для которого она создана
    private final int epicId;

    public Subtask(String name, int epicId) {
        super(name);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

}