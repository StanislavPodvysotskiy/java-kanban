package main.tasks;
import java.time.LocalDateTime;
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

    /*@Override
    public LocalDateTime getStartTime() {
        if (subtasks.isEmpty()) {
            return null;
        }
        startTime = subtasks.get(0).getStartTime();
        for (Subtask subtask : subtasks) {
            if (subtask.getStartTime().isBefore(startTime)) {
                startTime = subtask.getStartTime();
            }
        }
        return startTime;
    }*/

    @Override
    public String getEndTime() {
        if (subtasks.isEmpty()) {
            return null;
        }
        LocalDateTime endTime = startTime;
        endTime = LocalDateTime.parse(subtasks.get(0).getEndTime(), DATE_TIME_FORMATTER);
        for (Subtask subtask : subtasks) {
            int result = endTime.compareTo(LocalDateTime.parse(subtask.getEndTime(), DATE_TIME_FORMATTER));
            if (result < 0) {
                endTime = LocalDateTime.parse(subtask.getEndTime(), DATE_TIME_FORMATTER);
            }
        }
        return endTime.format(DATE_TIME_FORMATTER);
    }

}