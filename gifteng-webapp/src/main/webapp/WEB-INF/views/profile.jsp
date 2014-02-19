<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>User profile</title>
    <meta http-equiv="refresh" content="0; url=${assembly.social.signin.ok}?token=<c:out value="${token}" />" />
</head>
<body>
    <%--
    <div><c:out value="${token}" /></div>
    <div><c:out value="${fullName}" /></div>
    <div><c:out value="${email}" /></div>
    --%>
</body>
</html>
