package com.vidyasagar.attendance.service;

import com.vidyasagar.attendance.entity.Student;
import com.vidyasagar.attendance.entity.StudentDTO;
import com.vidyasagar.attendance.exception.ResourceNotFoundException;
import com.vidyasagar.attendance.mapper.StudentMapper;
import com.vidyasagar.attendance.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service implementation for managing Student operations.
 * This class handles all business logic and interacts with
 * the StudentRepository and StudentMapper for persistence and data transformation.
 */
@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    // Constructor Injection
    public StudentServiceImpl(StudentRepository studentRepository, StudentMapper studentMapper) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
    }

//    private StudentDTO convertToDTO(Student student) {
//        return new StudentDTO(student.getId(), student.getName(), student.getEmail());
//    }

    @Override
    public String getHelloMessage() {
        return "Student API working fine!";
    }

    /**
     * Saves a new student to the database.
     *
     * @param studentDto Data received from the API layer (converted from request body)
     * @return Saved StudentDTO with generated ID and mapped fields
     */
    @Override
    public StudentDTO saveStudent(StudentDTO studentDto) {
        Student student = studentMapper.toEntity(studentDto);
        Student saveStudent = studentRepository.save(student);
        return studentMapper.toDTO(saveStudent);
    }

    /**
     * Retrieves all students from the database.
     *
     * @return List of StudentDTO objects representing all students
     */
    @Override
    public List<StudentDTO> getAllStudents() {
        List<Student> students = studentRepository.findAll();
        return studentMapper.toDTOList(students);
//        return studentRepository.findAll()
//                .stream()
//                .map(this::convertToDTO)
//                .collect(Collectors.toList());
    }

    /**
     * Retrieves a student by their unique ID.
     *
     * @param id Student ID received from the API layer
     * @return StudentDTO representing the found student
     * @throws ResourceNotFoundException if the student does not exist
     */
    public StudentDTO getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id " + id));
        return studentMapper.toDTO(student);

//        Student student = studentRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id " + id));
//        return convertToDTO(student);
    }

    /**
     * Updates an existing student's details.
     *
     * @param id             Student ID from the API layer
     * @param studentDetails StudentDTO containing updated field values
     * @return Updated StudentDTO after saving to the database
     * @throws ResourceNotFoundException if no student is found with the given ID
     */
    @Override
    public StudentDTO updateStudent(Long id, StudentDTO studentDetails) {
        // Find existing student
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id " + id));

        //update fields
//        student.setName(studentDetails.getName());
//        student.setEmail(studentDetails.getEmail());

        studentMapper.updateStudentFromDTO(studentDetails, student);

        //Save updated student
        Student updatedStudent = studentRepository.save(student);
        return studentMapper.toDTO(updatedStudent);
    }

    /**
     * Deletes a student by ID.
     *
     * @param id Student ID from the API layer
     * @throws ResourceNotFoundException if no student is found with the given ID
     */
    @Override
    public void deleteStudent(Long id) {
        // Find existing student
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id " + id));

        studentRepository.deleteById(id);
    }

    /**
     * Finds students by their name (exact match).
     *
     * @param name Name of the student to search for
     * @return List of StudentDTO objects matching the given name
     */
    @Override
    public List<StudentDTO> findByName(String name) {
        List<Student> students = studentRepository.findByName(name);
        return studentMapper.toDTOList(students);
    }

    /**
     * Finds students whose age is greater than the specified value.
     *
     * @param age Minimum age threshold
     * @return List of StudentDTO objects where student.age > given age
     */
    @Override
    public List<StudentDTO> findByAgeGreaterThan(int age) {
        List<Student> students = studentRepository.findByAgeGreaterThan(age);
        return studentMapper.toDTOList(students);
    }

    /**
     * Finds students whose email contains the given keyword (case-sensitive by default).
     *
     * @param keyword Keyword or partial email to search for
     * @return List of StudentDTO objects matching the search criteria
     */
    @Override
    public List<StudentDTO> findByEmailContains(String keyword) {
        List<Student> students = studentRepository.findByEmailContains(keyword);
        return studentMapper.toDTOList(students);
    }


}
