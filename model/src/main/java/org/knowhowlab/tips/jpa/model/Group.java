package org.knowhowlab.tips.jpa.model;

import javax.persistence.*;
import java.util.List;

/**
 * @author dpishchukhin
 */
@NamedQueries({
        @NamedQuery(name = Group.GET_GROUPS, query = "SELECT DISTINCT record"
                + " FROM Group record"
                + " ORDER BY record.name"),
        @NamedQuery(name = Group.GET_GROUP_BY_ID, query = "SELECT DISTINCT record"
                + " FROM Group record"
                + " WHERE record.id = :groupId")
})
@Entity
@Table(name = "GROUPS")
public class Group {
    public static final String GET_GROUPS = "GET_GROUPS";
    public static final String GET_GROUP_BY_ID = "GET_GROUP_BY_ID";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "group")
    private List<Student> students;

    public Group() {
    }

    public Group(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}
