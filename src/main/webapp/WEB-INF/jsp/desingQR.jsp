<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>${titulo}</title>
    <%-- este in css por lo mientras no se ni que hace --%>
    <style>
      .card-layout {
        text-align: center;
        padding: 20px;
      }
      .qr-card {
        display: inline-block;
        padding: 24px;
        border-radius: 16px;
        background-size: cover;
        background-position: center;
        background-repeat: no-repeat;
      }
    </style>
  </head>
  <body>
    <div class="card-layout">


      <div class="qr-card" style="background-image: url('${fondo}');">
        <h1>${producto.nombre}</h1>
        <img src="data:image/png;base64,${qrBase64}" alt="QR de ${producto.nombre}" width="250" height="250" />
      </div>
    </div>
  </body>
</html>
