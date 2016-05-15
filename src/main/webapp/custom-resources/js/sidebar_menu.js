$("#menu-toggle").click(function(e) {
    e.preventDefault();
    $("#wrapper").toggleClass("toggled");
});
 $("#menu-toggle-2").click(function(e) {
    e.preventDefault();
    $("#wrapper").toggleClass("toggled-2");
    $('#menu ul').hide();
});

//Minhas funcoes

//Pega o item do menu clicado e set no campo hidden
function setPick(pick){
    $('#pick').val(pick);
};

//Enviar a escolha do menu
function submitPickSenderForm(){  
  $('#menuPickSenderForm').submit();
};

function loadPageByPickedMenu(element, pageLoadedPath){
    if($("#pick").val() === 'sair'){
        $.redirect("/index.jsp", null);
    }
    $('.active').removeClass('active');
    $(element).addClass('active');
    $("#page-content-wrapper").html('<object data="' + pageLoadedPath + '" class="col-xs-12" id="object_data" style="padding: 0; margin: 0;">');
};

//Reseta informacoes do modal quando cancelado
$('.modal').on('hidden.bs.modal', function(){
    $(this).find('form')[0].reset();
});

//Abre o modal de login quando clicado
$('#login_btn').click(function(e) {
    $('#modal_login').modal('toggle');
});

//Quando o modal login e aberto focar no campo de login
$('#modal_login').on('shown.bs.modal', function () {
  $('#login').focus()
})

//Abre o modal do cadstro
$('#cadastro_btn').click(function(e) {
    $('#modal_cadastro').modal('toggle');
});

 function initMenu() {
  $('#menu ul').hide();
  $('#menu ul').children('.current').parent().show();
  //$('#menu ul:first').show();
  $('#menu li a').click(
    function() {
      var checkElement = $(this).next();
      if((checkElement.is('ul')) && (checkElement.is(':visible'))) {
        return false;
        }
      if((checkElement.is('ul')) && (!checkElement.is(':visible'))) {
        $('#menu ul:visible').slideUp('normal');
        checkElement.slideDown('normal');
        return false;
        }
      }
    );
    
   if($("#userId").val() === ""){
        $("#menu-toggle-2").css("display", "none");
    } else {
        $("#menu-toggle-2").css("display", "inline");
    }
    
    $("#imgUserConfigs").click(function () {
        var dataContent = "<button type=\"button\" class=\"btn btn-primary\" onClick=\"loadPageByPickedMenu('#configuracoes', '/configuracoes')\">Configurações</button>";
        $(this).attr('data-content', dataContent);
        $(this).popover("toggle");
    });
}

$(document).ready(function() {
    initMenu();
    loadPageByPickedMenu('#menu_livros_em_doacao', '/sendo_doados');
});
