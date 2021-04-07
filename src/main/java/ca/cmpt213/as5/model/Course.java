package ca.cmpt213.as5.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.*;

/**
 * Course class stores and aggregates information of course offerings.
 * A Course object is uniquely identified by the course number, also called catalog number.
 */
public class Course {
    private long courseId;
    private String catalogNumber;
    private static long nextOfferingId = 0;
    private Map<Long, CourseOffering> offeringIdMap = new HashMap<>(); // offeringId -> CourseOffering.
    private SortedMap<Semester, List<CourseOffering>> semesterOfferingMap = new TreeMap<>(Comparator.
            comparing(Semester::getSemesterCode)); // Semester -> (CourseOffering[]).

    public Course(String catalogNumber) {
        this.courseId = Utilities.getIdPlaceholder();
        this.catalogNumber = catalogNumber;
    }

    public String getCatalogNumber() {
        return catalogNumber;
    }

    public long getCourseId() {
        return courseId;
    }

    @JsonIgnore
    public List<CourseOffering> getCourseOfferings() {
        List<CourseOffering> offerings = new ArrayList<>();
        for (List<CourseOffering> offeringList : semesterOfferingMap.values()) {
            offerings.addAll(offeringList);
        }
        return offerings;
    }

    @JsonIgnore
    public SortedMap<Semester, List<CourseOffering>> getSemesterOfferingMap() {
        return semesterOfferingMap;
    }

    @JsonIgnore
    public Map<Long, CourseOffering> getOfferingIdMap() {
        return offeringIdMap;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    public void addOffering(CourseOffering newCourseOffering) {
        Semester newSemester = newCourseOffering.getSemester();
        if (semesterOfferingMap.containsKey(newSemester)) {
            List<CourseOffering> offeringList = semesterOfferingMap.get(newSemester);
            addLocationOffering(newCourseOffering, offeringList);
        } else {
            addNewSemesterOffering(newSemester, newCourseOffering);
        }
    }

    private void addLocationOffering(CourseOffering newCourseOffering, List<CourseOffering> offeringList) {
        for (CourseOffering offering : offeringList) {
            if (offering.getLocation().equals(newCourseOffering.getLocation())) {
                offering.addInstructors(newCourseOffering.getInstructorSet());
                OfferingSection newSection = newCourseOffering.getOfferingSections().get(0);
                addOfferingSection(newSection, offering);
                return;
            }
        }

        addNewLocationOffering(newCourseOffering, offeringList);
    }

    private void addOfferingSection(OfferingSection newSection, CourseOffering offering) {
        for (OfferingSection section : offering.getOfferingSections()) {
            if (section.getType().equals(newSection.getType())) {
                section.addEnrollmentCap(newSection.getEnrollmentCap());
                section.addEnrollmentTotal(newSection.getEnrollmentTotal());
                return;
            }
        }

        offering.addNewOfferingSection(newSection);
    }

    private void addNewLocationOffering(CourseOffering newCourseOffering, List<CourseOffering> offeringList) {
        newCourseOffering.setCourseOfferingId(nextOfferingId);
        offeringIdMap.put(nextOfferingId, newCourseOffering);
        offeringList.add(newCourseOffering);
        offeringList.sort(Comparator.comparing(CourseOffering::getLocation));
        nextOfferingId++;
    }

    private void addNewSemesterOffering(Semester newSemester, CourseOffering newCourseOffering) {
        newCourseOffering.setCourseOfferingId(nextOfferingId);
        offeringIdMap.put(nextOfferingId, newCourseOffering);
        List<CourseOffering> courseOfferingList = new ArrayList<>();
        courseOfferingList.add(newCourseOffering);
        semesterOfferingMap.put(newSemester, courseOfferingList);
        nextOfferingId++;
    }

    @Override
    public String toString() {
        return "Course{" +
                "courseId=" + courseId +
                ", catalogNumber='" + catalogNumber + '\'' +
                '}';
    }
}
