package main.tasks;

import main.enums.Status;
import main.enums.TaskTypes;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {

    //в этой переменной хранится ID эпика, для которого она создана
    private final int epicId;

    public Subtask(String name, int epicId) {
        super(name);
        super.type = TaskTypes.SUBTASK;
        this.epicId = epicId;
    }

    public Subtask(String name, String description, Status status,
                   LocalDateTime startTime, Duration duration, int epicId, TaskTypes type) {
        super(name, description, status, startTime, duration);
        super.type = TaskTypes.SUBTASK;
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

}