$(document).ready(function() {

	$(".ui.dropdown").dropdown();
    
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
    			   },
    			   error: function(xhr,status,error) {
    				   //alert(error);
    			   }    			   
    			});
    	}
    });
        
    $('.ui.form').form ({
		env_Submit: {
			identifier  : 'env_Submit',
		    	rules: [
		    	    {
		    	    	type   : 'empty',
		    	    	prompt : 'Please select an environment.'
		    	    }
		    	]
			},

	    ejb_Submit: {
	      identifier	: 'ejb_Submit',
	      	rules: [
	      	{
	          type   : 'empty',
	          prompt : 'Please select an EJB.'
	        }
	      ]
	    },
	    
	    search_crit1: {
  	      identifier	: 'search_crit1',
  	      	rules: [
  	      	{
  	          type   : 'empty',
  	          prompt : 'Please enter search criteria.'
  	        }
  	      ]
  	    },
  	    
  	    user: {
  	    	identifier	: 'user',
  	    	rules: [
  	    	{
  	    		type	: 'empty',
  	    		prompt	: 'Please enter your T or X-ID.'
  	    	}
  	    	]
  	    },
  	    
  	    pass: {
	    	identifier	: 'pass',
	    	rules: [
	    	{
	    		type	: 'empty',
	    		prompt	: 'Please enter your password.'
	    	}
	    	]
	    },

  	    app: {
	    	identifier	: 'app',
	    	rules: [
	    	{
	    		type	: 'empty',
	    		prompt	: 'Please select an application.'
	    	}
	    	]
	    },
	    
	    numResults: {
	    	identifier	: 'numResults',
	    	optional	: true,
	    	rules: [
	    	{
	    		type	: 'integer[1..50]',
	    		prompt	: 'Please enter a number of results between 1 and 50.'
	    	}
	    	]
	    },
    })
    
    .form ("clear");
    
    $('.ui.clear.button') .click (function() {
    	$('.ui.form.error').removeClass('error');
    	$('.error.message .list').remove();
    });
    
    $('.ui.form .unix.field').popup({
		on: 'hover'
	});
    
    $('.authentication.message .close')
    .on('click', function() {
      $(this)
        .closest('.authentication.message')
        .transition('fade')
      ;
    });

  });

