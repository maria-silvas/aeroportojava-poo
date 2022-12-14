package utils;

import java.util.Scanner;

public class GetOption {
    public static int get(int min, int max, Scanner scanner) {
        int option = 0;
        do {
            System.out.print("Opção: ");
            option = scanner.nextInt();
            scanner.nextLine();
            if (option < min || option > max) {
                System.out.println("Opção inválida. Tente novamente.");
            }
        } while (option < min || option > max);
        return option;
    }
}
