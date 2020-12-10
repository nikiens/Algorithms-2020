package lesson5;

import org.jetbrains.annotations.NotNull;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class OpenAddressingSet<T> extends AbstractSet<T> {

    private final int bits;

    private final int capacity;

    private final Object[] storage;

    private int size = 0;

    private final Object DELETED = new Object();

    private int startingIndex(Object element) {
        return element.hashCode() & (0x7FFFFFFF >> (31 - bits));
    }

    public OpenAddressingSet(int bits) {
        if (bits < 2 || bits > 31) {
            throw new IllegalArgumentException();
        }
        this.bits = bits;
        capacity = 1 << bits;
        System.out.println(capacity);
        storage = new Object[capacity];
    }

    @Override
    public int size() {
        return size;
    }

    /**
     * Проверка, входит ли данный элемент в таблицу
     */
    @Override
    public boolean contains(Object o) {
        int index = startingIndex(o);
        Object current = storage[index];
        while (current != null) {
            if (current.equals(o)) {
                return true;
            }
            index = (index + 1) % capacity;
            current = storage[index];
        }
        return false;
    }

    /**
     * Добавление элемента в таблицу.
     *
     * Не делает ничего и возвращает false, если такой же элемент уже есть в таблице.
     * В противном случае вставляет элемент в таблицу и возвращает true.
     *
     * Бросает исключение (IllegalStateException) в случае переполнения таблицы.
     * Обычно Set не предполагает ограничения на размер и подобных контрактов,
     * но в данном случае это было введено для упрощения кода.
     */
    @Override
    public boolean add(T t) {
        int startingIndex = startingIndex(t);
        int index = startingIndex;
        Object current = storage[index];
        while (current != null && current != DELETED) {
            if (current.equals(t)) {
                return false;
            }
            index = (index + 1) % capacity;
            if (index == startingIndex) {
                throw new IllegalStateException("Table is full");
            }
            current = storage[index];
        }
        storage[index] = t;
        size++;
        return true;
    }

    /**
     * Удаление элемента из таблицы
     *
     * Если элемент есть в таблица, функция удаляет его из дерева и возвращает true.
     * В ином случае функция оставляет множество нетронутым и возвращает false.
     * Высота дерева не должна увеличиться в результате удаления.
     *
     * Спецификация: {@link Set#remove(Object)} (Ctrl+Click по remove)
     *
     * Средняя
     */

    // Time - O(1) - best, O(n) - worst
    @Override
    public boolean remove(Object o) {
        int idx = startingIndex(o);
        Object current = storage[idx];

        while (current != null) {
            if (current.equals(o)) {
                storage[idx] = DELETED;
                size--;
                return true;
            }
            idx = (idx + 1) % capacity;
            current = storage[idx];
        }
        return false;
    }

    /**
     * Создание итератора для обхода таблицы
     *
     * Не забываем, что итератор должен поддерживать функции next(), hasNext(),
     * и опционально функцию remove()
     *
     * Спецификация: {@link Iterator} (Ctrl+Click по Iterator)
     *
     * Средняя (сложная, если поддержан и remove тоже)
     */
    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new OpenAddressingSetIterator();
    }

    private class OpenAddressingSetIterator implements Iterator<T> {
        private int currentElementIndex;
        private Object current;
        private Object next;
        private int idx;

        private boolean advanceToNextElement() {
            if (currentElementIndex == size()) {
                return false;
            }

            while ((current = storage[idx]) == null || current == DELETED) {
                idx++;
            }

            next = storage[idx];
            idx++;
            currentElementIndex++;
            return true;
        }


        // Time - O(1) - Best, O(n) - Worst
        @Override
        public boolean hasNext() {
            return next != null || advanceToNextElement();
        }

        // Time - O(1) - Best, O(n) - Worst
        @SuppressWarnings("unchecked")
        @Override
        public T next() {
            if (!hasNext()) throw new NoSuchElementException();

            T n = (T) next;
            next = null;

            return n;
        }

        // Time - O(1) - Best/Worst
        @Override
        public void remove() {
            if (current == null) throw new IllegalStateException();

            storage[idx - 1] = DELETED;
            currentElementIndex--;
            size--;

            current = null;
        }
    }
}
