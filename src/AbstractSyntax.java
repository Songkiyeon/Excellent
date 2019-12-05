import java.util.*;

class Program {
    Declarations decpart;
    Block body;
    int Layer;
    int currentLayer;
//    node Temp_node;
//    tree T;
    
    Program (Declarations d, Block b) {
        decpart = d;
        body = b;
    }
    void display() {
    	for(int i=0; i<decpart.size();i++) {
    		System.out.println(decpart.get(i));
    	}
    	for(int j=0; j<body.members.size();j++) {
    		System.out.println(body.members.get(j));
    	}
    }
//    void Check_addNode(int l,node s) {
//    	if (l>Layer) {
//    		T.add(new nodeLayer());
//    		Layer++;
//    	} 
//    	T.get(l).addNode(s);
//    }
//    void display() {
//    	Layer =-1;
//    	currentLayer = 0;
//    	T = new tree();
//    	node n = new node("Program");
//    	Temp_node = n; 
//    	Check_addNode(currentLayer,n);
//    	currentLayer++;
//
//    	Print_Decls(decpart);
//    	Print_Block(body);
//    	System.out.println("==========Abstract Syntax tree============");
//    	System.out.println(T.get(0).nodelist.get(0).s+"(root)\n");
//    	for(int i=1;i<=Layer;i++) {
//    		for(int j=0;j<T.get(i).nodelist.size();j++) {
//    			System.out.print(T.get(i).nodelist.get(j).Print_node(T, i, j, Layer));
//    		}
//    		System.out.println("\n");
//    	}
//    	
//    }
//    void Print_Decls(Declarations ds) {
//    	node n = new node("Decls");
//    	n.setParent(Temp_node);
//    	Temp_node.addChild(n);
//    	Check_addNode(currentLayer++,n);
//    	
//    	Temp_node=n;
//    	for(int i=0;i<ds.size();i++) {
//    		Print_Declaration(ds.get(i));
//    	}
//    	currentLayer--;
//    	Temp_node = Temp_node.parent;
//    }
//    void Print_Declaration(Declaration De) {
//    	node n = new node("Decl");
//    	n.setParent(Temp_node);
//    	Temp_node.addChild(n);
//    	
//    	Check_addNode(currentLayer++,n);
//    	
//    	Temp_node=n;
//    	node tn = new node(De.t.toString());
//    	tn.setParent(Temp_node);
//    	Temp_node.addChild(tn);
//    	
//    	Check_addNode(currentLayer,tn);
//    	for(int i=0;i<De.v.size();i++) {
//    		node vn = new node(De.v.get(i).toString());
//    		vn.setParent(Temp_node);
//    		Temp_node.addChild(vn);
//    		Check_addNode(currentLayer,vn);
//    	}
//    	currentLayer--;
//    	Temp_node = Temp_node.parent;
//    }
//    void Print_Block(Statement B) {
//    	if (B.getClass().getName()=="Block") {
//
//        	node n = new node("Block");
//        	n.setParent(Temp_node);
//        	Temp_node.addChild(n);
//    		
//    		Check_addNode(currentLayer++,n);
//    		Temp_node = n;
//    		for(int i=0;i<((Block)B).members.size();i++) {
//    			Print_Block(((Block)B).members.get(i));
//    		}
//    		currentLayer--;
//    		Temp_node = Temp_node.parent;
//    	} else {
//    		if(B.getClass().getName()=="Assignment") {
//    			Print_Assignment((Assignment)B);		
//    		} else if(B.getClass().getName()=="Loop") {
//    			Print_Loop(((Loop)B));
//    		} else if(B.getClass().getName()=="Skip") {    			
//    		} 
//    		else if(B.getClass().getName()=="Conditional") {
//	    		Print_Conditional((Conditional)B);
//    		} 
//    		
//    	}
//    }
//    void Print_Conditional(Conditional C) {
//    	node n = new node("if");
//    	n.setParent(Temp_node);
//    	Temp_node.addChild(n);
//
//    	Check_addNode(currentLayer++, n);
//    	Temp_node = n;
//    	
//		Print_Expression(C.test);
//		Print_Block(C.thenbranch);
//		if(C.elsebranch.getClass().getName()!="Skip") { 
//			Print_Block(C.elsebranch);		
//		}
//		currentLayer--;
//		Temp_node = Temp_node.parent;
//    }
//    void Print_Loop(Loop L) {
//    	node n = new node("while");
//    	n.setParent(Temp_node);
//    	Temp_node.addChild(n);
//
//    	Check_addNode(currentLayer++,n);
//    	Temp_node = n;
//    	
//    	Print_Expression(L.test);
//    	Print_Block(L.body);
//    	currentLayer--;
//    	Temp_node = Temp_node.parent;
//    }
//    void Print_Assignment(Assignment As) {
//
//    	node n = new node("=");
//    	n.setParent(Temp_node);
//    	Temp_node.addChild(n);
//    	
//		Check_addNode(currentLayer++,n);		
//		Temp_node = n;
//		
//		Print_Expression(As.target);
//		Print_Expression(As.source);
//		currentLayer--;
//		Temp_node = Temp_node.parent;
//    }
//    void Print_Expression(Expression Ex) {
//    	if(Ex.getClass().getName()=="Variable") {
//    		Print_Variable((Variable)Ex);
//    	} else if(Ex.getClass().getName()=="Value") {
//    	} else if(Ex.getClass().getName()=="Binary") {
//    		Print_Binary((Binary)Ex);
//    	} else if(Ex.getClass().getName()=="Unary") {
//    		Print_Unary((Unary)Ex);
//    	} else if(Ex.getClass().getName()=="IntValue") {
//    		Print_IntValue((IntValue)Ex);
//    	} else if(Ex.getClass().getName()=="FloatValue") {
//    		Print_FloatValue((FloatValue)Ex);
//    	} else if(Ex.getClass().getName()=="CharValue") {    		
//    		Print_CharValue((CharValue)Ex);
//    	} else if(Ex.getClass().getName()=="BoolValue") {
//    		Print_BoolValue((BoolValue)Ex);
//    	}
//    	
//    	else {
//    	}
//    }
//    void Print_IntValue(IntValue Iv) {
//    	node n = new node(Iv.toString());
//    	n.setParent(Temp_node);
//    	Temp_node.addChild(n);
//
//		Check_addNode(currentLayer,n);
//    }
//    void Print_FloatValue(FloatValue Iv) {
//    	node n = new node(Iv.toString());
//    	n.setParent(Temp_node);
//    	Temp_node.addChild(n);
//    	
//    	Check_addNode(currentLayer,n);
//    }
//    void Print_CharValue(CharValue Iv) {
//    	node n = new node(Iv.toString());
//    	n.setParent(Temp_node);
//    	Temp_node.addChild(n);
//
//    	Check_addNode(currentLayer,n);
//    }
//    void Print_BoolValue(BoolValue Iv) {
//    	node n = new node(Iv.toString());
//    	n.setParent(Temp_node);
//    	Temp_node.addChild(n);
//
//    	Check_addNode(currentLayer,n);
//    }
//    void Print_Variable(Variable V) {
//    	node n = new node(V.toString());
//    	n.setParent(Temp_node);
//    	Temp_node.addChild(n);
//
//    	Check_addNode(currentLayer,n);
//    }
//    void Print_Binary(Binary Bi) {
//    	node n = new node(Bi.op.toString());
//    	n.setParent(Temp_node);
//    	Temp_node.addChild(n);
//
//		Check_addNode(currentLayer++,n);
//		Temp_node = n;
//    	Print_Expression(Bi.term1);
//    	Print_Expression(Bi.term2);
//    	currentLayer--;
//    	Temp_node = Temp_node.parent;
//    }
//    void Print_Unary(Unary Ui) {
//    	node n = new node(Ui.op.toString());
//    	n.setParent(Temp_node);
//    	Temp_node.addChild(n);
//		Check_addNode(currentLayer++,n);
//		Temp_node = n;
//    	Print_Expression(Ui.term);
//    	currentLayer--;
//    	Temp_node = Temp_node.parent;
//    }
}
//class tree extends ArrayList<nodeLayer>{
//	
//}
//class nodeLayer{
//	ArrayList<node> nodelist = new ArrayList<node>();
//	void addNode(node s) {
//		nodelist.add(s);
//	}
//}
//class node{
//	String s;
//	node parent = null;
//	ArrayList<node> child = new ArrayList<node>();
//	int location = 0;
//	node(String str){
//		s = str;
//	}
//	void setParent(node p) {
//		parent = p;
//	}
//	void addChild(node c) {
//		child.add(c);
//	}
//	String Print_node(tree t,int i, int j, int depth) {
//		String tabs = "";
//		if(j==0) {
//			for(int k=0;k<t.get(i).nodelist.get(j).parent.location;k++) {
//				tabs += "\t";
//			}
//			t.get(i).nodelist.get(j).location=t.get(i).nodelist.get(j).parent.location;
//			return tabs+t.get(i).nodelist.get(j).s;
//		} else {
//			
//			node le = sameParent_Left(t,i,j-1,depth);
//			node ri = sameParent_Right(t,i,j-1,depth);
//
//			int alpha = t.get(i).nodelist.get(j).parent.location - ri.location;
//			if(alpha<0)alpha=0;
//
//			int interval=0;
//			for(int z=le.parent.child.indexOf(le)+1;z<le.parent.child.indexOf(ri);z++) {
//				interval += NodeWidth(le.parent.child.get(z));
//			}
//			for(int k=0;
//					k<NodeWidth(le)
//					+alpha
//					+interval
//					- (t.get(i).nodelist.get(j-1).location-le.location);
//					k++) {
//				tabs += "\t";				
//			}
//			
//			t.get(i).nodelist.get(j).location += le.location 
//												+ NodeWidth(le)
//												+ alpha
//												+ interval;
//			return tabs+t.get(i).nodelist.get(j).s;			
//		}
//	}
//	node sameParent_Left(tree t, int i, int j, int depth) {
//		node left_node = t.get(i).nodelist.get(j);
//		node right_node = t.get(i).nodelist.get(j+1);
//		node Temp_node = t.get(i).nodelist.get(j);
//		
//		left_node = t.get(i).nodelist.get(j).parent;
//		right_node = t.get(i).nodelist.get(j+1).parent;
//		while (!(left_node==right_node)) {
//			Temp_node = left_node;
//			left_node = left_node.parent;
//			right_node = right_node.parent;			
//		}
//				
//		return Temp_node;		
//	}
//	node sameParent_Right(tree t, int i, int j, int depth) {
//		node left_node = t.get(i).nodelist.get(j);
//		node right_node = t.get(i).nodelist.get(j+1);
//		node Temp_node = t.get(i).nodelist.get(j);
//		
//		left_node = t.get(i).nodelist.get(j).parent;
//		right_node = t.get(i).nodelist.get(j+1).parent;
//		while (!(left_node==right_node)) {
//			Temp_node = right_node;
//			left_node = left_node.parent;
//			right_node = right_node.parent;			
//		}
//		return Temp_node;		
//	}
//	int NodeWidth(node n) {
//		int sum =0;
//		for(int i=0;i<n.child.size();i++) {
//			sum += NodeWidth(n.child.get(i));
//		}
//		
//		if(sum==0) return 1;
//		return sum;
//	}
//}
class Declarations extends ArrayList<declare> {
}

