package ca.cmpt213.as5.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Watcher has course data and implements Observer interface to provide a method when there is a course change.
 */
public class Watcher implements Observer {
    private long id;
    private Department department;
    private Course course;
    private List<String> events = new ArrayList<>();

    public Watcher(long id, Department department, Course course) {
        this.id = id;
        this.department = department;
        this.course = course;
    }

    public long getId() {
        return id;
    }

    public Department getDepartment() {
        return department;
    }

    public Course getCourse() {
        return course;
    }

    public List<String> getEvents() {
        return events;
    }

    @Override
    public void courseChanged(CourseOffering offering) {
        OfferingSection section = offering.getOfferingSections().get(0);
        String event = new Date() + ": Added section " + section.getType() + " with enrollment (" +
                section.getEnrollmentTotal() + " / " + section.getEnrollmentCap() + ") to offering " +
                offering.getTerm() + " " + offering.getYear();
        events.add(event);
    }

    @Override
    public String toString() {
        return "Watcher{" +
                "id=" + id +
                ", department=" + department +
                ", course=" + course +
                ", events=" + events +
                '}';
    }
}
