function formData(form, b){
	var arr = $('#'+form).serializeArray();
	var d = {};
	for(var i=0;i<arr.length;i++){
		if(arr[i].value && arr[i].value.length > 0){
			d[arr[i].name]=arr[i].value;
		}else if(b){
			d[arr[i].name]='[null]';
		}
	}
	return d;
}

function ajaxOption(url, data, succ){
	var a = {
		url : url,
		type : 'post',
		dataType : 'json',
		success : function(r) {
			if (r.flag != "error") {
				alert(r.result ? '操作成功（' + r.result + '）！' : '操作成功！');
				if (succ) {
					succ();
				}
			} else {
				$.messager.show({
					title : '提示',
					msg : r.error
				});
			}
		},
		error : function(r) {
			 top.location.href="login/doit";
		}
	};
	if(data){
		a.data = data;
	}
	$.ajax(a);
}

function ajaxData(url, data, fun){
	$.ajax({
		url:url,
		data:data,
		type : 'post',
		dataType : 'json',
		success : function(r){
			if(fun)fun(r);
		},
		error: function(r){
			 top.location.href="login/doit";
		}
	});
}

function dels(grid, url, fun){
	var rows = $('#'+grid).datagrid('getChecked');
	var ids = [];
	var l = rows.length;
	if(l > 0){
		confirm('您确定要删除当前所选的数据（'+l+'）？', '请确认', function(){
			for(;l--;){
				ids.push(rows[l].id);
			}
			var data = {
				ids:ids.join(',')
			};
			if(fun){
				data = fun(data);
			}
			$.ajax({
				 url:url,
				 data:data,
				 type : 'post',
				 dataType : 'json',
				 success : function(r){
					 if (r.flag == "success") {
						 $('#'+grid).datagrid();
						info('操作成功');
					}else{
						warning(r.error);
					}
				 }
			 });
		});
	}else{
		warning("请选择删除记录！");
	}
}


function isEmpty(v){
	return !v || v.length < 1 || v == '[null]';
}


var _tempLookup = [];
function lookupName(type, val){
	if(!val){
		return '';
	}
	var lookup = getLookup(type);
	if(!lookup){
		return 'undefined';
	}
	for(var i=0;i<lookup.length;i++){
		if(lookup[i].value == val){
			return lookup[i].text;
		}
	}
}

function getLookup(type, def){

	for(var i=0;i<_tempLookup.length ; i ++){
		if(_tempLookup[i].type == type){
			return _tempLookup[i].lookup;
		}
	}
	var lookup = [];
	$.ajax({
		 url:'server/lookup',
		 data:{
			 '#lookup_type-eq':type
		 },
		 type : 'post',
		 dataType : 'json',
		 async:false,
		 success : function(r){
			lookup = r.data;
		 },
		 error : function(r){
			 top.location.href="login/doit";
		 }
	});
	if(def){
		for(var i=0;i<lookup.length;i++){
			if(lookup[i].value == def){
				lookup[i].selected = true;
			}
		}
	}
	_tempLookup.push({
		type:type,
		lookup:lookup
	})
	return lookup;
}
