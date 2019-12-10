import java.util.*;

class colorTuple {
	int color;
	String Type;

	colorTuple(int color, String Type) {
		this.color = color;
		this.Type = Type;

	}
}

class valueTuple {
	String name;
	String type;

	valueTuple(int color, String name, ArrayList<colorTuple> colorTable) {
		this.name = name;

		for (int i = 0; i < colorTable.size(); i++) {
			if (colorTable.get(i).color == color) {
				type = colorTable.get(i).Type;
			}
		}
	}
}

public class TypeChecker {
	Program prog;
	ArrayList<colorTuple> colorTable = new ArrayList<colorTuple>();
	ArrayList<valueTuple> valueTable = new ArrayList<valueTuple>();

	String errorMessage;

	TypeChecker(Program prog) {
		this.prog = prog;
	}

	public boolean ValidationStart() {
		if (CheckDeclaration()) {
			if (CheckBody()) {
				return true;
			} else {
				System.out.println("error : " + errorMessage);
				return false;
			}
		} else {
			System.out.println("error : " + errorMessage);
			return false;
		}
	}

	public boolean CheckDeclaration() {
		for (int i = 0; i < prog.decpart.size(); i++) {
			boolean not_already_color = true;
			if (prog.decpart.get(i).getClass().getName() == "DefineColor") {
				for (int p = 0; p < colorTable.size(); p++) {
					if (colorTable.get(p).color == ((DefineColor) (prog.decpart.get(i))).color) {
						errorMessage = "already defined color => " + ((DefineColor) (prog.decpart.get(i))).Type;
						return false;
					}
					if (colorTable.get(p).Type == ((DefineColor) (prog.decpart.get(i))).Type) {
						errorMessage = "already defined Type => " + ((DefineColor) (prog.decpart.get(i))).Type;
						return false;

					}
				}
				colorTable.add(new colorTuple(((DefineColor) (prog.decpart.get(i))).color,
						((DefineColor) (prog.decpart.get(i))).Type));
			}
			if (prog.decpart.get(i).getClass().getName() == "Declaration") {
				for (int p = 0; p < colorTable.size(); p++) {
					if (colorTable.get(p).color == ((Declaration) (prog.decpart.get(i))).color) {
						not_already_color = false;
						break;
					}
				}
				if (not_already_color) {
					errorMessage = "not declared color => " + ((Declaration) (prog.decpart.get(i))).name;
					return false;
				} else {
					valueTable.add(new valueTuple(((Declaration) (prog.decpart.get(i))).color,
							((Declaration) (prog.decpart.get(i))).name, colorTable));
				}
				if(!check_right_type(((Declaration)(prog.decpart.get(i))).value,((Declaration)(prog.decpart.get(i))).color,colorTable)) {
					return false;
				} 
			}
			if (prog.decpart.get(i).getClass().getName() == "Array") {
				for (int p = 0; p < colorTable.size(); p++) {
					if (colorTable.get(p).color == ((Array) (prog.decpart.get(i))).color) {
						not_already_color = false;
						break;
					}
				}
				if (not_already_color) {
					errorMessage = "not declared color => " + ((Array) (prog.decpart.get(i))).name;
					return false;
				} else {
					valueTable.add(new valueTuple(((Array) (prog.decpart.get(i))).color,
							((Array) (prog.decpart.get(i))).name, colorTable));
				}
				for(int u=0;u<((Array) (prog.decpart.get(i))).matrix.size();u++) {
					for(int s=0;s<((Array) (prog.decpart.get(i))).matrix.get(u).size();s++) {
						if(!check_right_type(((Array) (prog.decpart.get(i))).matrix.get(u).get(s),((Array) (prog.decpart.get(i))).color,colorTable)) {
							return false;
						}
					}
					
				}

			}
		}
		return true;
	}

	public boolean CheckBody() {
		for (int j = 0; j < prog.body.members.size(); j++) {
			if (checkBodyError(prog.body.members.get(j), valueTable)) {

			} else {
				return false;
			}
		}
		return true;
	}

