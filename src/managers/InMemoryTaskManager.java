package managers;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;
import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    private int lastId = 1;
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();

    //метод добавляет новую задачу в хэшмап
    @Override
    public void addTask(Task task) {
        task.setId(lastId++);
        tasks.put(task.getId(), task);
        System.out.println("Задача создана");
    }

    //метод добавляет новую зпик задачу в хэшмап
    @Override
    public void addEpicTask(Epic task) {
        task.setId(lastId++);
        epics.put(task.getId(), task);
        System.out.println("Задача создана");
    }

    //метод добавляет новую подзадачу в хэшмап
    @Override
    public void addSubtask(Subtask task) {
        if (epics.containsKey(task.getEpicId())) {
            task.setId(lastId++);
            epics.get(task.getEpicId()).addSubtask(task);
            subtasks.put(task.getId(), task);
            System.out.println("Задача создана");
        } else {
            System.out.println("Эпик задачи с таким ID не найдено");
        }
    }

    //метод возвращает название и описание задачи
    @Override
    public Task getTaskById(int id) {
        if (tasks.containsKey(id)) {
            Managers.getDefaultHistory().add(tasks.get(id));
            return tasks.get(id);
        }
        return null;
    }

    //метод возвращает название и описание эпик задачи
    @Override
    public Epic getEpicById(int id) {
        if (epics.containsKey(id)) {
            Managers.getDefaultHistory().add(epics.get(id));
            return epics.get(id);
        }
        return null;
    }

    //метод возвращает название и описание подзадачи
    @Override
    public Subtask getSubtaskById(int id) {
        if (subtasks.containsKey(id)) {
            Managers.getDefaultHistory().add(subtasks.get(id));
            return subtasks.get(id);
        }
        return null;
    }

    //метод проходит циклами по всем хэшмапа, если в них есть объекты и отображает их
    @Override
    public void showAllTasks() {
        if (tasks.isEmpty() && epics.isEmpty() && subtasks.isEmpty()) {
            System.out.println("Задач пока нет");
        } else {
            if (!tasks.isEmpty()) {
                for (Integer task : tasks.keySet()) {
                    System.out.println("ID " + task + " Название " + tasks.get(task).getName());
                }
            }
            if (!epics.isEmpty()) {
                for (Integer epicTask : epics.keySet()) {
                    System.out.println("ID " + epicTask + " Название " + epics.get(epicTask).getName());
                }
            }
            if (!subtasks.isEmpty()) {
                for (Integer subTask : subtasks.keySet()) {
                    System.out.println("ID " + subTask + " Название " + subtasks.get(subTask).getName());
                }
            }
        }
    }

    //запрос статуса у задачи, для эпик задачи статус рассчитывается отдельно
    @Override
    public String getStatusById(int id) {
        String statusDescription = "Такой задачи нет";
        if (tasks.containsKey(id)) {
            statusDescription = tasks.get(id).getStatus().toString();
        } else if (epics.containsKey(id)) {
            statusDescription = getEpicStatus(id).toString();
        } else if (subtasks.containsKey(id)) {
            statusDescription = subtasks.get(id).getStatus().toString();
        }
        return statusDescription;
    }

    //метод меняет статус для обычных задач и подзадач
    @Override
    public String setStatusById(int id, String status) {
        String newStatus = "Статус введен не верно";
        if (status.equals("IN_PROGRESS") || status.equals("DONE")) {
            if (tasks.containsKey(id)) {
                newStatus = "Новый статус - " + tasks.get(id).setStatus(Status.valueOf(status));
            } else if (epics.containsKey(id)) {
                newStatus = "Нельзя изменить статус для эпик задачи.";
            } else if (subtasks.containsKey(id)) {
                newStatus = "Новый статус - " + subtasks.get(id).setStatus(Status.valueOf(status));
            } else {
                newStatus = "Такой задачи нет";
            }
        }
        return newStatus;
    }

    //метод удаляет задачи
    @Override
    public void removeTaskById(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
            Managers.getDefaultHistory().remove(id);
            System.out.println("Задача удалена");
        } else {
            System.out.println("Такой задачи нет");
        }
    }

    //метод удаляет эпик задачи
    //при удалении эпик задачи удаляются все его подзадачи
    @Override
    public void removeEpicById(int id) {
        if (epics.containsKey(id)) {
            for (Subtask subTasksId : epics.get(id).getSubtasks()) {
                subtasks.remove(subTasksId.getId());
                Managers.getDefaultHistory().remove(subTasksId.getId());
            }
            epics.remove(id);
            Managers.getDefaultHistory().remove(id);
            System.out.println("Эпик задача удалена");
        } else {
            System.out.println("Такой эпик задачи нет");
        }
    }

    //метод удаляет задачи
    @Override
    public void removeSubtaskById(int id) {
        if (subtasks.containsKey(id)) {
            for (int i = 0; i < epics.get(subtasks.get(id).getEpicId()).getSubtasks().size(); i++) {
                if (epics.get(subtasks.get(id).getEpicId()).getSubtasks().get(i).getId() == id) {
                    epics.get(subtasks.get(id).getEpicId()).getSubtasks().remove(i);
                }
            }
            subtasks.remove(id);
            Managers.getDefaultHistory().remove(id);
            System.out.println("Подзадача удалена");
        } else {
            System.out.println("Такой подзадачи нет");
        }
    }

    //метод удаляет все задачи
    @Override
    public void removeAllTasks() {
        if (tasks.isEmpty() && epics.isEmpty() && subtasks.isEmpty()) {
            System.out.println("Нечего удалять, у вас пока нет задач");
        } else {
            tasks.clear();
            epics.clear();
            subtasks.clear();
            System.out.println("Все задачи удалены");
        }
    }

    //метод выводит все подзадачи выбранной эпик задачи, если они есть
    @Override
    public void getEpicSubtasks(int id) {
        if (!epics.containsKey(id)) {
            System.out.println("Такой эпик задачи нет");
        } else if (epics.get(id).getSubtasks().isEmpty()) {
            System.out.println("Подзадач пока нет");
        } else {
            System.out.println("Поиск подзадач для эпик задачи с ID " + id);
            for (Task task : epics.get(id).getSubtasks()) {
                System.out.println("Найдена подзадача ID " + task.getId());
            }
        }
    }

    //метод рассчитывает статус эпик задачи
    @Override
    public Status getEpicStatus(int id) {
        Status epicStatus = epics.get(id).getStatus();
        ArrayList<Status> subTaskStatusNew = new ArrayList<>();
        ArrayList<Status> subTaskStatusInProgress = new ArrayList<>();
        ArrayList<Status> subTaskStatusDone = new ArrayList<>();
        for (Task task : epics.get(id).getSubtasks()) {
            if (task.getStatus() == Status.NEW) {
                subTaskStatusNew.add(task.getStatus());
            } else if (task.getStatus() == Status.IN_PROGRESS) {
                subTaskStatusInProgress.add(task.getStatus());
            } else if (task.getStatus() == Status.DONE) {
                subTaskStatusDone.add(task.getStatus());
            }
        }
        if (subTaskStatusNew.isEmpty() && subTaskStatusInProgress.isEmpty() && !subTaskStatusDone.isEmpty()) {
            epicStatus = Status.DONE;
        } else if (!subTaskStatusInProgress.isEmpty() || !subTaskStatusDone.isEmpty()) {
            epicStatus = Status.IN_PROGRESS;
        }
        return epicStatus;
    }

}