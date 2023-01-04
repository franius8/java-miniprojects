import javax.swing.*;
import java.awt.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calculator extends JFrame {

    Changer changer;
    public Calculator() {
        super("Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(250, 360);
        setLayout(null);
        JLabel EquationLabel = new JLabel();
        EquationLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        EquationLabel.setName("EquationLabel");
        EquationLabel.setBounds(25,10, 200,30);
        add(EquationLabel);
        JLabel ResultLabel = new JLabel();
        ResultLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        ResultLabel.setForeground(new Color(20, 121, 29));
        ResultLabel.setName("ResultLabel");
        ResultLabel.setBounds(25, 40, 200, 30);
        add(ResultLabel);
        JButton Parentheses = new JButton();
        Parentheses.setName("Parentheses");
        Parentheses.setBounds(25, 70, 50, 40);
        Parentheses.setText("( )");
        Parentheses.addActionListener(e -> EquationLabel.setText(addParentheses(EquationLabel.getText())));
        add(Parentheses);
        JButton Clear = new JButton();
        Clear.setName("Clear");
        Clear.setBounds(125, 70, 50, 40);
        Clear.setText("C");
        Clear.addActionListener(e -> {
            EquationLabel.setText("");
            changer.toBlack();
        });
        add(Clear);
        JButton Delete = new JButton();
        Delete.setName("Delete");
        Delete.setBounds(175, 70, 50, 40);
        Delete.setText("Del");
        Delete.addActionListener(e -> EquationLabel.setText(EquationLabel.getText()
                .substring(0, EquationLabel.getText().length() - 1)));
        add(Delete);
        JButton Square = new JButton();
        Square.setName("PowerTwo");
        Square.setBounds(25, 110, 50,40);
        Square.setText("x^2");
        Square.addActionListener(e -> EquationLabel.setText(EquationLabel.getText() + "^(2)"));
        add(Square);
        JButton PowerY = new JButton();
        PowerY.setName("PowerY");
        PowerY.setBounds(75, 110, 50, 40);
        PowerY.setText("x^y");
        PowerY.addActionListener(e -> EquationLabel.setText(EquationLabel.getText() + "^("));
        add(PowerY);
        JButton SquareRoot = new JButton();
        SquareRoot.setName("SquareRoot");
        SquareRoot.setBounds(125, 110, 50, 40);
        SquareRoot.setText("\u221A");
        SquareRoot.addActionListener(e -> EquationLabel.setText(EquationLabel.getText() + "√("));
        add(SquareRoot);
        JButton Divide = new JButton();
        Divide.setName("Divide");
        Divide.setBounds(175, 110, 50, 40);
        Divide.setText("\u00F7");
        Divide.addActionListener(e -> EquationLabel.setText(addOperator("\u00F7", EquationLabel.getText())));
        add(Divide);
        JButton Seven = new JButton();
        Seven.setName("Seven");
        Seven.setBounds(25, 150, 50, 40);
        Seven.setText("7");
        Seven.addActionListener(e -> EquationLabel.setText(EquationLabel.getText() + "7"));
        add(Seven);
        JButton Eight = new JButton();
        Eight.setName("Eight");
        Eight.setBounds(75, 150, 50, 40);
        Eight.setText("8");
        Eight.addActionListener(e -> EquationLabel.setText(EquationLabel.getText() + "8"));
        add(Eight);
        JButton Nine = new JButton();
        Nine.setName("Nine");
        Nine.setBounds(125, 150, 50, 40);
        Nine.setText("9");
        Nine.addActionListener(e -> EquationLabel.setText(EquationLabel.getText() + "9"));
        add(Nine);
        JButton Multiply = new JButton();
        Multiply.setName("Multiply");
        Multiply.setBounds(175, 150, 50, 40);
        Multiply.setText("\u00D7");
        Multiply.addActionListener(e -> EquationLabel.setText(addOperator("\u00D7", EquationLabel.getText())));
        add(Multiply);
        JButton Four = new JButton();
        Four.setName("Four");
        Four.setBounds(25, 190, 50, 40);
        Four.setText("4");
        Four.addActionListener(e -> EquationLabel.setText(EquationLabel.getText() + "4"));
        add(Four);
        JButton Five = new JButton();
        Five.setName("Five");
        Five.setBounds(75, 190, 50, 40);
        Five.setText("5");
        Five.addActionListener(e -> EquationLabel.setText(EquationLabel.getText() + "5"));
        add(Five);
        JButton Six = new JButton();
        Six.setName("Six");
        Six.setBounds(125, 190, 50, 40);
        Six.setText("6");
        Six.addActionListener(e -> EquationLabel.setText(EquationLabel.getText() + "6"));
        add(Six);
        JButton Subtract = new JButton();
        Subtract.setName("Subtract");
        Subtract.setBounds(175, 190, 50, 40);
        Subtract.setText("-");
        Subtract.addActionListener(e -> EquationLabel.setText(addOperator("-", EquationLabel.getText())));
        add(Subtract);
        JButton One = new JButton();
        One.setName("One");
        One.setBounds(25, 230, 50, 40);
        One.setText("1");
        One.addActionListener(e -> EquationLabel.setText(EquationLabel.getText() + "1"));
        add(One);
        JButton Two = new JButton();
        Two.setName("Two");
        Two.setBounds(75, 230, 50, 40);
        Two.setText("2");
        Two.addActionListener(e -> EquationLabel.setText(EquationLabel.getText() + "2"));
        add(Two);
        JButton Three = new JButton();
        Three.setName("Three");
        Three.setBounds(125, 230, 50, 40);
        Three.setText("3");
        Three.addActionListener(e -> EquationLabel.setText(EquationLabel.getText() + "3"));
        add(Three);
        JButton Add = new JButton();
        Add.setName("Add");
        Add.setBounds(175, 230, 50, 40);
        Add.setText("\u002B");
        Add.addActionListener(e -> EquationLabel.setText(addOperator("\u002B", EquationLabel.getText())));
        add(Add);
        JButton PlusMinus = new JButton();
        PlusMinus.setName("PlusMinus");
        PlusMinus.setBounds(25, 270, 50, 40);
        PlusMinus.setText("±");
        PlusMinus.addActionListener(e -> EquationLabel.setText(negate(EquationLabel.getText())));
        add(PlusMinus);
        JButton Zero = new JButton();
        Zero.setName("Zero");
        Zero.setBounds(75, 270, 50, 40);
        Zero.setText("0");
        Zero.addActionListener(e -> EquationLabel.setText(EquationLabel.getText() + "0"));
        add(Zero);
        JButton Dot = new JButton();
        Dot.setName("Dot");
        Dot.setBounds(125, 270, 50, 40);
        Dot.setText(".");
        Dot.addActionListener(e -> EquationLabel.setText(EquationLabel.getText() + "."));
        add(Dot);
        JButton Equals = new JButton();
        Equals.setName("Equals");
        Equals.setBounds(175, 270, 50, 40);
        Equals.setText("=");
        Equals.addActionListener(e -> ResultLabel.setText(solve(EquationLabel.getText())));
        add(Equals);
        setVisible(true);
        changer = new Changer(EquationLabel);
    }

    private String addOperator(String operator, String EquationLabelText) {
        String lastChar;
        try {
            lastChar = EquationLabelText.substring(EquationLabelText.length() - 1);
        } catch (Exception e) {
            return EquationLabelText;
        }
        if (lastChar.matches("[\u002B\u00D7\u00F7^-]")) {
            return EquationLabelText.substring(0, EquationLabelText.length() - 1) + operator;
        } else {
            if (EquationLabelText.charAt(0) == '.') {
                return "0" + EquationLabelText + operator;
            } else if (lastChar.equals(".")) {
                return EquationLabelText + "0" + operator;
            } else {
                return EquationLabelText + operator;
            }
        }
    }

    private String addParentheses(String EquationLabelText) {
        Pattern leftPattern = Pattern.compile("[(]");
        Matcher leftMatcher = leftPattern.matcher(EquationLabelText);
        Pattern rightPattern = Pattern.compile("[)]");
        Matcher rightMatcher = rightPattern.matcher(EquationLabelText);
        if (leftMatcher.results().count() == rightMatcher.results().count()) {
            return EquationLabelText + "(";
        } else {
            String lastChar = EquationLabelText.substring(EquationLabelText.length() - 1);
            if (lastChar.equals("(") || lastChar.matches("[\u002B\u00D7\u00F7-]")) {
                return EquationLabelText + "(";
            } else {
                return EquationLabelText + ")";
            }
        }
    }

    private String negate(String EquationLabelText) {
        if (EquationLabelText.startsWith("(-")) {
            return EquationLabelText.substring(2);
        } else {
            return "(-" + EquationLabelText;
        }
    }
    private String solve(String eq) {
        String lastChar = eq.substring(eq.length() - 1);
        if (lastChar.matches("[\u002B\u00D7\u00F7-]")) {
            changer.toRed();
            return "";
        }
        double result = solvePostFix(toPostFix(eq));
        if (result == (int) result ) {
            return Integer.toString((int) result);
        } else {
            return Double.toString(result);
        }
    }

    private ArrayList<String> toPostFix (String eq) {
        ArrayList<String> postFixResult = new ArrayList<>();
        Deque<String> stack = new ArrayDeque<>();
        String[] splitEq = eq.split("");
        StringBuilder number = new StringBuilder();
        for (String element: splitEq) {
            if (!number.toString().equals("") && !element.matches("[.0-9]+")) {
                postFixResult.add(number.toString());
                number = new StringBuilder();
            } else if (element.matches("[.0-9]+")) {
                number.append(element);
                continue;
            }
             if (element.equals(")")) {
                while (true) {
                    assert stack.peek() != null;
                    if (stack.peek().equals("(")) {
                        stack.removeFirst();
                        break;
                    }
                    postFixResult.add(stack.removeFirst());
                }
            } else if (stack.isEmpty() || stack.peek().equals("(") || element.equals("(") ||
                     precedence(element) > precedence(stack.peek())) {
                 stack.addFirst(element);
             } else {
                while (true) {
                    if (stack.isEmpty() || stack.peek().equals("(") || precedence(stack.peek()) < precedence(element)) {
                        break;
                    }
                    postFixResult.add(stack.removeFirst());
                }
                stack.addFirst(element);
            }
        }
        if (!number.toString().equals("")) {
            postFixResult.add(number.toString());
        }
        while(!stack.isEmpty()) {
            postFixResult.add(stack.removeFirst());
        }
        return postFixResult;
    }
    private double solvePostFix(ArrayList<String> eq) {
        Deque<String> stack = new ArrayDeque<>();
        for (String element: eq) {
            if (element.matches("-?[0-9.]+")) {
                stack.addFirst(element);
            } else if (element.equals("\u221A")) {
                double first = Double.parseDouble(stack.removeFirst());
                if (first < 0) {
                    changer.toRed();
                    return 0;
                }
                stack.addFirst(Double.toString(Math.sqrt(first)));
            } else if (element.equals("-") && stack.size() == 1) {
                stack.addFirst("-" + stack.removeFirst());
            } else {
                double second = Double.parseDouble(stack.removeFirst());
                double first = Double.parseDouble(stack.removeFirst());
                switch (element) {
                    case "\u002B" -> stack.addFirst(Double.toString(first + second));
                    case "-" -> stack.addFirst(Double.toString(first - second));
                    case "\u00D7" -> stack.addFirst(Double.toString(first * second));
                    case "\u00F7" -> {
                        if (second == 0) {
                            changer.toRed();
                            return 0;
                        }
                        stack.addFirst(Double.toString(first / second));
                    }
                    case "^" -> stack.addFirst(Double.toString(Math.pow(first, second)));
                }
            }
        }
        assert stack.peek() != null;
        return Double.parseDouble(stack.peek());
    }
    private int precedence(String operator) {
        return switch (operator) {
            case "\u221A", "^" -> 2;
            case "\u00D7", "\u00F7" -> 1;
            default -> 0;
        };
    }
}

class Changer {
    private final JLabel EquationLabel;

    public Changer(JLabel EquationLabel) {
        this.EquationLabel = EquationLabel;
    }

    public void toRed() {
        EquationLabel.setForeground(Color.RED.darker());
    }

    public void toBlack() {
        EquationLabel.setForeground(Color.BLACK);
    }
}
