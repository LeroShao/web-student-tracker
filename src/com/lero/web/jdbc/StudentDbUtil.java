package com.lero.web.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

public class StudentDbUtil {
	
	private DataSource dataSource;
	
	public StudentDbUtil(DataSource theDataSource) {
		dataSource = theDataSource;
	}
	
	public List<Student> getStudents() throws Exception{
		List<Student> students = new ArrayList<>();
		
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		
		try {
			myConn = dataSource.getConnection();
			myStmt = myConn.createStatement();
			myRs = myStmt.executeQuery("select * from student order by last_name");
			
			while(myRs.next()) {
				int id = myRs.getInt("id");
				String firstName = myRs.getString("first_name");
				String lastName = myRs.getString("last_name");
				String email = myRs.getString("email");
				
				Student tempStudent = new Student(id, firstName, lastName, email);
				students.add(tempStudent);		
			}
			return students;		
		}
		finally {
			close(myRs, myStmt, myConn);	
		}		
	}

	public List<Student> sortStudents() throws Exception{
		List<Student> studentsByFirstName = new ArrayList<>();
		
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		
		try {
			myConn = dataSource.getConnection();
			myStmt = myConn.createStatement();
			myRs = myStmt.executeQuery("select * from student order by first_name");
			
			while(myRs.next()) {
				int id = myRs.getInt("id");
				String firstName = myRs.getString("first_name");
				String lastName = myRs.getString("last_name");
				String email = myRs.getString("email");
				
				Student tempStudent = new Student(id, firstName, lastName, email);
				studentsByFirstName.add(tempStudent);
				}
			return studentsByFirstName;
		}
		finally {
			close(myRs, myStmt, myConn);
		}
		
	}
	private void close(ResultSet myRs, Statement myStmt, Connection myConn) {

		try {
			if(myRs != null) myRs.close();
			if(myStmt != null) myStmt.close();
			if(myConn != null) myConn.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			}
	}

	public void addStudent(Student theStudent) throws Exception {
		Connection myConn = null;
		PreparedStatement myStmt = null;
		
		try {
			//get db connection
			myConn = dataSource.getConnection();
			
			// create SQL for insert
			String sql = "insert into student (first_name, last_name, email)"
					+ "values(?, ?, ?)";
			
			myStmt = myConn.prepareStatement(sql);
			
			//set param values for the student
			myStmt.setString(1, theStudent.getFirstName());
			myStmt.setString(2, theStudent.getLastName());
			myStmt.setString(3, theStudent.getEmail());
			
			//execute SQL insert
			myStmt.execute();
		
		}
		//clean up JDBC objects
		finally {
			close(null, myStmt, myConn);
			
		}
		
	}

	public Student getStudent(String theStudentId) throws Exception {
		Student theStudent = null;
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		int studentId;
		
		try {
			studentId = Integer.parseInt(theStudentId);
			myConn = dataSource.getConnection();
			String sql = "select * from student where id=?";
			myStmt = myConn.prepareStatement(sql);
			myStmt.setInt(1, studentId);
			myRs = myStmt.executeQuery();
			
			if(myRs.next()) {
				String firstName = myRs.getString("first_name");
				String lastName = myRs.getString("last_name");
				String email = myRs.getString("email");
				
				theStudent = new Student(studentId, firstName, lastName, email); 
			}
			else {
				throw new Exception("Could not find student: " + studentId);
			}
			
			return theStudent;
		}
		finally {
			close(myRs, myStmt, myConn);
		}
		
	}

	public void updateStudent(Student theStudent) throws Exception {
		Connection myConn = null;
		PreparedStatement myStmt = null;
		
		try {
			myConn = dataSource.getConnection();
			String sql = "update student "
					+ "set first_name=?, last_name=?, email=? "
					+ "where id=?";
			myStmt = myConn.prepareStatement(sql);
			
			myStmt.setString(1, theStudent.getFirstName());
			myStmt.setString(2, theStudent.getLastName());
			myStmt.setString(3, theStudent.getEmail());
			myStmt.setInt(4, theStudent.getId());

			myStmt.execute();
		}
		finally {
			close(null, myStmt, myConn);
		}
		
	}

	public void deleteStudent(int id) throws Exception{

		Connection myConn = null;
		PreparedStatement myStmt = null;
		
		try {
			myConn = dataSource.getConnection();
			String sql = "delete from student where id=?";
			myStmt = myConn.prepareStatement(sql);
			myStmt.setInt(1, id);
			myStmt.execute();
			
			
		}
		finally {
			close(null, myStmt, myConn);
		}
	}

}
