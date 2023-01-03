import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Scanner;
import java.math.BigInteger;
import java.math.BigDecimal;

public class Numberconverter {

    static Scanner scanner = new Scanner(System.in);
    final static BigDecimal fiftyFiveDec = new BigDecimal("55");
    final static BigInteger fiftyFive = new BigInteger("55");

    public static void main(String[] args) {
        while (true) {
            System.out.println("Enter two numbers in format: {source base} {target base} (To quit type /exit)");
            String choice = scanner.nextLine();
            if (choice.equals("/exit")) break;
            if (choice.equals("")) continue;
            int sourceBase = Integer.parseInt(choice.split(" ")[0]);
            int targetBase = Integer.parseInt(choice.split(" ")[1]);
            converter(sourceBase, targetBase);
        }
    }

    private static void converter(int sourceBase, int targetBase) {
        while (true) {
            System.out.println("Enter number in base " + sourceBase + " to convert to base " + targetBase +
                    " (To go back type /back)");
            String source = scanner.nextLine().replace("\n", "");

            if (source.equals("/back")) return;
            if (sourceBase == 10) {
                System.out.println("Conversion result: " + fromDecimal(source, targetBase));
            } else if (targetBase == 10) {
                System.out.println("Conversion result: " + toDecimal(source, sourceBase));
            } else {
                BigDecimal middle = toDecimal(source, sourceBase);
                String result = fromDecimal(middle.toString(), targetBase);
                if (source.contains(".") && !result.contains(".")) {
                    System.out.println("Conversion result: " + result + ".00000");
                } else {
                    if (result.contains(".")) {
                        result = result.split("[.]")[0] + "." + padRightZeros(result.split("[.]")[1]);
                    }
                    System.out.println("Conversion result: " + result);
                }
            }
        }
    }
    private static String fromDecimal(String numberString, int base) {
        BigDecimal number = new BigDecimal(numberString);
        BigInteger numberIntPart = number.toBigInteger();
        BigDecimal numberFracPart = number.divideAndRemainder(BigDecimal.ONE)[1];
        StringBuilder reverseResult = new StringBuilder();
        BigInteger baseBigInteger = new BigInteger(String.valueOf(base));
        if (numberIntPart.equals(BigInteger.ZERO)) {
            reverseResult.append("0");
        } else {
            while (numberIntPart.compareTo(BigInteger.ONE) >= 0) {
                if (numberIntPart.mod(baseBigInteger).compareTo(BigInteger.TEN) < 0) {
                    reverseResult.append(numberIntPart.mod(baseBigInteger));
                } else {
                    reverseResult.append((char) (numberIntPart.mod(baseBigInteger).add(fiftyFive).intValue()));
                }
                numberIntPart = numberIntPart.divide(baseBigInteger);
            }
        }
        String convertedFracPart;
        if (!isIntegerValue(numberFracPart)) {
            convertedFracPart = "." + fromDecimalFracPart(numberFracPart, base);
        } else {
            convertedFracPart = "";
        }
        return reverseResult.reverse().append(convertedFracPart).toString();
    }

    private static String fromDecimalFracPart(BigDecimal number, int base) {
        StringBuilder result = new StringBuilder();
        BigDecimal baseBigDecimal = new BigDecimal(base);
        for (int i = 0; i < 5; i++) {
            BigDecimal[] intAndRemainder = number.multiply(baseBigDecimal).divideAndRemainder(BigDecimal.ONE);
            if (intAndRemainder[0].compareTo(BigDecimal.TEN) < 0) {
                result.append(intAndRemainder[0].toBigInteger());
            } else {
                result.append((char) intAndRemainder[0].add(fiftyFiveDec).intValue());
            }
            if (isIntegerValue(intAndRemainder[1])) break;
            number = intAndRemainder[1];
        }
        return result.toString();
    }
    private static BigDecimal toDecimal(String source, int base) {
        StringBuilder reversedSource;
        StringBuilder fracPart = null;
        if (source.contains(".")) {
            reversedSource = new StringBuilder(source.split("[.]")[0]).reverse();
            fracPart = new StringBuilder(source.split("[.]")[1]);
        } else {
            reversedSource = new StringBuilder(source).reverse();
        }

        BigInteger result = new BigInteger("0");
        for (int i = 0; i < reversedSource.length(); i++) {
            result = result.add(new BigInteger(String.valueOf(
                    Math.round( Character.digit(reversedSource.charAt(i), base) * Math.pow(base, i))
            )));
        }
        BigDecimal decimalResult = new BigDecimal(result);
        if (fracPart != null) {
            decimalResult = decimalResult.add(toDecimalFracPart(fracPart.toString(), base));
        }
        return decimalResult;
    }

    private static BigDecimal toDecimalFracPart(String number, int base) {
        BigDecimal result = new BigDecimal("0");
        BigDecimal bigDecimalBase = new BigDecimal(base);
        for (int i = 0; i < number.length(); i++) {
            result = result.add(new BigDecimal (Character.digit(number.charAt(i), base)).multiply(
                    bigDecimalBase.pow(-(i + 1), new MathContext(5))
            ));
        }
        result = result.setScale(5, RoundingMode.HALF_UP);
        return result;
    }
    private static boolean isIntegerValue(BigDecimal bd) {
        return bd.stripTrailingZeros().scale() <= 0;
    }

    private static String padRightZeros(String str) {
        StringBuilder result = new StringBuilder(str);
        while (result.length() < 5) {
            result.append("0");
        }
        return result.toString();
    }
}
