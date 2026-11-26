Create database porElGusto;
use porElGusto;
CREATE TABLE TipoUsuario (
  idTipoUsuario INT PRIMARY KEY AUTO_INCREMENT,
  rol VARCHAR(100)
);

CREATE TABLE Usuario (
  idUsuario INT PRIMARY KEY AUTO_INCREMENT,
  nombre VARCHAR(150),
  direccion VARCHAR(255),
  password VARCHAR(255),
  correoElectronico VARCHAR(150),
  telefono VARCHAR(50),
  fechaRegistro DATE,
  idTipoUsuario INT,
  fechaNacimiento DATE,
  activo BIT(1),
  FOREIGN KEY (idTipoUsuario) REFERENCES TipoUsuario(idTipoUsuario)
);

CREATE TABLE Contacto (
  idContacto INT PRIMARY KEY AUTO_INCREMENT,
  nombre VARCHAR(150),
  tipoContacto VARCHAR(100),
  idUsuario INT,
  telefono VARCHAR(30),
  calle VARCHAR(160),
  colonia VARCHAR(80),
  ciudad VARCHAR(80),
  codigoPostal VARCHAR(10),
  referencias VARCHAR(200),
  activo BIT(1),
  FOREIGN KEY (idUsuario) REFERENCES Usuario(idUsuario)
);

CREATE TABLE MetodoPago (
  idMetodosDePago INT PRIMARY KEY AUTO_INCREMENT,
  tipoPago VARCHAR(100),
  descripcion VARCHAR(255),
  idUsuario INT,
  FOREIGN KEY (idUsuario) REFERENCES Usuario(idUsuario)
);

CREATE TABLE ListaDeDeseos (
  idLista INT PRIMARY KEY AUTO_INCREMENT,
  fechaCreacion DATE,
  idUsuario INT,
  FOREIGN KEY (idUsuario) REFERENCES Usuario(idUsuario)
);

-- Categorías del menú, grupos y temporadas (los productos guardan el nombre de su categoría)
CREATE TABLE Categoria (
  idCategoria INT PRIMARY KEY AUTO_INCREMENT,
  nombre VARCHAR(80),
  slug VARCHAR(80),
  descripcion VARCHAR(300),
  tipo VARCHAR(20),
  fechaInicio DATE,
  fechaFin DATE,
  orden INT
);

CREATE TABLE ArregloFloral (
  idArregloFloral INT PRIMARY KEY AUTO_INCREMENT,
  nombre VARCHAR(150),
  detalles VARCHAR(255),
  costo DECIMAL(10,2),
  idTipoArreglo INT,
  categoria VARCHAR(100),
  etiqueta VARCHAR(100),
  disponibilidad VARCHAR(100),
  sku VARCHAR(20),
  existencias INT,
  puntoReorden INT,
  activo BIT(1),
  personalizable BIT(1),
  permiteQR BIT(1),
  vistas BIGINT,
  ventas INT,
  fechaAlta DATETIME(6),
  prueba BIT(1)
);

CREATE TABLE ArregloFloral_Coleccion (
  idArregloFloral INT,
  idCategoria INT,
  PRIMARY KEY (idArregloFloral, idCategoria),
  FOREIGN KEY (idArregloFloral) REFERENCES ArregloFloral(idArregloFloral),
  FOREIGN KEY (idCategoria) REFERENCES Categoria(idCategoria)
);

CREATE TABLE ListaDeseo_ArregloFloral (
  idLista INT,
  idArregloFloral INT,
  avisado BIT(1),
  fechaAgregado DATETIME(6),
  PRIMARY KEY (idLista, idArregloFloral),
  FOREIGN KEY (idLista) REFERENCES ListaDeDeseos(idLista),
  FOREIGN KEY (idArregloFloral) REFERENCES ArregloFloral(idArregloFloral)
);

CREATE TABLE CarritoDeCompra (
  idCarritoDeCompra INT PRIMARY KEY AUTO_INCREMENT,
  fecha DATE,
  total DECIMAL(10,2),
  idUsuario INT,
  FOREIGN KEY (idUsuario) REFERENCES Usuario(idUsuario)
);

CREATE TABLE ContenidoCarrito (
  idContenido INT PRIMARY KEY AUTO_INCREMENT,
  cantidad INT,
  precioUnitario DECIMAL(10,2),
  idCarritoDeCompra INT,
  idArregloFloral INT,
  personalizacion VARCHAR(500),
  nota VARCHAR(300),
  FOREIGN KEY (idCarritoDeCompra) REFERENCES CarritoDeCompra(idCarritoDeCompra),
  FOREIGN KEY (idArregloFloral) REFERENCES ArregloFloral(idArregloFloral)
);

CREATE TABLE Cupon (
  idCupon INT PRIMARY KEY AUTO_INCREMENT,
  nombre VARCHAR(150),
  codigo VARCHAR(100),
  fechaVencimiento DATE,
  limiteDeUsos INT,
  estado VARCHAR(50),
  porcentajeDescuento DECIMAL(5,2),
  montoDescuento DECIMAL(10,2),
  compraMinima DECIMAL(10,2),
  fechaInicio DATE,
  usos INT,
  descripcion VARCHAR(300)
);

