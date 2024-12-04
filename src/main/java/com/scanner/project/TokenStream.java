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
        if (isEof) return null;
    
        while (nextChar == '/') {
            nextChar = readChar();
            if (nextChar == '/') {
                while((int)nextChar!=10&&(int)nextChar!=12&&(int)nextChar!=13) {
                    nextChar = readChar();
                }
                skipWhiteSpace();
                if(isEof) return null;
            } 
            else {
                t.setValue("/");
                t.setType("Operator");
                return t;
            }
        }
    
        if (isOperator(nextChar)) {
            t.setType("Operator");
            t.setValue(t.getValue() + nextChar);
            switch (nextChar) {
                case ':':
                    nextChar = readChar();
                    
                    if (nextChar == '=') {
                        t.setValue(t.getValue() + nextChar);
                        nextChar = readChar();
                        return t;
                    } else {
                        t.setType("Other");
                        nextChar=readChar();
                    }
                    return t;  
                case '<':
                    nextChar = readChar();
                    
                    if (nextChar == '=') {
                        t.setValue(t.getValue() + nextChar);
                        nextChar = readChar();
                        return t;
                    } else {
                        t.setValue("<");
                    }
                    return t;  
                case '>':
                    nextChar = readChar();
                    
                    if (nextChar == '=') {
                        t.setValue(t.getValue() + nextChar);
                        nextChar = readChar();
                        return t;
                    } else {
                        t.setValue(">");
                    }
                    return t;
                case '=':
                    nextChar = readChar();
                    
                    if (nextChar == '=') {
                        t.setValue(t.getValue() + nextChar);
                        nextChar = readChar();
                        return t;
                    } else {
                        t.setType("Other");
                    }
                    return t;
                case '!':
                    nextChar = readChar();
                    
                    if (nextChar == '=') {
                        
                        t.setValue(t.getValue() + nextChar);
                        nextChar = readChar();
                        return t;
                    
                    } else {
                        t.setValue("!");
                    }
                    return t;
                case '|':
                    nextChar = readChar();
                    
                    if (nextChar == '|') {
                        t.setValue(t.getValue() + nextChar);
                        nextChar = readChar();
                        return t;
                    
                    } else {
                        t.setType("Other");
                        nextChar=readChar();
                    }
                    return t;
    
                case '&':
                    nextChar = readChar();
                    
                    if (nextChar == '&') {
                        t.setValue(t.getValue() + nextChar);
                        nextChar = readChar();
                        return t;
                    
                    } else {
                        t.setType("Other");
                        nextChar=readChar();
                    }
                    return t;
    
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
            } else if (t.getValue().equals("True") || t.getValue().equals("False")) {
                t.setType("Literal");
            }
            if (isEndOfToken(nextChar)){
                return t;
            }
            
        }
    
        if (isDigit(nextChar)) {
            t.setType("Literal");
            while (isDigit(nextChar)) {
                t.setValue(t.getValue() + nextChar);
                nextChar = readChar();
            }
           if (isEndOfToken(nextChar)) {
            return t;
        }
    }
        
        t.setType("Other");
        
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
        if (isEof) 
            return (char) 0;
        System.out.flush();
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
        if(s.equals("bool")||s.equals("else")||s.equals("if")||s.equals("integer")||s.equals("main")||s.equals("while"))
		{
			return true;
		}
		return false;
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
        if (c=='('||c==')'||c=='{'||c=='}'||c==';'||c==',')
		{
			return true;
		}
		return false;
	}

    private boolean isOperator(char c) {
        if (c=='*'||c=='-'||c=='+'||c=='<'||c=='>'||c=='|'||c=='!'||c=='&'||c=='='||c=='/'||c==':')
		{
			return true;
		}
		return false;
	}

    private boolean isLetter(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    private boolean isDigit(char c) {
        if (c >= '0' && c <= '9')
		{
			return true;
		}
		return false;
	}

	public boolean isEndofFile() {
		return isEof;
	}
}

