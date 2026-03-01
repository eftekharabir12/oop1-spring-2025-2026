public class Strings {
    public static void main(String[] args) {
        String str1 = "Hello, World!";
        String str2 = "Java Programming";

        // Length of the strings
        System.out.println("Length of str1: " + str1.length());
        System.out.println("Length of str2: " + str2.length());





        // Concatenation
        String concatenated = str1 + " " + str2;
        System.out.println("Concatenated String: " + concatenated);

        // Substring
        String Dha = str2.substring(7,16);
        System.out.println("Substring of str2: " + Dha);



        // Replace
        String replacedString = str1.replace("World", "Java");
        System.out.println("Replaced String: " + replacedString);

        // Convert to Uppercase
        String upperCaseString = str2.toUpperCase();
        System.out.println("Uppercase str2: " + upperCaseString);

        // Convert to Lowercase
        String lowerCaseString = str1.toLowerCase();
        System.out.println("Lowercase str1: " + lowerCaseString);

        // Trim
        String stringWithSpaces = " Trim me! ";
        String trimmedString = stringWithSpaces.trim();
        System.out.println("Trimmed String: '" + trimmedString + "'");

        // Boolean checks
        boolean startsWithHello = str1.startsWith("Hello");
        boolean endsWithProgramming = str2.endsWith("Java");
        System.out.println("str1 starts with 'Hello': " + startsWithHello);
        System.out.println("str2 ends with 'Programming': " + endsWithProgramming);

        // Character at index
        char charAtIndex5 = str1.charAt(5);
        System.out.println("Character at index 5 of str1: " + charAtIndex5);

        // Index of substring
        int indexOfJava = str2.indexOf("Java");
        System.out.println("Index of 'Java' in str2: " + indexOfJava);
    }
}
 