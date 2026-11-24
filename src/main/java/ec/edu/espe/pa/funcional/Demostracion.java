package ec.edu.espe.pa.funcional;

import ec.edu.espe.pa.funcional.modelo.Incidencia;
import java.util.function.*;

// Imports nuevos para la Fase 3:
import ec.edu.espe.pa.funcional.contrato.ReglaIncidencia;
import ec.edu.espe.pa.funcional.reglas.PrioridadAlta;

public class Demostracion {
    public static void main(String[] args) {

        // ===== Fase 2: las cuatro interfaces estándar =====
        Predicate<Incidencia> estaAbierta  = i -> "ABIERTA".equals(i.estado());
        Function<Incidencia, String> obtenerResumen = i -> i.id() + " - " + i.titulo();
        Consumer<Incidencia> mostrar       = i -> System.out.println(i.titulo());
        Supplier<Incidencia> crearEjemplo  = () -> new Incidencia("INC-001", "Error de acceso", "ABIERTA", 3);

        // Se ejecutan invocando el método abstracto de cada una
        Incidencia ejemplo = crearEjemplo.get();      // Supplier  -> get()
        boolean abierta    = estaAbierta.test(ejemplo); // Predicate -> test()
        String resumen     = obtenerResumen.apply(ejemplo); // Function  -> apply()
        mostrar.accept(ejemplo);                       // Consumer  -> accept()

        System.out.println("abierta = " + abierta);
        System.out.println("resumen = " + resumen);

        // ===== Fase 3: mismo contrato, tres implementaciones =====
        System.out.println("\n--- Fase 3 ---");

        // 2) Clase anónima
        ReglaIncidencia abiertaAnonima = new ReglaIncidencia() {
            @Override
            public boolean evaluar(Incidencia incidencia) {
                return "ABIERTA".equals(incidencia.estado());
            }
        };

        // 3) Expresión lambda
        ReglaIncidencia abiertaLambda = incidencia -> "ABIERTA".equals(incidencia.estado());

        // Combinación con el método default y adaptación a Predicate mediante referencia a método
        ReglaIncidencia requiereAtencion = abiertaLambda.y(new PrioridadAlta());
        Predicate<Incidencia> comoPredicado = requiereAtencion::evaluar;

        // Imprimimos los resultados para comprobar que las 3 formas dan lo mismo
        System.out.println("anonima  = " + abiertaAnonima.evaluar(ejemplo));
        System.out.println("lambda   = " + abiertaLambda.evaluar(ejemplo));
        System.out.println("requiere = " + requiereAtencion.evaluar(ejemplo));
        System.out.println("predicate= " + comoPredicado.test(ejemplo));
    }
}