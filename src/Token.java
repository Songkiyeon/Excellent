public class Token {
    private static final int KEYWORDS = TokenType.Eof.ordinal();
    private static final String[] reserved = new String[KEYWORDS];
    private static Token[] token = new Token[KEYWORDS];

    /* 2019-11-28+ */
    public static final Token blkTok = new Token(TokenType.Blk, "blk");
    public static final Token prtTok = new Token(TokenType.Prt, "prt");
    public static final Token scnTok = new Token(TokenType.Scn, "scn");
    public static final Token scnsTok = new Token(TokenType.Scns, "scns");
    public static final Token comTok = new Token(TokenType.Com, "com");
    public static final Token comsTok = new Token(TokenType.Coms, "coms");
    public static final Token defTok = new Token(TokenType.Def, "def");
    public static final Token retTok = new Token(TokenType.Ret, "ret");
    public static final Token typeeqTok = new Token(TokenType.TypeEquals, "??");
    public static final Token fulleqTok = new Token(TokenType.FullEquals, "?=");
    public static final Token twoplusTok = new Token(TokenType.TwoPlus, "++");
    public static final Token twominusTok = new Token(TokenType.TwoMinus, "--");
    /* 2019-11-30+ */
    public static final Token stringTok = new Token(TokenType.String, "string");
    public static final Token endTok = new Token(TokenType.End, "end");
    public static final Token nullTok = new Token(TokenType.Null, "null");
    /* 2019-12-08+ */
    public static final Token commaTok = new Token(TokenType.Comma, ",");
    
    public static final Token eofTok = new Token(TokenType.Eof, "<<EOF>>");
    public static final Token boolTok = new Token(TokenType.Bool, "bool");
    public static final Token charTok = new Token(TokenType.Char, "char");
    public static final Token falseTok = new Token(TokenType.False, "false");
    public static final Token floatTok = new Token(TokenType.Float, "float");
    public static final Token ifTok = new Token(TokenType.If, "if");
    public static final Token intTok = new Token(TokenType.Int, "int");
    public static final Token trueTok = new Token(TokenType.True, "true");
    public static final Token whileTok = new Token(TokenType.While, "while");
    public static final Token leftBracketTok = new Token(TokenType.LeftBracket, "[");
    public static final Token rightBracketTok = new Token(TokenType.RightBracket, "]");
    public static final Token leftParenTok = new Token(TokenType.LeftParen, "(");
    public static final Token rightParenTok = new Token(TokenType.RightParen, ")");
    public static final Token assignTok = new Token(TokenType.Assign, "=");
    public static final Token eqeqTok = new Token(TokenType.Equals, "==");
    public static final Token ltTok = new Token(TokenType.Less, "<");
    public static final Token lteqTok = new Token(TokenType.LessEqual, "<=");
    public static final Token gtTok = new Token(TokenType.Greater, ">");
    public static final Token gteqTok = new Token(TokenType.GreaterEqual, ">=");
    public static final Token notTok = new Token(TokenType.Not, "!");
    public static final Token noteqTok = new Token(TokenType.NotEqual, "<>");
    public static final Token plusTok = new Token(TokenType.Plus, "+");
    public static final Token minusTok = new Token(TokenType.Minus, "-");
    public static final Token multiplyTok = new Token(TokenType.Multiply, "*");
    public static final Token divideTok = new Token(TokenType.Divide, "/");
    public static final Token andTok = new Token(TokenType.And, "&&");
    public static final Token orTok = new Token(TokenType.Or, "||");

    private TokenType type;
    private String value = "";
    private int row;
    private int column;
    private int color;
    
    private Token (TokenType t, String v) {
        type = t;
        value = v;
        row = -1;
        column = -1;
        color = -1;
        if (t.compareTo(TokenType.Eof) < 0) {
            int ti = t.ordinal();
            reserved[ti] = v;
            token[ti] = this;
        }
    }

    public Token (TokenType t, String v, int r, int c, int cl) {
        type = t;
        value = v;
        row = r;
        column = c;
        color = cl;
        if (t.compareTo(TokenType.Eof) < 0) {
            int ti = t.ordinal();
            reserved[ti] = v;
            token[ti] = this;
        }
    }
    
   public TokenType type() {   return type; }
   public String value() { return value; }
   public int row() { return row; }
   public int column() { return column; }
   public int color() { return color; }
    
    public static Token keyword  ( String name, int r, int c, int cl) {
        char ch = name.charAt(0);
        if (ch >= 'A' && ch <= 'Z') return mkIdentTok(name, r, c, cl);
        for (int i = 0; i < KEYWORDS; i++)
         if (name.equals(reserved[i])) return new Token(token[i].type(), token[i].value(), r, c, cl);
        return mkIdentTok(name, r, c, cl);
    } // keyword

    public static Token mkIdentTok (String name, int r, int c, int cl) {
        return new Token(TokenType.Identifier, name, r, c, cl);
    }
    public static Token mkIntLiteral (String name, int r, int c, int cl) {
        return new Token(TokenType.IntLiteral, name, r, c, cl);
    }
    public static Token mkFloatLiteral (String name, int r, int c, int cl) {
        return new Token(TokenType.FloatLiteral, name, r, c, cl);
    }
    public static Token mkCharLiteral (String name, int r, int c, int cl) {
        return new Token(TokenType.CharLiteral, name, r, c, cl);
    }
    public static Token mkStringLiteral (String name, int r, int c, int cl) {
       return new Token(TokenType.StringLiteral, name, r, c, cl);
    }
    public void print() {
       System.out.printf("%13s %10s %3d %3d %3d\n",type, value, row, column, color);
    }
    public String toString ( ) {
      return type + "\t" + value + "\t" + row + "\t" + column + "\t" + color;
//        if (type.compareTo(TokenType.Identifier) < 0) return value;
//        return type + "\t" + value;
    } // toString

    public static void main (String[] args) {
    }
}