$(document).ready(function() {

	$(".ui.dropdown").dropdown();
   
	setFormValidation();
            
    $(".ui.app.dropdown").dropdown({
    	onChange : function() {
    		var environment = $(".environment.name").text();
    		var application = $(".application.field .ui.dropdown").dropdown("get value");
    		$.ajax({
    			   url: 'getcomponents/',
    			   data: {
    				   environment : environment,
    				   application : application
    			   },
    			   type: 'GET',
    			   success: function(result) {
    				   $(".component.field").html(result);
    				   $(".ui.component.dropdown").dropdown();
    				   setFormValidation();
    			   },
    			   error: function(xhr,status,error) {
    				   //alert(error);
    			   }    			   
    			});
    	}
    }); 
        
    $('.ui.clear.button') .click (function() {
    	$('.ui.form.error').removeClass('error');
    	$('.error.message .list').remove();
    });
        
    $('.ui.form .unix.field').popup({
		on: 'hover'
	});
    
    $('.authentication.message .close').on('click', function() {
		$(this).closest('.authentication.message').transition('fade');
    });
	
    //$(document).on("keydown", disableF5);
    
});

//function disableF5(e) { if ((e.which || e.keyCode) == 116 || (e.which || e.keyCode) == 82) e.preventDefault(); };

function setFormValidation() {
	$('.ui.form').form ({
  	    user: {
  	    	identifier : 'user',
  	    	rules: [{
  	    		type	: 'empty',
  	    		prompt	: 'Please enter your T or X-ID.'
  	    	}]
  	    },
  	    
  	    pass: {
	    	identifier	: 'pass',
	    	rules: [{
	    		type	: 'empty',
	    		prompt	: 'Please enter your password.'
	    	}]
	    },
  	    
		application: {
	    	identifier	: 'application',
	    	rules: [{
	    		type	: 'empty',
	    		prompt	: 'Please select an application.'
	    	}]
	    },

	    component: {
	    	identifier	: 'component',
	    	rules: [{
	    		type	: 'empty',
	    		prompt	: 'Please select a component.'
	    	}]
	    },
	    
	    numResults: {
	    	identifier	: 'numResults',
	    	optional	: true,
	    	rules: [{
	    		type	: 'integer[1..50]',
	    		prompt	: 'Please enter a number of results between 1 and 50.'
	    	}]
	    },
	    
	    dateSelect: {
	    	identifier	: 'dateSelect',
	    	optional	: true,
	    	rules: [{
	    		type	: 'regExp[/^[0-9]{4}\/(0[1-9]|1[0-2])\/(0[1-9]|1[0-9]|2[0-9]|3[0-1])([ ]*-[ ]*[0-9]{4}\/(0[1-9]|1[0-2])\/(0[1-9]|1[0-9]|2[0-9]|3[0-1]))?$/]',
	    		prompt	: 'Please enter a valid date MM/DD/YYYY (or date range).'
	    	}]
	    },

		search_crit1: {
			identifier : 'search_crit1',
			rules: [{
				type   : 'empty',
				prompt : 'Please enter search criteria.'
			}]
		},

		file: {
			identifier : 'file',
			rules: [{
				type   : 'empty',
				prompt : 'Please enter a filepath.'
			}]
		},

		lineNumber: {
			identifier : 'lineNumber',
			rules: [{
				type   : 'integer',
				prompt : 'Please enter a valid line number.'
			}]
		},
	    
		linesBefore: {
	    	identifier	: 'linesBefore',
	    	optional	: true,
	    	rules: [{
	    		type	: 'integer[0..500]',
	    		prompt	: 'Please enter a valid number for lines before (0 - 500).'
	    	}]
	    },
	    
	    linesAfter: {
	    	identifier	: 'linesAfter',
	    	optional	: true,
	    	rules: [{
	    		type	: 'integer[0..500]',
	    		prompt	: 'Please enter a valid number for lines before (0 - 500).'
	    	}]
	    },		
				
    });
}