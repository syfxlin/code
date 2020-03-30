package calculatorDemo;

import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Stack;

/**
 * calculatorDemo
 */
public class calculatorDemo {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (in.hasNext()) {
            String str = in.nextLine();
            Stack<String> fix = infixToSuffix(str);
            for (int i = 0; i < fix.size(); i++) {
                System.out.print(fix.get(i));
            }
            System.out.println();
            System.out.println("结果为:" + (int) compute(fix));
        }
        in.close();
    }

    public static Stack<String> infixToSuffix(String s) {
        s = s.replaceAll("\\+", "|+|").replaceAll("-", "|-|").replaceAll("*", "|*|").replaceAll("/", "|/|")
                .replaceAll("\\(", "|(|").replaceAll("\\)", "|)|").replaceAll("\\^", "|^|").replaceAll("\\|\\|", "|");
        s = s.replaceAll("([\\+\\-*/\\^])\\|-\\|", "$1|-");
        String[] arr = s.split("\\|");
        Stack<String> stack1 = new Stack<String>();
        Stack<String> stack2 = new Stack<String>();
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equals(""))
                continue;
            switch (arr[i].charAt(arr[i].length() - 1)) {
                case '-':
                case '+':
                    while (stack1.size() != 0 && stack1.peek().charAt(0) != '(') {
                        stack2.push(stack1.pop());
                    }
                    stack1.push(arr[i]);
                    break;
                case '*':
                case '/':
                    while (stack1.size() != 0 && stack1.peek().charAt(0) != '(' && stack1.peek().charAt(0) != '+'
                            && stack1.peek().charAt(0) != '-') {
                        stack2.push(stack1.pop());
                    }
                    stack1.push(arr[i]);
                    break;
                case '(':
                    stack1.push(arr[i]);
                    break;
                case ')':
                    while (stack1.size() != 0 && stack1.peek().charAt(0) != '(') {
                        stack2.push(stack1.pop());
                    }
                    stack1.pop();
                    break;
                case '^':
                    while (stack1.size() != 0 && stack1.peek().charAt(0) != '(' && stack1.peek().charAt(0) != '*'
                            && stack1.peek().charAt(0) != '/' && stack1.peek().charAt(0) != '+'
                            && stack1.peek().charAt(0) != '-') {
                        stack2.push(stack1.pop());
                    }
                    stack1.push(arr[i]);
                    break;

                default:
                    stack2.push(arr[i]);
                    break;
            }
        }
        while (stack1.size() != 0) {
            stack2.push(stack1.pop());
        }
        return stack2;
    }

    public static Deque<String> prefixToInfix(String s, String split) {
        if (!split.equals("|")) {
            s = s.replaceAll(split, "|");
        }
        String[] arr = s.split("\\|");
        Stack<String> stack1 = new Stack<String>();
        Deque<String> stack2 = new LinkedList<String>();
        int lavel = 2;
        for (int i = arr.length - 1; i >= 0; i--) {
            if (arr[i].equals(""))
                continue;
            switch (arr[i].charAt(arr[i].length() - 1)) {
                case '+':
                case '-':
                    stack2.addLast(stack1.pop());
                    stack2.addLast(arr[i]);
                    stack2.addLast(stack1.pop());
                    lavel = 1;
                    break;
                case '*':
                case '/':
                    if (lavel == 1) {
                        stack2.addFirst("(");
                        stack2.addLast(")");
                    }

                    break;

                default:
                    stack1.push(arr[i]);
                    break;
            }
        }
        return stack2;
    }

    public static double compute(Stack<String> s) {
        Stack<Double> c = new Stack<Double>();
        while (!s.empty()) {
            String curr = s.remove(0);
            if (curr.length() > 1) {
                c.push(Double.parseDouble(curr));
            } else if (curr.charAt(0) == '+') {
                double num2 = c.pop();
                double num1 = c.pop();
                c.push(num1 + num2);
            } else if (curr.charAt(0) == '-') {
                double num2 = c.pop();
                double num1 = c.pop();
                c.push(num1 - num2);
            } else if (curr.charAt(0) == '*') {
                double num2 = c.pop();
                double num1 = c.pop();
                c.push(num1 * num2);
            } else if (curr.charAt(0) == '/') {
                double num2 = c.pop();
                double num1 = c.pop();
                c.push(num1 / num2);
            } else if (curr.charAt(0) == '^') {
                double num2 = c.pop();
                double num1 = c.pop();
                c.push(Math.pow(num1, num2));
            } else {
                c.push(Double.parseDouble(curr));
            }
        }
        return c.pop();
    }

    // TODO: 中缀转前缀，前缀转中缀，后缀转中缀，前缀转后缀，后缀转前缀
}
