-- ============================================================
-- 1. TipoUsuario
-- ============================================================
INSERT IGNORE INTO TipoUsuario (idTipoUsuario, rol) VALUES
                                                 (1, 'Cliente'),
                                                 (2, 'Administrador'),
                                                 (3, 'Empleado'),
                                                 (4, 'Repartidor');

-- ============================================================
-- 2. Usuario
-- ============================================================
INSERT IGNORE INTO Usuario (idUsuario, nombre, direccion, password, correoElectronico, telefono, fechaRegistro, idTipoUsuario) VALUES
                                                                                                                            (1, 'Ana María López', 'Av. Reforma 123, CDMX', 'hash_ana123', 'ana.lopez@email.com', '5551234567', '2024-01-15', 1),
                                                                                                                            (2, 'Carlos Ramírez', 'Calle 5 de Mayo 45, Puebla', 'hash_carlos456', 'carlos.ramirez@email.com', '2229876543', '2024-02-20', 1),
                                                                                                                            (3, 'Laura Fernández', 'Blvd. Kukulcán 78, Cancún', 'hash_laura789', 'laura.fernandez@email.com', '9984567890', '2024-03-10', 1),
                                                                                                                            (4, 'Roberto Sánchez', 'Av. Juárez 200, Guadalajara', 'hash_admin001', 'admin@porElGusto.com', '3331112233', '2023-12-01', 2),
                                                                                                                            (5, 'María González', 'Calle Hidalgo 12, Monterrey', 'hash_emp002', 'maria.gonzalez@porElGusto.com', '8184445566', '2024-01-05', 3),
                                                                                                                            (6, 'Jorge Torres', 'Av. Insurgentes 500, CDMX', 'hash_rep003', 'jorge.torres@porElGusto.com', '5557778899', '2024-02-01', 4),
                                                                                                                            (7, 'Sofía Herrera', 'Calle 20 de Noviembre 89, Querétaro', 'hash_sofia321', 'sofia.herrera@email.com', '4423334455', '2024-04-18', 1);

-- ============================================================
-- 3. Contacto
-- ============================================================
INSERT IGNORE INTO Contacto (idContacto, nombre, tipoContacto, idUsuario) VALUES
                                                                       (1, 'Ana María López', 'Principal', 1),
                                                                       (2, 'Carlos Ramírez', 'Principal', 2),
                                                                       (3, 'Laura Fernández', 'Principal', 3),
                                                                       (4, 'Roberto Sánchez', 'Administración', 4),
                                                                       (5, 'María González', 'Soporte', 5),
                                                                       (6, 'Jorge Torres', 'Reparto', 6),
                                                                       (7, 'Sofía Herrera', 'Principal', 7);

-- ============================================================
-- 4. MetodoPago
-- ============================================================
INSERT IGNORE INTO MetodoPago (idMetodosDePago, tipoPago, descripcion, idUsuario) VALUES
                                                                               (1, 'Tarjeta de Crédito', 'Visa terminación 1234', 1),
                                                                               (2, 'PayPal', 'cuenta carlos.ramirez@email.com', 2),
                                                                               (3, 'Tarjeta de Débito', 'Mastercard terminación 5678', 3),
                                                                               (4, 'Transferencia SPEI', 'Cuenta BBVA ***8901', 4),
                                                                               (5, 'Efectivo', 'Pago contra entrega', 7),
                                                                               (6, 'Tarjeta de Crédito', 'American Express terminación 4321', 1);

-- ============================================================
-- 5. ListaDeDeseos
-- ============================================================
INSERT IGNORE INTO ListaDeDeseos (idLista, fechaCreacion, idUsuario) VALUES
                                                                  (1, '2024-05-01', 1),
                                                                  (2, '2024-05-10', 2),
                                                                  (3, '2024-05-15', 3),
                                                                  (4, '2024-06-01', 7);


-- ============================================================
-- 6. ArregloFloral (catálogo de la tienda: 88 arreglos con foto en /img/productos)
-- ============================================================
-- Categorías del menú, grupos y temporadas (tabla Categoria). Los productos guardan el nombre de su categoría.
INSERT IGNORE INTO Categoria (idCategoria, nombre, slug, descripcion, tipo, orden) VALUES
    (1, 'Temporada', 'temporada', 'Los arreglos favoritos de la temporada.', 'CATEGORIA', 0),
    (2, 'Detalles', 'detalles', 'Pequeños detalles para sorprender.', 'CATEGORIA', 1),
    (3, 'Floreros', 'floreros', 'Arreglos en florero listos para lucir.', 'CATEGORIA', 2),
    (4, 'Ramos', 'ramos', 'Ramos envueltos a mano.', 'CATEGORIA', 3),
    (5, 'Cajas', 'cajas', 'Flores en caja: elegantes y duraderas.', 'CATEGORIA', 4),
    (6, 'Orquídeas', 'orquideas', 'Orquídeas naturales en maceta.', 'CATEGORIA', 5),
    (7, 'Adicionales', 'adicionales', 'Complementos para tu regalo.', 'CATEGORIA', 6),
    (8, 'Primavera', 'primavera', 'Colores frescos: tulipanes, gerberas, hortensias y peonías.', 'TEMPORADA', 0),
    (9, 'Cumpleaños', 'cumpleanos', 'Globos, peluches y dulces para celebrar.', 'GRUPO', 1),
    (10, 'Aniversario', 'aniversario', 'Rosas para decir te amo.', 'GRUPO', 2),
    (11, 'Lujo', 'lujo', 'Nuestros arreglos más espectaculares.', 'GRUPO', 3);

