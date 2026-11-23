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
-- 6. ArregloFloral
-- ============================================================
INSERT IGNORE INTO ArregloFloral (idArregloFloral, nombre, detalles, costo, idTipoArreglo, categoria, etiqueta, disponibilidad) VALUES
                                                                                                                             (1, 'Ramo de Rosas Rojas', '12 rosas rojas con follaje y lazo', 850.00, 1, 'Ramos', 'Romántico', 'Disponible'),
                                                                                                                             (2, 'Arreglo Primaveral', 'Girasoles, margaritas y tulipanes', 650.00, 2, 'Arreglos', 'Alegre', 'Disponible'),
                                                                                                                             (3, 'Orquídea Elegante', 'Orquídea phalaenopsis en maceta de cerámica', 1200.00, 3, 'Plantas', 'Elegante', 'Disponible'),
                                                                                                                             (4, 'Caja de Rosas y Chocolates', 'Rosas rosadas con chocolates Ferrero', 950.00, 1, 'Cajas', 'Romántico', 'Disponible'),
                                                                                                                             (5, 'Centro de Mesa Otoñal', 'Arreglo con hortensias y hojas secas', 780.00, 2, 'Centros de mesa', 'Otoñal', 'Disponible'),
                                                                                                                             (6, 'Ramo de Novia', 'Rosas blancas y lilas con listón de seda', 1500.00, 1, 'Ramos', 'Nupcial', 'Bajo pedido'),
                                                                                                                             (7, 'Maceta de Suculentas', 'Set de 3 suculentas en macetas de barro', 450.00, 3, 'Plantas', 'Minimalista', 'Disponible'),
                                                                                                                             (8, 'Arreglo de Cumpleaños', 'Globos, flores coloridas y tarjeta', 700.00, 2, 'Arreglos', 'Fiesta', 'Disponible');

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
-- 14. Imagen
-- ============================================================
INSERT IGNORE INTO Imagen (idImagen, nombre, direccionImg, idArregloFloral) VALUES
                                                                         (1, 'rosas_rojas_1', 'https://placehold.co/600x400/png', 1),
                                                                         (2, 'rosas_rojas_2', 'https://placehold.co/600x400/png', 1),
                                                                         (3, 'primaveral_1', 'https://placehold.co/600x400/png', 2),
                                                                         (4, 'orquidea_1', 'https://placehold.co/600x400/png', 3),
                                                                         (5, 'orquidea_2', 'https://placehold.co/600x400/png', 3),
                                                                         (6, 'caja_rosas_chocolates', 'https://placehold.co/600x400/png', 4),
                                                                         (7, 'centro_otonal_1', 'https://placehold.co/600x400/png', 5),
                                                                         (8, 'ramo_novia_1', 'https://placehold.co/600x400/png', 6),
                                                                         (9, 'suculentas_1', 'https://placehold.co/600x400/png', 7),
                                                                         (10, 'cumpleanos_1', 'https://placehold.co/600x400/png', 8);

-- ============================================================
-- 15. Modificacion
-- ============================================================
INSERT IGNORE INTO Modificacion (idModificacion, nombre, descripcion, idCosto, tipoModificacion, idArregloFloral) VALUES
                                                                                                               (1, 'Tarjeta dedicatoria', 'Mensaje impreso en papel algodón', 50, 'Extra', 1),
                                                                                                               (2, 'Extra rosas', 'Agregar 6 rosas rojas adicionales', 350, 'Tamaño', 1),
                                                                                                               (3, 'Listón dorado', 'Cambio de listón estándar a satinado dorado', 30, 'Detalle', 2);
