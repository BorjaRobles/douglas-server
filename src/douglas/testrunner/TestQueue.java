package douglas.testrunner;

import douglas.domain.Test;

import java.util.ArrayList;
import java.util.List;

public class TestQueue {

    private static List<Queueable> items = new ArrayList<>();

    public static boolean isEmpty() {
        return items.isEmpty();
    }

    public static Queueable nextItem() {
        if(isEmpty()) return null;
        Queueable currentItem = items.get(0);
        items.remove(0);
        return currentItem;
    }

    public static void addAll(List<Test> newItems) {
        items.addAll(newItems);
    }

    public static void add(Queueable newItem) {
        items.add(newItem);
    }

}
