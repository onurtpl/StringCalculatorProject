public class App {
    private static boolean isNumber(char ch) {
        char[] numbers = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
        for (char number : numbers) {
            if (number == ch)
                return true;
        }
        return false;
    }

    private static char[] getOperators(String str) {
        char[] template = new char[str.length()];
        int index = 0;
        for (char ch : str.toCharArray()) {
            if (!isNumber(ch)) {
                template[index] = ch;
                index++;
            }
        }

        char[] operators = new char[index];
        for (int i = 0; i < operators.length; i++) {
            operators[i] = template[i];
        }

        return operators;
    }

    private static int[] getNumbers(String str) {
        int[] template = new int[str.length()];
        int index = 0;
        String buffer = new String();
        for (char ch : str.toCharArray()) {
            if (isNumber(ch)) {
                buffer += ch;
            } else {
                template[index] = Integer.parseInt(buffer);
                buffer = new String();
                index++;
            }
        }
        template[index] = Integer.parseInt(buffer);
        index++;

        int[] numbers = new int[index];
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = template[i];
        }
        return numbers;
    }

    private static int[] deleteAndInsertNumber(int[] numbers, int index, int newValue) {

        int[] result = new int[numbers.length - 1];
        for (int i = 0; i < result.length; i++) {
            if (i < index) {
                result[i] = numbers[i];
            } else if (i == index) {
                result[i] = newValue;
            } else {
                result[i] = numbers[i + 1];
            }
        }
        return result;
    }

    private static char[] deleteCharArray(char[] chars, int index) {
        char[] template = new char[chars.length - 1];
        for (int i = 0; i < template.length; i++) {
            if (i < index) {
                template[i] = chars[i];
            } else {
                template[i] = chars[i + 1];
            }
        }
        return template;
    }

    private static char[] insertIndexToCharArray(char[] chars, int index, char ch) {
        char[] newChars = new char[chars.length + 1];

        for (int i = 0; i < newChars.length; i++) {
            if (i < index) {
                newChars[i] = chars[i];
            } else if (i == index) {
                newChars[i] = ch;
            } else {
                newChars[i] = chars[i - 1];
            }
        }

        return newChars;
    }

    private static String formatter(String str) {
        // trim spaces
        str = str.replaceAll("\\s+", "");
        // ilk de??er negatif mi?
        String template;
        if (str.charAt(0) == '-')
            template = new String("0" + str);
        else
            template = new String(str);

        char[] chars = template.toCharArray();
        int charIndex = 0;
        // ??arpma i??aret kontrol
        for (int i = 0; i < template.length(); i++, charIndex++) {
            char ch = template.charAt(i);
            if (isNumber(ch)) {
                if (i < template.length() - 1) {
                    char nextChar = template.charAt(i + 1);
                    if (nextChar == '(') {
                        // chars = new char[chars.length + 1];
                        charIndex++;
                        chars = insertIndexToCharArray(chars, charIndex, '*');
                    }
                }
            }
            else if (ch == ')') {
                if (i < template.length() - 1) {
                    char nextChar = template.charAt(i + 1);
                    if (nextChar == '(') {
                        // chars = new char[chars.length + 1];
                        charIndex++;
                        chars = insertIndexToCharArray(chars, charIndex, '*');
                    }
                }
            }
        }
        return String.valueOf(chars);
    }

    private static int getMultiplicationOrDivider(char[] operators) {
        for (int i = 0; i < operators.length; i++) {
            if (operators[i] == '*' || operators[i] == '/') {
                return i;
            }
        }
        return -1;
    }

    private static int calculation(int[] numbers, char[] operators) {
        int result;
        // 34+12*5 stringinden elde edilen operators dizisindeki elemanlar t??kenene kadar i??leme devam edilecektir
        while (true) {
            int multDivIndex = getMultiplicationOrDivider(operators);
            // i??lem ??nceli??i ??arpma ve b??lmede. ifadede ??arpma veya b??lme i??lemi var m?? kontrol edilir
            if (multDivIndex >= 0) {
                int template;
                if (operators[multDivIndex] == '*') {
                    template = numbers[multDivIndex] * numbers[multDivIndex + 1];
                } else {
                    template = numbers[multDivIndex] / numbers[multDivIndex + 1];
                }
                numbers = deleteAndInsertNumber(numbers, multDivIndex, template);
                if (operators.length > 1) {
                    operators = deleteCharArray(operators, multDivIndex);
                } else {
                    result = template;
                    break;
                }
            }
            // sadece toplama ve ????karma i??emleri i??eren ifadede i??lemler index s??ralams??na g??re yap??l??r
            else {
                int template;
                if (operators[0] == '+') {
                    template = numbers[0] + numbers[1];
                } else {
                    template = numbers[0] - numbers[1];
                }
                numbers = deleteAndInsertNumber(numbers, 0, template);
                if (operators.length > 1) {
                    operators = deleteCharArray(operators, 0);
                } else {
                    result = template;
                    break;
                }
            }
        }
        return result;
    }

    private static char[] editCharArray(char[] chars, int index1, int index2, String newStr) {
        String charStr = String.valueOf(chars);

        String str1 = charStr.substring(0, index1);
        String str2 = charStr.substring(index2 + 1);
        String result = str1 + newStr + str2;
        return result.toCharArray();
    }

    private static String extractBrackets(String str) {
        char[] chars = str.toCharArray();
        while(true) {
            int bracketIndex1 = -1;
            int bracketIndex2 = -1;
            for(int i = 0; i < chars.length; i++) {
                if(chars[i] == '(')
                    bracketIndex1 = i;
                else if(chars[i] == ')') {
                    bracketIndex2 = i;
                    break;
                }
            }
            if(bracketIndex2>0) {
                char[] bracketChars = new char[bracketIndex2 - bracketIndex1 - 1];
                int index = 0;
                for(int i = bracketIndex1 + 1; i < bracketIndex2; i++, index++) {
                    bracketChars[index] = chars[i];
                }
                String bracketStr = String.valueOf(bracketChars);
                int[] numbers = getNumbers(bracketStr);
                char[] operators = getOperators(bracketStr);
                if(operators.length > 0) {
                    String result = String.valueOf(calculation(numbers, operators));
                    chars = editCharArray(chars, bracketIndex1, bracketIndex2, result);
                } else {
                    String result = String.valueOf(numbers[0]);
                    chars = editCharArray(chars, bracketIndex1, bracketIndex2, result);
                }
            }
            else break;
        }
        return String.valueOf(chars);
    }

    private static String getStringCalculator(String str) {
        // Gelen stringi belli bir formata sok
        // 3(9+12) -> 3*(9+12)
        // (3+5)(5+3) -> (3+5)*(5+3)
        // -12+5 -> 0-12+5
        // bo??luklar?? temizle
        String formatStr = formatter(str);
        // i??lem ??nceli??i parantezde. ??nce parantezlerdeki i??lemleri tamamla
        String noneBrackets = extractBrackets(formatStr);
        // parantezsiz verilerin i??lemine devam et -> 3+5-14/2 gibi
        // say??lar?? ve operat??rleri ayr?? dizilerde tut
        int[] numbers = getNumbers(noneBrackets);
        /*
            d??zenlenmi?? string 34+12*5 vb formatta olaca????ndan say?? dizisi operator dizisinden 1 adet fazla olacakt??r.
            ifadede i??lem ??nceli??i * i??leminde ve operator dizisindeki indeksi 1.
            say??s?? dizisinde index 1 de say?? 12 ve bir sonraki indexde 5
            12*5=60 sonucu bulunur ve string 12+60 olarak tekrar d??zenlenir
            Bu d??ng?? operator dizisi bo?? olana kadar devam eder ve sonu?? elde edilir.
        */
        char[] operators = getOperators(noneBrackets);
        int result = calculation(numbers, operators);
        return String.valueOf(result);
    }
    public static void main(String[] args) {

        String res = getStringCalculator("8-7*(12+100/2)*9-2");
        System.out.println(res);
    }
}