abstract class declare{
	
}

class Declaration extends declare{
//	ArrayList<Variable> v = new ArrayList<Variable>();
//    Type t;
	String name;
	int color;
    String value;

//    Declaration (Variable var, Type type, String value) {
//        this.v.add(var); this.t = type; this.value = value;
//    }
    Declaration (String name, int type, String value) {
        this.name = name; 
        this.color = type; 
        this.value = value;
    }
//    public void addValue(Variable var) {
//    	v.add(var);
//    }
    public void setValue(String value) {
    	this.value=value;
    }
}
class DefineColor extends declare {
	int color;
	String Type;
	DefineColor(int color, String Type){
		this.color = color;
		this.Type = Type;
	}
}
class Array extends declare{
//	Type type;
	String name;
	int color;
	int x, y;
	ArrayList<ArrayList<String>> matrix = new ArrayList<ArrayList<String>>();	

	Array(int y, int x, int color, String name){
		this.y = y;
		this.x = x;
		for(int i=0; i<y;i++) {
			ArrayList<String> row = new ArrayList<String>();
			for(int j=0; j<x; j++) {
				row.add("null");
			}
			matrix.add(row);
		}
		this.name = name;
		this.color = color;
	}
	public void addNode(int y, int x, String value) {
		(matrix.get(y)).set(x, value);
	}
}