-- Productos nuevos (en una base de datos vacía).
INSERT IGNORE INTO ArregloFloral (idArregloFloral, sku, nombre, detalles, costo, idTipoArreglo, categoria, etiqueta, disponibilidad, existencias, puntoReorden, activo, personalizable, permiteQR, vistas, ventas, fechaAlta) VALUES
    (1, 'PG-001', 'Ramo Dulce Encanto', 'Gerberas rosadas, rosas crema, rosas spray rosadas, crisantemo pompón blanco, margaritas rosadas, gypsophila, alstroemeria blanca y eucalipto.', 940.00, 1, 'Temporada', 'Alstroemerias, Dulces, Eucalipto, Gerberas, Gypsophila, Margaritas, Rosas', 'Disponible', 3, 3, TRUE, TRUE, TRUE, 0, 0, CURRENT_TIMESTAMP - INTERVAL '40' DAY),
    (2, 'PG-002', 'Ramo Tierna Ilusión', 'Ramo de 24 rosas y 8 gerberas.', 1200.00, 1, 'Temporada', 'Gerberas, Rosas', 'Disponible', 10, 3, TRUE, TRUE, TRUE, 37, 13, CURRENT_TIMESTAMP - INTERVAL '51' DAY),
    (3, 'PG-003', 'Ramo Noche Estrellada', 'Ramo floral con gran variedad de flores: rosas, margaritas, girasol y follajes.', 1200.00, 1, 'Temporada', 'Girasoles, Margaritas, Rosas', 'Disponible', 4, 3, TRUE, TRUE, TRUE, 74, 9, CURRENT_TIMESTAMP - INTERVAL '62' DAY),
    (4, 'PG-004', 'La Incondicional', 'Ramo inspirado en una de las canciones más icónicas de Luis Miguel, con rosas blancas y negras y código QR musical.', 1200.00, 1, 'Temporada', 'Rosas', 'Disponible', 11, 3, TRUE, TRUE, TRUE, 21, 5, CURRENT_TIMESTAMP - INTERVAL '73' DAY),
    (5, 'PG-005', 'Ramo Rosa Palo', 'Ramo de 50 rosas y 8 gerberas.', 1700.00, 1, 'Temporada', 'Gerberas, Rosas', 'Disponible', 5, 3, TRUE, TRUE, TRUE, 58, 1, CURRENT_TIMESTAMP - INTERVAL '84' DAY),
    (6, 'PG-006', 'Amor Carmesí', 'Rosas rojas, gerberas mini rosadas, claveles rosados, alstroemerias rosadas y follaje.', 1800.00, 1, 'Temporada', 'Alstroemerias, Claveles, Gerberas, Rosas', 'Disponible', 12, 3, TRUE, TRUE, TRUE, 5, 14, CURRENT_TIMESTAMP - INTERVAL '95' DAY),
    (7, 'PG-007', 'Ramo Aura Floral', 'Rosas rosadas, rosas spray, hortensias azul/lila, claveles, lisianthus, cymbidium, lilium oriental, narcisos, delphinium, hypericum, matricaria y follaje verde.', 3900.00, 1, 'Temporada', 'Claveles, Hortensias, Rosas', 'Agotado', 0, 3, TRUE, TRUE, TRUE, 42, 10, CURRENT_TIMESTAMP - INTERVAL '106' DAY),
    (8, 'PG-008', 'Euforia Rosa', 'Composición floral en tonos rosa con rosas, hortensias, claveles, lisianthus y follajes.', 2600.00, 1, 'Temporada', 'Claveles, Hortensias, Rosas', 'Disponible', 13, 3, TRUE, TRUE, TRUE, 79, 6, CURRENT_TIMESTAMP - INTERVAL '117' DAY),
    (9, 'PG-009', 'Cien Razones', '100 rosas y tulipanes.', 2800.00, 1, 'Temporada', 'Rosas, Tulipanes', 'Disponible', 7, 3, TRUE, TRUE, TRUE, 26, 2, CURRENT_TIMESTAMP - INTERVAL '128' DAY),
    (10, 'PG-010', 'Euforia Carmesí', '100 rosas, lirios y dólar.', 2800.00, 1, 'Temporada', 'Lirios, Rosas', 'Disponible', 14, 3, TRUE, TRUE, TRUE, 63, 15, CURRENT_TIMESTAMP - INTERVAL '139' DAY),
    (11, 'PG-011', 'Caja Floral Gran Amor', 'Caja floral con 50 rosas y follajes, oso de peluche y globo personalizado.', 2100.00, 1, 'Temporada', 'Caja, Globos, Peluches, Rosas', 'Disponible', 8, 3, TRUE, TRUE, TRUE, 10, 11, CURRENT_TIMESTAMP - INTERVAL '150' DAY),
    (12, 'PG-012', 'Reflejo de Amor', '100 rosas, baby y espejo al centro personalizado.', 2800.00, 1, 'Temporada', 'Rosas', 'Disponible', 15, 3, TRUE, TRUE, TRUE, 47, 7, CURRENT_TIMESTAMP - INTERVAL '41' DAY),
    (13, 'PG-013', 'Reyna de Rosas', '150 rosas rojas.', 3900.00, 1, 'Temporada', 'Rosas', 'Disponible', 9, 3, TRUE, TRUE, TRUE, 84, 3, CURRENT_TIMESTAMP - INTERVAL '52' DAY),
    (14, 'PG-014', 'Jardín de Tulipanes', 'Caja floral con 20 tulipanes, follajes y green alrededor en forma de jardinera.', 2800.00, 1, 'Temporada', 'Caja, Tulipanes', 'Disponible', 3, 3, TRUE, TRUE, TRUE, 31, 16, CURRENT_TIMESTAMP - INTERVAL '63' DAY),
    (15, 'PG-015', 'Sueño en Rosa', 'Cerámica floral con gran variedad de flores: rosas, mini rosas, lilis, mini gerbera, pompón, crisantemo, escabiosa, hipérico, eucalipto y follajes.', 2800.00, 1, 'Temporada', 'Eucalipto, Gerberas, Rosas', 'Disponible', 10, 3, TRUE, TRUE, TRUE, 68, 12, CURRENT_TIMESTAMP - INTERVAL '74' DAY),
    (16, 'PG-016', 'Caja Floral Mi Amor', 'Caja floral con 50 rosas rojas y follajes.', 1900.00, 1, 'Temporada', 'Caja, Rosas', 'Disponible', 4, 3, TRUE, TRUE, TRUE, 15, 8, CURRENT_TIMESTAMP - INTERVAL '85' DAY),
    (17, 'PG-017', 'Vaso Floral de Starbucks', 'Vaso térmico Starbucks con gran variedad de flores, lisianthus, rosas y clavel.', 1500.00, 1, 'Temporada', 'Claveles, Rosas', 'Disponible', 11, 3, TRUE, TRUE, TRUE, 52, 4, CURRENT_TIMESTAMP - INTERVAL '96' DAY),
    (18, 'PG-018', 'Edén Floral', 'Base de madera con gran variedad de flores al centro y una orquídea.', 2800.00, 1, 'Temporada', 'Orquídeas', 'Disponible', 5, 3, TRUE, TRUE, TRUE, 89, 0, CURRENT_TIMESTAMP - INTERVAL '107' DAY),
    (19, 'PG-019', 'Bolsa Floral Amor a la Mexicana', 'Bolsa hecha a mano con gran variedad de flores: rosa, lisianthus, anturio, perrito, gerbera importada, mini rosa y follajes.', 2200.00, 1, 'Temporada', 'Anturios, Gerberas, Rosas', 'Disponible', 12, 3, TRUE, TRUE, TRUE, 36, 13, CURRENT_TIMESTAMP - INTERVAL '118' DAY),
    (20, 'PG-020', 'Bolsa Floral Rojo Amor', 'Bolsa hecha a mano con gran variedad de flores: rosa, lisianthus, anturio, perrito, gerbera importada, mini rosa y follajes.', 2200.00, 1, 'Temporada', 'Anturios, Gerberas, Rosas', 'Disponible', 6, 3, TRUE, TRUE, TRUE, 73, 9, CURRENT_TIMESTAMP - INTERVAL '129' DAY),
    (21, 'PG-021', 'Florero Amor y Pasión', 'Base de cerámica negra con rosas, tulipanes, lisianthus, clavel, anturio y follajes.', 3200.00, 1, 'Temporada', 'Anturios, Claveles, Florero, Rosas, Tulipanes', 'Disponible', 13, 3, TRUE, TRUE, TRUE, 20, 5, CURRENT_TIMESTAMP - INTERVAL '140' DAY),
    (22, 'PG-022', 'Florero Dulzura', 'Rosas en tono fucsia y varas de lilis en florero de cristal transparente.', 740.00, 2, 'Detalles', 'Florero, Rosas', 'Disponible', 7, 3, TRUE, TRUE, TRUE, 57, 1, CURRENT_TIMESTAMP - INTERVAL '151' DAY),
    (23, 'PG-023', 'Florero Algodón de Azúcar', 'Gerberas en tonos rosas acompañadas con astromelia en un florero tornasol.', 780.00, 2, 'Detalles', 'Alstroemerias, Florero, Gerberas, Rosas', 'Disponible', 14, 3, TRUE, TRUE, TRUE, 4, 14, CURRENT_TIMESTAMP - INTERVAL '42' DAY),
    (24, 'PG-024', 'Florero Gran Detalle', 'Florero de cristal con rosas en tonos rosas y astromelias.', 720.00, 2, 'Detalles', 'Alstroemerias, Florero, Rosas', 'Disponible', 8, 3, TRUE, TRUE, TRUE, 41, 10, CURRENT_TIMESTAMP - INTERVAL '53' DAY),
    (25, 'PG-025', 'Florero Carmesí', '12 rosas rojas en florero de cristal.', 720.00, 2, 'Detalles', 'Florero, Rosas', 'Disponible', 15, 3, TRUE, TRUE, TRUE, 78, 6, CURRENT_TIMESTAMP - INTERVAL '64' DAY),
    (26, 'PG-026', 'Combo Love Love', 'Ramo de 24 rosas y follajes con oso de peluche.', 1100.00, 2, 'Detalles', 'Peluches, Rosas', 'Disponible', 9, 3, TRUE, TRUE, TRUE, 25, 2, CURRENT_TIMESTAMP - INTERVAL '75' DAY),
    (27, 'PG-027', 'Florero Amor', 'Disponible con 12, 24, 50 o 100 rosas. Se pueden elegir tonos rosa, morado, amarillo, blanco o rojo.', 720.00, 3, 'Floreros', 'Florero, Rosas', 'Disponible', 3, 3, TRUE, TRUE, TRUE, 62, 15, CURRENT_TIMESTAMP - INTERVAL '86' DAY),
    (28, 'PG-028', 'Pecera Eterno Amanecer', 'Pecera de cristal con follajes preservados y rosas anaranjadas.', 1300.00, 3, 'Floreros', 'Rosas', 'Disponible', 10, 3, TRUE, TRUE, TRUE, 9, 11, CURRENT_TIMESTAMP - INTERVAL '97' DAY),
    (29, 'PG-029', 'Florero Tulipanes', '10 o 20 tulipanes en florero de cristal. Tonos fucsia, morado, amarillo, naranja y bicolor.', 1100.00, 3, 'Floreros', 'Florero, Tulipanes', 'Disponible', 4, 3, TRUE, TRUE, TRUE, 46, 7, CURRENT_TIMESTAMP - INTERVAL '108' DAY),
    (30, 'PG-030', 'Arreglo Floral Ternura', 'Base de cerámica rosa tipo griego con gran variedad de flores y follajes.', 3200.00, 3, 'Floreros', 'Rosas', 'Agotado', 0, 3, TRUE, TRUE, TRUE, 83, 3, CURRENT_TIMESTAMP - INTERVAL '119' DAY),
    (31, 'PG-031', 'Arreglo Floral Elegancia', 'Base de cerámica rosa tipo griego con rosas, lisianthus, orquídeas, mini rosas y follajes.', 2800.00, 3, 'Floreros', 'Orquídeas, Rosas', 'Disponible', 5, 3, TRUE, TRUE, TRUE, 30, 16, CURRENT_TIMESTAMP - INTERVAL '130' DAY),
    (32, 'PG-032', 'Arreglo Floral Alegría', 'Base de cerámica negra con gran variedad de flores y follajes.', 4200.00, 3, 'Floreros', '', 'Disponible', 12, 3, TRUE, TRUE, TRUE, 67, 12, CURRENT_TIMESTAMP - INTERVAL '141' DAY),
    (33, 'PG-033', 'Florero Ilusión', 'Florero de cristal con hortensia, clavel, lisianthus, rosas y follajes.', 1900.00, 3, 'Floreros', 'Claveles, Florero, Hortensias, Rosas', 'Disponible', 6, 3, TRUE, TRUE, TRUE, 14, 8, CURRENT_TIMESTAMP - INTERVAL '152' DAY),
    (34, 'PG-034', 'Florero Ilusión Secreta', 'Florero de cristal con hortensia, clavel, lisianthus, rosas, orquídeas, green y follajes.', 2200.00, 3, 'Floreros', 'Claveles, Florero, Hortensias, Orquídeas, Rosas', 'Disponible', 13, 3, TRUE, TRUE, TRUE, 51, 4, CURRENT_TIMESTAMP - INTERVAL '43' DAY),
    (35, 'PG-035', 'Pecera Reyna', 'Pecera de cristal con variedad de flores, hortensia, clavel, lisianthus, rosas, orquídeas, green y follajes.', 2800.00, 3, 'Floreros', 'Claveles, Hortensias, Orquídeas, Rosas', 'Disponible', 7, 3, TRUE, TRUE, TRUE, 88, 0, CURRENT_TIMESTAMP - INTERVAL '54' DAY),
    (36, 'PG-036', 'Pecera Color a Tu Vida', 'Gran variedad de flores: rosas, clavel, lisianthus, hortensia, gerbera, lilis y follajes.', 1900.00, 3, 'Floreros', 'Claveles, Gerberas, Hortensias, Rosas', 'Disponible', 14, 3, TRUE, TRUE, TRUE, 35, 13, CURRENT_TIMESTAMP - INTERVAL '65' DAY),
    (37, 'PG-037', 'Florero Amor Amor', '75 rosas y orquídeas.', 2200.00, 3, 'Floreros', 'Florero, Orquídeas, Rosas', 'Disponible', 8, 3, TRUE, TRUE, TRUE, 72, 9, CURRENT_TIMESTAMP - INTERVAL '76' DAY),
    (38, 'PG-038', 'Pecera Toque de Mar', 'Pecera de cristal con hortensia, flores preservadas premium, tulipanes y rosas.', 2800.00, 3, 'Floreros', 'Hortensias, Rosas, Tulipanes', 'Disponible', 15, 3, TRUE, TRUE, TRUE, 19, 5, CURRENT_TIMESTAMP - INTERVAL '87' DAY),
    (39, 'PG-039', 'Florero Suspiro', 'Gran variedad de rosas, lisianthus, gerberas y orquídeas en gran base de cerámica.', 3500.00, 3, 'Floreros', 'Florero, Gerberas, Orquídeas, Rosas', 'Disponible', 9, 3, TRUE, TRUE, TRUE, 56, 1, CURRENT_TIMESTAMP - INTERVAL '98' DAY),
    (40, 'PG-040', 'Florero Promesa de Amor', 'Florero en base de espejo con hortensias, clavel, lisianthus, orquídeas, rosas y polar.', 3500.00, 3, 'Floreros', 'Claveles, Florero, Hortensias, Orquídeas, Rosas', 'Disponible', 3, 3, TRUE, TRUE, TRUE, 3, 14, CURRENT_TIMESTAMP - INTERVAL '109' DAY),
    (41, 'PG-041', 'Jardín de Venus', 'Base de cerámica rosa con anturios rosa, rosas premium, mini rosas, lisianthus, perritos y trachelium morado.', 4300.00, 3, 'Floreros', 'Anturios, Rosas', 'Disponible', 10, 3, TRUE, TRUE, TRUE, 40, 10, CURRENT_TIMESTAMP - INTERVAL '120' DAY),
    (42, 'PG-042', 'Florero Amor Infinito', 'Base de cerámica con 50 rosas y follajes.', 1700.00, 3, 'Floreros', 'Florero, Rosas', 'Disponible', 4, 3, TRUE, TRUE, TRUE, 77, 6, CURRENT_TIMESTAMP - INTERVAL '131' DAY),
    (43, 'PG-043', 'Florero Máxima Elegancia', 'Gran pecera de cristal con 200 rosas rojas, variedad de orquídeas al costado y follajes.', 6900.00, 3, 'Floreros', 'Florero, Orquídeas, Rosas', 'Disponible', 11, 3, TRUE, TRUE, TRUE, 24, 2, CURRENT_TIMESTAMP - INTERVAL '142' DAY),
    (44, 'PG-044', 'Elegancia Pura', 'Ensaladera en espejo con 500 rosas de invernadero.', 12000.00, 3, 'Floreros', 'Rosas', 'Disponible', 5, 3, TRUE, TRUE, TRUE, 61, 15, CURRENT_TIMESTAMP - INTERVAL '153' DAY),
    (45, 'PG-045', 'Ramos de Rosas', 'Ramo de 12 o 24 rosas. Colores disponibles: rosa, morado, amarillo, blanco o rojo.', 540.00, 4, 'Ramos', 'Rosas', 'Disponible', 12, 3, TRUE, TRUE, TRUE, 8, 11, CURRENT_TIMESTAMP - INTERVAL '44' DAY),
    (46, 'PG-046', 'Ramo en Escalera', 'Ramo de 24 o 50 rosas. Colores disponibles: rosa, morado, amarillo, blanco o rojo.', 980.00, 4, 'Ramos', 'Rosas', 'Disponible', 6, 3, TRUE, TRUE, TRUE, 45, 7, CURRENT_TIMESTAMP - INTERVAL '55' DAY),
    (47, 'PG-047', 'Ramo Girasoles', 'Ramo de 10 girasoles.', 890.00, 4, 'Ramos', 'Girasoles', 'Disponible', 13, 3, TRUE, TRUE, TRUE, 82, 3, CURRENT_TIMESTAMP - INTERVAL '66' DAY),
    (48, 'PG-048', 'Ramo Calidez', 'Ramo de girasoles, rosas y astromelias.', 1100.00, 4, 'Ramos', 'Alstroemerias, Girasoles, Rosas', 'Disponible', 7, 3, TRUE, TRUE, TRUE, 29, 16, CURRENT_TIMESTAMP - INTERVAL '77' DAY),
    (49, 'PG-049', 'Ramo Elegancia 100 Rosas', 'Ramo de 100 rosas y orquídeas.', 2900.00, 4, 'Ramos', 'Orquídeas, Rosas', 'Disponible', 14, 3, TRUE, TRUE, TRUE, 66, 12, CURRENT_TIMESTAMP - INTERVAL '88' DAY),
    (50, 'PG-050', 'Ramo Amor en Letras', 'Ramo de 100 rosas en bouquet con letra personalizada.', 3200.00, 4, 'Ramos', 'Rosas', 'Disponible', 8, 3, TRUE, TRUE, TRUE, 13, 8, CURRENT_TIMESTAMP - INTERVAL '99' DAY),
    (51, 'PG-051', 'Ramo Momentos', 'Ramo de 100 rosas en bouquet con banda personalizada, orquídeas y fotografías.', 3200.00, 4, 'Ramos', 'Orquídeas, Rosas', 'Disponible', 15, 3, TRUE, TRUE, TRUE, 50, 4, CURRENT_TIMESTAMP - INTERVAL '110' DAY),
    (52, 'PG-052', 'Ramo Personalizado con Corona', 'Disponible con 50 o 100 rosas, corona y banda personalizada.', 1700.00, 4, 'Ramos', 'Rosas', 'Disponible', 9, 3, TRUE, TRUE, TRUE, 87, 0, CURRENT_TIMESTAMP - INTERVAL '121' DAY),
    (53, 'PG-053', 'Ramo Dulce Amor', 'Ramo de rosas y chocolates Ferrero.', 1500.00, 4, 'Ramos', 'Chocolates, Dulces, Rosas', 'Disponible', 3, 3, TRUE, TRUE, TRUE, 34, 13, CURRENT_TIMESTAMP - INTERVAL '132' DAY),
    (54, 'PG-054', 'Ramo Dulce Amor Jumbo', 'Ramo de 100 rosas y chocolates Ferrero.', 3900.00, 4, 'Ramos', 'Chocolates, Dulces, Rosas', 'Disponible', 10, 3, TRUE, TRUE, TRUE, 71, 9, CURRENT_TIMESTAMP - INTERVAL '143' DAY),
    (55, 'PG-055', 'Ramo Inspiración', 'Ramo de 100 rosas en tonos pastel.', 2800.00, 4, 'Ramos', 'Rosas', 'Agotado', 0, 3, TRUE, TRUE, TRUE, 18, 5, CURRENT_TIMESTAMP - INTERVAL '154' DAY),
    (56, 'PG-056', 'Ramos Clásicos', 'Ramos de 100, 200, 300 o 400 rosas. Colores disponibles: rosa, morado, amarillo, blanco o rojo.', 2800.00, 4, 'Ramos', 'Rosas', 'Disponible', 11, 3, TRUE, TRUE, TRUE, 55, 1, CURRENT_TIMESTAMP - INTERVAL '45' DAY),
    (57, 'PG-057', 'Ramo Color de Tus Ojos', 'Ramo con 18 gerberas.', 780.00, 4, 'Ramos', 'Gerberas', 'Disponible', 5, 3, TRUE, TRUE, TRUE, 2, 14, CURRENT_TIMESTAMP - INTERVAL '56' DAY),
    (58, 'PG-058', 'Ramo Luz de Día', 'Ramo de 20 girasoles, flores y follajes.', 1800.00, 4, 'Ramos', 'Girasoles', 'Disponible', 12, 3, TRUE, TRUE, TRUE, 39, 10, CURRENT_TIMESTAMP - INTERVAL '67' DAY),
    (59, 'PG-059', 'Ramo Peonías', 'Solo temporada. Disponible con 10 o 20 peonías.', 9200.00, 4, 'Ramos', 'Peonías', 'Disponible', 6, 3, TRUE, TRUE, TRUE, 76, 6, CURRENT_TIMESTAMP - INTERVAL '78' DAY),
    (60, 'PG-060', 'Círculo Romance', 'Caja personalizada con rosas rojas.', 980.00, 5, 'Cajas', 'Caja, Rosas', 'Disponible', 13, 3, TRUE, TRUE, TRUE, 23, 2, CURRENT_TIMESTAMP - INTERVAL '89' DAY),
    (61, 'PG-061', 'Caja Floral Corazón de Oro', 'Caja floral con rosas y chocolates Ferrero.', 1500.00, 5, 'Cajas', 'Caja, Chocolates, Rosas', 'Disponible', 7, 3, TRUE, TRUE, TRUE, 60, 15, CURRENT_TIMESTAMP - INTERVAL '100' DAY),
    (62, 'PG-062', 'Caja Floral Arcoíris', 'Caja floral con flor baby de colores.', 980.00, 5, 'Cajas', 'Caja', 'Disponible', 14, 3, TRUE, TRUE, TRUE, 7, 11, CURRENT_TIMESTAMP - INTERVAL '111' DAY),
    (63, 'PG-063', 'Caja Floral Sublime Amor', 'Caja floral con rosas en dos tonos y astromelia.', 1350.00, 5, 'Cajas', 'Alstroemerias, Caja, Rosas', 'Disponible', 8, 3, TRUE, TRUE, TRUE, 44, 7, CURRENT_TIMESTAMP - INTERVAL '122' DAY),
    (64, 'PG-064', 'Caja Floral Amor Mío', 'Caja floral con 50 o 100 rosas y orquídeas.', 1800.00, 5, 'Cajas', 'Caja, Orquídeas, Rosas', 'Disponible', 15, 3, TRUE, TRUE, TRUE, 81, 3, CURRENT_TIMESTAMP - INTERVAL '133' DAY),
    (65, 'PG-065', 'Caja Floral Alas de Ángel', 'Caja floral con rosas, lisianthus, anturios, girasoles, orquídeas y follajes.', 2800.00, 5, 'Cajas', 'Anturios, Caja, Girasoles, Orquídeas, Rosas', 'Disponible', 9, 3, TRUE, TRUE, TRUE, 28, 16, CURRENT_TIMESTAMP - INTERVAL '144' DAY),
    (66, 'PG-066', 'Caja Floral Magia', 'Caja floral con rosas, lisianthus, orquídeas, clavel, alelí, anturios y follajes exóticos.', 3600.00, 5, 'Cajas', 'Anturios, Caja, Claveles, Orquídeas, Rosas', 'Disponible', 3, 3, TRUE, TRUE, TRUE, 65, 12, CURRENT_TIMESTAMP - INTERVAL '155' DAY),
    (67, 'PG-067', 'Caja Floral Oso Cariñoso', 'Caja floral con figura de oso de flores naturales, anturios y rosas.', 2600.00, 5, 'Cajas', 'Anturios, Caja, Peluches, Rosas', 'Disponible', 10, 3, TRUE, TRUE, TRUE, 12, 8, CURRENT_TIMESTAMP - INTERVAL '46' DAY),
    (68, 'PG-068', 'Caja Floral Más Que Amor', 'Caja floral con rosas de distintos tonos, lisianthus, orquídeas, astromelia, mini rosa y follajes.', 4900.00, 5, 'Cajas', 'Alstroemerias, Caja, Orquídeas, Rosas', 'Disponible', 4, 3, TRUE, TRUE, TRUE, 49, 4, CURRENT_TIMESTAMP - INTERVAL '57' DAY),
    (69, 'PG-069', 'Caja Floral Sol en Tus Manos', 'Caja floral con 40 girasoles y follajes.', 3800.00, 5, 'Cajas', 'Caja, Girasoles', 'Disponible', 11, 3, TRUE, TRUE, TRUE, 86, 0, CURRENT_TIMESTAMP - INTERVAL '68' DAY),
    (70, 'PG-070', 'Caja de 6 Peonías Preservadas', 'Caja floral con 6 peonías preservadas. Duración aproximada: 2 años.', 1800.00, 5, 'Cajas', 'Caja, Peonías', 'Disponible', 5, 3, TRUE, TRUE, TRUE, 33, 13, CURRENT_TIMESTAMP - INTERVAL '79' DAY),
    (71, 'PG-071', 'Caja de 12 Peonías Preservadas', 'Caja floral con 12 peonías preservadas. Duración aproximada: 2 años.', 3500.00, 5, 'Cajas', 'Caja, Peonías', 'Disponible', 12, 3, TRUE, TRUE, TRUE, 70, 9, CURRENT_TIMESTAMP - INTERVAL '90' DAY),
    (72, 'PG-072', 'Orquídea en Esfera', 'Orquídea de dos varas en base esfera.', 980.00, 6, 'Orquídeas', 'Orquídeas', 'Disponible', 6, 3, TRUE, TRUE, TRUE, 17, 5, CURRENT_TIMESTAMP - INTERVAL '101' DAY),
    (73, 'PG-073', 'Orquídea en Espejo', 'Orquídea en base cerámica de 2 o 4 varas.', 1600.00, 6, 'Orquídeas', 'Orquídeas', 'Disponible', 13, 3, TRUE, TRUE, TRUE, 54, 1, CURRENT_TIMESTAMP - INTERVAL '112' DAY),
    (74, 'PG-074', 'Mini Orquídea Dalí', 'Mini orquídea de dos varas en base de cristal.', 980.00, 6, 'Orquídeas', 'Orquídeas', 'Disponible', 7, 3, TRUE, TRUE, TRUE, 1, 14, CURRENT_TIMESTAMP - INTERVAL '123' DAY),
    (75, 'PG-075', 'Mini Orquídeas Arcoíris', '6 varas de mini orquídeas en base cerámica.', 2900.00, 6, 'Orquídeas', 'Orquídeas', 'Disponible', 14, 3, TRUE, TRUE, TRUE, 38, 10, CURRENT_TIMESTAMP - INTERVAL '134' DAY),
    (76, 'PG-076', 'Orquídea Radiante', 'Orquídea en base cerámica de 4 o 6 varas con follajes y suculenta.', 2800.00, 6, 'Orquídeas', 'Orquídeas', 'Disponible', 8, 3, TRUE, TRUE, TRUE, 75, 6, CURRENT_TIMESTAMP - INTERVAL '145' DAY),
    (77, 'PG-077', 'Orquídea Pureza', 'Orquídea en base cerámica de 4 o 6 varas con follajes y suculenta.', 2600.00, 6, 'Orquídeas', 'Orquídeas', 'Disponible', 15, 3, TRUE, TRUE, TRUE, 22, 2, CURRENT_TIMESTAMP - INTERVAL '156' DAY),
    (78, 'PG-078', 'Orquídea Cascada', 'Orquídea de 2 varas en base cerámica.', 2300.00, 6, 'Orquídeas', 'Orquídeas', 'Disponible', 9, 3, TRUE, TRUE, TRUE, 59, 15, CURRENT_TIMESTAMP - INTERVAL '47' DAY),
    (79, 'PG-079', 'Orquídeas Paraíso', 'Orquídea de 10 varas en bonsayera.', 8900.00, 6, 'Orquídeas', 'Orquídeas', 'Disponible', 3, 3, TRUE, TRUE, TRUE, 6, 11, CURRENT_TIMESTAMP - INTERVAL '58' DAY),
    (80, 'PG-080', 'Jardín de Orquídeas', 'Orquídea de 6 varas en base de madera rústica, suculentas y tronco.', 3500.00, 6, 'Orquídeas', 'Orquídeas', 'Agotado', 0, 3, TRUE, TRUE, TRUE, 43, 7, CURRENT_TIMESTAMP - INTERVAL '69' DAY),
    (81, 'PG-081', 'Orquídeas Gran Edén', 'Orquídea de 9 varas en base de cerámica.', 4600.00, 6, 'Orquídeas', 'Orquídeas', 'Disponible', 4, 3, TRUE, TRUE, TRUE, 80, 3, CURRENT_TIMESTAMP - INTERVAL '80' DAY),
    (82, 'PG-082', 'Caja Floral con 10 Varas de Orquídeas', 'Caja floral jumbo con gran variedad de flores y 10 varas de orquídeas al centro.', 6300.00, 6, 'Orquídeas', 'Caja, Orquídeas', 'Disponible', 11, 3, TRUE, TRUE, TRUE, 27, 16, CURRENT_TIMESTAMP - INTERVAL '91' DAY),
    (83, 'PG-083', 'Chocolates Ferrero', 'Presentaciones de 16 o 24 piezas.', 320.00, 7, 'Adicionales', 'Chocolates', 'Disponible', 5, 3, TRUE, FALSE, TRUE, 64, 12, CURRENT_TIMESTAMP - INTERVAL '5' DAY),
    (84, 'PG-084', 'Oso de Amor', 'Oso de peluche para complementar tu arreglo.', 480.00, 7, 'Adicionales', 'Peluches', 'Disponible', 12, 3, TRUE, FALSE, TRUE, 11, 8, CURRENT_TIMESTAMP - INTERVAL '6' DAY),
    (85, 'PG-085', 'Globo Personalizado', 'Globo personalizado.', 380.00, 7, 'Adicionales', 'Globos', 'Disponible', 6, 3, TRUE, FALSE, TRUE, 48, 4, CURRENT_TIMESTAMP - INTERVAL '7' DAY),
    (86, 'PG-086', 'Caja Dulce Amor', 'Caja botanera con variedad de dulces y botanas.', 890.00, 7, 'Adicionales', 'Caja, Dulces', 'Disponible', 13, 3, TRUE, FALSE, TRUE, 85, 0, CURRENT_TIMESTAMP - INTERVAL '3' DAY),
    (87, 'PG-087', 'Caja Personalizada Dulce Alegría', 'Caja personalizada con variedad de dulces y botanas.', 980.00, 7, 'Adicionales', 'Caja, Dulces', 'Disponible', 7, 3, TRUE, FALSE, TRUE, 32, 13, CURRENT_TIMESTAMP - INTERVAL '4' DAY),
    (88, 'PG-088', 'Carrito Botanero & Bar', 'Carrito botanero & bar.', 1600.00, 7, 'Adicionales', '', 'Disponible', 14, 3, TRUE, FALSE, TRUE, 69, 9, CURRENT_TIMESTAMP - INTERVAL '5' DAY);

