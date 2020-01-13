package pl.pkrysztofiak.gridview;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

public class Numbers implements ObservableList<Integer> {

    private final ObservableList<Integer> numbers = FXCollections.observableArrayList();

    @Override
    public void addListener(ListChangeListener<? super Integer> listener) {
        numbers.addListener(listener);
    }

    @Override
    public  void forEach(Consumer<? super Integer> action) {
        numbers.forEach(action);
    }

    @Override
    public void removeListener(ListChangeListener<? super Integer> listener) {
        numbers.removeListener(listener);
    }

    @Override
    public void addListener(InvalidationListener listener) {
        numbers.addListener(listener);
    }

    @Override
    public boolean addAll(Integer... elements) {
        return numbers.addAll(elements);
    }

    @Override
    public boolean setAll(Integer... elements) {
        return numbers.setAll(elements);
    }

    @Override
    public boolean setAll(Collection<? extends Integer> col) {
        return numbers.setAll(col);
    }

    @Override
    public boolean removeAll(Integer... elements) {
        return numbers.removeAll(elements);
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        numbers.removeListener(listener);
    }

    @Override
    public boolean retainAll(Integer... elements) {
        return numbers.retainAll(elements);
    }

    @Override
    public void remove(int from, int to) {
        numbers.remove(from, to);
    }

    @Override
    public  FilteredList<Integer> filtered(Predicate<Integer> predicate) {
        return numbers.filtered(predicate);
    }

    @Override
    public  SortedList<Integer> sorted(Comparator<Integer> comparator) {
        return numbers.sorted(comparator);
    }

    @Override
    public  SortedList<Integer> sorted() {
        return numbers.sorted();
    }

    @Override
    public int size() {
        return numbers.size();
    }

    @Override
    public boolean isEmpty() {
        return numbers.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return numbers.contains(o);
    }

    @Override
    public Iterator<Integer> iterator() {
        return numbers.iterator();
    }

    @Override
    public Object[] toArray() {
        return numbers.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return numbers.toArray(a);
    }

    @Override
    public boolean add(Integer e) {
        return numbers.add(e);
    }

    @Override
    public boolean remove(Object o) {
        return numbers.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return numbers.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends Integer> c) {
        return numbers.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends Integer> c) {
        return numbers.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return numbers.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return numbers.retainAll(c);
    }

    @Override
    public  void replaceAll(UnaryOperator<Integer> operator) {
        numbers.replaceAll(operator);
    }

    @Override
    public  boolean removeIf(Predicate<? super Integer> filter) {
        return numbers.removeIf(filter);
    }

    @Override
    public  void sort(Comparator<? super Integer> c) {
        numbers.sort(c);
    }

    @Override
    public void clear() {
        numbers.clear();
    }

    @Override
    public boolean equals(Object o) {
        return numbers.equals(o);
    }

    @Override
    public int hashCode() {
        return numbers.hashCode();
    }

    @Override
    public Integer get(int index) {
        return numbers.get(index);
    }

    @Override
    public Integer set(int index, Integer element) {
        return numbers.set(index, element);
    }

    @Override
    public void add(int index, Integer element) {
        numbers.add(index, element);
    }

    @Override
    public  Stream<Integer> stream() {
        return numbers.stream();
    }

    @Override
    public Integer remove(int index) {
        return numbers.remove(index);
    }

    @Override
    public  Stream<Integer> parallelStream() {
        return numbers.parallelStream();
    }

    @Override
    public int indexOf(Object o) {
        return numbers.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return numbers.lastIndexOf(o);
    }

    @Override
    public ListIterator<Integer> listIterator() {
        return numbers.listIterator();
    }

    @Override
    public ListIterator<Integer> listIterator(int index) {
        return numbers.listIterator(index);
    }

    @Override
    public List<Integer> subList(int fromIndex, int toIndex) {
        return numbers.subList(fromIndex, toIndex);
    }

    @Override
    public  Spliterator<Integer> spliterator() {
        return numbers.spliterator();
    }
}
