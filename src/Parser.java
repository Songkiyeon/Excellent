import java.util.*;

public class Parser {
//	Token Temp;
    Token token;         
    Lexer lexer;
    int color_box_color;
    int color_box_row;
    boolean change_color = false;
    
    public Parser(Lexer ts) { 
        lexer = ts;                          
        token = lexer.next();            
    }
  
    private String match (TokenType t) { 
        String value = token.value();
        if (token.type().equals(t))
            token = lexer.next();
        else
            error(t);
        return value;
    }
  
    private void error(TokenType tok) {
        System.err.println("Syntax error: expecting: " + tok 
                           + "; saw: " + token);
        System.exit(1);
    }
  
    private void error(String tok) {
        System.err.println("Syntax error: expecting: " + tok 
                           + "; saw: " + token);
        System.exit(1);
    }
  
    public Program program() {
        Declarations Ds = declarations();

        while(isColor()) {
//        	System.out.println("de");
          declaration(Ds);        	        	
        }
        
//    	System.out.println("!!");
        Block b = statements(false);
//    	System.out.println("??");
        Program pro = new Program(Ds,b);
//    	System.out.println(";;");
                
        return pro;  
    }
  
    private Declarations declarations () {
    	Declarations ds = new Declarations();
        return ds;
    }
  
    private void declaration (Declarations ds) {
    	if(isType()) {
    		DefineColor DC = new DefineColor(token.color(),token.value());
    		token = lexer.next();
    		ds.add(DC);

    	} else {

    		ArrayList<declare> AL = new ArrayList<declare>();
    		int line;
    		line = token.row();
    		while(token.value()!="null" && token.row()==line) {
    			if(isLeftBracket()) {
    				
    				//수정되는 부분
    				//이거
    				String name = ((Declaration)(AL.get(AL.size()-1))).name;
    				//
    				
    				AL.remove(AL.size()-1);
    				String temp_index = "";
    				match(TokenType.LeftBracket);
    				temp_index = token.value();
    				token=lexer.next();
    				match(TokenType.RightBracket);
    				if(isLeftBracket()) {
        				match(TokenType.LeftBracket);    					
        				
        				//이거
    					Array matrix = new Array(Integer.parseInt(temp_index),Integer.parseInt(token.value()),token.color(),name);
    					//
    					
    					token=lexer.next();
    					match(TokenType.RightBracket);    					    					
    					AL.add(matrix);
    				} else {
    					//이거
    					Array matrix = new Array(1,Integer.parseInt(temp_index),token.color(),name);
    					//
    					//여기까지
    					
    					AL.add(matrix);
    				}
    				
    			} else {
        			Declaration De = new Declaration(
//        					new Variable(token.value()),
//        					new Type(token.value()),"null"
        					token.value(),
        					token.color(),
        					"null"
        					);
        			AL.add(De);
        			token = lexer.next();    				
    			}
    		}
    		while(token.value()!="null" && token.row()==line+1) {
    			if((AL.get(token.column())).getClass().getName()=="Array") {
    				int normalrow = token.row();
    				int normalcol = token.column();
    				Array va = ((Array)(AL.get(token.column())));
    					while(token.type()!=TokenType.Null) {
    						va.addNode(token.row()-normalrow,token.column()-normalcol,token.value());
    						token = lexer.next();
    					}
    				
        			token = lexer.next();    				
    			} else {
           			((Declaration)AL.get(token.column())).setValue(token.value());
        			token = lexer.next();    				
    			}
    		}
    		for(int i=0; i<AL.size();i++) {
    			ds.add(AL.get(i));
    		}
    	}
    	if(token.value()=="null") {
    		token = lexer.next();
    	}
    }
  
    private Type type () {
        Type t = null;
        return t;          
    }
  
    private Statement statement() {
        Statement s = new Skip();
        return s;
    }
  