-- Bases de datos creadas antes: los arreglos de prueba (sin SKU) se reemplazan una sola vez por el catálogo real.
UPDATE ArregloFloral SET sku = 'PG-001', nombre = 'Ramo Dulce Encanto', detalles = 'Gerberas rosadas, rosas crema, rosas spray rosadas, crisantemo pompón blanco, margaritas rosadas, gypsophila, alstroemeria blanca y eucalipto.', costo = 940.00, idTipoArreglo = 1, categoria = 'Temporada', etiqueta = 'Alstroemerias, Dulces, Eucalipto, Gerberas, Gypsophila, Margaritas, Rosas', disponibilidad = 'Disponible', existencias = 3, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 0, ventas = 0, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '40' DAY WHERE idArregloFloral = 1 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-002', nombre = 'Ramo Tierna Ilusión', detalles = 'Ramo de 24 rosas y 8 gerberas.', costo = 1200.00, idTipoArreglo = 1, categoria = 'Temporada', etiqueta = 'Gerberas, Rosas', disponibilidad = 'Disponible', existencias = 10, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 37, ventas = 13, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '51' DAY WHERE idArregloFloral = 2 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-003', nombre = 'Ramo Noche Estrellada', detalles = 'Ramo floral con gran variedad de flores: rosas, margaritas, girasol y follajes.', costo = 1200.00, idTipoArreglo = 1, categoria = 'Temporada', etiqueta = 'Girasoles, Margaritas, Rosas', disponibilidad = 'Disponible', existencias = 4, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 74, ventas = 9, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '62' DAY WHERE idArregloFloral = 3 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-004', nombre = 'La Incondicional', detalles = 'Ramo inspirado en una de las canciones más icónicas de Luis Miguel, con rosas blancas y negras y código QR musical.', costo = 1200.00, idTipoArreglo = 1, categoria = 'Temporada', etiqueta = 'Rosas', disponibilidad = 'Disponible', existencias = 11, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 21, ventas = 5, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '73' DAY WHERE idArregloFloral = 4 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-005', nombre = 'Ramo Rosa Palo', detalles = 'Ramo de 50 rosas y 8 gerberas.', costo = 1700.00, idTipoArreglo = 1, categoria = 'Temporada', etiqueta = 'Gerberas, Rosas', disponibilidad = 'Disponible', existencias = 5, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 58, ventas = 1, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '84' DAY WHERE idArregloFloral = 5 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-006', nombre = 'Amor Carmesí', detalles = 'Rosas rojas, gerberas mini rosadas, claveles rosados, alstroemerias rosadas y follaje.', costo = 1800.00, idTipoArreglo = 1, categoria = 'Temporada', etiqueta = 'Alstroemerias, Claveles, Gerberas, Rosas', disponibilidad = 'Disponible', existencias = 12, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 5, ventas = 14, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '95' DAY WHERE idArregloFloral = 6 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-007', nombre = 'Ramo Aura Floral', detalles = 'Rosas rosadas, rosas spray, hortensias azul/lila, claveles, lisianthus, cymbidium, lilium oriental, narcisos, delphinium, hypericum, matricaria y follaje verde.', costo = 3900.00, idTipoArreglo = 1, categoria = 'Temporada', etiqueta = 'Claveles, Hortensias, Rosas', disponibilidad = 'Agotado', existencias = 0, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 42, ventas = 10, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '106' DAY WHERE idArregloFloral = 7 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-008', nombre = 'Euforia Rosa', detalles = 'Composición floral en tonos rosa con rosas, hortensias, claveles, lisianthus y follajes.', costo = 2600.00, idTipoArreglo = 1, categoria = 'Temporada', etiqueta = 'Claveles, Hortensias, Rosas', disponibilidad = 'Disponible', existencias = 13, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 79, ventas = 6, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '117' DAY WHERE idArregloFloral = 8 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-009', nombre = 'Cien Razones', detalles = '100 rosas y tulipanes.', costo = 2800.00, idTipoArreglo = 1, categoria = 'Temporada', etiqueta = 'Rosas, Tulipanes', disponibilidad = 'Disponible', existencias = 7, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 26, ventas = 2, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '128' DAY WHERE idArregloFloral = 9 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-010', nombre = 'Euforia Carmesí', detalles = '100 rosas, lirios y dólar.', costo = 2800.00, idTipoArreglo = 1, categoria = 'Temporada', etiqueta = 'Lirios, Rosas', disponibilidad = 'Disponible', existencias = 14, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 63, ventas = 15, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '139' DAY WHERE idArregloFloral = 10 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-011', nombre = 'Caja Floral Gran Amor', detalles = 'Caja floral con 50 rosas y follajes, oso de peluche y globo personalizado.', costo = 2100.00, idTipoArreglo = 1, categoria = 'Temporada', etiqueta = 'Caja, Globos, Peluches, Rosas', disponibilidad = 'Disponible', existencias = 8, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 10, ventas = 11, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '150' DAY WHERE idArregloFloral = 11 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-012', nombre = 'Reflejo de Amor', detalles = '100 rosas, baby y espejo al centro personalizado.', costo = 2800.00, idTipoArreglo = 1, categoria = 'Temporada', etiqueta = 'Rosas', disponibilidad = 'Disponible', existencias = 15, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 47, ventas = 7, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '41' DAY WHERE idArregloFloral = 12 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-013', nombre = 'Reyna de Rosas', detalles = '150 rosas rojas.', costo = 3900.00, idTipoArreglo = 1, categoria = 'Temporada', etiqueta = 'Rosas', disponibilidad = 'Disponible', existencias = 9, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 84, ventas = 3, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '52' DAY WHERE idArregloFloral = 13 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-014', nombre = 'Jardín de Tulipanes', detalles = 'Caja floral con 20 tulipanes, follajes y green alrededor en forma de jardinera.', costo = 2800.00, idTipoArreglo = 1, categoria = 'Temporada', etiqueta = 'Caja, Tulipanes', disponibilidad = 'Disponible', existencias = 3, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 31, ventas = 16, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '63' DAY WHERE idArregloFloral = 14 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-015', nombre = 'Sueño en Rosa', detalles = 'Cerámica floral con gran variedad de flores: rosas, mini rosas, lilis, mini gerbera, pompón, crisantemo, escabiosa, hipérico, eucalipto y follajes.', costo = 2800.00, idTipoArreglo = 1, categoria = 'Temporada', etiqueta = 'Eucalipto, Gerberas, Rosas', disponibilidad = 'Disponible', existencias = 10, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 68, ventas = 12, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '74' DAY WHERE idArregloFloral = 15 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-016', nombre = 'Caja Floral Mi Amor', detalles = 'Caja floral con 50 rosas rojas y follajes.', costo = 1900.00, idTipoArreglo = 1, categoria = 'Temporada', etiqueta = 'Caja, Rosas', disponibilidad = 'Disponible', existencias = 4, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 15, ventas = 8, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '85' DAY WHERE idArregloFloral = 16 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-017', nombre = 'Vaso Floral de Starbucks', detalles = 'Vaso térmico Starbucks con gran variedad de flores, lisianthus, rosas y clavel.', costo = 1500.00, idTipoArreglo = 1, categoria = 'Temporada', etiqueta = 'Claveles, Rosas', disponibilidad = 'Disponible', existencias = 11, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 52, ventas = 4, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '96' DAY WHERE idArregloFloral = 17 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-018', nombre = 'Edén Floral', detalles = 'Base de madera con gran variedad de flores al centro y una orquídea.', costo = 2800.00, idTipoArreglo = 1, categoria = 'Temporada', etiqueta = 'Orquídeas', disponibilidad = 'Disponible', existencias = 5, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 89, ventas = 0, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '107' DAY WHERE idArregloFloral = 18 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-019', nombre = 'Bolsa Floral Amor a la Mexicana', detalles = 'Bolsa hecha a mano con gran variedad de flores: rosa, lisianthus, anturio, perrito, gerbera importada, mini rosa y follajes.', costo = 2200.00, idTipoArreglo = 1, categoria = 'Temporada', etiqueta = 'Anturios, Gerberas, Rosas', disponibilidad = 'Disponible', existencias = 12, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 36, ventas = 13, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '118' DAY WHERE idArregloFloral = 19 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-020', nombre = 'Bolsa Floral Rojo Amor', detalles = 'Bolsa hecha a mano con gran variedad de flores: rosa, lisianthus, anturio, perrito, gerbera importada, mini rosa y follajes.', costo = 2200.00, idTipoArreglo = 1, categoria = 'Temporada', etiqueta = 'Anturios, Gerberas, Rosas', disponibilidad = 'Disponible', existencias = 6, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 73, ventas = 9, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '129' DAY WHERE idArregloFloral = 20 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-021', nombre = 'Florero Amor y Pasión', detalles = 'Base de cerámica negra con rosas, tulipanes, lisianthus, clavel, anturio y follajes.', costo = 3200.00, idTipoArreglo = 1, categoria = 'Temporada', etiqueta = 'Anturios, Claveles, Florero, Rosas, Tulipanes', disponibilidad = 'Disponible', existencias = 13, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 20, ventas = 5, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '140' DAY WHERE idArregloFloral = 21 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-022', nombre = 'Florero Dulzura', detalles = 'Rosas en tono fucsia y varas de lilis en florero de cristal transparente.', costo = 740.00, idTipoArreglo = 2, categoria = 'Detalles', etiqueta = 'Florero, Rosas', disponibilidad = 'Disponible', existencias = 7, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 57, ventas = 1, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '151' DAY WHERE idArregloFloral = 22 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-023', nombre = 'Florero Algodón de Azúcar', detalles = 'Gerberas en tonos rosas acompañadas con astromelia en un florero tornasol.', costo = 780.00, idTipoArreglo = 2, categoria = 'Detalles', etiqueta = 'Alstroemerias, Florero, Gerberas, Rosas', disponibilidad = 'Disponible', existencias = 14, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 4, ventas = 14, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '42' DAY WHERE idArregloFloral = 23 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-024', nombre = 'Florero Gran Detalle', detalles = 'Florero de cristal con rosas en tonos rosas y astromelias.', costo = 720.00, idTipoArreglo = 2, categoria = 'Detalles', etiqueta = 'Alstroemerias, Florero, Rosas', disponibilidad = 'Disponible', existencias = 8, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 41, ventas = 10, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '53' DAY WHERE idArregloFloral = 24 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-025', nombre = 'Florero Carmesí', detalles = '12 rosas rojas en florero de cristal.', costo = 720.00, idTipoArreglo = 2, categoria = 'Detalles', etiqueta = 'Florero, Rosas', disponibilidad = 'Disponible', existencias = 15, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 78, ventas = 6, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '64' DAY WHERE idArregloFloral = 25 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-026', nombre = 'Combo Love Love', detalles = 'Ramo de 24 rosas y follajes con oso de peluche.', costo = 1100.00, idTipoArreglo = 2, categoria = 'Detalles', etiqueta = 'Peluches, Rosas', disponibilidad = 'Disponible', existencias = 9, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 25, ventas = 2, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '75' DAY WHERE idArregloFloral = 26 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-027', nombre = 'Florero Amor', detalles = 'Disponible con 12, 24, 50 o 100 rosas. Se pueden elegir tonos rosa, morado, amarillo, blanco o rojo.', costo = 720.00, idTipoArreglo = 3, categoria = 'Floreros', etiqueta = 'Florero, Rosas', disponibilidad = 'Disponible', existencias = 3, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 62, ventas = 15, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '86' DAY WHERE idArregloFloral = 27 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-028', nombre = 'Pecera Eterno Amanecer', detalles = 'Pecera de cristal con follajes preservados y rosas anaranjadas.', costo = 1300.00, idTipoArreglo = 3, categoria = 'Floreros', etiqueta = 'Rosas', disponibilidad = 'Disponible', existencias = 10, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 9, ventas = 11, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '97' DAY WHERE idArregloFloral = 28 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-029', nombre = 'Florero Tulipanes', detalles = '10 o 20 tulipanes en florero de cristal. Tonos fucsia, morado, amarillo, naranja y bicolor.', costo = 1100.00, idTipoArreglo = 3, categoria = 'Floreros', etiqueta = 'Florero, Tulipanes', disponibilidad = 'Disponible', existencias = 4, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 46, ventas = 7, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '108' DAY WHERE idArregloFloral = 29 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-030', nombre = 'Arreglo Floral Ternura', detalles = 'Base de cerámica rosa tipo griego con gran variedad de flores y follajes.', costo = 3200.00, idTipoArreglo = 3, categoria = 'Floreros', etiqueta = 'Rosas', disponibilidad = 'Agotado', existencias = 0, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 83, ventas = 3, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '119' DAY WHERE idArregloFloral = 30 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-031', nombre = 'Arreglo Floral Elegancia', detalles = 'Base de cerámica rosa tipo griego con rosas, lisianthus, orquídeas, mini rosas y follajes.', costo = 2800.00, idTipoArreglo = 3, categoria = 'Floreros', etiqueta = 'Orquídeas, Rosas', disponibilidad = 'Disponible', existencias = 5, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 30, ventas = 16, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '130' DAY WHERE idArregloFloral = 31 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-032', nombre = 'Arreglo Floral Alegría', detalles = 'Base de cerámica negra con gran variedad de flores y follajes.', costo = 4200.00, idTipoArreglo = 3, categoria = 'Floreros', etiqueta = '', disponibilidad = 'Disponible', existencias = 12, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 67, ventas = 12, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '141' DAY WHERE idArregloFloral = 32 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-033', nombre = 'Florero Ilusión', detalles = 'Florero de cristal con hortensia, clavel, lisianthus, rosas y follajes.', costo = 1900.00, idTipoArreglo = 3, categoria = 'Floreros', etiqueta = 'Claveles, Florero, Hortensias, Rosas', disponibilidad = 'Disponible', existencias = 6, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 14, ventas = 8, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '152' DAY WHERE idArregloFloral = 33 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-034', nombre = 'Florero Ilusión Secreta', detalles = 'Florero de cristal con hortensia, clavel, lisianthus, rosas, orquídeas, green y follajes.', costo = 2200.00, idTipoArreglo = 3, categoria = 'Floreros', etiqueta = 'Claveles, Florero, Hortensias, Orquídeas, Rosas', disponibilidad = 'Disponible', existencias = 13, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 51, ventas = 4, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '43' DAY WHERE idArregloFloral = 34 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-035', nombre = 'Pecera Reyna', detalles = 'Pecera de cristal con variedad de flores, hortensia, clavel, lisianthus, rosas, orquídeas, green y follajes.', costo = 2800.00, idTipoArreglo = 3, categoria = 'Floreros', etiqueta = 'Claveles, Hortensias, Orquídeas, Rosas', disponibilidad = 'Disponible', existencias = 7, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 88, ventas = 0, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '54' DAY WHERE idArregloFloral = 35 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-036', nombre = 'Pecera Color a Tu Vida', detalles = 'Gran variedad de flores: rosas, clavel, lisianthus, hortensia, gerbera, lilis y follajes.', costo = 1900.00, idTipoArreglo = 3, categoria = 'Floreros', etiqueta = 'Claveles, Gerberas, Hortensias, Rosas', disponibilidad = 'Disponible', existencias = 14, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 35, ventas = 13, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '65' DAY WHERE idArregloFloral = 36 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-037', nombre = 'Florero Amor Amor', detalles = '75 rosas y orquídeas.', costo = 2200.00, idTipoArreglo = 3, categoria = 'Floreros', etiqueta = 'Florero, Orquídeas, Rosas', disponibilidad = 'Disponible', existencias = 8, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 72, ventas = 9, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '76' DAY WHERE idArregloFloral = 37 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-038', nombre = 'Pecera Toque de Mar', detalles = 'Pecera de cristal con hortensia, flores preservadas premium, tulipanes y rosas.', costo = 2800.00, idTipoArreglo = 3, categoria = 'Floreros', etiqueta = 'Hortensias, Rosas, Tulipanes', disponibilidad = 'Disponible', existencias = 15, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 19, ventas = 5, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '87' DAY WHERE idArregloFloral = 38 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-039', nombre = 'Florero Suspiro', detalles = 'Gran variedad de rosas, lisianthus, gerberas y orquídeas en gran base de cerámica.', costo = 3500.00, idTipoArreglo = 3, categoria = 'Floreros', etiqueta = 'Florero, Gerberas, Orquídeas, Rosas', disponibilidad = 'Disponible', existencias = 9, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 56, ventas = 1, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '98' DAY WHERE idArregloFloral = 39 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-040', nombre = 'Florero Promesa de Amor', detalles = 'Florero en base de espejo con hortensias, clavel, lisianthus, orquídeas, rosas y polar.', costo = 3500.00, idTipoArreglo = 3, categoria = 'Floreros', etiqueta = 'Claveles, Florero, Hortensias, Orquídeas, Rosas', disponibilidad = 'Disponible', existencias = 3, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 3, ventas = 14, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '109' DAY WHERE idArregloFloral = 40 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-041', nombre = 'Jardín de Venus', detalles = 'Base de cerámica rosa con anturios rosa, rosas premium, mini rosas, lisianthus, perritos y trachelium morado.', costo = 4300.00, idTipoArreglo = 3, categoria = 'Floreros', etiqueta = 'Anturios, Rosas', disponibilidad = 'Disponible', existencias = 10, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 40, ventas = 10, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '120' DAY WHERE idArregloFloral = 41 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-042', nombre = 'Florero Amor Infinito', detalles = 'Base de cerámica con 50 rosas y follajes.', costo = 1700.00, idTipoArreglo = 3, categoria = 'Floreros', etiqueta = 'Florero, Rosas', disponibilidad = 'Disponible', existencias = 4, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 77, ventas = 6, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '131' DAY WHERE idArregloFloral = 42 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-043', nombre = 'Florero Máxima Elegancia', detalles = 'Gran pecera de cristal con 200 rosas rojas, variedad de orquídeas al costado y follajes.', costo = 6900.00, idTipoArreglo = 3, categoria = 'Floreros', etiqueta = 'Florero, Orquídeas, Rosas', disponibilidad = 'Disponible', existencias = 11, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 24, ventas = 2, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '142' DAY WHERE idArregloFloral = 43 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-044', nombre = 'Elegancia Pura', detalles = 'Ensaladera en espejo con 500 rosas de invernadero.', costo = 12000.00, idTipoArreglo = 3, categoria = 'Floreros', etiqueta = 'Rosas', disponibilidad = 'Disponible', existencias = 5, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 61, ventas = 15, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '153' DAY WHERE idArregloFloral = 44 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-045', nombre = 'Ramos de Rosas', detalles = 'Ramo de 12 o 24 rosas. Colores disponibles: rosa, morado, amarillo, blanco o rojo.', costo = 540.00, idTipoArreglo = 4, categoria = 'Ramos', etiqueta = 'Rosas', disponibilidad = 'Disponible', existencias = 12, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 8, ventas = 11, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '44' DAY WHERE idArregloFloral = 45 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-046', nombre = 'Ramo en Escalera', detalles = 'Ramo de 24 o 50 rosas. Colores disponibles: rosa, morado, amarillo, blanco o rojo.', costo = 980.00, idTipoArreglo = 4, categoria = 'Ramos', etiqueta = 'Rosas', disponibilidad = 'Disponible', existencias = 6, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 45, ventas = 7, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '55' DAY WHERE idArregloFloral = 46 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-047', nombre = 'Ramo Girasoles', detalles = 'Ramo de 10 girasoles.', costo = 890.00, idTipoArreglo = 4, categoria = 'Ramos', etiqueta = 'Girasoles', disponibilidad = 'Disponible', existencias = 13, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 82, ventas = 3, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '66' DAY WHERE idArregloFloral = 47 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-048', nombre = 'Ramo Calidez', detalles = 'Ramo de girasoles, rosas y astromelias.', costo = 1100.00, idTipoArreglo = 4, categoria = 'Ramos', etiqueta = 'Alstroemerias, Girasoles, Rosas', disponibilidad = 'Disponible', existencias = 7, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 29, ventas = 16, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '77' DAY WHERE idArregloFloral = 48 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-049', nombre = 'Ramo Elegancia 100 Rosas', detalles = 'Ramo de 100 rosas y orquídeas.', costo = 2900.00, idTipoArreglo = 4, categoria = 'Ramos', etiqueta = 'Orquídeas, Rosas', disponibilidad = 'Disponible', existencias = 14, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 66, ventas = 12, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '88' DAY WHERE idArregloFloral = 49 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-050', nombre = 'Ramo Amor en Letras', detalles = 'Ramo de 100 rosas en bouquet con letra personalizada.', costo = 3200.00, idTipoArreglo = 4, categoria = 'Ramos', etiqueta = 'Rosas', disponibilidad = 'Disponible', existencias = 8, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 13, ventas = 8, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '99' DAY WHERE idArregloFloral = 50 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-051', nombre = 'Ramo Momentos', detalles = 'Ramo de 100 rosas en bouquet con banda personalizada, orquídeas y fotografías.', costo = 3200.00, idTipoArreglo = 4, categoria = 'Ramos', etiqueta = 'Orquídeas, Rosas', disponibilidad = 'Disponible', existencias = 15, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 50, ventas = 4, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '110' DAY WHERE idArregloFloral = 51 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-052', nombre = 'Ramo Personalizado con Corona', detalles = 'Disponible con 50 o 100 rosas, corona y banda personalizada.', costo = 1700.00, idTipoArreglo = 4, categoria = 'Ramos', etiqueta = 'Rosas', disponibilidad = 'Disponible', existencias = 9, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 87, ventas = 0, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '121' DAY WHERE idArregloFloral = 52 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-053', nombre = 'Ramo Dulce Amor', detalles = 'Ramo de rosas y chocolates Ferrero.', costo = 1500.00, idTipoArreglo = 4, categoria = 'Ramos', etiqueta = 'Chocolates, Dulces, Rosas', disponibilidad = 'Disponible', existencias = 3, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 34, ventas = 13, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '132' DAY WHERE idArregloFloral = 53 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-054', nombre = 'Ramo Dulce Amor Jumbo', detalles = 'Ramo de 100 rosas y chocolates Ferrero.', costo = 3900.00, idTipoArreglo = 4, categoria = 'Ramos', etiqueta = 'Chocolates, Dulces, Rosas', disponibilidad = 'Disponible', existencias = 10, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 71, ventas = 9, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '143' DAY WHERE idArregloFloral = 54 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-055', nombre = 'Ramo Inspiración', detalles = 'Ramo de 100 rosas en tonos pastel.', costo = 2800.00, idTipoArreglo = 4, categoria = 'Ramos', etiqueta = 'Rosas', disponibilidad = 'Agotado', existencias = 0, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 18, ventas = 5, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '154' DAY WHERE idArregloFloral = 55 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-056', nombre = 'Ramos Clásicos', detalles = 'Ramos de 100, 200, 300 o 400 rosas. Colores disponibles: rosa, morado, amarillo, blanco o rojo.', costo = 2800.00, idTipoArreglo = 4, categoria = 'Ramos', etiqueta = 'Rosas', disponibilidad = 'Disponible', existencias = 11, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 55, ventas = 1, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '45' DAY WHERE idArregloFloral = 56 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-057', nombre = 'Ramo Color de Tus Ojos', detalles = 'Ramo con 18 gerberas.', costo = 780.00, idTipoArreglo = 4, categoria = 'Ramos', etiqueta = 'Gerberas', disponibilidad = 'Disponible', existencias = 5, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 2, ventas = 14, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '56' DAY WHERE idArregloFloral = 57 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-058', nombre = 'Ramo Luz de Día', detalles = 'Ramo de 20 girasoles, flores y follajes.', costo = 1800.00, idTipoArreglo = 4, categoria = 'Ramos', etiqueta = 'Girasoles', disponibilidad = 'Disponible', existencias = 12, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 39, ventas = 10, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '67' DAY WHERE idArregloFloral = 58 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-059', nombre = 'Ramo Peonías', detalles = 'Solo temporada. Disponible con 10 o 20 peonías.', costo = 9200.00, idTipoArreglo = 4, categoria = 'Ramos', etiqueta = 'Peonías', disponibilidad = 'Disponible', existencias = 6, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 76, ventas = 6, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '78' DAY WHERE idArregloFloral = 59 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-060', nombre = 'Círculo Romance', detalles = 'Caja personalizada con rosas rojas.', costo = 980.00, idTipoArreglo = 5, categoria = 'Cajas', etiqueta = 'Caja, Rosas', disponibilidad = 'Disponible', existencias = 13, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 23, ventas = 2, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '89' DAY WHERE idArregloFloral = 60 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-061', nombre = 'Caja Floral Corazón de Oro', detalles = 'Caja floral con rosas y chocolates Ferrero.', costo = 1500.00, idTipoArreglo = 5, categoria = 'Cajas', etiqueta = 'Caja, Chocolates, Rosas', disponibilidad = 'Disponible', existencias = 7, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 60, ventas = 15, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '100' DAY WHERE idArregloFloral = 61 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-062', nombre = 'Caja Floral Arcoíris', detalles = 'Caja floral con flor baby de colores.', costo = 980.00, idTipoArreglo = 5, categoria = 'Cajas', etiqueta = 'Caja', disponibilidad = 'Disponible', existencias = 14, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 7, ventas = 11, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '111' DAY WHERE idArregloFloral = 62 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-063', nombre = 'Caja Floral Sublime Amor', detalles = 'Caja floral con rosas en dos tonos y astromelia.', costo = 1350.00, idTipoArreglo = 5, categoria = 'Cajas', etiqueta = 'Alstroemerias, Caja, Rosas', disponibilidad = 'Disponible', existencias = 8, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 44, ventas = 7, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '122' DAY WHERE idArregloFloral = 63 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-064', nombre = 'Caja Floral Amor Mío', detalles = 'Caja floral con 50 o 100 rosas y orquídeas.', costo = 1800.00, idTipoArreglo = 5, categoria = 'Cajas', etiqueta = 'Caja, Orquídeas, Rosas', disponibilidad = 'Disponible', existencias = 15, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 81, ventas = 3, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '133' DAY WHERE idArregloFloral = 64 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-065', nombre = 'Caja Floral Alas de Ángel', detalles = 'Caja floral con rosas, lisianthus, anturios, girasoles, orquídeas y follajes.', costo = 2800.00, idTipoArreglo = 5, categoria = 'Cajas', etiqueta = 'Anturios, Caja, Girasoles, Orquídeas, Rosas', disponibilidad = 'Disponible', existencias = 9, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 28, ventas = 16, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '144' DAY WHERE idArregloFloral = 65 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-066', nombre = 'Caja Floral Magia', detalles = 'Caja floral con rosas, lisianthus, orquídeas, clavel, alelí, anturios y follajes exóticos.', costo = 3600.00, idTipoArreglo = 5, categoria = 'Cajas', etiqueta = 'Anturios, Caja, Claveles, Orquídeas, Rosas', disponibilidad = 'Disponible', existencias = 3, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 65, ventas = 12, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '155' DAY WHERE idArregloFloral = 66 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-067', nombre = 'Caja Floral Oso Cariñoso', detalles = 'Caja floral con figura de oso de flores naturales, anturios y rosas.', costo = 2600.00, idTipoArreglo = 5, categoria = 'Cajas', etiqueta = 'Anturios, Caja, Peluches, Rosas', disponibilidad = 'Disponible', existencias = 10, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 12, ventas = 8, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '46' DAY WHERE idArregloFloral = 67 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-068', nombre = 'Caja Floral Más Que Amor', detalles = 'Caja floral con rosas de distintos tonos, lisianthus, orquídeas, astromelia, mini rosa y follajes.', costo = 4900.00, idTipoArreglo = 5, categoria = 'Cajas', etiqueta = 'Alstroemerias, Caja, Orquídeas, Rosas', disponibilidad = 'Disponible', existencias = 4, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 49, ventas = 4, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '57' DAY WHERE idArregloFloral = 68 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-069', nombre = 'Caja Floral Sol en Tus Manos', detalles = 'Caja floral con 40 girasoles y follajes.', costo = 3800.00, idTipoArreglo = 5, categoria = 'Cajas', etiqueta = 'Caja, Girasoles', disponibilidad = 'Disponible', existencias = 11, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 86, ventas = 0, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '68' DAY WHERE idArregloFloral = 69 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-070', nombre = 'Caja de 6 Peonías Preservadas', detalles = 'Caja floral con 6 peonías preservadas. Duración aproximada: 2 años.', costo = 1800.00, idTipoArreglo = 5, categoria = 'Cajas', etiqueta = 'Caja, Peonías', disponibilidad = 'Disponible', existencias = 5, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 33, ventas = 13, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '79' DAY WHERE idArregloFloral = 70 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-071', nombre = 'Caja de 12 Peonías Preservadas', detalles = 'Caja floral con 12 peonías preservadas. Duración aproximada: 2 años.', costo = 3500.00, idTipoArreglo = 5, categoria = 'Cajas', etiqueta = 'Caja, Peonías', disponibilidad = 'Disponible', existencias = 12, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 70, ventas = 9, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '90' DAY WHERE idArregloFloral = 71 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-072', nombre = 'Orquídea en Esfera', detalles = 'Orquídea de dos varas en base esfera.', costo = 980.00, idTipoArreglo = 6, categoria = 'Orquídeas', etiqueta = 'Orquídeas', disponibilidad = 'Disponible', existencias = 6, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 17, ventas = 5, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '101' DAY WHERE idArregloFloral = 72 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-073', nombre = 'Orquídea en Espejo', detalles = 'Orquídea en base cerámica de 2 o 4 varas.', costo = 1600.00, idTipoArreglo = 6, categoria = 'Orquídeas', etiqueta = 'Orquídeas', disponibilidad = 'Disponible', existencias = 13, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 54, ventas = 1, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '112' DAY WHERE idArregloFloral = 73 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-074', nombre = 'Mini Orquídea Dalí', detalles = 'Mini orquídea de dos varas en base de cristal.', costo = 980.00, idTipoArreglo = 6, categoria = 'Orquídeas', etiqueta = 'Orquídeas', disponibilidad = 'Disponible', existencias = 7, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 1, ventas = 14, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '123' DAY WHERE idArregloFloral = 74 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-075', nombre = 'Mini Orquídeas Arcoíris', detalles = '6 varas de mini orquídeas en base cerámica.', costo = 2900.00, idTipoArreglo = 6, categoria = 'Orquídeas', etiqueta = 'Orquídeas', disponibilidad = 'Disponible', existencias = 14, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 38, ventas = 10, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '134' DAY WHERE idArregloFloral = 75 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-076', nombre = 'Orquídea Radiante', detalles = 'Orquídea en base cerámica de 4 o 6 varas con follajes y suculenta.', costo = 2800.00, idTipoArreglo = 6, categoria = 'Orquídeas', etiqueta = 'Orquídeas', disponibilidad = 'Disponible', existencias = 8, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 75, ventas = 6, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '145' DAY WHERE idArregloFloral = 76 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-077', nombre = 'Orquídea Pureza', detalles = 'Orquídea en base cerámica de 4 o 6 varas con follajes y suculenta.', costo = 2600.00, idTipoArreglo = 6, categoria = 'Orquídeas', etiqueta = 'Orquídeas', disponibilidad = 'Disponible', existencias = 15, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 22, ventas = 2, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '156' DAY WHERE idArregloFloral = 77 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-078', nombre = 'Orquídea Cascada', detalles = 'Orquídea de 2 varas en base cerámica.', costo = 2300.00, idTipoArreglo = 6, categoria = 'Orquídeas', etiqueta = 'Orquídeas', disponibilidad = 'Disponible', existencias = 9, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 59, ventas = 15, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '47' DAY WHERE idArregloFloral = 78 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-079', nombre = 'Orquídeas Paraíso', detalles = 'Orquídea de 10 varas en bonsayera.', costo = 8900.00, idTipoArreglo = 6, categoria = 'Orquídeas', etiqueta = 'Orquídeas', disponibilidad = 'Disponible', existencias = 3, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 6, ventas = 11, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '58' DAY WHERE idArregloFloral = 79 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-080', nombre = 'Jardín de Orquídeas', detalles = 'Orquídea de 6 varas en base de madera rústica, suculentas y tronco.', costo = 3500.00, idTipoArreglo = 6, categoria = 'Orquídeas', etiqueta = 'Orquídeas', disponibilidad = 'Agotado', existencias = 0, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 43, ventas = 7, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '69' DAY WHERE idArregloFloral = 80 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-081', nombre = 'Orquídeas Gran Edén', detalles = 'Orquídea de 9 varas en base de cerámica.', costo = 4600.00, idTipoArreglo = 6, categoria = 'Orquídeas', etiqueta = 'Orquídeas', disponibilidad = 'Disponible', existencias = 4, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 80, ventas = 3, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '80' DAY WHERE idArregloFloral = 81 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-082', nombre = 'Caja Floral con 10 Varas de Orquídeas', detalles = 'Caja floral jumbo con gran variedad de flores y 10 varas de orquídeas al centro.', costo = 6300.00, idTipoArreglo = 6, categoria = 'Orquídeas', etiqueta = 'Caja, Orquídeas', disponibilidad = 'Disponible', existencias = 11, puntoReorden = 3, activo = TRUE, personalizable = TRUE, permiteQR = TRUE, vistas = 27, ventas = 16, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '91' DAY WHERE idArregloFloral = 82 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-083', nombre = 'Chocolates Ferrero', detalles = 'Presentaciones de 16 o 24 piezas.', costo = 320.00, idTipoArreglo = 7, categoria = 'Adicionales', etiqueta = 'Chocolates', disponibilidad = 'Disponible', existencias = 5, puntoReorden = 3, activo = TRUE, personalizable = FALSE, permiteQR = TRUE, vistas = 64, ventas = 12, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '5' DAY WHERE idArregloFloral = 83 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-084', nombre = 'Oso de Amor', detalles = 'Oso de peluche para complementar tu arreglo.', costo = 480.00, idTipoArreglo = 7, categoria = 'Adicionales', etiqueta = 'Peluches', disponibilidad = 'Disponible', existencias = 12, puntoReorden = 3, activo = TRUE, personalizable = FALSE, permiteQR = TRUE, vistas = 11, ventas = 8, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '6' DAY WHERE idArregloFloral = 84 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-085', nombre = 'Globo Personalizado', detalles = 'Globo personalizado.', costo = 380.00, idTipoArreglo = 7, categoria = 'Adicionales', etiqueta = 'Globos', disponibilidad = 'Disponible', existencias = 6, puntoReorden = 3, activo = TRUE, personalizable = FALSE, permiteQR = TRUE, vistas = 48, ventas = 4, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '7' DAY WHERE idArregloFloral = 85 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-086', nombre = 'Caja Dulce Amor', detalles = 'Caja botanera con variedad de dulces y botanas.', costo = 890.00, idTipoArreglo = 7, categoria = 'Adicionales', etiqueta = 'Caja, Dulces', disponibilidad = 'Disponible', existencias = 13, puntoReorden = 3, activo = TRUE, personalizable = FALSE, permiteQR = TRUE, vistas = 85, ventas = 0, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '3' DAY WHERE idArregloFloral = 86 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-087', nombre = 'Caja Personalizada Dulce Alegría', detalles = 'Caja personalizada con variedad de dulces y botanas.', costo = 980.00, idTipoArreglo = 7, categoria = 'Adicionales', etiqueta = 'Caja, Dulces', disponibilidad = 'Disponible', existencias = 7, puntoReorden = 3, activo = TRUE, personalizable = FALSE, permiteQR = TRUE, vistas = 32, ventas = 13, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '4' DAY WHERE idArregloFloral = 87 AND sku IS NULL;
UPDATE ArregloFloral SET sku = 'PG-088', nombre = 'Carrito Botanero & Bar', detalles = 'Carrito botanero & bar.', costo = 1600.00, idTipoArreglo = 7, categoria = 'Adicionales', etiqueta = '', disponibilidad = 'Disponible', existencias = 14, puntoReorden = 3, activo = TRUE, personalizable = FALSE, permiteQR = TRUE, vistas = 69, ventas = 9, fechaAlta = CURRENT_TIMESTAMP - INTERVAL '5' DAY WHERE idArregloFloral = 88 AND sku IS NULL;

