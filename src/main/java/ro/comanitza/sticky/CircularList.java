package ro.comanitza.sticky;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Circular list implementation
 *
 * Please not this class is not thread safe
 *
 * @author stefan.comanita
 * @param <T>
 */
public class CircularList<T> {

    private final int capacity;
    private final List<T> list;

    private int index = 0;

    /**
     * track if we passed the capacity/warped arround the list
     */
    private boolean passesCapacity;

    public CircularList() {
        this(20);
    }

    public CircularList(final int capacity) {
        this.capacity = capacity;

        list = new ArrayList<>(capacity);
    }

    public void add(final T value) {

        if (index < capacity) {

            /*
             * if we passed the capacity we should use set
             */
            if (passesCapacity) {
                list.set(index++, value);
            } else {
                list.add(index++, value);
            }

            return;
        }

        /*
         * mark that we passed the capacity, this will always remain true after the first pass
         */
        passesCapacity = true;
        index = 0;
        list.set(index++, value);
    }

    public List<T> fetchList() {

        return new ArrayList<>(list);
    }

    public void clear() {

        list.clear();
        index = 0;
    }

    public int getCapacity() {
        return capacity;
    }
}
