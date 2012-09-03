function selectGroup() {
	  $("#groups").change(function() {
			$("#loader").show();
			$.get("ajax/winner/" + $(this).val(), function(data) {
				$("#winner").html(data);
				$("#winner").fadeIn(3000);
				$("#loader").hide();
			});
			
	    });
}