-- Colecciones de cada arreglo (Primavera, Cumpleaños, Aniversario, Lujo).
INSERT IGNORE INTO ArregloFloral_Coleccion (idArregloFloral, idCategoria) VALUES
    (1, 8), (1, 9), (2, 8), (3, 8), (5, 8), (5, 10), (6, 8), (6, 10),
    (7, 8), (7, 10), (7, 11), (8, 8), (8, 10), (9, 8), (9, 10), (10, 10),
    (11, 9), (11, 10), (12, 10), (13, 10), (13, 11), (14, 8), (15, 8), (15, 10),
    (16, 10), (17, 10), (19, 8), (19, 10), (20, 8), (20, 10), (21, 8), (21, 10),
    (21, 11), (23, 8), (26, 9), (29, 8), (30, 10), (30, 11), (31, 10), (32, 11),
    (33, 8), (33, 10), (34, 8), (34, 10), (35, 8), (35, 10), (36, 8), (36, 10),
    (37, 10), (38, 8), (38, 10), (39, 8), (39, 10), (39, 11), (40, 8), (40, 10),
    (40, 11), (41, 10), (41, 11), (42, 10), (43, 10), (43, 11), (44, 10), (44, 11),
    (47, 8), (48, 8), (49, 10), (50, 10), (50, 11), (51, 10), (51, 11), (52, 10),
    (53, 9), (53, 10), (54, 9), (54, 10), (54, 11), (55, 10), (56, 10), (57, 8),
    (58, 8), (59, 8), (59, 11), (61, 9), (61, 10), (64, 10), (65, 8), (65, 10),
    (66, 10), (66, 11), (67, 9), (67, 10), (68, 10), (68, 11), (69, 8), (69, 11),
    (70, 8), (71, 8), (71, 11), (79, 11), (80, 11), (81, 11), (82, 11), (83, 9),
    (84, 9), (85, 9), (86, 9), (87, 9);
