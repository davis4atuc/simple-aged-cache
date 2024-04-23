package io.collective;

import java.time.Clock;
import java.util.ArrayList;
import java.util.List;

public class SimpleAgedCache {
    private Clock myClock;
    private List<ExpirableEntry> myList;

    public SimpleAgedCache(Clock clock) {
        this.myClock = clock;
        this.myList = new ArrayList<ExpirableEntry>();
    }

    public SimpleAgedCache() {
        this.myClock = Clock.systemUTC();
        this.myList = new ArrayList<ExpirableEntry>();
    }

    public void put(Object key, Object value, int retentionInMillis) {
        removeExpired();
        ExpirableEntry item = new ExpirableEntry(key, value, retentionInMillis, myClock);
        myList.add(item);
    }

    public boolean isEmpty() {
        removeExpired();
        return myList.size() == 0;
    }

    public int size() {
        removeExpired();
        return myList.size();
    }

    public Object get(Object key) {
        removeExpired();
        for (ExpirableEntry item : myList) {
            if (item.key == key)
                return item.value;
        }
        return null;
    }

    public void removeExpired() {
        myList.removeIf(b -> b.isExpired(myClock));
    }
}

class ExpirableEntry {

    Object key;
    Object value;
    long expireMillisecond;

    public ExpirableEntry(Object key, Object value, int retentionInMillis, Clock clock) {
        this.key = key;
        this.value = value;
        this.expireMillisecond = clock.millis() + retentionInMillis;
    }

    public boolean isExpired(Clock clock) {
        return clock.millis() > expireMillisecond;
    }
}