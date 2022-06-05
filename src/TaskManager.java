import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private int lastID = 1;
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, EpicTask> epicTasks = new HashMap<>();
    private HashMap<Integer, SubTask> subTasks = new HashMap<>();

    public void addNewTask(Task newTask) {  //метод добавляет новую задачу в хэшмап
        newTask.setId(lastID);
        tasks.put(lastID++, newTask);
        System.out.println("Задача создана");
    }

    public void addNewEpicTask(EpicTask newTask) {  //метод добавляет новую зэпик адачу в хэшмап
        newTask.setId(lastID);
        epicTasks.put(lastID++, newTask);
        System.out.println("Задача создана");
    }

    public void addNewSubTask(SubTask newTask) {  //метод добавляет новую подзадачу в хэшмап
        if (epicTasks.containsKey(newTask.getEpicsId())) {
            newTask.setId(lastID);
            epicTasks.get(newTask.getEpicsId()).getSubTasksIds().add(lastID);
            subTasks.put(lastID++, newTask);
            System.out.println("Задача создана");
        } else {
            System.out.println("Эпик задачи с таким ID не найдено");
        }
    }

    public String getByID(int id) { //метод возвращает с названием и описанием задачи
        String task = "Такой задачи нет";
        if (tasks.containsKey(id)) {
            task = "Задача: "+tasks.get(id).getNameOfTask()+" Описание: "+tasks.get(id).getDescription();
        } else if (epicTasks.containsKey(id)) {
            task = "Задача: "+epicTasks.get(id).getNameOfTask()+" Описание: "+epicTasks.get(id).getDescription();
        } else  if (subTasks.containsKey(id)) {
            task = "Задача: "+subTasks.get(id).getNameOfTask()+" Описание: "+subTasks.get(id).getDescription();
        }
        return task;
    }

    public void showAllTasks() { //метод проходит циклами по всем хэшмапа, если в них есть объекты и отображает их
        if (tasks.isEmpty() && epicTasks.isEmpty() && subTasks.isEmpty()) {
            System.out.println("Задач пока нет");
        } else {
            if (!(tasks.isEmpty())) {
                for (Integer task : tasks.keySet()) {
                    System.out.println("ID " + task + " Название " + tasks.get(task).getNameOfTask());
                }
            }
            if (!(epicTasks.isEmpty())) {
                for (Integer epicTask : epicTasks.keySet()) {
                    System.out.println("ID " + epicTask + " Название " + epicTasks.get(epicTask).getNameOfTask());
                }
            }
            if (!(subTasks.isEmpty())) {
                for (Integer subTask : subTasks.keySet()) {
                    System.out.println("ID " + subTask + " Название " + subTasks.get(subTask).getNameOfTask());
                }
            }
        }
    }

    public String getStatusById(int id) { //запрос статуса у задачи, для эпик задачи статус рассчитывается отдельно
        String status = "Такой задачи нет";
        if (tasks.containsKey(id)) {
            status = tasks.get(id).getStatus();
        } else if (epicTasks.containsKey(id)) {
            status = getEpicStatus(id);
        } else  if (subTasks.containsKey(id)) {
            status = subTasks.get(id).getStatus();
        }
        return status;
    }

    public String setStatusById(int id, String status) { //метод меняет статус для обычных задач и подзадач
        String newStatus = "Статус введен не верно";
        if (status.equals("IN_PROGRESS") || status.equals("DONE")) {
            if (tasks.containsKey(id)) {
                newStatus = "Новый статус - " + tasks.get(id).setStatus(status);
            } else if (epicTasks.containsKey(id)) {
                newStatus = "Нельзя изменить статус для эпик задачи.";
            } else if (subTasks.containsKey(id)) {
                newStatus = "Новый статус - " + subTasks.get(id).setStatus(status);
            } else {
                newStatus = "Такой задачи нет";
            }
        }
        return newStatus;
    }

    public void removeTaskById(int id) {  //метод удаляет задачи
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else if (epicTasks.containsKey(id)) {  //при удалении эпик задачи удаляются все его подзадачи
            for (Integer subTasksId : epicTasks.get(id).getSubTasksIds()) {
                subTasks.remove(subTasksId);
            }
            epicTasks.remove(id);
        } else if (subTasks.containsKey(id)) {  //при удалении подзадачи удаляется запись о ней в списке эпик задачи
            for (int i = 0; i < epicTasks.get(subTasks.get(id).getEpicsId()).getSubTasksIds().size(); i++) {
                if (epicTasks.get(subTasks.get(id).getEpicsId()).getSubTasksIds().get(i) == id) {
                    epicTasks.get(subTasks.get(id).getEpicsId()).getSubTasksIds().remove(i);
                }
            }
            subTasks.remove(id);
        } else {
            System.out.println("Такой задачи нет");
        }
    }

    public void removeAllTasks() { //метод удаляет все задачи
        tasks.clear();
        epicTasks.clear();
        subTasks.clear();
    }

    public void getEpicSubTasks(int id) { //метод выводит все подзадачи выбранной эпик задачи, если они есть
        if (subTasks.isEmpty()) {
            System.out.println("Подзадач пока нет");
        } else {
            System.out.println("Поиск подзадач для эпик задачи с ID " + id);
            for (int num : epicTasks.get(id).getSubTasksIds()) {
                if (epicTasks.get(id).getSubTasksIds().isEmpty()) {
                    System.out.println("У данной эпик задачи пока нет подзадач");
                } else {
                    System.out.println("Найдена подзадача ID " + num);
                }
            }
        }
    }

    public String getEpicStatus(int id) {  //метод рассчитывает статус эпик задачи
        String epicStatus = epicTasks.get(id).getStatus();
        ArrayList<String> subTaskStatusNew = new ArrayList<>();
        ArrayList<String> subTaskStatusInProgress = new ArrayList<>();
        ArrayList<String> subTaskStatusDone = new ArrayList<>();
        for (int num : epicTasks.get(id).getSubTasksIds()) {
            if (subTasks.get(num).getStatus().equals("New")) {
                subTaskStatusNew.add(subTasks.get(num).getStatus());
            } else if (subTasks.get(num).getStatus().equals("IN_PROGRESS")) {
                subTaskStatusInProgress.add(subTasks.get(num).getStatus());
            } else if (subTasks.get(num).getStatus().equals("DONE")) {
                subTaskStatusDone.add(subTasks.get(num).getStatus());
            }
        }
        if (subTaskStatusNew.isEmpty() && subTaskStatusInProgress.isEmpty()) {
            epicStatus = "DONE";
        } else if (!(subTaskStatusInProgress.isEmpty()) || !(subTaskStatusDone.isEmpty())) {
            epicStatus = "IN_PROGRESS";
        }
        return epicStatus;
    }

}