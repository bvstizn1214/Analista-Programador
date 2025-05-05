package com.mycompany.teatromorolist2;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TeatroMoroList2 {

    static Ticket[] ventasEntradas = new Ticket[90];
    static int totalVentas = 0;

    static int idReservaActual = 1;
    static int idClienteAcual = 1;

    static String[] asientosVIP = new String[30];
    static String[] asientosPalco = new String[30];
    static String[] asientosGeneral = new String[30];

    static String[] clientes = new String[90];

    static List<Descuento> listaDescuentos = new ArrayList<>();
    static List<Reserva> listaReservas = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        inicializarDescuentos();

        int opcionMenu;

        do {
            mostrarMenuPrincipal();
            opcionMenu = leerNumeroEntero(scanner, "Seleccione una opción válida:");
            if (opcionMenu == 5) {
                break;
            }

            switch (opcionMenu) {
                case 1 ->
                    reservarEntradas(scanner);
                case 2 ->
                    comprarEntradaReservada(scanner);
                case 3 ->
                    modificarEntradasReservadas(scanner);
                default ->
                    System.out.println("Opción no válida. Intente nuevamente.");
            }

        } while (true);

        System.out.println("Gracias por usar el sistema del Teatro Moro. ¡Hasta pronto!");
    }
    
    //Inicializa los descuentos disppnibles en el sistema, como estudiante o tercera edad)
    static void inicializarDescuentos() {
        listaDescuentos.add(new Descuento("Estudiante", 10));
        listaDescuentos.add(new Descuento("Tercera Edad", 15));
    }

    //Muestra el menú de inicio del programa
    static void mostrarMenuPrincipal() {
        System.out.println("\n--- Menú Principal ---");
        System.out.println("1) Reservar entradas");
        System.out.println("2) Comprar entradas reservadas");
        System.out.println("3) Modificar entradas reservadas");
        System.out.println("4) Imprimir boleta");
        System.out.println("5) Salir");
    }
    
    //Permite reservar entradas, capturando sus datos personales y del ticket
    static void reservarEntradas(Scanner scanner) {
        System.out.println("Ingrese su nombre como ID de cliente:");
        String nombreCliente;
        do {
            System.out.println("Proporcione un nombre válido:");
            nombreCliente = scanner.nextLine().trim();

            if (nombreCliente.matches("^[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ\\s]+$")) {
                break;
            } else {
                System.out.println("Entrada inválida. Solo se permiten letras y espacios. Intente nuevamente.");
            }
        } while (true);

        int idCliente = buscarCliente(nombreCliente);
        if (idCliente == -1) {
            if (idClienteAcual < clientes.length) {
                clientes[idClienteAcual] = nombreCliente;
                idCliente = idClienteAcual++;
                System.out.println("Cliente registrado exitosamente con ID: " + idCliente);
            } else {
                System.out.println("No se pueden registrar más clientes. Límite alcanzado.");
                return;
            }
        } else {
            System.out.println("Cliente identificado con ID: " + idCliente);
        }

        while (true) {
            System.out.println("\nSeleccione la zona para reservar:");
            System.out.println("1) VIP (20,000 pesos)");
            System.out.println("2) Palco (15,000 pesos)");
            System.out.println("3) General (10,000 pesos)");
            System.out.println("4) Volver al menú principal");

            int zonaSeleccionada = leerNumeroEntero(scanner, "Seleccione una opción válida:");

            if (zonaSeleccionada == 4) {
                System.out.println("Volviendo al menú principal...");
                break;
            } else if (zonaSeleccionada < 1 || zonaSeleccionada > 4) {
                System.out.println("Opción no válida. Intente nuevamente.");
                continue;
            }

            String[] asientosZona = obtenerAsientosZona(zonaSeleccionada);
            String zonaNombre = obtenerNombreZona(zonaSeleccionada);
            int precioZona = obtenerPrecioZona(zonaSeleccionada);

            mostrarAsientosDisponibles(asientosZona, zonaNombre);

            String asientoReservado;
            do {
                System.out.println("Ingrese el código del asiento que desea reservar:");
                asientoReservado = scanner.nextLine().trim().toUpperCase();

                
                if (asientoReservado.matches("^" + zonaNombre.charAt(0) + "\\d+$")) {
                    int numeroAsiento = Integer.parseInt(asientoReservado.substring(1));
                    if (numeroAsiento > 0 && numeroAsiento <= asientosZona.length) {
                        break;
                    } else {
                        System.out.println("Asiento inválido. Por favor, elija un asiento dentro del rango mostrado.");
                    }
                } else {
                    System.out.println("Formato inválido del asiento. Intente nuevamente. Ejemplo: " + zonaNombre.charAt(0) + "1, " + zonaNombre.charAt(0) + "10, etc.");
                }
            } while (true);

            int estadoAsiento = esAsientoValido(asientoReservado, asientosZona, zonaNombre);

            switch (estadoAsiento) {
                case 0:
                    cambiarEstadoAsiento(asientosZona, asientoReservado, "Reservado");
                    listaReservas.add(new Reserva(idReservaActual++, nombreCliente, asientoReservado, zonaNombre, precioZona));
                    System.out.println("Asiento reservado exitosamente.");
                    break;
                case -3:
                    System.out.println("El asiento ya está reservado. Elija otro asiento.");
                    break;
                default:
                    System.out.println("Error desconocido. Intente nuevamente.");
            }

            String continuar;
            do {
                System.out.println("¿Desea reservar otro asiento? (Si/No):");
                continuar = scanner.nextLine().trim().toLowerCase();

                if (continuar.equals("si") || continuar.equals("no")) {
                    break;
                } else {
                    System.out.println("Entrada inválida. Especifique 'Si' o 'No'.");
                }
            } while (true);

            if (continuar.equals("no")) {
                break;
            }
        }
    }

    //Gestiona la compra de entradas reservadas, aplica los descuentos y procesa el pago
    static void comprarEntradaReservada(Scanner scanner) {
    
    if (listaReservas.isEmpty()) {
        System.out.println("\n--- Reservas Actuales ---");
        System.out.println("No hay reservas disponibles.");
        return;
    }

    listarReservas();
    int idReservaSeleccionada = leerNumeroEntero(scanner, "Ingrese el ID de la reserva que desea comprar:");

    Reserva reservaSeleccionada = buscarReserva(idReservaSeleccionada);
    if (reservaSeleccionada != null) {
        aplicarDescuento(scanner, reservaSeleccionada);

        boolean pagoExitoso = procesarPago(scanner, reservaSeleccionada);
        if (pagoExitoso) {
            registrarVenta(reservaSeleccionada);
            listaReservas.remove(reservaSeleccionada);
            System.out.println("Compra realizada con éxito.");
        } else {
            System.out.println("La compra no se completó.");
        }
    } else {
        System.out.println("No se encontró una reserva con el ID ingresado.");
    }
}

    /*Modifica las entradas reservadas, pero no las compradas, cambia su asiento o zona
    actualizando su precio, zona y asiento*/
    static void modificarEntradasReservadas(Scanner scanner) {
    
    if (listaReservas.isEmpty()) {
        System.out.println("\n--- Reservas Actuales ---");
        System.out.println("No hay reservas disponibles para modificar.");
        return;
    }

    listarReservas();
    int idReservaSeleccionada = leerNumeroEntero(scanner, "Ingrese el ID de la reserva que desea modificar:");
    Reserva reservaSeleccionada = buscarReserva(idReservaSeleccionada);

    if (reservaSeleccionada != null) {
        System.out.println("¿Qué desea modificar?");
        System.out.println("1) Asiento");
        System.out.println("2) Zona");

        int opcionModificacion = leerNumeroEntero(scanner, "Seleccione una opción válida:");

        if (opcionModificacion == 1) {
            modificarAsiento(scanner, reservaSeleccionada);
        } else if (opcionModificacion == 2) {
            modificarZona(scanner, reservaSeleccionada);
        } else {
            System.out.println("Opción no válida.");
        }
    } else {
        System.out.println("No se encontró una reserva con el ID ingresado.");
    }
}

    // Cambia el estado de un asiento específico por ejemplo, marcarlo como reservado
    static void cambiarEstadoAsiento(String[] asientosZona, String codigoAsiento, String estado) {
        int index = obtenerIndiceAsiento(codigoAsiento);
        if (index >= 0 && index < asientosZona.length) {
            asientosZona[index] = estado;
        }
    }

    // Devuelve el índice correspondiente a un código de asiento, o -1 si es inválido
    static int obtenerIndiceAsiento(String codigoAsiento) {
        try {
            return Integer.parseInt(codigoAsiento.substring(1)) - 1;
        } catch (Exception e) {
            return -1;
        }
    }

    // Lista todas las reservas actuales con detalles como ID, cliente, zona y asiento
    static void listarReservas() {
        System.out.println("\n--- Reservas Actuales ---");
        if (listaReservas.isEmpty()) {
            System.out.println("No hay reservas disponibles.");
            return;
        }
        for (Reserva reserva : listaReservas) {
            System.out.printf("ID Reserva: %d | Cliente: %s | Zona: %s | Asiento: %s | Precio: %d\n",
                    reserva.idReserva, reserva.idCliente, reserva.zona, reserva.asiento, reserva.precio);
        }
    }

    // Busca una reserva específica por su ID y la devuelve; si no existe, devuelve null
    static Reserva buscarReserva(int idReserva) {
        for (Reserva reserva : listaReservas) {
            if (reserva.idReserva == idReserva) {
                return reserva;
            }
        }
        return null;
    }

    // Aplica un descuento específico a una reserva, si corresponde
    static void aplicarDescuento(Scanner scanner, Reserva reserva) {
        System.out.println("¿El cliente aplica a algún descuento?");
        for (int i = 0; i < listaDescuentos.size(); i++) {
            Descuento descuento = listaDescuentos.get(i);
            System.out.printf("%d) %s (%d%%)\n", i + 1, descuento.descripcion, descuento.porcentaje);
        }
        System.out.println((listaDescuentos.size() + 1) + ") No aplica");

        int opcionDescuento = leerNumeroEntero(scanner, "Seleccione una opción válida:");
        if (opcionDescuento > 0 && opcionDescuento <= listaDescuentos.size()) {
            Descuento descuentoSeleccionado = listaDescuentos.get(opcionDescuento - 1);
            reserva.precio -= reserva.precio * descuentoSeleccionado.porcentaje / 100;
            System.out.println("Descuento aplicado exitosamente.");
        } else {
            System.out.println("No se aplicó ningún descuento.");
        }
    }

    // Procesa el pago para una reserva, permitiendo elegir entre débito o efectivo
    static boolean procesarPago(Scanner scanner, Reserva reserva) {
        System.out.printf("El costo total es: %d pesos\n", reserva.precio);

        String respuesta;
        do {
            System.out.println("¿Desea pagar ahora? (Si/No):");
            respuesta = scanner.nextLine().trim().toLowerCase();

            if (respuesta.equals("si") || respuesta.equals("no")) {
                break;
            } else {
                System.out.println("Entrada inválida. Especifique 'Si' o 'No'.");
            }
        } while (true);

        if (respuesta.equals("si")) {
            int metodoPago;
            do {
                System.out.println("Seleccione el método de pago:");
                System.out.println("1) Débito");
                System.out.println("2) Efectivo");

                metodoPago = leerNumeroEntero(scanner, "Seleccione una opción válida:");

                switch (metodoPago) {
                    case 1 -> {
                        System.out.println("Ingrese su contraseña para pago Débito (solo números):");

                        while (!scanner.hasNextInt()) {
                            System.out.println("Error: La contraseña debe ser un número.");
                            scanner.next();
                        }

                        int contraseña = scanner.nextInt();
                        scanner.nextLine();
                        System.out.println("Pago con Débito procesado exitosamente para la reserva ID " + reserva.idReserva);
                        return true;
                    }
                    case 2 -> {
                        int pago;
                        int cambio;

                        System.out.println("El costo del ticket: " + reserva.precio + " pesos");
                        System.out.println("Ingrese el monto con el que desea pagar:");

                        while (!scanner.hasNextInt()) {
                            System.out.println("Error: Debe ser un monto válido");
                            scanner.next();
                        }

                        pago = scanner.nextInt();
                        scanner.nextLine();

                        while (pago < reserva.precio) {
                            System.out.println("El monto ingresado es insuficiente");

                            while (!scanner.hasNextInt()) {
                                System.out.println("Error: Debe ingresar un monto válido");
                                scanner.next();
                            }

                            pago = scanner.nextInt();
                            scanner.nextLine();
                        }

                        cambio = pago - reserva.precio;
                        if (cambio > 0) {
                            System.out.printf("Pago procesado exitosamente. Su cambio es: %d pesos.\n", cambio);
                        } else {
                            System.out.println("Pago procesado exitosamente. No hay cambio.");
                        }

                        System.out.println("Pago en efectivo procesado exitosamente para la reserva ID " + reserva.idReserva);
                        return true;
                    }
                    default ->
                        System.out.println("Método de pago no válido. Intente nuevamente.");
                }
            } while (true);
        }

        return false;
    }

    // Registra una venta completada y la almacena en el arreglo de ventas
    static void registrarVenta(Reserva reserva) {
        ventasEntradas[totalVentas++] = new Ticket(reserva.zona, reserva.asiento, reserva.precio);
    }

    // Permite modificar el asiento de una reserva existente, validando su disponibilidad
    static void modificarAsiento(Scanner scanner, Reserva reserva) {
        String[] asientosZona = obtenerAsientosZona(obtenerCodigoZona(reserva.zona));
        mostrarAsientosDisponibles(asientosZona, reserva.zona);

        String nuevoAsiento;
        while (true) {
            System.out.println("Ingrese el nuevo asiento:");
            nuevoAsiento = scanner.nextLine().trim().toUpperCase();

            if (nuevoAsiento.matches("^" + reserva.zona.charAt(0) + "\\d+$")) {
                int numeroAsiento = Integer.parseInt(nuevoAsiento.substring(1));
                if (numeroAsiento > 0 && numeroAsiento <= asientosZona.length) {
                    break;
                } else {
                    System.out.println("Asiento inválido. Por favor, elija un asiento dentro del rango mostrado.");
                }
            } else {
                System.out.println("Formato inválido del asiento. Intente nuevamente. Ejemplo: " + reserva.zona.charAt(0) + "1, " + reserva.zona.charAt(0) + "10, etc.");
            }
        }

        int estadoAsiento = esAsientoValido(nuevoAsiento, asientosZona, reserva.zona);

        switch (estadoAsiento) {
            case 0:
                cambiarEstadoAsiento(asientosZona, reserva.asiento, null);
                reserva.asiento = nuevoAsiento;
                cambiarEstadoAsiento(asientosZona, nuevoAsiento, "Reservado");
                System.out.println("Asiento modificado correctamente.");
                break;
            case -1:
                System.out.println("Formato inválido del asiento. Intente nuevamente. Ejemplo de formato: P1, V10, etc.");
                break;
            case -2:
                System.out.println("Asiento inválido. Por favor, elija un asiento mostrado.");
                break;
            case -3:
                System.out.println("El asiento ya está reservado. Elija otro asiento.");
                break;
            default:
                System.out.println("Error desconocido. Intente nuevamente.");
        }
    }

    // Permite cambiar la zona de una reserva, actualizando el precio y el asiento
    static void modificarZona(Scanner scanner, Reserva reserva) {
        System.out.println("Seleccione la nueva zona:");
        System.out.println("1) VIP");
        System.out.println("2) Palco");
        System.out.println("3) General");

        int nuevaZona = leerNumeroEntero(scanner, "Seleccione una opción válida:");
        if (nuevaZona < 1 || nuevaZona > 3) {
            System.out.println("Zona inválida. Intente nuevamente.");
            return;
        }

        String[] nuevosAsientos = obtenerAsientosZona(nuevaZona);
        String nuevaZonaNombre = obtenerNombreZona(nuevaZona);
        int nuevoPrecio = obtenerPrecioZona(nuevaZona);

        mostrarAsientosDisponibles(nuevosAsientos, nuevaZonaNombre);

        String nuevoAsiento;
        while (true) {
            System.out.println("Ingrese el nuevo asiento:");
            nuevoAsiento = scanner.nextLine().trim().toUpperCase();
           
            if (nuevoAsiento.matches("^" + nuevaZonaNombre.charAt(0) + "\\d+$")) {
                int numeroAsiento = Integer.parseInt(nuevoAsiento.substring(1));
                if (numeroAsiento > 0 && numeroAsiento <= nuevosAsientos.length) {
                    break;
                } else {
                    System.out.println("Asiento inválido. Por favor, elija un asiento dentro del rango mostrado.");
                }
            } else {
                System.out.println("Formato inválido del asiento. Intente nuevamente. Ejemplo: " + nuevaZonaNombre.charAt(0) + "1, " + nuevaZonaNombre.charAt(0) + "10, etc.");
            }
        }

        int estadoAsiento = esAsientoValido(nuevoAsiento, nuevosAsientos, nuevaZonaNombre);

        switch (estadoAsiento) {
            case 0:            
                cambiarEstadoAsiento(obtenerAsientosZona(obtenerCodigoZona(reserva.zona)), reserva.asiento, null);
               
                reserva.asiento = nuevoAsiento;
                reserva.zona = nuevaZonaNombre;
                reserva.precio = nuevoPrecio;
                cambiarEstadoAsiento(nuevosAsientos, nuevoAsiento, "Reservado");
                System.out.println("Zona y asiento modificados correctamente.");
                break;
            case -1:
                System.out.println("Formato inválido del asiento. Intente nuevamente. Ejemplo de formato: P1, V10, etc.");
                break;
            case -2:
                System.out.println("Asiento inválido. Por favor, elija un asiento dentro del rango mostrado.");
                break;
            case -3:
                System.out.println("El asiento ya está reservado. Elija otro asiento.");
                break;
            default:
                System.out.println("Error desconocido. Intente nuevamente.");
        }
    }

    // Muestra todos los asientos disponibles en una zona específica
    static void mostrarAsientosDisponibles(String[] asientos, String sufijo) {
        System.out.println("\nAsientos disponibles en " + sufijo + ":");
        for (int i = 0; i < asientos.length; i++) {
            String codigoAsiento = sufijo.charAt(0) + String.valueOf(i + 1);
            System.out.print(asientos[i] == null ? codigoAsiento + "\t" : "[X]\t");
            if ((i + 1) % 10 == 0) {
                System.out.println();
            }
        }
        System.out.println();
    }

     // Verifica si un asiento es válido para una reserva, devolviendo un código de estado
    static int esAsientoValido(String asiento, String[] asientos, String sufijo) {
        try {
            
            if (!asiento.startsWith(sufijo.substring(0, 1))) {
                return -1;
            }

            int numero = Integer.parseInt(asiento.substring(1));
            if (numero <= 0 || numero > asientos.length) {
                return -2; 
            }

            if (asientos[numero - 1] != null) {
                return -3;
            }

            return 0;
        } catch (Exception e) {
            return -1;
        }
    }

    // Devuelve el nombre de la zona dado un número (1 para VIP, 2 para Palco, 3 para General)
    static String obtenerNombreZona(int zona) {
        return switch (zona) {
            case 1 ->
                "VIP";
            case 2 ->
                "Palco";
            case 3 ->
                "General";
            default ->
                "Zona Desconocida";
        };
    }

    // Devuelve el precio de la zona basado en su número (1 para VIP, 2 para Palco, 3 para General)
    static int obtenerPrecioZona(int zona) {
        return switch (zona) {
            case 1 ->
                20000;
            case 2 ->
                15000;
            case 3 ->
                10000;
            default ->
                0;
        };
    }

    // Devuelve el arreglo de asientos correspondiente a la zona seleccionada
    static String[] obtenerAsientosZona(int zona) {
        return switch (zona) {
            case 1 ->
                asientosVIP;
            case 2 ->
                asientosPalco;
            case 3 ->
                asientosGeneral;
            default ->
                null;
        };
    }

    // Devuelve el código numérico de una zona basado en su nombre (VIP, Palco, General)
    static int obtenerCodigoZona(String zonaNombre) {
        return switch (zonaNombre) {
            case "VIP" ->
                1;
            case "Palco" ->
                2;
            case "General" ->
                3;
            default ->
                0;
        };
    }

    // Lee y valida número entero que ingresa el usuario al seleccionar una opcion, asegurando que la entrada sea válida
    static int leerNumeroEntero(Scanner scanner, String mensaje) {
        System.out.println(mensaje);
        while (!scanner.hasNextInt()) {
            System.out.println("Debe ingresar una opción válida.");
            scanner.next();
        }
        int numero = scanner.nextInt();
        scanner.nextLine();
        return numero;
    }

    // Busca un cliente por su nombre y devuelve su ID, o -1 si no existe
    static int buscarCliente(String nombreCliente) {
        for (int i = 0; i < idClienteAcual; i++) {
            if (clientes[i] != null && clientes[i].equalsIgnoreCase(nombreCliente)) {
                return i;
            }
        }
        return -1;
    }

    //Representa un ticket de entrada vendido
    //Contiene informacion sobre la zona, el asiento y el precio pagado
    static class Ticket {

        String zona;
        String asiento;
        int precio;

        Ticket(String zona, String asiento, int precio) {
            this.zona = zona;
            this.asiento = asiento;
            this.precio = precio;
        }
    }

    //Representa una entrada reservada por el cliente
    //Contiene el ID de la reserva, el nombre del cliente, el asiento, zona y precio
    static class Reserva {

        String asiento;
        String zona;
        int precio;
        int idReserva;
        String idCliente;

        Reserva(int idReserva, String idCliente, String asiento, String zona, int precio) {
            this.asiento = asiento;
            this.zona = zona;
            this.precio = precio;
            this.idReserva = idReserva;
            this.idCliente = idCliente;
        }
    }

    // Representa un descuento que se puede aplicar a una entrada
    // Contiene la descripción del descuento y el porcentaje aplicado
    static class Descuento {

        String descripcion; // Descripción del descuento (por ejemplo, "Estudiante", "Tercera Edad")
        int porcentaje; // Porcentaje de descuento (por ejemplo, 10%, 15%)

        Descuento(String descripcion, int porcentaje) {
            this.descripcion = descripcion;
            this.porcentaje = porcentaje;
        }
    }
}
