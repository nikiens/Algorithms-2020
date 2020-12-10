package lesson4;

import java.util.*;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Префиксное дерево для строк
 */
public class Trie extends AbstractSet<String> implements Set<String> {

    private static class Node {
        Map<Character, Node> children = new LinkedHashMap<>();

        private boolean formsString() {
            return children.containsKey((char) 0);
        }
    }

    private final Node root = new Node();

    private int size = 0;

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        root.children.clear();
        size = 0;
    }

    private String withZero(String initial) {
        return initial + (char) 0;
    }

    @Nullable
    private Node findNode(String element) {
        Node current = root;
        for (char character : element.toCharArray()) {
            if (current == null) return null;
            current = current.children.get(character);
        }
        return current;
    }

    @Override
    public boolean contains(Object o) {
        String element = (String) o;
        return findNode(withZero(element)) != null;
    }

    @Override
    public boolean add(String element) {
        Node current = root;
        boolean modified = false;
        for (char character : withZero(element).toCharArray()) {
            Node child = current.children.get(character);
            if (child != null) {
                current = child;
            } else {
                modified = true;
                Node newChild = new Node();
                current.children.put(character, newChild);
                current = newChild;
            }
        }
        if (modified) {
            size++;
        }
        return modified;
    }

    @Override
    public boolean remove(Object o) {
        String element = (String) o;
        Node current = findNode(element);
        if (current == null) return false;
        if (current.children.remove((char) 0) != null) {
            size--;
            return true;
        }
        return false;
    }

    /**
     * Итератор для префиксного дерева
     * <p>
     * Спецификация: {@link Iterator} (Ctrl+Click по Iterator)
     * <p>
     * Сложная
     */
    @NotNull
    @Override
    public Iterator<String> iterator() {
        return new TrieIterator();
    }

    private final class TrieIterator implements Iterator<String> {
        private final Deque<Iterator<Map.Entry<Character, Node>>> nodes = new ArrayDeque<>();
        private final StringBuilder buf = new StringBuilder();

        private String nextString;
        private Node current;

        private TrieIterator() {
            nodes.push(root.children.entrySet().iterator());
        }

        private boolean advanceToNextString() {
            while (!nodes.isEmpty()) {
                buf.setLength(nodes.size() - 1);

                while (nodes.getFirst().hasNext()) {
                    Map.Entry<Character, Node> next = nodes.getFirst().next();
                    if (next.getKey().equals('\0')) {
                        continue;
                    }

                    buf.append(next.getKey());

                    if (!next.getValue().formsString()) {
                        nodes.push(next.getValue().children.entrySet().iterator());
                    } else {
                        nextString = buf.toString();

                        if (next.getValue().children.size() > 1) {
                            nodes.push(next.getValue().children.entrySet().iterator());
                        }
                        current = next.getValue();
                        return true;
                    }
                }
                nodes.removeFirst();
            }
            return false;
        }

        // Time - O(1) - best; O()
        @Override
        public boolean hasNext() {
            return nextString != null || advanceToNextString();
        }

        // Time - O(1) - best; O()
        @Override
        public String next() {
            if (!hasNext()) throw new NoSuchElementException();

            String s = nextString;
            nextString = null;
            return s;
        }

        // Time - O(1) - best; O(1) - worst
        @Override
        public void remove() {
            if (current == null) throw new IllegalStateException();

            /*
              Тут, конечно, правильнее было бы удалять через итератор,
              но тогда пришлось бы дополнительно итерироваться по детям
              пока не попадем в элемент \0, ибо нет никакой гарантии,
              что позиция итератора находится в нужном месте.
             */
            current.children.remove('\0');
            size--;

            // Подмена итератора, дабы не вызвать ConcurrentModificationException
            if (!current.children.isEmpty()) {
                nodes.removeFirst();
                nodes.push(current.children.entrySet().iterator());
            }
            current = null;
        }
    }
}