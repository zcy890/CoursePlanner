package ca.cmpt213.as5.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.*;

/**
 * CourseOffering stores the information of an offering, uniquely identified by the semester and location name
 * (e.g. "BURNABY", "HRBRCNTR", "SURREY" .etc).
 */
public class CourseOffering {
    private long courseOfferingId;
    private String location;
    private String instructors;
    private String term;
    private long semesterCode;
    private int year;
    private Semester semester;
    private SortedSet<String> instructorSet;
    private List<OfferingSection> offeringSections;

    public CourseOffering(Semester semester, String location, SortedSet<String> instructorSet, List<OfferingSection> offeringSections)
    {
        this.courseOfferingId = Utilities.getIdPlaceholder();
        this.semester = semester;
        this.term = semester.getTerm();
        this.semesterCode = semester.getSemesterCode();
        this.year = semester.getYear();
        this.location = location;
        this.instructorSet = instructorSet;
        this.offeringSections = offeringSections;
    }

    public long getCourseOfferingId() {
        return courseOfferingId;
    }

    public String getInstructors() {
        StringJoiner joiner = new StringJoiner(", ");
        for (String instructor : instructorSet) {
            joiner.add(instructor);
        }
        return joiner.toString();
    }

    public String getTerm() {
        return term;
    }

    public long getSemesterCode() {
        return semesterCode;
    }

    public int getYear() {
        return year;
    }

    public String getLocation() {
        return location;
    }

    @JsonIgnore
    public Semester getSemester() {
        return semester;
    }

    @JsonIgnore
    public SortedSet<String> getInstructorSet() {
        return instructorSet;
    }

    @JsonIgnore
    public List<OfferingSection> getOfferingSections() {
        return offeringSections;
    }

    public void setCourseOfferingId(long id) {
        this.courseOfferingId = id;
    }

    public void addInstructors(Set<String> newInstructors) {
        this.instructorSet.addAll(newInstructors);
    }

    public void addNewOfferingSection(OfferingSection section) {
        offeringSections.add(section);
        offeringSections.sort(Comparator.comparing(OfferingSection::getType));
    }

    @Override
    public String toString() {
        return "CourseOffering{" +
                "courseOfferingId=" + courseOfferingId +
                ", location='" + location + '\'' +
                ", instructors='" + instructors + '\'' +
                ", semester=" + semester +
                '}';
    }
}
