const btn_menu = document.querySelector(".btn_menu");
const opciones = document.querySelector(".opciones");

if (btn_menu && opciones) {
    btn_menu.addEventListener("click", function () {
        const abierto = opciones.classList.toggle("activo");

        btn_menu.setAttribute("aria-expanded", abierto);
        btn_menu.setAttribute(
            "aria-label",
            abierto ? "Cerrar menú de navegación" : "Abrir menú de navegación"
        );
    });

    opciones.addEventListener("click", function (event) {
        if (event.target.closest("a")) {
            opciones.classList.remove("activo");
            btn_menu.setAttribute("aria-expanded", "false");
            btn_menu.setAttribute("aria-label", "Abrir menú de navegación");
        }
    });

}
