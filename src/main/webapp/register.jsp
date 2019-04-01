<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Gmail Enrollment</title>
</head>
<body>
    <form action = "<%= request.getContextPath()%>/register/submit" method = "post">
<!--     <form action = "register/submit" method = "post"> -->
        Your Gmail Address: <input type = "text" name = "gmail"/><br/>
        <input type = "submit" value = "submit"/>
    </form>
</body>
</html