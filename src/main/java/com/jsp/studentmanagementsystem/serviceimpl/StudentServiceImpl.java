package com.jsp.studentmanagementsystem.serviceimpl;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.util.Optional;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.message.SimpleMessage;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jsp.studentmanagementsystem.dto.MessageData;
import com.jsp.studentmanagementsystem.dto.StudentRequest;
import com.jsp.studentmanagementsystem.dto.StudentResponse;
import com.jsp.studentmanagementsystem.entity.Student;
import com.jsp.studentmanagementsystem.exception.StudentEmailsNotFoundByGradeException;
import com.jsp.studentmanagementsystem.exception.StudentNotFoundById;
import com.jsp.studentmanagementsystem.exception.StudentNotFoundByPhNoException;
import com.jsp.studentmanagementsystem.exception.studentNotFoundByEmail;
import com.jsp.studentmanagementsystem.repository.StudentRepo;
import com.jsp.studentmanagementsystem.service.StudentService;
import com.jsp.studentmanagementsystem.util.ResponseStructure;

@Service
public class StudentServiceImpl implements StudentService {
	@Autowired
	private StudentRepo repo;

	@Autowired
	private JavaMailSender javaMailSender;

	@Override
	public ResponseEntity<ResponseStructure<StudentResponse>> saveStudent(StudentRequest studentRequest) {
		Student student = new Student();
		student.setStudentName(studentRequest.getStudentName());
		student.setStudentEmail(studentRequest.getStudentEmail());
		student.setStudentGrade(studentRequest.getStudentGrade());
		student.setStudentPhNo(studentRequest.getStudentPhNo()); 
		student.setStudentPassword(studentRequest.getStudentPassword());

		Student Student2 = repo.save(student);

		StudentResponse response = new StudentResponse();
		response.setStudentId(Student2.getStudentId());
		response.setStudentName(Student2.getStudentName());
		response.setStudentGrade(Student2.getStudentGrade());

		//		ResponseStructure<Student> structure = new ResponseStructure<Student>();
		ResponseStructure<StudentResponse> structure = new ResponseStructure<>();
		structure.setStatus(HttpStatus.CREATED.value());
		structure.setMessage("student data saved successfully");
		//structure.setData(Student2);
		structure.setData(response);
		return new ResponseEntity<ResponseStructure<StudentResponse>>(structure, HttpStatus.CREATED);
	}

	//	 @Override
	//	public ResponseEntity<Student> updateStudent(Student student, int studentId) {
	//		Optional<Student> optional = repo.findById(studentId);
	//		if(optional.isPresent()) {
	//			Student student2 = optional.get();
	//			student.setStudentId(student2.getStudentId());
	//			//student.setStudentName(student2.getStudentName());
	//			Student student3 = repo.save(student);
	//			return new  ResponseEntity<Student> (student3, HttpStatus.OK);
	//		}
	//		return null;
	//	}
	@Override
	public ResponseEntity<ResponseStructure<StudentResponse>> updateStudent(StudentRequest studentRequest, int studentId) {




		Optional<Student> optional = repo.findById(studentId);

		if (optional.isPresent()) {
			Student existingStudent = optional.get();

			// Update the properties of the existing student with the values from the
			// updated student
			//updated.setStudentId(existingStudent.getStudentId());
			//	updatedRequest.setStudentId(existingStudent.getStudentId());
			Student student = new Student();
			//student.setStudentId(student);
			student.setStudentName(studentRequest.getStudentName());
			student.setStudentEmail(studentRequest.getStudentEmail());
			student.setStudentGrade(studentRequest.getStudentGrade());
			student.setStudentPhNo(studentRequest.getStudentPhNo()); 
			student.setStudentPassword(studentRequest.getStudentPassword());
			student.setStudentId(existingStudent.getStudentId());


			//->Student savedStudent = repo.save(updatedRequest);
			Student savedStudent = repo.save(student);

			StudentResponse response = new StudentResponse();
			response.setStudentId(savedStudent.getStudentId());
			response.setStudentName(savedStudent.getStudentName());
			response.setStudentGrade(savedStudent.getStudentGrade()); 




			ResponseStructure<StudentResponse> structure = new ResponseStructure<>();
			structure.setStatus(HttpStatus.OK.value()); // Use OK (200) for successful update
			structure.setMessage("---- Student  data updated successfully---");
			//structure.setData(savedStudent); // Return the updated student
			structure.setData(response);
			return new ResponseEntity<ResponseStructure<StudentResponse>>(structure,HttpStatus.OK);
		}

		// Return 404 Not Found if student with the given ID was not found
		ResponseStructure<StudentResponse> notFoundStructure = new ResponseStructure<>();
		notFoundStructure.setStatus(HttpStatus.NOT_FOUND.value());
		notFoundStructure.setMessage("Student not found with ID: " + studentId);

		return new ResponseEntity<ResponseStructure<StudentResponse>>(notFoundStructure, HttpStatus.NOT_FOUND);
		//return null;
	}

