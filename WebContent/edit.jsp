<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>つぶやき編集</title>
<link href="./css/style.css" rel="stylesheet" type="text/css">
</head>
<body>

	<div class="main-contents">

		<div class="header">
			<a href="./">ホーム</a> <a href="setting">設定</a> <a href="logout">ログアウト</a>
		</div>

		<div class="profile">
			<div class="name">
				<h2>
					<c:out value="${loginUser.name}" />
				</h2>
			</div>
			<div class="account">
				@
				<c:out value="${loginUser.account}" />
			</div>
			<div class="description">
				<c:out value="${loginUser.description}" />
			</div>
		</div>

		<c:if test="${ not empty errorMessages }">
			<div class="errorMessages">
				<ul>
					<c:forEach items="${errorMessages}" var="errorMessage">
						<li><c:out value="${errorMessage}" />
					</c:forEach>
				</ul>
			</div>
			<c:remove var="errorMessages" scope="session" />
		</c:if>

		<div class="form-area">
			<form action="edit" method="post">
				<input name="message_id" type="hidden" value="${message.id}">
				<textarea name="text" cols="100" rows="5" class="tweet-box">${message.text}</textarea>
				<br /> <input type="submit" value="更新">（140文字まで）
			</form>
		</div>

		<div class="copyright">Copyright(c)大山佳祐</div>

	</div>
</body>
</html>
