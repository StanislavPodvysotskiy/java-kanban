package main.tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Task {

    private final String name;
    private String description;
    private int id = -1;
    private Status status = Status.NEW;
    protected LocalDateTime startTime;
    protected Duration duration;
    protected TaskTypes type;
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    public final DateTimeFormatter durationFormatter = DateTimeFormatter.ofPattern("HH");

    public Task(String name) {
        this.name = name;
        this.type = TaskTypes.TASK;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Status setStatus(Status status) {
        this.status = status;
        return status;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(String dateTime) {
        startTime = LocalDateTime.parse(dateTime, DATE_TIME_FORMATTER);
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Long hours) {
        duration = Duration.ofHours(hours);
    }

    public String getEndTime() {
        LocalDateTime endTime = startTime.plus(duration);
        return endTime.format(DATE_TIME_FORMATTER);
    }


    public Status getStatus() {
        return status;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public TaskTypes getType() {
        return type;
    }

    public void setType(TaskTypes type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ID " + id + " Название " + name + "\n";
    }

}