	//	 @Override
	//	    public ResponseEntity<ResponseStructure<Student>> deleteStudent(int studentId) {
	//	        Optional<Student> optional = repo.findById(studentId);
	//	        if (optional.isPresent()) {
	//	            Student student = optional.get();
	//	            repo.delete(student);
	//	            return new ResponseEntity<ResponseStructure<Student>>(HttpStatus.NO_CONTENT);
	//	        }
	//	        return new ResponseEntity<ResponseStructure<Student>>(HttpStatus.NOT_FOUND);
	//	    }
	@Override
	public ResponseEntity<ResponseStructure<StudentResponse>> deleteStudent(StudentRequest studentRequest,int studentId) {
		Optional<Student> optional = repo.findById(studentId);

		if (optional.isPresent()) {
			Student student = optional.get();
			Student student1 = new Student(); 
			student1.setStudentName(studentRequest.getStudentName());
			student1.setStudentEmail(studentRequest.getStudentEmail());
			student1.setStudentGrade(studentRequest.getStudentGrade());
			student1.setStudentPhNo(studentRequest.getStudentPhNo()); 
			student1.setStudentId(student.getStudentId());

			repo.delete(student);

			StudentResponse response = new StudentResponse();
			response.setStudentId(student.getStudentId());
			response.setStudentName(student.getStudentName());
			response.setStudentGrade(student.getStudentGrade());

			// Return a response indicating successful deletion with status 204 No Content
			ResponseStructure<StudentResponse> structure = new ResponseStructure<>();
			structure.setStatus(HttpStatus.OK.value());
			structure.setMessage("Student deleted successfully");
			structure.setData(response);
			return new ResponseEntity<ResponseStructure<StudentResponse>>(structure, HttpStatus.OK);
		}

		// Return a response indicating student not found with status 404 Not Found
		ResponseStructure<StudentResponse> notFoundStructure = new ResponseStructure<>();
		notFoundStructure.setStatus(HttpStatus.NOT_FOUND.value());
		notFoundStructure.setMessage("Student not found with ID: " + studentId);

		//	    return new ResponseEntity<>(notFoundStructure, HttpStatus.NOT_FOUND);
		throw new StudentNotFoundById("-----------Failed to delete the student-------------");
	}

	//	    @Override
	//	    public ResponseEntity<Student> findStudentbyId(int studentId) {
	//	        Optional<Student> optional = repo.findById(studentId);
	//	        if (optional.isPresent()) {
	//	            Student student = optional.get();
	//	            return new ResponseEntity<>(student, HttpStatus.OK);
	//	        }
	//	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	//	    }
	@Override
	public ResponseEntity<ResponseStructure<Student>> findStudentbyId(int studentId) {
		Optional<Student> optional = repo.findById(studentId);

		if (optional.isPresent()) {
			Student student = optional.get();

			ResponseStructure<Student> structure = new ResponseStructure<>();
			structure.setStatus(HttpStatus.OK.value());
			structure.setMessage("Student found");
			structure.setData(student);

			return new ResponseEntity<ResponseStructure<Student>>(structure, HttpStatus.OK);
		}

		// Return a response indicating student not found with status 404 Not Found
		ResponseStructure<Student> notFoundStructure = new ResponseStructure<>();
		notFoundStructure.setStatus(HttpStatus.NOT_FOUND.value());
		notFoundStructure.setMessage("Student not found with ID: " + studentId);

		//return new ResponseEntity<ResponseStructure<Student>>(notFoundStructure, HttpStatus.NOT_FOUND);
		throw new StudentNotFoundById("-------------Student Id is not Available-------------");
	}  

