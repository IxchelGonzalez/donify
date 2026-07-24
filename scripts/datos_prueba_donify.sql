-- ================================================================
-- DATOS DE PRUEBA PARA PROYECTO FINAL DONIFY
-- Genera al menos 30 registros por cada tabla.
--
-- IMPORTANTE:
-- Donacion_Ingresos.id_donador referencia Usuarios(id_usuario),
-- no Donadores(id_donador). Por eso las donaciones usan usuarios
-- de tipo 'donador'. La tabla Donadores se llena por separado porque
-- actualmente no está relacionada mediante una llave foránea.
-- ================================================================

USE proyectofinaldonify;

SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE Entrega_Detalle;
TRUNCATE TABLE Entrega_Salida;
TRUNCATE TABLE Donacion_Detalle;
TRUNCATE TABLE Donacion_Ingresos;
TRUNCATE TABLE Donadores;
TRUNCATE TABLE Prendas;
TRUNCATE TABLE Beneficiarios;
TRUNCATE TABLE Usuarios;
TRUNCATE TABLE Asociaciones;

SET FOREIGN_KEY_CHECKS = 1;

-- 30 ASOCIACIONES
INSERT INTO Asociaciones (id_asociacion, nombre, ubicacion, verificacion) VALUES
(1, 'Banco de Ropa Cuernavaca', 'Cuernavaca, Morelos', 1),
(2, 'Manos Unidas Morelos', 'Jiutepec, Morelos', 1),
(3, 'Abrigo para Todos', 'Temixco, Morelos', 1),
(4, 'Fundación Esperanza', 'Yautepec, Morelos', 1),
(5, 'Ropero Comunitario Emiliano Zapata', 'Emiliano Zapata, Morelos', 1),
(6, 'Ayuda Tepoztlán', 'Tepoztlán, Morelos', 1),
(7, 'Corazones Solidarios', 'Cuautla, Morelos', 1),
(8, 'Red de Apoyo Jojutla', 'Jojutla, Morelos', 1),
(9, 'Vestir con Dignidad', 'Xochitepec, Morelos', 1),
(10, 'Fundación Nuevo Amanecer', 'Zacatepec, Morelos', 1),
(11, 'Unidos por Morelos', 'Puente de Ixtla, Morelos', 1),
(12, 'Casa de Apoyo Infantil', 'Cuernavaca, Morelos', 1),
(13, 'Ropero San José', 'Jiutepec, Morelos', 0),
(14, 'Compartiendo Sonrisas', 'Cuautla, Morelos', 1),
(15, 'Alianza Comunitaria', 'Ayala, Morelos', 0),
(16, 'Fundación Tejiendo Futuro', 'Temixco, Morelos', 1),
(17, 'Centro de Ayuda Familiar', 'Yautepec, Morelos', 1),
(18, 'Ropa y Esperanza', 'Tlayacapan, Morelos', 0),
(19, 'Apoyo Sin Fronteras', 'Huitzilac, Morelos', 1),
(20, 'Comunidad Solidaria', 'Tetecala, Morelos', 0),
(21, 'Dona una Sonrisa', 'Mazatepec, Morelos', 1),
(22, 'Ropero del Sur', 'Tlaltizapán, Morelos', 1),
(23, 'Fundación Buen Vestir', 'Miacatlán, Morelos', 0),
(24, 'Ayudando Corazones', 'Ocuituco, Morelos', 1),
(25, 'Manos que Abrigan', 'Yecapixtla, Morelos', 1),
(26, 'Red Humanitaria Morelos', 'Jonacatepec, Morelos', 0),
(27, 'Centro Comunitario Amistad', 'Atlatlahucan, Morelos', 1),
(28, 'Fundación Renacer', 'Axochiapan, Morelos', 1),
(29, 'Ropero Esperanza Viva', 'Tlaquiltenango, Morelos', 0),
(30, 'Solidaridad Activa', 'Tepalcingo, Morelos', 1);

