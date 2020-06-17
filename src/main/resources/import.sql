INSERT INTO PERFILES (id, perfil) VALUES (1, 'ADMINISTRADOR');
INSERT INTO PERFILES (id, perfil) VALUES (2, 'USUARIO');
INSERT INTO usuarios (email, estatus, fecha_registro, nombre, password, username) VALUES ('admin@gmail.com', 1, '2020-01-01', 'admin', '$2a$10$e4hUFy92NHLDkXLnJPhejOUtEew1vQGMpL.FoX0yceKM/bnq3te5e', 'admin');
INSERT INTO usuarios (email, estatus, fecha_registro, nombre, password, username) VALUES ('nuevo@gmail.com', 1, '2020-01-01', 'nuevo', '$2a$10$xVNO6rxXJ/4ZfPC2Dxnr8OquYZ26cnwppqkFDwj9WSBoGVOlKUHHS', 'nuevo');
insert into usuario_perfil values(1,1)
insert into usuario_perfil values(2,2)