	//	    @Override
	//	    public ResponseEntity<ResponseStructure<List<Student>>> findAllStudent() {
	//	        List<Student> students = repo.findAll();
	//	        if (!students.isEmpty()) {
	//	            return new ResponseEntity<>(students, HttpStatus.OK);
	//	        }
	//	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	//	    }
	@Override
	public ResponseEntity<ResponseStructure<List<StudentResponse>>> findAllStudent() { 
		List<Student> list = repo.findAll();

		if (!list.isEmpty()) {
			List<StudentResponse> res = new ArrayList<StudentResponse>();
			for (Student student : list) {
				StudentResponse response = new StudentResponse();
				response.setStudentId(student.getStudentId());
				response.setStudentName(student.getStudentName());
				response.setStudentGrade(student.getStudentGrade());
				res.add(response);
			}

			ResponseStructure<List<StudentResponse>> structure = new ResponseStructure<List<StudentResponse>>();
			structure.setStatus(HttpStatus.OK.value());
			structure.setMessage("Students data retrieved successfully");
			structure.setData(res);

			return new ResponseEntity<ResponseStructure<List<StudentResponse>>>(structure, HttpStatus.OK);
		} else {
			ResponseStructure<List<StudentResponse>> notFoundStructure = new ResponseStructure<>();
			notFoundStructure.setStatus(HttpStatus.NOT_FOUND.value());
			notFoundStructure.setMessage("No students found");

			return new ResponseEntity<ResponseStructure<List<StudentResponse>>>(notFoundStructure, HttpStatus.NOT_FOUND);
		}
	}
	@Override
	public ResponseEntity<ResponseStructure<StudentResponse>> findStudentByEmail(String studentEmail) {
		// TODO Auto-generated method stub
		Student student = repo.findByStudentEmail(studentEmail);
		if(student != null) {
			StudentResponse response =new StudentResponse();
			response.setStudentId(student.getStudentId());
			response.setStudentName(student.getStudentName());
			response.setStudentGrade(student.getStudentGrade());


			ResponseStructure<StudentResponse> structure = new ResponseStructure<StudentResponse>();
			structure.setStatus(HttpStatus.FOUND.value());
			structure.setMessage("Student Found Based on Email");
			structure.setData(response);

			return new ResponseEntity<ResponseStructure<StudentResponse>>(structure, HttpStatus.FOUND);
		}
		else {
			//	throw new studentNotFoundByEmail("Failed to find student");
			ResponseStructure<StudentResponse> notFoundStructure = new ResponseStructure<StudentResponse>();
			notFoundStructure.setStatus(HttpStatus.NOT_FOUND.value());
			notFoundStructure.setMessage("Student not found with Email: " + studentEmail);

			//return new ResponseEntity<ResponseStructure<StudentResponse>>(notFoundStructure,HttpStatus.NOT_FOUND);
			throw new studentNotFoundByEmail("------------Failed to find studentEmail--------");

		} 
	}


	//	

	//	
	//	
	@Override
	public ResponseEntity<ResponseStructure<List<String>>> getAllEmailByGrade(String studentGrade) {
		List<String> lis=repo.getAllEmailsByGrade(studentGrade);
		if(lis.isEmpty()!=true)
		{ 

			//StudentResponse response =new StudentResponse();

			ResponseStructure<List<String>> structure=new ResponseStructure<>();
			structure.setData(lis);
			structure.setMessage("successfully got the Grade");
			structure.setStatus(HttpStatus.FOUND.value());

			return new ResponseEntity<ResponseStructure<List<String>>>(structure,HttpStatus.FOUND);

		}
		else
		{ 
			ResponseStructure<StudentResponse> notFoundStructure = new ResponseStructure<StudentResponse>();
			notFoundStructure.setStatus(HttpStatus.NOT_FOUND.value());
			notFoundStructure.setMessage("Student not found with Grade: " + studentGrade);
			throw new StudentEmailsNotFoundByGradeException("----------Students  are  not found for the given grade----------");

		}
	}

	@Override
	public ResponseEntity<ResponseStructure<StudentResponse>> findStudentByPhNo(long studentPhno) {
		Student student1 = repo.findByStudentPhNo(studentPhno); 
		if (student1 != null) {
			StudentResponse response = new StudentResponse();
			response.setStudentId(student1.getStudentId());
			response.setStudentName(student1.getStudentName());
			response.setStudentGrade(student1.getStudentGrade());

			ResponseStructure<StudentResponse> structure = new ResponseStructure<StudentResponse>();
			structure.setStatus(HttpStatus.FOUND.value());
			structure.setMessage("student found for the given phone number");
			structure.setData(response);
			return new ResponseEntity<ResponseStructure<StudentResponse>>(structure, HttpStatus.FOUND);
		} 
		else
		{
			ResponseStructure<StudentResponse>   notFoundStructure = new ResponseStructure<StudentResponse>();	
			notFoundStructure.setStatus(HttpStatus.NOT_FOUND.value());
			notFoundStructure.setMessage("Student not found with Phone number: " +  studentPhno);
			// //return new ResponseEntity<ResponseStructure<StudentResponse>>(notFoundStructure,HttpStatus.NOT_FOUND); 
			throw new StudentNotFoundByPhNoException("------------Failed to find studentPhno--------");
		}
	}

