package ca.cmpt213.as5.model;

/**
 * Interface for observers to implement to be able to observe changes to courses.
 */
public interface Observer {
    void courseChanged(CourseOffering courseOffering);
}