CREATE TABLE Pedido (
  idPedido INT PRIMARY KEY AUTO_INCREMENT,
  fechaCreacion DATE,
  estadoEnvio VARCHAR(100),
  direccionEntrega VARCHAR(255),
  fechaEntrega DATE,
  instruccionesEspeciales VARCHAR(255),
  idCarritoDeCompra INT,
  idMetodosDePago INT,
  idCupon INT,
  folio VARCHAR(20),
  fechaHora DATETIME(6),
  idUsuario INT,
  nombreCliente VARCHAR(120),
  correoCliente VARCHAR(120),
  telefonoCliente VARCHAR(30),
  destinatario VARCHAR(120),
  telefonoDestinatario VARCHAR(30),
  horarioEntrega VARCHAR(20),
  distanciaKm DOUBLE,
  subtotal DECIMAL(10,2),
  descuento DECIMAL(10,2),
  costoEnvio DECIMAL(10,2),
  iva DECIMAL(10,2),
  total DECIMAL(10,2),
  formaPago VARCHAR(20),
  estadoPago VARCHAR(30),
  proveedorPago VARCHAR(40),
  transaccion VARCHAR(120),
  fechaPago DATETIME(6),
  idEmpleado INT,
  numeroGuia VARCHAR(20),
  repartidor VARCHAR(120),
  telefonoRepartidor VARCHAR(30),
  horaEstimada DATETIME(6),
  latitud DOUBLE,
  longitud DOUBLE,
  ubicacionActualizada DATETIME(6),
  recibio VARCHAR(120),
  fechaEntregado DATETIME(6),
  entregaVerificadaQR BIT(1),
  dedicatoria VARCHAR(500),
  tokenQr VARCHAR(40),
  FOREIGN KEY (idCarritoDeCompra) REFERENCES CarritoDeCompra(idCarritoDeCompra),
  FOREIGN KEY (idMetodosDePago) REFERENCES MetodoPago(idMetodosDePago),
  FOREIGN KEY (idUsuario) REFERENCES Usuario(idUsuario),
  FOREIGN KEY (idEmpleado) REFERENCES Usuario(idUsuario),
  FOREIGN KEY (idCupon) REFERENCES Cupon(idCupon)
);

CREATE TABLE Ticket (
  idTicket INT PRIMARY KEY AUTO_INCREMENT,
  folioVenta VARCHAR(100),
  fecha DATE,
  hora TIME,
  total DECIMAL(10,2),
  idPedido INT UNIQUE,
  FOREIGN KEY (idPedido) REFERENCES Pedido(idPedido)
);

CREATE TABLE Factura (
  idFactura INT PRIMARY KEY AUTO_INCREMENT,
  RFC VARCHAR(20),
  razonSocial VARCHAR(150),
  direccionFiscal VARCHAR(255),
  cfdi VARCHAR(100),
  fechaEmision DATE,
  total DECIMAL(10,2),
  idPedido INT UNIQUE,
  FOREIGN KEY (idPedido) REFERENCES Pedido(idPedido)
);

CREATE TABLE Imagen (
  idImagen INT PRIMARY KEY AUTO_INCREMENT,
  nombre VARCHAR(150),
  direccionImg VARCHAR(255),
  idArregloFloral INT,
  FOREIGN KEY (idArregloFloral) REFERENCES ArregloFloral(idArregloFloral)
);

CREATE TABLE Modificacion (
  idModificacion INT PRIMARY KEY AUTO_INCREMENT,
  nombre VARCHAR(150),
  descripcion VARCHAR(255),
  idCosto INT,
  tipoModificacion VARCHAR(100),
  idArregloFloral INT,
  disponible BIT(1),
  orden INT,
  FOREIGN KEY (idArregloFloral) REFERENCES ArregloFloral(idArregloFloral)
);

-- ============================================================
-- Tienda en línea: historial, promociones, notificaciones, correos, recuperación y soporte
-- ============================================================
CREATE TABLE HistorialPedido (
  idHistorial INT PRIMARY KEY AUTO_INCREMENT,
  idPedido INT NOT NULL,
  estado VARCHAR(30),
  nota VARCHAR(500),
  autor VARCHAR(120),
  fechaHora DATETIME(6),
  FOREIGN KEY (idPedido) REFERENCES Pedido(idPedido)
);

CREATE TABLE Promocion (
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

CREATE TABLE Promocion_ArregloFloral (
  idPromocion INT,
  idArregloFloral INT,
  PRIMARY KEY (idArregloFloral, idPromocion),
  FOREIGN KEY (idPromocion) REFERENCES Promocion(idPromocion),
  FOREIGN KEY (idArregloFloral) REFERENCES ArregloFloral(idArregloFloral)
);

CREATE TABLE Notificacion (
  idNotificacion INT PRIMARY KEY AUTO_INCREMENT,
  idUsuario INT NOT NULL,
  titulo VARCHAR(150),
  cuerpo VARCHAR(500),
  liga VARCHAR(200),
  leida BIT(1),
  fechaHora DATETIME(6),
  FOREIGN KEY (idUsuario) REFERENCES Usuario(idUsuario)
);

CREATE TABLE Correo (
  idCorreo INT PRIMARY KEY AUTO_INCREMENT,
  para VARCHAR(150),
  asunto VARCHAR(200),
  cuerpo VARCHAR(4000),
  fechaHora DATETIME(6)
);

CREATE TABLE CodigoRecuperacion (
  idCodigo INT PRIMARY KEY AUTO_INCREMENT,
  idUsuario INT NOT NULL,
  codigoHash VARCHAR(100),
  vence DATETIME(6),
  usado BIT(1),
  intentos INT,
  fechaHora DATETIME(6),
  FOREIGN KEY (idUsuario) REFERENCES Usuario(idUsuario)
);

CREATE TABLE SolicitudSoporte (
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

CREATE TABLE MensajeSoporte (
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

CREATE TABLE ConfiguracionTienda (
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

CREATE TABLE Visita (
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
