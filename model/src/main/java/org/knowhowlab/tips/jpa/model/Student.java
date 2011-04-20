package org.knowhowlab.tips.jpa.model;

import javax.persistence.*;

/**
 * @author dpishchukhin
 */
@NamedQueries({
        @NamedQuery(name = Student.GET_STUDENTS, query = "SELECT DISTINCT record"
                + " FROM Student record"
                + " ORDER BY record.lastName"),
        @NamedQuery(name = Student.GET_STUDENT_BY_ID, query = "SELECT DISTINCT record"
                + " FROM Student record"
                + " WHERE record.id = :studentId")
})
@Entity
@Table(name = "STUDENTS")
public class Student {
    public static final String GET_STUDENTS = "GET_STUDENTS";
    public static final String GET_STUDENT_BY_ID = "GET_STUDENT_BY_ID";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String firstName;
    private String lastName;

    @ManyToOne(optional = false)
    private Group group;

    public Student() {
    }

    public Student(String firstName, String lastName, Group group) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.group = group;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getId() {
        return id;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
