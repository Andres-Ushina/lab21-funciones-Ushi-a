package ec.edu.espe.pa.funcional;

import ec.edu.espe.pa.funcional.funcional.ProcesadorFuncional;
import ec.edu.espe.pa.funcional.imperativo.ProcesadorImperativo;
import ec.edu.espe.pa.funcional.modelo.Incidencia;
import ec.edu.espe.pa.funcional.reglas.ReglasIncidencia;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EquivalenciaTest {

    private final ProcesadorImperativo imperativo = new ProcesadorImperativo();
    private final ProcesadorFuncional funcional = new ProcesadorFuncional();

    @Test
    void imperativoYFuncionalSonEquivalentesEnCasoNormal() {
        List<Incidencia> datos = datosBase();

        List<String> esperado = imperativo.titulosQueRequierenAtencion(datos);
        List<String> actual = funcional.titulosQueRequierenAtencion(
                datos,
                ReglasIncidencia.REQUIERE_ATENCION,
                Incidencia::titulo
        );

        assertEquals(esperado, actual);
    }

    @Test
    void equivalenciaEnCasosLimite() {
        List<Incidencia> datos = List.of(
                new Incidencia("I3", "P3", "ABIERTA", 3),
                new Incidencia("I4", "P4", "ABIERTA", 4),
                new Incidencia("I5", "P5", "ABIERTA", 5),
                new Incidencia("I6", "P4 cerrada", "CERRADA", 4)
        );

        assertEquals(
                imperativo.titulosQueRequierenAtencion(datos),
                funcional.titulosQueRequierenAtencion(datos, ReglasIncidencia.REQUIERE_ATENCION, Incidencia::titulo)
        );
    }

    @Test
    void equivalenciaConListaVacia() {
        List<Incidencia> datos = List.of();
        assertEquals(
                imperativo.titulosQueRequierenAtencion(datos),
                funcional.titulosQueRequierenAtencion(datos, ReglasIncidencia.REQUIERE_ATENCION, Incidencia::titulo)
        );
    }

    @Test
    void noModificaDatosOriginales() {
        List<Incidencia> datos = new ArrayList<>(datosBase());
        List<Incidencia> copia = List.copyOf(datos);

        funcional.procesar(
                datos,
                ReglasIncidencia.REQUIERE_ATENCION,
                ReglasIncidencia.RESUMEN,
                incidencia -> { }
        );

        assertEquals(copia, datos);
    }

    @Test
    void procesarPermiteCambiarReglaSinCambiarMetodo() {
        List<Incidencia> datos = datosBase();
        List<String> criticas = funcional.titulosQueRequierenAtencion(
                datos,
                ReglasIncidencia.ESTA_ABIERTA.and(ReglasIncidencia::esCritica),
                Incidencia::titulo
        );

        assertEquals(List.of("Error de acceso"), criticas);
    }

    private List<Incidencia> datosBase() {
        return List.of(
                new Incidencia("INC-001", "Error de acceso", "ABIERTA", 5),
                new Incidencia("INC-002", "Pantalla azul", "ABIERTA", 4),
                new Incidencia("INC-003", "Correo lento", "EN_PROCESO", 4),
                new Incidencia("INC-004", "Solicitud de cambio", "ABIERTA", 3),
                new Incidencia("INC-005", "Impresora", "CERRADA", 5)
        );
    }
}