class Type {
    final static Type INT = new Type("int");
    final static Type BOOL = new Type("bool");
    final static Type CHAR = new Type("char");
    final static Type FLOAT = new Type("float");
    final static Type END = new Type("end");
    final static Type STRING = new Type("string");
    
    private String id;

    Type (String t) { id = t; }

    public String toString ( ) { return id; }
}

abstract class Statement {

}

class Skip extends Statement {
}

class Block extends Statement {
    public ArrayList<Statement> members = new ArrayList<Statement>();

}

class Assignment extends Statement {
    Variable target;
    Expression source;

    Assignment (Variable t, Expression e) {
        target = t;
        source = e;
    }

}

class Conditional extends Statement {
	ArrayList<Expression> Exprs;
	ArrayList<Block> Statements;
    
    Conditional (ArrayList<Expression> E, ArrayList<Block> S) {
    	Exprs = E; Statements = S;
    }    
}

class Loop extends Statement {
	ArrayList<Expression> Exprs;
	ArrayList<Block> Statements;
    
    Loop (ArrayList<Expression> E, ArrayList<Block> S) {
    	Exprs = E; Statements = S;
    }    
}


class Prt extends Statement{
	ArrayList<String> list = new ArrayList<String>();
	Prt(ArrayList<String> l){
		this.list = l;
	}
}

