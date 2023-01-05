package bullscows;

import java.util.Scanner;
import java.util.Random;

public class BullsCows {

    static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        System.out.println("Input the length of the secret code:");
        int length;
        try {
            length = scanner.nextInt();
        } catch (Exception e) {
            System.out.println("Error: invalid length.");
            return;
        }

        System.out.println("Input the number of possible symbols in the code:");
        int possibleCharacters = scanner.nextInt();
        if (possibleCharacters > 36) {
            System.out.println("Error: a maximum of 36 characters are allowed.");
            return;
        }
        if (length > possibleCharacters || length == 0) {
            System.out.println("Error: can't generate a secret number with a given length because there " +
                    "aren't enough unique digits. It is also not possible to use a length of 0.");
            return;
        }
        String code = generator(length, possibleCharacters);
        System.out.println("Okay, let's start a game!");
        boolean guessed = false;
        int counter = 1;
        while (!guessed) {
            guessed = turn(code, counter);
            counter += 1;
        }
    }
    private static String generator(int length, int possibleCharacters) {
        StringBuilder code = new StringBuilder();
        Random random = new Random();
        while (code.length() < length) {
            int randomInt = random.nextInt(possibleCharacters);
            String randomChar = randomInt > 10 ? Character.toString((char) randomInt + 86) : Integer.toString(randomInt);
                if (!code.toString().contains(randomChar)) {
                    code.append(randomChar);
                }
        }
        String starString = "*".repeat(length);
        String possibleCharDescription;
        if (possibleCharacters > 10) {
            String lastChar = Character.toString((char) possibleCharacters + 86);
            possibleCharDescription = "(0-9, a-" + lastChar + ")";
        } else {
            possibleCharDescription = "(0-9)";
        }
        System.out.println("The secret is prepared: " + starString + " " + possibleCharDescription + ".");
        return code.toString();
    }

    private static boolean turn(String code, int counter) {
        System.out.println("Turn " + counter + ":");
        String guess = scanner.next();
        return grader(code, guess);
    }
    private static boolean grader(String codeString, String guessString) {
        int bulls = 0;
        int cows = 0;
        for (int i = 0; i < codeString.length(); i++) {
            try {
                if (codeString.charAt(i) == guessString.charAt(i)) {
                    bulls += 1;
                } else if (guessString.contains(Character.toString(codeString.charAt(i)))) {
                    cows += 1;
                }
            } catch (Exception e) {
                System.out.println("aaaa");
            }

        }
        if (bulls == 0 && cows == 0) {
            System.out.println("Grade: None.");
        } else if (bulls == codeString.length()) {
            System.out.println("Grade: " + codeString.length() + " bulls");
            System.out.println("Congratulations! You guessed the secret code.");
            return true;
        } else {
            System.out.println("Grade: " + bulls + " bull(s) and " + cows + " cow(s).");
        }
        return false;
    }
}
