public class SubTask extends Task {

    private int epicsId;  //в этой переменной хранится ID эпика, для которого она создана

    public SubTask(String nameOfTask, int epicsId) {
        super(nameOfTask);
        this.epicsId=epicsId;
    }

    public int getEpicsId() {
        return epicsId;
    }

}