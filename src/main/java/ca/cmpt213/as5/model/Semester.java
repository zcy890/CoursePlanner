package ca.cmpt213.as5.model;

/**
 * Semester has the functions to convert semester code to year and term.
 */
public class Semester {
    private long semesterCode;
    private int year;
    private String term;

    public Semester(long semesterCode) {
        this.semesterCode = semesterCode;
        calculateYear();
        extractTerm();
    }

    public long getSemesterCode() {
        return semesterCode;
    }

    public int getYear() {
        return year;
    }

    public String getTerm() {
        return term;
    }

    private void calculateYear() {
        int semester = (int) semesterCode;
        int[] digits = new int[4];
        int i = 0;
        while (semester > 0 && i < digits.length) {
            digits[i] = semester % 10;
            semester /= 10;
            i++;
        }
        year = 1900 + 100 * digits[3] + 10 * digits[2] + digits[1];
    }

    private void extractTerm() {
        long semester = semesterCode % 10;
        if (semester == 1) {
            term = "Spring";
        } else if (semester == 4) {
            term = "Summer";
        } else {
            term = "Fall";
        }
    }

    @Override
    public String toString() {
        return "Semester{" +
                "semesterCode=" + semesterCode +
                ", year=" + year +
                ", term='" + term + '\'' +
                '}';
    }
}