	public boolean checkBodyError(Statement s, ArrayList<valueTuple> valueTable) {

		if (s.getClass().getName() == "Assignment") {
			if (!check_declared_value(((Assignment) s).target.toString(), valueTable)) {
				return false;
			}
			if (!check_expression((((Assignment) s).source), NameToType(((Assignment) s).target.toString(), valueTable),
					valueTable)) {
				return false;
			}
			return true;
		} else if (s.getClass().getName() == "Conditional") {
			for (int i = 0; i < ((Conditional) s).Exprs.size(); i++) {
				if (!check_expression(((Conditional) s).Exprs.get(i), "bool", valueTable)) {
					return false;
				}
			}
			for (int i = 0; i < ((Conditional) s).Statements.size(); i++) {
				if (!checkBodyError(((Conditional) s).Statements.get(i), valueTable)) {
					return false;
				}
			}
			return true;
		} else if (s.getClass().getName() == "Loop") {
			for (int i = 0; i < ((Loop) s).Exprs.size(); i++) {
				if (!check_expression(((Loop) s).Exprs.get(i), "bool", valueTable)) {
					return false;
				}
			}
			for (int i = 0; i < ((Loop) s).Statements.size(); i++) {
				if (!checkBodyError(((Loop) s).Statements.get(i), valueTable)) {
					return false;
				}
			}
			return true;
		} else if (s.getClass().getName() == "Prt") {// 고민중
//			System.out.println("PR");

		} else if (s.getClass().getName() == "Scn") {
			if (check_string(((Scn) s).pri)) {// 수정해야함
				if (check_declared_value(((Scn) s).value, valueTable)) {
					return true;
				} else {
					errorMessage = "Input have to variable => " + ((Scn) s).value;
					return false;
				}
			} else {
				errorMessage = "Print string have to String Type => " + ((Scn) s).pri;
				return false;
			}
		} else if (s.getClass().getName() == "Scns") {
			if (check_string(((Scns) s).pri)) {// 수정해야함
				for (int i = 0; i < ((Scns) s).values.size(); i++) {
					if (check_declared_value(((Scns) s).values.get(i), valueTable)) {

					} else {
						errorMessage = "Input have to variable => " + ((Scns) s).values.get(i);
						return false;
					}
				}
			} else {
				errorMessage = "Print string have to String Type => " + ((Scn) s).pri;
				return false;
			}
		} else if (s.getClass().getName() == "Com") { // 처리할게 없음

			// System.out.println("Co");
		} else if (s.getClass().getName() == "Coms") { // 처리할게 없음

			// System.out.println("Cos");
		} else if (s.getClass().getName() == "Def") {
			ArrayList<colorTuple> localColorTable = new ArrayList<colorTuple>();
			ArrayList<valueTuple> localValueTable = new ArrayList<valueTuple>();

			localColorTable.clear();
			localValueTable.clear();

			ArrayList<valueTuple> localTempTable = new ArrayList<valueTuple>();

			for (int i = 0; i < ((Def) s).Parameter_type.size(); i++) {
				if (!check_declared_color(((Def) s).Parameter_type.get(i))) {
					errorMessage = "Not declared color => " + ((Def) s).Parameter_name.get(i);
					return false;
				} else {
					localTempTable.add(new valueTuple(((Def) s).Parameter_type.get(i), ((Def) s).Parameter_name.get(i),
							colorTable));
				}
			}
			for (int i=0; i<((Def)s).Ds.size();i++) {
				if(((Def)s).Ds.get(i).getClass().getName().equals("Declaration")){					
					if(!check_declared_color(((Declaration)((Def)s).Ds.get(i)).color)) {
						errorMessage = "Not declared color => " + ((Declaration)((Def)s).Ds.get(i)).name;	
						return false;
					} else {
						localTempTable.add(new valueTuple(((Declaration)((Def)s).Ds.get(i)).color,((Declaration)((Def)s).Ds.get(i)).name,colorTable));
					}
				}
				if(((Def)s).Ds.get(i).getClass().getName().equals("Array")){					
					if(!check_declared_color(((Array)((Def)s).Ds.get(i)).color)) {
						errorMessage = "Not declared color => " + ((Array)((Def)s).Ds.get(i)).name;	
						return false;
					} else {
						localTempTable.add(new valueTuple(((Array)((Def)s).Ds.get(i)).color,((Array)((Def)s).Ds.get(i)).name,colorTable));
					}
				}
			}
			
			for (int i = 0; i < valueTable.size(); i++) {
				localValueTable.add(valueTable.get(i));
			}
			
			for (int i = 0; i < localTempTable.size(); i++) {
				boolean flag = false;

				for (int j = 0; j < localValueTable.size(); j++) {

					if (localValueTable.get(j).name.equals(localTempTable.get(i).name)) {
						localValueTable.set(j, localTempTable.get(i));
						flag = true;
					}
				}
				if (!flag) {
					localValueTable.add(localTempTable.get(i));
				}

			}
//			for(int i=0;i<localValueTable.size();i++) {
//				System.out.println(localValueTable.get(i).name);
//				System.out.println(localValueTable.get(i).type);
//			}
			for (int i = 0; i < ((Def) s).b.members.size(); i++) {
				if (!checkBodyError(((Def) s).b.members.get(i), localValueTable)) {
					return false;
				}
			}
			if (!check_expression(((Def) s).ret, "", localValueTable)) {
				return false;
			}
			// 리턴부분은 고민중....
			return true;
		}

		return true;
	}

