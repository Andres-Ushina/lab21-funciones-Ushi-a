package ec.edu.espe.pa.funcional;

import ec.edu.espe.pa.funcional.modelo.Incidencia;
import org.junit.jupiter.api.Test;

import static ec.edu.espe.pa.funcional.reglas.ReglasIncidencia.*;
import static org.junit.jupiter.api.Assertions.*;

class ReglasIncidenciaTest {

    private Incidencia inc(int prioridad) {
        return new Incidencia("INC-T", "test", "ABIERTA", prioridad);
    }

    @Test
    void estaAbiertaEsVerdaderoSoloConEstadoAbierta() {
        assertTrue(ESTA_ABIERTA.test(new Incidencia("1", "t", "ABIERTA", 3)));
        assertFalse(ESTA_ABIERTA.test(new Incidencia("1", "t", "CERRADA", 3)));
        assertFalse(ESTA_ABIERTA.test(new Incidencia("1", "t", "EN_PROCESO", 3)));
    }

    @Test
    void prioridadAltaRespetaElUmbralEnLosLimites() {
        assertFalse(PRIORIDAD_ALTA.test(inc(3)), "3 está por debajo del umbral");
        assertTrue (PRIORIDAD_ALTA.test(inc(4)), "4 es el umbral exacto");
        assertTrue (PRIORIDAD_ALTA.test(inc(5)), "5 es el máximo");
    }

    @Test
    void prioridadInvalidaEsRechazadaAlConstruir() {
        assertThrows(IllegalArgumentException.class,
                () -> new Incidencia("1", "t", "ABIERTA", 0));
        assertThrows(IllegalArgumentException.class,
                () -> new Incidencia("1", "t", "ABIERTA", 6));
    }

    @Test
    void requiereAtencionEsAbiertaYAlta() {
        assertTrue (REQUIERE_ATENCION.test(new Incidencia("1", "t", "ABIERTA", 4)));
        assertFalse(REQUIERE_ATENCION.test(new Incidencia("1", "t", "ABIERTA", 3)));
        assertFalse(REQUIERE_ATENCION.test(new Incidencia("1", "t", "CERRADA", 5)));
        assertFalse(REQUIERE_ATENCION.test(new Incidencia("1", "t", "EN_PROCESO", 5)));
    }

    @Test
    void categoriaCubreTodasLasPrioridades() {
        assertEquals("CRITICA", CATEGORIA.apply(inc(5)));
        assertEquals("ALTA",    CATEGORIA.apply(inc(4)));
        assertEquals("MEDIA",   CATEGORIA.apply(inc(3)));
        assertEquals("MEDIA",   CATEGORIA.apply(inc(2)));
        assertEquals("BAJA",    CATEGORIA.apply(inc(1)));
    }

    @Test
    void resumenUneIdYTitulo() {
        Incidencia i = new Incidencia("INC-9", "Falla", "ABIERTA", 3);
        assertEquals("INC-9 - Falla", RESUMEN.apply(i));
    }

    @Test
    void esCriticaSoloEnPrioridadCinco() {
        assertTrue (esCritica(inc(5)));
        assertFalse(esCritica(inc(4)));
        assertFalse(esCritica(inc(1)));
    }

    @Test
    void generadorSinteticoProduceDatosValidos() {
        var sup = generadorSintetico();
        Incidencia a = sup.get();
        Incidencia b = sup.get();
        assertNotEquals(a.id(), b.id());
        assertTrue(a.prioridad() >= 1 && a.prioridad() <= 5);
    }
}