package com.scanner.project;
// Implementation of the Scanner for KAY


// This code DOES NOT implement a scanner for JAY yet. You have to complete
// the code and also make sure it implements a scanner for JAY - not something
// else.

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class TokenStream {
    
    // READ THE COMPLETE FILE FIRST
	// You will need to adapt it to KAY, NOT JAY
    
    // Instance variables 
    private boolean isEof = false; // is end of file
    private char nextChar = ' '; // next character in input stream
    private BufferedReader input;

    // This function was added to make the demo file work
    public boolean isEoFile() {
        return isEof;
    }

    // Constructor
	// Pass a filename for the program text as a source for the TokenStream.
    public TokenStream(String fileName) {
        try {
            input = new BufferedReader(new FileReader(fileName));
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + fileName);
            // System.exit(1); // Removed to allow ScannerDemo to continue
			// running after the input file is not found.
            isEof = true;
        }
    }

    public Token nextToken() { // Main function of the scanner
                                // Return next token type and value.
        Token t = new Token();
        t.setType("Other"); // For now it is Other
        t.setValue("");
        
        // First check for whitespaces and bypass them
        skipWhiteSpace();
        if (isEof) return null;
        
        // Then check for a comment, and bypass it
		// but remember that / may also be a division operator.
        while (nextChar == '/') {
            // Changed if to while to avoid the 2nd line being printed when
			// there are two comment lines in a row.
            nextChar = readChar();
            if (nextChar == '/') { // If / is followed by another /
                // skip rest of line - it's a comment.
				// TODO TO BE COMPLETED W completed 
                while((int)nextChar!=10&&(int)nextChar!=12&&(int)nextChar!=13) {
                    nextChar = readChar();
                }
                // look for <cr>, <lf>, <ff>
                skipWhiteSpace();
                if(isEof) return null;
            } 
            else {
                // A slash followed by anything else must be an operator.
                t.setValue("/");
                t.setType("Operator");
                return t;
            }
        }
        
        // Then check for an operator; this part of the code should recover 2-character
		// operators as well as 1-character ones.
        if (isOperator(nextChar)) {
            t.setType("Operator");
            t.setValue(t.getValue() + nextChar);
            
            switch (nextChar) {
            // TODO TO BE COMPLETED WHERE NEEDED
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
                    // <=
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
                    // >=
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
                    // ==
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
                    // Look or &&
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
            // TODO TO BE COMPLETED
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
        // check for whitespaces, and bypass them
        while (!isEof && isWhiteSpace(nextChar)) {
            nextChar = readChar();
        }
    }

    private boolean isSeparator(char c) {
        // TODO TO BE COMPLETED
        if (c=='('||c==')'||c=='{'||c=='}'||c==';'||c==',')
		{
			return true;
		}
		return false;
	}

    private boolean isOperator(char c) {
        // Checks for characters that start operators
		// TODO TO BE COMPLETED w completed 
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
        // TODO TO BE COMPLETED
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

