package com.lero.web.jdbc;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class StudentControllerServlet
 */
@WebServlet("/StudentControllerServlet")
public class StudentControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private StudentDbUtil studentDbUtil;
	
	@Resource(name="jdbc/web_student_tracker")
	private DataSource dataSource;

	@Override
	public void init() throws ServletException {

		super.init();
		
		try {
			studentDbUtil = new StudentDbUtil(dataSource);
		}
		catch(Exception e) {
			throw new ServletException(e);
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			//read command param
			String theCommand = request.getParameter("command");
			
			//if the command is missing, default to listing students 
			if(theCommand == null) {
				theCommand = "LIST";
				}
			
			//route to the appropriate method
			switch(theCommand) {
			
			case "LIST":
				listStudents(request, response);
				break;
			case "ADD":
				addStudent(request, response);
				break;
			case "LOAD":
				loadStudent(request, response);
				break;
			case "UPDATE":
				updateStudent(request, response);
				break;
			case "DELETE":
				deleteStudent(request, response);
				break;
			case "SORT":
				sortStudent(request, response);
				break;
			default:
				listStudents(request, response);	
			}			
		} 
		catch (Exception exc) {
			throw new ServletException(exc);
		}

	}

	private void sortStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<Student> sortByFirst= studentDbUtil.sortStudents();
		
		request.setAttribute("sortedByFirst", sortByFirst);
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("sortedByFirstName.jsp");
		dispatcher.forward(request, response);
	}

	private void deleteStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		int id = Integer.parseInt(request.getParameter("studentId"));
		studentDbUtil.deleteStudent(id);
		listStudents(request, response);
		
	}

	private void updateStudent(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//read student info from form data
		int id = Integer.parseInt(request.getParameter("studentId"));
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");

		//create a new student object
		Student theStudent = new Student(id, firstName, lastName, email);
		
		//update database
		studentDbUtil.updateStudent(theStudent);
		
		//send back to list students page
		listStudents(request, response);

		
	}

	private void loadStudent(HttpServletRequest request, HttpServletResponse response) throws Exception{
		// read studentId from form data
		String theStudentId = request.getParameter("studentId");
		
		//get student info from database(db util)
		Student theStudent = studentDbUtil.getStudent(theStudentId);
		
		//place student in request attribute
		request.setAttribute("THE_STUDENT", theStudent);
		
		//send to jsp page( update-student-form.jsp
		RequestDispatcher dispathcer = request.getRequestDispatcher("/update-student-form.jsp");
		dispathcer.forward(request, response);
	}

	private void addStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// read student info from form data
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		
		//create a new student object
		Student theStudent = new Student(firstName, lastName, email);
		
		//add to database
		studentDbUtil.addStudent(theStudent);
		
		//send back to main page(student list)
		listStudents(request, response);
		
	}

	private void listStudents(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		List<Student> students = studentDbUtil.getStudents();
		
		request.setAttribute("STUDENT_LIST", students);
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("list-student.jsp");
		dispatcher.forward(request, response);
		
	}

}










