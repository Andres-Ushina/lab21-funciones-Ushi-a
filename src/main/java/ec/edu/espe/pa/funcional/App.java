package ec.edu.espe.pa.funcional;

import ec.edu.espe.pa.funcional.modelo.Incidencia;
import ec.edu.espe.pa.funcional.imperativo.ProcesadorImperativo;
import java.util.List;

public class App {
    public static void main(String[] args) {
        List<Incidencia> datos = List.of(
                new Incidencia("INC-001", "Servidor caído", "ABIERTA", 5),
                new Incidencia("INC-002", "Impresora sin tóner", "ABIERTA", 2),
                new Incidencia("INC-003", "VPN lenta", "EN_PROCESO", 4),
                new Incidencia("INC-004", "Disco lleno", "ABIERTA", 4)
        );
        new ProcesadorImperativo().mostrarQueRequierenAtencion(datos);
    }
}