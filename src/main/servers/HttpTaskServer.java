package main.servers;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import main.enums.Status;
import main.managers.FileBackedTaskManager;
import main.managers.Managers;
import main.tasks.Epic;
import main.tasks.Subtask;
import main.tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final Gson gson = new Gson();
    public static final FileBackedTaskManager fileBackedTaskManager = Managers.getDefaultFileBackedTaskManager(
            "resources/data.csv");

    public static void main(String[] args) throws IOException {
        HttpServer httpServer = HttpServer.create();
        String data = fileBackedTaskManager.readFile(fileBackedTaskManager.getPath());
        fileBackedTaskManager.readLines(data);


        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new PostsHandler());
        httpServer.createContext("/tasks/task", new PostsTaskHandler());
        httpServer.createContext("/tasks/epic", new PostsEpicHandler());
        httpServer.createContext("/tasks/subtask", new PostsSubtaskHandler());
        httpServer.createContext("/tasks/history", new PostsHistoryHandler());
        httpServer.start();

        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
        fileBackedTaskManager.writeHistory(fileBackedTaskManager.toString(fileBackedTaskManager.getHistoryManager()));
        //httpServer.stop(1);
    }

    static class PostsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            System.out.println("Запрошены все задачи в приоритетном порядке");
            String response = gson.toJson(fileBackedTaskManager.getPrioritizedTasks()) +
                    gson.toJson(fileBackedTaskManager.getPrioritizedEpics());
            httpExchange.sendResponseHeaders(200, 0);

            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

    static class PostsTaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String response = "";

            String method = httpExchange.getRequestMethod();
            String path = httpExchange.getRequestURI().getPath();
            String query = httpExchange.getRequestURI().getQuery();

            switch (method) {
                case "GET":
                    if (Objects.nonNull(query)) {
                        System.out.println("Запрошена задача по ID");
                        int id = Integer.parseInt(query.substring(3));
                        if (fileBackedTaskManager.getTaskById(id) != null) {
                            response = gson.toJson(fileBackedTaskManager.getTaskById(id));
                            httpExchange.sendResponseHeaders(200, 0);
                        } else if (fileBackedTaskManager.getEpicById(id) != null) {
                            response = gson.toJson(fileBackedTaskManager.getEpicById(id));
                            httpExchange.sendResponseHeaders(200, 0);
                        } else if (fileBackedTaskManager.getSubtaskById(id) != null) {
                            response = gson.toJson(fileBackedTaskManager.getSubtaskById(id));
                            httpExchange.sendResponseHeaders(200, 0);
                        } else {
                            response = "Задание с таким ID нет";
                            httpExchange.sendResponseHeaders(404, 0);
                        }
                    } else {
                        System.out.println("Запрошены все задачи");
                        response = gson.toJson(fileBackedTaskManager.getAllTasks());
                        httpExchange.sendResponseHeaders(200, 0);
                    }
                    break;
                case "POST":
                    InputStream inputStream = httpExchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                    System.out.println(body);
                    JsonElement jsonElement = JsonParser.parseString(body);
                    if (!jsonElement.isJsonObject()) {
                        response = "Не верное тело запроса";
                        httpExchange.sendResponseHeaders(400, 0);
                    } else {
                        JsonObject jsonObject = jsonElement.getAsJsonObject();
                        int id = jsonObject.get("id").getAsInt();
                        String name = jsonObject.get("name").getAsString();
                        String description = jsonObject.get("description").getAsString();
                        String status = jsonObject.get("status").getAsString();
                        String startTime = jsonObject.get("startTime").getAsString();
                        Long duration = jsonObject.get("duration").getAsLong();
                        String type = jsonObject.get("type").getAsString();
                        if (fileBackedTaskManager.getTaskById(id) != null) {
                            fileBackedTaskManager.getTaskById(id).setDescription(description);
                            fileBackedTaskManager.getTaskById(id).setStatus(Status.valueOf(status));
                            fileBackedTaskManager.getTaskById(id).setStartTime(startTime);
                            fileBackedTaskManager.getTaskById(id).setDuration(duration);
                            response = "Задача с ID " + id + " обновлена";
                            httpExchange.sendResponseHeaders(200, 0);
                        } else if (fileBackedTaskManager.getEpicById(id) != null) {
                            fileBackedTaskManager.getEpicById(id).setDescription(description);
                            fileBackedTaskManager.getEpicById(id).setStatus(Status.valueOf(status));
                            fileBackedTaskManager.getEpicById(id).setStartTime(startTime);
                            fileBackedTaskManager.getEpicById(id).setDuration(duration);
                            response = "Эпик задача с ID " + id + " обновлена";
                            httpExchange.sendResponseHeaders(200, 0);
                        } else if (fileBackedTaskManager.getSubtaskById(id) != null) {
                            fileBackedTaskManager.getSubtaskById(id).setDescription(description);
                            fileBackedTaskManager.getSubtaskById(id).setStatus(Status.valueOf(status));
                            fileBackedTaskManager.getSubtaskById(id).setStartTime(startTime);
                            fileBackedTaskManager.getSubtaskById(id).setDuration(duration);
                            response = "Подзадача с ID " + id + " обновлена";
                            httpExchange.sendResponseHeaders(200, 0);
                        } else {
                            if (type.equals("TASK")) {
                                int sizeBefore = fileBackedTaskManager.getTasks().size();
                                Task task = new Task(name);
                                task.setDescription(description);
                                task.setStatus(Status.valueOf(status));
                                task.setStartTime(startTime);
                                task.setDuration(duration);
                                fileBackedTaskManager.addTask(task);
                                if (fileBackedTaskManager.getTasks().size() > sizeBefore) {
                                    response = "Задача создана";
                                    httpExchange.sendResponseHeaders(200, 0);
                                } else {
                                    response = "На данную дату и время уже есть задача, выбирите другое время";
                                    httpExchange.sendResponseHeaders(400, 0);
                                }

                            }
                            if (type.equals("EPIC")) {
                                int sizeBefore = fileBackedTaskManager.getEpics().size();
                                Epic epic = new Epic(name);
                                epic.setDescription(description);
                                epic.setStatus(Status.valueOf(status));
                                epic.setStartTime(startTime);
                                epic.setDuration(duration);
                                fileBackedTaskManager.addEpicTask(epic);
                                if (fileBackedTaskManager.getTasks().size() > sizeBefore) {
                                    response = "Эпик задача создана";
                                    httpExchange.sendResponseHeaders(200, 0);
                                } else {
                                    response = "На данную дату и время уже есть задача, выбирите другое время";
                                    httpExchange.sendResponseHeaders(400, 0);
                                }
                            }
                            if (type.equals("SUBTASK")) {
                                int sizeBefore = fileBackedTaskManager.getSubtasks().size();
                                int epicId = jsonObject.get("epic").getAsInt();
                                Subtask subtask = new Subtask(name, epicId);
                                subtask.setDescription(description);
                                subtask.setStatus(Status.valueOf(status));
                                subtask.setStartTime(startTime);
                                subtask.setDuration(duration);
                                fileBackedTaskManager.addSubtask(subtask);
                                if (fileBackedTaskManager.getTasks().size() > sizeBefore) {
                                    response = "Подзадача создана";
                                    httpExchange.sendResponseHeaders(200, 0);
                                } else {
                                    response = "На данную дату и время уже есть задача, выбирите другое время";
                                    httpExchange.sendResponseHeaders(400, 0);
                                }
                            }
                        }
                    }
                    break;
                case "DELETE":
                    if (path.equals("/tasks/task")) {
                        fileBackedTaskManager.removeAllTasks();
                        response = "Все задания удалены";
                        httpExchange.sendResponseHeaders(200, 0);
                    }
                    if (Objects.nonNull(query)) {
                        int id = Integer.parseInt(query.substring(3));
                        if (fileBackedTaskManager.getTaskById(id) != null) {
                            fileBackedTaskManager.removeTaskById(id);
                            response = "Задание с ID " + id + " удалено";
                            httpExchange.sendResponseHeaders(200, 0);
                        } else if (fileBackedTaskManager.getEpicById(id) != null) {
                            fileBackedTaskManager.removeEpicById(id);
                            response = "Задание с ID " + id + " удалено";
                            httpExchange.sendResponseHeaders(200, 0);
                        } else if (fileBackedTaskManager.getSubtaskById(id) != null) {
                            fileBackedTaskManager.removeSubtaskById(id);
                            response = "Задание с ID " + id + " удалено";
                            httpExchange.sendResponseHeaders(200, 0);
                        } else {
                            response = "Задание с таким ID нет";
                            httpExchange.sendResponseHeaders(404, 0);
                        }
                    }
                    break;
                default:
                    response = "Некорректный метод!";
                    httpExchange.sendResponseHeaders(400, 0);
            }

            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

    static class PostsEpicHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String query = httpExchange.getRequestURI().getQuery();
            int id = Integer.parseInt(query.substring(3));
            String response = gson.toJson(fileBackedTaskManager.getEpicById(id));

            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

    static class PostsSubtaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String response;
            String query = httpExchange.getRequestURI().getQuery();
            int id = Integer.parseInt(query.substring(3));
            if (fileBackedTaskManager.getSubtaskById(id) == null) {
                response = "Такой подзадачи нет";
                httpExchange.sendResponseHeaders(400, 0);
            } else {
                response = "ID эпик задачи " + gson.toJson(fileBackedTaskManager.getSubtaskById(id).getEpicId());
                httpExchange.sendResponseHeaders(200, 0);
            }

            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

    static class PostsHistoryHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String response;
            System.out.println("Запрошена история");
            if (fileBackedTaskManager.getHistoryManager().getHistory() == null) {
                response = "Вы пока не просматривали задачи";
                httpExchange.sendResponseHeaders(200, 0);
            } else {
                response = gson.toJson(fileBackedTaskManager.getHistoryManager().getHistory());
                httpExchange.sendResponseHeaders(200, 0);
            }

            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }
}