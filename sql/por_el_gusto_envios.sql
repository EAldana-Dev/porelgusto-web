CREATE DATABASE IF NOT EXISTS por_el_gusto_envios
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE por_el_gusto_envios;

-- 1. Roles o tipos de usuario
CREATE TABLE tipo_usuario (
    id_tipo_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    descripcion VARCHAR(150)
) ENGINE=InnoDB;

-- 2. Catalogo de metodos de pago (no guarda numeros de tarjeta ni datos bancarios)
CREATE TABLE metodo_pago (
    id_metodo_pago INT AUTO_INCREMENT PRIMARY KEY,
    tipo_pago VARCHAR(50) NOT NULL,
    descripcion VARCHAR(150),
    estado BOOLEAN NOT NULL DEFAULT TRUE
) ENGINE=InnoDB;

-- 3. Usuarios
CREATE TABLE usuario (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    id_tipo_usuario INT NOT NULL,
    id_metodo_pago_favorito INT NULL,
    nombre VARCHAR(100) NOT NULL,
    direccion VARCHAR(255),
    correo_electronico VARCHAR(150) NOT NULL,
    telefono VARCHAR(20),
    contrasena_hash VARCHAR(255) NOT NULL,
    fecha_registro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_usuario_tipo
        FOREIGN KEY (id_tipo_usuario) REFERENCES tipo_usuario(id_tipo_usuario),
    CONSTRAINT fk_usuario_metodo_favorito
        FOREIGN KEY (id_metodo_pago_favorito) REFERENCES metodo_pago(id_metodo_pago)
) ENGINE=InnoDB;

-- 4. Contactos adicionales de cada usuario
CREATE TABLE contacto (
    id_contacto INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    nombre VARCHAR(100),
    tipo_contacto VARCHAR(30) NOT NULL,
    dato_contacto VARCHAR(150) NOT NULL,
    CONSTRAINT fk_contacto_usuario
        FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
) ENGINE=InnoDB;

-- 5. Arreglos florales del catalogo
CREATE TABLE arreglo_floral (
    id_arreglo_floral INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    tipo_arreglo VARCHAR(60),
    detalles VARCHAR(255),
    costo DECIMAL(10,2) NOT NULL,
    disponibilidad BOOLEAN NOT NULL DEFAULT TRUE,
    categoria VARCHAR(60),
    etiqueta VARCHAR(60)
) ENGINE=InnoDB;

-- 6. Imagenes asociadas a un arreglo floral; se guarda la ruta, no la imagen en si
CREATE TABLE imagen (
    id_imagen INT AUTO_INCREMENT PRIMARY KEY,
    id_arreglo_floral INT NOT NULL,
    nombre VARCHAR(100),
    direccion_img VARCHAR(500) NOT NULL,
    CONSTRAINT fk_imagen_arreglo
        FOREIGN KEY (id_arreglo_floral) REFERENCES arreglo_floral(id_arreglo_floral)
) ENGINE=InnoDB;

-- 7. Modificaciones que se ofrecen para cada arreglo floral
CREATE TABLE modificacion (
    id_modificacion INT AUTO_INCREMENT PRIMARY KEY,
    id_arreglo_floral INT NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255),
    tipo_modificacion VARCHAR(60),
    costo_adicional DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    CONSTRAINT fk_modificacion_arreglo
        FOREIGN KEY (id_arreglo_floral) REFERENCES arreglo_floral(id_arreglo_floral)
) ENGINE=InnoDB;

-- 8. Carritos de compra de los usuarios
CREATE TABLE carrito_compra (
    id_carrito INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    total DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    estado VARCHAR(30) NOT NULL DEFAULT 'Abierto',
    CONSTRAINT fk_carrito_usuario
        FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
) ENGINE=InnoDB;

