package com.example.school.repository;

import com.example.school.model.Student;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;

import java.util.*;

public interface StudentRepository {

    ArrayList<Student> getStudents();

    Student getStudentById(int studentId);

    Student addStudent(Student student);

    String addMultipleStudents(ArrayList<Student> studentsList);

    Student updateStudent(int studentId, Student student);

    void deleteStudent(int studentId);
}
