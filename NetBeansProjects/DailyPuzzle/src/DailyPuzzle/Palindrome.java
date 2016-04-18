/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DailyPuzzle;

import java.util.Scanner;

/**
 *
 * @author Nick Villano
 */
public class Palindrome {

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        boolean going = true;
        while (going) {
            System.out.print("Enter a word or phrase: ");
            String word = s.nextLine();
            boolean palidrome = true;
            for (int i = 0; i < word.length(); i++) {
                if (word.charAt(i) != word.charAt(word.length() - 1 - i)) {
                    palidrome = false;
                }
            }
            if (palidrome) {
                System.out.println("\"" + word + "\" " + "is a palidrome.");
            } else {
                System.out.println("\"" + word + "\" " + "is not a palidrome.");
            }
            System.out.print("Test another? ");
            if (!"y".equals(s.next())) {
                going = false;
            }
        }
    }
}