-- ============================================================
-- 7. ListaDeseo_ArregloFloral
-- ============================================================
INSERT IGNORE INTO ListaDeseo_ArregloFloral (idLista, idArregloFloral) VALUES
                                                                    (1, 1),
                                                                    (1, 3),
                                                                    (1, 6),
                                                                    (2, 2),
                                                                    (2, 4),
                                                                    (3, 5),
                                                                    (3, 8),
                                                                    (4, 1),
                                                                    (4, 7);

-- ============================================================
-- 8. CarritoDeCompra
-- ============================================================
INSERT IGNORE INTO CarritoDeCompra (idCarritoDeCompra, fecha, total, idUsuario) VALUES
                                                                             (1, '2024-06-10', 1500.00, 1),
                                                                             (2, '2024-06-12', 650.00, 2),
                                                                             (3, '2024-06-15', 1200.00, 3),
                                                                             (4, '2024-06-18', 950.00, 7),
                                                                             (5, '2024-06-20', 0.00, 1);

-- ============================================================
-- 9. ContenidoCarrito
-- ============================================================
INSERT IGNORE INTO ContenidoCarrito (idContenido, cantidad, precioUnitario, idCarritoDeCompra, idArregloFloral) VALUES
                                                                                                             (1, 1, 850.00, 1, 1),
                                                                                                             (2, 1, 650.00, 1, 2),
                                                                                                             (3, 1, 650.00, 2, 2),
                                                                                                             (4, 1, 1200.00, 3, 3),
                                                                                                             (5, 1, 950.00, 4, 4),
                                                                                                             (6, 2, 450.00, 4, 7),
                                                                                                             (7, 1, 780.00, 1, 5);

