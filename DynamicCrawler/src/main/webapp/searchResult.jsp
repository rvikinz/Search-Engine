<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
		<script src="js/jquery-2.1.4.js" ></script>
        <script src="js/search.js"></script>
        <link rel="stylesheet" type="text/css" href="css/theme.css"/>
   <script type="text/javascript">
    $(document).ready(function(){
    	$('.workshop').css('position', 'relative');
    	$('body').css('background', '#151515');    	
    });
   </script>
</head>
<body>

<section class="workshop">
	<form id="searchForm" action="search" label="SEARCH" method="GET">
	
		<input id="query" type="search" name ="query" placeholder="Search...">
		<button type="submit" value="Search">Search</button>
		<div class="clear"></div>
		<div style="margin-top: 1pc;">
		<span>TF.IDF</span>
			<input class="smallRadio" id="radio1" type="radio" name="radioOption" value="1" checked="true">
			<input class="radioText" disabled="true" type="text" value="TF">
			<input class="smallRadio" id="radio2" type="radio" name="radioOption" value="2">
			<input class="radioText" disabled="true" type="text" value="Page Rank">
			<input class="smallRadio" id="radio3" type="radio" name="radioOption" value="3">
			<input class="radioText" disabled="true" type="text" value="TF.IDF & Page Rank">
		</div>
	</form>
	<div class="panel">
	<table id="table">
		<thead>
			<tr>
			</tr>
		</thead>
		<tbody class="tableBody">
			<c:forEach items="${searchResults}" var="current">
				<tr>
					<td><a target="_blank" href=<c:out value="${current.url}"/>> <c:out value="${current.title}"/></a></td>
					<td class="td" ><b><c:out value="${current.score }" /></b></td>
				</tr>
				<tr>
					<td><c:out value="${current.snippet}" /></td>
					<td><c:out value="${current.fileName}" /></td>
				</tr> 	
			</c:forEach>
		</tbody>
	</table>
	</div>
	</section>
</body>
</html>