package ca.cmpt213.as5.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.*;

/**
 * Department stores department name and a list of courses sorted by catalog number.
 */
public class Department {
    private long deptId;
    private String name;
    static private long nextCourseId = 0;
    private Map<Long, Course> courseIdMap = new HashMap<>(); // courseID -> Course.
    private SortedMap<String, Course> courseTreeMap = new TreeMap<>(); // course.catalogNumber -> Course.

    public Department(String name) {
        this.deptId = Utilities.getIdPlaceholder();
        this.name = name;
    }

    public long getDeptId() {
        return deptId;
    }

    public String getName() {
        return name;
    }

    @JsonIgnore
    public Map<Long, Course> getCourseIdMap() {
        return courseIdMap;
    }

    @JsonIgnore
    public List<Course> getCourses() {
        return new ArrayList<>(courseTreeMap.values());
    }

    public void setDeptId(long deptId) {
        this.deptId = deptId;
    }

    public void addCourse(Course newCourse, CourseOffering newCourseOffering) {
        Course course;
        if (courseTreeMap.containsKey(newCourse.getCatalogNumber())) {
            course = courseTreeMap.get(newCourse.getCatalogNumber());
        } else {
            course = newCourse;
            newCourse.setCourseId(nextCourseId);
            courseIdMap.put(nextCourseId, newCourse);
            courseTreeMap.put(newCourse.getCatalogNumber(), newCourse);
            nextCourseId++;
        }

        course.addOffering(newCourseOffering);
    }

    @Override
    public String toString() {
        return "Department{" +
                "deptId=" + deptId +
                ", name='" + name + '\'' +
                '}';
    }
}
