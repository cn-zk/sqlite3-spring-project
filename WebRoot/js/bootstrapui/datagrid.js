(function($){
	function getOptions(target){
		var _this = $(target);
		var opts = _this.attr('data-options');
		if(opts){
			opts = eval('({'+opts+'})');
			
			if(opts.queryParams){
				opts.queryParams = eval('('+opts.queryParams+')')();
			}
			if(opts.sort){
				opts.queryParams.sort = opts.sort;
			}
			if(opts.order){
				opts.queryParams.order = opts.order;
			}
		}
		return opts;
	}
	
	function create(target){
		var _this = $(target);
		var state = $.data(target, 'datagrid');
		var opts = $.extend({}, state.options, getOptions(target));
		
		state.cells = {};
		var ths = _this.find('thead tr th');
		for(var i=0;i<ths.length;i++){
			var cts = eval('({'+$(ths[i]).attr('data-options')+'})');
			if(i==0 && cts.type == 'checkbox'){
				$(ths[i]).html('');
				var cbo = $('<div class="inbox-page"><input type="checkbox" class="checkbox"></div>');
				cbo.find('input[type="checkbox"]').on('click', function(){
					var box_v = $(this).attr('checked');
					if(box_v){
						$(this).removeAttr('checked')
					}else{
						$(this).attr('checked', 'checked');
					}
					var tds_ = _this.find('tr td div input.checkbox');
					for(var td_i=0;td_i<tds_.length;td_i ++){
						var td_v = $(tds_[td_i]).attr('checked');
						if((box_v && td_v) || (!box_v && !td_v)){
							$(tds_[td_i]).click();
						}
					}
				});
				cbo.appendTo($(ths[i]));
			}
			state.cells[cts['field']]=cts;
		}
		_this.find('tbody').remove();
		ajaxData(
			opts.url,
			opts.queryParams,
			function(r){
				if(r.error){
					top.location.href="login/doit";
				}
				$.data(target, 'datagrid').data.rows = r.rows;
				var tb =$('<tbody></tbody>').appendTo(_this);
				for(var i=0;i<r.rows.length;i++){
					var tdr = '<tr>', tv;
					for(var en in state.cells){
						if(en == 'sn'){
							tdr += '<td scope="row">';
							if(state.cells[en].type == 'checkbox'){
								tdr += '<div class="inbox-page"><input type="checkbox" class="checkbox" onClick="javascript:if($(this).attr(\'checked\')){$(this).removeAttr(\'checked\');}else{$(this).attr(\'checked\',\'checked\');}"></div>';
							}else{
								tdr += (i+1);
							}
							tdr += '</td>';
						}else{
							tv = r.rows[i][en];
							if(state.cells[en].formatter){
								tdr += '<td>'+state.cells[en].formatter(tv, r.rows[i])+'</td>';
							}else{
								if(!tv){
									tv = '';
								}
								tdr += '<td>'+tv+'</td>';
							}
						}
					}
					tdr += '</tr>';
					$(tdr).appendTo(tb);
				}
			}
		);
	
	}
	
	$.fn.datagrid = function(options, param){
		if (typeof options == 'string'){
			var method = $.fn.datagrid.methods[options];
			if (method){
				return method(this[0], param);
			}else {
				throw 'not find method:'+options;
			}
		}
		
		options = options || {};

		return this.each(function(){
			var state = $.data(this, 'datagrid');
			if (state){
				$.extend(state, options);
			} else {
				state = $.data(this, 'datagrid', $.extend({}, $.fn.datagrid.defaults, options));
			}
			create(this);
		});
	}
	
	$.fn.datagrid.methods = {
		getChecked:function(target){

			var _t = $(target);
			var tds_ = _t.find('tr td div input.checkbox');
			var rows = [];
			for(var td_i=0;td_i<tds_.length;td_i ++){
				var td_v = $(tds_[td_i]).attr('checked');
				if(td_v){
					rows.push($.data(target, 'datagrid').data.rows[td_i]);
				}
			}
			return rows;
		}
	};
	
	$.fn.datagrid.defaults = {
		data:{
			rows:[]
		}
	};
})(jQuery);

$(function(){
	
	$('.b-combobox').combobox();
	$('.b-datagrid').datagrid();
	
});