	@Override
	public ResponseEntity<String> extractDataFromExcel(MultipartFile file) throws IOException {
		XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
		for(Sheet sheet:workbook) {
			for(Row row:sheet) {
				if(row.getRowNum()>0) { 
					if(row!=null) {
						String name = row.getCell(0).getStringCellValue();
						String email = row.getCell(1).getStringCellValue();
						long phoneNumber = (long)row.getCell(2).getNumericCellValue();
						String grade = row.getCell(3).getStringCellValue();
						String password = row.getCell(4).getStringCellValue();

						System.out.println(name +","+email+","+phoneNumber+","+grade+","+password);

						Student student = new Student();
						student.setStudentName(name);
						student.setStudentGrade(grade);
						student.setStudentEmail(email);
						student.setStudentPhNo(phoneNumber); 
						student.setStudentPassword(password);

						repo.save(student);

					}
				}
			}
		}
		//to avoid error in worksheet
		workbook.close();
		return null;

	}

	@Override
	public ResponseEntity<String> writeToExcel(String filepath) throws IOException {
		List<Student> students = repo.findAll();
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet();

		Row header = sheet.createRow(0);
		header.createCell(0).setCellValue("studentId");
		header.createCell(1).setCellValue("studentName");
		header.createCell(2).setCellValue("studentEmail");
		header.createCell(3).setCellValue("studentPhNo");
		header.createCell(4).setCellValue("studentGrade");
		header.createCell(5).setCellValue("studentPassword");

		int rowNum=1;
		for(Student student:students) {
			Row row = sheet.createRow(rowNum++);

			row.createCell(0).setCellValue(student.getStudentId());
			row.createCell(1).setCellValue(student.getStudentName());
			row.createCell(2).setCellValue(student.getStudentEmail());
			row.createCell(3).setCellValue(student.getStudentPhNo());
			row.createCell(4).setCellValue(student.getStudentGrade());
			row.createCell(5).setCellValue(student.getStudentPassword());
		}

		FileOutputStream outputstream = new FileOutputStream(filepath);
		workbook.write(outputstream);

		workbook.close();
		return new ResponseEntity<String>("Data Transfered to Excel sheet",HttpStatus.OK);

	}
	//   String line="";
	//	@Override
	//	public ResponseEntity<String> setData(MultipartFile file) throws IOException {
	//		  BufferedReader fileReader = new BufferedReader(new FileReader("src/main/resources/test.xlsx"));
	//		  while((line=fileReader.readLine())!=null) {
	//			  String [] data =line.split(",");
	//			  // Assuming the order of data in CSV is: name, grade, email, phoneNumber, password
	//			     String name = data[0].trim();
	//			        String email = data[1].trim();
	//			        long phoneNumber = Long.parseLong(data[2].trim());
	//			        String grade = data[3].trim(); // Assuming grade is a String, adjust if it's a numeric value
	//			        String password = data[4].trim();
	//
	//			        Student student1 = new Student();
	//			        student1.setStudentName(name);
	//			        student1.setStudentEmail(email);
	//			        student1.setStudentPhNo(phoneNumber);
	//			        student1.setStudentGrade(grade);
	//			        student1.setStudentPassword(password);
	//
	//			        // Save the student object to the database or perform other operations
	//			        repo.save(student1);
	//			    }
	//			    fileReader.close();
	//				return null;
	//	}


	@Override
	public ResponseEntity<String> setData(MultipartFile file) throws IOException {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
			@SuppressWarnings("deprecation")
			CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);


			for (CSVRecord csvRecord : csvParser) {
				String name = csvRecord.get("Name");
				String email = csvRecord.get("Email");   
				long phoneNumber = Long.parseLong(csvRecord.get("PhoneNumber"));
				String grade = csvRecord.get("Grade");
				String password = csvRecord.get("Password");

				Student student = new Student();
				student.setStudentName(name);
				student.setStudentEmail(email);
				student.setStudentPhNo(phoneNumber);
				student.setStudentGrade(grade);
				student.setStudentPassword(password);

				repo.save(student);
			}
		}

		return ResponseEntity.ok("Student data saved successfully.");
	}



	@Override
	public ResponseEntity<String> sendMail(MessageData messageData) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(messageData.getTo());
		message.setSubject(messageData.getSubject());
		message.setText(messageData.getText());
		message.setSentDate(new Date());

		javaMailSender.send(message);

		return new ResponseEntity<String>("Mail send Successfully",HttpStatus.OK);


	}

}



//	@Override
//	public boolean hasCsvFormat(MultipartFile file) {
//		String type ="text/csv";
//		if(!type.equals(file.getContentType()))
//		return true;
//		return false;
//	}
//
//	@Override
//	public void processAndSaveData(MultipartFile file) throws IOException {
//		List<Student> students = csvToStudent(file.getInputStream());
//		repo.saveAll(students);
//		
//	}
//
//	private List<Student> csvToStudent(InputStream inputStream) throws UnsupportedEncodingException {
//    BufferedReader fileReader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
//    CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnore)
//		return null;
//	}














