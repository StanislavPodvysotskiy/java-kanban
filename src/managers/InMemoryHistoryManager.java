package managers;
import tasks.Task;
import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private final LinkedList<Task> tasksHistory = new LinkedList<>();
    private final Map<Integer, Node<Task>> nodeMap = new HashMap<>();

    @Override
    public void add(Task task) {
        if (nodeMap.containsKey(task.getId())) {
            removeNode(nodeMap.get(task.getId()));
            nodeMap.remove(task.getId());
        }
        linkLast(task);
    }

    @Override
    public List<Task> getHistory() {
        if (tasksHistory.isEmpty()) {
            System.out.println("Вы пока не просматривали задачи");
            return null;
        }
        return getTasks();
    }

    @Override
    public void remove(int id) {
        removeNode(nodeMap.get(id));
        nodeMap.remove(id);
    }

    private Node<Task> head;
    private Node<Task> tail;

    public void linkLast(Task task) {
        final Node<Task> oldTail = tail;
        final Node<Task> newNode = new Node<>(oldTail, task, null);
        tail = newNode;
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.next = newNode;
        }
        tasksHistory.add(task);
        nodeMap.put(task.getId(), newNode);
    }

    public List<Task> getTasks() {
        return new ArrayList<>(tasksHistory);
    }

    public void removeNode(Node<Task> node) {
        if (node.prev != null) {
            Node<Task> prevNode = node.prev;
            prevNode.next = node.next;
        }
        if (node.next != null) {
            Node<Task> nextNode = node.next;
            nextNode.prev = node.prev;
        }
        tasksHistory.remove(node.data);
    }

    private static class Node<E> {
        private E data;
        private Node<E> next;
        private Node<E> prev;

        public Node(Node<E> prev, E data, Node<E> next) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }

}


