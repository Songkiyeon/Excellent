import java.io.*;

public class Lexer {
    private boolean isEof = false;
    private char ch = ' '; 
    private BufferedReader input;
    private String line = "";
    private int lineno = 0;
    private int col = 1;
    private final String letters = "abcdefghijklmnopqrstuvwxyz" + "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final String digits = "0123456789";
    private final char eolnCh = '\n';
    private final char eofCh = '\004';
    
    private int flag = 0;
    private int tokRow;
    private int tokCol;
    private int tokColor;
//    
    public Lexer (String fileName) { // source filename
        try {
            input = new BufferedReader (new FileReader(termPcompiler.extract_excel(fileName)));
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found: " + fileName);
            System.exit(1);
        }
    }
    
    public String getLineNum()
    {
       return ""+lineno;
    }

    private char nextChar() { // Return next char
       if (ch == eofCh) error("Attempt to read past end of file");
        col++;
        if (col >= line.length()) {
            try {
                line = input.readLine( );
            } catch (IOException e) {
                System.err.println(e);
                System.exit(1);
            } // try
            if (line == null) // at end of file
                line = "" + eofCh;
            else {
                //System.out.println(lineno + ":\t" + line);
                lineno++;
                line += eolnCh;
            } // if line
            col = 0;
        } // if col
        return line.charAt(col);
    }

    public Token next( ) { // Return next token
        do {
           if(flag == 0) {
              if (ch == eofCh) return Token.eofTok; // 예외
              tokRow = Integer.parseInt(concat(digits).trim()); ch = nextChar();
              tokCol =  Integer.parseInt(concat(digits)); ch =nextChar();
              tokColor = Integer.parseInt(concat(digits));
              flag = 1;
           }
            if (isLetter(ch)) { // ident or keyword
                String spelling = concat(letters + digits);
                return Token.keyword(spelling, tokRow, tokCol, tokColor);
            } else if (isDigit(ch)) { // int or float literal
                String number = concat(digits);
                if (ch != '.')  // int Literal
                    return Token.mkIntLiteral(number, tokRow, tokCol, tokColor);
                number += concat(digits);
                return Token.mkFloatLiteral(number, tokRow, tokCol, tokColor);
            } else switch (ch) {
            case ' ': case '\t': case '\r': 
                ch = nextChar();
                break;
            case eolnCh:
               flag = 0;
               ch = nextChar();
               break;
               
            case '/':  // divide or comment
                ch = nextChar();
                if (ch != '/')  return makeTok(Token.divideTok, tokRow, tokCol, tokColor);
                // comment
                do {
                    ch = nextChar();
                } while (ch != eolnCh);
                ch = nextChar();
                break;
            
            case '\'':  // char literal
                char ch1 = nextChar();
                check('\'');
            return Token.mkCharLiteral("" + ch1, tokRow, tokCol, tokColor);
         
            case '\"': // string literal
               String r = "";
               do {
                  r += ch;
                  ch = nextChar();
               } while (ch !='\"');
               r = r.substring(1);
               ch = nextChar();
               return Token.mkStringLiteral(r, tokRow, tokCol, tokColor);
         
            case eofCh: return Token.eofTok; // 예외
            case '+': return makeTok(chkOpt('+', Token.plusTok, Token.twoplusTok), tokRow, tokCol, tokColor);
            case '-': return makeTok(chkOpt('+', Token.minusTok, Token.twominusTok), tokRow, tokCol, tokColor);
            case '*': ch = nextChar(); return makeTok(Token.multiplyTok, tokRow, tokCol, tokColor);
            case '(': ch = nextChar(); return makeTok(Token.leftParenTok, tokRow, tokCol, tokColor);
            case ')': ch = nextChar(); return makeTok(Token.rightParenTok, tokRow, tokCol, tokColor);
            case '[': ch = nextChar(); return makeTok(Token.leftBracketTok, tokRow, tokCol, tokColor);
            case ']': ch = nextChar(); return makeTok(Token.rightBracketTok, tokRow, tokCol, tokColor);
            case '&': check('&'); return makeTok(Token.andTok, tokRow, tokCol, tokColor);
            case '|': check('|');    return makeTok(Token.orTok, tokRow, tokCol, tokColor);
            case '?': return makeTok(chkOpt3('?','=', Token.typeeqTok, Token.fulleqTok), tokRow, tokCol, tokColor);
            case '=': return makeTok(chkOpt('=', Token.assignTok, Token.eqeqTok), tokRow, tokCol, tokColor);
            case '!':    ch = nextChar(); return makeTok(Token.notTok, tokRow, tokCol, tokColor);
            case '<': return makeTok(chkOpt2('=','>', Token.ltTok, Token.lteqTok,Token.noteqTok), tokRow, tokCol, tokColor);
            case '>': return makeTok(chkOpt('=', Token.gtTok, Token.gteqTok), tokRow, tokCol, tokColor);
            default:  error("Illegal character " + ch); 
            } // switch
        } while (true);
    } // next

    private Token makeTok(Token t, int r, int c, int cl) {
      return new Token(t.type(), t.value(), r, c, cl);
    }
    
    private boolean isLetter(char c) {
        return (c>='a' && c<='z' || c>='A' && c<='Z');
    }
  
    private boolean isDigit(char c) {
        return ('0'<= c && c <= '9');
    }

    private void check(char c) {
        ch = nextChar();
        if (ch != c) 
            error("Illegal character, expecting " + c);
        ch = nextChar();
    }

    private Token chkOpt(char c, Token one, Token two) {
       ch = nextChar();
       if(ch !=c) return one;
       ch = nextChar();
       return two;       
    }
    private Token chkOpt2(char c1, char c2, Token one, Token two, Token three) {
       ch = nextChar();
       if(ch == c1) {
          ch = nextChar();
          return two;
       } else if (ch == c2) {
          ch = nextChar();
          return three;
       }
       return one;       
    }
    private Token chkOpt3(char c1, char c2, Token one, Token two) {
       ch = nextChar();
       if(ch == c1) {
          ch = nextChar();
          return one;
       } else if (ch == c2) {
          ch = nextChar();
          return two;
       }
       error("?하나만 들어왔어요.");
       return null;
    }

    private String concat(String set) {
        String r = "";
        do {
            r += ch;
            ch = nextChar();
        } while (set.indexOf(ch) >= 0);
        return r;
    }

    public void error (String msg) {
        System.err.print(line);
        System.err.println("Error: column " + col + " " + msg);
        System.exit(1);
    }

    static public void main ( String[] argv ) {
        Lexer lexer = new Lexer("InputCode.xlsx");
        Token tok = lexer.next( );
        while (tok != Token.eofTok) {
           tok.print();
//            System.out.println(tok.toString());
            tok = lexer.next( );
        }
    }
}