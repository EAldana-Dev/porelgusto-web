// JavaScript propio de Por el Gusto (sin librerías: solo Bootstrap).
document.addEventListener("DOMContentLoaded", () => {
    const base = document.body.dataset.base || "";

    // ---------- Sugerencias del buscador ----------
    document.querySelectorAll("[data-buscador]").forEach((form) => {
        const input = form.querySelector("input[name=q]");
        const caja = document.createElement("div");
        caja.className = "sugerencias d-none";
        form.appendChild(caja);
        let temporizador;
        let activa = -1;

        const cerrar = () => { caja.classList.add("d-none"); activa = -1; };

        input.addEventListener("input", () => {
            clearTimeout(temporizador);
            const q = input.value.trim();
            if (q.length < 2) return cerrar();
            temporizador = setTimeout(async () => {
                const res = await fetch(`${base}/api/productos?q=${encodeURIComponent(q)}`);
                const datos = await res.json();
                caja.innerHTML = "";
                if (!datos.length) {
                    caja.innerHTML = `<div class="p-3 small texto-gris">Sin resultados para «${q.replace(/</g, "&lt;")}»</div>`;
                }
                datos.forEach((p) => {
                    const a = document.createElement("a");
                    a.href = `${base}/producto/${p.sku}`;
                    a.innerHTML = `<img src="${base}${p.imagen}" alt=""><span class="flex-grow-1"><span class="d-block fw-semibold"></span>
                        <span class="small texto-gris"></span></span><span class="fw-bold"></span>`;
                    a.querySelector(".fw-semibold").textContent = p.nombre;
                    a.querySelector(".small").textContent = p.categoria + (p.disponible ? "" : " · No disponible");
                    a.querySelector(".fw-bold").textContent = p.precio;
                    caja.appendChild(a);
                });
                caja.classList.remove("d-none");
            }, 220);
        });

        input.addEventListener("keydown", (e) => {
            const ligas = [...caja.querySelectorAll("a")];
            if (!ligas.length || caja.classList.contains("d-none")) return;
            if (e.key === "ArrowDown" || e.key === "ArrowUp") {
                e.preventDefault();
                activa = (activa + (e.key === "ArrowDown" ? 1 : -1) + ligas.length) % ligas.length;
                ligas.forEach((l, i) => l.classList.toggle("activa", i === activa));
            } else if (e.key === "Enter" && activa >= 0) {
                e.preventDefault();
                location.href = ligas[activa].href;
            } else if (e.key === "Escape") {
                cerrar();
            }
        });
        document.addEventListener("click", (e) => { if (!form.contains(e.target)) cerrar(); });
    });

    // ---------- Confirmar acciones peligrosas ----------
    document.querySelectorAll("form[data-confirmar]").forEach((f) => {
        f.addEventListener("submit", (e) => {
            if (!confirm(f.dataset.confirmar)) e.preventDefault();
        });
    });

    // ---------- Selectores de cantidad (− / +) ----------
    document.querySelectorAll("[data-cantidad]").forEach((grupo) => {
        const input = grupo.querySelector("input");
        const min = Number(input.min || 1);
        const max = Number(input.max || 10);
        grupo.querySelectorAll("button[data-paso]").forEach((b) => {
            b.addEventListener("click", () => {
                const v = Math.min(max, Math.max(min, Number(input.value) + Number(b.dataset.paso)));
                input.value = v;
                if (grupo.dataset.cantidad === "auto") grupo.closest("form").requestSubmit();
            });
        });
    });

    // ---------- Precio con personalización (ficha de producto) ----------
    const total = document.querySelector("[data-precio-total]");
    if (total) {
        const precioBase = Number(total.dataset.precioBase);
        const cantidad = document.querySelector("input[name=cantidad]");
        const formato = new Intl.NumberFormat("es-MX", { style: "currency", currency: "MXN", maximumFractionDigits: 0 });
        const recalcular = () => {
            let extra = 0;
            document.querySelectorAll("input[name=opciones]:checked").forEach((o) => { extra += Number(o.dataset.costo); });
            total.textContent = formato.format((precioBase + extra) * Number(cantidad.value || 1));
        };
        document.querySelectorAll("input[name=opciones], input[name=cantidad]").forEach((i) => i.addEventListener("change", recalcular));
        document.querySelectorAll("[data-cantidad] button").forEach((b) => b.addEventListener("click", recalcular));
        recalcular();
    }

    // ---------- Horarios según la fecha de entrega (hoy solo quedan algunos) ----------
    document.querySelectorAll("[data-fecha-entrega]").forEach((sel) => {
        const horario = document.querySelector(sel.dataset.fechaEntrega);
        const hoy = sel.dataset.hoy;
        const deHoy = (horario.dataset.horariosHoy || "").split("|");
        const actualizar = () => {
            [...horario.options].forEach((o) => {
                if (!o.value) return;
                o.disabled = sel.value === hoy && !deHoy.includes(o.value);
                if (o.disabled && o.selected) horario.value = "";
            });
        };
        sel.addEventListener("change", actualizar);
        actualizar();
    });

    // ---------- Direcciones guardadas en el pago ----------
    document.querySelectorAll("[data-direccion]").forEach((b) => {
        b.addEventListener("click", () => {
            const d = JSON.parse(b.dataset.direccion);
            Object.entries(d).forEach(([campo, valor]) => {
                const input = document.querySelector(`[name=${campo}]`);
                if (input) input.value = valor || "";
            });
            document.querySelectorAll("[data-direccion]").forEach((x) => x.classList.remove("border-primary"));
            b.classList.add("border-primary");
        });
    });

    // ---------- Tarjeta: formato y vista previa ----------
    const numero = document.querySelector("#numero");
    if (numero) {
        const vista = document.querySelector("[data-vista-numero]");
        const vistaTitular = document.querySelector("[data-vista-titular]");
        const vence = document.querySelector("#vence");
        numero.addEventListener("input", () => {
            numero.value = numero.value.replace(/\D/g, "").slice(0, 19).replace(/(.{4})/g, "$1 ").trim();
            vista.textContent = numero.value || "•••• •••• •••• ••••";
        });
        vence.addEventListener("input", () => {
            let v = vence.value.replace(/\D/g, "").slice(0, 4);
            if (v.length > 2) v = v.slice(0, 2) + "/" + v.slice(2);
            vence.value = v;
        });
        document.querySelector("#titular").addEventListener("input", (e) => {
            vistaTitular.textContent = e.target.value.toUpperCase() || "NOMBRE DEL TITULAR";
        });
    }

    // ---------- Rastreo en vivo del repartidor ----------
    const mapa = document.querySelector("[data-rastreo]");
    if (mapa) {
        const actualizar = async () => {
            const res = await fetch(`${base}/api/rastreo/${mapa.dataset.rastreo}`);
            if (!res.ok) return;
            const d = await res.json();
            document.querySelector("[data-rastreo-estado]").textContent = d.estado;
            document.querySelector("[data-rastreo-actualizado]").textContent = d.actualizado ? "Ubicación actualizada " + d.actualizado : "Aún sin ubicación del repartidor";
            if (d.latitud != null) {
                const lat = Number(d.latitud), lon = Number(d.longitud), m = 0.006;
                mapa.src = `https://www.openstreetmap.org/export/embed.html?bbox=${lon - m},${lat - m},${lon + m},${lat + m}&layer=mapnik&marker=${lat},${lon}`;
                mapa.classList.remove("d-none");
            }
        };
        actualizar();
        setInterval(actualizar, 30000);
    }

    // ---------- Enviar mi ubicación (empleado repartidor) ----------
    document.querySelectorAll("[data-enviar-ubicacion]").forEach((b) => {
        b.addEventListener("click", () => {
            const estado = document.querySelector("[data-ubicacion-estado]");
            if (!navigator.geolocation) { estado.textContent = "Tu navegador no permite obtener la ubicación."; return; }
            estado.textContent = "Obteniendo ubicación…";
            navigator.geolocation.getCurrentPosition(async (pos) => {
                const res = await fetch(`${base}/api/telemetria`, {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({ tracking: b.dataset.enviarUbicacion, latitud: pos.coords.latitude, longitud: pos.coords.longitude }),
                });
                const d = await res.json();
                estado.textContent = d.ok ? "Ubicación enviada. El cliente ya la ve en su seguimiento." : d.error;
            }, () => { estado.textContent = "No se pudo obtener la ubicación (revisa los permisos)."; });
        });
    });

    // ---------- Vista previa del QR en el generador ----------
    const qrTexto = document.querySelector("#qrTexto");
    if (qrTexto) {
        document.querySelectorAll("[data-qr-rapido]").forEach((s) => s.addEventListener("change", () => {
            if (s.value) qrTexto.value = location.origin + base + s.value;
        }));
    }

    // ---------- Mostrar tooltips de Bootstrap ----------
    document.querySelectorAll('[data-bs-toggle="tooltip"]').forEach((el) => new bootstrap.Tooltip(el));
});

