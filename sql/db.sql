Create database porElGusto;
use porElGusto;
CREATE TABLE TipoUsuario (
  idTipoUsuario INT PRIMARY KEY,
  rol VARCHAR(100)
);

CREATE TABLE Usuario (
  idUsuario INT PRIMARY KEY,
  nombre VARCHAR(150),
  direccion VARCHAR(255),
  password VARCHAR(255),
  correoElectronico VARCHAR(150),
  telefono VARCHAR(50),
  fechaRegistro DATE,
  idTipoUsuario INT,
  FOREIGN KEY (idTipoUsuario) REFERENCES TipoUsuario(idTipoUsuario)
);

CREATE TABLE Contacto (
  idContacto INT PRIMARY KEY,
  nombre VARCHAR(150),
  tipoContacto VARCHAR(100),
  idUsuario INT,
  FOREIGN KEY (idUsuario) REFERENCES Usuario(idUsuario)
);

CREATE TABLE MetodoPago (
  idMetodosDePago INT PRIMARY KEY,
  tipoPago VARCHAR(100),
  descripcion VARCHAR(255),
  idUsuario INT,
  FOREIGN KEY (idUsuario) REFERENCES Usuario(idUsuario)
);

CREATE TABLE ListaDeDeseos (
  idLista INT PRIMARY KEY,
  fechaCreacion DATE,
  idUsuario INT,
  FOREIGN KEY (idUsuario) REFERENCES Usuario(idUsuario)
);

CREATE TABLE ArregloFloral (
  idArregloFloral INT PRIMARY KEY,
  nombre VARCHAR(150),
  detalles VARCHAR(255),
  costo DECIMAL(10,2),
  idTipoArreglo INT,
  categoria VARCHAR(100),
  etiqueta VARCHAR(100),
  disponibilidad VARCHAR(100)
);

CREATE TABLE ListaDeseo_ArregloFloral (
  idLista INT,
  idArregloFloral INT,
  PRIMARY KEY (idLista, idArregloFloral),
  FOREIGN KEY (idLista) REFERENCES ListaDeDeseos(idLista),
  FOREIGN KEY (idArregloFloral) REFERENCES ArregloFloral(idArregloFloral)
);

CREATE TABLE CarritoDeCompra (
  idCarritoDeCompra INT PRIMARY KEY,
  fecha DATE,
  total DECIMAL(10,2),
  idUsuario INT,
  FOREIGN KEY (idUsuario) REFERENCES Usuario(idUsuario)
);

CREATE TABLE ContenidoCarrito (
  idContenido INT PRIMARY KEY,
  cantidad INT,
  precioUnitario DECIMAL(10,2),
  idCarritoDeCompra INT,
  idArregloFloral INT,
  FOREIGN KEY (idCarritoDeCompra) REFERENCES CarritoDeCompra(idCarritoDeCompra),
  FOREIGN KEY (idArregloFloral) REFERENCES ArregloFloral(idArregloFloral)
);

CREATE TABLE Cupon (
  idCupon INT PRIMARY KEY,
  nombre VARCHAR(150),
  codigo VARCHAR(100),
  fechaVencimiento DATE,
  limiteDeUsos INT,
  estado VARCHAR(50),
  porcentajeDescuento DECIMAL(5,2)
);

CREATE TABLE Pedido (
  idPedido INT PRIMARY KEY,
  fechaCreacion DATE,
  estadoEnvio VARCHAR(100),
  direccionEntrega VARCHAR(255),
  fechaEntrega DATE,
  instruccionesEspeciales VARCHAR(255),
  idCarritoDeCompra INT,
  idMetodosDePago INT,
  idCupon INT,
  FOREIGN KEY (idCarritoDeCompra) REFERENCES CarritoDeCompra(idCarritoDeCompra),
  FOREIGN KEY (idMetodosDePago) REFERENCES MetodoPago(idMetodosDePago),
  FOREIGN KEY (idCupon) REFERENCES Cupon(idCupon)
);

CREATE TABLE Ticket (
  idTicket INT PRIMARY KEY,
  folioVenta VARCHAR(100),
  fecha DATE,
  hora TIME,
  total DECIMAL(10,2),
  idPedido INT UNIQUE,
  FOREIGN KEY (idPedido) REFERENCES Pedido(idPedido)
);

CREATE TABLE Factura (
  idFactura INT PRIMARY KEY,
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
  idImagen INT PRIMARY KEY,
  nombre VARCHAR(150),
  direccionImg VARCHAR(255),
  idArregloFloral INT,
  FOREIGN KEY (idArregloFloral) REFERENCES ArregloFloral(idArregloFloral)
);

CREATE TABLE Modificacion (
  idModificacion INT PRIMARY KEY,
  nombre VARCHAR(150),
  descripcion VARCHAR(255),
  idCosto INT,
  tipoModificacion VARCHAR(100),
  idArregloFloral INT,
  FOREIGN KEY (idArregloFloral) REFERENCES ArregloFloral(idArregloFloral)
);