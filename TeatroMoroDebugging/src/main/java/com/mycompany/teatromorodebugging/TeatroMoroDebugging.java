//Añadi breakpoints en los scanner más importants
//Tambien al crear new Ticket y al llamar los metodos de obtener informacion del ticket
//Y en las llamadas de imprimir boleta y cuando se obtienen los datos de esa boleta

package com.mycompany.teatromorodebugging;

import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class TeatroMoroDebugging {

    static Ticket[] ticketsReservados = new Ticket[100];
    static int cantidadTickets = 0;

    public static String[] asientosVIP = new String[30];
    public static String[] asientosPalco = new String[30];
    public static String[] asientosGeneral = new String[30];

    static int totalEntradasReservadas = 0;
    static int totalEntradasVendidas = 0;
    static double ingresoTotal = 0;

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        int eleccionDeMenu;

        do {
            eliminarReservasExpiradas();
            System.out.println("Entradas reservadas: " + totalEntradasReservadas + "");
            System.out.println("Entradas vendidas: " + totalEntradasVendidas + "");
            System.out.println("Ingresos totales: " + ingresoTotal + "");

            mostrarTiempoRestante();
            mostrarMenuPrincipal();

            while (!scanner.hasNextInt()) {
                System.out.println("Ingrese un digito válido");
                scanner.next();
            }

            eleccionDeMenu = scanner.nextInt();
            scanner.nextLine();

            if (eleccionDeMenu > 5 || eleccionDeMenu < 1) {
                System.out.println("Por favor, seleccione una opción valida");
            }

            switch (eleccionDeMenu) {
                case 1:
                    reservarEntradas(scanner);
                    break;
                case 2:
                    comprarEntradaReservada(scanner, ticketsReservados);

                    break;
                case 3:
                    modificarEntradasCompradas(scanner, ticketsReservados);

                    break;
                case 4:
                    imprimirEntradaComprada(scanner, ticketsReservados);
                    break;
                case 5:
                    System.out.println("Hasta pronto!");
                    break;
            }
        } while (eleccionDeMenu != 5);
        scanner.close();
    }

    public static void mostrarMenuPrincipal() {
        System.out.println("\n--- Bienvenido al sistema de ventas Teatro Moro ---");
        System.out.println("Por favor, elija una opción válida:");
        System.out.println("1) Reservar entradas ");
        System.out.println("2) Comprar entradas reservadas ");
        System.out.println("3) Modificar venta de entrada");
        System.out.println("4) Imprimir boleta");
        System.out.println("5) Salir");
    }

    public static void reservarEntradas(Scanner scanner) {
        boolean seguir = true;
        while (seguir) {
            System.out.println("\nDisponemos de las siguientes zonas:");
            System.out.println("1) VIP (20.000)");
            System.out.println("2) Palco (15.000)");
            System.out.println("3) Entrada General (10.000)");
            System.out.println("4) Volver al menú principal");

            int zonaElegida;

            while (!scanner.hasNextInt()) {
                System.out.println("Ingrese un dígito válido");
                scanner.next();
            }

            zonaElegida = scanner.nextInt();
            scanner.nextLine();

            switch (zonaElegida) {
                case 1:
                    procesarZona(scanner, 1, asientosVIP, "VIP", 20000);
                    break;
                case 2:
                    procesarZona(scanner, 2, asientosPalco, "Palco", 15000);
                    break;
                case 3:
                    procesarZona(scanner, 3, asientosGeneral, "Entrada General", 10000);
                    break;
                case 4:
                    volverAlMenuPrincipal();
                    return;
                default:
                    System.out.println("Seleccione una opción válida");
                    continue;
            }

            System.out.println("¿Desea reservar otra entrada? (Si/No)");
            String resp = scanner.nextLine().trim();

            if (!resp.equalsIgnoreCase("Si")) {
                seguir = false;
            }
        }
    }

    public static void mostrarAsientos(String[] asientos, String nombreZona) {
        System.out.println("\nAsientos disponibles para " + nombreZona + " :");
        char prefijo = nombreZona.charAt(0);
        for (int i = 0; i < asientos.length; i++) {

            if (asientos[i] == null) {
                System.out.printf("%c%-2d\t", prefijo, i + 1);
            } else {
                System.out.print("[X]\t");
            }
            if ((i + 1) % 10 == 0) {
                System.out.println();
            }
        }
        System.out.println();
    }

    public static void procesarZona(Scanner scanner, int zonaCodigo, String[] arregloZona, String nombreZona, int precioBase) {
    mostrarAsientos(arregloZona, nombreZona);
    String asientoElegido;
    do {
        System.out.println("Ingrese el código del asiento:");
        asientoElegido = scanner.nextLine().trim().toUpperCase();

        if (!esAsientoValido(asientoElegido, arregloZona, nombreZona)) {
            System.out.println("Asiento inválido o ya reservado. Intente nuevamente.");
        }

    } while (!esAsientoValido(asientoElegido, arregloZona, nombreZona));

    reservarAsiento(asientoElegido, arregloZona);
    boolean pagoExitoso = preguntarPagoInmediato(scanner, nombreZona, asientoElegido, precioBase);

    Ticket nuevoTicket = new Ticket(zonaCodigo, asientoElegido, precioBase);
    nuevoTicket.setTiempoReserva(System.currentTimeMillis());
    nuevoTicket.setEstado(pagoExitoso ? "Comprado" : "Reservado");

    ticketsReservados[cantidadTickets++] = nuevoTicket;

    if (pagoExitoso) {
        totalEntradasVendidas++;
        ingresoTotal += nuevoTicket.getPrecioBase();
    } else {
        totalEntradasReservadas++;
    }
}

    public static boolean esAsientoValido(String asiento, String[] arregloZona, String nombreZona) {
        if (asiento == null || asiento.length() < 2) {
            return false;
        }
        char letra = asiento.charAt(0);
        String numeroA = asiento.substring(1);

        try {
            int numero = Integer.parseInt(numeroA);

            if (numero < 1 || numero > arregloZona.length) {
                return false;
            }

            char esperado = nombreZona.charAt(0);
            if (letra != esperado) {
                return false;
            }
            return arregloZona[numero - 1] == null;

        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static void reservarAsiento(String asiento, String[] asientos) {

        int numero = Integer.parseInt(asiento.substring(1));

        asientos[numero - 1] = "Reservado";
        System.out.println("Asiento " + asiento + " reservado exitosamente");
    }

    public static boolean preguntarPagoInmediato(Scanner scanner, String zona, String asientoElegido, int precioBase) {

        String respuesta;
        int metodoDePago;

        do {
            System.out.println("¿Desea pagar de inmediato? (Si/No)");
            respuesta = scanner.nextLine().trim();

            if (respuesta.equalsIgnoreCase("Si")) {
                do {
                    System.out.println("Elija método de pago:");
                    System.out.println("1) Débito");
                    System.out.println("2) Efectivo");

                    while (!scanner.hasNextInt()) {
                        System.out.println("Ingrese una opción de pago válida");
                        scanner.next();
                    }
                    metodoDePago = scanner.nextInt();
                    scanner.nextLine();

                    switch (metodoDePago) {
                        case 1:
                            System.out.println("Ingrese su contraseña para pago Débito (solo números):");

                            while (!scanner.hasNextInt()) {
                                System.out.println("Error: La contraseña debe ser un número.");
                                scanner.next();
                            }

                            int contraseña = scanner.nextInt();
                            scanner.nextLine();
                            System.out.println("Pago con Débito procesado para zona " + zona + ", asiento " + asientoElegido.toUpperCase());

                            return true;

                        case 2:
                            int pago;
                            int cambio;

                            System.out.println("El costo del ticket: " + precioBase + " pesos");
                            System.out.println("Ingrese el monto con el que desea pagar:");

                            while (!scanner.hasNextInt()) {
                                System.out.println("Error: Debe ser un monto válido");
                                scanner.next();
                            }

                            pago = scanner.nextInt();
                            scanner.nextLine();

                            while (pago < precioBase) {
                                System.out.println("El monto ingresado es insuficiente");

                                while (!scanner.hasNextInt()) {
                                    System.out.println("Error: Debe ingresar un monto valido");
                                    scanner.next();
                                }

                                pago = scanner.nextInt();
                                scanner.nextLine();
                            }

                            cambio = pago - precioBase;
                            if (cambio > 0) {
                                System.out.println("Pago procesado exitosamente. Su cambio es: " + cambio + " pesos.");
                            } else {
                                System.out.println("Pago procesado exitosamente. No hay cambio.");
                            }

                            System.out.println("Pago en efectivo procesado para zona " + zona + ", asiento " + asientoElegido);

                            return true;

                        default:
                            System.out.println("Opción no válida. Intente nuevamente.");
                            break;
                    }

                } while (true);

            } else if (respuesta.equalsIgnoreCase("No")) {

                System.out.println("Su ticket ha sido reservado, en menú principal puede pagarlo");

                return false;
            } else {

                System.out.println("Respuesta no válida. Por favor, escriba Si o No.");
            }

        } while (true);
    }

    public static void comprarEntradaReservada(Scanner scanner, Ticket[] tickets) {
        boolean volverAlMenuPrincipal = false;

        do {
            System.out.println("Seleccione una opción:");
            System.out.println("1) Ver entradas reservadas y proceder al pago");
            System.out.println("2) Regresar al menú principal");

            int opcionMenu;

            while (!scanner.hasNextInt()) {
                System.out.println("Ingrese una opción válida (1 o 2):");
                scanner.next();
            }
            opcionMenu = scanner.nextInt();
            scanner.nextLine();

            switch (opcionMenu) {
                case 1:

                    listarEntradasReservadas(tickets);
                    boolean hayReservadas = false;

                    for (Ticket t : tickets) {
                        if (t != null && "Reservado".equals(t.getEstado())) {
                            hayReservadas = true;
                            break;
                        }
                    }

                    if (!hayReservadas) {
                        System.out.println("No hay entradas reservadas \n");
                        return;
                    }

                    System.out.println("Ingrese el código del asiento reservado:");
                    String codigoAsiento = scanner.nextLine().toUpperCase();

                    Ticket ticket = buscarTicketPorAsiento(codigoAsiento, tickets);

                    if (ticket == null) {
                        System.out.println("No se encontró el ticket reservado con ese código.");
                        return;
                    }

                    if ("Comprado".equals(ticket.getEstado())) {
                        System.out.println("El asiento ya ha sido comprado.");
                        return;
                    }

                    System.out.println("Ticket reservado encontrado. Seleccione una opción:");
                    System.out.println("1) Proceder con el pago");
                    System.out.println("2) Regresar al menú principal");

                    int opcion;

                    while (!scanner.hasNextInt()) {
                        System.out.println("Ingrese una opción válida (1 o 2):");
                        scanner.next();
                    }

                    opcion = scanner.nextInt();
                    scanner.nextLine();

                    switch (opcion) {
                        case 1:
                            System.out.println("Procediendo al pago...");
                            boolean pagoExitoso = preguntarPagoInmediato(scanner, ImpresorBoleta.convertirZona(ticket.getZona()),
                                    ticket.getAsientoElegido(), ticket.getPrecioBase());

                            if (pagoExitoso) {
                                ticket.setEstado("Comprado");
                                System.out.println("Compra exitosa. Su asiento ha sido comprado.");

                                totalEntradasReservadas--;
                                totalEntradasVendidas++;
                                ingresoTotal += ticket.getPrecioBase();
                                volverAlMenuPrincipal = true;

                            } else {
                                System.out.println("La compra no se completó.");
                            }

                            break;
                        case 2:

                            System.out.println("Regresando al menú principal...");
                            volverAlMenuPrincipal = true;
                            break;

                        default:
                            System.out.println("Opción no válida. Por favor, seleccione nuevamente.");
                            break;
                    }
                    break;

                case 2:
                    volverAlMenuPrincipal();
                    volverAlMenuPrincipal = true;
                    break;

                default:
                    System.out.println("Opción no válida. Por favor, seleccione nuevamente.");
                    break;
            }
        } while (!volverAlMenuPrincipal);
    }

    public static void modificarEntradasCompradas(Scanner scanner, Ticket[] tickets) {

        System.out.println("\n--- Entradas Compradas ---");
        List<Ticket> comprados = new ArrayList<>();

        int numero = 1;

        for (Ticket t : tickets) {
            if (t != null && "Comprado".equalsIgnoreCase(t.getEstado())) {
                System.out.printf("%d) Zona: %s  |  Asiento: %s  |  Precio: %d  \n",
                        numero,
                        ImpresorBoleta.convertirZona(t.getZona()),
                        t.getAsientoElegido(),
                        t.getPrecioBase()
                );

                comprados.add(t);
                numero++;
            }
        }

        if (comprados.isEmpty()) {
            System.out.println("No hay entradas compradas.");
            return;
        }

        System.out.println("Seleccione el número de la entrada que desea modificar:");
        int seleccion = -1;
        while (!scanner.hasNextInt() || (seleccion = scanner.nextInt()) < 1 || seleccion > comprados.size()) {
            System.out.println("Selección inválida. Intente nuevamente:");
            scanner.nextLine();
        }
        scanner.nextLine();
        Ticket elegido = comprados.get(seleccion - 1);
        modificarTicket(scanner, elegido);
    }

    public static void imprimirEntradaComprada(Scanner scanner, Ticket[] tickets) {

        System.out.println("\n--- Imprimir Boleta de Entrada Comprada ---");
        List<Ticket> comprados = new ArrayList<>();

        int numero = 1;

        for (Ticket t : tickets) {
            if (t != null && "Comprado".equalsIgnoreCase(t.getEstado())) {
                System.out.printf("%d) Zona: %s  |  Asiento: %s  |  Precio: %d  \n",
                        numero,
                        ImpresorBoleta.convertirZona(t.getZona()),
                        t.getAsientoElegido(),
                        t.getPrecioBase()
                );

                comprados.add(t);
                numero++;
            }
        }

        if (comprados.isEmpty()) {
            System.out.println("No hay entradas compradas para imprimir.");
            return;
        }

        System.out.println("Seleccione el número de la entrada para imprimir su boleta:");

        int seleccion = -1;

        while (!scanner.hasNextInt() || (seleccion = scanner.nextInt()) < 1 || seleccion > comprados.size()) {
            System.out.println("Selección inválida. Intente nuevamente:");
            scanner.nextLine();
        }

        scanner.nextLine();

        Ticket elegido = comprados.get(seleccion - 1);
        ImpresorBoleta.imprimirBoleta(elegido);
    }

    public static void modificarTicket(Scanner scanner, Ticket ticket) {

        int eleccionMenuModificacion;
        do {
            System.out.println("¿Qué desea modificar?");
            System.out.println("1) Zona");
            System.out.println("2) Asiento");
            System.out.println("3) Volver al menú principal");

            while (!scanner.hasNextInt()) {
                System.out.println("Ingrese una opción válida");
                scanner.next();
            }

            eleccionMenuModificacion = scanner.nextInt();
            scanner.nextLine();

            switch (eleccionMenuModificacion) {

                case 1:
                    String zonaActual = ticket.getZona();
                    String[] arregloZonaActual = getZonaArreglo(zonaActual);

                    if (arregloZonaActual != null) {

                        liberarAsiento(ticket, arregloZonaActual);

                        System.out.println("Seleccione la nueva zona:");
                        System.out.println("1) VIP");
                        System.out.println("2) Palco");
                        System.out.println("3) Entrada General");

                        int nuevaZona = scanner.nextInt();
                        scanner.nextLine();

                        String[] arregloZonaNueva = getZonaArreglo(String.valueOf(nuevaZona));

                        if (arregloZonaNueva != null) {
                            ticket.setZona(String.valueOf(nuevaZona));
                            mostrarAsientos(arregloZonaNueva, convertirNombreZona(nuevaZona));

                            String nuevoAsiento;

                            do {
                                System.out.println("Ingrese el nuevo asiento en la nueva zona:");
                                nuevoAsiento = scanner.nextLine().trim().toUpperCase();
                                if (!esAsientoValido(nuevoAsiento, arregloZonaNueva, convertirNombreZona(nuevaZona))) {
                                    System.out.println("Asiento inválido o ya reservado. Intente nuevamente.");
                                }
                            } while (!esAsientoValido(nuevoAsiento, arregloZonaNueva, convertirNombreZona(nuevaZona)));

                            reservarAsiento(nuevoAsiento, arregloZonaNueva);
                            ticket.setAsientoElegido(nuevoAsiento);
                            System.out.println("Zona y asiento modificados correctamente.");
                        }
                    }
                    break;
                case 2:

                    String zonaAsientoActual = ticket.getZona();
                    String[] arregloZonaAsiento = getZonaArreglo(zonaAsientoActual);

                    if (arregloZonaAsiento != null) {

                        mostrarAsientos(arregloZonaAsiento, convertirNombreZona(Integer.parseInt(zonaAsientoActual)));

                        String nuevoAsiento;

                        do {
                            System.out.println("Ingrese el nuevo asiento:");
                            nuevoAsiento = scanner.nextLine().trim().toUpperCase();
                            if (!esAsientoValido(nuevoAsiento, arregloZonaAsiento, convertirNombreZona(Integer.parseInt(zonaAsientoActual)))) {
                                System.out.println("Asiento inválido o ya reservado. Intente nuevamente.");
                            }
                        } while (!esAsientoValido(nuevoAsiento, arregloZonaAsiento, convertirNombreZona(Integer.parseInt(zonaAsientoActual))));

                        liberarAsiento(ticket, arregloZonaAsiento);
                        reservarAsiento(nuevoAsiento, arregloZonaAsiento);

                        ticket.setAsientoElegido(nuevoAsiento);
                        System.out.println("Asiento modificado correctamente.");
                    }
                    break;
                case 3:
                    volverAlMenuPrincipal();
                    return;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
            }
        } while (true);
    }

    public static String[] getZonaArreglo(String zona) {
        switch (zona) {
            case "1":
                return asientosVIP;
            case "2":
                return asientosPalco;
            case "3":
                return asientosGeneral;
            default:
                return null;
        }
    }

    public static void liberarAsiento(Ticket ticket, String[] arregloZona) {
        int numeroAsientoActual = Integer.parseInt(ticket.getAsientoElegido().substring(1)) - 1;

        arregloZona[numeroAsientoActual] = null;

        System.out.println("Asiento " + ticket.getAsientoElegido() + " liberado de la zona actual.");
    }

    public static class ImpresorBoleta {

        public static void imprimirBoleta(TeatroMoroDebugging.Ticket ticket) {
            
            String zona = convertirZona(ticket.getZona());
            String asiento = ticket.getAsientoElegido();
            String precio = ticket.getPrecioBase() + " pesos";
            String borde = "/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\";
            System.out.println(borde);
            System.out.println("|                           |");
            System.out.println("| Información de la Boleta |");
            System.out.println("|                           |");
            System.out.printf("|  Zona: %-14s     |\n", zona);
            System.out.printf("|  Asiento: %-16s|\n", asiento.toUpperCase());
            System.out.printf("|  Precio Base: %-12s|\n", precio);
            System.out.println("|                           |");
            System.out.println(borde);
        }

        public static String convertirZona(String zonaCodigo) {
            switch (zonaCodigo) {
                case "1":
                    return "VIP";

                case "2":
                    return "Palco";
                case "3":
                    return "Entrada General";
                default:
                    return "Zona Desconocida";
            }
        }

    }

    public static class Ticket {

        private String zona;
        private String asientoElegido;
        private int precioBase;
        private String estado;
        private long tiempoReserva;

        public Ticket(int zonaCodigo, String asiento, int precio) {
            this.zona = String.valueOf(zonaCodigo);
            this.asientoElegido = asiento;
            this.precioBase = precio;
            this.estado = "Reservado";
            this.tiempoReserva = System.currentTimeMillis();
        }

        public String getZona() {
            return zona;
        }

        public void setZona(String zona) {
            this.zona = zona;
        }

        public String getAsientoElegido() {
            return asientoElegido;
        }

        public void setAsientoElegido(String asientoElegido) {
            this.asientoElegido = asientoElegido;
        }

        public int getPrecioBase() {
            return precioBase;
        }

        public void setPrecioBase(int precioBase) {
            this.precioBase = precioBase;
        }

        public String getEstado() {
            return estado;
        }

        public void setEstado(String estado) {
            this.estado = estado;
        }

        public long getTiempoReserva() {
            return tiempoReserva;
        }

        public void setTiempoReserva(long tiempoReserva) {
            this.tiempoReserva = tiempoReserva;
        }
    }

    public static void eliminarReservasExpiradas() {
        
        long tiempo = System.currentTimeMillis();
        
        for (int i = 0; i < cantidadTickets; i++) {
            
            Ticket ticket = ticketsReservados[i];
            
            if (ticket != null && "Reservado".equals(ticket.getEstado())) {
                
                long tiempoPasado = tiempo - ticket.getTiempoReserva();
                
                if (tiempoPasado > 60_000) {

                    String[] zona = getZonaArreglo(ticket.getZona());
                    if (zona != null) {
                        
                        int numeroAsiento = Integer.parseInt(ticket.getAsientoElegido().substring(1)) - 1;
                        zona[numeroAsiento] = null;
                    }

                    System.out.println("La reserva del asiento " + ticket.getAsientoElegido() + " ha expirado.");

                    ticketsReservados[i] = null;
                    totalEntradasReservadas--;
                }
            }
        }
    }

    public static void mostrarTiempoRestante() {
        
        long tiempoActual = System.currentTimeMillis();

        for (int i = 0; i < cantidadTickets; i++) {
            Ticket ticket = ticketsReservados[i];

            if (ticket != null && "Reservado".equals(ticket.getEstado())) {
                long tiempoTranscurrido = (tiempoActual - ticket.getTiempoReserva()) / 1000;
                long segundosRestantes = 60 - tiempoTranscurrido;

                if (segundosRestantes <= 0) {
                    liberarAsiento(ticket, getZonaArreglo(ticket.getZona()));
                    ticket.setEstado("Expirado");
                    System.out.println("La reserva del asiento " + ticket.getAsientoElegido() + " ha expirado.");
                } else {
                    System.out.println("Asiento " + ticket.getAsientoElegido()
                            + " (Zona " + ImpresorBoleta.convertirZona(ticket.getZona())
                            + ") - tiempo restante: " + segundosRestantes + " segundos");
                }
            }
        }
    }

    public static void volverAlMenuPrincipal() {
        System.out.println("Volviendo al menú principal...");
    }

    public static void listarEntradasReservadas(Ticket[] tickets) {
        
        System.out.println("Entradas reservadas:");
        
        boolean hayReservas = false;
        
        for (Ticket ticket : tickets) {
            if (ticket != null && "Reservado".equals(ticket.getEstado())) {
                System.out.println("Asiento: " + ticket.getAsientoElegido() + " - Zona: " + ticket.getZona() + " - Precio: " + ticket.getPrecioBase() + " - Estado: " + ticket.getEstado());
                hayReservas = true;
            }
        }
        
        
    }

    public static Ticket buscarTicketPorAsiento(String asiento, Ticket[] tickets) {
        for (Ticket ticket : tickets) {
            if (ticket != null && ticket.getAsientoElegido().equalsIgnoreCase(asiento)) {
                return ticket;
            }
        }
        return null;
    }

    public static String convertirNombreZona(int zonaCodigo) {
        switch (zonaCodigo) {
            case 1:
                return "VIP";
            case 2:
                return "Palco";
            case 3:
                return "Entrada General";
            default:
                return "Zona Desconocida";
        }
    }

    
}
