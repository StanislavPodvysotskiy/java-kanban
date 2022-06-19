package Tasks;

public class Task {

    private final String name;
    private String description;
    private int id;
    private Status status = Status.NEW;

    public Task(String name) {
        this.name = name;
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

    @Override
    public String toString() {
        return "ID " + id + " Название " + name + "\n";
    }

}