<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="pg" uri="https://porelgusto.mx/funciones" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%-- Tarjeta con QR del producto (diseño del equipo): foto de fondo, nombre y QR; se puede ver o descargar en PDF. --%>
<t:pagina titulo="Tarjeta QR · ${producto.nombre}" simple="true">
<div class="container py-5 text-center" style="max-width: 720px">
    <div class="d-flex justify-content-between align-items-center mb-4 no-imprimir">
        <a href="${ctx}/producto/${producto.sku}" class="btn btn-link px-0"><i class="bi bi-arrow-left me-1"></i>Volver al producto</a>
        <div class="d-flex gap-2">
            <button type="button" class="btn btn-outline-ciruela" onclick="manejarPdf(false)"><i class="bi bi-eye me-1"></i>Ver PDF</button>
            <button type="button" class="btn btn-ciruela" onclick="manejarPdf(true)"><i class="bi bi-download me-1"></i>Descargar PDF</button>
        </div>
    </div>

    <div id="cardParaPdf" class="qr-card d-inline-block rounded-4 p-4 shadow-sm" style="background-image: url('${ctx}${fondo}'); background-size: cover; background-position: center">
        <div class="bg-white bg-opacity-75 rounded-4 px-4 py-3">
            <div class="d-flex flex-column lh-1 mb-2">
                <span class="logo-titulo">POR EL GUSTO</span>
                <span class="logo-subtitulo">ENVÍO DE FLORES</span>
            </div>
            <h1 class="h3 mb-3"><c:out value="${producto.nombre}"/></h1>
            <img src="data:image/png;base64,${qrBase64}" alt="QR de <c:out value='${producto.nombre}'/>" width="250" height="250">
            <p class="small texto-gris mt-2 mb-0">${producto.sku} · ${pg:dinero(producto.costo)}</p>
        </div>
    </div>
    <p class="small texto-gris mt-3 no-imprimir">Al escanear el código se abre: <c:out value="${contenidoQr}"/></p>
</div>

<script src="https://cdn.jsdelivr.net/npm/html2canvas@1.4.1/dist/html2canvas.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/jspdf@2.5.1/dist/jspdf.umd.min.js"></script>
<script>
  async function construirDocPdf() {
    const card = document.getElementById("cardParaPdf");
    const { jsPDF } = window.jspdf;
    const canvas = await html2canvas(card, { scale: 2, useCORS: true, backgroundColor: "#ffffff" });
    const imgData = canvas.toDataURL("image/png");
    const pxToMm = 0.264583;
    const anchoMm = (canvas.width / 2) * pxToMm; // dividimos entre 2 por scale: 2
    const altoMm = (canvas.height / 2) * pxToMm;
    const pdf = new jsPDF({ orientation: anchoMm > altoMm ? "landscape" : "portrait", unit: "mm", format: [anchoMm, altoMm] });
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
</t:pagina>
