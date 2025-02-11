$(document).ready(function() {
	
	function checkboxHighlight () {		
		$('.highlighter .search1.checkbox').checkbox({			
			onChecked: function () {
				$('.line').highlight($(this).val(), 'highlight1');
			},			
			onUnchecked: function () {
				$('.line').removeHighlight('highlight1');
			}		
		});
		
		$('.highlighter .search2.checkbox').checkbox({			
			onChecked: function () {
				$('.line').highlight($(this).val(), 'highlight2');
			},			
			onUnchecked: function () {
				$('.line').removeHighlight('highlight2');
			}		
		});
		
		$('.highlighter .search3.checkbox').checkbox({			
			onChecked: function () {
				$('.line').highlight($(this).val(), 'highlight3');
			},			
			onUnchecked: function () {
				$('.line').removeHighlight('highlight3');
			}		
		});
		
		$('.highlighter .search4.checkbox').checkbox({		
			onChecked: function () {
				$('.line').highlight($(this).val(), 'highlight4');
			},			
			onUnchecked: function () {
				$('.line').removeHighlight('highlight4');
			}		
		});
				
		$('.highlighter .button')
		  .on('click', function() {
		    $('.highlighter .checkbox').checkbox( $(this).data('method') );
		  }
		);		
	}
	
	checkboxHighlight();
	
	$(".content .search").each( function (index) {
		$(this).on("click", function(){
			var filepath = $(this).parent().parent().parent().find('.file.header').text();
			var line = $(this).data('value');
			var url = $(this).parent().parent().parent().find('.result.url').text() + "?filePath=" + encodeURIComponent(filepath) + "&lineNumber=" + encodeURIComponent(line);
			window.open(url);				
		});
    });
    
	$('.ui.sticky').sticky({
		offset: 20
	});
	
	$('.minimizing.button').click(function () {
		$('.highlighter.segment').after("<button class='ui maximizing icon button'><i class='add square icon'></i></button>");
		$segment = $('.highlighter.segment').detach();
		
		$('.maximizing.button').bind('click', function () {
			$(this).after($segment[0]);
			$(this).remove();
			checkboxHighlight();
		});
		
	});
	
	$('.content .info.circle.icon').popup({
		on: 'hover'
	});
	
});