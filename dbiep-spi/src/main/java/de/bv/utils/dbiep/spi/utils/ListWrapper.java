package de.bv.utils.dbiep.spi.utils;

import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ListWrapper: A wrapper class for the {@link List} interface
 *
 * <p>The {@code List} interface does not provide a fluent interface which heavily influence the readability of
 * source code, e.g. when adding elements to a list.
 * This wrapper class provides methods with a fluent interface for wrapping and unwrapping lists while providing a
 * fluent interface.</p>
 *
 * @param <E> element type of the list
 *
 * @version 2022-01
 * @since 2022-01
 * @author Bernhard Volz
 */
public class ListWrapper<E> {

    private final List<E> wrappedList;

    protected ListWrapper(List<E> list) {
        this.wrappedList = list;
    }

    public static <E> ListWrapper<E> wrap(List<E> list) {
        return new ListWrapper<>(list);
    }

    public static <E> ListWrapper<E> newArrayList() {
        return new ListWrapper<>(Lists.newArrayList());
    }

    @SafeVarargs
    public static <E> ListWrapper<E> newArrayList(E... elements) {
        return new ListWrapper<>(Lists.newArrayList(elements));
    }

    public List<E> asList() {
        return this.wrappedList;
    }

    public ListWrapper<E> clear() {
        this.wrappedList.clear();
        return this;
    }

    public ListWrapper<E> add(E element) {
        this.wrappedList.add(element);
        return this;
    }

    public E[] toArray(E[] array) {
        return this.wrappedList.toArray(array);
    }

    public String[] toStringArray() {
        return Arrays.stream(this.toArray())
                .sequential()
                .map(Object::toString)
                .toArray(String[]::new);
    }

    public Object[] toArray() {
        return this.wrappedList.toArray();
    }
}