-- ============================================================
-- 10. Cupon
-- ============================================================
INSERT IGNORE INTO Cupon (idCupon, nombre, codigo, fechaVencimiento, limiteDeUsos, estado, porcentajeDescuento) VALUES
                                                                                                             (1, 'Descuento San Valentín', 'AMOR2024', '2024-02-14', 100, 'Expirado', 15.00),
                                                                                                             (2, 'Bienvenida', 'BIENVENIDO10', '2024-12-31', 500, 'Activo', 10.00),
                                                                                                             (3, 'Día de las Madres', 'MADRES2024', '2024-05-10', 200, 'Expirado', 20.00),
                                                                                                             (4, 'Verano Floral', 'VERANO15', '2024-08-31', 300, 'Activo', 15.00),
                                                                                                             (5, 'Navidad', 'NAVIDAD20', '2024-12-24', 150, 'Activo', 20.00),
                                                                                                             (6, 'Primera Compra', 'PRIMERA5', '2025-01-31', 1000, 'Activo', 5.00);

-- ============================================================
-- 11. Pedido
-- ============================================================
INSERT IGNORE INTO Pedido (idPedido, fechaCreacion, estadoEnvio, direccionEntrega, fechaEntrega, instruccionesEspeciales, idCarritoDeCompra, idMetodosDePago, idCupon) VALUES
                                                                                                                                                                    (1, '2024-06-10', 'Entregado', 'Av. Reforma 123, CDMX', '2024-06-11', 'Dejar en recepción', 1, 1, 2),
                                                                                                                                                                    (2, '2024-06-12', 'En camino', 'Calle 5 de Mayo 45, Puebla', '2024-06-13', 'Llamar al llegar', 2, 2, NULL),
                                                                                                                                                                    (3, '2024-06-15', 'Preparando', 'Blvd. Kukulcán 78, Cancún', '2024-06-17', 'Entregar antes de las 12:00', 3, 3, 4),
                                                                                                                                                                    (4, '2024-06-18', 'Pendiente', 'Calle 20 de Noviembre 89, Querétaro', '2024-06-20', 'Tocar el timbre 3 veces', 4, 5, 6);

-- ============================================================
-- 12. Ticket
-- ============================================================
INSERT IGNORE INTO Ticket (idTicket, folioVenta, fecha, hora, total, idPedido) VALUES
                                                                            (1, 'TK-2024-0001', '2024-06-10', '10:30:00', 1435.00, 1),
                                                                            (2, 'TK-2024-0002', '2024-06-12', '14:15:00', 650.00, 2),
                                                                            (3, 'TK-2024-0003', '2024-06-15', '09:45:00', 1020.00, 3),
                                                                            (4, 'TK-2024-0004', '2024-06-18', '16:20:00', 1805.00, 4);

-- ============================================================
-- 13. Factura
-- ============================================================
INSERT IGNORE INTO Factura (idFactura, RFC, razonSocial, direccionFiscal, cfdi, fechaEmision, total, idPedido) VALUES
                                                                                                            (1, 'LOPA850101ABC', 'Ana María López', 'Av. Reforma 123, CDMX', 'CFDI-2024-0001', '2024-06-10', 1435.00, 1),
                                                                                                            (2, 'RAMC900202DEF', 'Carlos Ramírez', 'Calle 5 de Mayo 45, Puebla', 'CFDI-2024-0002', '2024-06-12', 650.00, 2),
                                                                                                            (3, 'FEGL920303GHI', 'Laura Fernández', 'Blvd. Kukulcán 78, Cancún', 'CFDI-2024-0003', '2024-06-15', 1020.00, 3);


-- ============================================================
-- 14. Imagen (foto principal de cada arreglo)
-- ============================================================
INSERT IGNORE INTO Imagen (idImagen, nombre, direccionImg, idArregloFloral) VALUES
    (1, 'Ramo Dulce Encanto', '/img/productos/PG-001.webp', 1),
    (2, 'Ramo Tierna Ilusión', '/img/productos/PG-002.webp', 2),
    (3, 'Ramo Noche Estrellada', '/img/productos/PG-003.webp', 3),
    (4, 'La Incondicional', '/img/productos/PG-004.webp', 4),
    (5, 'Ramo Rosa Palo', '/img/productos/PG-005.webp', 5),
    (6, 'Amor Carmesí', '/img/productos/PG-006.webp', 6),
    (7, 'Ramo Aura Floral', '/img/productos/PG-007.webp', 7),
    (8, 'Euforia Rosa', '/img/productos/PG-008.webp', 8),
    (9, 'Cien Razones', '/img/productos/PG-009.webp', 9),
    (10, 'Euforia Carmesí', '/img/productos/PG-010.webp', 10),
    (11, 'Caja Floral Gran Amor', '/img/productos/PG-011.webp', 11),
    (12, 'Reflejo de Amor', '/img/productos/PG-012.webp', 12),
    (13, 'Reyna de Rosas', '/img/productos/PG-013.webp', 13),
    (14, 'Jardín de Tulipanes', '/img/productos/PG-014.webp', 14),
    (15, 'Sueño en Rosa', '/img/productos/PG-015.webp', 15),
    (16, 'Caja Floral Mi Amor', '/img/productos/PG-016.webp', 16),
    (17, 'Vaso Floral de Starbucks', '/img/productos/PG-017.webp', 17),
    (18, 'Edén Floral', '/img/productos/PG-018.webp', 18),
    (19, 'Bolsa Floral Amor a la Mexicana', '/img/productos/PG-019.webp', 19),
    (20, 'Bolsa Floral Rojo Amor', '/img/productos/PG-020.webp', 20),
    (21, 'Florero Amor y Pasión', '/img/productos/PG-021.webp', 21),
    (22, 'Florero Dulzura', '/img/productos/PG-022.webp', 22),
    (23, 'Florero Algodón de Azúcar', '/img/productos/PG-023.webp', 23),
    (24, 'Florero Gran Detalle', '/img/productos/PG-024.webp', 24),
    (25, 'Florero Carmesí', '/img/productos/PG-025.webp', 25),
    (26, 'Combo Love Love', '/img/productos/PG-026.webp', 26),
    (27, 'Florero Amor', '/img/productos/PG-027.webp', 27),
    (28, 'Pecera Eterno Amanecer', '/img/productos/PG-028.webp', 28),
    (29, 'Florero Tulipanes', '/img/productos/PG-029.webp', 29),
    (30, 'Arreglo Floral Ternura', '/img/productos/PG-030.webp', 30),
    (31, 'Arreglo Floral Elegancia', '/img/productos/PG-031.webp', 31),
    (32, 'Arreglo Floral Alegría', '/img/productos/PG-032.webp', 32),
    (33, 'Florero Ilusión', '/img/productos/PG-033.webp', 33),
    (34, 'Florero Ilusión Secreta', '/img/productos/PG-034.webp', 34),
    (35, 'Pecera Reyna', '/img/productos/PG-035.webp', 35),
    (36, 'Pecera Color a Tu Vida', '/img/productos/PG-036.webp', 36),
    (37, 'Florero Amor Amor', '/img/productos/PG-037.webp', 37),
    (38, 'Pecera Toque de Mar', '/img/productos/PG-038.webp', 38),
    (39, 'Florero Suspiro', '/img/productos/PG-039.webp', 39),
    (40, 'Florero Promesa de Amor', '/img/productos/PG-040.webp', 40),
    (41, 'Jardín de Venus', '/img/productos/PG-041.webp', 41),
    (42, 'Florero Amor Infinito', '/img/productos/PG-042.webp', 42),
    (43, 'Florero Máxima Elegancia', '/img/productos/PG-043.webp', 43),
    (44, 'Elegancia Pura', '/img/productos/PG-044.webp', 44),
    (45, 'Ramos de Rosas', '/img/productos/PG-045.webp', 45),
    (46, 'Ramo en Escalera', '/img/productos/PG-046.webp', 46),
    (47, 'Ramo Girasoles', '/img/productos/PG-047.webp', 47),
    (48, 'Ramo Calidez', '/img/productos/PG-048.webp', 48),
    (49, 'Ramo Elegancia 100 Rosas', '/img/productos/PG-049.webp', 49),
    (50, 'Ramo Amor en Letras', '/img/productos/PG-050.webp', 50),
    (51, 'Ramo Momentos', '/img/productos/PG-051.webp', 51),
    (52, 'Ramo Personalizado con Corona', '/img/productos/PG-052.webp', 52),
    (53, 'Ramo Dulce Amor', '/img/productos/PG-053.webp', 53),
    (54, 'Ramo Dulce Amor Jumbo', '/img/productos/PG-054.webp', 54),
    (55, 'Ramo Inspiración', '/img/productos/PG-055.webp', 55),
    (56, 'Ramos Clásicos', '/img/productos/PG-056.webp', 56),
    (57, 'Ramo Color de Tus Ojos', '/img/productos/PG-057.webp', 57),
    (58, 'Ramo Luz de Día', '/img/productos/PG-058.webp', 58),
    (59, 'Ramo Peonías', '/img/productos/PG-059.webp', 59),
    (60, 'Círculo Romance', '/img/productos/PG-060.webp', 60),
    (61, 'Caja Floral Corazón de Oro', '/img/productos/PG-061.webp', 61),
    (62, 'Caja Floral Arcoíris', '/img/productos/PG-062.webp', 62),
    (63, 'Caja Floral Sublime Amor', '/img/productos/PG-063.webp', 63),
    (64, 'Caja Floral Amor Mío', '/img/productos/PG-064.webp', 64),
    (65, 'Caja Floral Alas de Ángel', '/img/productos/PG-065.webp', 65),
    (66, 'Caja Floral Magia', '/img/productos/PG-066.webp', 66),
    (67, 'Caja Floral Oso Cariñoso', '/img/productos/PG-067.webp', 67),
    (68, 'Caja Floral Más Que Amor', '/img/productos/PG-068.webp', 68),
    (69, 'Caja Floral Sol en Tus Manos', '/img/productos/PG-069.webp', 69),
    (70, 'Caja de 6 Peonías Preservadas', '/img/productos/PG-070.webp', 70),
    (71, 'Caja de 12 Peonías Preservadas', '/img/productos/PG-071.webp', 71),
    (72, 'Orquídea en Esfera', '/img/productos/PG-072.webp', 72),
    (73, 'Orquídea en Espejo', '/img/productos/PG-073.webp', 73),
    (74, 'Mini Orquídea Dalí', '/img/productos/PG-074.webp', 74),
    (75, 'Mini Orquídeas Arcoíris', '/img/productos/PG-075.webp', 75),
    (76, 'Orquídea Radiante', '/img/productos/PG-076.webp', 76),
    (77, 'Orquídea Pureza', '/img/productos/PG-077.webp', 77),
    (78, 'Orquídea Cascada', '/img/productos/PG-078.webp', 78),
    (79, 'Orquídeas Paraíso', '/img/productos/PG-079.webp', 79),
    (80, 'Jardín de Orquídeas', '/img/productos/PG-080.webp', 80),
    (81, 'Orquídeas Gran Edén', '/img/productos/PG-081.webp', 81),
    (82, 'Caja Floral con 10 Varas de Orquídeas', '/img/productos/PG-082.webp', 82),
    (83, 'Chocolates Ferrero', '/img/productos/PG-083.webp', 83),
    (84, 'Oso de Amor', '/img/productos/PG-084.webp', 84),
    (85, 'Globo Personalizado', '/img/productos/PG-085.webp', 85),
    (86, 'Caja Dulce Amor', '/img/productos/PG-086.webp', 86),
    (87, 'Caja Personalizada Dulce Alegría', '/img/productos/PG-087.webp', 87),
    (88, 'Carrito Botanero & Bar', '/img/productos/PG-088.webp', 88);

