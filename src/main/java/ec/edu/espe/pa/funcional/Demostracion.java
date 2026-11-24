package ec.edu.espe.pa.funcional;

import ec.edu.espe.pa.funcional.modelo.Incidencia;
import java.util.function.*;

// Imports nuevos para la Fase 3:
import ec.edu.espe.pa.funcional.contrato.ReglaIncidencia;
import ec.edu.espe.pa.funcional.reglas.PrioridadAlta;
import ec.edu.espe.pa.funcional.reglas.ReglasIncidencia;


import java.util.ArrayList;
import java.util.List;
import ec.edu.espe.pa.funcional.contrato.FabricaIncidencia;
import ec.edu.espe.pa.funcional.reglas.ReglasIncidencia;
import static ec.edu.espe.pa.funcional.reglas.ReglasIncidencia.*;

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


        // Puedes agregar al final del main de Demostracion:
        var sup = ReglasIncidencia.generadorSintetico();
        for (int i = 0; i < 5; i++) {
            Incidencia in = sup.get();
            System.out.println(in.id() + " | " + in.estado() + " | " +
                    in.prioridad() + " | " + ReglasIncidencia.CATEGORIA.apply(in));
        }


        System.out.println("========== Prueba correspondiente a la Fase 5 ========");
        // ===== Fase 5: composición =====
        Predicate<Incidencia> noAbierta     = ESTA_ABIERTA.negate();
        Predicate<Incidencia> pendienteBaja = ESTA_ABIERTA.and(PRIORIDAD_ALTA.negate());
        Predicate<Incidencia> revisarHoy    = REQUIERE_ATENCION.or(i -> i.prioridad() == 5);

        Function<String, String> aMayusculas         = String::toUpperCase;
        Function<Incidencia, String> resumenMayus    = RESUMEN.andThen(aMayusculas);
        Function<Incidencia, String> mismoResultado  = aMayusculas.compose(RESUMEN);
        Function<Incidencia, String> etiqueta        = CATEGORIA.andThen(c -> "[" + c + "]");

        List<String> bitacora = new ArrayList<>();
        Consumer<Incidencia> registrar = i -> bitacora.add(i.id());
        Consumer<Incidencia> notificarYRegistrar = NOTIFICAR.andThen(registrar);

// ===== Referencias a métodos (4 variantes) =====
        Predicate<Incidencia> esCritica = ReglasIncidencia::esCritica;     // método estático
        Function<Incidencia, String> titulo = Incidencia::titulo;          // instancia arbitraria
        Supplier<List<Incidencia>> nuevaLista = ArrayList::new;            // constructor sin args
        FabricaIncidencia fabrica = Incidencia::new;                       // constructor con args

// Ejecución de la composición
        Incidencia demo = new Incidencia("INC-777", "Servidor caído", "ABIERTA", 5);
        System.out.println("andThen  = " + resumenMayus.apply(demo));
        System.out.println("compose  = " + mismoResultado.apply(demo));
        System.out.println("etiqueta = " + etiqueta.apply(demo));
        notificarYRegistrar.accept(demo);
        System.out.println("bitacora = " + bitacora);

    }


}