$(document).ready(function() {
	    		
	$('.daterange').daterangepicker({singleDatePicker: false, format: 'YYYY/MM/DD'});
	
	$('.daterange').keyup(function() {
		$('.daterange').data('daterangepicker').setOptions({singleDatePicker: false, format: 'YYYY/MM/DD'});		
	});
	
});
