package ec.edu.espe.pa.funcional;

import ec.edu.espe.pa.funcional.modelo.Incidencia;
import java.util.function.*;

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
        String resumen     = obtenerResumen.apply(ejemplo); // Function -> apply()
        mostrar.accept(ejemplo);                       // Consumer  -> accept()

        System.out.println("abierta = " + abierta);
        System.out.println("resumen = " + resumen);
    }
}