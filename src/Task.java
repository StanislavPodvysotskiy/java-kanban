public class Task {

    private String nameOfTask;
    private String description;
    private int id;
    private String status = "New";

    public Task(String nameOfTask) {
        this.nameOfTask=nameOfTask;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameOfTask() {
        return nameOfTask;
    }

    public String setStatus(String status) {
        this.status = status;
        return status;
    }

    public String getStatus() {
        return status;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}