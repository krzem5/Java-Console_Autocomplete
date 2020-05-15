package com.krzem.console_autocomplete;



import com.sun.jna.Function;
import com.sun.jna.platform.win32.WinDef.BOOL;
import com.sun.jna.platform.win32.WinDef.DWORD;
import com.sun.jna.platform.win32.WinDef.DWORDByReference;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.Character;
import java.lang.Exception;
import java.lang.Math;
import java.util.ArrayList;
import java.util.List;



public abstract class Autocomplete{
	public static final class ANSI{
		public static final String RESET_ALL="\033[0m";

		public static final String BRIGHT="\033[1m";
		public static final String DIM="\033[2m";
		public static final String UNDERLINE="\033[4m";
		public static final String NORMAL="\033[22m";

		public static final String TITLE="\033]2;%s";

		public static final class CURSOR{
			public static final String UP="\033[%dA";
			public static final String DOWN="\033[%dB";
			public static final String RIGHT="\033[%dC";
			public static final String LEFT="\033[%dD";
			public static final String NEXT_LINE="\033[%dE";
			public static final String PREVIOUS_LINE="\033[%dF";
			public static final String COLUMN="\033[%dG";
			public static final String POSITION="\033[%d;%dH";
			public static final String ERASE_DISPLAY="\033[%dJ";
			public static final String ERASE_LINE="\033[%dK";
			public static final String SCROLL_UP="\033[%dS";
			public static final String SCROLL_DOWN="\033[%dT";
			public static final String HV_POSITION="\033[%d;%df";
			public static final String SGR="\033[%d %d";
			public static final String AUX_PORT_ON="\033[5i";
			public static final String AUX_PORT_OFF="\033[4i";
			public static final String REPORT_POSITION="\033[6n";
		}

		public static final class FOREGROUND{
			public static final String BLACK="\033[30m";
			public static final String RED="\033[31m";
			public static final String GREEN="\033[32m";
			public static final String YELLOW="\033[33m";
			public static final String BLUE="\033[34m";
			public static final String MAGENTA="\033[35m";
			public static final String CYAN="\033[36m";
			public static final String WHITE="\033[37m";
			public static final String RESET="\033[39m";

			public static final String LIGHT_BLACK="\033[90m";
			public static final String LIGHT_RED="\033[91m";
			public static final String LIGHT_GREEN="\033[92m";
			public static final String LIGHT_YELLOW="\033[93m";
			public static final String LIGHT_BLUE="\033[94m";
			public static final String LIGHT_MAGENTA="\033[95m";
			public static final String LIGHT_CYAN="\033[96m";
			public static final String LIGHT_WHITE="\033[97m";

			public static final String CUSTOM="\033[38;2;%d;%d;%dm";
		}

		public static final class BACKGROUND{
			public static final String BLACK="\033[40m";
			public static final String RED="\033[41m";
			public static final String GREEN="\033[42m";
			public static final String YELLOW="\033[43m";
			public static final String BLUE="\033[44m";
			public static final String MAGENTA="\033[45m";
			public static final String CYAN="\033[46m";
			public static final String WHITE="\033[47m";
			public static final String RESET="\033[49m";

			public static final String LIGHT_BLACK="\033[100m";
			public static final String LIGHT_RED="\033[101m";
			public static final String LIGHT_GREEN="\033[102m";
			public static final String LIGHT_YELLOW="\033[103m";
			public static final String LIGHT_BLUE="\033[104m";
			public static final String LIGHT_MAGENTA="\033[105m";
			public static final String LIGHT_CYAN="\033[106m";
			public static final String LIGHT_WHITE="\033[107m";

			public static final String CUSTOM="\033[48;2;%d;%d;%dm";
		}
	}



	private BufferedReader _r;



	public Autocomplete(){
		this._setup();
	}



	public abstract String autocomplete(String s);



	public abstract String highlight(String s);



	public final String match(String s,String[] l){
		String o=null;
		double b=0;
		for (String t:l){
			if (t.startsWith(s)==false){
				continue;
			}
			double v=(double)s.length()/t.length();
			if (v>b){
				o=t;
				b=v;
			}
		}
		return o;
	}