class Scn extends Statement{
	String pri;
	String value;
	
	Scn(String pri, String v){
		this.pri = pri;
		this.value = v;
	}
}

class Scns extends Statement{
	String pri;
	ArrayList<String> values = new ArrayList<String>();
	
	Scns(String pri, ArrayList<String> v){
		this.pri = pri;
		this.values = v;
	}
}

class Def extends Statement{
	String name;
	Block b;
	ArrayList<Integer> Parameter_type;
	ArrayList<String> Parameter_name;
	Expression ret;
	
	Def(String n, ArrayList<Integer> t, ArrayList<String> s, Block b, Expression r){
		this.name = n;
		this.Parameter_type = t;
		this.Parameter_name = s;
		this.b = b;
		this.ret = r;
	}
}

class Com extends Statement{
	String comment;
	Com(String c){
		this.comment = c;
	}
}
class Coms extends Statement{
	String comment;
	Coms(String c){
		this.comment = c;
	}	
}





abstract class Expression {

}

class Variable extends Expression {
    private String id;

    Variable (String s) { id = s; }

    public String toString( ) { return id; }
    
    public boolean equals (Object obj) {
        String s = ((Variable) obj).id;
        return id.equals(s); 
    }
    
    public int hashCode ( ) { return id.hashCode( ); }

}


class ArrayValue extends Expression{
    private String id;
    private int row = -1;
    private int col = -1;
    
    ArrayValue (String value,int row, int col) { 
    	this.row = row;
    	this.col = col;
    	this.id = value;
    }

    public String toString() { return id;}

    public boolean equals(Object obj) {
        String s = ((ArrayValue) obj).id;
        return id.equals(s);     	
    }
    public int hashCode ( ) { return id.hashCode( ); }

}






abstract class Value extends Expression {
    protected Type type;
    protected boolean undef = true;

