import java.util.*;

class colorTuple{
	int color;
	String Type;
	colorTuple(int color, String Type){
		this.color = color;
		this.Type = Type;
		
	}
}
class valueTuple{
	String name;
	String type;
	valueTuple(int color, String name, ArrayList<colorTuple> colorTable){
		this.name = name;
		
		for(int i=0;i<colorTable.size();i++) {
			if(colorTable.get(i).color == color) {
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
	
	TypeChecker(Program prog){
		this.prog = prog;
	}

	public boolean ValidationStart(){
		if(CheckDeclaration()) {
			if(CheckBody()) {
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
		for(int i=0; i<prog.decpart.size();i++) {
			boolean not_already_color = true;
			if(prog.decpart.get(i).getClass().getName()=="DefineColor") {
				for(int p=0;p<colorTable.size();p++) {
					if(colorTable.get(p).color == ((DefineColor)(prog.decpart.get(i))).color) {
						errorMessage = "already defined color";
						return false;
					}
					if(colorTable.get(p).Type == ((DefineColor)(prog.decpart.get(i))).Type) {
						errorMessage = "already defined Type";
						return false;
						
					}
				}
				colorTable.add(new colorTuple(((DefineColor)(prog.decpart.get(i))).color,((DefineColor)(prog.decpart.get(i))).Type));
			}
			if(prog.decpart.get(i).getClass().getName()=="Declaration") {
				for(int p=0;p<colorTable.size();p++) {
					if(colorTable.get(p).color==((Declaration)(prog.decpart.get(i))).color) {
						not_already_color = false;
						break;
					}
				}
				if(not_already_color) {
					errorMessage = "not declared color";
					return false;
				} else {
					valueTable.add(new valueTuple(
						((Declaration)(prog.decpart.get(i))).color,
						((Declaration)(prog.decpart.get(i))).name,
						colorTable
					));
				}
			}
			if(prog.decpart.get(i).getClass().getName()=="Array") {
				for(int p=0;p<colorTable.size();p++) {
					if(colorTable.get(p).color==((Array)(prog.decpart.get(i))).color) {
						not_already_color = false;
						break;
					}
				}
				if(not_already_color) {
					errorMessage = "not declared color";
					return false;
				} else {
					valueTable.add(new valueTuple(
							((Array)(prog.decpart.get(i))).color,
							((Array)(prog.decpart.get(i))).name,
							colorTable
					));
				}
			}
    	}
		return true;
	}
	public boolean CheckBody() {
		for(int j=0; j<prog.body.members.size();j++) {
			if(checkBodyError(prog.body.members.get(j))) {
				
			} else {
				return false;
			}
    	}		
		return true;
	}
	public boolean checkBodyError(Statement s) {

		if(s.getClass().getName() == "Assignment") {
			if(!check_declared_value(((Assignment)s).target.toString())) {
				return false;
			}

			System.out.println(((Assignment)s).source);
			
			
			System.out.println("AS");
		}
		else if(s.getClass().getName() == "Conditional") {
			System.out.println("Cond");
			
		}
		else if(s.getClass().getName() == "Loop") {
			System.out.println("LO");
			
		}
		else if(s.getClass().getName() == "Prt") {
			System.out.println("PR");
			
		}
		else if(s.getClass().getName() == "Scn") {
			
			System.out.println("SC");
		}
		else if(s.getClass().getName() == "Scns") {
			
			System.out.println("SCs");
		}
		else if(s.getClass().getName() == "Com") {
			
			System.out.println("Co");
		}
		else if(s.getClass().getName() == "Coms") {
			
			System.out.println("Cos");
		}
		else if(s.getClass().getName() == "Def") {
			
			System.out.println("Def");
		}
		return true;
	}
	public boolean check_declared_value(String s) {
		for(int i=0;i<valueTable.size();i++) {
			if(s.equals(valueTable.get(i).name)) {
				return true;
			}
		}
		errorMessage = "Not declared variable";
		return false;
	}

}