    private Block statements (boolean flag){
        Block b = new Block();
//        System.out.println("eee");
//        while(!(isEndbrase()) && !(isReturn())) {
        while(!(isReturn())) {
   //	        System.out.println("ㅃoom!!");
        	if(isNull()){
	        	color_box_row = token.row();

	        	if(color_box_color != token.color()) {
		        	color_box_color = token.color();
//		        	token = lexer.next();
	        	
		        	break;
	        	} else {
		        	color_box_color = token.color();	        		
		        	token = lexer.next();
    	        	continue;
	        	}
	        	
	        }
        	if(flag) {
//        		System.out.print("flag row : ");
//        		System.out.println(token.row());
        		
        		if(token.row()!=color_box_row) {
        			break;
        		}
        	}
        	
        	Statement S = null;
	        if(isIdentifier()) {
	        	//System.out.println(token.value());
	        	S = assignment();
	        } else if(isWhileLoop()) {
	        	S = whileStatement();
	        } else if(isIfLoop()) {
	        	S = ifStatement();
	        } else if(isPrt()) {
	        	S = prtStatement();
	        } else if(isScn()) {
	        	S = scnStatement();
	        } else if(isScns()) {
	        	S = scnsStatement();
	        } else if(isFunction()) {
	        	S = defStatement();
	        } else if(isComment()) {
	        	S = comStatement();
	        } else if(isComs()) {
	        	S = comsStatement();
	        } else if(isFinish()) {
//		        System.out.println("boom!!");
	        	break;
	        } 
	        b.members.add(S);
        }
        return b;
    }

    private Assignment assignment () {

    	Variable V = new Variable(token.value());
    	token=lexer.next();
    	
    	match(TokenType.Assign);
    	
    	Expression e = expression();
     	     	
    	Assignment as = new Assignment(V,e);
//    	System.out.println("outofAss");
        return as;
    }
  
    private Conditional ifStatement () {
//    	System.out.print("conditional IN : ");
//    	System.out.println(token.value());
    	token=lexer.next();
    	ArrayList<Integer> expr_color = new ArrayList<Integer>(); //color list
    	ArrayList<Expression> ex_list = new ArrayList<Expression>(); //조건 리스트
    	ArrayList<Block> bl_list = new ArrayList<Block>(); //실행 리스트
//    	System.out.println("Check");
    	//조건문 탐색
		expr_color.add(token.color());

		Expression e = expression();
//    	System.out.println("Check???");
    	ex_list.add(e);
//    	System.out.println("Check2");
    	//

    	//조건문들
    	while(!expr_color.contains(token.color())) {
//        	System.out.println("Check3");
    		expr_color.add(token.color());    		
        	Expression e2 = expression();    		
//        	Block e2 = statements();    		
        	ex_list.add(e2);
    	}
    	//
    	
    	//body들
//    	System.out.println("here:");
//    	System.out.println(token.value());
//    	System.out.println(token.color());
		while(token.color() !=0) {
        	Block b = statements(true);
        	bl_list.add(b);
		}
//    	System.out.print("conditional OUT : ");
//    	System.out.println(token.value());
//    	System.out.print("conditional OUT");
    	return new Conditional(ex_list,bl_list);
    }
  
    private Loop whileStatement () {
//    	System.out.print("Loop IN");
    	token=lexer.next();
    	ArrayList<Integer> expr_color = new ArrayList<Integer>(); //color list
    	ArrayList<Expression> ex_list = new ArrayList<Expression>(); //조건 리스트
    	ArrayList<Block> bl_list = new ArrayList<Block>(); //실행 리스트
    	//조건문 탐색
		expr_color.add(token.color());

		Expression e = expression();
    	ex_list.add(e);
    	//

    	//조건문들
    	while(!expr_color.contains(token.color())) {
//        	System.out.println("!!");
    		expr_color.add(token.color());    		
        	Expression e2 = expression();    		
        	ex_list.add(e2);
    	}
    	//
    	color_box_color = token.color();
    	//body들
    	while(token.color()!=0) {
//        	System.out.print("띠요옹 : ");
//        	System.out.println();
//    		token = lexer.next();//한칸 띄고 부터 바디
        	Block b = statements(true);
        	bl_list.add(b);
    	}
//    	System.out.print("Loop OUT");
    	return new Loop(ex_list,bl_list);
    }

