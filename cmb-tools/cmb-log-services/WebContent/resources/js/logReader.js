$(document)
  .ready(function() {

    $(".ui.dropdown")
      .dropdown()
    ;
    
    $('.ui.form')    
    .form ({
    	file: {
    		identifier  : 'file',
    	    	rules: [
    	    	    {
    	    	    	type   : 'empty',
    	    	    	prompt : 'Please enter a filepath.'
    	        	}
    	        ]
    	    },
    	    
    	    lineNum: {
    	    	identifier	: 'lineNumber',
    	      	rules: [
    	      	{
    	          type   : 'integer',
    	          prompt : 'Please enter a valid line number.'
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
    	    
    	    before: {
      	      identifier  : 'before',
      	      optional	: true,
      	      	rules: [
      	      	{
      	          type   : 'integer[0..500]',
      	          prompt : 'Please enter a valid number for lines before (0 - 500).'
      	        }
      	      ]
      	    },
      	    
      	  after: {
    	      identifier  : 'after',
    	      optional	: true,
    	      	rules: [
    	      	{
    	          type   : 'integer[0..500]',
    	          prompt : 'Please enter a valid number for lines after (0 - 500).'
    	        }
    	      ]
    	    },
    	    
    })
        
    $('.ui.clear.button') .click (function() {
    	$('.ui.form.error').removeClass('error');
    	$('.error.message .list').remove();
    });
    
  })
;

