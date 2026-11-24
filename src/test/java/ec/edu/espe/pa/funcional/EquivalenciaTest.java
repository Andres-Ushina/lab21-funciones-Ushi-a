package ec.edu.espe.pa.funcional;

import ec.edu.espe.pa.funcional.funcional.ProcesadorFuncional;
import ec.edu.espe.pa.funcional.imperativo.ProcesadorImperativo;
import ec.edu.espe.pa.funcional.modelo.Incidencia;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EquivalenciaTest {

    private final ProcesadorImperativo imperativo = new ProcesadorImperativo();
    private final ProcesadorFuncional funcional   = new ProcesadorFuncional();

    private List<Incidencia> datos() {
        return List.of(
                new Incidencia("INC-001", "A", "ABIERTA",    5),
                new Incidencia("INC-002", "B", "ABIERTA",    3),
                new Incidencia("INC-003", "C", "EN_PROCESO", 5),
                new Incidencia("INC-004", "D", "ABIERTA",    4),
                new Incidencia("INC-005", "E", "CERRADA",    5),
                new Incidencia("INC-006", "F", "ABIERTA",    1)
        );
    }

    @Test
    void casosNormalesProducenLoMismo() {
        List<Incidencia> d = datos();
        assertEquals(
                imperativo.titulosQueRequierenAtencion(d),
                funcional.titulosQueRequierenAtencion(d)
        );
    }

    @Test
    void casosLimiteProducenLoMismo() {
        List<Incidencia> borde = List.of(
                new Incidencia("X", "borde3", "ABIERTA", 3),
                new Incidencia("Y", "borde4", "ABIERTA", 4),
                new Incidencia("Z", "borde5", "ABIERTA", 5)
        );
        assertEquals(
                imperativo.titulosQueRequierenAtencion(borde),
                funcional.titulosQueRequierenAtencion(borde)
        );
    }

    @Test
    void listaVaciaProduceLoMismo() {
        assertEquals(
                imperativo.titulosQueRequierenAtencion(List.of()),
                funcional.titulosQueRequierenAtencion(List.of())
        );
    }

    @Test
    void listaSinCoincidenciasProduceLoMismo() {
        List<Incidencia> sinAtencion = List.of(
                new Incidencia("1", "a", "CERRADA",    5),
                new Incidencia("2", "b", "ABIERTA",    3),
                new Incidencia("3", "c", "EN_PROCESO", 4)
        );
        assertEquals(
                imperativo.titulosQueRequierenAtencion(sinAtencion),
                funcional.titulosQueRequierenAtencion(sinAtencion)
        );
    }

    @Test
    void datosOriginalesNoSeModifican() {
        List<Incidencia> original = datos();
        List<Incidencia> copia    = List.copyOf(original);
        funcional.titulosQueRequierenAtencion(original);
        imperativo.titulosQueRequierenAtencion(original);
        assertEquals(copia, original);
    }
}