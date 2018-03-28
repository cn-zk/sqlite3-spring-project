<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>


<div class="modal fade" id="dialog-modal-confirm" tabindex="-1">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title">Confirm Panel</h4>
			</div>
			<div class="modal-body"></div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				<button type="button" class="btn btn-primary" data-dismiss="modal" >confirm</button>
			</div>
		</div><!-- /.modal-content -->
	</div><!-- /.modal-dialog -->
</div><!-- /.modal -->
<div class="modal fade" id="dialog-modal-view" tabindex="-1">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title">Modal Panel</h4>
			</div>
			<div class="modal-body">
				<iframe frameborder="0" style="width: 100%;min-height:400px;"></iframe>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				<button type="button" class="btn btn-primary">Save changes</button>
			</div>
		</div><!-- /.modal-content -->
	</div><!-- /.modal-dialog -->
</div><!-- /.modal -->
<div class="modal fade" id="dialog-modal-info" tabindex="-1">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="panel-info">
				<div class="panel-heading">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
					<h3 class="panel-title">Panel Info</h3>
				</div>
				<div class="panel-body"></div>
			</div>
		</div>
	</div>
</div>
<div class="modal fade" id="dialog-modal-warning" tabindex="-1">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="panel-warning">
				<div class="panel-heading">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
					<h3 class="panel-title">Panel Warning</h3>
				</div>
				<div class="panel-body"></div>
			</div>
		</div>
	</div>
</div>


<script type="text/javascript">
function opend(t, src){
	var d = $('#dialog-modal-view').modal({backdrop:false});
	if(t){
		$('#dialog-modal-view div div div h4').html(t);
	}else{
		$('#dialog-modal-view div div div h4').html('Modal Panel');
	}
	$('#dialog-modal-view div div div iframe').attr('src', src);
	$('#dialog-modal-view div div div button[class="btn btn-primary"]').one('click',function(){
		var s = $('#dialog-modal-view div div div iframe')[0].contentWindow.onSave;
		if(s && s()){
			d.modal('hide')
		}
	});
	$('#dialog-modal-view').modal('show');
}
function confirm(s, t, fun){
	var d = $('#dialog-modal-confirm');
	if(t){
		$('#dialog-modal-confirm div div div div h3').html(t);
	}else{
		$('#dialog-modal-confirm div div div div h3').html('Confirm Panel');
	}
	$('#dialog-modal-confirm div div div[class="modal-body"]').html(s);
	
	$('#dialog-modal-confirm div div div button[class="btn btn-primary"]').one('click',fun);
	$('#dialog-modal-confirm').modal('show');
}
function info(s, t){
	var d = $('#dialog-modal-info');
	if(t){
		$('#dialog-modal-info div div div div h3').html(t);
	}else{
		$('#dialog-modal-info div div div div h3').html('Panel Info');
	}
	$('#dialog-modal-info div div div div[class="panel-body"]').html(s);
	$('#dialog-modal-info').modal('show');
}
function warning(s, t){
	var d = $('#dialog-modal-warning');
	if(t){
		$('#dialog-modal-warning div div div div h3').html(t);
	}else{
		$('#dialog-modal-warning div div div div h3').html('Panel Warning');
	}
	$('#dialog-modal-warning div div div div[class="panel-body"]').html(s);
	$('#dialog-modal-warning').modal('show');
}
</script>