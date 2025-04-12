//ESCRIBIRÉ COMENTARIOS EN LAS FUNCIONES "NUEVAS" O MAS COMPLEJAS

package com.mycompany.ventasteatromoro;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;

public class VentasTeatroMoro {
    //Variables estaticas para almacenar numeros globales
    //La tercera variable estatica está dentro de la ultima clase, al final del programa
    static int capacidadTotal = 150;
    static ArrayList<Ticket> ticketsVendidos = new ArrayList<>();

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        
        //Se crea la variable para almacenar la elección del menú principal
        int eleccionDeMenu;

        //Se crea una variable randomizada para darle una capa de personalización a la disponibilidad de asientos
        int entradasDisponibles = random.nextInt(101) + 50;

        do {
            System.out.println("\nBienvenido al sistema de Entradas Teatro Moro");
            System.out.println("Por favor seleccione una opción");
            System.out.println("1.- Venta de Entradas");
            System.out.println("2.- Promociones");
            System.out.println("3.- Búsqueda de entradas");
            System.out.println("4.- Eliminar entradas");
            System.out.println("5.- Salir");

            while (!scanner.hasNextInt()) {
                System.out.println("Solo se permiten números como elección del menú");
                scanner.next();
            }

            eleccionDeMenu = scanner.nextInt();
            scanner.nextLine();

            if (eleccionDeMenu < 1 || eleccionDeMenu > 5) {
                System.out.println("Opción inválida");
                continue;
            }

            switch (eleccionDeMenu) {

                case 1:
                    //Se declara una variable local que solo será usada en este bloque
                    int tipoEntrada;
                    do {
                        System.out.println("\nElija su entrada:");
                        System.out.println("1) VIP");
                        System.out.println("2) Platea");
                        System.out.println("3) Entrada General");
                        System.out.println("Entradas disponibles: " + entradasDisponibles + " de " + capacidadTotal + " asientos totales");

                        while (!scanner.hasNextInt()) {
                            System.out.println("¡Error! Ingrese un número válido.");
                            scanner.next();
                        }

                        tipoEntrada = scanner.nextInt();
                        scanner.nextLine();

                        if (tipoEntrada < 1 || tipoEntrada > 3) {
                            System.out.println("Opción inválida, por favor intente nuevamente.");
                        }

                    } while (tipoEntrada < 1 || tipoEntrada > 3);

                    //Variable tipo string vacía para usar segun sea necesario en cada case
                    String zona = "";
                    //Variable tipo int para almacenar el valor de cada zona en cada case
                    int precioBase = 0;

                    switch (tipoEntrada) {
                        case 1 -> {
                            zona = "VIP";
                                                    
                            precioBase = 20000;
                        }
                        case 2 -> {
                            zona = "Platea";
                            precioBase = 15000;
                        }
                        case 3 -> {
                            zona = "Entrada General";
                            precioBase = 10000;
                        }
                    }
                    

                    //CAPTURA DE DATOS DEL USUARIO
                    
                    
                    int edad;
                    do {
                        System.out.println("Ingrese su edad: ");
                        while (!scanner.hasNextInt()) {
                            System.out.println("Ingrese una edad válida.");
                            scanner.next();
                        }
                        edad = scanner.nextInt();
                        scanner.nextLine();
                    } while (edad <= 0);

                    
                    //Declaracion de variables para usar y validar las respuestas del usuario
                    String respuestaEstudiante;
                    boolean esEstudiante = false;
                    boolean respuestaValida = false;

                    do {
                        System.out.println("¿Es usted estudiante? (Si/No)");
                        respuestaEstudiante = scanner.nextLine().trim();

                        if (respuestaEstudiante.equalsIgnoreCase("Si")) {
                            esEstudiante = true;
                            respuestaValida = true;
                        } else if (respuestaEstudiante.equalsIgnoreCase("No")) {
                            esEstudiante = false;
                            respuestaValida = true;
                        } else {
                            System.out.println("Respuesta inválida. Escriba 'Si' o 'No'.");
                        }
                    } while (!respuestaValida);

                    
                    //Se crea una nueva Persona guardando su edad y si es estudiante dentro de estudiante
                    Persona persona = new Persona(edad, esEstudiante);

                    
                    //Variables para guardar momentaneamente los descuentos
                    double descuentoEstudiante = 0;
                    double descuentoTerceraEdad = 0;

                    
                    //Se usa la variable persona y sus metodos creado en la clase Persona
                    if (persona.esTerceraEdad()) {
                        descuentoTerceraEdad = 0.15;
                    }

                    if (persona.esEstudiante()) {
                        descuentoEstudiante = 0.10;
                    }

                    //Se crea la variable cantidad entradas para capturar la cantidad que el usuario quiere compra
                    int cantidadEntradas;

                    //Se valida en caso de que la variable Random sea igual a 0
                    if (entradasDisponibles == 0) {
                        System.out.println("Lo sentimos, no quedan entradas disponibles");
                    }

                    do {
                        System.out.println("¿Cuántas entradas desea comprar? (Disponibles: " + entradasDisponibles + " Entradas)");
                        if (scanner.hasNextInt()) {
                            cantidadEntradas = scanner.nextInt();
                            scanner.nextLine();
                            if (cantidadEntradas <= 0) {
                                System.out.println("Ingrese una cantidad válida.");
                            } else if (cantidadEntradas > entradasDisponibles) {
                                System.out.println("Solo quedan " + entradasDisponibles + " entradas disponibles");
                                cantidadEntradas = -1;
                            }
                        } else {
                            System.out.println("Ingrese una cantidad válida.");
                            scanner.nextLine();
                            cantidadEntradas = -1;
                        }
                    } while (cantidadEntradas <= 0);

                    
                    //Se crea una variable local para almacenar los decuentos por cantidad comprada y se almacena dentro de la misma
                    double descuentoPorCantidad = 0;
                    if (cantidadEntradas >= 10) {
                        descuentoPorCantidad = 0.20;
                    } else if (cantidadEntradas >= 5) {
                        descuentoPorCantidad = 0.10;
                    } else if (cantidadEntradas >= 3) {
                        descuentoPorCantidad = 0.05;
                    }

                    //Se operan los descuentos obtenidos para almacenarlos dentro de la variable descriptiva correspondiente 
                    double descuentoTotal = descuentoTerceraEdad + descuentoPorCantidad + descuentoEstudiante;
                    double precioFinalUnitario = precioBase - (precioBase * descuentoTotal);
                    double precioFinal = precioFinalUnitario * cantidadEntradas;

                    
                    //Se muestra información de la venta al usuario, se usan los metodos de la Persona guardados en 'persona'
                    //Elegí el metodo Math.round ya que me causaba ruido visual que los valores terminen en .00 simulando centavos
                    System.out.println("\nResumen del comprador:");
                    System.out.println("Edad: " + persona.getEdad());
                    System.out.println("Estudiante: " + (persona.esEstudiante() ? "Sí" : "No"));
                    System.out.println("Tercera edad: " + (persona.esTerceraEdad() ? "Sí" : "No"));
                    System.out.println("Zona elegida: " + zona);
                    System.out.println("Precio base por entrada: $" + Math.round(precioBase));
                    System.out.println("Descuento Estudiante: " + Math.round(descuentoEstudiante * 100) + "%");
                    System.out.println("Descuento Tercera Edad: " + Math.round(descuentoTerceraEdad * 100) + "%");
                    System.out.println("Descuento por cantidad comprada: " + Math.round(descuentoPorCantidad * 100) + "%");
                    System.out.println("Descuento total aplicado: " + Math.round(descuentoTotal * 100) + "%");
                    System.out.println("Precio final unitario: $" + Math.round(precioFinalUnitario));
                    System.out.println("Cantidad de entradas: " + cantidadEntradas);
                    System.out.println("Total a pagar: $" + Math.round(precioFinal));

                    //Una vez comprado el ticket se crea un nuevo Ticket con la informacion guardada en sus argumentos y se almacena en 'nuevoTicket'               
                    Ticket nuevoTicket = new Ticket(zona, precioFinal, persona, cantidadEntradas);
                    //Se añade 'nuevoTicket al Array ticketsVendidos
                    ticketsVendidos.add(nuevoTicket);
                    
                    //Se resta la cantidad de entradas vendidas, entradas disponibles es un Random que muestra un solo valor por inicio de programa
                    entradasDisponibles -= cantidadEntradas;

                    //Se muestra la información del numero del ticket vendido
                    System.out.println("¡Entrada vendida! Número de ticket: " + nuevoTicket.getNumero());
                    break;

                case 2:
                 
                    System.out.println("\nPromociones disponibles:");
                    System.out.println("1) 10% de descuento estudiantes");
                    System.out.println("2) 15% de descuento tercera edad");
                    System.out.println("3) 5% de descuento por comprar 3 o más entradas");
                    System.out.println("4) 10% de descuento por comprar 5 o más entradas");
                    System.out.println("5) 20% de descuento por comprar 10 o más entradas");
                    System.out.println("       === Los escuentos son acumulativos===     ");
                    break;

                case 3:
                    if (ticketsVendidos.isEmpty()) {
                        System.out.println("No hay tickets registrados.");
                        break;
                    }
                    int menuTicket;
                    do {
                        System.out.println("\n--- Menú de Búsqueda de Entradas ---");
                        System.out.println("1) Buscar por número de ticket");
                        System.out.println("2) Buscar por zona (VIP, Platea, Entrada General)");
                        System.out.println("3) Buscar por tipo de comprador (Estudiante / Tercera edad)");
                        System.out.println("4) Volver al menú principal");

                        while (!scanner.hasNextInt()) {
                            System.out.println("Ingrese una opción válida");
                            scanner.next();
                        }

                        menuTicket = scanner.nextInt();
                        scanner.nextLine();

                        switch (menuTicket) {
                            case 1:

                                System.out.println("Ingrese el número de ticket a buscar:");
                                while (!scanner.hasNextInt()) {
                                    System.out.println("Ingrese un número válido:");
                                    scanner.next();
                                }

                                int numeroBuscado = scanner.nextInt();
                                scanner.nextLine();

                                //Se crea una variable booleana "bandera" para verificar y validar si se encontró un ticket previamente almacenado o no
                                boolean encontrado = false;
                                
                                //Se itera la clase Ticket dentro de los ticketsVendidos y se guarda en 't'
                                for (Ticket t : ticketsVendidos) {
                                    //Si el numero buscado está dentro de los valores que contiene la variable 't'
                                    if (t.getNumero() == numeroBuscado) {
                                        //Enconces muetra los resultados
                                        t.mostrarInfoTicket();
                                        
                                        //El valor de la variable se cambia a true
                                        encontrado = true;
                                        break;
                                    }
                                }
                                //La variable bandera hace que muestre el siguiente mensaje en caso de que no se encuentre un ticket
                                if (!encontrado) {
                                    System.out.println("No se encontró un ticket con ese número.");
                                }
                                break;

                            case 2:
                                
                                //Mismo caso anterior, variables locales y una variable bandera para validar un caso u otro
                                String zonaBuscada;
                                boolean zonaValida;
                                do {
                                    System.out.println("Ingrese la zona (VIP, Platea, Entrada General");
                                    
                                    //Se ingresa y se almacena la zona que se quiere buscar eliminando los valores vacios al inicio y al final
                                    zonaBuscada = scanner.nextLine().trim();
                                    
                                    //La variable almacena el valor ingresado por el usuario, "es uno o es otro o es el otro"
                                    zonaValida = zonaBuscada.equalsIgnoreCase("VIP")
                                            || zonaBuscada.equalsIgnoreCase("Platea")
                                            || zonaBuscada.equalsIgnoreCase("Entrada General");
                                    if (!zonaValida) {
                                        System.out.println("Zona inválida, escriba con separación en caso de ser necesario");
                                    }
                                } while (!zonaValida);

                                
                                //Se usa la variable bandera para validar si se muestra una informacion u otra
                                boolean zonaEncontrada = false;
                                for (Ticket t : ticketsVendidos) {
                                    if (t.getZona().equalsIgnoreCase(zonaBuscada)) {
                                        t.mostrarInfoTicket();
                                        zonaEncontrada = true;
                                    }

                                }
                                if (!zonaEncontrada) {
                                    System.out.println("No se encontraron tickets en esa zona");
                                }
                                break;
                            case 3:

                                //Mismo caso anterior con variable local y variabel bandera
                                int tipoComprador = 0;
                                boolean compradorValido = false;

                                do {
                                    System.out.println("Seleccione tipo de comprador:");
                                    System.out.println("1) Estudiante");
                                    System.out.println("2) Tercera edad");

                                    if (scanner.hasNextInt()) {
                                        tipoComprador = scanner.nextInt();
                                        scanner.nextLine(); // Limpiar el buffer

                                        if (tipoComprador == 1 || tipoComprador == 2) {
                                            compradorValido = true;
                                        } else {
                                            System.out.println("Opción inválida. Debe ser 1 o 2.");
                                        }
                                    } else {
                                        System.out.println("Por favor, ingrese un número válido.");
                                        scanner.next(); // Limpiar entrada inválida (como letras)
                                    }
                                } while (!compradorValido);

                                
                                //Variable local para usar como bandera
                                boolean tipoEncontrado = false;

                                //Se itera la clase Ticket dentro de ticketsVendidos y se almacena en 't'
                                for (Ticket t : ticketsVendidos) {
                                    Persona p = t.getPersona();

                                    //Se crea un ciclo para validar el dato ingresado(1 o 2)  y si es estudiante o tercera edad
                                    if ((tipoComprador == 1 && p.esEstudiante())
                                            || (tipoComprador == 2 && p.esTerceraEdad())) {
                                        t.mostrarInfoTicket();
                                        tipoEncontrado = true;
                                    }
                                }

                                if (!tipoEncontrado) {
                                    System.out.println("No se encontraron tickets para este tipo de comprador.");
                                }

                                break;
                            default:
                                System.out.println("Opción no válida");
                        }
                    } while (menuTicket != 4);

                    break;

                case 4:
                    //Se muestra este mensaje en caso de que no hayan mentradas vendidas y se limita el ingreso a esta zona
                    if (ticketsVendidos.isEmpty()) {
                        System.out.println("No hay tickets registradas para eliminar");
                        
                        break;
                    }

                    //Se muestran los tickets válidos para borrar
                    for (Ticket t : ticketsVendidos) {

                        t.mostrarInfoTicket();
                    }
                    System.out.println("Ingrese el número del ticket a eliminar:");
                    int numeroTicket = scanner.nextInt();
                    scanner.nextLine();

                    //se crea una variable booleana que almacena el mecanismo de borrador
                    //Elimina de ticketsVendidos el valor que proporciona el usuario en numeroTicket
                    //Y comprueba si es igual al getNumero dentro de 't' que contiene la iteracion de ticketsVendidos
                    //la expresion '->' la aprendí cuando comencé a usar Kotlin por hobby, es una expresion lambda
                    //Y como kotlin está basado en Java decidí intentarlo, consulté con una IA y según lo que dice, es valido usarlo
                    boolean eliminado = ticketsVendidos.removeIf(t -> t.getNumero() == numeroTicket);

                    
                    if (eliminado) {
                        System.out.println("Ticket #" + numeroTicket + " eliminado correctamente.");
                    } else {
                        System.out.println("No se encontró un ticket con ese número.");
                    }
                    break;

                case 5:
                    System.out.println("¡Hasta pronto!");
                    break;
            }

        } while (eleccionDeMenu != 5);

