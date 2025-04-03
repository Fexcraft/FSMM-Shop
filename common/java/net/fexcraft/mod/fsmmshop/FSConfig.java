package net.fexcraft.mod.fsmmshop;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.mod.uni.ConfigBase;

import java.io.File;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class FSConfig extends ConfigBase {

	public static int MAXUSEBALANCE;
	public static String MAXUSEBALMSG;

	public FSConfig(File fl){
		super(fl, "FSMM Shop Block");
	}

	@Override
	protected void fillInfo(JsonMap map){
		map.add("info", "FSMM Shop Block Configuration File");
		map.add("wiki", "https://fexcraft.net/wiki/mod/fsmm");
	}

	@Override
	protected void fillEntries(){
		String cat = "general";
		entries.add(new ConfigEntry(this, cat, "max-use-balance", 0).rang(0, Integer.MAX_VALUE)
			.info("If player balance is higher than this value, the shop can't be used. Use `0` to disable.")
			.cons((con, map) -> MAXUSEBALANCE = con.getInteger(map))
			.req(false, false)
		);
		entries.add(new ConfigEntry(this, cat, "max-use-balance-msg", "fsmmshop.max-use-balance-msg")
			.info("Message to show in chat if the player's balance is over the max-use-balance value. Supports lang entries or literal text.")
			.cons((con, map) -> MAXUSEBALMSG = con.getString(map))
			.req(false, false)
		);
	}

	@Override
	protected void onReload(JsonMap map){
		//
	}

}
