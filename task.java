import java.util.Scanner;

public class task {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter 5 numbers: ");
        int sum = 0;
        for (int i = 1 ; i <= 5; i++) {
            int num = scan.nextInt();
            sum = sum + num;
        }
        System.out.println(sum);
        scan.close();

    }
}