-- 62 USUARIOS: 30 donadores, 30 instituciones y 2 administradores
INSERT INTO Usuarios (id_usuario, usuario, contrasena, curp, tipo_usuario, id_asociacion) VALUES
(1, 'donador01', 'Donify01*', 'DON001000101HMSXXX', 'donador', NULL),
(2, 'donador02', 'Donify02*', 'DON002000101HMSXXX', 'donador', NULL),
(3, 'donador03', 'Donify03*', 'DON003000101HMSXXX', 'donador', NULL),
(4, 'donador04', 'Donify04*', 'DON004000101HMSXXX', 'donador', NULL),
(5, 'donador05', 'Donify05*', 'DON005000101HMSXXX', 'donador', NULL),
(6, 'donador06', 'Donify06*', 'DON006000101HMSXXX', 'donador', NULL),
(7, 'donador07', 'Donify07*', 'DON007000101HMSXXX', 'donador', NULL),
(8, 'donador08', 'Donify08*', 'DON008000101HMSXXX', 'donador', NULL),
(9, 'donador09', 'Donify09*', 'DON009000101HMSXXX', 'donador', NULL),
(10, 'donador10', 'Donify10*', 'DON010000101HMSXXX', 'donador', NULL),
(11, 'donador11', 'Donify11*', 'DON011000101HMSXXX', 'donador', NULL),
(12, 'donador12', 'Donify12*', 'DON012000101HMSXXX', 'donador', NULL),
(13, 'donador13', 'Donify13*', 'DON013000101HMSXXX', 'donador', NULL),
(14, 'donador14', 'Donify14*', 'DON014000101HMSXXX', 'donador', NULL),
(15, 'donador15', 'Donify15*', 'DON015000101HMSXXX', 'donador', NULL),
(16, 'donador16', 'Donify16*', 'DON016000101HMSXXX', 'donador', NULL),
(17, 'donador17', 'Donify17*', 'DON017000101HMSXXX', 'donador', NULL),
(18, 'donador18', 'Donify18*', 'DON018000101HMSXXX', 'donador', NULL),
(19, 'donador19', 'Donify19*', 'DON019000101HMSXXX', 'donador', NULL),
(20, 'donador20', 'Donify20*', 'DON020000101HMSXXX', 'donador', NULL),
(21, 'donador21', 'Donify21*', 'DON021000101HMSXXX', 'donador', NULL),
(22, 'donador22', 'Donify22*', 'DON022000101HMSXXX', 'donador', NULL),
(23, 'donador23', 'Donify23*', 'DON023000101HMSXXX', 'donador', NULL),
(24, 'donador24', 'Donify24*', 'DON024000101HMSXXX', 'donador', NULL),
(25, 'donador25', 'Donify25*', 'DON025000101HMSXXX', 'donador', NULL),
(26, 'donador26', 'Donify26*', 'DON026000101HMSXXX', 'donador', NULL),
(27, 'donador27', 'Donify27*', 'DON027000101HMSXXX', 'donador', NULL),
(28, 'donador28', 'Donify28*', 'DON028000101HMSXXX', 'donador', NULL),
(29, 'donador29', 'Donify29*', 'DON029000101HMSXXX', 'donador', NULL),
(30, 'donador30', 'Donify30*', 'DON030000101HMSXXX', 'donador', NULL),
(31, 'institucion01', 'Inst01#2026', 'INS001000101MMSXXX', 'institucion', 1),
(32, 'institucion02', 'Inst02#2026', 'INS002000101MMSXXX', 'institucion', 2),
(33, 'institucion03', 'Inst03#2026', 'INS003000101MMSXXX', 'institucion', 3),
(34, 'institucion04', 'Inst04#2026', 'INS004000101MMSXXX', 'institucion', 4),
(35, 'institucion05', 'Inst05#2026', 'INS005000101MMSXXX', 'institucion', 5),
(36, 'institucion06', 'Inst06#2026', 'INS006000101MMSXXX', 'institucion', 6),
(37, 'institucion07', 'Inst07#2026', 'INS007000101MMSXXX', 'institucion', 7),
(38, 'institucion08', 'Inst08#2026', 'INS008000101MMSXXX', 'institucion', 8),
(39, 'institucion09', 'Inst09#2026', 'INS009000101MMSXXX', 'institucion', 9),
(40, 'institucion10', 'Inst10#2026', 'INS010000101MMSXXX', 'institucion', 10),
(41, 'institucion11', 'Inst11#2026', 'INS011000101MMSXXX', 'institucion', 11),
(42, 'institucion12', 'Inst12#2026', 'INS012000101MMSXXX', 'institucion', 12),
(43, 'institucion13', 'Inst13#2026', 'INS013000101MMSXXX', 'institucion', 13),
(44, 'institucion14', 'Inst14#2026', 'INS014000101MMSXXX', 'institucion', 14),
(45, 'institucion15', 'Inst15#2026', 'INS015000101MMSXXX', 'institucion', 15),
(46, 'institucion16', 'Inst16#2026', 'INS016000101MMSXXX', 'institucion', 16),
(47, 'institucion17', 'Inst17#2026', 'INS017000101MMSXXX', 'institucion', 17),
(48, 'institucion18', 'Inst18#2026', 'INS018000101MMSXXX', 'institucion', 18),
(49, 'institucion19', 'Inst19#2026', 'INS019000101MMSXXX', 'institucion', 19),
(50, 'institucion20', 'Inst20#2026', 'INS020000101MMSXXX', 'institucion', 20),
(51, 'institucion21', 'Inst21#2026', 'INS021000101MMSXXX', 'institucion', 21),
(52, 'institucion22', 'Inst22#2026', 'INS022000101MMSXXX', 'institucion', 22),
(53, 'institucion23', 'Inst23#2026', 'INS023000101MMSXXX', 'institucion', 23),
(54, 'institucion24', 'Inst24#2026', 'INS024000101MMSXXX', 'institucion', 24),
(55, 'institucion25', 'Inst25#2026', 'INS025000101MMSXXX', 'institucion', 25),
(56, 'institucion26', 'Inst26#2026', 'INS026000101MMSXXX', 'institucion', 26),
(57, 'institucion27', 'Inst27#2026', 'INS027000101MMSXXX', 'institucion', 27),
(58, 'institucion28', 'Inst28#2026', 'INS028000101MMSXXX', 'institucion', 28),
(59, 'institucion29', 'Inst29#2026', 'INS029000101MMSXXX', 'institucion', 29),
(60, 'institucion30', 'Inst30#2026', 'INS030000101MMSXXX', 'institucion', 30),
(61, 'admin', 'admin123', 'ADM001000101HMSXXX', 'admin', NULL),
(62, 'supervisor', 'super2026', 'ADM002000101MMSXXX', 'admin', NULL);