    private Prt prtStatement() {
//    	System.out.println("PRt");
    	int Prt_row = token.row();
    	match(TokenType.Prt);
    	
    	ArrayList<String> list = new ArrayList<String>();
    	while(token.row()==Prt_row) {
    		list.add(token.value());
    		token = lexer.next();
    	}
    	return new Prt(list);
    }
    private Scn scnStatement() {    	
    	match(TokenType.Scn);
    	String pri;
    	String value;
    	
    	//출력문 삽입
    	pri = token.value();
    	token = lexer.next();
    	//
    	
    	//변수삽입
		value = token.value();
		token = lexer.next();
    	//
    	return new Scn(pri,value);
    }
    private Scns scnsStatement() {
    	int Scns_row = token.row();
    	match(TokenType.Scns);
    	String pri;
    	ArrayList<String> values= new ArrayList<String>();
    	
    	//출력문 삽입
    	pri = token.value();
    	token = lexer.next();
    	//
    	
    	//변수삽입
    	while(token.row()==Scns_row) {
    		values.add(token.value());
    		token = lexer.next();
    	}
    	//
    	return new Scns(pri,values);
    }
    private Def defStatement() {
//    	System.out.println("DEF");
    	match(TokenType.Def);
    	String name;
    	ArrayList<Integer> pt = new ArrayList<Integer>();
    	ArrayList<String> ps = new ArrayList<String>();
    	Block b;
    	Expression re;
    	
    	//name
    	name = token.value();
    	token = lexer.next();
    	
    	//parameters
    	while(token.color()!=0) {
        	pt.add(token.color());
        	ps.add(token.value());    
        	token = lexer.next();
//        	System.out.println("here~");
    	}
    	
    	//body
//    	System.out.println("her!");
    	
    	b = statements(false);
//    	System.out.println("here####~");
    	match(TokenType.Ret);
//    	System.out.println("here%%%%~");

    	//return
    	re = expression();
    	
    	return new Def(name,pt,ps,b,re);    	
    }
    private Com comStatement() {
//    	System.out.println("Com");
    	int Com_row;
    	String Com_String = "";
    	
    	Com_row = token.row();
    	match(TokenType.Com);
    	while((token.row()==Com_row)) {
    		Com_String += token.value() + " ";
    		token = lexer.next();
    	}
    	
    	return new Com(Com_String);
    }
    private Coms comsStatement() {
//    	System.out.println("Cos");
    	int Com_row;
    	String Com_String = "";
    	
    	Com_row = token.row();
    	match(TokenType.Coms);
    	while(token.value()!="null") {
    		Com_String += token.value() + " ";
    		token = lexer.next();
    	}
    	match(TokenType.Null);
    	return new Coms(Com_String);
    }
        
    private Expression expression () {
//    	System.out.println("CheckQQ");

    	Expression e = conjunction();
//    	System.out.println("CheckWW");
    	while(isOr()) {
    		Operator op = new Operator(match(token.type()));
    		Expression con2 = conjunction();
    		e = new Binary(op,e,con2);
    	}
    	return e;  
    }
  
    private Expression conjunction () {
//    	System.out.println("CheckRR");
    	Expression e = equality();
    	while(isAnd()) {
    		Operator op = new Operator(match(token.type()));
    		Expression e2 = equality();
    		e = new Binary(op,e,e2);
    	}    	
    	return e;  
    }
  
    private Expression equality () {
//    	System.out.println("CheckEE");
    	Expression e = relation();
    	if(isEqualityOp()) {
    		Operator op = new Operator(match(token.type()));
    		Expression rel2 = relation();
    		e = new Binary(op,e,rel2);
    	}
        return e;  
    }

    private Expression relation (){
//    	System.out.println("CheckZZ");
    	Expression e = addition();
    	if(isRelationalOp()) {    	
    		Operator op = new Operator(match(token.type()));
    		Expression add2 = addition();
    		e = new Binary(op,e,add2);
    	}
        return e;  
    }
  
    private Expression addition () {
//    	System.out.println("CheckAA");
        Expression e = term();
        while (isAddOp()) {
            Operator op = new Operator(match(token.type()));
            Expression term2 = term();
            e = new Binary(op, e, term2);
        }
        return e;
    }
  
