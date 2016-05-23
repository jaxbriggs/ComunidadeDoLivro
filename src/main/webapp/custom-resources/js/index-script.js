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
    
    /*
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
    })*/
    
    //Altera o label do campo de acordo com a escolha inicial
    pessoaInstRadio.ready(function (){
        if((this.checked && this.value == "pessoa") || !this.checked){ 
           lblCpfOrCnpj.html("CPF");
           cpfOrCnpj.attr("placeholder", "Digite o CPF");
           cpfOrCnpj.attr("maxlength", "14");
           mascaraCpf();
        } else {
           lblCpfOrCnpj.html("CNPJ");
           cpfOrCnpj.attr("placeholder", "Digite o CNPJ");
           cpfOrCnpj.attr("maxlength", "18");
           mascaraCnpj();
        }   
    });
    
    //Altera o label quando do radio e mudado
    pessoaInstRadio.change(function (){
        if(this.checked && this.value == "pessoa"){ 
           lblCpfOrCnpj.html("CPF");
           cpfOrCnpj.attr("placeholder", "Digite o CPF");
           cpfOrCnpj.attr("maxlength", "14");
           mascaraCpf();
        } else {
           lblCpfOrCnpj.html("CNPJ");
           cpfOrCnpj.attr("placeholder", "Digite o CNPJ");
           cpfOrCnpj.attr("maxlength", "18");
           mascaraCnpj();
        }   
    });

    //Insere mascara para o CPF
    function mascaraCpf(){
       $('#cpfOrCnpj').bind('keypress', function (event){//ao pressionar o teclado no campo cpfOrCnpj, aciona a função
            var digitos_cpf_cnpj = /^[0-9\.\-\b]$/;// identifica cada numero
            var caractercapturado = event.keyCode || event.which; //captura ação do mouse ou teclado
            if (!digitos_cpf_cnpj.test(String.fromCharCode(caractercapturado))
                                 && caractercapturado != 9 
                                 && caractercapturado != 37 && caractercapturado != 38
                                 && caractercapturado != 39 && caractercapturado != 40){ 
                alert("Digite somente números");
                return false;
            }
            var tamanho = $("#cpfOrCnpj").val().length;//000.000.000-00
            var cpf_cnpj = $("#cpfOrCnpj").val();
            if(caractercapturado !=8){    
                if (tamanho == 3){ 
                $("#cpfOrCnpj").val(cpf_cnpj+".");
                }
                if (tamanho == 7){ 
                $("#cpfOrCnpj").val(cpf_cnpj+".");
                }
                if (tamanho == 11){ 
                $("#cpfOrCnpj").val(cpf_cnpj+"-");
                }  
            }
        });
    };
    
    //Insere mascara para o CNPJ
    function mascaraCnpj(){
       $('#cpfOrCnpj').bind('keypress', function (event){//ao pressionar o teclado no campo cpfOrCnpj, aciona a função
            var digitos_cpf_cnpj = /^[0-9\.\-\/\b]$/;// identifica cada numero
            var caractercapturado = event.keyCode || event.which; //captura ação do mouse ou teclado
            if (!digitos_cpf_cnpj.test(String.fromCharCode(caractercapturado))
                                 && caractercapturado != 9 
                                 && caractercapturado != 37 && caractercapturado != 38
                                 && caractercapturado != 39 && caractercapturado != 40){ 
                alert("Digite somente números");
                return false;
            }
            var tamanho = $("#cpfOrCnpj").val().length;//99.999.999/9999-99
            var cpf_cnpj = $("#cpfOrCnpj").val();
            if(caractercapturado !=8){
                if (tamanho == 2){ 
                $("#cpfOrCnpj").val(cpf_cnpj+".");
                }
                if (tamanho == 6){ 
                $("#cpfOrCnpj").val(cpf_cnpj+".");
                }
                if (tamanho == 10){ 
                $("#cpfOrCnpj").val(cpf_cnpj+"/");
                }
                if (tamanho == 15){ 
                $("#cpfOrCnpj").val(cpf_cnpj+"-");
                } 
            }        
        });
    };
    
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
                        alert("CEP não encontrado.");
                    }
                });
            } //end if.
            else {
                //cep é inválido.
                limpa_formulario_cep();
                alert("Formato de CEP inváido.");
            }
        } //end if.
        else {
            //cep sem valor, limpa formul�rio.
            limpa_formulario_cep();
        }
    })

    //Intercepta o botao cadastrar usuario
    $('#btnCadastrarUsuario').on("click",function(event) {
        if($("#signInPassword").val().length >= 6 && $("#signInPassword2").val().length >= 6){
            if(!($("#signInPassword").val() === $("#signInPassword2").val())){
                alert("As senhas nao conferem!");
                event.preventDefault();
            }
        } else {
            alert("As senha deve conter 6 caracteres no minimo!");
            event.preventDefault();
        }
    }); 
    
    $('#btnSalvarAlteracoes').on("click",function() {
        if($("#signInPassword").val().length >= 6 && $("#signInPassword2").val().length >= 6){
            if(!($("#signInPassword").val() === $("#signInPassword2").val())){
                alert("As senhas nao conferem!");
                event.preventDefault();
            } else {
                var posting = $.post('/cadastrarUsuario', $("#signUpForm").serialize());

                posting.done(function( data ) {
                    if(data.success){
                        alert("As alteracoes foram feitas com sucesso!");
                    } else {
                        alert("Ocorreu uma falha ao alterar perfil!");
                    }
                });
            }
        } else {
            alert("As senha deve conter 6 caracteres no minimo!");
            event.preventDefault();
        }
    }); 

 });
 


       

   

