package vong.piler.her.steakmachine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Stack;

import vong.piler.her.steakmachine.StackElement.Type;

public class Steakmachine {
	
	private static final int PROGRAM_MEMORY_SIZE = 100;
	private static final int CODE_MEMORY_SIZE = 100;
	private Stack<StackElement> stack;
	private StackElement[] programmMemory;
	private String[] codeMemory;
	
	private int instructionPointer = 0;
	private boolean running;
	
	
	
	public static void main(String[] args) {
		Steakmachine steack = new Steakmachine();
		steack.init();
		URL url = steack.getClass().getResource("variablenDeklarationDiesDas.vch");
		steack.load(new File(url.getPath()));
		steack.run();
	}
	
	private void load(File file) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
		String line;
		int i = 0;
		while((line = br.readLine()) != null) {
			codeMemory[i] = line;
			i++;			
		}
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public void init() {
		stack = new Stack<>();
		codeMemory = new String[CODE_MEMORY_SIZE];
		programmMemory = new StackElement[PROGRAM_MEMORY_SIZE];
		instructionPointer = 0;
	}

	public void run() {
		running = true;
		while(running) {
    		String rawCommand = readCommand();
    		instructionPointer++;
    		Command command = decodeCommand(rawCommand);
    		executeCommand(command);
   		
    	}
        
    }
	
    private String readCommand() {
    	String rawCommand = codeMemory[instructionPointer];
    	return rawCommand;
    }
    
    private Command decodeCommand(String rawCommand) {
    	Command command = new Command();
    	String[] commandParts = rawCommand.split(" ");
    	switch(commandParts.length) {
    	case 1:
    		command.setOpCode(OperationEnum.valueOf(commandParts[0]));
    		break;
    	case 2: 
    		command.setOpCode(OperationEnum.valueOf(commandParts[0]));
    		command.setFirstParam(commandParts[1]);
    		break;
    		default:
    			//TODO throw exception
    	}
    	return command;
    }
    
    private void executeCommand(Command command) {
    	switch(command.getOpCode()) {
    	case PSZ:
    		psz(command.getFirstParam());
    		break;
    	case ADD:
    		add();
    		break;
    	case PRT:
    		prt();
    		break;
		case AAL:
			aal();
			break;
		case AND:
			and();
			break;
		case DIV:
			div();
			break;
		case END:
			end();
			break;
		case EQZ:
			eqz();
			break;
		case EQI:
			eqi();
			break;
		case GTR:
			gtr();
			break;
		case JMP:
			jmp();
			break;
		case JMT:
			jmt();
			break;
		case LES:
			les();
			break;
		case MOD:
			mod();
			break;
		case MUL:
    		mul();
			break;
		case NQI:
			nqi();
			break;
		case NQZ:
			nqz();
			break;
		case OHR:
			ohr();
			break;
		case PSA:
			psa(command.getFirstParam());
			break;
		case PSI:
			psi(command.getFirstParam());
			break;
		case SUB:
    		sub();
			break;
		case SVA:
    		sva();
			break;
		case SVZ:
    		svz();
			break;
		case SVI:
    		svi();
			break;
		default:
			break;
    	}
    }

	private void eqz() {
		double a = popZal();
		double b = popZal();
		boolean isso = b == a;
		pushIsso(isso);	
	}
	
	private void eqi() {
		boolean a = popIsso();
		boolean b = popIsso();
		boolean isso = b == a;
		pushIsso(isso);	
	}
	
	private void nqz() {
		double a = popZal();
		double b = popZal();
		boolean isso = b != a;
		pushIsso(isso);
	}
	
	private void nqi() {
		boolean a = popIsso();
		boolean b = popIsso();
		boolean isso = b != a;
		pushIsso(isso);	
	}

	private void sub() {
		double a = popZal();
		double b = popZal();
		double result = b - a;
		pushZal(result);
	}

	private void mod() {
		double a = popZal();
		double b = popZal();
		double result = b % a;
		pushZal(result);
		
	}

	private void les() {
		double a = popZal();
		double b = popZal();
		boolean result = b < a;
		pushIsso(result);
		
	}

