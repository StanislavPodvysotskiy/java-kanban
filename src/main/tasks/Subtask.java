package main.tasks;

import main.enums.TaskTypes;

public class Subtask extends Task {

    //в этой переменной хранится ID эпика, для которого она создана
    private final int epicId;
    private TaskTypes type;

    public Subtask(String name, int epicId) {
        super(name);
        super.type = TaskTypes.SUBTASK;
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

}