-- Bases de datos creadas antes: se cambian las fotos de prueba por las del catálogo (una sola vez).
UPDATE Imagen SET nombre = 'Ramo Dulce Encanto', direccionImg = '/img/productos/PG-001.webp', idArregloFloral = 1 WHERE idImagen = 1 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Ramo Tierna Ilusión', direccionImg = '/img/productos/PG-002.webp', idArregloFloral = 2 WHERE idImagen = 2 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Ramo Noche Estrellada', direccionImg = '/img/productos/PG-003.webp', idArregloFloral = 3 WHERE idImagen = 3 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'La Incondicional', direccionImg = '/img/productos/PG-004.webp', idArregloFloral = 4 WHERE idImagen = 4 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Ramo Rosa Palo', direccionImg = '/img/productos/PG-005.webp', idArregloFloral = 5 WHERE idImagen = 5 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Amor Carmesí', direccionImg = '/img/productos/PG-006.webp', idArregloFloral = 6 WHERE idImagen = 6 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Ramo Aura Floral', direccionImg = '/img/productos/PG-007.webp', idArregloFloral = 7 WHERE idImagen = 7 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Euforia Rosa', direccionImg = '/img/productos/PG-008.webp', idArregloFloral = 8 WHERE idImagen = 8 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Cien Razones', direccionImg = '/img/productos/PG-009.webp', idArregloFloral = 9 WHERE idImagen = 9 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Euforia Carmesí', direccionImg = '/img/productos/PG-010.webp', idArregloFloral = 10 WHERE idImagen = 10 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Caja Floral Gran Amor', direccionImg = '/img/productos/PG-011.webp', idArregloFloral = 11 WHERE idImagen = 11 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Reflejo de Amor', direccionImg = '/img/productos/PG-012.webp', idArregloFloral = 12 WHERE idImagen = 12 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Reyna de Rosas', direccionImg = '/img/productos/PG-013.webp', idArregloFloral = 13 WHERE idImagen = 13 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Jardín de Tulipanes', direccionImg = '/img/productos/PG-014.webp', idArregloFloral = 14 WHERE idImagen = 14 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Sueño en Rosa', direccionImg = '/img/productos/PG-015.webp', idArregloFloral = 15 WHERE idImagen = 15 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Caja Floral Mi Amor', direccionImg = '/img/productos/PG-016.webp', idArregloFloral = 16 WHERE idImagen = 16 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Vaso Floral de Starbucks', direccionImg = '/img/productos/PG-017.webp', idArregloFloral = 17 WHERE idImagen = 17 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Edén Floral', direccionImg = '/img/productos/PG-018.webp', idArregloFloral = 18 WHERE idImagen = 18 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Bolsa Floral Amor a la Mexicana', direccionImg = '/img/productos/PG-019.webp', idArregloFloral = 19 WHERE idImagen = 19 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Bolsa Floral Rojo Amor', direccionImg = '/img/productos/PG-020.webp', idArregloFloral = 20 WHERE idImagen = 20 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Florero Amor y Pasión', direccionImg = '/img/productos/PG-021.webp', idArregloFloral = 21 WHERE idImagen = 21 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Florero Dulzura', direccionImg = '/img/productos/PG-022.webp', idArregloFloral = 22 WHERE idImagen = 22 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Florero Algodón de Azúcar', direccionImg = '/img/productos/PG-023.webp', idArregloFloral = 23 WHERE idImagen = 23 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Florero Gran Detalle', direccionImg = '/img/productos/PG-024.webp', idArregloFloral = 24 WHERE idImagen = 24 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Florero Carmesí', direccionImg = '/img/productos/PG-025.webp', idArregloFloral = 25 WHERE idImagen = 25 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Combo Love Love', direccionImg = '/img/productos/PG-026.webp', idArregloFloral = 26 WHERE idImagen = 26 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Florero Amor', direccionImg = '/img/productos/PG-027.webp', idArregloFloral = 27 WHERE idImagen = 27 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Pecera Eterno Amanecer', direccionImg = '/img/productos/PG-028.webp', idArregloFloral = 28 WHERE idImagen = 28 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Florero Tulipanes', direccionImg = '/img/productos/PG-029.webp', idArregloFloral = 29 WHERE idImagen = 29 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Arreglo Floral Ternura', direccionImg = '/img/productos/PG-030.webp', idArregloFloral = 30 WHERE idImagen = 30 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Arreglo Floral Elegancia', direccionImg = '/img/productos/PG-031.webp', idArregloFloral = 31 WHERE idImagen = 31 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Arreglo Floral Alegría', direccionImg = '/img/productos/PG-032.webp', idArregloFloral = 32 WHERE idImagen = 32 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Florero Ilusión', direccionImg = '/img/productos/PG-033.webp', idArregloFloral = 33 WHERE idImagen = 33 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Florero Ilusión Secreta', direccionImg = '/img/productos/PG-034.webp', idArregloFloral = 34 WHERE idImagen = 34 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Pecera Reyna', direccionImg = '/img/productos/PG-035.webp', idArregloFloral = 35 WHERE idImagen = 35 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Pecera Color a Tu Vida', direccionImg = '/img/productos/PG-036.webp', idArregloFloral = 36 WHERE idImagen = 36 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Florero Amor Amor', direccionImg = '/img/productos/PG-037.webp', idArregloFloral = 37 WHERE idImagen = 37 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Pecera Toque de Mar', direccionImg = '/img/productos/PG-038.webp', idArregloFloral = 38 WHERE idImagen = 38 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Florero Suspiro', direccionImg = '/img/productos/PG-039.webp', idArregloFloral = 39 WHERE idImagen = 39 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Florero Promesa de Amor', direccionImg = '/img/productos/PG-040.webp', idArregloFloral = 40 WHERE idImagen = 40 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Jardín de Venus', direccionImg = '/img/productos/PG-041.webp', idArregloFloral = 41 WHERE idImagen = 41 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Florero Amor Infinito', direccionImg = '/img/productos/PG-042.webp', idArregloFloral = 42 WHERE idImagen = 42 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Florero Máxima Elegancia', direccionImg = '/img/productos/PG-043.webp', idArregloFloral = 43 WHERE idImagen = 43 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Elegancia Pura', direccionImg = '/img/productos/PG-044.webp', idArregloFloral = 44 WHERE idImagen = 44 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Ramos de Rosas', direccionImg = '/img/productos/PG-045.webp', idArregloFloral = 45 WHERE idImagen = 45 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Ramo en Escalera', direccionImg = '/img/productos/PG-046.webp', idArregloFloral = 46 WHERE idImagen = 46 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Ramo Girasoles', direccionImg = '/img/productos/PG-047.webp', idArregloFloral = 47 WHERE idImagen = 47 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Ramo Calidez', direccionImg = '/img/productos/PG-048.webp', idArregloFloral = 48 WHERE idImagen = 48 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Ramo Elegancia 100 Rosas', direccionImg = '/img/productos/PG-049.webp', idArregloFloral = 49 WHERE idImagen = 49 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Ramo Amor en Letras', direccionImg = '/img/productos/PG-050.webp', idArregloFloral = 50 WHERE idImagen = 50 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Ramo Momentos', direccionImg = '/img/productos/PG-051.webp', idArregloFloral = 51 WHERE idImagen = 51 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Ramo Personalizado con Corona', direccionImg = '/img/productos/PG-052.webp', idArregloFloral = 52 WHERE idImagen = 52 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Ramo Dulce Amor', direccionImg = '/img/productos/PG-053.webp', idArregloFloral = 53 WHERE idImagen = 53 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Ramo Dulce Amor Jumbo', direccionImg = '/img/productos/PG-054.webp', idArregloFloral = 54 WHERE idImagen = 54 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Ramo Inspiración', direccionImg = '/img/productos/PG-055.webp', idArregloFloral = 55 WHERE idImagen = 55 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Ramos Clásicos', direccionImg = '/img/productos/PG-056.webp', idArregloFloral = 56 WHERE idImagen = 56 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Ramo Color de Tus Ojos', direccionImg = '/img/productos/PG-057.webp', idArregloFloral = 57 WHERE idImagen = 57 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Ramo Luz de Día', direccionImg = '/img/productos/PG-058.webp', idArregloFloral = 58 WHERE idImagen = 58 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Ramo Peonías', direccionImg = '/img/productos/PG-059.webp', idArregloFloral = 59 WHERE idImagen = 59 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Círculo Romance', direccionImg = '/img/productos/PG-060.webp', idArregloFloral = 60 WHERE idImagen = 60 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Caja Floral Corazón de Oro', direccionImg = '/img/productos/PG-061.webp', idArregloFloral = 61 WHERE idImagen = 61 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Caja Floral Arcoíris', direccionImg = '/img/productos/PG-062.webp', idArregloFloral = 62 WHERE idImagen = 62 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Caja Floral Sublime Amor', direccionImg = '/img/productos/PG-063.webp', idArregloFloral = 63 WHERE idImagen = 63 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Caja Floral Amor Mío', direccionImg = '/img/productos/PG-064.webp', idArregloFloral = 64 WHERE idImagen = 64 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Caja Floral Alas de Ángel', direccionImg = '/img/productos/PG-065.webp', idArregloFloral = 65 WHERE idImagen = 65 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Caja Floral Magia', direccionImg = '/img/productos/PG-066.webp', idArregloFloral = 66 WHERE idImagen = 66 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Caja Floral Oso Cariñoso', direccionImg = '/img/productos/PG-067.webp', idArregloFloral = 67 WHERE idImagen = 67 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Caja Floral Más Que Amor', direccionImg = '/img/productos/PG-068.webp', idArregloFloral = 68 WHERE idImagen = 68 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Caja Floral Sol en Tus Manos', direccionImg = '/img/productos/PG-069.webp', idArregloFloral = 69 WHERE idImagen = 69 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Caja de 6 Peonías Preservadas', direccionImg = '/img/productos/PG-070.webp', idArregloFloral = 70 WHERE idImagen = 70 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Caja de 12 Peonías Preservadas', direccionImg = '/img/productos/PG-071.webp', idArregloFloral = 71 WHERE idImagen = 71 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Orquídea en Esfera', direccionImg = '/img/productos/PG-072.webp', idArregloFloral = 72 WHERE idImagen = 72 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Orquídea en Espejo', direccionImg = '/img/productos/PG-073.webp', idArregloFloral = 73 WHERE idImagen = 73 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Mini Orquídea Dalí', direccionImg = '/img/productos/PG-074.webp', idArregloFloral = 74 WHERE idImagen = 74 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Mini Orquídeas Arcoíris', direccionImg = '/img/productos/PG-075.webp', idArregloFloral = 75 WHERE idImagen = 75 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Orquídea Radiante', direccionImg = '/img/productos/PG-076.webp', idArregloFloral = 76 WHERE idImagen = 76 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Orquídea Pureza', direccionImg = '/img/productos/PG-077.webp', idArregloFloral = 77 WHERE idImagen = 77 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Orquídea Cascada', direccionImg = '/img/productos/PG-078.webp', idArregloFloral = 78 WHERE idImagen = 78 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Orquídeas Paraíso', direccionImg = '/img/productos/PG-079.webp', idArregloFloral = 79 WHERE idImagen = 79 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Jardín de Orquídeas', direccionImg = '/img/productos/PG-080.webp', idArregloFloral = 80 WHERE idImagen = 80 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Orquídeas Gran Edén', direccionImg = '/img/productos/PG-081.webp', idArregloFloral = 81 WHERE idImagen = 81 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Caja Floral con 10 Varas de Orquídeas', direccionImg = '/img/productos/PG-082.webp', idArregloFloral = 82 WHERE idImagen = 82 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Chocolates Ferrero', direccionImg = '/img/productos/PG-083.webp', idArregloFloral = 83 WHERE idImagen = 83 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Oso de Amor', direccionImg = '/img/productos/PG-084.webp', idArregloFloral = 84 WHERE idImagen = 84 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Globo Personalizado', direccionImg = '/img/productos/PG-085.webp', idArregloFloral = 85 WHERE idImagen = 85 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Caja Dulce Amor', direccionImg = '/img/productos/PG-086.webp', idArregloFloral = 86 WHERE idImagen = 86 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Caja Personalizada Dulce Alegría', direccionImg = '/img/productos/PG-087.webp', idArregloFloral = 87 WHERE idImagen = 87 AND direccionImg LIKE '/imagenes/%';
UPDATE Imagen SET nombre = 'Carrito Botanero & Bar', direccionImg = '/img/productos/PG-088.webp', idArregloFloral = 88 WHERE idImagen = 88 AND direccionImg LIKE '/imagenes/%';
-- ============================================================
-- 15. Modificacion
-- ============================================================
INSERT IGNORE INTO Modificacion (idModificacion, nombre, descripcion, idCosto, tipoModificacion, idArregloFloral) VALUES
                                                                                                               (1, 'Tarjeta dedicatoria', 'Mensaje impreso en papel algodón', 50, 'Extra', 1),
                                                                                                               (2, 'Extra rosas', 'Agregar 6 rosas rojas adicionales', 350, 'Tamaño', 1),
                                                                                                               (3, 'Listón dorado', 'Cambio de listón estándar a satinado dorado', 30, 'Detalle', 2);

-- ============================================================
-- 16. Cuentas iniciales de la tienda (contraseñas cifradas con BCrypt; se entregan aparte, no van en el repositorio).
--     Cámbialas desde Mi cuenta después de entrar por primera vez.
-- ============================================================
INSERT IGNORE INTO Usuario (idUsuario, nombre, direccion, password, correoElectronico, telefono, fechaRegistro, idTipoUsuario, fechaNacimiento, activo) VALUES
    (8, 'Elena Aldana', 'Av. Juárez 200, CDMX', '$2a$10$iQUxUmtfyEhBHvTnOTPNf.cHiQ/5Zw7Ep/uHBietPqYn1F1zGBFKq', 'admin@porelgusto.mx', '55 1050 1488', '2025-01-10', 2, '1990-03-21', TRUE),
    (9, 'Marco Jiménez', 'Calle Hidalgo 12, CDMX', '$2a$10$EHZe4bKpYioSquk473SW2uUIbDDM/yaPYEitTzze8LpYsrSzREXjS', 'empleado@porelgusto.mx', '55 9876 5432', '2025-02-01', 3, '1995-07-02', TRUE),
    (10, 'Ana López', 'Av. Insurgentes Sur 1234, CDMX', '$2a$10$b3D469yT76XfvjuvJNNdyOAyajTNKmru.Z1SQhN4hCIU.oqW3ObUK', 'cliente@porelgusto.mx', '55 1234 5678', '2025-05-14', 1, '1998-05-14', TRUE),
    (11, 'Luis Torres', 'Av. Insurgentes 500, CDMX', '$2a$10$5SZlMJz7PN41.s8ABj2Ho.XdLchyRHXWGa6mGOorKm9IW/nfy2tvK', 'repartidor@porelgusto.mx', '55 7777 8899', '2025-06-01', 4, '1993-11-30', TRUE);

