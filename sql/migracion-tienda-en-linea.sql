-- Migración para bases de datos creadas con la versión anterior de sql/db.sql.
-- Agrega las columnas y tablas de la tienda en línea. Ejecutar una sola vez, antes de arrancar con el perfil prod
-- (en dev no hace falta: Hibernate las agrega solo con ddl-auto=update).
USE porElGusto;

ALTER TABLE Usuario ADD COLUMN fechaNacimiento DATE;
ALTER TABLE Usuario ADD COLUMN activo BIT(1);

ALTER TABLE Contacto ADD COLUMN telefono VARCHAR(30);
ALTER TABLE Contacto ADD COLUMN calle VARCHAR(160);
ALTER TABLE Contacto ADD COLUMN colonia VARCHAR(80);
ALTER TABLE Contacto ADD COLUMN ciudad VARCHAR(80);
ALTER TABLE Contacto ADD COLUMN codigoPostal VARCHAR(10);
ALTER TABLE Contacto ADD COLUMN referencias VARCHAR(200);
ALTER TABLE Contacto ADD COLUMN activo BIT(1);

ALTER TABLE ArregloFloral ADD COLUMN sku VARCHAR(20);
ALTER TABLE ArregloFloral ADD COLUMN existencias INT;
ALTER TABLE ArregloFloral ADD COLUMN puntoReorden INT;
ALTER TABLE ArregloFloral ADD COLUMN activo BIT(1);
ALTER TABLE ArregloFloral ADD COLUMN personalizable BIT(1);
ALTER TABLE ArregloFloral ADD COLUMN permiteQR BIT(1);
ALTER TABLE ArregloFloral ADD COLUMN vistas BIGINT;
ALTER TABLE ArregloFloral ADD COLUMN ventas INT;
ALTER TABLE ArregloFloral ADD COLUMN fechaAlta DATETIME(6);
ALTER TABLE ArregloFloral ADD COLUMN prueba BIT(1);

ALTER TABLE ListaDeseo_ArregloFloral ADD COLUMN avisado BIT(1);
ALTER TABLE ListaDeseo_ArregloFloral ADD COLUMN fechaAgregado DATETIME(6);

ALTER TABLE ContenidoCarrito ADD COLUMN personalizacion VARCHAR(500);
ALTER TABLE ContenidoCarrito ADD COLUMN nota VARCHAR(300);

ALTER TABLE Cupon ADD COLUMN montoDescuento DECIMAL(10,2);
ALTER TABLE Cupon ADD COLUMN compraMinima DECIMAL(10,2);
ALTER TABLE Cupon ADD COLUMN fechaInicio DATE;
ALTER TABLE Cupon ADD COLUMN usos INT;
ALTER TABLE Cupon ADD COLUMN descripcion VARCHAR(300);

ALTER TABLE Pedido ADD COLUMN folio VARCHAR(20);
ALTER TABLE Pedido ADD COLUMN fechaHora DATETIME(6);
ALTER TABLE Pedido ADD COLUMN idUsuario INT;
ALTER TABLE Pedido ADD COLUMN nombreCliente VARCHAR(120);
ALTER TABLE Pedido ADD COLUMN correoCliente VARCHAR(120);
ALTER TABLE Pedido ADD COLUMN telefonoCliente VARCHAR(30);
ALTER TABLE Pedido ADD COLUMN destinatario VARCHAR(120);
ALTER TABLE Pedido ADD COLUMN telefonoDestinatario VARCHAR(30);
ALTER TABLE Pedido ADD COLUMN horarioEntrega VARCHAR(20);
ALTER TABLE Pedido ADD COLUMN distanciaKm DOUBLE;
ALTER TABLE Pedido ADD COLUMN subtotal DECIMAL(10,2);
ALTER TABLE Pedido ADD COLUMN descuento DECIMAL(10,2);
ALTER TABLE Pedido ADD COLUMN costoEnvio DECIMAL(10,2);
ALTER TABLE Pedido ADD COLUMN iva DECIMAL(10,2);
ALTER TABLE Pedido ADD COLUMN total DECIMAL(10,2);
ALTER TABLE Pedido ADD COLUMN formaPago VARCHAR(20);
ALTER TABLE Pedido ADD COLUMN estadoPago VARCHAR(30);
ALTER TABLE Pedido ADD COLUMN proveedorPago VARCHAR(40);
ALTER TABLE Pedido ADD COLUMN transaccion VARCHAR(120);
ALTER TABLE Pedido ADD COLUMN fechaPago DATETIME(6);
ALTER TABLE Pedido ADD COLUMN idEmpleado INT;
ALTER TABLE Pedido ADD COLUMN numeroGuia VARCHAR(20);
ALTER TABLE Pedido ADD COLUMN repartidor VARCHAR(120);
ALTER TABLE Pedido ADD COLUMN telefonoRepartidor VARCHAR(30);
ALTER TABLE Pedido ADD COLUMN horaEstimada DATETIME(6);
ALTER TABLE Pedido ADD COLUMN latitud DOUBLE;
ALTER TABLE Pedido ADD COLUMN longitud DOUBLE;
ALTER TABLE Pedido ADD COLUMN ubicacionActualizada DATETIME(6);
ALTER TABLE Pedido ADD COLUMN recibio VARCHAR(120);
ALTER TABLE Pedido ADD COLUMN fechaEntregado DATETIME(6);
ALTER TABLE Pedido ADD COLUMN entregaVerificadaQR BIT(1);
ALTER TABLE Pedido ADD COLUMN dedicatoria VARCHAR(500);
ALTER TABLE Pedido ADD COLUMN tokenQr VARCHAR(40);
ALTER TABLE Pedido ADD FOREIGN KEY (idUsuario) REFERENCES Usuario(idUsuario);
ALTER TABLE Pedido ADD FOREIGN KEY (idEmpleado) REFERENCES Usuario(idUsuario);

