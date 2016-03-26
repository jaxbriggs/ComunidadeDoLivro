$(document).ready(function(){
    
    //LABEL
    var lblCpfOrCnpj = $("#lblCpfOrCnpj");
    
    //INPUTS
    var login = $("#login");
    var password = $("#password");
    var name = $("#name");
    var email = $("#userEmail");
    var userLogin = $("#userLogin");
    var signInPassword = $("#signInPassword");
    var signInPassword2 = $("#signInPassword2");
    var cep = $("#cep");
    var rua = $("#rua");
    var bairro = $("#bairro");
    var cidade = $("#cidade");
    var uf = $("#uf");
    var pessoaInstRadio = $('input:radio[name="pessoa-inst"]');
    var cpfOrCnpj = $("#cpfOrCnpj");
    var tel = $("#tel");
    
    function limparCampos(){
        login.val('');
        password.val('');
        name.val('');
        email.val('');
        userLogin.val('');
        signInPassword.val('');
        signInPassword2.val('');
        cep.val('');
        rua.val('');
        cpfOrCnpj.val('');
        bairro.val('');
        cidade.val('');
        tel.val('');
        uf.val('');
        $("#pessoaRadio").prop("checked", true)
    }
    
    $("#signUpForm").ready(function (){
        $("#signUpForm").css("display", "none")
    })
    
    //Altera o label do campo de acordo com a escolha inicial
    pessoaInstRadio.ready(function (){
        if((this.checked && this.value == "pessoa") || !this.checked){ 
           lblCpfOrCnpj.html("CPF");
           cpfOrCnpj.attr("placeholder", "Digite o CPF");
        } else {
           lblCpfOrCnpj.html("CNPJ");
           cpfOrCnpj.attr("placeholder", "Digite o CNPJ");
        }   
    });
    
    //Altera o label quando do radio e mudado
    pessoaInstRadio.change(function (){
        if(this.checked && this.value == "pessoa"){ 
           lblCpfOrCnpj.html("CPF");
           cpfOrCnpj.attr("placeholder", "Digite o CPF");
        } else {
           lblCpfOrCnpj.html("CNPJ");
           cpfOrCnpj.attr("placeholder", "Digite o CNPJ");
        }   
    });
    
    
    //Altera o form exibido conforme o link de logar/cadastrar e clicado 
    $("#signInOrUp").text("Clique para se cadastrar");
    $("#signInUpFormTitle").text("Fa�a o login");
    $("#signInOrUp").click(function(){
        if($("#signInForm").css("display") != "none"){
            $("#signInUpFormTitle").text("Fa�a o cadastro");
            $("#signInOrUp").text("Clique para fazer o login");
            $("#signInForm").css("display","none");
            $("#signUpForm").css("display","inline");
            limparCampos();
        } else {
            $("#signInUpFormTitle").text("Fa�a o login");
            $("#signInOrUp").text("Clique para se cadastrar");
            $("#signInForm").css("display","inline");
            $("#signUpForm").css("display","none");
            limparCampos();
        }
    })
    
    
    /*Valida o campo name conforme o banco*/
    $('#name').bind('keypress', function (event){//ao pressionar o teclado no camo name, aciona a função
       var validacaracter = /^[a-zA-Z \b]$/;// new RegExp("^[a-zA-Z \b]+$") constroi a expressao regular que identifica letra e espaço
       var caractercapturado = event.keyCode || event.which; //captura ação do mouse ou teclado
       //lista de keyCode, 9 do tab e 37 a 40 setas        
        if (!validacaracter.test(String.fromCharCode(caractercapturado)) && caractercapturado != 9 
                                 && caractercapturado != 37 && caractercapturado != 38
                                 && caractercapturado != 39 && caractercapturado != 40){ 
        //event.preventDefault();
        alert("O nome deve conter apenas letras.");
        return false;
        }
    });

    
    
    
    
    /*PREECHIMENTO DO ENDERECO PELO CEP*/
    function limpa_formulario_cep() {
    // Limpa valores do formulario de cep.
    $("#rua").val("");
    $("#bairro").val("");
    $("#cidade").val("");
    $("#uf").val("");
    $("#ibge").val("");
    }

    //Quando o campo cep perde o foco.
    $("#cep").blur(function() {

        //Nova variável "cep" somente com digitos.
        var cep = $(this).val().replace(/\D/g, '');

        //Verifica se campo cep possui valor informado.
        if (cep != "") {

            //Expressão regular para validar o CEP.
            var validacep = /^[0-9]{8}$/;

            //Valida o formato do CEP.
            if(validacep.test(cep)) {
                //Preenche os campos com "..." enquanto consulta webservice.
                $("#rua").val("...")
                $("#bairro").val("...")
                $("#cidade").val("...")
                $("#uf").val("...")
                $("#ibge").val("...")

                //Consulta o webservice viacep.com.br/
                $.getJSON("//viacep.com.br/ws/"+ cep +"/json/?callback=?", function(dados) {
                    if (!("erro" in dados)) {
                        //Atualiza os campos com os valores da consulta.
                        $("#rua").val(dados.logradouro);
                        $("#bairro").val(dados.bairro);
                        $("#cidade").val(dados.localidade);
                        $("#uf").val(dados.uf);
                        $("#ibge").val(dados.ibge);
                    } //end if.
                    else {
                        //CEP pesquisado não foi encontrado.
                        limpa_formulario_cep();
                        alert("CEP n�o encontrado.");
                    }
                });
            } //end if.
            else {
                //cep é inválido.
                limpa_formulario_cep();
                alert("Formato de CEP inv�ido.");
            }
        } //end if.
        else {
            //cep sem valor, limpa formul�rio.
            limpa_formulario_cep();
        }
    })
    
 });
 
 /* $.ajax({
            type: "POST",
            url: "YOURURL",
            data:{
                edited_question: editedQuestionObj,
                question: editedQuestionId
            },
            success: function(){
                modalObj.dialog('close');
                modalObj.html('');
            },
            complete: function(){
                //window.location.reload(true);
            }
        });*/
 


       

   

