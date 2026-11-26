package com.neksbyte.porelgustoweb.dto;

import jakarta.validation.constraints.*;

/** Datos fiscales para solicitar la factura de un pedido. */
public class FacturaForm {
    @NotBlank(message = "Escribe tu RFC")
    @Pattern(regexp = "^[A-Za-zÑñ&]{3,4}\\d{6}[A-Za-z0-9]{3}$", message = "Escribe un RFC válido (12 o 13 caracteres)")
    private String rfc;

    @NotBlank(message = "Escribe la razón social")
    @Size(max = 150)
    private String razonSocial;

    @NotBlank(message = "Escribe la dirección fiscal")
    @Size(max = 255)
    private String direccionFiscal;

    @NotBlank(message = "Elige el uso del CFDI")
    @Size(max = 100)
    private String cfdi = "G03 Gastos en general";

    public String getRfc() { return rfc; }
    public void setRfc(String rfc) { this.rfc = rfc; }
    public String getRazonSocial() { return razonSocial; }
    public void setRazonSocial(String razonSocial) { this.razonSocial = razonSocial; }
    public String getDireccionFiscal() { return direccionFiscal; }
    public void setDireccionFiscal(String direccionFiscal) { this.direccionFiscal = direccionFiscal; }
    public String getCfdi() { return cfdi; }
    public void setCfdi(String cfdi) { this.cfdi = cfdi; }
}
