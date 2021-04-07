package ca.cmpt213.as5.model;

/**
 * OfferingSection stores information about a section, uniquely identified by type of section
 * (e.g. "LEC", "LAB" .etc).
 */
public class OfferingSection {
    private String type;
    private int enrollmentCap;
    private int enrollmentTotal;

    public OfferingSection(String type, int enrollmentCap, int enrollmentTotal) {
        this.type = type;
        this.enrollmentCap = enrollmentCap;
        this.enrollmentTotal = enrollmentTotal;
    }

    public String getType() {
        return type;
    }

    public int getEnrollmentCap() {
        return enrollmentCap;
    }

    public int getEnrollmentTotal() {
        return enrollmentTotal;
    }

    public void addEnrollmentCap(int cap) {
        enrollmentCap += cap;
    }

    public void addEnrollmentTotal(int total) {
        enrollmentTotal += total;
    }
}