        scanner.close();
    }

    // Clase persona con variables de instancia
    public static class Persona {

        private int edad;
        private boolean esEstudiante;

        
        //Se crea el constructor con sus parametros
        public Persona(int edad, boolean esEstudiante) {
            this.edad = edad;
            this.esEstudiante = esEstudiante;
        }
        //Se crean metodos publicos para acceder a la informacion del usuario desde fuera de su clase
        public int getEdad() {
            return edad;
        }

        public boolean esEstudiante() {
            return esEstudiante;
        }

        public boolean esTerceraEdad() {
            return edad >= 65;
        }
    }

    public static class Ticket {

        private static int contadorDeTickets = 1;

        private int numero;
        private String zona;
        private double precioFinal;
        private Persona persona;
        private int cantidadEntradas;

        public Ticket(String zona, double precioFinal, Persona persona, int cantidadEntradas) {
            //Se aumenta el contador de tickets almacenado en numero
            this.numero = contadorDeTickets++;
            this.zona = zona;
            this.precioFinal = precioFinal;
            this.persona = persona;
            this.cantidadEntradas = cantidadEntradas;
        }

        public int getNumero() {
            return numero;
        }

        public String getZona() {
            return zona;
        }

        public double getPrecioFinal() {
            return precioFinal;
        }

        public Persona getPersona() {
            return persona;
        }

        public int getCantidadEntradas() {
            return cantidadEntradas;
        }

        //Este metodo es void ya que no hace return o no regresa ningun valor si no que se usa para mostrar informacion del ticket guardado NO el comprado
        public void mostrarInfoTicket() {
            System.out.println("=== Ticket #" + numero + " ===");
            System.out.println("Zona: " + zona);
            System.out.println("Precio Final: $" + precioFinal);
            System.out.println("Edad: " + persona.getEdad());
            System.out.println("Estudiante: " + (persona.esEstudiante() ? "Sí" : "No"));
            System.out.println("Tercera Edad: " + (persona.esTerceraEdad() ? "Sí" : "No"));
            System.out.println("Cantidad comprada: " + cantidadEntradas + " entradas");
            System.out.println("======================");
        }
    }
}