// ---------- Costo de envío según la dirección (página de pago) ----------
document.addEventListener("DOMContentLoaded", () => {
    const form = document.querySelector("[data-cotizar-envio]");
    if (!form) return;
    const campos = ["calle", "colonia", "ciudad", "codigoPostal"].map((n) => form.querySelector(`[name=${n}]`)).filter(Boolean);
    let temporizador;
    const cotizar = () => {
        clearTimeout(temporizador);
        temporizador = setTimeout(async () => {
            const cp = form.querySelector("[name=codigoPostal]").value.trim();
            if (!/^\d{5}$/.test(cp)) return;
            const params = new URLSearchParams();
            campos.forEach((c) => params.append(c.name, c.value));
            document.querySelectorAll("[data-envio-costo]").forEach((e) => { e.textContent = "Calculando…"; });
            try {
                const res = await fetch(`${form.dataset.cotizarEnvio}?${params}`);
                const d = await res.json();
                document.querySelectorAll("[data-envio-costo]").forEach((e) => { e.textContent = d.envio; });
                document.querySelectorAll("[data-envio-detalle]").forEach((e) => { e.textContent = d.detalle || ""; });
                document.querySelectorAll("[data-envio-iva]").forEach((e) => { e.textContent = d.iva; });
                document.querySelectorAll("[data-envio-total]").forEach((e) => { e.textContent = d.total; });
            } catch (e) { /* se calcula de nuevo al pagar */ }
        }, 500);
    };
    campos.forEach((c) => c.addEventListener("change", cotizar));
    // Al elegir una dirección guardada también se recalcula.
    document.querySelectorAll("[data-direccion]").forEach((b) => b.addEventListener("click", () => setTimeout(cotizar, 50)));
});