	private void jmt() {
		int address = popAddress();
		boolean isso = popIsso();
		if(isso) {
			instructionPointer = address;
		}	
	}

	private void jmp() {
		int address = popAddress();
		instructionPointer = address;
	}


	private void ohr() {
		boolean a = popIsso();
		boolean b = popIsso();
		boolean result = b || a;
		pushIsso(result);
		
	}

	private void psa(String firstParam) {
		pushAddress(Integer.parseInt(firstParam));
		
	}

	private void psi(String firstParam) {
		pushIsso(parseIsso(firstParam));
		
	}

	private boolean parseIsso(String firstParam) {
		int isso = Integer.parseInt(firstParam);
		return isso !=0 ? true : false;
	}

	private void sva() {
		int address = popAddress();
		int storeAddress = popAddress();
		StackElement element = new StackElement(Type.ADDRESS, storeAddress);
		programmMemory[storeAddress] = element;		
	}
	
	private void svi() {
		int address = popAddress();
		boolean isso = popIsso();
		StackElement element = new StackElement(Type.ISSO, isso);
		programmMemory[address] = element;		
	}
	
	private void svz() {
		int address = popAddress();
		double zal = popZal();
		StackElement element = new StackElement(Type.ZAL, zal);
		programmMemory[address] = element;			
	}

	private void gtr() {
		double a = popZal();
		double b = popZal();
		boolean result = b > a;
		pushIsso(result);
		
	}

	private void div() {
		double a = popZal();
		double b = popZal();
		double result = b / a;
		pushZal(result);		
	}

	private void and() {
		boolean a = popIsso();
		boolean b = popIsso();
		boolean result = b && a;
		pushIsso(result);		
	}

	private void aal() {
		System.out.println("Halo I bims 1 aal vong Halo W�rlt her");
	}

	private void end() {
		running = false;
		System.out.println("Programm exited without error");
	}

	private void mul() {
		double a = popZal();
		double b = popZal();
		pushZal(b * a);
	}

	private void psz(String arg) {
		pushZal(Double.parseDouble(arg));
	}

	private void prt() {
		StackElement element = stack.pop();
		String out = element.toString();
		if(element.getType() == Type.ADDRESS) {
			int address = (int) element.getValue();
			StackElement global = programmMemory[address];
			out = out + " -> " + global.toString();
		}		
		System.out.println(out);
	}

	private void add() {
		double a = popZal();
		double b = popZal();
		double result = b + a;
		pushZal(result);
	}
    
    private void pushZal(double zal) {
    	StackElement element = new StackElement(Type.ZAL, zal);
    	stack.push(element);
    }
    
    private void pushAddress(int address) {
    	StackElement element = new StackElement(Type.ADDRESS, address);
    	stack.push(element);
    }
    
    private void pushIsso(boolean isso) {
    	StackElement element = new StackElement(Type.ISSO, isso);
    	stack.push(element);
    }
    
    private double popZal() {
    	StackElement element = stack.pop();
    	double zal;
    	if(element.getType() == Type.ADDRESS) {
    		zal = loadZal((int) element.getValue());
    	}else {
    		zal = (double) element.getValue();
    	}
    	return zal;
    }
    
    private int popAddress() {
    	StackElement element = stack.pop();
    	int address = (int) element.getValue();
    	return address;
    }
    
    private boolean popIsso() {
    	StackElement element = stack.pop();
    	boolean isso;
    	if(element.getType() == Type.ADDRESS) {
    		isso = loadIsso((int) element.getValue());
    	}else {
    		isso = (boolean) element.getValue();
    	}
    	return isso;
    }
    
    private double loadZal(int address) {
    	double zal = (double) programmMemory[address].getValue();
    	return zal;
    }
 
    /*
     * TODO add for pointer support
    private int loadAddress(int pAddress) {
    	int address = (int) programmMemory[pAddress];
    	return address;
    }
    
    */
    private boolean loadIsso(int address) {
    	boolean isso = (boolean) programmMemory[address].getValue();
    	return isso;
    }


    
}