    private Expression term () {
//    	System.out.println("CheckSS");
        Expression e = factor();
        while (isMultiplyOp()) {
            Operator op = new Operator(match(token.type()));
            Expression term2 = factor();
            e = new Binary(op, e, term2);
        }
        return e;
    }
  
    private Expression factor() {
//    	System.out.println("CheckDD");
        if (isUnaryOp()) {
            Operator op = new Operator(match(token.type()));
            Expression term = primary();
            return new Unary(op, term);
        }
        else return primary();
    }
  
    private Expression primary () {
//    	System.out.println("CheckCC");
//    	System.out.println(token.value());
//    	System.out.println(token.row());
//    	System.out.println(token.column());
        Expression e = null;
        if (token.type().equals(TokenType.Identifier)) {
//        	System.out.println("ID");
//            System.out.println(1);
        	
//    		System.out.println("hehe");
//    		char temp_value = token.value().charAt(0);
//    		token = lexer.next();
//    		if(isLeftBracket()) {
//    			match(TokenType.LeftBracket);
//    			int temp_col = Integer.parseInt(token.value());
//    			match(TokenType.RightBracket);
//    			if(isLeftBracket()) {
//        			match(TokenType.LeftBracket);
//        			value = new ArrayValue(token.value(),temp_col,Integer.parseInt(token.value()));  
//        			match(TokenType.RightBracket);
//    			} else {
//        			value = new ArrayValue(token.value(),-1,temp_col);
//    			}
//
//    		} else {
//        		value = new CharValue(temp_value);
//    		}
//        	System.out.println("here");
        	String id = match(TokenType.Identifier);
        	if(isLeftBracket()) {
        		match(TokenType.LeftBracket);
        		int temp_col = Integer.parseInt(token.value());
        		token = lexer.next();
        		match(TokenType.RightBracket);
        		if(isLeftBracket()) {
        			match(TokenType.LeftBracket);
        			e = new ArrayValue(id,temp_col,Integer.parseInt(token.value()));  
            		token = lexer.next();
        			match(TokenType.RightBracket);
        		} else {
        			e = new ArrayValue(id,-1,temp_col);
        		}
        	} else {
                e = new Variable(id);
        	}
        	
        	
        	
        } else if (isLiteral()) {
//        	System.out.println("LI");
//            System.out.println(2);
            e = literal();
        } else if (token.type().equals(TokenType.LeftParen)) {
//        	System.out.println("LP");
//            System.out.println(3);
            token = lexer.next();
            e = expression();       
            match(TokenType.RightParen);
        } else if (isType( )) {
//        	System.out.println("TY");
//            System.out.println(4);
            Operator op = new Operator(match(token.type()));
            match(TokenType.LeftParen);
            Expression term = expression();
            match(TokenType.RightParen);
            e = new Unary(op, term);
        } else if(isNull()) {
//            System.out.println(5);
        	color_box_row = token.row();
        	color_box_color = token.color();
        	token = lexer.next();
        } else error("Identifier | Literal | ( | Type");
        return e;
    }

    private Value literal( ) {
//    	System.out.println("CheckXX");
    	
    	Value value = null;
    	if(token.type().equals(TokenType.IntLiteral)) {
    		value = new IntValue(Integer.parseInt(token.value()));    			
    		token = lexer.next();
    	} else if (token.type().equals(TokenType.FloatLiteral)) {
    		value = new FloatValue(Float.parseFloat(token.value()));    			
    		token = lexer.next();
    	} else if (token.type().equals(TokenType.CharLiteral)) {    		
//    		System.out.println("here3");
    		value = new CharValue(token.value().charAt(0));
    		token = lexer.next();
//    		System.out.println("hehe");
//    		char temp_value = token.value().charAt(0);
//    		token = lexer.next();
//    		if(isLeftBracket()) {
//    			match(TokenType.LeftBracket);
//    			int temp_col = Integer.parseInt(token.value());
//    			match(TokenType.RightBracket);
//    			if(isLeftBracket()) {
//        			match(TokenType.LeftBracket);
//        			value = new ArrayValue(token.value(),temp_col,Integer.parseInt(token.value()));  
//        			match(TokenType.RightBracket);
//    			} else {
//        			value = new ArrayValue(token.value(),-1,temp_col);
//    			}
//
//    		} else {
//        		value = new CharValue(temp_value);
//    		}
    	
    	
    	
    	} else if (token.type().equals(TokenType.True)) {
//    		System.out.println("here4");
    		value = new BoolValue(true);
    		token = lexer.next();
    	} else if (token.type().equals(TokenType.False)){
//    		System.out.println("here5");
    		value = new BoolValue(false);
    		token = lexer.next();
    	} else {
    		System.out.println("error");
    	}
    	return value; 
    }
    private boolean isColor() {
    	if (token.color() != 0) {
    		return true;
    	}
    	return false;
    }
    private boolean isAddOp( ) {
        return token.type().equals(TokenType.Plus) ||
               token.type().equals(TokenType.Minus);
    }
    
