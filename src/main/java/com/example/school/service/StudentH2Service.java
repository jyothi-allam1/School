package com.example.school.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.school.model.Student;
import com.example.school.model.StudentRowMapper;
import com.example.school.repository.StudentRepository;

import java.util.*;
import java.util.ArrayList;

@Service
public class StudentH2Service implements StudentRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public ArrayList<Student> getStudents() {
        List<Student> studentList = jdbcTemplate.query("SELECT * FROM STUDENT", new StudentRowMapper());
        return new ArrayList<>(studentList);
    }

    @Override
    public Student getStudentById(int studentId) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM STUDENT WHERE studentId = ?", new Object[] { studentId }, new StudentRowMapper());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found");
        }
    }

    @Override
    public Student addStudent(Student student) {
        jdbcTemplate.update(
                "INSERT INTO STUDENT(studentName, gender, standard) VALUES (?, ?, ?)",
                student.getStudentName(), student.getGender(), student.getStandard());

        return jdbcTemplate.queryForObject(
                "SELECT * FROM STUDENT WHERE studentId = LAST_INSERT_ID()",
                new StudentRowMapper());
    }

    @Override
    public int addMultipleStudents(ArrayList<Student> studentsList) {
        String sql = "INSERT INTO STUDENT(studentName, gender, standard) VALUES (?, ?, ?)";
        List<Object[]> batchArgs = new ArrayList<>();
        for (Student student : students) {
            batchArgs.add(new Object[] { student.getStudentName(), student.getGender(), student.getStandard() });
        }
        int[] insertCounts = jdbcTemplate.batchUpdate(sql, batchArgs);
        int totalInserted = 0;
        for (int count : insertCounts) {
            totalInserted += count;
        }
        return totalInserted;
    }

    @Override
    public Student updateStudent(int studentId, Student student) {
        jdbcTemplate.update(
                "UPDATE STUDENT SET studentName = ?, gender = ?, standard = ? WHERE studentId = ?",
                student.getStudentName(), student.getGender(), student.getStandard(), studentId);

        return getStudentById(studentId);
    }

    @Override
    public void deleteStudent(int studentId) {
        jdbcTemplate.update(
                "DELETE FROM STUDENT WHERE studentId = ?",
                studentId);
    }
}