	public String NameToType(String name, ArrayList<valueTuple> valueTable) {
		String n;
		for (int i = 0; i < valueTable.size(); i++) {
			if (valueTable.get(i).name.contentEquals(name)) {
				n = valueTable.get(i).type;
				return n;
			}
		}
		return "";
	}

	public boolean check_declared_color(int s) {
		for (int i = 0; i < colorTable.size(); i++) {
			if (s == colorTable.get(i).color) {
				return true;
			}
		}
		errorMessage = "Not declared color => " + s;
		return false;

	}

	public boolean check_declared_value(String s, ArrayList<valueTuple> valueTable) {
		for (int i = 0; i < valueTable.size(); i++) {
			if (s.equals(valueTable.get(i).name)) {
				return true;
			}
		}
		errorMessage = "Not declared variable => " + s;
		return false;
	}

	public boolean check_expression(Expression e, String type, ArrayList<valueTuple> valueTable) {
		// System.out.println(e.getClass());
		if (e.getClass().getName().equals("Binary")) {// 해결 인줄알았는데 미해결
			// System.out.println("binary!");

			if (((Binary) e).op.BooleanOp()) {
				if (check_expression(((Binary) e).term1, "bool", valueTable)) {
					if (check_expression(((Binary) e).term2, "bool", valueTable)) {
						if (type.equals(""))
							return true;
						if (type.equals("bool")) {
							return true;
						} else {
							errorMessage = "Necessary Type is not bool Type" + ((Binary) e).toString();
							return false;
						}
					} else {
						errorMessage = "Type Have To bool => " + ((Binary) e).term2.toString();
						return false;
					}
				} else {
					errorMessage = "Type Have To bool => " + ((Binary) e).term1.toString();
					return false;
				}

			} else if (((Binary) e).op.RelationalOp()) {

				if (((Binary) e).op.val == "==" || ((Binary) e).op.val == "<>") {// 이거 처리해야함?
					if (type.equals(""))
						return true;
					if (type.equals("bool")) {
						return true;
					} else {
						errorMessage = "Necessary Type is not bool Type => " + ((Binary) e).toString();
						return false;
					}

				} else {
					if (check_expression(((Binary) e).term1, "int", valueTable)
							|| check_expression(((Binary) e).term1, "float", valueTable)) {
						if (check_expression(((Binary) e).term2, "int", valueTable)
								|| check_expression(((Binary) e).term2, "float", valueTable)) {
							if (type.equals(""))
								return true;
							if (type.equals("bool")) {
								return true;
							} else {
								errorMessage = "Necessary Type is not bool Type => " + ((Binary) e).toString();
								return false;
							}
						} else {
							errorMessage = "Type Have To int/float => " + ((Binary) e).term2.toString();
							return false;
						}
					} else {
						errorMessage = "Type Have To int/float => " + ((Binary) e).term1.toString();
						return false;
					}
				}
			}
//			System.out.println("here : "+type);
			type = "int";
			if (check_expression(((Binary) e).term1, type, valueTable)) {
				if (check_expression(((Binary) e).term2, type, valueTable)) {
					if (type.equals(""))
						return true;
					if (type == "int")
						return true;
				}
			}
			type = "float";
			if (check_expression(((Binary) e).term1, type, valueTable)) {
				if (check_expression(((Binary) e).term2, type, valueTable)) {
					if (type.equals(""))
						return true;
					if (type == "float")
						return true;
				}
			}
			type = "bool";
			if (check_expression(((Binary) e).term1, type, valueTable)) {
				if (check_expression(((Binary) e).term2, type, valueTable)) {
					if (type.equals(""))
						return true;
					if (type == "bool")
						return true;
				}
			}
			type = "char";
			if (check_expression(((Binary) e).term1, type, valueTable)) {
				if (check_expression(((Binary) e).term2, type, valueTable)) {
					if (type.equals(""))
						return true;
					if (type == "char")
						return true;
				}
			}
			type = "string";
			if (check_expression(((Binary) e).term1, type, valueTable)) {
				if (check_expression(((Binary) e).term2, type, valueTable)) {
					if (type.equals(""))
						return true;
					if (type == "string")
						return true;
				}
			}
			errorMessage = "Different Type operation => " + ((Binary) e).term1.toString() + " "
					+ ((Binary) e).op.toString() + " " + ((Binary) e).term2.toString();
			return false;
//			return true;
		} else if (e.getClass().getName().equals("Unary")) { // 미해결
//			System.out.println("Unary!");
			return true;
		} else if (e.getClass().getName().equals("Variable")) {// 해결
//			System.out.println("Variable!");
			if (!check_declared_value(((Variable) e).toString(), valueTable)) {
				errorMessage = "Not declared Variable => " + ((Variable) e).toString();
				return false;
			}
			if (!NameToType(((Variable) e).toString(), valueTable).equals(type)) {
				if (type.equals(""))
					return true;
				errorMessage = "Incorrect Variable Type => " + ((Variable) e).toString();
				return false;
			}
			return true;
		} else if (e.getClass().getName().equals("ArrayValue")) {// 대충해결

			// if(((ArrayValue)e).toString())
//			System.out.println("ArrayValue!");
			if (!check_declared_value(((ArrayValue) e).toString(), valueTable)) {
				errorMessage = "Not declared ArrayVariable => " + ((ArrayValue) e).toString();
				return false;
			}
			if (!NameToType(((ArrayValue) e).toString(), valueTable).equals(type)) {
				if (type.equals(""))
					return true;
				errorMessage = "Incorrect Variable Type => " + ((ArrayValue) e).toString();
				return false;
			}

			return true;
		} else if (e.getClass().getName().equals("Value")) {// ?? 이건나오기나하나
//			System.out.println("value!");
			return true;
		} else if (e.getClass().getName().equals("IntValue")) {// 해결
//			System.out.println("IntValue!");
			if (type.equals(""))
				return true;
			if (type.equals("int")) {
				return true;
			} else {
				errorMessage = "Incorrect IntValue => " + e;
				return false;
			}
		} else if (e.getClass().getName().equals("CharValue")) {// 미해결 char처리 string이랑 구분 어케함
//			System.out.println("CharValue!");
			if (type.equals(""))
				return true;
			if (type.equals("char")) {
				return true;
			} else {
				errorMessage = "Incorrect CharValue => " + e;
				return false;
			}

		} else if (e.getClass().getName().equals("BoolValue")) {// 미해결 true=> indentifier 로들어옴
//			System.out.println("BoolValue!");
			if (type.equals(""))
				return true;
			if (type.equals("bool")) {
				return true;
			} else {
				errorMessage = "Incorrect BoolValue => " + e;
				return false;
			}

		} else if (e.getClass().getName().equals("FloatValue")) {// 해결
//			System.out.println("FloatValue!");
			if (type.equals(""))
				return true;
			if (type.equals("float")) {
				return true;
			} else {
				errorMessage = "Incorrect FloatValue => " + e;
				return false;
			}
		} else if (e.getClass().getName().equals("StringValue")) {// 만들어야함
//			System.out.println("StringValue!");
			if (type.equals(""))
				return true;
			if (type.equals("string")) {
				return true;
			} else {
				errorMessage = "Incorrect StringValue => " + e;
				return false;
			}
		} else if(e.getClass().getName().equals("FuncValue")) {
			return true;
		} else {
			System.out.println("else?");
			System.out.println(e);
			return false;
		}
	}

