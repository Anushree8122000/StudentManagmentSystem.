package com.jsp.studentmanagementsystem.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jsp.studentmanagementsystem.dto.MessageData;
import com.jsp.studentmanagementsystem.dto.StudentRequest;
import com.jsp.studentmanagementsystem.dto.StudentResponse;
import com.jsp.studentmanagementsystem.entity.Student;
import com.jsp.studentmanagementsystem.service.StudentService;
import com.jsp.studentmanagementsystem.util.ResponseStructure;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/students")
public class StudentController {
	@Autowired
	private StudentService service;

	/* @RequestMapping(method = RequestMethod.POST, value="student") */

	@PostMapping //to Save
	//public ResponseEntity<ResponseStructure<Student>> saveStudent(@RequestBody Student student){
	public ResponseEntity<ResponseStructure<StudentResponse>> saveStudent(@RequestBody @Valid StudentRequest studentRequest){
		//return service.saveStudent(student);
		return service.saveStudent(studentRequest);
	}



	/*Using @RequestParam
	 * @PutMapping("/updatestudent") //to update public ResponseEntity<Student>
	 * updateStudent(@RequestBody Student student,@RequestParam int studentId){
	 * 
	 * return service.updateStudent(student, studentId); }
	 */
	@PutMapping("/{studentId}") //to update 
	public ResponseEntity<ResponseStructure<StudentResponse>> updateStudent(@RequestBody  @Valid  StudentRequest studentRequest,@PathVariable int studentId){

		return service.updateStudent(studentRequest, studentId); 
	}


	@DeleteMapping("/{studentId}") 
	public ResponseEntity<ResponseStructure<StudentResponse>> deleteStudent(@RequestBody StudentRequest studentRequest,@PathVariable int studentId) {
		return service.deleteStudent(studentRequest,studentId);
	} 

	//NOTE, FOR @RequestParam =>   @GetMapping("/studentId"),POSTMAN=>  http://localhost:8080/students/studentId?studentId=1
	//NOTE, FOR @PathVariable =>     @GetMapping("/{studentId}"),POSTMAN=>  http://localhost:8080/students/1
	@GetMapping("/{studentId}")
	public ResponseEntity<ResponseStructure<Student>> findStudentById(@PathVariable int studentId) {
		return service.findStudentbyId(studentId);
	}

	@CrossOrigin()
	@GetMapping
	public ResponseEntity<ResponseStructure<List<StudentResponse>>> findAllStudents() {
		return service.findAllStudent();   
	}

	// @GetMapping("/{studentEmail}")
	//In POSTMAN we have pass like this http://localhost:8080/students?studentEmail=shrishtigowda89@gmail.com
	@GetMapping(params ="studentEmail" )
	public ResponseEntity<ResponseStructure<StudentResponse>>  findStudentByEmail(@RequestParam String studentEmail){
		return service. findStudentByEmail(studentEmail);
	}
	//In POSTMAN we have to  pass like this http://localhost:8080/students?grade=A
	@GetMapping(params ="studentGrade" )
	public ResponseEntity<ResponseStructure<List<String>>>  getAllEmailByGrade(@RequestParam String studentGrade){
		return service.getAllEmailByGrade(studentGrade);

	}
	//In POSTMAN we have to  pass like this http://localhost:8080/students?studentPhNo=0987654345678
	@GetMapping(params = "studentPhNo")
	public ResponseEntity<ResponseStructure<StudentResponse>> findStudentByPhNo(@RequestParam long studentPhNo)
	{
		return service.findStudentByPhNo(studentPhNo);
	}
	//annotate with cross origin on which method we have to use
	// @CrossOrigin(origins="http://localhost:8080/students")
	//delete
	//find  
	//findall
	//@PostMapping("/extract/file/{file}")
	@PostMapping("/extract")
	public ResponseEntity<String> ExtractDataFromExcel(@RequestParam MultipartFile file) throws IOException{
		return service.extractDataFromExcel(file);
	}

	@PostMapping("/write/excel")
	public ResponseEntity<String> writeToExcel(@RequestParam String filepath) throws IOException{
		return service.writeToExcel(filepath);
	}
	//@RequestMapping(path = "/Studentdata", method = RequestMethod.POST)

	//CSV(Comma Separated Value)
	@PostMapping("/sheet")
	public ResponseEntity<String> setData(@RequestParam("file") MultipartFile file) throws IOException {
		return service.setData(file);
	}


	@PostMapping("/mail")
	public ResponseEntity<String> sendMail(@RequestBody MessageData messageData){
		return service.sendMail(messageData);
	}    
}
