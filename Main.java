package readability;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {
    public static int returnYears(double score) {
        switch ((int) Math.ceil(score)) {
            case 1:
                return 6;
            case 2:
                return 7;
            case 3:
                return 9;
            case 4:
                return 10;
            case 5:
                return 11;
            case 6:
                return 12;
            case 7:
                return 13;
            case 8:
                return 14;
            case 9:
                return 15;
            case 10:
                return 16;
            case 11:
                return 17;
            case 12:
                return 18;
            case 13:
                return 24;
            case 14:
                return 24;
            default:
                return 0;
        }
    }
    public static String readFileAsString(String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(fileName)));
    }

    public static int automatedReadabilityIndex(double characters, double words, double sentences) {
        double score = 4.71 * characters / words + 0.5 * words / sentences - 21.43;
        System.out.printf("Automated Readability Index: %.2f (about %d-year-olds).\n", score, returnYears(score));
        return returnYears(score);
    }

    public static int fleschKincardTest(double words, double sentences, double syllables) {
        double score = 0.39 * words / sentences + 11.8 * syllables / words - 15.59;
        System.out.printf("Flesch–Kincaid readability tests: %.2f (about %d-year-olds).\n", score, returnYears(score));
        return returnYears(score);
    }

    public static int smogIndex(double polysyllables, double sentences) {
        double score = 1.043 * Math.sqrt(polysyllables * 30 / sentences) + 3.1291;
        System.out.printf("Simple Measure of Gobbledygook: %.2f (about %d-year-olds).\n", score, returnYears(score));
        return returnYears(score);
    }

    public static int colemanLiauIndex(double L, double S) {
        double score = 0.0588 * L - 0.296 * S - 15.8;
        System.out.printf("Coleman–Liau index: %.2f (about %d-year-olds).\n", score, returnYears(score));
        return returnYears(score);
    }

    public static double calculateCharacters(String userText) {
        String[] wordsArr = userText.split("\\s+");
        double characters = 0;
        for (int i = 0; i < wordsArr.length; i++) {
            char[] wordInLetters = wordsArr[i].toCharArray();
            characters += wordInLetters.length;
        }
        return characters;
    }
    public static double vowels(String words) {
        double count = 0;
        for (int i = 0; i < words.length(); i++) {
            if (words.charAt(i) == 'a') {
                count++;
            }
        }
        if (count == 0) {
            return 1;
        } else {
            return count;
        }
    }
    public static double[] calculateSyllables(String[] wordsArr) {
        double[] syllables = new double[2];
        String res = "";
        for (String s : wordsArr) {
            if (s.endsWith("e") || s.endsWith("E")) {
                res = s.substring(0 , s.length() - 1);
            } else {
                res = s;
            }
            if (vowels(res.replaceAll("[aeiuoyAEIOUY]+", "a")) > 2) {
                syllables[1]++;
            }
            syllables[0] += vowels(res.replaceAll("[aeiuoyAEIOUY]+", "a"));

        }
        return syllables;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
       String userText = "";
        try {
            userText  = readFileAsString(args[0]);
        } catch (Exception e) {
            System.out.print("Error");
        }
        String[] wordsArr = userText.split("\\s+");
        double sentences = userText.split("\\.|\\?|!").length;
        double words = wordsArr.length;
        double characters =  calculateCharacters(userText);
        double syllables = calculateSyllables(wordsArr)[0];
        double polysyllables = calculateSyllables(wordsArr)[1];

        System.out.println("The text is:");
        System.out.println(userText);
        System.out.printf("Words: %.2f\n", words);
        System.out.printf("Sentences: %.2f\n", sentences);
        System.out.printf("Characters: %.2f\n", characters);
        System.out.printf("Syllables: %.2f\n", syllables);
        System.out.printf("Polysyllables: %.2f\n", polysyllables);

        System.out.println("Enter the score you want to calculate (ARI, FK, SMOG, CL, all): ");
        String index = scanner.next();
        int k = 0;
        switch (index) {
            case "ARI":
                k = automatedReadabilityIndex(characters, words, sentences);
                System.out.printf("This text should be understood in average by %d-year-olds.", k);
                break;
            case "FK":
                k = fleschKincardTest(words, sentences, syllables);
                System.out.printf("This text should be understood in average by %d-year-olds.", k);
                break;
            case "SMOG":
                k = smogIndex(polysyllables, sentences);
                System.out.printf("This text should be understood in average by %d-year-olds.", k);
                break;
            case "CL":
                k = colemanLiauIndex(characters / words * 100, sentences / words * 100);
                System.out.printf("This text should be understood in average by %d-year-olds.", k);
                break;
            case "all":
                k += automatedReadabilityIndex(characters, words, sentences);
                k += fleschKincardTest(words, sentences, syllables);
                k += smogIndex(polysyllables, sentences);
                k += colemanLiauIndex(characters / words * 100, sentences / words * 100);
                System.out.printf("This text should be understood in average by %d-year-olds.", k);
                break;
            default:
                break;
        }
    }
}
