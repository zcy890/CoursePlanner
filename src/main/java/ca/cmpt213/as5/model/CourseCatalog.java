package ca.cmpt213.as5.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

/**
 * CourseCatalog class provides the encompassing model to store and search courses.
 */
public class CourseCatalog {
//    private static final String DATA_PATH = "data/small_data.csv";
    private static final String DATA_PATH = "data/course_data_2018.csv";
    private WatcherManager watcherManager;
    private Map<Long, Department> departmentIdMap = new HashMap<>();
    private SortedMap<String, Department> departmentMap = new TreeMap<>();
    private static long nextDeptId = 0;

    public CourseCatalog(WatcherManager watcherManager) {
        this.watcherManager = watcherManager;
        parseCSVFile();
    }

    public void addCourseOffering(String deptName, Course course, CourseOffering courseOffering) {
        Department dept;
        if (departmentMap.containsKey(deptName)) {
            dept = departmentMap.get(deptName);
        } else {
            dept = new Department(deptName);
            dept.setDeptId(nextDeptId);
            departmentIdMap.put(nextDeptId, dept);
            departmentMap.put(deptName, dept);
            nextDeptId++;
        }

        dept.addCourse(course, courseOffering);
        notifyWatchers(dept, course, courseOffering);
    }

    private void notifyWatchers(Department dept, Course course, CourseOffering offering) {
        for (Watcher watcher : watcherManager.getWatcherList()) {
            if (watcher.getDepartment().getDeptId() == dept.getDeptId()
                    && watcher.getCourse().getCatalogNumber().equals(course.getCatalogNumber()))
            {
                watcher.courseChanged(offering);
            }
        }
    }

    public void dumpModel() {
        for (Department dept : departmentMap.values()) {
            for (Course course : dept.getCourses()) {
                System.out.println(dept.getName() + " " + course.getCatalogNumber());
                printOfferings(course);
            }
        }
    }

    public List<Department> getDepartments() {
        return new ArrayList<>(departmentMap.values());
    }

    @JsonIgnore public Map<Long, Department> getDepartmentIdMap() {
        return departmentIdMap;
    }

    public List<Course> getCoursesByDeptId(long deptId) {
        Department dept = departmentIdMap.get(deptId);
        return dept.getCourses();
    }

    public List<CourseOffering> getCourseOfferingsByIds(long deptId, long courseId) {
        Department dept = departmentIdMap.get(deptId);
        Course course = dept.getCourseIdMap().get(courseId);
        return course.getCourseOfferings();
    }

    public List<OfferingSection> getOfferingSectionsByIds(long deptId, long courseId, long offeringId) {
        Department dept = departmentIdMap.get(deptId);
        Course course = dept.getCourseIdMap().get(courseId);
        CourseOffering offering = course.getOfferingIdMap().get(offeringId);
        return offering.getOfferingSections();
    }

    private void printOfferings(Course course) {
        for (Map.Entry<Semester, List<CourseOffering>> semesterOfferingMap : course.getSemesterOfferingMap().entrySet()) {
            for (CourseOffering offering : semesterOfferingMap.getValue()) {
                // Semester code, location, and instructors.
                System.out.println("\t" + semesterOfferingMap.getKey().getSemesterCode() + " in " + offering.getLocation() +
                        " by " + offering.getInstructors());
                // OfferingSections.
                for (OfferingSection section : offering.getOfferingSections()) {
                    System.out.println("\t\tTYPE=" + section.getType() + ", "
                            + "Enrollment=" + section.getEnrollmentTotal() + "/" + section.getEnrollmentCap());
                }
            }
        }
    }

    private void parseCSVFile() {
        final String ERR_MSG = "Error parsing CSV file. Please fix the file and run the application again.";
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(DATA_PATH))) {
            br.readLine(); // Skip the header.
            while ((line = br.readLine()) != null) {
                parseLineAndInputToModel(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(ERR_MSG);
            System.exit(0);
        }
    }

    /**
     * Each line is parsed based on this column order:
     * 0 = SEMESTER
     * 1 = SUBJECT
     * 2 = CATALOGNUMBER
     * 3 = LOCATION
     * 4 = ENROLMENTCAPACITY
     * 5 = ENROLMENTTOTAL
     * 6 = INSTRUCTORS
     * 7 = COMPONENTCODE
     */
    private void parseLineAndInputToModel(String line) {
        String[] data = line.split(",");
        long semesterCode = Long.parseLong(data[0]);
        String subjectName = data[1].trim();
        String catalogNum = data[2].trim();
        String location = data[3].trim();
        int enrolCap = Integer.parseInt(data[4]);
        int enrolTotal = Integer.parseInt(data[5]);
        SortedSet<String> instructors = new TreeSet<>();
        for (int i = 6; i < data.length - 1; i++) {
            String str = data[i];
            String trimmedStr = str.replaceAll("\"", "").trim();
            if (!trimmedStr.equals("<null>") && !trimmedStr.equals("(null)")) {
                instructors.add(trimmedStr);
            }
        }
        String sectionType = data[data.length - 1];

        inputToModel(semesterCode, subjectName, catalogNum, location, enrolCap, enrolTotal, instructors, sectionType);
    }

    public void inputOfferingDataToModel(OfferingData offeringData) {
        long semesterCode = Long.parseLong(offeringData.getSemester());
        String subjectName = offeringData.getSubjectName();
        String catalogNum = offeringData.getCatalogNumber();
        String location = offeringData.getLocation();
        int enrolCap = offeringData.getEnrollmentCap();
        int enrolTotal = offeringData.getEnrollmentTotal();
        String instructor = offeringData.getInstructor();
        SortedSet<String> instructors = new TreeSet<>();
        instructors.add(instructor);
        String sectionType = offeringData.getComponent();

        inputToModel(semesterCode, subjectName, catalogNum, location, enrolCap, enrolTotal, instructors, sectionType);
    }

    private void inputToModel(long semesterCode, String subjectName, String catalogNum,
                              String location, int enrolCap, int enrolTotal, SortedSet<String> instructors,
                              String sectionType) {
        OfferingSection offeringSection = new OfferingSection(sectionType, enrolCap, enrolTotal);
        List<OfferingSection> sectionList = new ArrayList<>();
        sectionList.add(offeringSection);

        Semester semester = new Semester(semesterCode);
        CourseOffering courseOffering = new CourseOffering(semester, location, instructors, sectionList);
        Course course = new Course(catalogNum);

        addCourseOffering(subjectName, course, courseOffering);
    }
}