	public boolean check_string(String s) {// 만들어야함
		return true;
	}

	public boolean check_right_type(String s, int color,ArrayList<colorTuple> colorTable) {
		if(s.equals("null")) return true;
		String type = "";
		for(int i=0;i<colorTable.size();i++) {
			if (colorTable.get(i).color == color) {
				type = colorTable.get(i).Type;
				break;
			}
		}
		if(s.equals("true")||s.equals("false")) {
			if(type.equals("bool")) return true;
			else {
				errorMessage = "Type is Not " + type + " => " + s.toString();
				return false;
			}
		}
		if(s.charAt(0) == '"') {
			if(type.equals("string")) return true;
			else {
				errorMessage = "Type is Not " + type + " => " + s.toString();
				return false;
			}
		}
		if(s.charAt(0) == '\'') {
			if(type.equals("char")) return true;
			else {
				errorMessage = "Type is Not " + type + " => " + s.toString();
				return false;
			}
		}
		if(s.charAt(0) == '0' ||
			s.charAt(0) == '1' ||
			s.charAt(0) == '2' ||
			s.charAt(0) == '3' ||
			s.charAt(0) == '4' ||
			s.charAt(0) == '5' ||
			s.charAt(0) == '6' ||
			s.charAt(0) == '7' ||
			s.charAt(0) == '8' ||
			s.charAt(0) == '9') {

			if(s.indexOf(".")>0) {
				if(type.equals("float")) return true;
				else {
					errorMessage = "Type is Not " + type + " => " + s.toString();
					return false;
				}
			} else {
				if(type.equals("int")) return true;
				else {
					errorMessage = "Type is Not " + type + " => " + s.toString();
					return false;
				}
				
			}
		}else {
			errorMessage = "Type is Not " + type + " => " + s.toString();
			return false;
		}
	}
	
	
}