    int intValue ( ) {
        assert false : "should never reach here";
        return 0;
    }     
    boolean boolValue ( ) {
        assert false : "should never reach here";
        return false;
    }
    char charValue ( ) {
        assert false : "should never reach here";
        return ' ';
    }
    float floatValue ( ) {
        assert false : "should never reach here";
        return 0.0f;
    }
    

    boolean isUndef( ) { return undef; }

    Type type ( ) { return type; }
    static Value mkValue (Type type) {
        if (type == Type.INT) return new IntValue( );
        if (type == Type.BOOL) return new BoolValue( );
        if (type == Type.CHAR) return new CharValue( );
        if (type == Type.FLOAT) return new FloatValue( );
        throw new IllegalArgumentException("Illegal type in mkValue");
    }
}

class IntValue extends Value {
    private int value = 0;

    IntValue ( ) { type = Type.INT; }

    IntValue (int v) { this( ); value = v; undef = false; }

    int intValue ( ) {
        assert !undef : "reference to undefined int value";
        return value;
    }

    public String toString( ) {
        if (undef)  return "undef";
        return "" + value;
    }

}

class BoolValue extends Value {
    private boolean value = false;

    BoolValue ( ) { type = Type.BOOL; }

    BoolValue (boolean v) { this( ); value = v; undef = false; }

    boolean boolValue ( ) {
        assert !undef : "reference to undefined bool value";
        return value;
    }

    int intValue ( ) {
        assert !undef : "reference to undefined bool value";
        return value ? 1 : 0;
    }

    public String toString( ) {
        if (undef)  return "undef";
        return "" + value;
    }

}

class CharValue extends Value {
    private char value = ' ';

    CharValue ( ) { type = Type.CHAR; }

    CharValue (char v) { this( ); value = v; undef = false; }

    char charValue ( ) {
        assert !undef : "reference to undefined char value";
        return value;
    }

    public String toString( ) {
        if (undef)  return "undef";
        return "" + value;
    }

}

class FloatValue extends Value {
    private float value = 0;

    FloatValue ( ) { type = Type.FLOAT; }

    FloatValue (float v) { this( ); value = v; undef = false; }

    float floatValue ( ) {
        assert !undef : "reference to undefined float value";
        return value;
    }

    public String toString( ) {
        if (undef)  return "undef";
        return "" + value;
    }
}





class Binary extends Expression {
    Operator op;
    Expression term1, term2;

    Binary (Operator o, Expression l, Expression r) {
        op = o; term1 = l; term2 = r;
    }
}

class Unary extends Expression {
    Operator op;
    Expression term;

    Unary (Operator o, Expression e) {
        op = o; term = e;
    }
}

class Operator {
    final static String AND = "&&";
    final static String OR = "||";
    final static String LT = "<";
    final static String LE = "<=";
    final static String EQ = "==";
    final static String NE = "!=";
    final static String GT = ">";
    final static String GE = ">=";
    final static String PLUS = "+";
    final static String MINUS = "-";
    final static String TIMES = "*";
    final static String DIV = "/";
    final static String NOT = "!";
    final static String NEG = "-";
    final static String INT = "int";
    final static String FLOAT = "float";
    final static String CHAR = "char";
    final static String INT_LT = "INT<";
    final static String INT_LE = "INT<=";
    final static String INT_EQ = "INT==";
    final static String INT_NE = "INT!=";
    final static String INT_GT = "INT>";
    final static String INT_GE = "INT>=";
    final static String INT_PLUS = "INT+";
    final static String INT_MINUS = "INT-";
    final static String INT_TIMES = "INT*";
    final static String INT_DIV = "INT/";
    final static String INT_NEG = "-";
    final static String FLOAT_LT = "FLOAT<";
    final static String FLOAT_LE = "FLOAT<=";
    final static String FLOAT_EQ = "FLOAT==";
    final static String FLOAT_NE = "FLOAT!=";
    final static String FLOAT_GT = "FLOAT>";
    final static String FLOAT_GE = "FLOAT>=";
    final static String FLOAT_PLUS = "FLOAT+";
    final static String FLOAT_MINUS = "FLOAT-";
    final static String FLOAT_TIMES = "FLOAT*";
    final static String FLOAT_DIV = "FLOAT/";
    final static String FLOAT_NEG = "-";
    final static String CHAR_LT = "CHAR<";
    final static String CHAR_LE = "CHAR<=";
    final static String CHAR_EQ = "CHAR==";
    final static String CHAR_NE = "CHAR!=";
    final static String CHAR_GT = "CHAR>";
    final static String CHAR_GE = "CHAR>=";
    final static String BOOL_LT = "BOOL<";
    final static String BOOL_LE = "BOOL<=";
    final static String BOOL_EQ = "BOOL==";
    final static String BOOL_NE = "BOOL!=";
    final static String BOOL_GT = "BOOL>";
    final static String BOOL_GE = "BOOL>=";
    final static String I2F = "I2F";
    final static String F2I = "F2I";
    final static String C2I = "C2I";
    final static String I2C = "I2C";
    