-- 30 BENEFICIARIOS
INSERT INTO Beneficiarios (id_beneficiario, nombre, sexo, fecha_ultima_recepcion) VALUES
(1, 'Ana García López', 'Femenino', NULL),
(2, 'Luis Martínez Ruiz', 'Masculino', '2025-01-23'),
(3, 'María Hernández Soto', 'Otro', '2025-02-05'),
(4, 'Carlos Ramírez Flores', 'Femenino', '2025-02-18'),
(5, 'Sofía Torres Díaz', 'Masculino', '2025-03-03'),
(6, 'Jorge Vargas Cruz', 'Otro', '2025-03-16'),
(7, 'Fernanda Morales Reyes', 'Femenino', NULL),
(8, 'Miguel Castillo Vega', 'Masculino', '2025-04-11'),
(9, 'Daniela Mendoza Silva', 'Otro', '2025-04-24'),
(10, 'Ricardo Ortiz Rojas', 'Femenino', '2025-05-07'),
(11, 'Valeria Navarro Luna', 'Masculino', '2025-05-20'),
(12, 'Héctor Jiménez Campos', 'Otro', '2025-06-02'),
(13, 'Camila Ramos Mejía', 'Femenino', NULL),
(14, 'Raúl Romero Salas', 'Masculino', '2025-06-28'),
(15, 'Paola Guerrero Pineda', 'Otro', '2025-07-11'),
(16, 'Eduardo Medina Fuentes', 'Femenino', '2025-07-24'),
(17, 'Andrea Cruz Aguilar', 'Masculino', '2025-08-06'),
(18, 'José Reyes Valdez', 'Otro', '2025-08-19'),
(19, 'Natalia Sánchez Mora', 'Femenino', NULL),
(20, 'Manuel Flores Tapia', 'Masculino', '2025-09-14'),
(21, 'Ximena Díaz Cabrera', 'Otro', '2025-09-27'),
(22, 'Arturo López Arias', 'Femenino', '2025-10-10'),
(23, 'Renata Gómez Vázquez', 'Masculino', '2025-10-23'),
(24, 'Fernando Pérez Miranda', 'Otro', '2025-11-05'),
(25, 'Lucía Álvarez Núñez', 'Femenino', NULL),
(26, 'Diego Soto Carrillo', 'Masculino', '2025-12-01'),
(27, 'Gabriela Vega Rosales', 'Otro', '2025-12-14'),
(28, 'Óscar Ruiz Estrada', 'Femenino', '2025-12-27'),
(29, 'Elena Silva Palma', 'Masculino', '2026-01-09'),
(30, 'Roberto Mora Escobar', 'Otro', '2026-01-22');

