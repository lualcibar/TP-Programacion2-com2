
--  BASE DE DATOS DEL SISTEMA TALLER MECANICO
--  Ejecutar este script completo en MySQL (Workbench, consola, etc.)
--  ATENCION: borra y vuelve a crear la base 'taller_mecanico'. Si lo volves
--  a ejecutar, los datos vuelven al estado inicial (util para la defensa).
--
--  Usuarios de prueba (DNI / contrasenia):
--    ADMINISTRADOR : 21123456 / admin123      (tambien 15123456 / 1234)
--    CLIENTE activo: 21931131 / 1234          (todos los clientes usan 1234)
--    CLIENTE de baja (no puede ingresar): 18555666 y 29111222
--  Recepcionistas y mecanicos existen como empleados pero no tienen menu propio.
--
--  Baja logica: columna 'activo' en cliente, empleado y vehiculo; el turno usa
--  el estado CANCELADO. Nunca se hace DELETE, por eso las FK son RESTRICT.

DROP DATABASE IF EXISTS taller_mecanico;
CREATE DATABASE taller_mecanico CHARACTER
SET utf8mb4 --es la codificación de los textos. Sirve para guardar bien tildes y eñes (Pérez, Rodríguez, Núñez).
COLLATE utf8mb4_general_ci; --son las reglas para comparar textos.
                            --El ci significa que no distingue mayúsculas de minúsculas,
                            --así que ab123cd y AB123CD son la misma patente en la clave primaria.
USE taller_mecanico;

CREATE TABLE cliente (
    dni        VARCHAR(8)   PRIMARY KEY,
    nombre     VARCHAR(100) NOT NULL,
    telefono   VARCHAR(20)  NOT NULL,
    email      VARCHAR(100) NOT NULL,
    password   VARCHAR(50)  NOT NULL,
    activo     BOOLEAN      NOT NULL DEFAULT TRUE
);

CREATE TABLE empleado (
    dni        VARCHAR(8)   PRIMARY KEY,
    nombre     VARCHAR(100) NOT NULL,
    telefono   VARCHAR(20)  NOT NULL,
    email      VARCHAR(100) NOT NULL,
    password   VARCHAR(50)  NOT NULL,
    cargo      ENUM('ADMINISTRADOR','MECANICO','RECEPCIONISTA') NOT NULL,
    salario    DECIMAL(12,2) NOT NULL,
    activo     BOOLEAN      NOT NULL DEFAULT TRUE
);

CREATE TABLE vehiculo (
    patente    VARCHAR(10)  PRIMARY KEY,
    marca      VARCHAR(50)  NOT NULL,
    modelo     VARCHAR(50)  NOT NULL,
    anio       INT          NOT NULL,
    dni_duenio VARCHAR(8)   NOT NULL,
    activo     BOOLEAN      NOT NULL DEFAULT TRUE,
    FOREIGN KEY (dni_duenio) REFERENCES cliente(dni)
);

CREATE TABLE reparacion (
    id               INT AUTO_INCREMENT PRIMARY KEY,
    patente_vehiculo VARCHAR(10)  NOT NULL,
    dni_mecanico     VARCHAR(8)   NOT NULL,
    descripcion      VARCHAR(255) NOT NULL,
    costo            DECIMAL(12,2) NOT NULL,
    estado           ENUM('PENDIENTE','EN_CURSO','FINALIZADA') NOT NULL DEFAULT 'PENDIENTE',
    fecha_inicio     DATE NULL,
    fecha_fin        DATE NULL,
    FOREIGN KEY (patente_vehiculo) REFERENCES vehiculo(patente),
    FOREIGN KEY (dni_mecanico)     REFERENCES empleado(dni)
);

CREATE TABLE turno (
    id               INT AUTO_INCREMENT PRIMARY KEY,
    patente_vehiculo VARCHAR(10) NOT NULL,
    dni_mecanico     VARCHAR(8)  NOT NULL,
    fecha            DATE        NOT NULL,
    estado           ENUM('PENDIENTE','CANCELADO','ATENDIDO') NOT NULL DEFAULT 'PENDIENTE',
    FOREIGN KEY (patente_vehiculo) REFERENCES vehiculo(patente),
    FOREIGN KEY (dni_mecanico)     REFERENCES empleado(dni)
);

