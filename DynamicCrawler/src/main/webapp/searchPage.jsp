<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
	<script src="js/jquery-2.1.4.js" ></script>
    <script src="js/search.js"></script>
    <link rel="stylesheet" type="text/css" href="css/theme.css"/>
</head>
<body>
<section class="workshop">
	<form id="searchForm" action="search" label="SEARCH" method="GET">
	
		<input id="query" type="search" name ="query" placeholder="Search...">
		<button type="submit" value="Search">Search</button>
		<div class="clear"></div>
		<div style="margin-top: 4pc;">
		<span>TF.IDF</span>
			<input class="smallRadio" id="radio1" type="radio" name="radioOption" value="1" checked="true">
			<input class="radioText" disabled="true" type="text" value="TF">
			<input class="smallRadio" id="radio2" type="radio" name="radioOption" value="2">
			<input class="radioText" disabled="true" type="text" value="Page Rank">
			<input class="smallRadio" id="radio3" type="radio" name="radioOption" value="3">
			<input class="radioText" disabled="true" type="text" value="TF.IDF & Page Rank">
		</div>
	</form>
</section>
</body>
</html>