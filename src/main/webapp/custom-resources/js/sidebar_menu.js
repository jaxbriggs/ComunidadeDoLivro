$("#menu-toggle").click(function(e) {
    e.preventDefault();
    $("#wrapper").toggleClass("toggled");
});
 $("#menu-toggle-2").click(function(e) {
    e.preventDefault();
    $("#wrapper").toggleClass("toggled-2");
    $('#menu ul').hide();
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
}

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
    $('.active').removeClass('active');
    $(element).addClass('active');
    $("#page-content-wrapper").html('<object data="' + pageLoadedPath + '" class="col-xs-12" id="object_data" >');
};
    
$(document).ready(function() {
    initMenu();
});