    private boolean isMultiplyOp( ) {
        return token.type().equals(TokenType.Multiply) ||
               token.type().equals(TokenType.Divide);
    }
    
    private boolean isUnaryOp( ) {
        return token.type().equals(TokenType.Not) ||
               token.type().equals(TokenType.Minus);
    }
    
    private boolean isEqualityOp( ) {
        return token.type().equals(TokenType.Equals) ||
            token.type().equals(TokenType.NotEqual);
    }
    
    private boolean isRelationalOp( ) {
        return token.type().equals(TokenType.Less) ||
               token.type().equals(TokenType.LessEqual) || 
               token.type().equals(TokenType.Greater) ||
               token.type().equals(TokenType.GreaterEqual);
    }
    
    private boolean isType( ) {
        return token.type().equals(TokenType.Int)
            || token.type().equals(TokenType.Bool) 
            || token.type().equals(TokenType.Float)
            || token.type().equals(TokenType.Char)
        	|| token.type().equals(TokenType.End)
        	|| token.type().equals(TokenType.String);
    }
    
    private boolean isLiteral( ) {
        return token.type().equals(TokenType.IntLiteral) ||
            isBooleanLiteral() ||
            token.type().equals(TokenType.FloatLiteral) ||
            token.type().equals(TokenType.CharLiteral);
    }
    
    private boolean isBooleanLiteral( ) {
        return token.type().equals(TokenType.True) ||
            token.type().equals(TokenType.False);
    }
    
    private boolean isIdentifier() {
    	return token.type().equals(TokenType.Identifier);
    }
    
    private boolean isEndbrase() {
    	if (token != null) {
    		return  (token.value() == "null")? true : false;
    	} else {
    		return true;
    	}
    }
    private boolean isAnd() {
    	return token.type().equals(TokenType.And);
    }
    private boolean isOr() {
    	return token.type().equals(TokenType.Or);
    }
    private boolean isWhileLoop() {
    	return token.type().equals(TokenType.While);
    }
    private boolean isIfLoop() {
    	return token.type().equals(TokenType.If);
    }
    private boolean isLeftBracket(){
    	return token.type().equals(TokenType.LeftBracket);    	
    }
    private boolean isPrt() {
    	return token.type().equals(TokenType.Prt);
    }
    private boolean isScn() {
    	return token.type().equals(TokenType.Scn);    	
    }
    private boolean isScns() {
    	return token.type().equals(TokenType.Scns);    	    	
    }
    private boolean isFunction() {
    	return token.type().equals(TokenType.Def);    	    	    	
    }
    private boolean isComment() {    	
    	return token.type().equals(TokenType.Com);    	    	    	
    }
    private boolean isComs() {    	
    	return token.type().equals(TokenType.Coms);    	    	    	
    }
    private boolean isReturn() {
    	return token.type().equals(TokenType.Ret);    	    	    	    	
    }
    private boolean isNull() {
    	return token.type().equals(TokenType.Null);
    }
    private boolean isFinish() {
    	return token.type().equals(TokenType.Eof);
    }
//    private boolean check_matrix() {
//    	return false;
//    }
    public static void main(String args[]) {
//    	String path = Lexer.class.getResource("").getPath();
        Parser parser  = new Parser(new Lexer("test.xlsx"));
        Program prog = parser.program();
        TypeChecker T = new TypeChecker(prog);
        T.ValidationStart();
        prog.display();          
    } 
}
