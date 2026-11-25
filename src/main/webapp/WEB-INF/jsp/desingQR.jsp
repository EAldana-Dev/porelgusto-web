<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>${titulo}</title>
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

      .acciones-pdf {
        margin-top: 16px;
      }
    </style>
  </head>
  <body>
    <div class="card-layout">
      <div id="cardParaPdf" class="qr-card" style="background-image: url('${fondo}');">
        <h1>${producto.nombre}</h1>
        <img src="data:image/png;base64,${qrBase64}" alt="QR de ${producto.nombre}" width="250" height="250" />
      </div>

      <div class="acciones-pdf">
        <button type="button" onclick="manejarPdf(false)">Ver PDF</button>
        <button type="button" onclick="manejarPdf(true)">Descargar PDF</button>
      </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/html2canvas@1.4.1/dist/html2canvas.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/jspdf@2.5.1/dist/jspdf.umd.min.js"></script>
    <script>
      async function construirDocPdf() {
        const card = document.getElementById("cardParaPdf");
        const { jsPDF } = window.jspdf;

        const canvas = await html2canvas(card, {
          scale: 2,
          useCORS: true,
          backgroundColor: "#ffffff"
        });

        const imgData = canvas.toDataURL("image/png");

        const pxToMm = 0.264583;
        const anchoMm = (canvas.width / 2) * pxToMm; // dividimos entre 2 por scale: 2
        const altoMm = (canvas.height / 2) * pxToMm;

        const pdf = new jsPDF({
          orientation: anchoMm > altoMm ? "landscape" : "portrait",
          unit: "mm",
          format: [anchoMm, altoMm] // Tamaño exacto de la tarjeta
        });

        pdf.addImage(imgData, "PNG", 0, 0, anchoMm, altoMm);
        return pdf;
      }

      async function manejarPdf(descargar) {
        const pdf = await construirDocPdf();
        if (descargar) {
          pdf.save("tarjeta-qr.pdf");
        } else {
          window.open(pdf.output("bloburl"), "_blank");
        }
      }
    </script>
  </body>
</html>
