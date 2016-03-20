$(document).ready(function(){
    
        //Faz requisicao do login
        $("#signInForm").submit(function( event ) {
            event.preventDefault();

            var posting = $.post('/login', $("#signInForm").serialize());

            posting.done(function( data ) {
                if(data !== null){
                    $.redirect("/home.jsp", data);
                } else {
                    $('#myModal').modal('toggle');
                }
            });
        });
    
});