-- 30 PRENDAS
INSERT INTO Prendas (id_prenda, tipo_prenda, estado_prenda, stock) VALUES
(1, 'Playera', 'Nueva', 22),
(2, 'Playera', 'Buen estado', 29),
(3, 'Camisa', 'Nueva', 36),
(4, 'Camisa', 'Buen estado', 43),
(5, 'Pantalón', 'Nuevo', 50),
(6, 'Pantalón', 'Buen estado', 57),
(7, 'Sudadera', 'Nueva', 18),
(8, 'Sudadera', 'Buen estado', 25),
(9, 'Chamarra', 'Nueva', 32),
(10, 'Chamarra', 'Buen estado', 39),
(11, 'Vestido', 'Nuevo', 46),
(12, 'Vestido', 'Buen estado', 53),
(13, 'Falda', 'Nueva', 60),
(14, 'Falda', 'Buen estado', 21),
(15, 'Suéter', 'Nuevo', 28),
(16, 'Suéter', 'Buen estado', 35),
(17, 'Short', 'Nuevo', 42),
(18, 'Short', 'Buen estado', 49),
(19, 'Blusa', 'Nueva', 56),
(20, 'Blusa', 'Buen estado', 17),
(21, 'Zapatos', 'Nuevos', 24),
(22, 'Zapatos', 'Buen estado', 31),
(23, 'Tenis', 'Nuevos', 38),
(24, 'Tenis', 'Buen estado', 45),
(25, 'Calcetines', 'Nuevos', 52),
(26, 'Bufanda', 'Nueva', 59),
(27, 'Gorra', 'Nueva', 20),
(28, 'Ropa interior', 'Nueva', 27),
(29, 'Pijama', 'Nueva', 34),
(30, 'Pijama', 'Buen estado', 41);

