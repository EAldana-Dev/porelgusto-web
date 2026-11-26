<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${titulo}</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: sans-serif;
        }

        body {
            background-color: #fff;
            padding: 15px;
        }

        .contenedor-principal {
            display: flex;
            height: 95vh;
            border: 1px solid #000;
            padding: 10px;
            gap: 20px;
        }

        .barra-lateral {
            width: 200px;
            display: flex;
            flex-direction: column;
            gap: 15px;
        }

        .caja-navegacion {
            border: 1px solid #000;
            padding: 20px 10px;
            font-size: 14px;
            text-align: left;
        }

        .contenido-central {
            flex: 1;
            display: flex;
            flex-direction: column;
            gap: 15px;
        }

        .foto-grande {
            flex: 1;
            border: 1px solid #000;
            display: flex;
            justify-content: center;
            align-items: center;
            font-size: 14px;
        }

        .contenedor-miniaturas {
            display: flex;
            gap: 10px;
            height: 80px;
        }

        .miniatura {
            flex: 1;
            border: 1px solid #000;
            display: flex;
            justify-content: center;
            align-items: center;
            font-size: 12px;
        }

        .panel-derecho {
            width: 350px;
            display: flex;
            flex-direction: column;
            gap: 25px;
            padding-top: 10px;
        }

        .grupo-campo {
            display: flex;
            flex-direction: column;
            gap: 5px;
        }

        .grupo-campo label {
            font-size: 12px;
        }

        .grupo-campo input {
            border: 1px solid #000;
            padding: 8px;
            outline: none;
            width: 100%;
        }

        .contenedor-etiquetas {
            border: 1px solid #000;
            padding: 15px 10px;
            display: flex;
            gap: 15px;
            flex-wrap: wrap;
            min-height: 80px;
            align-items: flex-start;
        }

        .etiqueta {
            border: 1px solid #000;
            padding: 5px 15px;
            font-size: 12px;
            text-align: center;
        }

        .boton-mas {
            font-size: 16px;
            display: flex;
            align-items: center;
            justify-content: center;
            cursor: pointer;
        }
    </style>
</head>
<body>

<div class="contenedor-principal">

    <aside class="barra-lateral">
        <div class="caja-navegacion">barra de navegacion</div>
        <div class="caja-navegacion">arreglo de flores</div>
        <div class="caja-navegacion">envios</div>
        <div class="caja-navegacion">usuarios</div>
        <div class="caja-navegacion">inventario</div>
    </aside>

    <main class="contenido-central">
        <div class="foto-grande">foto en grande</div>

        <div class="contenedor-miniaturas">
            <div class="miniatura">foto 1</div>
            <div class="miniatura">foto 2</div>
            <div class="miniatura">foto 3</div>
            <div class="miniatura boton-mas">+</div>
        </div>
    </main>

    <aside class="panel-derecho">
        <div class="grupo-campo">
            <label>nombre</label>
            <input type="text">
        </div>

        <div class="grupo-campo">
            <label>precio</label>
            <input type="text">
        </div>

        <div class="grupo-campo">
            <label>etiquetas</label>
            <div class="contenedor-etiquetas">
                <div class="etiqueta">asdsadasd</div>
                <div class="etiqueta"></div>
                <div class="etiqueta"></div>
                <div class="boton-mas">+</div>
            </div>
        </div>
    </aside>

</div>

</body>
</html>