--            CLIENTES (15)
INSERT INTO cliente (dni, nombre, telefono, email, password, activo) VALUES
    ('18555666', 'Carlos Pérez', '2236661122', 'carlos@gmail.com', '1234', FALSE),
    ('20555666', 'Rodrigo Vega', '2236667788', 'rodri@gmail.com', '1234', TRUE),
    ('21931131', 'Felipe Rodríguez', '2235425164', 'felipe@gmail.com', '1234', TRUE),
    ('22999000', 'Nicolás Díaz', '2230001122', 'nico@gmail.com', '1234', TRUE),
    ('24333444', 'Tomás Romero', '2234445566', 'tomas@gmail.com', '1234', TRUE),
    ('25333444', 'Martín Sosa', '2231239876', 'martin@gmail.com', '1234', TRUE),
    ('27777888', 'Diego Fernández', '2238885566', 'diego@gmail.com', '1234', TRUE),
    ('29111222', 'Pablo Ruiz', '2232223344', 'pablo@gmail.com', '1234', FALSE),
    ('30222333', 'Lucía Gómez', '2234561234', 'lucia@gmail.com', '1234', TRUE),
    ('33888999', 'Camila López', '2239997788', 'camila@gmail.com', '1234', TRUE),
    ('36000111', 'Ana Martínez', '2231112233', 'ana@gmail.com', '1234', TRUE),
    ('37444555', 'Julieta Álvarez', '2235556677', 'juli@gmail.com', '1234', TRUE),
    ('38444555', 'Valentina Torres', '2235557788', 'vale@gmail.com', '1234', TRUE),
    ('40666777', 'Sofía Herrera', '2237773344', 'sofia@gmail.com', '1234', TRUE),
    ('41222333', 'Florencia Castro', '2233334455', 'flor@gmail.com', '1234', TRUE);

--            EMPLEADOS (15)
INSERT INTO empleado (dni, nombre, telefono, email, password, cargo, salario, activo) VALUES
    ('15123456', 'Roberto Sánchez', '2231234567', 'roberto@taller.com', '1234', 'ADMINISTRADOR', 200000, TRUE),
    ('17901234', 'Hernán Ortiz', '2239012345', 'hernan@taller.com', '1234', 'MECANICO', 145000, TRUE),
    ('19345678', 'Jorge Medina', '2233456789', 'jorge@taller.com', '1234', 'MECANICO', 155000, TRUE),
    ('21123456', 'Alejandro Gil', '2231234560', 'alejandro@taller.com', 'admin123', 'ADMINISTRADOR', 195000, TRUE),
    ('23567890', 'Gustavo Blanco', '2235678901', 'gustavo@taller.com', '1234', 'RECEPCIONISTA', 118000, TRUE),
    ('26789012', 'Fernando Mora', '2237890123', 'fernando@taller.com', '1234', 'MECANICO', 152000, TRUE),
    ('28234567', 'Marina Suárez', '2232345678', 'marina@taller.com', '1234', 'RECEPCIONISTA', 120000, TRUE),
    ('28345678', 'Maximiliano Luna', '2233456780', 'maxi@taller.com', '1234', 'MECANICO', 158000, TRUE),
    ('31890123', 'Claudia Vargas', '2238901234', 'claudia@taller.com', '1234', 'RECEPCIONISTA', 122000, TRUE),
    ('32456789', 'Paula Ríos', '2234567890', 'paula@taller.com', '1234', 'MECANICO', 148000, TRUE),
    ('35234567', 'Beatriz Cabrera', '2232345670', 'beatriz@taller.com', '1234', 'RECEPCIONISTA', 119000, TRUE),
    ('39678901', 'Natalia Reyes', '2236789012', 'natalia@taller.com', '1234', 'MECANICO', 160000, TRUE),
    ('43012345', 'Silvina Ponce', '2230123456', 'silvina@taller.com', '1234', 'MECANICO', 153000, TRUE),
    ('44456789', 'Cecilia Ibáñez', '2234567800', 'ceci@taller.com', '1234', 'MECANICO', 147000, TRUE),
    ('47832917', 'Lucía Alcibar', '2235425164', 'lucia@taller.com', '1234', 'MECANICO', 150000, TRUE);

--            VEHICULOS (15)
INSERT INTO vehiculo (patente, marca, modelo, anio, dni_duenio, activo) VALUES
    ('AB456WX', 'Toyota', 'Yaris', 2023, '37444555', TRUE),
    ('AB786RG', 'Ford', 'Palio', 2014, '21931131', TRUE),
    ('CD789YZ', 'Peugeot', '301', 2016, '20555666', TRUE),
    ('DC432HJ', 'Chevrolet', 'Corsa', 2010, '30222333', TRUE),
    ('EF123AB', 'Volkswagen', 'Gol', 2018, '25333444', TRUE),
    ('GH456CD', 'Renault', 'Sandero', 2016, '38444555', TRUE),
    ('IJ789EF', 'Toyota', 'Corolla', 2020, '18555666', TRUE),
    ('KL012GH', 'Peugeot', '208', 2019, '40666777', TRUE),
    ('MN345IJ', 'Fiat', 'Cronos', 2021, '27777888', TRUE),
    ('OP678KL', 'Honda', 'Civic', 2017, '33888999', TRUE),
    ('QR901MN', 'Nissan', 'Sentra', 2015, '22999000', TRUE),
    ('ST234OP', 'Ford', 'Focus', 2013, '36000111', TRUE),
    ('UV567QR', 'Chevrolet', 'Cruze', 2022, '29111222', TRUE),
    ('WX890ST', 'Volkswagen', 'Polo', 2020, '41222333', TRUE),
    ('YZ123UV', 'Renault', 'Logan', 2012, '24333444', TRUE);

