
$(document).ready(function() 
{
	$('#submitBtn').click(function() 
    {
		var formData = $('#searchForm').serialize();
		console.log(formData);
		$.ajax({
			type: 'GET',
			url: "/search?query="+ $("#query").val() + "&radio1=" + $("#radio1").val() + "&radio2=" + $("#radio2").val() + "&radio3=" + $("#radio3").val(),
			cache: false
		}).done(function(data) {
			console.log(data);
		});
	});
});