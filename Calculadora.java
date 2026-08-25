import java.util.Scanner;

public class Calculadora {

    public static void main(String[] args) {

        Scanner leer = new Scanner(System.in);

        // Declaramos variables
        int n1;
        int n2;

        int suma;
        int resta;
        int multiplicacion;
        int modulo;

        double division;
        double potencia;
        double raiz;


        // Pedimos los números
        System.out.println("Ingrese el primer numero:");
        n1 = leer.nextInt();

        System.out.println("Ingrese el segundo numero:");
        n2 = leer.nextInt();


        // SUMA
        suma = n1 + n2;

        System.out.println("La suma de " + n1 + " y " + n2 + " es " + suma);


        // RESTA
        resta = n1 - n2;

        System.out.println("La resta de " + n1 + " y " + n2 + " es " + resta);


        // MULTIPLICACIÓN
        multiplicacion = n1 * n2;

        System.out.println("La multiplicacion de " + n1 + " y " + n2 + " es " + multiplicacion);


        // DIVISIÓN
        if (n2 != 0) {

            division = (double) n1 / n2;

            System.out.println("La division de " + n1 + " entre " + n2 + " es " + division);

        } else {

            System.out.println("La division de " + n1 + " entre " + n2 + " no es posible");
        }


        // POTENCIA
        potencia = Math.pow(n1, n2);

        System.out.println("La potencia de " + n1 + " elevado a " + n2 + " es " + potencia);


        // RAÍZ
        if (n1 >= 0 && n2 != 0) {

            raiz = Math.pow(n1, 1.0 / n2);

            System.out.println("La raiz de indice " + n2 + " de " + n1 + " es " + raiz);

        } else {

            System.out.println("La raiz de indice " + n2 + " de " + n1 + " no es posible");
        }


        // MÓDULO
        if (n2 != 0) {

            modulo = n1 % n2;

            System.out.println("El modulo de " + n1 + " entre " + n2 + " es " + modulo);

        } else {

            System.out.println("El modulo de " + n1 + " entre " + n2 + " no es posible");
        }


        leer.close();
    }
}