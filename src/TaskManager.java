import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private int lastId = 1;
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();

    //метод добавляет новую задачу в хэшмап
    public void addTask(Task task) {
        task.setId(lastId++);
        tasks.put(task.getId(), task);
        System.out.println("Задача создана");
    }

    //метод добавляет новую зпик задачу в хэшмап
    public void addEpicTask(Epic task) {
        task.setId(lastId++);
        epics.put(task.getId(), task);
        System.out.println("Задача создана");
    }

    //метод добавляет новую подзадачу в хэшмап
    public void addSubTask(Subtask task) {
        if (epics.containsKey(task.getEpicId())) {
            task.setId(lastId++);
            epics.get(task.getEpicId()).getSubtaskIds().add(task.getId());
            subtasks.put(task.getId(), task);
            System.out.println("Задача создана");
        } else {
            System.out.println("Эпик задачи с таким ID не найдено");
        }
    }

    //метод возвращает с названием и описанием задачи
    public String getByID(int id) {
        String nameAndDescription = "Такой задачи нет";
        if (tasks.containsKey(id)) {
            nameAndDescription = "Задача: " + tasks.get(id).getName() + " Описание: " +tasks.get(id).getDescription();
        } else if (epics.containsKey(id)) {
            nameAndDescription = "Задача: " + epics.get(id).getName() + " Описание: " + epics.get(id).getDescription();
        } else  if (subtasks.containsKey(id)) {
            nameAndDescription = "Задача: " + subtasks.get(id).getName() + " Описание: " + subtasks.get(id).getDescription();
        }
        return nameAndDescription;
    }

    //метод проходит циклами по всем хэшмапа, если в них есть объекты и отображает их
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
    public String getStatusById(int id) {
        String statusDescription = "Такой задачи нет";
        if (tasks.containsKey(id)) {
            statusDescription = tasks.get(id).getStatus();
        } else if (epics.containsKey(id)) {
            statusDescription = getEpicStatus(id);
        } else  if (subtasks.containsKey(id)) {
            statusDescription = subtasks.get(id).getStatus();
        }
        return statusDescription;
    }

    //метод меняет статус для обычных задач и подзадач
    public String setStatusById(int id, String status) {
        String newStatus = "Статус введен не верно";
        if (status.equals("IN_PROGRESS") || status.equals("DONE")) {
            if (tasks.containsKey(id)) {
                newStatus = "Новый статус - " + tasks.get(id).setStatus(status);
            } else if (epics.containsKey(id)) {
                newStatus = "Нельзя изменить статус для эпик задачи.";
            } else if (subtasks.containsKey(id)) {
                newStatus = "Новый статус - " + subtasks.get(id).setStatus(status);
            } else {
                newStatus = "Такой задачи нет";
            }
        }
        return newStatus;
    }

    //метод удаляет задачи
    public void removeTaskById(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
            System.out.println("Задача удалена");
            //при удалении эпик задачи удаляются все его подзадачи
        } else if (epics.containsKey(id)) {
            for (Integer subTasksId : epics.get(id).getSubtaskIds()) {
                subtasks.remove(subTasksId);
            }
            epics.remove(id);
            System.out.println("Задача удалена");
            //при удалении подзадачи удаляется запись о ней в списке эпик задачи
        } else if (subtasks.containsKey(id)) {
            for (int i = 0; i < epics.get(subtasks.get(id).getEpicId()).getSubtaskIds().size(); i++) {
                if (epics.get(subtasks.get(id).getEpicId()).getSubtaskIds().get(i) == id) {
                    epics.get(subtasks.get(id).getEpicId()).getSubtaskIds().remove(i);
                }
            }
            subtasks.remove(id);
            System.out.println("Задача удалена");
        } else {
            System.out.println("Такой задачи нет");
        }
    }

    //метод удаляет все задачи
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
    public void getEpicSubtasks(int id) {
        if (subtasks.isEmpty()) {
            System.out.println("Подзадач пока нет");
        } else {
            System.out.println("Поиск подзадач для эпик задачи с ID " + id);
            for (int num : epics.get(id).getSubtaskIds()) {
                if (epics.get(id).getSubtaskIds().isEmpty()) {
                    System.out.println("У данной эпик задачи пока нет подзадач");
                } else {
                    System.out.println("Найдена подзадача ID " + num);
                }
            }
        }
    }

    //метод рассчитывает статус эпик задачи
    public String getEpicStatus(int id) {
        String epicStatus = epics.get(id).getStatus();
        ArrayList<String> subTaskStatusNew = new ArrayList<>();
        ArrayList<String> subTaskStatusInProgress = new ArrayList<>();
        ArrayList<String> subTaskStatusDone = new ArrayList<>();
        for (int num : epics.get(id).getSubtaskIds()) {
            if (subtasks.get(num).getStatus().equals("New")) {
                subTaskStatusNew.add(subtasks.get(num).getStatus());
            } else if (subtasks.get(num).getStatus().equals("IN_PROGRESS")) {
                subTaskStatusInProgress.add(subtasks.get(num).getStatus());
            } else if (subtasks.get(num).getStatus().equals("DONE")) {
                subTaskStatusDone.add(subtasks.get(num).getStatus());
            }
        }
        if (subTaskStatusNew.isEmpty() && subTaskStatusInProgress.isEmpty()) {
            epicStatus = "DONE";
        } else if (!subTaskStatusInProgress.isEmpty() || !subTaskStatusDone.isEmpty()) {
            epicStatus = "IN_PROGRESS";
        }
        return epicStatus;
    }

}