-- Bases creadas antes: se reemplazan las contraseñas iniciales anteriores (solo si no se han cambiado).
UPDATE Usuario SET password = '$2a$10$iQUxUmtfyEhBHvTnOTPNf.cHiQ/5Zw7Ep/uHBietPqYn1F1zGBFKq' WHERE idUsuario = 8 AND password = '$2a$10$ElEByyVVVXkvwyHGvA2K8u7ZB5t04UUFyhLEEmLbOeOOVVKbh8Yc6';
UPDATE Usuario SET password = '$2a$10$EHZe4bKpYioSquk473SW2uUIbDDM/yaPYEitTzze8LpYsrSzREXjS' WHERE idUsuario = 9 AND password = '$2a$10$GcAqhM595KuPCm877uV1ZuAMoOCjThO93dKrEz8C9D9aS8fbUL2Ky';
UPDATE Usuario SET password = '$2a$10$b3D469yT76XfvjuvJNNdyOAyajTNKmru.Z1SQhN4hCIU.oqW3ObUK' WHERE idUsuario = 10 AND password = '$2a$10$nRix3M6iurqiRGSWkpBrEOSceYfNjIQSEnxx6ICeX2ZqhAildmVb.';
UPDATE Usuario SET password = '$2a$10$5SZlMJz7PN41.s8ABj2Ho.XdLchyRHXWGa6mGOorKm9IW/nfy2tvK' WHERE idUsuario = 11 AND password = '$2a$10$oVbkihS9M0nrrt5gDK1UA.O9u/rp33dgPD1WO66fqxYPAZm.NLY7a';

-- Dirección guardada de la clienta de prueba (tabla Contacto).
INSERT IGNORE INTO Contacto (idContacto, nombre, tipoContacto, idUsuario, telefono, calle, colonia, ciudad, codigoPostal, referencias, activo) VALUES
    (8, 'Laura López', 'Casa de mamá', 10, '55 2222 3333', 'Av. Insurgentes Sur 1234, depto 5', 'Del Valle', 'Benito Juárez, CDMX', '03100', 'Portón negro', TRUE);

-- Lista de deseos de la clienta de prueba (un producto agotado: se le avisa cuando regrese).
INSERT IGNORE INTO ListaDeDeseos (idLista, fechaCreacion, idUsuario) VALUES (5, CURRENT_DATE, 10);
INSERT IGNORE INTO ListaDeseo_ArregloFloral (idLista, idArregloFloral, avisado, fechaAgregado) VALUES
    (5, 7, FALSE, CURRENT_TIMESTAMP - INTERVAL '2' DAY),
    (5, 20, TRUE, CURRENT_TIMESTAMP - INTERVAL '1' DAY);

-- ============================================================
-- 17. Opciones de personalización para todos los arreglos (Modificacion sin arreglo)
--     idCosto = costo adicional en pesos
-- ============================================================
INSERT IGNORE INTO Modificacion (idModificacion, nombre, descripcion, idCosto, tipoModificacion, idArregloFloral, disponible, orden) VALUES
    (4, 'Tarjeta de dedicatoria impresa', 'Tarjeta con tu mensaje escrito a mano', 35, 'Extra', NULL, TRUE, 0),
    (5, 'Moño de satín', 'Moño en el color de la envoltura', 45, 'Detalle', NULL, TRUE, 1),
    (6, 'Envoltura de lujo', 'Papel coreano doble con listón', 90, 'Detalle', NULL, TRUE, 2),
    (7, 'Globo metálico «Te amo»', 'Globo de 45 cm inflado con helio', 120, 'Extra', NULL, TRUE, 3),
    (8, 'Caja de chocolates (8 pzas)', 'Chocolates finos surtidos', 180, 'Extra', NULL, TRUE, 4),
    (9, 'Peluche pequeño', 'Osito de peluche de 20 cm', 250, 'Extra', NULL, TRUE, 5),
    (10, 'Vela aromática', 'Vela de soya con aroma a rosas', 160, 'Extra', NULL, FALSE, 6);

-- ============================================================
-- 18. Cupones vigentes y promoción de temporada
-- ============================================================
INSERT IGNORE INTO Cupon (idCupon, nombre, codigo, fechaVencimiento, limiteDeUsos, estado, porcentajeDescuento, montoDescuento, compraMinima, fechaInicio, usos, descripcion) VALUES
    (7, 'Bienvenida', 'BIENVENIDA10', '2027-12-31', 1000, 'Activo', 10.00, NULL, 500.00, '2026-01-01', 0, '10% en tu primera compra.'),
    (8, 'Envío por nuestra cuenta', 'REGALO150', '2027-06-30', 500, 'Activo', NULL, 150.00, 1000.00, '2026-01-01', 0, '$150 de descuento en compras desde $1,000.'),
    (9, 'Verano 2026', 'VERANO25', '2026-08-31', 300, 'Activo', 25.00, NULL, NULL, '2026-06-01', 0, 'Cupón de ejemplo ya vencido.');

INSERT IGNORE INTO Promocion (idPromocion, nombre, descripcion, tipo, tipoDescuento, valor, condiciones, fechaInicio, fechaFin, activa, idCategoria) VALUES
    (1, 'Colección Primavera', '20% de descuento por tiempo limitado en toda la colección Primavera.', 'OFERTA', 'PORCENTAJE', 20.00, NULL, '2026-01-01 00:00:00', '2027-12-31 23:59:00', TRUE, 8);

-- ============================================================
-- 19. Pedidos anteriores: folio, cliente, importes e historial para la tienda en línea (una sola vez)
-- ============================================================
UPDATE Pedido SET folio = CONCAT('PG2406', LPAD(idPedido, 4, '0')) WHERE folio IS NULL AND idPedido <= 4;
UPDATE Pedido SET idUsuario = (SELECT c.idUsuario FROM CarritoDeCompra c WHERE c.idCarritoDeCompra = Pedido.idCarritoDeCompra) WHERE idUsuario IS NULL AND idPedido <= 4;
UPDATE Pedido SET nombreCliente = (SELECT u.nombre FROM Usuario u WHERE u.idUsuario = Pedido.idUsuario),
                  correoCliente = (SELECT u.correoElectronico FROM Usuario u WHERE u.idUsuario = Pedido.idUsuario),
                  telefonoCliente = (SELECT u.telefono FROM Usuario u WHERE u.idUsuario = Pedido.idUsuario),
                  destinatario = (SELECT u.nombre FROM Usuario u WHERE u.idUsuario = Pedido.idUsuario)
              WHERE nombreCliente IS NULL AND idPedido <= 4;
UPDATE Pedido SET total = (SELECT t.total FROM Ticket t WHERE t.idPedido = Pedido.idPedido) WHERE total IS NULL AND idPedido <= 4;
UPDATE Pedido SET subtotal = total, descuento = 0, costoEnvio = 0, horarioEntrega = '12:00 - 15:00', estadoPago = 'APROBADO',
                  proveedorPago = 'Registro anterior', formaPago = 'TARJETA'
              WHERE estadoPago IS NULL AND idPedido <= 4;
INSERT IGNORE INTO HistorialPedido (idHistorial, idPedido, estado, nota, autor, fechaHora) VALUES
    (1, 1, 'Entregado', 'Registro anterior a la tienda en línea', 'Sistema', '2024-06-11 13:00:00'),
    (2, 2, 'En camino', 'Registro anterior a la tienda en línea', 'Sistema', '2024-06-13 10:00:00'),
    (3, 3, 'Preparando', 'Registro anterior a la tienda en línea', 'Sistema', '2024-06-15 09:45:00'),
    (4, 4, 'Pendiente', 'Registro anterior a la tienda en línea', 'Sistema', '2024-06-18 16:20:00');

-- ============================================================
-- 20. Dos pedidos de la clienta de prueba para ver el seguimiento (uno entregado y otro en camino)
-- ============================================================
INSERT IGNORE INTO CarritoDeCompra (idCarritoDeCompra, fecha, total, idUsuario) VALUES
    (6, CURRENT_DATE - INTERVAL '6' DAY, 1200.00, 10),
    (7, CURRENT_DATE - INTERVAL '1' DAY, 2835.00, 10);
INSERT IGNORE INTO ContenidoCarrito (idContenido, cantidad, precioUnitario, idCarritoDeCompra, idArregloFloral, personalizacion, nota) VALUES
    (8, 1, 1200.00, 6, 2, NULL, NULL),
    (9, 1, 2835.00, 7, 14, 'Tarjeta de dedicatoria impresa (+$35)', 'Colores pastel, por favor');
INSERT IGNORE INTO MetodoPago (idMetodosDePago, tipoPago, descripcion, idUsuario) VALUES
    (7, 'Tarjeta de crédito o débito', 'Visa terminación 4242', 10),
    (8, 'Tarjeta de crédito o débito', 'Visa terminación 4242', 10);
INSERT IGNORE INTO Pedido (idPedido, fechaCreacion, estadoEnvio, direccionEntrega, fechaEntrega, instruccionesEspeciales, idCarritoDeCompra,
                           idMetodosDePago, idCupon, folio, fechaHora, idUsuario, nombreCliente, correoCliente, telefonoCliente, destinatario,
                           telefonoDestinatario, horarioEntrega, subtotal, descuento, costoEnvio, iva, total, formaPago, estadoPago, proveedorPago,
                           transaccion, fechaPago, idEmpleado, numeroGuia, repartidor, telefonoRepartidor, horaEstimada, recibio, fechaEntregado,
                           entregaVerificadaQR, dedicatoria, tokenQr) VALUES
    (5, CURRENT_DATE - INTERVAL '6' DAY, 'Entregado', 'Av. Insurgentes Sur 1234, depto 5, Col. Del Valle, Benito Juárez, CDMX, C.P. 03100', CURRENT_DATE - INTERVAL '5' DAY, 'Portón negro', 6,
        7, NULL, 'PG2611190427', CURRENT_TIMESTAMP - INTERVAL '6' DAY, 10, 'Ana López', 'cliente@porelgusto.mx', '55 1234 5678', 'Laura López',
        '55 2222 3333', '12:00 - 15:00', 1200.00, 0, 150.00, 186.21, 1350.00, 'TARJETA', 'APROBADO', 'Simulado',
        'SIM-DEMO-0001', CURRENT_TIMESTAMP - INTERVAL '6' DAY, 9, 'TRK482915', 'Marco Jiménez', '55 9876 5432', CURRENT_TIMESTAMP - INTERVAL '5' DAY, 'Laura López', CURRENT_TIMESTAMP - INTERVAL '5' DAY,
        FALSE, '¡Feliz cumpleaños, mamá! Gracias por todo. Te quiero mucho. — Ana', 'a3f1c9e2b7d84f05a6c1e9d2b3f4a5c6'),
    (6, CURRENT_DATE - INTERVAL '1' DAY, 'En camino', 'Av. Insurgentes Sur 1234, depto 5, Col. Del Valle, Benito Juárez, CDMX, C.P. 03100', CURRENT_DATE + INTERVAL '1' DAY, 'Portón negro', 7,
        8, NULL, 'PG2611240913', CURRENT_TIMESTAMP - INTERVAL '1' DAY, 10, 'Ana López', 'cliente@porelgusto.mx', '55 1234 5678', 'Laura López',
        '55 2222 3333', '12:00 - 15:00', 2835.00, 0, 0.00, 391.03, 2835.00, 'TARJETA', 'APROBADO', 'Simulado',
        'SIM-DEMO-0002', CURRENT_TIMESTAMP - INTERVAL '1' DAY, 9, 'TRK730518', 'Marco Jiménez', '55 9876 5432', CURRENT_TIMESTAMP + INTERVAL '1' DAY, NULL, NULL,
        FALSE, 'Para la mejor mamá del mundo. ¡Te quiero! — Ana', 'b8e2d4f6a1c34e7f9b0d2c4e6f8a1b3d');
INSERT IGNORE INTO HistorialPedido (idHistorial, idPedido, estado, nota, autor, fechaHora) VALUES
    (5, 5, 'Pendiente', 'Pago aprobado', 'Ana López', CURRENT_TIMESTAMP - INTERVAL '6' DAY),
    (6, 5, 'Preparando', 'Pedido confirmado', 'Elena Aldana', CURRENT_TIMESTAMP - INTERVAL '6' DAY + INTERVAL '2' HOUR),
    (7, 5, 'En camino', NULL, 'Elena Aldana', CURRENT_TIMESTAMP - INTERVAL '5' DAY - INTERVAL '3' HOUR),
    (8, 5, 'Entregado', NULL, 'Elena Aldana', CURRENT_TIMESTAMP - INTERVAL '5' DAY),
    (9, 6, 'Pendiente', 'Pago aprobado', 'Ana López', CURRENT_TIMESTAMP - INTERVAL '1' DAY),
    (10, 6, 'Preparando', 'Pedido confirmado', 'Elena Aldana', CURRENT_TIMESTAMP - INTERVAL '1' DAY + INTERVAL '1' HOUR),
    (11, 6, 'En camino', NULL, 'Marco Jiménez', CURRENT_TIMESTAMP - INTERVAL '2' HOUR);
INSERT IGNORE INTO Ticket (idTicket, folioVenta, fecha, hora, total, idPedido) VALUES
    (5, 'TK-PG2611190427', CURRENT_DATE - INTERVAL '6' DAY, '11:20:00', 1350.00, 5),
    (6, 'TK-PG2611240913', CURRENT_DATE - INTERVAL '1' DAY, '18:05:00', 2835.00, 6);
INSERT IGNORE INTO Notificacion (idNotificacion, idUsuario, titulo, cuerpo, liga, leida, fechaHora) VALUES
    (1, 10, '¡Bienvenido a Por el Gusto!', 'Tu cuenta está lista.', '/cuenta', TRUE, CURRENT_TIMESTAMP - INTERVAL '7' DAY),
    (2, 10, 'Pedido PG2611240913: En camino', 'Tu pedido PG2611240913 ahora está: En camino.', '/pedido/PG2611240913', FALSE, CURRENT_TIMESTAMP - INTERVAL '2' HOUR);

-- ============================================================
-- 21. Producto de prueba para probar pagos reales: $10 MXN (mínimo de Stripe), oculto del catálogo y sin envío.
--     Solo se llega con su liga: /producto/PG-000
-- ============================================================
INSERT IGNORE INTO ArregloFloral (idArregloFloral, sku, nombre, detalles, costo, idTipoArreglo, categoria, etiqueta, disponibilidad, existencias, puntoReorden, activo, personalizable, permiteQR, vistas, ventas, fechaAlta, prueba) VALUES
    (999, 'PG-000', 'Producto de prueba', 'Para probar los pagos con tarjeta y PayPal. No se entrega nada ni se cobra envío.', 10.00, 2, 'Detalles', NULL, 'Disponible', 999, 0, TRUE, FALSE, FALSE, 0, 0, CURRENT_TIMESTAMP, TRUE);
INSERT IGNORE INTO Imagen (idImagen, nombre, direccionImg, idArregloFloral) VALUES
    (999, 'Producto de prueba', '/imagenes/logo.png', 999);
