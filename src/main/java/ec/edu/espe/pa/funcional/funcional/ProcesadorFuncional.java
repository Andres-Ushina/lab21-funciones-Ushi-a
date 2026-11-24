package ec.edu.espe.pa.funcional.funcional;

import ec.edu.espe.pa.funcional.modelo.Incidencia;
import java.util.ArrayList;
import java.util.List;
import java.util.function.*;
import static ec.edu.espe.pa.funcional.reglas.ReglasIncidencia.REQUIERE_ATENCION;

public class ProcesadorFuncional {

    /**
     * Ciclo genérico: para cada incidencia,
     *   1) la selecciona (Predicate),
     *   2) la transforma (Function),
     *   3) actúa sobre el resultado (Consumer).
     */
    public <R> List<R> procesar(
            List<Incidencia> incidencias,
            Predicate<Incidencia> seleccion,
            Function<Incidencia, R> transformacion,
            Consumer<R> accion) {

        List<R> resultado = new ArrayList<>();
        for (Incidencia incidencia : incidencias) {
            if (seleccion.test(incidencia)) {
                R valor = transformacion.apply(incidencia);
                accion.accept(valor);
                resultado.add(valor);
            }
        }
        return resultado;
    }

    // Equivalente a ProcesadorImperativo.titulosQueRequierenAtencion
    public List<String> titulosQueRequierenAtencion(List<Incidencia> incidencias) {
        return titulosQueRequierenAtencion(incidencias, REQUIERE_ATENCION, Incidencia::titulo);
    }

    public <R> List<R> titulosQueRequierenAtencion(
            List<Incidencia> incidencias,
            Predicate<Incidencia> seleccion,
            Function<Incidencia, R> transformacion) {
        return procesar(incidencias, seleccion, transformacion, valor -> { });
    }

    // Equivalente a ProcesadorImperativo.mostrarQueRequierenAtencion
    public void mostrarQueRequierenAtencion(List<Incidencia> incidencias) {
        procesar(incidencias, REQUIERE_ATENCION, Incidencia::titulo, System.out::println);
    }
}



