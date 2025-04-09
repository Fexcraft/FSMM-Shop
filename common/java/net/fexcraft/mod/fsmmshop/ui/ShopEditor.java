package net.fexcraft.mod.fsmmshop.ui;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.mod.fsmm.util.Config;
import net.fexcraft.mod.uni.EnvInfo;
import net.fexcraft.mod.uni.tag.TagCW;
import net.fexcraft.mod.uni.ui.ContainerInterface;
import net.fexcraft.mod.uni.ui.UIButton;
import net.fexcraft.mod.uni.ui.UserInterface;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class ShopEditor extends UserInterface {

	private ShopEditorCon con;

	public ShopEditor(JsonMap map, ContainerInterface container) throws Exception{
		super(map, container);
		con = (ShopEditorCon)container;
	}

	@Override
	public void init(){
		fields.get("price").text(Config.getWorthAsString(con.shop.price, false));
	}

	@Override
	public boolean onAction(UIButton button, String id, int x, int y, int mb){
		switch(id){
			case "admin":{
				TagCW com = TagCW.create();
				com.set("task", "admin");
				container.SEND_TO_SERVER.accept(com);
				return true;
			}
			case "confirm":{
				TagCW com = TagCW.create();
				com.set("task", "price");
				com.set("price", format());
				container.SEND_TO_SERVER.accept(com);
				return true;
			}
			case "sell":{
				TagCW com = TagCW.create();
				com.set("task", "sell");
				container.SEND_TO_SERVER.accept(com);
				return true;
			}
			case "buy":{
				TagCW com = TagCW.create();
				com.set("task", "buy");
				container.SEND_TO_SERVER.accept(com);
				return true;
			}
			case "item":{
				TagCW com = TagCW.create();
				com.set("task", "item");
				container.SEND_TO_SERVER.accept(com);
				return true;
			}
		}
		return false;
	}

	@Override
	public void predraw(float pticks, int mouseX, int mouseY) {
		buttons.get("sell").tx = buttons.get("sell").htx = con.shop.sell ? 31 : 101;
		buttons.get("buy").tx = buttons.get("buy").htx = con.shop.sell ? 101 : 31;
		if(con.shop.admin){
			texts.get("title").transval("ui.fsmmshop.shop_name.admin");
		}
		else{
			if(con.shop.owner == null){
				texts.get("title").transval("ui.fsmmshop.shop_name.new");
			}
			else{
				texts.get("title").transval("ui.fsmmshop.shop_name.owned", con.shop.oname);
			}
		}
	}

	@Override
	public void postdraw(float pticks, int mouseX, int mouseY){
		drawer.draw(gLeft + 10, gTop + 24, con.shop.stack);
	}

	@Override
	public void getTooltip(int mx, int my, List<String> list){
		if(buttons.get("admin").hovered()){
			list.add(drawer.translate("ui.fsmmshop.shop_editor.admin." + (con.shop.admin ? "active" : "inactive")));
			list.add(drawer.translate("ui.fsmmshop.shop_editor.admin.toggle"));
		}
		if(mx >= gLeft + 10 && mx <= gLeft + 26 && my >= gTop + 24 && my <= gTop + 40){
			list.add(con.shop.stack.getName());
			//todo stack info
			list.add(con.shop.stack.getID() + " | " + con.shop.stack.damage());
		}
	}

	private static final DecimalFormat df = new DecimalFormat("#.000", new DecimalFormatSymbols(Locale.US));
	static { df.setRoundingMode(RoundingMode.DOWN); }

	private final long format(){
		try{
			String str = fields.get("price").text().replace(Config.DOT, "").replace(",", ".");
			if(str.length() == 0) return 0;
			String format = df.format(Double.parseDouble(str));
			return Long.parseLong(format.replace(",", "").replace(".", ""));
		}
		catch(Exception e){
			con.player.entity.send("INVALID INPUT: " + e.getMessage());
			if(EnvInfo.DEV) e.printStackTrace();
			return 0;
		}
	}

}
