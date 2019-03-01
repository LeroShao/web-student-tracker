<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE thml>
<html>

<link type="text/css" rel="stylesheet" href="css/style.css">

<head>
	<title>Student Tracker App</title>
	
	
</head>
<body>

	<div id="wrapper">
		<div id="header">
		<h2>Unicorn University</h2>
		</div>
	</div>
	
	<div id="container">
		<div id="content">
		
		<input type="button" value="Add Student"
				onclick="window.location.href='add-student-form.jsp'; return false;"
				class="add-student-button"
		/>
		
			<table>
			
				<tr>
					<th>First Name</th>
					<th>Last Name</th>
					<th>Email</th>
					<th>Action</th>
				</tr>
				
				<c:forEach var = "tempStudent" items = "${sortedByFirst}">
					<c:url var="tempLink" value="StudentControllerServlet">
						<c:param name="command" value="LOAD"/>
						<c:param name="studentId" value="${tempStudent.id}"/>
					</c:url>
					
					<c:url var="deleteLink" value="StudentControllerServlet">
						<c:param name="command" value="DELETE"/>
						<c:param name="studentId" value="${tempStudent.id}"/>
					</c:url>
				
					<tr>
						<td>${tempStudent.firstName}</td>
						<td>${tempStudent.lastName}</td>
						<td>${tempStudent.email}</td>
						<td>
							<a href="${tempLink}">Update</a>
							|
							<a href="${deleteLink}"
							onclick="if(!(confirm('Are you sure you want to delete this student?')))return false">Delete</a>
						</td>
					</tr>
					
				</c:forEach>
			
			
			</table>
		
		</div>
	</div>
<form action="StudentControllerServlet" method = "GET">
	<input type ="hidden" name ="command" value = "List"/>
	<table>
		<tr>
			<td>
				<input type = "submit" value = "Sort Students by Last Name"/>
			</td>
		</tr>
	</table>
</form>
</body>


</html>