ALTER TABLE Modificacion ADD COLUMN disponible BIT(1);
ALTER TABLE Modificacion ADD COLUMN orden INT;

CREATE TABLE IF NOT EXISTS Categoria (
  idCategoria INT PRIMARY KEY AUTO_INCREMENT,
  nombre VARCHAR(80),
  slug VARCHAR(80),
  descripcion VARCHAR(300),
  tipo VARCHAR(20),
  fechaInicio DATE,
  fechaFin DATE,
  orden INT
);

CREATE TABLE IF NOT EXISTS ArregloFloral_Coleccion (
  idArregloFloral INT,
  idCategoria INT,
  PRIMARY KEY (idArregloFloral, idCategoria),
  FOREIGN KEY (idArregloFloral) REFERENCES ArregloFloral(idArregloFloral),
  FOREIGN KEY (idCategoria) REFERENCES Categoria(idCategoria)
);

CREATE TABLE IF NOT EXISTS HistorialPedido (
  idHistorial INT PRIMARY KEY AUTO_INCREMENT,
  idPedido INT NOT NULL,
  estado VARCHAR(30),
  nota VARCHAR(500),
  autor VARCHAR(120),
  fechaHora DATETIME(6),
  FOREIGN KEY (idPedido) REFERENCES Pedido(idPedido)
);

CREATE TABLE IF NOT EXISTS Promocion (
  idPromocion INT PRIMARY KEY AUTO_INCREMENT,
  nombre VARCHAR(80),
  descripcion VARCHAR(500),
  tipo VARCHAR(20),
  tipoDescuento VARCHAR(20),
  valor DECIMAL(10,2),
  condiciones VARCHAR(300),
  fechaInicio DATETIME(6),
  fechaFin DATETIME(6),
  activa BIT(1),
  idCategoria INT,
  FOREIGN KEY (idCategoria) REFERENCES Categoria(idCategoria)
);

CREATE TABLE IF NOT EXISTS Promocion_ArregloFloral (
  idPromocion INT,
  idArregloFloral INT,
  PRIMARY KEY (idArregloFloral, idPromocion),
  FOREIGN KEY (idPromocion) REFERENCES Promocion(idPromocion),
  FOREIGN KEY (idArregloFloral) REFERENCES ArregloFloral(idArregloFloral)
);

CREATE TABLE IF NOT EXISTS Notificacion (
  idNotificacion INT PRIMARY KEY AUTO_INCREMENT,
  idUsuario INT NOT NULL,
  titulo VARCHAR(150),
  cuerpo VARCHAR(500),
  liga VARCHAR(200),
  leida BIT(1),
  fechaHora DATETIME(6),
  FOREIGN KEY (idUsuario) REFERENCES Usuario(idUsuario)
);

CREATE TABLE IF NOT EXISTS Correo (
  idCorreo INT PRIMARY KEY AUTO_INCREMENT,
  para VARCHAR(150),
  asunto VARCHAR(200),
  cuerpo VARCHAR(4000),
  fechaHora DATETIME(6)
);

CREATE TABLE IF NOT EXISTS CodigoRecuperacion (
  idCodigo INT PRIMARY KEY AUTO_INCREMENT,
  idUsuario INT NOT NULL,
  codigoHash VARCHAR(100),
  vence DATETIME(6),
  usado BIT(1),
  intentos INT,
  fechaHora DATETIME(6),
  FOREIGN KEY (idUsuario) REFERENCES Usuario(idUsuario)
);

CREATE TABLE IF NOT EXISTS SolicitudSoporte (
  idSolicitud INT PRIMARY KEY AUTO_INCREMENT,
  idUsuario INT,
  nombreContacto VARCHAR(120),
  correoContacto VARCHAR(120),
  asunto VARCHAR(120),
  estado VARCHAR(20),
  folioPedido VARCHAR(20),
  fechaLimite DATETIME(6),
  idAgente INT,
  fechaHora DATETIME(6),
  FOREIGN KEY (idUsuario) REFERENCES Usuario(idUsuario),
  FOREIGN KEY (idAgente) REFERENCES Usuario(idUsuario)
);

CREATE TABLE IF NOT EXISTS MensajeSoporte (
  idMensaje INT PRIMARY KEY AUTO_INCREMENT,
  idSolicitud INT NOT NULL,
  idUsuario INT,
  nombreAutor VARCHAR(120),
  contenido VARCHAR(2000),
  dePersonal BIT(1),
  fechaHora DATETIME(6),
  FOREIGN KEY (idSolicitud) REFERENCES SolicitudSoporte(idSolicitud),
  FOREIGN KEY (idUsuario) REFERENCES Usuario(idUsuario)
);

CREATE TABLE IF NOT EXISTS ConfiguracionTienda (
  idConfiguracion INT PRIMARY KEY,
  stripeSecreta VARCHAR(600),
  paypalClientId VARCHAR(200),
  paypalSecreta VARCHAR(600),
  paypalProduccion BIT(1),
  origenDireccion VARCHAR(255),
  origenLatitud DOUBLE,
  origenLongitud DOUBLE,
  actualizado DATETIME(6),
  actualizadoPor VARCHAR(150)
);

CREATE TABLE IF NOT EXISTS Visita (
  idVisita BIGINT PRIMARY KEY AUTO_INCREMENT,
  fechaHora DATETIME(6),
  ruta VARCHAR(200),
  visitante VARCHAR(64),
  origen VARCHAR(80),
  campana VARCHAR(80),
  pais VARCHAR(80),
  region VARCHAR(80),
  ciudad VARCHAR(80),
  dispositivo VARCHAR(20),
  navegador VARCHAR(30)
);
