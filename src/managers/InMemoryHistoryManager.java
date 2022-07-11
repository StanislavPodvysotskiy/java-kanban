package managers;
import tasks.Task;

import java.util.*;

class InMemoryHistoryManager implements HistoryManager {

    private static final CustomLinkedList<Task> TASKS_HISTORY = new CustomLinkedList<>();
    private static final Map<Integer, Node> IDS_AND_NODES = new HashMap<>();
    private static Integer idForMap;

    @Override
    public void add(Task task) {
        if (IDS_AND_NODES.containsKey(task.getId())) {
            TASKS_HISTORY.removeNode(IDS_AND_NODES.get(task.getId()));
            IDS_AND_NODES.remove(task.getId());
        }
        idForMap = task.getId();
        TASKS_HISTORY.linkLast(task);
    }

    @Override
    public List<Task> getHistory() {
        if (TASKS_HISTORY.isEmpty()) {
            System.out.println("Вы пока не просматривали задачи");
            return null;
        }
        return TASKS_HISTORY.getTasks();
    }
    @Override
    public void remove(int id) {
        TASKS_HISTORY.removeNode(IDS_AND_NODES.get(id));
        IDS_AND_NODES.remove(id);
    }

    public static class CustomLinkedList<T> extends LinkedList{

        private Node<T> head;

        private Node<T> tail;

        public void linkLast(T element) {
            final Node<T> oldTail = tail;
            final Node<T> newNode = new Node<>(oldTail, element, null);
            tail = newNode;
            if (oldTail == null)
                head = newNode;
            else
                oldTail.next = newNode;
            TASKS_HISTORY.add(newNode);
            IDS_AND_NODES.put(idForMap, newNode);
        }

        public List<Task> getTasks() {
            return new ArrayList<Task>(TASKS_HISTORY);
        }

        public void removeNode(Node node) {
            TASKS_HISTORY.remove(node);
        }
    }
}


