package ca.cmpt213.as5.controllers;

import ca.cmpt213.as5.model.*;
import ca.cmpt213.as5.model.Observer;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * REST API Controller for CourseCatalog.
 * Responsible for routing the endpoints and responding to REST request.
 */
@RestController
public class CoursePlannerController {
    WatcherManager watcherManager = new WatcherManager();
    CourseCatalog catalog = new CourseCatalog(watcherManager);

    @GetMapping("/api/dump-model")
    public void dumpModel() {
        catalog.dumpModel();
    }

    @GetMapping("/api/about")
    public Map<String, String> about() {
        Map<String, String> about = new HashMap<>();
        about.put("appName", "Course Planner");
        about.put("authorName", "Phuong Tran and Cheng Yan");
        return about;
    }

    @GetMapping("/api/departments")
    public List<Department> getDepartments() {
        return catalog.getDepartments();
    }

    @GetMapping ("/api/departments/{deptId}/courses")
    public List<Course> getCourses(@PathVariable long deptId) {
        return catalog.getCoursesByDeptId(deptId);
    }

    @GetMapping("/api/departments/{deptId}/courses/{courseId}/offerings")
    public List<CourseOffering> getOfferings(
            @PathVariable("deptId") long deptId,
            @PathVariable("courseId") long courseId
    ) {
        return catalog.getCourseOfferingsByIds(deptId, courseId);
    }

    @GetMapping("/api/departments/{deptId}/courses/{courseId}/offerings/{offeringId}")
    public List<OfferingSection> getOfferingSections (
            @PathVariable("deptId") long deptId,
            @PathVariable("courseId") long courseId,
            @PathVariable("offeringId") long offeringId
    ) {
        return catalog.getOfferingSectionsByIds(deptId, courseId, offeringId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/addoffering")
    public OfferingData addOffering(@RequestBody OfferingData data) {
        catalog.inputOfferingDataToModel(data);
        // TODO: return just OfferingSection data instead of OfferingData
        return data;
    }

    @GetMapping("/api/watchers")
    public List<Watcher> getWatchers() {
        return watcherManager.getWatcherList();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/watchers")
    public Watcher addWatcher(@RequestBody Map<String, String> request) {
        long deptId = Long.parseLong(request.get("deptId"));
        long courseId = Long.parseLong(request.get("courseId"));
        watcherManager.addWatcher(deptId, courseId, catalog);
        return watcherManager.getLatestWatcher();
    }

    @GetMapping("/api/watchers/{watcherId}")
    public List<String> getEvents(@PathVariable("watcherId") long watcherId) {
        return watcherManager.getEventsByWatcherId(watcherId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/api/watchers/{watcherId}")
    public void deleteWatcher(@PathVariable("watcherId") long watcherId) {
        watcherManager.deleteWatcher(watcherId);
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Request ID not found.")
    @ExceptionHandler(NullPointerException.class)
    public void badIdExceptionHandler() {
    }
}
