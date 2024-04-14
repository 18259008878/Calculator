import java.util.*;
import java.math.*;

public class CalculatorUtil {
	public static String calDAO(List<String> expr) {
		operationStack.clear();
		numberStack.clear();
		try {
			calculate(expr);
		} catch(DivisonByZeroException e) {
			return new String(e.getMessage());
		}
		return new BigDecimal(numberStack.peek()).stripTrailingZeros().toPlainString();
	}

    private static void calculate(List<String> list) throws DivisonByZeroException {
		for (String item:list) {
			if (isNumber(item)) {
				numberStack.push(item);
			} else if (item.equals("(")) {
				operationStack.push(item);
			} else if (item.equals(")")) {
				while(!operationStack.peek().equals("(")) {
					calc(operationStack.pop());
				}
				operationStack.pop();
			} else {
				while (operationStack.size()!=0
				&&getPriority(operationStack.peek())>=getPriority(item)) {
					calc(operationStack.pop());
				}
				operationStack.push(item);
			}
		}

		while (operationStack.size()!=0) {
			calc(operationStack.pop());
		}
	}

	private static void calc(String opt) throws DivisonByZeroException {
		Double res=0.;
		//二元运算符
		if (opt.equals("+")||opt.equals("－")||opt.equals("×")
		||opt.equals("÷")||opt.equals("%")) {
			Double num2=Double.parseDouble(numberStack.pop());
			Double num1=Double.parseDouble(numberStack.pop());
			if (opt.equals("+")) {
				res=num1+num2;
			} else if (opt.equals("－")) {
				res=num1-num2;
			} else if (opt.equals("×")) {
				res=num1*num2;
			} else if (opt.equals("÷")) {
				if (num2==0) {
					throw new DivisonByZeroException();
				}
				res=num1/num2;
			} else if (opt.equals("%")) {
				if (num2==0) {
					throw new DivisonByZeroException();
				}
				res=num1%num2;
			}
		}
		numberStack.push(res.toString());
	}

    public static int getPriority(String operation) {
		int priority=0;
		if (operation.equals("+")||operation.equals("－")) {
			priority=1;
		} else if (operation.equals("×")||operation.equals("÷")) {
			priority=2;
		}
		return priority;
	}

	public static boolean isNumber(String str) {
		try {
            Double.parseDouble(str);
            return true;
        } catch(Exception e){
            return false;
        }
	}
	
	private static Stack<String> numberStack=new Stack<>();
	private static Stack<String> operationStack=new Stack<>();
}

class DivisonByZeroException extends ArithmeticException {
	public DivisonByZeroException() {
		super("除数不能为零");
	}
}