-- 30 DONADORES DEL CATÁLOGO
INSERT INTO Donadores (id_donador, nombre, correo, telefono) VALUES
(1, 'Ana Castillo Vega', 'donador01@correo.com', '7771000000'),
(2, 'Luis Mendoza Silva', 'donador02@correo.com', '7771000001'),
(3, 'María Ortiz Rojas', 'donador03@correo.com', '7771000002'),
(4, 'Carlos Navarro Luna', 'donador04@correo.com', '7771000003'),
(5, 'Sofía Jiménez Campos', 'donador05@correo.com', '7771000004'),
(6, 'Jorge Ramos Mejía', 'donador06@correo.com', '7771000005'),
(7, 'Fernanda Romero Salas', 'donador07@correo.com', '7771000006'),
(8, 'Miguel Guerrero Pineda', 'donador08@correo.com', '7771000007'),
(9, 'Daniela Medina Fuentes', 'donador09@correo.com', '7771000008'),
(10, 'Ricardo Cruz Aguilar', 'donador10@correo.com', '7771000009'),
(11, 'Valeria Reyes Valdez', 'donador11@correo.com', '7771000010'),
(12, 'Héctor Sánchez Mora', 'donador12@correo.com', '7771000011'),
(13, 'Camila Flores Tapia', 'donador13@correo.com', '7771000012'),
(14, 'Raúl Díaz Cabrera', 'donador14@correo.com', '7771000013'),
(15, 'Paola López Arias', 'donador15@correo.com', '7771000014'),
(16, 'Eduardo Gómez Vázquez', 'donador16@correo.com', '7771000015'),
(17, 'Andrea Pérez Miranda', 'donador17@correo.com', '7771000016'),
(18, 'José Álvarez Núñez', 'donador18@correo.com', '7771000017'),
(19, 'Natalia Soto Carrillo', 'donador19@correo.com', '7771000018'),
(20, 'Manuel Vega Rosales', 'donador20@correo.com', '7771000019'),
(21, 'Ximena Ruiz Estrada', 'donador21@correo.com', '7771000020'),
(22, 'Arturo Silva Palma', 'donador22@correo.com', '7771000021'),
(23, 'Renata Mora Escobar', 'donador23@correo.com', '7771000022'),
(24, 'Fernando García López', 'donador24@correo.com', '7771000023'),
(25, 'Lucía Martínez Ruiz', 'donador25@correo.com', '7771000024'),
(26, 'Diego Hernández Soto', 'donador26@correo.com', '7771000025'),
(27, 'Gabriela Ramírez Flores', 'donador27@correo.com', '7771000026'),
(28, 'Óscar Torres Díaz', 'donador28@correo.com', '7771000027'),
(29, 'Elena Vargas Cruz', 'donador29@correo.com', '7771000028'),
(30, 'Roberto Morales Reyes', 'donador30@correo.com', '7771000029');

-- 30 DONACIONES DE INGRESO
INSERT INTO Donacion_Ingresos (id_donacion_ingreso, id_donador, id_asociacion, fecha_donacion) VALUES
(1, 1, 3, '2025-02-10'),
(2, 2, 6, '2025-02-19'),
(3, 3, 9, '2025-02-28'),
(4, 4, 12, '2025-03-09'),
(5, 5, 15, '2025-03-18'),
(6, 6, 18, '2025-03-27'),
(7, 7, 21, '2025-04-05'),
(8, 8, 24, '2025-04-14'),
(9, 9, 27, '2025-04-23'),
(10, 10, 30, '2025-05-02'),
(11, 11, 3, '2025-05-11'),
(12, 12, 6, '2025-05-20'),
(13, 13, 9, '2025-05-29'),
(14, 14, 12, '2025-06-07'),
(15, 15, 15, '2025-06-16'),
(16, 16, 18, '2025-06-25'),
(17, 17, 21, '2025-07-04'),
(18, 18, 24, '2025-07-13'),
(19, 19, 27, '2025-07-22'),
(20, 20, 30, '2025-07-31'),
(21, 21, 3, '2025-08-09'),
(22, 22, 6, '2025-08-18'),
(23, 23, 9, '2025-08-27'),
(24, 24, 12, '2025-09-05'),
(25, 25, 15, '2025-09-14'),
(26, 26, 18, '2025-09-23'),
(27, 27, 21, '2025-10-02'),
(28, 28, 24, '2025-10-11'),
(29, 29, 27, '2025-10-20'),
(30, 30, 30, '2025-10-29');

