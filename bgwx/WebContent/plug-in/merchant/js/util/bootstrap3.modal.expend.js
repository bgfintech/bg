	

	
	(function($){
		$.fn.reload = function(){
			$(this).modal('hide');
			$(this).on('hidden.bs.modal',  function(){
					window.location.reload();
			});
		};
		
	})(jQuery)
	
	
	var reload = function(){
		window.location.reload();
	}
	
