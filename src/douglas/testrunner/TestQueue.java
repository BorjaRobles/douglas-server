package douglas.testrunner;

import java.util.LinkedList;
import java.util.List;

public class TestQueue {

    // Specifying all the methods as 'synchronized' to cope
    // with concurrency memory issues
    private List<Queueable> items = new LinkedList<>();

    public synchronized boolean isEmpty() {
        return items.isEmpty();
    }

    public synchronized Queueable nextItem() {
        Queueable currentItem = items.get(0);
        items.remove(0);
        return currentItem;
    }

    public synchronized void addAll(List<? extends Queueable> newItems) {
        items.addAll(newItems);
    }

    public synchronized void add(Queueable newItem) {
        items.add(newItem);
    }

}
