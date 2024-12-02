package com.scanner.project;
// Implementation of the Scanner for KAY
// Updates are made to complete the lexical analyzer for the KAY language.

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class TokenStream {
    private boolean isEof = false; 
    private char nextChar = ' '; 
    private BufferedReader input;

    public boolean isEoFile() {
        return isEof;
    }

    public TokenStream(String fileName) {
        try {
            input = new BufferedReader(new FileReader(fileName));
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + fileName);
            isEof = true;
        }
    }

    public Token nextToken() {
		Token t = new Token();
		t.setType("Other");
		t.setValue("");
	
		skipWhiteSpace();
	
		while (nextChar == '/') {
			nextChar = readChar();
			if (nextChar == '/') {
				while (!isEndOfLine(nextChar) && !isEof) {
					nextChar = readChar();
				}
				skipWhiteSpace();
			} else {
				t.setValue("/");
				t.setType("Operator");
				return t;
			}
		}
	
		if (isOperator(nextChar)) {
			t.setType("Operator");
			t.setValue(t.getValue() + nextChar);
			switch (nextChar) {
				case '<':
				case '>':
				case '!':
					nextChar = readChar();
					if (nextChar == '=') {
						t.setValue(t.getValue() + nextChar);
						nextChar = readChar();
					}
					return t;
	
				case '=':
					nextChar = readChar();
					if (nextChar == '=') {
						t.setValue(t.getValue() + nextChar);
						nextChar = readChar();
						return t; // Double equals is Operator
					} else {
						t.setType("Other"); // Single equals is Other
						return t;
					}
	
				default:
					nextChar = readChar();
					return t;
			}
		}
	
		if (isSeparator(nextChar)) {
			t.setType("Separator");
			t.setValue(t.getValue() + nextChar);
			nextChar = readChar();
			return t;
		}
	
		if (isLetter(nextChar)) {
			t.setType("Identifier");
			while (isLetter(nextChar) || isDigit(nextChar)) {
				t.setValue(t.getValue() + nextChar);
				nextChar = readChar();
			}
			if (isKeyword(t.getValue())) {
				t.setType("Keyword");
			} else if (t.getValue().equals("true") || t.getValue().equals("false")) {
				t.setType("Literal");
			} else if (t.getValue().equals("True") || t.getValue().equals("False")) {
				t.setType("Literal");
			}
			return t;
		}
	
		if (isDigit(nextChar)) {
			t.setType("Literal");
			while (isDigit(nextChar)) {
				t.setValue(t.getValue() + nextChar);
				nextChar = readChar();
			}
			if (nextChar == '.') {
				t.setType("Other"); // Numbers with periods are invalid
				while (!isWhiteSpace(nextChar) && !isEof) {
					t.setValue(t.getValue() + nextChar);
					nextChar = readChar();
				}
			} else if (isLetter(nextChar)) {
				t.setType("Other"); // Invalid sequence like 3aaa2
				while (!isWhiteSpace(nextChar) && !isSeparator(nextChar) && !isOperator(nextChar) && !isEof) {
					t.setValue(t.getValue() + nextChar);
					nextChar = readChar();
				}
			}
			return t;
		}
	
		if (isEof) {
			return t;
		}
	
		while (!isEndOfToken(nextChar)) {
			t.setValue(t.getValue() + nextChar);
			nextChar = readChar();
		}
	
		skipWhiteSpace();
		return t;
	}
	

    private char readChar() {
        int i = 0;
        if (isEof) return (char) 0;
        try {
            i = input.read();
        } catch (IOException e) {
            System.exit(-1);
        }
        if (i == -1) {
            isEof = true;
            return (char) 0;
        }
        return (char) i;
    }

    private boolean isKeyword(String s) {
        return s.equals("if") || s.equals("else") || s.equals("while") ||
               s.equals("integer") || s.equals("bool") || s.equals("main");
    }

    private boolean isWhiteSpace(char c) {
        return c == ' ' || c == '\t' || c == '\r' || c == '\n' || c == '\f';
    }

    private boolean isEndOfLine(char c) {
        return c == '\r' || c == '\n' || c == '\f';
    }

    private boolean isEndOfToken(char c) {
        return isWhiteSpace(nextChar) || isOperator(nextChar) || isSeparator(nextChar) || isEof;
    }

    private void skipWhiteSpace() {
        while (!isEof && isWhiteSpace(nextChar)) {
            nextChar = readChar();
        }
    }

    private boolean isSeparator(char c) {
        return c == '(' || c == ')' || c == '{' || c == '}' || c == ',' || c == ';';
    }

    private boolean isOperator(char c) {
        return "!@#$%^&*-+=~<>?/|:".indexOf(c) != -1;
    }

    private boolean isLetter(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }
}

// Top-level main class
class Main {
    public static void main(String[] args) {
        // Specify the file path of your input file
        String fileName = "/Users/norm/Downloads/KAYCS361_611.txt";

        // Create a TokenStream instance with the file path
        TokenStream ts = new TokenStream(fileName);

        // Use the TokenStream to process the file
        System.out.println("Reading tokens from: " + fileName);
        while (!ts.isEoFile()) {
            Token token = ts.nextToken();
            System.out.println("Type: " + token.getType() + ", Value: " + token.getValue());
        }
    }
}
