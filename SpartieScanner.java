import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpartieScanner {
    private String source;

    private int start = 0;
    private int current = 0;
    private int line = 1;

    private static final Map<String, TokenType> keywords = new HashMap<>();
    static {
        keywords.put("if", TokenType.IF);
        keywords.put("else", TokenType.ELSE);
        keywords.put("for", TokenType.FOR);
        keywords.put("while", TokenType.WHILE);
        keywords.put("true", TokenType.TRUE);
        keywords.put("false", TokenType.FALSE);
        keywords.put("fun", TokenType.FUN);
        keywords.put("return", TokenType.RETURN);
        keywords.put("var", TokenType.VAR);
        keywords.put("print", TokenType.PRINT);
        keywords.put("null", TokenType.NULL);
    }

    public SpartieScanner(String source) {
        this.source = source;
    }

    public List<Token> scan() {
        List<Token> tokens = new ArrayList<>();

        Token token = null;
        while (!isAtEnd() && (token = getNextToken()) != null) {
            if (token.type != TokenType.IGNORE)
                tokens.add(token);
        }

        return tokens;
    }

    private Token getNextToken() {
        Token token = null;

        // Try to get each type of token, starting with a simple token, and getting a
        // little more complex
        token = getSingleCharacterToken();
        if (token == null)
            token = getComparisonToken();
        if (token == null)
            token = getDivideOrComment();
        if (token == null)
            token = getStringToken();
        if (token == null)
            token = getNumericToken();
        if (token == null)
            token = getIdentifierOrReservedWord();
        if (token == null) {
            error(line, String.format("Unexpected character '%c' at %d", source.charAt(current), current));
        }

        return token;
    }

    // TODO: Complete implementation
    private Token getSingleCharacterToken() {

        char nextCharacter = source.charAt(current);

        TokenType type = TokenType.UNDEFINED;

        switch (nextCharacter) {
            case ';':
                type = TokenType.SEMICOLON;
                break;
            case ',':
                type = TokenType.COMMA;
                break;
            // Not handling assign here, since it can be = or == (done in getcomparisontoken)
            case '{':
                type = TokenType.LEFT_BRACE;
                break;
            case '}':
                type = TokenType.RIGHT_BRACE;
                break;
            case '(':
                type = TokenType.LEFT_PAREN;
                break;
            case ')':
                type = TokenType.RIGHT_PAREN;
                break;
            // Not handling divide here since it can be / or // (done in getdivideorcomment)
            case '*':
                type = TokenType.MULTIPLY;
                break;
            case '+':
                type = TokenType.ADD;
                break;
            case '-':
                type = TokenType.SUBTRACT;
                break;
            // Not handling ! here since it can be ! or != (done in getcomparisontoken)


            // have to do whitespace / line handling here 
        }

        if (type != TokenType.UNDEFINED) {
            current++; // Go to next character
            return new Token(type, String.valueOf(nextCharacter), line);
        }
        return null;

    }

    private Token getComparisonToken() {
        char nextCharacter = source.charAt(current);
        TokenType type = TokenType.UNDEFINED;

        switch (nextCharacter) {
            case '=':
                if (examine('=')) {
                    type = TokenType.EQUIVALENT;
                    current += 2; // Advance twice
                } else {
                    type = TokenType.ASSIGN;
                    current++; // Advance once
                }
                break;
            case '!':
                if (examine('=')) {
                    type = TokenType.NOT_EQUAL;
                    current += 2; // Advance twice
                } else {
                    type = TokenType.NOT;
                    current++; // Advance once
                }
                break;
            case '<':
                if (examine('=')) {
                    type = TokenType.LESS_EQUAL;
                    current += 2; // Advance twice
                } else {
                    type = TokenType.LESS_THAN;
                    current++; // Advance once
                }
                break;
            case '>':
                if (examine('=')) {
                    type = TokenType.GREATER_EQUAL;
                    current += 2; // Advance twice
                } else {
                    type = TokenType.GREATER_THAN;
                    current++; // Advance once
                }
                break;
        }
        return null;
    }

    private Token getDivideOrComment() {
        char nextCharacter = source.charAt(current);

        if (nextCharacter == '/') {
            // This is either a comment or a divide
            if (examine('/')) {
                // This is a comment, so keep advancing until you hit the end of the line
                while (!isAtEnd() && source.charAt(current) != '\n') {
                    current++;
                }
                return new Token(TokenType.IGNORE, "", line);
            } else {
                // This is a divide
                current++;
                return new Token(TokenType.DIVIDE, "/", line);
            }
        }
        return null;
    }

    // TODO: Complete implementation
    private Token getStringToken() {
        // Hint: Check if you have a double quote, then keep reading until you hit
        // another double quote
        // But, if you do not hit another double quote, you should report an error
        char nextCharacter = source.charAt(current);

        String string = null;

        return null;
    }

    // TODO: Complete implementation
    private Token getNumericToken() {
        // Hint: Follow similar idea of String, but in this case if it is a digit
        // You should only allow one period in your scanner
        return null;
    }

    // TODO: Complete implementation
    private Token getIdentifierOrReservedWord() {
        // Hint: Assume first it is an identifier and once you capture it, then check if
        // it is a reserved word.
        return null;
    }

    // Helper Methods
    private boolean isDigit(char character) {
        return character >= '0' && character <= '9';
    }

    private boolean isAlpha(char character) {
        return character >= 'a' && character <= 'z' ||
                character >= 'A' && character <= 'Z';
    }

    // This will check if a character is what you expect, if so, it will advance
    // Useful for checking <= or //
    private boolean examine(char expected) {
        if (isAtEnd())
            return false;
        if (source.charAt(current + 1) != expected)
            return false;

        // Otherwise, it matches it, so advance
        return true;
    }

    private boolean isAtEnd() {
        return current >= source.length();
    }

    // Error handling
    private void error(int line, String message) {
        System.err.printf("Error occurred on line %d : %s\n", line, message);
        System.exit(ErrorCode.INTERPRET_ERROR);
    }
}
