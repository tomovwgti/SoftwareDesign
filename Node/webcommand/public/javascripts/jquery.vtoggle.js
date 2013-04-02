(function($){
	$.fn.vtoggle = function(config){
		var $this = $(this);

		switch ($this.css("visibility")){
			case "visible":
				$this.css("visibility","hidden");
				break;
			case "hidden":
				$this.css("visibility","visible");
				break;
			default:
				//何もしない
			break;
		}

		return $this; //メソッドチェーンにreturn
	};
})(jQuery);
