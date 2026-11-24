package ec.edu.espe.pa.funcional;

import ec.edu.espe.pa.funcional.contrato.ReglaIncidencia;
import ec.edu.espe.pa.funcional.modelo.Incidencia;
import ec.edu.espe.pa.funcional.reglas.PrioridadAlta;
import ec.edu.espe.pa.funcional.reglas.ReglasIncidencia;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ReglasIncidenciaTest {

    @Test
    void prioridadAltaCumpleLimites() {
        assertFalse(ReglasIncidencia.PRIORIDAD_ALTA.test(new Incidencia("I1", "A", "ABIERTA", 3)));
        assertTrue(ReglasIncidencia.PRIORIDAD_ALTA.test(new Incidencia("I2", "A", "ABIERTA", 4)));
        assertTrue(ReglasIncidencia.PRIORIDAD_ALTA.test(new Incidencia("I3", "A", "ABIERTA", 5)));
    }

    @Test
    void recordRechazaPrioridadFueraDeRango() {
        assertThrows(IllegalArgumentException.class, () -> new Incidencia("I0", "A", "ABIERTA", 0));
        assertThrows(IllegalArgumentException.class, () -> new Incidencia("I6", "A", "ABIERTA", 6));
    }

    @Test
    void estadoAbiertaEsSeguroAnteTexto() {
        assertTrue(ReglasIncidencia.ESTA_ABIERTA.test(new Incidencia("I1", "A", "ABIERTA", 2)));
        assertFalse(ReglasIncidencia.ESTA_ABIERTA.test(new Incidencia("I2", "A", "EN_PROCESO", 2)));
        assertFalse(ReglasIncidencia.ESTA_ABIERTA.test(new Incidencia("I3", "A", "CERRADA", 2)));
    }

    @Test
    void requiereAtencionCombinaReglas() {
        assertTrue(ReglasIncidencia.REQUIERE_ATENCION.test(new Incidencia("I1", "A", "ABIERTA", 4)));
        assertFalse(ReglasIncidencia.REQUIERE_ATENCION.test(new Incidencia("I2", "A", "ABIERTA", 3)));
        assertFalse(ReglasIncidencia.REQUIERE_ATENCION.test(new Incidencia("I3", "A", "CERRADA", 5)));
    }

    @Test
    void categoriaRespetaReglasDeDominio() {
        assertEquals("BAJA", ReglasIncidencia.CATEGORIA.apply(new Incidencia("I1", "A", "ABIERTA", 1)));
        assertEquals("MEDIA", ReglasIncidencia.CATEGORIA.apply(new Incidencia("I2", "A", "ABIERTA", 2)));
        assertEquals("MEDIA", ReglasIncidencia.CATEGORIA.apply(new Incidencia("I3", "A", "ABIERTA", 3)));
        assertEquals("ALTA", ReglasIncidencia.CATEGORIA.apply(new Incidencia("I4", "A", "ABIERTA", 4)));
        assertEquals("CRITICA", ReglasIncidencia.CATEGORIA.apply(new Incidencia("I5", "A", "ABIERTA", 5)));
    }

    @Test
    void contratoPropioTieneTresImplementacionesEquivalentes() {
        Incidencia incidencia = new Incidencia("I1", "A", "ABIERTA", 4);
        ReglaIncidencia clase = new PrioridadAlta();
        ReglaIncidencia anonima = new ReglaIncidencia() {
            @Override
            public boolean evaluar(Incidencia i) {
                return "ABIERTA".equals(i.estado());
            }
        };
        ReglaIncidencia lambda = i -> "ABIERTA".equals(i.estado());

        assertTrue(clase.evaluar(incidencia));
        assertEquals(anonima.evaluar(incidencia), lambda.evaluar(incidencia));
        assertTrue(lambda.y(clase).evaluar(incidencia));
    }

    @Test
    void generadorSinteticoEntregaDatosDeterministicos() {
        Supplier<Incidencia> generador = ReglasIncidencia.generadorSintetico();
        List<Incidencia> datos = Stream.generate(generador).limit(3).toList();

        assertEquals(List.of("INC-001", "INC-002", "INC-003"), datos.stream().map(Incidencia::id).toList());
        assertEquals(List.of("EN_PROCESO", "CERRADA", "ABIERTA"), datos.stream().map(Incidencia::estado).toList());
    }

    @Test
    void notificarNoModificaDatos() {
        Incidencia incidencia = new Incidencia("I1", "A", "ABIERTA", 5);
        List<String> ids = new ArrayList<>();
        ReglasIncidencia.NOTIFICAR.andThen(i -> ids.add(i.id())).accept(incidencia);
        assertEquals("I1", incidencia.id());
        assertEquals(List.of("I1"), ids);
    }
}
