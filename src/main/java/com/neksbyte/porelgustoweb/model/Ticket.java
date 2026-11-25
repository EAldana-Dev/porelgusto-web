package com.neksbyte.porelgustoweb.model;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "Ticket")
public class Ticket {
    @Id
    @Column(name = "idTicket")
    private Integer idTicket;

    @Column(name = "folioVenta", length = 100)
    private String folioVenta;

    @Column(name = "fecha")
    private LocalDate fecha;

    @Column(name = "hora")
    private LocalTime hora;

    @Column(name = "total", precision = 10, scale = 2)
    private BigDecimal total;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idPedido", unique = true)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Pedido pedido;

    public Ticket() {
    }

    public Integer getIdTicket() {
        return idTicket;
    }

    public void setIdTicket(Integer idTicket) {
        this.idTicket = idTicket;
    }

    public String getFolioVenta() {
        return folioVenta;
    }

    public void setFolioVenta(String folioVenta) {
        this.folioVenta = folioVenta;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

}
