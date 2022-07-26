package tasks;

public class Task {

    private final String name;
    private String description;
    private int id = -1;
    private Status status = Status.NEW;
    protected TaskTypes type;

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