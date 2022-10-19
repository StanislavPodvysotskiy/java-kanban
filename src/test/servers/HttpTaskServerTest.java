package test.servers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static main.servers.HttpTaskServer.fileBackedTaskManager;
import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {

    HttpClient client = HttpClient.newHttpClient();

    @Test
    public void shouldGetSortedTasks() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int responseCode = response.statusCode();
        assertEquals(200, responseCode);
    }

    @Test
    public void shouldGetAllTasks() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int responseCode = response.statusCode();
        assertEquals(200, responseCode);
    }

    @Test
    public void shouldGetTaskWithId1() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        int id = jsonObject.get("id").getAsInt();
        assertEquals(1, id);
    }

    @Test
    public void shouldBeResponseCode404Get() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task/?id=10");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int responseCode = response.statusCode();
        assertEquals(404, responseCode);
    }

    @Test
    public void shouldAddNewTask() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task/");
        String json = "{\n" +
                "\t\"id\": \"-1\",\n" +
                "\t\"name\": \"Тест задача\",\n" +
                "\t\"description\": \"Тестируем\",\n" +
                "\t\"status\": \"NEW\",\n" +
                "\t\"startTime\": \"09.10.2022 15:00\",\n" +
                "\t\"duration\": \"2\",\n" +
                "\t\"type\": \"TASK\"\n" +
                "}";
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Accept", "application/json")
                .POST(body)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int responseCode = response.statusCode();
        assertEquals(200, responseCode);
    }

    @Test
    public void shouldRemoveTaskById() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task/?id=2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int responseCode = response.statusCode();
        assertEquals(200, responseCode);
    }

    @Test
    public void shouldBeResponseCode404Delete() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task/?id=10");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int responseCode = response.statusCode();
        assertEquals(404, responseCode);
    }
}