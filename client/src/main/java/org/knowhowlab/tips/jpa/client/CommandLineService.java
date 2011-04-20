package org.knowhowlab.tips.jpa.client;

import org.knowhowlab.tips.jpa.model.Group;
import org.knowhowlab.tips.jpa.model.Student;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.jpa.EntityManagerFactoryBuilder;

import javax.persistence.*;
import java.io.PrintWriter;
import java.util.List;

/**
 * @author dpishchukhin
 */
public class CommandLineService {
    private BundleContext bc;
    private static final String STUDENTS_UNIT = "jpa.students";

    public CommandLineService(BundleContext bc) {
        this.bc = bc;
    }

    public void lsstuds(PrintWriter out, String... args) {
        try {
            ServiceReference reference = getEntityManagerFactoryServiceReference();
            try {
                EntityManagerFactory emf = (EntityManagerFactory) bc.getService(reference);
                EntityManager em = emf.createEntityManager();
                Query query = em.createNamedQuery(Student.GET_STUDENTS);
                List<Student> result = query.getResultList();
                if (result != null) {
                    out.println(String.format("Students: %d", result.size()));
                    for (Student student : result) {
                        out.println(String.format("%d %s %s %d", student.getId(), student.getFirstName(), student.getLastName(), student.getGroup().getId()));
                    }
                } else {
                    out.println("Result is null");
                }
                em.close();
            } finally {
                bc.ungetService(reference);
            }
        } catch (Exception e) {
            e.printStackTrace(out);
        }
    }

    public void lsgrps(PrintWriter out, String... args) {
        try {
            ServiceReference reference = getEntityManagerFactoryServiceReference();
            try {
                EntityManagerFactory emf = (EntityManagerFactory) bc.getService(reference);
                EntityManager em = emf.createEntityManager();
                Query query = em.createNamedQuery(Group.GET_GROUPS);
                List<Group> result = query.getResultList();
                if (result != null) {
                    out.println(String.format("Groups: %d", result.size()));
                    for (Group group : result) {
                        out.println(String.format("%d %s", group.getId(), group.getName()));
                    }
                } else {
                    out.println("Result is null");
                }
                em.close();
            } finally {
                bc.ungetService(reference);
            }
        } catch (Exception e) {
            e.printStackTrace(out);
        }
    }

    public void addgrp(PrintWriter out, String... args) {
        if (args == null || args.length != 1) {
            out.println("Group name param is missed");
            return;
        }
        try {
            ServiceReference reference = getEntityManagerFactoryServiceReference();
            try {
                EntityManagerFactory emf = (EntityManagerFactory) bc.getService(reference);
                EntityManager em = emf.createEntityManager();
                EntityTransaction transaction = em.getTransaction();
                try {
                    transaction.begin();
                    Group group = new Group(args[0]);
                    em.persist(group);
                    transaction.commit();
                    out.println(String.format("Group is persisted with ID: %d", group.getId()));
                } catch (Exception e) {
                    transaction.rollback();
                    e.printStackTrace(out);
                }
                em.close();
            } finally {
                bc.ungetService(reference);
            }
        } catch (Exception e) {
            e.printStackTrace(out);
        }
    }

    public void delgrp(PrintWriter out, String... args) {
        if (args == null || args.length != 1) {
            out.println("Group ID param is missed");
            return;
        }
        try {
            ServiceReference reference = getEntityManagerFactoryServiceReference();
            try {
                EntityManagerFactory emf = (EntityManagerFactory) bc.getService(reference);
                EntityManager em = emf.createEntityManager();
                EntityTransaction transaction = em.getTransaction();
                try {
                    transaction.begin();
                    int groupId = Integer.valueOf(args[0]);
                    Group group = getGroup(em, groupId);
                    if (group == null) {
                        throw new Exception(String.format("Unknown Group ID: %d", groupId));
                    }
                    em.remove(group);
                    transaction.commit();
                    out.println(String.format("Group with ID: %d is removed", groupId));
                } catch (Exception e) {
                    transaction.rollback();
                    e.printStackTrace(out);
                }
                em.close();
            } finally {
                bc.ungetService(reference);
            }
        } catch (Exception e) {
            e.printStackTrace(out);
        }
    }

    public void delstud(PrintWriter out, String... args) {
        if (args == null || args.length != 1) {
            out.println("Student ID param is missed");
            return;
        }
        try {
            ServiceReference reference = getEntityManagerFactoryServiceReference();
            try {
                EntityManagerFactory emf = (EntityManagerFactory) bc.getService(reference);
                EntityManager em = emf.createEntityManager();
                EntityTransaction transaction = em.getTransaction();
                try {
                    transaction.begin();
                    int studentId = Integer.valueOf(args[0]);
                    Student student = getStudent(em, studentId);
                    if (student == null) {
                        throw new Exception(String.format("Unknown Student ID: %d", studentId));
                    }
                    em.remove(student);
                    transaction.commit();
                    out.println(String.format("Student with ID: %d is removed", studentId));
                } catch (Exception e) {
                    transaction.rollback();
                    e.printStackTrace(out);
                }
                em.close();
            } finally {
                bc.ungetService(reference);
            }
        } catch (Exception e) {
            e.printStackTrace(out);
        }
    }

    public void addstud(PrintWriter out, String... args) {
        if (args == null || args.length != 3) {
            out.println("Wrong params");
            return;
        }
        try {
            ServiceReference reference = getEntityManagerFactoryServiceReference();
            try {
                EntityManagerFactory emf = (EntityManagerFactory) bc.getService(reference);
                EntityManager em = emf.createEntityManager();
                EntityTransaction transaction = em.getTransaction();
                try {
                    transaction.begin();
                    int groupId = Integer.valueOf(args[2]);
                    Group group = getGroup(em, groupId);
                    if (group == null) {
                        throw new Exception(String.format("Unknown Group ID: %d", groupId));
                    }
                    Student student = new Student(args[0], args[1], group);
                    em.persist(student);
                    transaction.commit();
                    out.println(String.format("Student is persisted with ID: %d", student.getId()));
                } catch (Exception e) {
                    transaction.rollback();
                    e.printStackTrace(out);
                }
                em.close();
            } finally {
                bc.ungetService(reference);
            }
        } catch (Exception e) {
            e.printStackTrace(out);
        }
    }

    private Group getGroup(EntityManager em, int groupId) {
        Query query = em.createNamedQuery(Group.GET_GROUP_BY_ID);
        query.setParameter("groupId", groupId);
        try {
            return (Group) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    private Student getStudent(EntityManager em, int studentId) {
        Query query = em.createNamedQuery(Student.GET_STUDENT_BY_ID);
        query.setParameter("studentId", studentId);
        try {
            return (Student) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    private ServiceReference getEntityManagerFactoryServiceReference() throws Exception {
        ServiceReference[] serviceReferences = bc.getServiceReferences(EntityManagerFactory.class.getName(),
                String.format("(%s=%s)", EntityManagerFactoryBuilder.JPA_UNIT_NAME, STUDENTS_UNIT));
        if (serviceReferences != null && serviceReferences.length > 0) {
            return serviceReferences[0];
        } else {
            throw new Exception("EntityManagerFactory is not available");
        }
    }
}
