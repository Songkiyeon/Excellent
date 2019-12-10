import java.io.File;
import java.io.FileWriter;
import java.util.*;

class Program {
	static String fileName = "excellent.js";
	static File file;
	static FileWriter fw;
	Declarations decpart;
	Block body;
	int Layer;
	int currentLayer;

	Program(Declarations d, Block b) {
		decpart = d;
		body = b;
	}

	void display(int k) {
		try {
			file = new File(fileName);
			fw = new FileWriter(file, false);
			
			for (int tab = 0; tab < k; tab++) {
				stringExport("\t");
			}
			stringExport("function excellent() {\n");
			decpart.display(++k);
			body.display(k);
			stringExport("}\n");
			System.out.println("Export Completed!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	static void stringExport(String cach) {
		try {
			fw.write(cach);
			fw.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

class Declarations extends ArrayList<declare> {
	public void display(int k) {
		for (int tab = 0; tab < k - 1; tab++) {
			Program.stringExport("\t");
		}
		for (int i = 0; i < this.size(); i++)
			this.get(i).display(k);
	}
}

abstract class declare {
	public void display(int k) {
	}
}

class Declaration extends declare {
	String name;
	int color;
	String value;

	Declaration(String name, int type, String value) {
		this.name = name;
		this.color = type;
		this.value = value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void display(int k) {
		for (int tab = 0; tab < k; tab++) {
			Program.stringExport("\t");
		}
		if (value.equals("null"))
			Program.stringExport("var " + name + ";\n");
		else
			Program.stringExport("var " + name + " = " + value + ";\n");
	}
}

class DefineColor extends declare {
	int color;
	String Type;

	DefineColor(int color, String Type) {
		this.color = color;
		this.Type = Type;
	}
}

class Array extends declare {
	String name;
	int color;

	int x, y;
	ArrayList<ArrayList<String>> matrix = new ArrayList<ArrayList<String>>();

	Array(int y, int x, int color, String name) {
		this.y = y;
		this.x = x;
		for (int i = 0; i < y; i++) {
			ArrayList<String> row = new ArrayList<String>();
			for (int j = 0; j < x; j++) {
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

// 잠시 대기
	public void display(int k) {
		for (int tab = 0; tab < k; tab++) {
			Program.stringExport("\t");
		}
//		Program.stringExport(color + " " + name + " = " + matrix);
		if (isDeclare(matrix)) {
			Program.stringExport("var " + name + " = [");
			for (int rows = 0; rows < y; rows++) {
				if (rows != 0 && rows != y)
					Program.stringExport(", ");
				Program.stringExport("[ ]");
			}
			Program.stringExport("];\n");
		} else {
			Program.stringExport("var " + name + " = " + matrix + ";\n");
		}
	}

	private boolean isDeclare(ArrayList<ArrayList<String>> temp) {
		boolean status = true;
		for (int start = 0; start < y; start++) {
			for (int end = 0; end < x; end++) {
				if (!temp.get(start).get(end).equals("null")) {
					status = false;
					break;
				}
			}
		}

		return status;
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

	Type(String t) {
		id = t;
	}

	public String toString() {
		return id;
	}
}

abstract class Statement {
	public void display(int k) {
	}
}

class Skip extends Statement {
}

class Block extends Statement {
	public ArrayList<Statement> members = new ArrayList<Statement>();

	public void display(int k) {
		for (int i = 0; i < members.size(); i++)
			members.get(i).display(k);
	}
	
	public void display2(int k) {
		for (int i = 0; i < members.size(); i++) {
			for (int tab = 0; tab < k; tab++) {
				Program.stringExport("\t");
			}
			members.get(i).display(k);
		}
	}
}

class Assignment extends Statement {
	Variable target;
	Expression source;

	Assignment(Variable t, Expression e) {
		target = t;
		source = e;
	}

	public void display(int k) {
		for (int tab = 0; tab < k; tab++) {
			Program.stringExport("\t");
		}
		target.display(k);
		Program.stringExport(" = ");
		source.display(k);
		Program.stringExport(";\n");
	}
}

class Conditional extends Statement {
	ArrayList<Expression> Exprs;
	ArrayList<Block> Statements;
	ArrayList<Expression> Ret;
	Conditional(ArrayList<Expression> E, ArrayList<Block> S, ArrayList<Expression> R) {
		Exprs = E;
		Statements = S;
		Ret = R;
	}

	public void display(int k) {
		Program.stringExport("\n");
		for (int tab = 0; tab < k; tab++) {
			Program.stringExport("\t");
		}
		Program.stringExport("if (");
		Exprs.get(0).display(k);
		Program.stringExport(") {\n");
		Statements.get(0).display2(k);
		for (int tab = 0; tab < k; tab++) {
			Program.stringExport("\t");
		}
		if(Ret.get(0)!=null) {
			Program.stringExport("\t");
			Program.stringExport("return ");			
			Ret.get(0).display(k);
			Program.stringExport(";");			
			Program.stringExport("\n");			
		}
		for (int tab = 0; tab < k; tab++) {
			Program.stringExport("\t");
		}
		
		Program.stringExport("}\n");
		for (int i = 1; i < Exprs.size(); i++) {
			for (int tab = 0; tab < k; tab++) {
				Program.stringExport("\t");
			}
			Program.stringExport("else if (");
			Exprs.get(i).display(k);
			Program.stringExport(") {\n");
			Statements.get(i).display2(k);
			for (int tab = 0; tab < k; tab++) {
				Program.stringExport("\t");
			}

			if(Ret.get(i)!=null) {
				Program.stringExport("\t");
				Program.stringExport("return ");			
				Ret.get(i).display(k);
				Program.stringExport(";");			
				Program.stringExport("\n");			
			}
			for (int tab = 0; tab < k; tab++) {
				Program.stringExport("\t");
			}
			
			
			
			
			Program.stringExport("}\n");
		}
	}
}

class Loop extends Statement {
	ArrayList<Expression> Exprs;
	ArrayList<Block> Statements;
	ArrayList<Expression> Ret;

	Loop(ArrayList<Expression> E, ArrayList<Block> S, ArrayList<Expression> R) {
		Exprs = E;
		Statements = S;
		Ret = R;
	}

	public void display(int k) {
		Program.stringExport("\n");
		for (int tab = 0; tab < k; tab++) {
			Program.stringExport("\t");
		}
		k++;
		Program.stringExport("while(true){\n");
		for (int tab = 0; tab < k; tab++) {
			Program.stringExport("\t");
		}
		Program.stringExport("if (");
		Exprs.get(0).display(k);
		Program.stringExport(") {\n");
		Statements.get(0).display2(k - 1);
		for (int tab = 0; tab < k; tab++) {
			Program.stringExport("\t");
		}
		if(Ret.get(0)!=null) {
			Program.stringExport("\t");
			Program.stringExport("return ");			
			Ret.get(0).display(k);
			Program.stringExport(";");			
			Program.stringExport("\n");			
		}
		for (int tab = 0; tab < k; tab++) {
			Program.stringExport("\t");
		}

		Program.stringExport("}\n");
		for (int i = 1; i < Exprs.size(); i++) {
			for (int tab = 0; tab < k; tab++) {
				Program.stringExport("\t");
			}
			Program.stringExport("else if (");
			Exprs.get(i).display(k);
			Program.stringExport(") {\n");
			Statements.get(i).display2(k - 1);
			for (int tab = 0; tab < k; tab++) {
				Program.stringExport("\t");
			}
			if(Ret.get(i)!=null) {
				Program.stringExport("\t");
				Program.stringExport("return ");			
				Ret.get(i).display(k);
				Program.stringExport(";");			
				Program.stringExport("\n");			
			}
			for (int tab = 0; tab < k; tab++) {
				Program.stringExport("\t");
			}
			Program.stringExport("}\n");
		}
		for (int tab = 0; tab < k; tab++) {
			Program.stringExport("\t");
		}
		k--;
		Program.stringExport("else break;\n");
		for (int tab = 0; tab < k; tab++) {
			Program.stringExport("\t");
		}
		Program.stringExport("}\n");
	}
}

class Prt extends Statement {
	ArrayList<String> list = new ArrayList<String>();

	Prt(ArrayList<String> l) {
		this.list = l;
	}

	public void display(int k) {
		for (int tab = 0; tab < k; tab++) {
			Program.stringExport("\t");
		}
		Program.stringExport("alert(");
		for (int i = 0; i < list.size(); i++) {
			if (i != 0)
				Program.stringExport(" + ");
			if (list.get(i).equals("blk"))
				Program.stringExport("\" \"");
			else
				Program.stringExport(list.get(i));
		}
		Program.stringExport(");\n");
	}
}

class Scn extends Statement {
	String pri;
	String value;

	Scn(String pri, String v) {
		this.pri = pri;
		this.value = v;
	}

	public void display(int k) {
		for (int tab = 0; tab < k; tab++) {
			Program.stringExport("\t");
		}
		Program.stringExport(value + " = prompt(" + pri + ");\n");
		for (int tab = 0; tab < k; tab++) {
			Program.stringExport("\t");
		}
		Program.stringExport(value + " = " + value + ".split(\" \");\n");
		for (int tab = 0; tab < k; tab++) {
			Program.stringExport("\t");
		}
		Program.stringExport(value + " = " + value + "[0];\n");
	}
}

class Scns extends Statement {
	String pri;
	ArrayList<String> values = new ArrayList<String>();

	Scns(String pri, ArrayList<String> v) {
		this.pri = pri;
		this.values = v;
	}

	public void display(int k) {
		for (int tab = 0; tab < k; tab++) {
			Program.stringExport("\t");
		}
		int len = values.size();
		Program.stringExport(values.get(len - 1) + " = prompt(" + pri + ");\n");
		for (int tab = 0; tab < k; tab++) {
			Program.stringExport("\t");
		}
		Program.stringExport(values.get(len - 1) + " = " + values.get(len - 1) + ".split(\" \");\n");
		for (int i = 0; i < len; i++) {
			for (int tab = 0; tab < k; tab++) {
				Program.stringExport("\t");
			}
			Program.stringExport(values.get(i) + " = " + values.get(len - 1) + "[" + i + "];\n");
		}
	}
}

class Def extends Statement {
	String name;
	Block b;
	Declarations Ds;
	ArrayList<Integer> Parameter_type;
	ArrayList<String> Parameter_name;
	Expression ret;

	Def(String n, ArrayList<Integer> t, ArrayList<String> s, Declarations Ds, Block b, Expression r) {
		this.name = n;
		this.Parameter_type = t;
		this.Parameter_name = s;
		this.Ds = Ds;
		this.b = b;
		this.ret = r;
	}

	public void display(int k) {
		Program.stringExport("\n");
		for (int tab = 0; tab < k; tab++) {
			Program.stringExport("\t");
		}
		Program.stringExport("function " + name + "(");
		Program.stringExport(Parameter_name.get(0));
		for (int i = 1; i < Parameter_name.size(); i++)
			Program.stringExport(", " + Parameter_name.get(i));
		Program.stringExport(") {\n");
		Program.stringExport("\t");
		Ds.display(k);
		b.display2(k);
		for (int tab = 0; tab < k + 1; tab++) {
			Program.stringExport("\t");
		}
		Program.stringExport("return ");
		ret.display(k);
		Program.stringExport(";\n");
		for (int tab = 0; tab < k; tab++) {
			Program.stringExport("\t");
		}
		Program.stringExport("}\n");
	}
}

class Com extends Statement {
	String comment;

	Com(String c) {
		this.comment = c;
	}
}

class Coms extends Statement {
	String comment;

	Coms(String c) {
		this.comment = c;
	}
}

abstract class Expression {
	public void display(int k) {
	}
}

class Variable extends Expression {
	private String id;
	int row = -1;
	int col = -1;

	Variable(String s) {
		id = s;
	}

	public String toString() {
		return id;
	}

	public boolean equals(Object obj) {
		String s = ((Variable) obj).id;
		return id.equals(s);
	}

	public int hashCode() {
		return id.hashCode();
	}

	public void display(int k) {
		if(row == -1) {
			if(col == -1) {
				Program.stringExport(id);				
			} else {
				Program.stringExport(id);								
				Program.stringExport("[");
				Program.stringExport(Integer.toString(col));
				Program.stringExport("]");
			}
		} else {
			Program.stringExport(id);								
			Program.stringExport("[");
			Program.stringExport(Integer.toString(row));
			Program.stringExport("]");			
			Program.stringExport("[");
			Program.stringExport(Integer.toString(col));
			Program.stringExport("]");			
		}
	}
}

class ArrayValue extends Expression {
	private String id;
	private int row = -1;
	private int col = -1;

	ArrayValue(String value, int row, int col) {
		this.row = row;
		this.col = col;
		this.id = value;
	}

	public String toString() {
		return id;
	}

	public boolean equals(Object obj) {
		String s = ((ArrayValue) obj).id;
		return id.equals(s);
	}

	public int hashCode() {
		return id.hashCode();
	}

	public void display(int k) {
		Program.stringExport(id + "[" + row + "][" + col + "]");
	}
}

class FuncValue extends Expression {
	String name;
	ArrayList<Expression> Parameter_name;

	FuncValue(String n, ArrayList<Expression> ex) {
		this.name = n;
		this.Parameter_name = ex;
	}

	public void display(int k) {
		Program.stringExport(name+"(");
		Parameter_name.get(0).display(0);
		for(int i = 1;i<Parameter_name.size();i++) {
			Program.stringExport(",");
			Parameter_name.get(i).display(0);
		}
		Program.stringExport(")");
	}
}

abstract class Value extends Expression {
	protected Type type;
	protected boolean undef = true;

	int intValue() {
		assert false : "should never reach here";
		return 0;
	}

	boolean boolValue() {
		assert false : "should never reach here";
		return false;
	}

	String charValue() {
		assert false : "should never reach here";
		return " ";
	}

	float floatValue() {
		assert false : "should never reach here";
		return 0.0f;
	}

	boolean isUndef() {
		return undef;
	}

	Type type() {
		return type;
	}

	static Value mkValue(Type type) {
		if (type == Type.INT)
			return new IntValue();
		if (type == Type.BOOL)
			return new BoolValue();
		if (type == Type.CHAR)
			return new CharValue();
		if (type == Type.FLOAT)
			return new FloatValue();
		throw new IllegalArgumentException("Illegal type in mkValue");
	}

	public void display(int k) {
		Program.stringExport(type + "");
	}
}

class IntValue extends Value {
	private int value = 0;

	IntValue() {
		type = Type.INT;
	}

	IntValue(int v) {
		this();
		value = v;
		undef = false;
	}

	int intValue() {
		assert !undef : "reference to undefined int value";
		return value;
	}

	public String toString() {
		if (undef)
			return "undef";
		return "" + value;
	}

	public void display(int k) {
		Program.stringExport(value + "");
	}

}

class BoolValue extends Value {
	private boolean value = false;

	BoolValue() {
		type = Type.BOOL;
	}

	BoolValue(boolean v) {
		this();
		value = v;
		undef = false;
	}

	boolean boolValue() {
		assert !undef : "reference to undefined bool value";
		return value;
	}

	int intValue() {
		assert !undef : "reference to undefined bool value";
		return value ? 1 : 0;
	}

	public String toString() {
		if (undef)
			return "undef";
		return "" + value;
	}

	public void display(int k) {
		Program.stringExport(value + "");
	}
}

class CharValue extends Value {
	private String value = "";

	CharValue() {
		type = Type.CHAR;
	}

	CharValue(String v) {
		this();
		value = v;
		undef = false;
	}

	String charValue() {
		assert !undef : "reference to undefined char value";
		return value;
	}

	public String toString() {
		if (undef)
			return "undef";
		return "" + value;
	}

	public void display(int k) {
		Program.stringExport(value);
	}
}

class FloatValue extends Value {
	private float value = 0;

	FloatValue() {
		type = Type.FLOAT;
	}

	FloatValue(float v) {
		this();
		value = v;
		undef = false;
	}

	float floatValue() {
		assert !undef : "reference to undefined float value";
		return value;
	}

	public String toString() {
		if (undef)
			return "undef";
		return "" + value;
	}

	public void display(int k) {
		Program.stringExport(value + "");
	}
}

class Binary extends Expression {
	Operator op;
	Expression term1, term2;

	Binary(Operator o, Expression l, Expression r) {
		op = o;
		term1 = l;
		term2 = r;
	}

	public void display(int k) {
		if (op.val.equals(op.TE)) {
			Program.stringExport("typeof(");
			term1.display(k);
			Program.stringExport(") ==");
			Program.stringExport(" typeof(");
			term2.display(k);
			Program.stringExport(")");
		} else {
			term1.display(k);
			op.display(k);
			term2.display(k);
		}

//      Program.stringExport();
	}
}

class Unary extends Expression {
	Operator op;
	Expression term;

	Unary(Operator o, Expression e) {
		op = o;
		term = e;
	}

	public void display(int k) {
		op.display(k);
		term.display(k);
	}
}

class Operator {
	final static String AND = "&&";
	final static String OR = "||";
	final static String LT = "<";
	final static String LE = "<=";
	final static String EQ = "==";
	final static String NE = "<>";
	final static String GT = ">";
	final static String GE = ">=";
	final static String TE = "??"; // Type Equals
	final static String FE = "?="; // Full Equals
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
	final static String INT_NE = "INT<>";
	final static String INT_GT = "INT>";
	final static String INT_GE = "INT>=";
	final static String INT_FE = "INT?="; // Full Equals
	final static String INT_TE = "INT??"; // Type Equals
	final static String INT_PLUS = "INT+";
	final static String INT_MINUS = "INT-";
	final static String INT_TIMES = "INT*";
	final static String INT_DIV = "INT/";
	final static String INT_NEG = "-";
	final static String FLOAT_LT = "FLOAT<";
	final static String FLOAT_LE = "FLOAT<=";
	final static String FLOAT_EQ = "FLOAT==";
	final static String FLOAT_NE = "FLOAT<>";
	final static String FLOAT_GT = "FLOAT>";
	final static String FLOAT_GE = "FLOAT>=";
	final static String FLOAT_FE = "FLOAT?="; // Full Equals
	final static String FLOAT_TE = "FLOAT??"; // Type Equals
	final static String FLOAT_PLUS = "FLOAT+";
	final static String FLOAT_MINUS = "FLOAT-";
	final static String FLOAT_TIMES = "FLOAT*";
	final static String FLOAT_DIV = "FLOAT/";
	final static String FLOAT_NEG = "-";
	final static String CHAR_LT = "CHAR<";
	final static String CHAR_LE = "CHAR<=";
	final static String CHAR_EQ = "CHAR==";
	final static String CHAR_NE = "CHAR<>";
	final static String CHAR_GT = "CHAR>";
	final static String CHAR_GE = "CHAR>=";
	final static String CHAR_FE = "CHAR?="; // Full Equals
	final static String CHAR_TE = "CHAR??"; // Type Equals
	final static String BOOL_LT = "BOOL<";
	final static String BOOL_LE = "BOOL<=";
	final static String BOOL_EQ = "BOOL==";
	final static String BOOL_NE = "BOOL<>";
	final static String BOOL_GT = "BOOL>";
	final static String BOOL_GE = "BOOL>=";
	final static String BOOL_FE = "BOOL?="; // Full Equals
	final static String BOOL_TE = "BOOL??"; // Type Equals
	final static String I2F = "I2F";
	final static String F2I = "F2I";
	final static String C2I = "C2I";
	final static String I2C = "I2C";

	String val;

	Operator(String s) {
		if (s.equals(FE))
			val = "===";
		else if (s.equals(NE))
			val = "!=";
		else
			val = s;
	}

	public String toString() {
		return val;
	}

	public void display(int k) {
		Program.stringExport(" " + val + " ");
	}

	public boolean equals(Object obj) {
		return val.equals(obj);
	}

	boolean BooleanOp() {
		return val.equals(AND) || val.equals(OR);
	}

	boolean RelationalOp() {
		return val.equals(LT) || val.equals(LE) || val.equals(EQ) || val.equals(NE) || val.equals(FE) || val.equals(TE)
				|| val.equals(GT) || val.equals(GE);
	}

	boolean ArithmeticOp() {
		return val.equals(PLUS) || val.equals(MINUS) || val.equals(TIMES) || val.equals(DIV);
	}

	boolean NotOp() {
		return val.equals(NOT);
	}

	boolean NegateOp() {
		return val.equals(NEG);
	}

	boolean intOp() {
		return val.equals(INT);
	}

	boolean floatOp() {
		return val.equals(FLOAT);
	}

	boolean charOp() {
		return val.equals(CHAR);
	}

	final static String intMap[][] = { { PLUS, INT_PLUS }, { MINUS, INT_MINUS }, { TIMES, INT_TIMES }, { DIV, INT_DIV },
			{ EQ, INT_EQ }, { NE, INT_NE }, { FE, INT_FE }, { TE, INT_TE }, { LT, INT_LT }, { LE, INT_LE },
			{ GT, INT_GT }, { GE, INT_GE }, { NEG, INT_NEG }, { FLOAT, I2F }, { CHAR, I2C } };

	final static String floatMap[][] = { { PLUS, FLOAT_PLUS }, { MINUS, FLOAT_MINUS }, { TIMES, FLOAT_TIMES },
			{ DIV, FLOAT_DIV }, { EQ, FLOAT_EQ }, { NE, FLOAT_NE }, { TE, FLOAT_TE }, { FE, FLOAT_FE },
			{ LT, FLOAT_LT }, { LE, FLOAT_LE }, { GT, FLOAT_GT }, { GE, FLOAT_GE }, { NEG, FLOAT_NEG }, { INT, F2I } };

	final static String charMap[][] = { { EQ, CHAR_EQ }, { NE, CHAR_NE }, { LT, CHAR_LT }, { LE, CHAR_LE },
			{ TE, CHAR_TE }, { FE, CHAR_FE }, { GT, CHAR_GT }, { GE, CHAR_GE }, { INT, C2I } };

	final static String boolMap[][] = { { EQ, BOOL_EQ }, { NE, BOOL_NE }, { LT, BOOL_LT }, { LE, BOOL_LE },
			{ FE, BOOL_FE }, { TE, BOOL_TE }, { GT, BOOL_GT }, { GE, BOOL_GE }, };

	final static private Operator map(String[][] tmap, String op) {
		for (int i = 0; i < tmap.length; i++)
			if (tmap[i][0].equals(op))
				return new Operator(tmap[i][1]);
		assert false : "should never reach here";
		return null;
	}

	final static public Operator intMap(String op) {
		return map(intMap, op);
	}

	final static public Operator floatMap(String op) {
		return map(floatMap, op);
	}

	final static public Operator charMap(String op) {
		return map(charMap, op);
	}

	final static public Operator boolMap(String op) {
		return map(boolMap, op);
	}
}