-- 9. Productos guardados dentro de cada carrito
CREATE TABLE contenido_carrito (
    id_contenido INT AUTO_INCREMENT PRIMARY KEY,
    id_carrito INT NOT NULL,
    id_arreglo_floral INT NOT NULL,
    cantidad INT NOT NULL DEFAULT 1,
    precio_unitario DECIMAL(10,2) NOT NULL,
    personalizacion TEXT,
    costo_adicional DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    CONSTRAINT fk_contenido_carrito
        FOREIGN KEY (id_carrito) REFERENCES carrito_compra(id_carrito),
    CONSTRAINT fk_contenido_arreglo
        FOREIGN KEY (id_arreglo_floral) REFERENCES arreglo_floral(id_arreglo_floral)
) ENGINE=InnoDB;

-- 10. Lista de deseos: una lista por usuario
CREATE TABLE lista_deseos (
    id_lista INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL UNIQUE,
    fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_lista_usuario
        FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
) ENGINE=InnoDB;

-- 11. Arreglos guardados dentro de la lista de deseos
CREATE TABLE detalle_lista_deseos (
    id_detalle_lista INT AUTO_INCREMENT PRIMARY KEY,
    id_lista INT NOT NULL,
    id_arreglo_floral INT NOT NULL,
    fecha_agregado DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    personalizacion TEXT,
    CONSTRAINT fk_detalle_lista
        FOREIGN KEY (id_lista) REFERENCES lista_deseos(id_lista),
    CONSTRAINT fk_detalle_lista_arreglo
        FOREIGN KEY (id_arreglo_floral) REFERENCES arreglo_floral(id_arreglo_floral)
) ENGINE=InnoDB;

-- 12. Cupones disponibles
CREATE TABLE cupon (
    id_cupon INT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(30) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    fecha_vencimiento DATE,
    limite_de_usos INT,
    estado BOOLEAN NOT NULL DEFAULT TRUE,
    porcentaje_descuento DECIMAL(5,2) NOT NULL
) ENGINE=InnoDB;

-- 13. Pedidos: cada pedido corresponde a un carrito ya guardado/convertido
CREATE TABLE pedido (
    id_pedido INT AUTO_INCREMENT PRIMARY KEY,
    id_carrito INT NOT NULL UNIQUE,
    id_metodo_pago INT NOT NULL,
    id_cupon INT NULL,
    fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    estado_envio VARCHAR(40) NOT NULL DEFAULT 'Pendiente',
    direccion_entrega VARCHAR(255) NOT NULL,
    fecha_entrega DATETIME,
    instrucciones_especiales TEXT,
    dedicatoria VARCHAR(255),
    total DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_pedido_carrito
        FOREIGN KEY (id_carrito) REFERENCES carrito_compra(id_carrito),
    CONSTRAINT fk_pedido_metodo_pago
        FOREIGN KEY (id_metodo_pago) REFERENCES metodo_pago(id_metodo_pago),
    CONSTRAINT fk_pedido_cupon
        FOREIGN KEY (id_cupon) REFERENCES cupon(id_cupon)
) ENGINE=InnoDB;

-- 14. Ticket generado para un pedido
CREATE TABLE ticket (
    id_ticket INT AUTO_INCREMENT PRIMARY KEY,
    id_pedido INT NOT NULL UNIQUE,
    folio_venta VARCHAR(40) NOT NULL UNIQUE,
    fecha DATE NOT NULL,
    hora TIME NOT NULL,
    total DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_ticket_pedido
        FOREIGN KEY (id_pedido) REFERENCES pedido(id_pedido)
) ENGINE=InnoDB;

-- 15. Factura asociada a un pedido
CREATE TABLE factura (
    id_factura INT AUTO_INCREMENT PRIMARY KEY,
    id_pedido INT NOT NULL UNIQUE,
    rfc VARCHAR(13) NOT NULL,
    razon_social VARCHAR(150) NOT NULL,
    direccion_fiscal VARCHAR(255),
    uuid_cfdi VARCHAR(36) UNIQUE,
    fecha_emision DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    total DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_factura_pedido
        FOREIGN KEY (id_pedido) REFERENCES pedido(id_pedido)
) ENGINE=InnoDB;
