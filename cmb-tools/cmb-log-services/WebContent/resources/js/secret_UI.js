$(document)
  .ready(function() {
	  
	    var cthulhu_bool = false;
	    var hodor_bool = false;
	    var hue_bool = false;
	    
	    $("#secret").click (function() {	    	
	    	img_dir = window.location.pathname.substring(0, window.location.pathname.lastIndexOf("/")) + '/resources';
	    	if (cthulhu_bool == false) {
	    		$("#panda").after('<img class="ui medium centered cthulhu image" src="' + img_dir + '/images/logSearch/CthulhuChibi_1.jpg"/>');
	    		$("#monkey").after('<img class="ui medium left floated cthulhu image" src="' + img_dir + '/images/logSearch/CthulhuChibi_2.jpg"/>');
	    		$("#bunny").after('<img class="ui medium centered cthulhu image" src="' + img_dir + '/images/logSearch/CthulhuChibi_3.jpg"/>');
	    		$("#lion").after('<img class="ui medium centered cthulhu image" src="' + img_dir + '/images/logSearch/CthulhuChibi_3.jpg"/>');
	    		$image = $('.ui.animal.image').detach();
	    		cthulhu_bool = true;
	    	}
	    	
	    	else {
	    		$('.ui.cthulhu.image').each (function (i) {
	    			$(this).after($image[i]);
	    		})
	    		$('.ui.cthulhu.image').remove();
	    		cthulhu_bool = false;
	    	}
	    }),
	    
	    $(".ui.form").submit (function(event) {
	    	if ($("[name=search_crit4]").val().toUpperCase() == "GAME OF THRONES") {
	    		event.preventDefault();
	    		
	    		if (hodor_bool == false) {
	    			$('.in_header').after('<label class="hodor_head">Hodor Hodor Hodor Hodor Hodor Hodor</label>');
	    			$header = $('.in_header').detach();
	    			
	    			$('.ui.error.message').after('<div class="hodor_message">HODOR HODOR HODOR</div>');
	    			$error = $('.ui.error.message').detach();
	    			
	    			
	    			hodor_bool = true;
	    		}
	    		
	    		else {
	    			$('.hodor_head').each (function(i) {
	    				$(this).after($header[i]);
	    			})
	    			$('.hodor_head').remove();
	    			
	    			
	    			$('.hodor_message').after($error[0]);
	    			$('.hodor_message').remove();
	    			
	    			hodor_bool = false;
	    		}
	    		
	    	}
	    
	    	if ($('[name=search_crit1]').val().toUpperCase() == "HUE" &&
	    			$('[name=search_crit2]').val().toUpperCase() == "HUE" &&
	    			$('[name=search_crit2]').val().toUpperCase() == "HUE") {
	    		event.preventDefault();
	    		
	    		if (hue_bool == false) {
	    			$('#bunny').parent()
	    			.after("<div class='six wide huehue field'><img class='ui massive right floated image' " +
	    					"id='huehue' src='" + img_dir + "/images/logSearch/HueingIntensifies.gif'/></div>");
	    			hue_bool = true;
	    		}
	    		
	    		else {
	    			$('.huehue').remove();
	    			hue_bool = false;
	    		}
	    		
	    	}
	    }),
	    
	    $('.ui.clear.button') .click (function() {
	    	$('.huehue').remove();
	    })
	  
  })
;