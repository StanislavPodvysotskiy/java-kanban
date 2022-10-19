package main.managers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import main.clients.KVTaskClient;

import java.io.IOException;

import main.enums.Status;
import main.tasks.Epic;
import main.tasks.Subtask;
import main.tasks.Task;

public class HTTPTaskManager extends FileBackedTaskManager{

    private final KVTaskClient kvTaskClient;
    private final String key;

    public HTTPTaskManager(String path) {
        super(path);
        this.kvTaskClient = new KVTaskClient(path);
        this.key = getKey();
    }

    public HTTPTaskManager load() throws IOException, InterruptedException {
        String data = kvTaskClient.load(key);
        HTTPTaskManager httpTaskManager = new HTTPTaskManager("http://localhost:8078");
        String[] lines = data.split("%%");
        for (int i = 0; i < lines.length; i++) {
            if(lines[i].substring(2, 4).equals("ID")) {
                String[] historyArray = lines[i].split("\n");
                for (String idSubstring : historyArray) {
                    int id;
                    if (idSubstring.substring(6, 7).equals(" ")) {
                        id = Integer.parseInt(idSubstring.substring(5, 6));
                    } else {
                        id = Integer.parseInt(idSubstring.substring(5, 7));
                    }
                    httpTaskManager.getTaskById(id);
                }
            } else {
                String[] taskArray = lines[i].split("/");
                for (int j = 0; j < taskArray.length; j++) {
                    String taskString = taskArray[j];
                    JsonElement jsonElement = JsonParser.parseString(taskString);
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    String name = jsonObject.get("name").getAsString();
                    String description = jsonObject.get("description").getAsString();
                    int id = jsonObject.get("id").getAsInt();
                    String status = jsonObject.get("status").getAsString();
                    String year = jsonObject.getAsJsonObject("startTime").getAsJsonObject("date")
                            .get("year").getAsString();
                    String month = jsonObject.getAsJsonObject("startTime").getAsJsonObject("date")
                            .get("month").getAsString();
                    if (Integer.parseInt(month) < 10) {
                        month = "0" + month;
                    }
                    String day = jsonObject.getAsJsonObject("startTime").getAsJsonObject("date").get("day")
                            .getAsString();
                    if (Integer.parseInt(day) < 10) {
                        day = "0" + day;
                    }
                    String hour = jsonObject.getAsJsonObject("startTime").getAsJsonObject("time").get("hour")
                            .getAsString();
                    if (Integer.parseInt(hour) < 10) {
                        hour = "0" + hour;
                    }
                    String minute = jsonObject.getAsJsonObject("startTime").getAsJsonObject("time").get("minute")
                            .getAsString();
                    if (Integer.parseInt(minute) < 10) {
                        minute = "0" + minute;
                    }
                    String seconds = jsonObject.getAsJsonObject("duration").get("seconds").getAsString();
                    Long duration = Long.parseLong(seconds) / 60 / 60;
                    String type = jsonObject.get("type").getAsString();
                    switch (type) {
                        case "TASK":
                            Task task = new Task(name);
                            task.setId(id);
                            task.setDescription(description);
                            task.setStatus(Status.valueOf(status));
                            task.setStartTime(day + "." + month + "." + year + " " + hour + ":" + minute);
                            task.setDuration(duration);
                            httpTaskManager.addTaskSuper(task);
                            break;
                        case "EPIC":
                            Epic epic = new Epic(name);
                            epic.setId(id);
                            epic.setDescription(description);
                            epic.setStatus(Status.valueOf(status));
                            epic.setStartTime(day + "." + month + "." + year + " " + hour + ":" + minute);
                            epic.setDuration(duration);
                            httpTaskManager.addEpicTaskSuper(epic);
                            break;
                        case "SUBTASK":
                            int epicId = jsonObject.get("epicId").getAsInt();
                            Subtask subtask = new Subtask(name, epicId);
                            subtask.setId(id);
                            subtask.setDescription(description);
                            subtask.setStatus(Status.valueOf(status));
                            subtask.setStartTime(day + "." + month + "." + year + " " + hour + ":" + minute);
                            subtask.setDuration(duration);
                            httpTaskManager.addSubtaskSuper(subtask);
                            break;
                        default:
                            System.out.println("Неверный тип задачи");
                    }
                }
            }
        }
        return httpTaskManager;
    }

    public void save(String json) throws IOException, InterruptedException {
        //String test = "test";
        kvTaskClient.put(key, json);
    }

    public String getRegisteredKey() {
        return key;
    }

    private String getKey() {
        kvTaskClient.registration(kvTaskClient.getServerUrl());
        return kvTaskClient.getKey();
    }

}
