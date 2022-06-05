import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        while(true) {
            Scanner scanner = new Scanner((System.in));
            System.out.println("Выберите действие");
            menu();
            String choice = scanner.nextLine();
            switch (choice) {
                case("1"):
                    System.out.println("Введите название задачи");
                    String nameOfTask = scanner.nextLine();
                    Task task = new Task(nameOfTask);
                    System.out.println("Введите описание задачи");
                    String tasKDescription = scanner.nextLine();
                    task.setDescription(tasKDescription);
                    taskManager.addNewTask(task);
                    break;
                case("2"):
                    System.out.println("Введите название задачи");
                    String nameOfEpicTask = scanner.nextLine();
                    EpicTask epicTask = new EpicTask(nameOfEpicTask);
                    System.out.println("Введите описание задачи");
                    String epicDescription = scanner.nextLine();
                    epicTask.setDescription(epicDescription);
                    taskManager.addNewEpicTask(epicTask);
                    break;
                case("3"):
                    System.out.println("Введите ID эпик задачи");
                    int epicsId = scanner.nextInt();
                    System.out.println("Введите название задачи");
                    String fix = scanner.nextLine(); //доп сканер лови \n от предыдущего сканера
                    String nameOfSubTask = scanner.nextLine();
                    SubTask subTask = new SubTask(nameOfSubTask, epicsId);
                    System.out.println("Введите описание задачи");
                    String subDescription = scanner.nextLine();
                    subTask.setDescription(subDescription);
                    taskManager.addNewSubTask(subTask);
                    break;
                case("4"):
                    System.out.println("Введите ID");
                    int idForTask = scanner.nextInt();
                    System.out.println(taskManager.getByID(idForTask));
                    break;
                case("5"):
                    System.out.println("Введите ID");
                    int idForEpicSubTasks = scanner.nextInt();
                    taskManager.getEpicSubTasks(idForEpicSubTasks);
                    break;
                case("6"):
                    taskManager.showAllTasks();
                    break;
                case("7"):
                    System.out.println("Введите ID");
                    int idForStatusShow = scanner.nextInt();
                    System.out.println(taskManager.getStatusById(idForStatusShow));
                    break;
                case("8"):
                    System.out.println("Введите ID");
                    int idForStatusChange = scanner.nextInt();
                    System.out.println("Укажите статус IN_PROGRESS или DONE");
                    String newStatus = scanner.next();
                    System.out.println(taskManager.setStatusById(idForStatusChange, newStatus));
                    break;
                case("9"):
                    System.out.println("Укажите ID");
                    int idForRemove = scanner.nextInt();
                    taskManager.removeTaskById(idForRemove);
                    System.out.println("Задача удалена");
                    break;
                case("10"):
                    taskManager.removeAllTasks();
                    System.out.println("Все задачи удалены");
                    break;
                case("0"):
                    System.out.println("Выход");
                    return;
                default:
                    System.out.println("Такой команды нет");
                    break;
            }
        }
    }

    public static void menu() {
        System.out.println("1. Создать новую задачу");
        System.out.println("2. Создать новую Эпик задачу");
        System.out.println("3. Создать новую подзадачу");
        System.out.println("4. Показать задачу");
        System.out.println("5. Показать подзадачи эпик задачи");
        System.out.println("6. Показать все задачи");
        System.out.println("7. Показать статус задачи");
        System.out.println("8. Изменить статус задачи");
        System.out.println("9. Удалить задачу");
        System.out.println("10. Удалить все задачи");
        System.out.println("0. Выход");
    }

}