--            REPARACIONES (15)
INSERT INTO reparacion (id, patente_vehiculo, dni_mecanico, descripcion, costo, estado, fecha_inicio, fecha_fin) VALUES
    (1, 'AB786RG', '47832917', 'Cambio de embrague', 85000, 'FINALIZADA', '2026-05-10', '2026-05-12'),
    (2, 'DC432HJ', '19345678', 'Reparación de frenos', 45000, 'FINALIZADA', '2026-05-15', '2026-05-15'),
    (3, 'EF123AB', '32456789', 'Cambio de aceite y filtros', 22000, 'FINALIZADA', '2026-05-20', '2026-05-20'),
    (4, 'GH456CD', '47832917', 'Reparación de suspensión', 120000, 'EN_CURSO', '2026-06-01', NULL),
    (5, 'KL012GH', '39678901', 'Cambio de correa de distribución', 95000, 'EN_CURSO', '2026-06-10', NULL),
    (6, 'MN345IJ', '26789012', 'Reparación eléctrica', 60000, 'FINALIZADA', '2026-05-25', '2026-05-27'),
    (7, 'OP678KL', '19345678', 'Cambio de pastillas de freno', 35000, 'FINALIZADA', '2026-06-05', '2026-06-05'),
    (8, 'QR901MN', '47832917', 'Reparación de caja de cambios', 200000, 'PENDIENTE', NULL, NULL),
    (9, 'ST234OP', '32456789', 'Alineación y balanceo', 18000, 'FINALIZADA', '2026-06-12', '2026-06-12'),
    (10, 'WX890ST', '39678901', 'Cambio de batería', 55000, 'FINALIZADA', '2026-06-15', '2026-06-15'),
    (11, 'YZ123UV', '26789012', 'Reparación de radiador', 75000, 'PENDIENTE', NULL, NULL),
    (12, 'AB456WX', '19345678', 'Cambio de amortiguadores', 110000, 'EN_CURSO', '2026-06-20', NULL),
    (13, 'CD789YZ', '47832917', 'Diagnóstico general', 15000, 'FINALIZADA', '2026-06-18', '2026-06-18'),
    (14, 'DC432HJ', '32456789', 'Cambio de bujías', 28000, 'PENDIENTE', NULL, NULL),
    (15, 'AB786RG', '39678901', 'Reparación de escape', 42000, 'PENDIENTE', NULL, NULL);

--            TURNOS (15)
INSERT INTO turno (id, patente_vehiculo, dni_mecanico, fecha, estado) VALUES
    (1, 'AB786RG', '47832917', '2026-06-25', 'PENDIENTE'),
    (2, 'DC432HJ', '19345678', '2026-06-26', 'PENDIENTE'),
    (3, 'EF123AB', '32456789', '2026-06-27', 'ATENDIDO'),
    (4, 'GH456CD', '47832917', '2026-06-28', 'PENDIENTE'),
    (5, 'IJ789EF', '39678901', '2026-06-30', 'CANCELADO'),
    (6, 'KL012GH', '19345678', '2026-07-01', 'PENDIENTE'),
    (7, 'MN345IJ', '26789012', '2026-07-02', 'PENDIENTE'),
    (8, 'OP678KL', '47832917', '2026-07-03', 'ATENDIDO'),
    (9, 'QR901MN', '32456789', '2026-07-04', 'PENDIENTE'),
    (10, 'ST234OP', '39678901', '2026-07-07', 'PENDIENTE'),
    (11, 'WX890ST', '26789012', '2026-07-08', 'PENDIENTE'),
    (12, 'YZ123UV', '19345678', '2026-07-09', 'CANCELADO'),
    (13, 'AB456WX', '47832917', '2026-07-10', 'PENDIENTE'),
    (14, 'CD789YZ', '32456789', '2026-07-11', 'PENDIENTE'),
    (15, 'AB786RG', '39678901', '2026-07-14', 'PENDIENTE');
