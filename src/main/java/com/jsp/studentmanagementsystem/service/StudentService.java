package com.jsp.studentmanagementsystem.service;

import java.io.FileNotFoundException;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.jsp.studentmanagementsystem.dto.MessageData;
import com.jsp.studentmanagementsystem.dto.StudentRequest;
import com.jsp.studentmanagementsystem.dto.StudentResponse;
import com.jsp.studentmanagementsystem.entity.Student;
import com.jsp.studentmanagementsystem.util.ResponseStructure;

public interface StudentService {

	//public ResponseEntity<Student> saveStudent(Student student);
	//	public ResponseEntity<ResponseStructure<Student>>saveStudent(Student student);
	public ResponseEntity<ResponseStructure<StudentResponse>>saveStudent(StudentRequest studentRequest);
	public ResponseEntity<ResponseStructure<StudentResponse>> updateStudent(StudentRequest studentRequest ,int studentId);
	public ResponseEntity<ResponseStructure<StudentResponse>> deleteStudent(StudentRequest studentRequest,int studentId);
	public ResponseEntity<ResponseStructure<Student>> findStudentbyId(int studentId);
	public ResponseEntity<ResponseStructure<List<StudentResponse>>> findAllStudent();

	public ResponseEntity<ResponseStructure<StudentResponse>> findStudentByEmail(String studentEmail);

	public ResponseEntity<ResponseStructure<List<String>>> getAllEmailByGrade(String studentGrade);


	public ResponseEntity<ResponseStructure<StudentResponse>> findStudentByPhNo(long studentPhno);

	public ResponseEntity<String> extractDataFromExcel(MultipartFile file) throws IOException ;

	public ResponseEntity<String> writeToExcel(String filepath)  throws IOException ;

	//	boolean hasCsvFormat (MultipartFile file);
	//	
	//	void processAndSaveData(MultipartFile file) throws IOException;
	//public void saveStudent() throws FileNotFoundException, IOException;
	//public void setData(Student student)throws FileNotFoundException, IOException;
	public ResponseEntity<String> setData(MultipartFile file)  throws IOException ;

	public ResponseEntity<String> sendMail(MessageData messageData);

}
