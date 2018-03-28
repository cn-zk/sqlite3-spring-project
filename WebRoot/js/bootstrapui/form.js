(function($){

	$.fn.form = function(options, param){
		if (typeof options == 'string'){
			var method = $.fn.form.methods[options];
			if (method){
				return method(this, param);
			}else {
				throw 'not find method:'+options;
			}
		}
		return this;
	}
	$.fn.form.methods = {
		load:function(target, j){
			var _this = $(target);
			for(var en in j){
				var o = _this.find('input[name="'+en+'"]');
				if(o.length){
					o.val(j[en]);
				}else{
					o = _this.find('select[name="'+en+'"]');
					if(o.length){
						o.val(j[en]);
					}
				}
				
			}
			return target;
		}
	};
})(jQuery);