    String val;
    
    Operator (String s) { val = s; }

    public String toString( ) { return val; }
    public boolean equals(Object obj) { return val.equals(obj); }
    
    boolean BooleanOp ( ) { return val.equals(AND) || val.equals(OR); }
    boolean RelationalOp ( ) {
        return val.equals(LT) || val.equals(LE) || val.equals(EQ)
            || val.equals(NE) || val.equals(GT) || val.equals(GE);
    }
    boolean ArithmeticOp ( ) {
        return val.equals(PLUS) || val.equals(MINUS)
            || val.equals(TIMES) || val.equals(DIV);
    }
    boolean NotOp ( ) { return val.equals(NOT) ; }
    boolean NegateOp ( ) { return val.equals(NEG) ; }
    boolean intOp ( ) { return val.equals(INT); }
    boolean floatOp ( ) { return val.equals(FLOAT); }
    boolean charOp ( ) { return val.equals(CHAR); }

    final static String intMap[ ] [ ] = {
        {PLUS, INT_PLUS}, {MINUS, INT_MINUS},
        {TIMES, INT_TIMES}, {DIV, INT_DIV},
        {EQ, INT_EQ}, {NE, INT_NE}, {LT, INT_LT},
        {LE, INT_LE}, {GT, INT_GT}, {GE, INT_GE},
        {NEG, INT_NEG}, {FLOAT, I2F}, {CHAR, I2C}
    };

    final static String floatMap[ ] [ ] = {
        {PLUS, FLOAT_PLUS}, {MINUS, FLOAT_MINUS},
        {TIMES, FLOAT_TIMES}, {DIV, FLOAT_DIV},
        {EQ, FLOAT_EQ}, {NE, FLOAT_NE}, {LT, FLOAT_LT},
        {LE, FLOAT_LE}, {GT, FLOAT_GT}, {GE, FLOAT_GE},
        {NEG, FLOAT_NEG}, {INT, F2I}
    };

    final static String charMap[ ] [ ] = {
        {EQ, CHAR_EQ}, {NE, CHAR_NE}, {LT, CHAR_LT},
        {LE, CHAR_LE}, {GT, CHAR_GT}, {GE, CHAR_GE},
        {INT, C2I}
    };

    final static String boolMap[ ] [ ] = {
        {EQ, BOOL_EQ}, {NE, BOOL_NE}, {LT, BOOL_LT},
        {LE, BOOL_LE}, {GT, BOOL_GT}, {GE, BOOL_GE},
    };

    final static private Operator map (String[][] tmap, String op) {
        for (int i = 0; i < tmap.length; i++)
            if (tmap[i][0].equals(op))
                return new Operator(tmap[i][1]);
        assert false : "should never reach here";
        return null;
    }

    final static public Operator intMap (String op) {
        return map (intMap, op);
    }

    final static public Operator floatMap (String op) {
        return map (floatMap, op);
    }

    final static public Operator charMap (String op) {
        return map (charMap, op);
    }

    final static public Operator boolMap (String op) {
        return map (boolMap, op);
    }

}
