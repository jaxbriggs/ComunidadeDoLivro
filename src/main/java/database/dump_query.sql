CREATE TABLE comunidade_do_livro.LIVRO(
      cd_livro 	SERIAL,
      cd_isbn_livro   		VARCHAR,
      nm_titulo_livro 		VARCHAR,
      nm_autor_livro  		VARCHAR,
      nm_editora_livro		VARCHAR,
      dt_publicacao_livro	VARCHAR,
      ds_livro			VARCHAR,
      qt_paginas_livro		INTEGER,
      nm_genero_livro		VARCHAR,
      im_capa_livro		VARCHAR,
      nm_idioma_livro		VARCHAR,
      CONSTRAINT pk_cd_livro PRIMARY KEY(cd_livro)
);

/*
CREATE TABLE comunidade_do_livro.STATUS(
      cd_status SERIAL,
      nm_status VARCHAR(100),
      CONSTRAINT pk_cd_status PRIMARY KEY(cd_status)
);
*/

CREATE TABLE comunidade_do_livro.ENDERECO(
      cd_endereco		      SERIAL,
      cd_codigo_postal_endereco	      VARCHAR(20) NOT NULL,	
      nm_rua_endereco		      VARCHAR(150) NOT NULL, 	
      cd_numero_endereco	      INTEGER NOT NULL, 
      nm_bairro_endereco              VARCHAR(150) NOT NULL,
      nm_cidade_endereco	      VARCHAR(150) NOT NULL,	
      sg_unidade_federativa_endereco  CHAR(2) NOT NULL,
      CONSTRAINT pk_cd_endereco PRIMARY KEY(cd_endereco)	
);

CREATE TABLE comunidade_do_livro.USUARIO(
      cd_usuario		SERIAL,
      cd_endereco_usuario       INTEGER,
      nm_usuario		VARCHAR(50),
      nm_login_usuario		VARCHAR(50),
      nm_email_usuario		VARCHAR(50),
      cd_senha_usuario	        VARCHAR NOT NULL,
      ic_ativo_usuario_sim_nao  BOOLEAN NOT NULL,
      ic_admin_usuario_sim_nao  BOOLEAN NOT NULL,
      cd_cpf_usuario		VARCHAR(20),
      cd_cnpj_usuario		VARCHAR(20),	
      cd_telefone_usuario	VARCHAR(20),	
      cd_celular_usuario	VARCHAR(20),
      dt_cadastro_usuario       TIMESTAMP,
      CONSTRAINT pk_cd_usuario PRIMARY KEY(cd_usuario),
      FOREIGN KEY (cd_endereco_usuario) REFERENCES comunidade_do_livro.ENDERECO (cd_endereco)  
);

CREATE TABLE comunidade_do_livro.TRANSACAO(
      cd_transacao			SERIAL,
      cd_doador_usuario_transacao	INTEGER,
      cd_donatario_usuario_transacao	INTEGER,
      cd_usuario_cadastrante            INTEGER,
      cd_livro_transacao		INTEGER NOT NULL,
      ic_transacao_finalizada_sim_nao	BOOLEAN,
      ic_transacao_autorizada_sim_nao	BOOLEAN,
      ic_transacao_ativa_sim_nao	BOOLEAN,
      qt_livro_transacao      		INTEGER,
      ds_observacao_livro_transacao	VARCHAR(200),
      dt_cadastro_transacao             TIMESTAMP,
      dt_transacao_finalizada           TIMESTAMP,      
      CONSTRAINT pk_cd_transacao PRIMARY KEY(cd_transacao),
      FOREIGN KEY (cd_doador_usuario_transacao) REFERENCES comunidade_do_livro.USUARIO (cd_usuario),
      FOREIGN KEY (cd_donatario_usuario_transacao) REFERENCES comunidade_do_livro.USUARIO (cd_usuario),
      FOREIGN KEY (cd_livro_transacao) REFERENCES comunidade_do_livro.LIVRO (cd_livro)
      --FOREIGN KEY (cd_status_transacao) REFERENCES comunidade_do_livro.STATUS (cd_status)
);

CREATE TABLE comunidade_do_livro.COMENTARIO(
      cd_comentario                       SERIAL,
      cd_transacao_comentario             INTEGER NOT NULL,
      dt_inclusao_comentario_transacao	  TIMESTAMP,
      cd_usuario_comentario               INTEGER, 
      dt_ultima_edicao_comentario         TIMESTAMP,
      FOREIGN KEY (cd_transacao_comentario) REFERENCES comunidade_do_livro.TRANSACAO (cd_transacao)	
);

CREATE TABLE comunidade_do_livro.CREDENCIAL
(
  refresh VARCHAR,
  access VARCHAR
);

INSERT INTO comunidade_do_livro.CREDENCIAL(refresh, access)
VALUES('1/_V4K53nYXfRXgv0UsKNSORYc3mbfKToKUPsUdCCTu7MMEudVrK5jSpoR30zcRFq6', 'ya29.sQLKvPmFqHAhn7lcdNnoWD5c_tAaSEDrFzsbMiGFJu10-HT-RPJZh_Pw0xTrbDgoYQ');
