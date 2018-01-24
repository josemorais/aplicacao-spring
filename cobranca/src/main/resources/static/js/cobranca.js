$('#confirmacaoExclusaoModal').on('show.bs.modal', function(event) {
	var button = $(event.relatedTarget); // Botão que acionou o modal
	var codigoTitulo = button.data('codigo'); // Extrai as informações dos atributos data-*
	var descricaoTitulo = button.data('descricao');
	
	var modal = $(this);
	var form = modal.find('form');
	var action = form.data('url-base');
	if (!action.endsWith('/')){
		action += '/';
	}
	form.attr('action', action + codigoTitulo);
	
	modal.find('.modal-body span').html('Tem certeza que deseja excluir o título <strong>'+ descricaoTitulo + '</strong>?');
})

$("#success-alert").fadeTo(2000, 500).slideUp(500, function(){
    $("#success-alert").slideUp(500);
});

$(function() {

	$('[rel="tooltip"]').tooltip();
	$('.js-currency').maskMoney({
		thousands: '.',
		decimal: ',',
		allowZero: true
	})
	
	$('.js-atualizar-status').on('click', function(event){
		event.preventDefault();//Remove a configuração padrão do botão
		
		var botaoReceber = $(event.currentTarget);
		var urlReceber = botaoReceber.attr('href');
		
		$.ajax({
			url: urlReceber,
			type: 'PUT',
			success: function(response) {
					var codigoTitulo = botaoReceber.data('codigo');
					$('[data-role='+ codigoTitulo + '] span').removeClass('label-danger').addClass('label-primary').html(response);
					botaoReceber.hide();
					$('[role="tooltip"]').hide();
			},
			error: function(xhr, ajaxOptions, thrownError) {
		        alert('Erro ao receber titulo' + ajaxOptions);
		    },
		});
		
	})
	
	
	$('input[name=descricao]').on('keyup', function(e){
		e.preventDefault();
		
		var baseUrl = document.location.origin;
		var filtroDescricao = $(this).val();
		var action = $('#form-pesquisa').attr('action');
		action = action + '/ajax' + '?descricao=' + filtroDescricao;
		window.history.pushState(null, null, baseUrl + action);
		
		var retorno = $.ajax({
			url: action,
			type: 'GET',
			contentType: 'application/json',
			dataType: 'json'
		})
		
		retorno.done(function(view){
			var htmlTabelaPesquisa = $('#table-template').html();
			var template = Handlebars.compile(htmlTabelaPesquisa);
			
			
			$("tr:has(td)").remove();
		//	var trHTML = '';
	        $.each(view, function (i, item) {
	        	var context = {"codigo": item.codigo, "descricao": item.descricao, "dataVencimento": item.dataVencimento};
			      // Generate the HTML for the template
	        	
			    var html = template(context);
			    console.log(html);
				 $('#records_table').html(html);
//	            trHTML += '<tr><td class="text-center">' + item.codigo + '</td><td>'
//	            										 + item.descricao + '</td><td>' 
//	            										 + item.dataVencimento + '</td><td>' 
//	            										 + item.valor + '</td><td>' 
//	            										 + '<span class="label label-primary">' 
//	            										 + item.status + '</span></td><td>'
//											             + '<a class="btn btn-link btn-xs" rel="tooltip" data-placement="top" title="Editar"'
//											             + 'href="/titulos/' + item.codigo + '">'
//											             + '<span class="glyphicon glyphicon-pencil" aria-hidden="true"></span></a>'
//											             + '<a class="btn btn-link btn-xs" rel="tooltip" data-placement="top" title="Excluir"'
//											             + ' data-target="#confirmacaoExclusaoModal" data-codigo="' + item.codigo + '" data-descricao="' + item.descricao +'">'
//											             + '<span class="glyphicon glyphicon-remove" aria-hidden="true"></span></a></td></tr>'
//	            										
//	            console.log(item);
	        });
			
	        
		})
		retorno.fail(function(error){
			console.log('fail' + error);
			alert('Erro ao buscar título!');
		})
		
	});
	
});

