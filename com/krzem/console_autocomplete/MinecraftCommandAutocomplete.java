package com.krzem.console_autocomplete;



import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;



public class MinecraftCommandAutocomplete extends Autocomplete{
	public static final String[] BASE_COMMANDS=new String[]{"advancement","attribute","ban","ban-ip","banlist","bossbar","clear","clone","data","datapack","debug","defaultgamemode","deop","difficulty","effect","enchant","execute","experience","fill","forceload","function","gamemode","gamerule","give","help","kick","kill","list","locate","locatebiome","loot","me","msg","op","pardon","particle","playsound","recipe","reload","remove","replaceitem","save","save-all","save-off","save-on","say","schedule","scoreboard","seed","setblock","setidletimeout","setworldspawn","spawnpoint","spectate","spreadplayers","stop","stopsound","summon","tag","team","teammsg","teleport","time","title","tp","trigger","w","weather","whitelist","worldborder","xp"};



	@Override
	public String autocomplete(String s){
		String sp=this.match(s,BASE_COMMANDS);
		if (sp!=null){
			return sp.substring(s.length());
		}
		return "";
	}



	@Override
	public String highlight(String s){
		s=s.replace("{","{{");
		if (this.match(s.split(" ")[0],BASE_COMMANDS)!=null&&this.match(s.split(" ")[0],BASE_COMMANDS).equals(s.split(" ")[0])){
			String[] sp=s.split(" ");
			sp[0]="{233,206,35}"+sp[0]+"{255,255,255}";
			return String.join(" ",sp);
		}
		return s;
	}
}