-- 60 DETALLES DE DONACIÓN: 2 prendas por donación
INSERT INTO Donacion_Detalle (id_donacion_detalle, id_donacion_ingreso, id_prenda, cantidad) VALUES
(1, 1, 1, 2),
(2, 1, 10, 4),
(3, 2, 3, 3),
(4, 2, 12, 5),
(5, 3, 5, 4),
(6, 3, 14, 6),
(7, 4, 7, 5),
(8, 4, 16, 1),
(9, 5, 9, 1),
(10, 5, 18, 2),
(11, 6, 11, 2),
(12, 6, 20, 3),
(13, 7, 13, 3),
(14, 7, 22, 4),
(15, 8, 15, 4),
(16, 8, 24, 5),
(17, 9, 17, 5),
(18, 9, 26, 6),
(19, 10, 19, 1),
(20, 10, 28, 1),
(21, 11, 21, 2),
(22, 11, 30, 2),
(23, 12, 23, 3),
(24, 12, 2, 3),
(25, 13, 25, 4),
(26, 13, 4, 4),
(27, 14, 27, 5),
(28, 14, 6, 5),
(29, 15, 29, 1),
(30, 15, 8, 6),
(31, 16, 1, 2),
(32, 16, 10, 1),
(33, 17, 3, 3),
(34, 17, 12, 2),
(35, 18, 5, 4),
(36, 18, 14, 3),
(37, 19, 7, 5),
(38, 19, 16, 4),
(39, 20, 9, 1),
(40, 20, 18, 5),
(41, 21, 11, 2),
(42, 21, 20, 6),
(43, 22, 13, 3),
(44, 22, 22, 1),
(45, 23, 15, 4),
(46, 23, 24, 2),
(47, 24, 17, 5),
(48, 24, 26, 3),
(49, 25, 19, 1),
(50, 25, 28, 4),
(51, 26, 21, 2),
(52, 26, 30, 5),
(53, 27, 23, 3),
(54, 27, 2, 6),
(55, 28, 25, 4),
(56, 28, 4, 1),
(57, 29, 27, 5),
(58, 29, 6, 2),
(59, 30, 29, 1),
(60, 30, 8, 3);

-- 30 ENTREGAS DE SALIDA
INSERT INTO Entrega_Salida (id_entrega_salida, id_beneficiario, id_asociacion, fecha_entrega, estado) VALUES
(1, 1, 5, '2025-03-13', 'Entregada'),
(2, 2, 10, '2025-03-21', 'Entregada'),
(3, 3, 15, '2025-03-29', 'Entregada'),
(4, 4, 20, '2025-04-06', 'Pendiente'),
(5, 5, 25, '2025-04-14', 'Entregada'),
(6, 6, 30, '2025-04-22', 'Entregada'),
(7, 7, 5, '2025-04-30', 'Entregada'),
(8, 8, 10, '2025-05-08', 'Pendiente'),
(9, 9, 15, '2025-05-16', 'Entregada'),
(10, 10, 20, '2025-05-24', 'Entregada'),
(11, 11, 25, '2025-06-01', 'Entregada'),
(12, 12, 30, '2025-06-09', 'Pendiente'),
(13, 13, 5, '2025-06-17', 'Entregada'),
(14, 14, 10, '2025-06-25', 'Entregada'),
(15, 15, 15, '2025-07-03', 'Entregada'),
(16, 16, 20, '2025-07-11', 'Pendiente'),
(17, 17, 25, '2025-07-19', 'Entregada'),
(18, 18, 30, '2025-07-27', 'Entregada'),
(19, 19, 5, '2025-08-04', 'Entregada'),
(20, 20, 10, '2025-08-12', 'Pendiente'),
(21, 21, 15, '2025-08-20', 'Entregada'),
(22, 22, 20, '2025-08-28', 'Entregada'),
(23, 23, 25, '2025-09-05', 'Entregada'),
(24, 24, 30, '2025-09-13', 'Pendiente'),
(25, 25, 5, '2025-09-21', 'Entregada'),
(26, 26, 10, '2025-09-29', 'Entregada'),
(27, 27, 15, '2025-10-07', 'Entregada'),
(28, 28, 20, '2025-10-15', 'Pendiente'),
(29, 29, 25, '2025-10-23', 'Entregada'),
(30, 30, 30, '2025-10-31', 'Entregada');

