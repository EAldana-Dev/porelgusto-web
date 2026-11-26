package com.neksbyte.porelgustoweb;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/** Arranca la aplicación completa con H2 (no necesita MySQL instalado). */
@SpringBootTest
@ActiveProfiles("h2")
class PorElGustoWebApplicationTests {

    @Test
    void contextLoads() {
    }
}