	public final String get(){
		try{
			String bf="";
			String bf_a=null;
			List<String> ctrl_z=new ArrayList<String>();
			while (true){
				char c=(char)this._r.read();
				switch ((int)c){
					case 3:
						System.out.print(String.format(Autocomplete.ANSI.CURSOR.ERASE_LINE+Autocomplete.ANSI.CURSOR.COLUMN+Autocomplete.ANSI.RESET_ALL,2,1));
						System.exit(1);
						break;
					case 8:
						ctrl_z.add(bf+"");
						if (bf_a==null){
							bf=bf.substring(0,Math.max(bf.length()-1,0));
						}
						else{
							bf=bf_a;
							bf_a=null;
						}
						break;
					case 9:
						String ac=this.autocomplete(bf);
						ctrl_z.add(bf+"");
						if (ac.length()>0&&bf_a==null){
							bf_a=bf+"";
							bf+=ac;
						}
						else{
							bf+="\t";
							bf_a=null;
						}
						break;
					case 13:
						System.out.print(String.format(Autocomplete.ANSI.CURSOR.ERASE_LINE+Autocomplete.ANSI.CURSOR.COLUMN,2,1));
						return bf;
					case 65535:
						bf=(ctrl_z.size()>0?ctrl_z.remove(ctrl_z.size()-1):bf);
						bf_a=null;
						break;
					default:
						if (Character.isISOControl((int)c)==false){
							ctrl_z.add(bf+"");
							bf+=String.valueOf(c);
							bf_a=null;
						}
						break;
				}
				System.out.print(String.format(Autocomplete.ANSI.CURSOR.ERASE_LINE+Autocomplete.ANSI.CURSOR.COLUMN+Autocomplete.ANSI.RESET_ALL+this._escape(bf).replace("\t","    ")+Autocomplete.ANSI.FOREGROUND.BLACK+Autocomplete.ANSI.BRIGHT+this.autocomplete(bf)+Autocomplete.ANSI.RESET_ALL+Autocomplete.ANSI.CURSOR.COLUMN,2,1,bf.replace("\t","    ").length()+1));
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}



	private String _escape(String s){
		s=this.highlight(s);
		int i=0;
		String o="";
		while (i<s.length()){
			if (s.charAt(i)=='{'&&(i==s.length()-1||s.charAt(i+1)!='{')){
				String[] cl=s.substring(i+1).split("}")[0].split(",");
				o+=String.format(Autocomplete.ANSI.FOREGROUND.CUSTOM,Integer.parseInt(cl[0]),Integer.parseInt(cl[1]),Integer.parseInt(cl[2]));
				i+=String.join(",",cl).length()+1;
			}
			else{
				if (s.charAt(i)=='{'){
					o+="{";
					i++;
				}
				o+=s.substring(i,i+1);
			}
			i++;
		}
		return o;
	}



	private void _setup(){
		HANDLE ho=(HANDLE)Function.getFunction("kernel32","GetStdHandle").invoke(HANDLE.class,new Object[]{new DWORD(-10)});
		DWORDByReference p_dw_m=new DWORDByReference(new DWORD(0));
		Function.getFunction("kernel32","GetConsoleMode").invoke(BOOL.class,new Object[]{ho,p_dw_m});
		DWORD dw_m=p_dw_m.getValue();
		dw_m.setValue(0);
		Function.getFunction("kernel32","SetConsoleMode").invoke(BOOL.class,new Object[]{ho,dw_m});
		ho=(HANDLE)Function.getFunction("kernel32","GetStdHandle").invoke(HANDLE.class,new Object[]{new DWORD(-11)});
		p_dw_m=new DWORDByReference(new DWORD(0));
		Function.getFunction("kernel32","GetConsoleMode").invoke(BOOL.class,new Object[]{ho,p_dw_m});
		dw_m=p_dw_m.getValue();
		dw_m.setValue(dw_m.intValue()|4);
		Function.getFunction("kernel32","SetConsoleMode").invoke(BOOL.class,new Object[]{ho,dw_m});
		this._r=new BufferedReader(new InputStreamReader(System.in));
	}
}