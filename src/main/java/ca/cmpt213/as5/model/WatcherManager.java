package ca.cmpt213.as5.model;

import java.util.*;

/**
 * WatcherManager manages a collection of watchers.
 */
public class WatcherManager {
    private long nextWatcherId = 0;
    private SortedMap<Long, Watcher> watcherIdMap = new TreeMap<>();

    public WatcherManager() {
    }

    public List<Watcher> getWatcherList() {
        return new ArrayList<>(watcherIdMap.values());
    }

    public Watcher getLatestWatcher() {
        return watcherIdMap.get(watcherIdMap.lastKey());
    }

    public List<String> getEventsByWatcherId(long watcherId) {
        return watcherIdMap.get(watcherId).getEvents();
    }

    public void addWatcher(long deptId, long courseId, CourseCatalog catalog) {
        Department dept = catalog.getDepartmentIdMap().get(deptId);
        Course course = dept.getCourseIdMap().get(courseId);
        Watcher newWatcher = new Watcher(nextWatcherId, dept, course);
        watcherIdMap.put(nextWatcherId, newWatcher);
        nextWatcherId++;
    }

    public void deleteWatcher(long watcherId) {
        watcherIdMap.remove(watcherId);
    }
}