-- 60 DETALLES DE ENTREGA: 2 prendas por entrega
INSERT INTO Entrega_Detalle (id_entrega_detalle, id_entrega_salida, id_prenda, cantidad) VALUES
(1, 1, 1, 2),
(2, 1, 9, 3),
(3, 2, 4, 3),
(4, 2, 12, 1),
(5, 3, 7, 1),
(6, 3, 15, 2),
(7, 4, 10, 2),
(8, 4, 18, 3),
(9, 5, 13, 3),
(10, 5, 21, 1),
(11, 6, 16, 1),
(12, 6, 24, 2),
(13, 7, 19, 2),
(14, 7, 27, 3),
(15, 8, 22, 3),
(16, 8, 30, 1),
(17, 9, 25, 1),
(18, 9, 3, 2),
(19, 10, 28, 2),
(20, 10, 6, 3),
(21, 11, 1, 3),
(22, 11, 9, 1),
(23, 12, 4, 1),
(24, 12, 12, 2),
(25, 13, 7, 2),
(26, 13, 15, 3),
(27, 14, 10, 3),
(28, 14, 18, 1),
(29, 15, 13, 1),
(30, 15, 21, 2),
(31, 16, 16, 2),
(32, 16, 24, 3),
(33, 17, 19, 3),
(34, 17, 27, 1),
(35, 18, 22, 1),
(36, 18, 30, 2),
(37, 19, 25, 2),
(38, 19, 3, 3),
(39, 20, 28, 3),
(40, 20, 6, 1),
(41, 21, 1, 1),
(42, 21, 9, 2),
(43, 22, 4, 2),
(44, 22, 12, 3),
(45, 23, 7, 3),
(46, 23, 15, 1),
(47, 24, 10, 1),
(48, 24, 18, 2),
(49, 25, 13, 2),
(50, 25, 21, 3),
(51, 26, 16, 3),
(52, 26, 24, 1),
(53, 27, 19, 1),
(54, 27, 27, 2),
(55, 28, 22, 2),
(56, 28, 30, 3),
(57, 29, 25, 3),
(58, 29, 3, 1),
(59, 30, 28, 1),
(60, 30, 6, 2);


-- ================================================================
-- CONSULTAS RÁPIDAS DE VALIDACIÓN
-- ================================================================

SELECT 'Asociaciones' AS tabla, COUNT(*) AS registros FROM Asociaciones
UNION ALL SELECT 'Usuarios', COUNT(*) FROM Usuarios
UNION ALL SELECT 'Beneficiarios', COUNT(*) FROM Beneficiarios
UNION ALL SELECT 'Prendas', COUNT(*) FROM Prendas
UNION ALL SELECT 'Donadores', COUNT(*) FROM Donadores
UNION ALL SELECT 'Donacion_Ingresos', COUNT(*) FROM Donacion_Ingresos
UNION ALL SELECT 'Donacion_Detalle', COUNT(*) FROM Donacion_Detalle
UNION ALL SELECT 'Entrega_Salida', COUNT(*) FROM Entrega_Salida
UNION ALL SELECT 'Entrega_Detalle', COUNT(*) FROM Entrega_Detalle;

-- Reporte de donaciones
SELECT
    di.id_donacion_ingreso,
    u.usuario AS donador,
    a.nombre AS asociacion,
    di.fecha_donacion,
    p.tipo_prenda,
    p.estado_prenda,
    dd.cantidad
FROM Donacion_Ingresos di
INNER JOIN Usuarios u ON u.id_usuario = di.id_donador
INNER JOIN Asociaciones a ON a.id_asociacion = di.id_asociacion
INNER JOIN Donacion_Detalle dd ON dd.id_donacion_ingreso = di.id_donacion_ingreso
INNER JOIN Prendas p ON p.id_prenda = dd.id_prenda
ORDER BY di.id_donacion_ingreso, dd.id_donacion_detalle;

-- Reporte de entregas
SELECT
    es.id_entrega_salida,
    b.nombre AS beneficiario,
    a.nombre AS asociacion,
    es.fecha_entrega,
    es.estado,
    p.tipo_prenda,
    p.estado_prenda,
    ed.cantidad
FROM Entrega_Salida es
INNER JOIN Beneficiarios b ON b.id_beneficiario = es.id_beneficiario
INNER JOIN Asociaciones a ON a.id_asociacion = es.id_asociacion
INNER JOIN Entrega_Detalle ed ON ed.id_entrega_salida = es.id_entrega_salida
INNER JOIN Prendas p ON p.id_prenda = ed.id_prenda
ORDER BY es.id_entrega_salida, ed.id_entrega_detalle;
