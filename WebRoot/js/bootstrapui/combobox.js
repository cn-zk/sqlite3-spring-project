(function($){
	function create(target){
		var _this = $(target);
		var state = $.data(target, 'combobox');
		
		var opts = _this.attr('data-options');
		_this.html('');
		if(opts){
			opts = eval('({'+opts+'})');
			if(opts.lookup){
				var t = getLookup(opts.lookup);
				for(var i=0;i<t.length;i++){
					$('<option value="'+t[i].value+'">'+t[i].text+'</option>').appendTo(_this);
				}
			}
			if(opts.onSelect){
				_this.on('change', function(){
					var os = _this.find("option:selected");
					opts.onSelect({value:os.val(), text:os.text()});
				});
			}
		}
	};
	$.fn.combobox = function(options, param){
		if (typeof options == 'string'){
			var method = $.fn.combobox.methods[options];
			if (method){
				return method(this, param);
			}else {
				throw 'not find method:'+options;
			}
		}

		options = options || {};
		
		return this.each(function(){
			var state = $.data(this, 'combobox');
			if (state){
				$.extend(state.options, options);
			} else {
				state = $.data(this, 'combobox', {
					options: $.extend({}, $.fn.combobox.defaults, options)
				});
			}
			create(this);
		});
	}
	$.fn.combobox.methods = {
	};
})(jQuery);
