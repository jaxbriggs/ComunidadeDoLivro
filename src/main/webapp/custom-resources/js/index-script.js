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
    
    
    
    /*PREECHIMENTO DO ENDERECO PELO CEP*/
    function limpa_formulario_cep() {
    // Limpa valores do formulário de cep.
    $("#rua").val("");
    $("#bairro").val("");
    $("#cidade").val("");
    $("#uf").val("");
    $("#ibge").val("");
    }

    //Quando o campo cep perde o foco.
    $("#cep").blur(function() {

        //Nova variável "cep" somente com dígitos.
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
                        alert("CEP não encontrado.");
                    }
                });
            } //end if.
            else {
                //cep é inválido.
                limpa_formulario_cep();
                alert("Formato de CEP inválido.");
            }
        } //end if.
        else {
            //cep sem valor, limpa formulário.
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
 


       

   

