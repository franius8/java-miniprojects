import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Readabilityscore {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String text = null;
        try {
            text = new String(Files.readAllBytes(Paths.get(args[0])));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String[] sentences = text.split("[!?.] ");
        int syllables = 0;
        int pollysyllables = 0;

        int wordsNumber = 0;
        for (String sentence: sentences) {
            String[] words = sentence.split(" ");
            wordsNumber += words.length;
            for (String word: words) {
                int wordSyllables = syllableCounter(word);
                syllables += wordSyllables;
                if (wordSyllables > 2) {
                    pollysyllables++;
                }
            }
        }

        int charNumber = text.replaceAll("[ \n\t]", "").length();

        System.out.println("The text is:");
        System.out.println(text);
        System.out.println("Words: " + wordsNumber);
        System.out.println("Sentences: " + sentences.length);
        System.out.println("Characters: " + charNumber);
        System.out.println("Syllables: " + syllables);
        System.out.println("Polysyllables: " + pollysyllables);
        System.out.println("Enter the score you want to calculate (ARI, FK, SMOG, CL, all):");
        switch (scanner.next()) {
            case "ARI" -> displayARI(charNumber, wordsNumber, sentences.length);
            case "FK" -> displayFK(wordsNumber, syllables, sentences.length);
            case "SMOG" -> displaySMOG(pollysyllables, sentences.length);
            case "CL" -> displayCL(charNumber, sentences.length, wordsNumber);
            case "all" -> {
                int ageSum = 0;
                ageSum += displayARI(charNumber, wordsNumber, sentences.length);
                ageSum += displayFK(wordsNumber, syllables, sentences.length);
                ageSum += displaySMOG(pollysyllables, sentences.length);
                ageSum += displayCL(charNumber, sentences.length, wordsNumber);
                double average = (double) ageSum / 4.0;
                System.out.println("This text should be understood in average by " + average + "-year-olds.");
            }
            default -> {
                System.out.println("Invalid choice.");
            }
        }

    }

    private static int syllableCounter(String word) {
        Pattern pattern = Pattern.compile("[^aeioyu][aeioyu]\\B|\\b[aeioyu]|[^aeioyu][aioyu]\\b");
        Matcher matcher = pattern.matcher(word);
        int vowels = (int) matcher.results().count();
        if (vowels == 0) vowels = 1;
        return vowels;
    }

    private static int displayARI(int charNumber, int wordsNumber, int sentencesNumber) {
        double score = 4.71 * ((double) charNumber / wordsNumber)  + 0.5 * ((double) wordsNumber / sentencesNumber) - 21.43;

        int age = getCommonAge(score);

        System.out.println("Automated Readability Index: " + score + " (about " + age + "-year-olds).");

        return age;
    }

    private static int displayFK(int wordsNumber, int syllables, int sentences) {
        double score = 0.39 * ((double) wordsNumber / sentences) + 11.8 * ((double)syllables / wordsNumber) - 15.59;

        int age = getCommonAge(score);

        System.out.println("Flesch–Kincaid readability tests: " + score + " (about " + age + "-year-olds).");

        return age;
    }

    private static int displaySMOG(int polysyllables, int sentences) {
        double score = 1.043 * Math.sqrt((double)polysyllables * ((double)30 / sentences)) + 3.1291;

        int age = getCommonAge(score);

        System.out.println("Simple Measure of Gobbledygook: " + score + " (about " + age + "-year-olds).");

        return age;
    }
    
    private static int displayCL(int charNumber, int sentences, int wordsNumber) {
        double l = (double) charNumber / wordsNumber * 100.0;
        double s = (double) sentences / wordsNumber * 100.0;
        
        double score = 0.0588 * l - 0.296 * s - 15.8;
        
        int age = getCommonAge(score);

        System.out.println("Coleman–Liau index: " + score + " (about " + age + "-year-olds).");

        return age;
    }

    private static int getCommonAge(double score) {
        int roundedScore = (int) Math.ceil(score);
        return switch (roundedScore) {
            case 1 -> 6;
            case 2 -> 7;
            case 3 -> 8;
            case 4 -> 9;
            case 5 -> 10;
            case 6 -> 11;
            case 7 -> 12;
            case 8 -> 13;
            case 9 -> 14;
            case 10 -> 15;
            case 11 -> 16;
            case 12 -> 17;
            case 13 -> 18;
            default -> 22;
        };
    }
}
