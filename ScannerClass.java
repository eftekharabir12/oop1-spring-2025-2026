import java.util.Scanner;

public class ScannerClass {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        byte age = scanner.nextByte();
        System.out.print("Your age is: " + age);
        //int year = scanner.nextInt();

        String name = scanner.nextLine().trim().toLowerCase();
        System.out.print("Your name is: " + name );

        scanner.close();
    }
}
 