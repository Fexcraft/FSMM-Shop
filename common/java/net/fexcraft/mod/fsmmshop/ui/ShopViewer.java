package net.fexcraft.mod.fsmmshop.ui;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.lib.common.utils.Formatter;
import net.fexcraft.lib.mc.gui.GenericGui;
import net.fexcraft.mod.fsmm.util.Config;
import net.fexcraft.mod.uni.EnvInfo;
import net.fexcraft.mod.uni.tag.TagCW;
import net.fexcraft.mod.uni.ui.ContainerInterface;
import net.fexcraft.mod.uni.ui.UIButton;
import net.fexcraft.mod.uni.ui.UserInterface;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

import static net.fexcraft.mod.uni.ui.ContainerInterface.SEND_TO_SERVER;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class ShopViewer extends UserInterface {

	private ShopViewerCon con;

	public ShopViewer(JsonMap map, ContainerInterface container) throws Exception{
		super(map, container);
		con = (ShopViewerCon)container;
	}

	@Override
	public void init(){
		TagCW com = TagCW.create();
		com.set("task", "sync");
		SEND_TO_SERVER.accept(com);
	}

	@Override
	public boolean onAction(UIButton button, String id, int x, int y, int mb){
		if(!id.startsWith("act")) return false;
		int am = 1;
		if(id.length() > 3){
			try{
				am = Integer.parseInt(id.substring(3));
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		TagCW com = TagCW.create();
		com.set("task", "act");
		com.set("amount", am);
		SEND_TO_SERVER.accept(com);
		return true;
	}

	@Override
	public void predraw(float pticks, int mouseX, int mouseY) {
		if(con.msg != null){
			texts.get("title").transval(con.msg);
		}
		else if(con.shop.admin){
			texts.get("title").transval("ui.fsmmshop.shop_name.admin");
			texts.get("amount").transval("ui.fsmmshop.shop_viewer.infinite");
		}
		else if(con.shop.owner == null){
				texts.get("title").transval("ui.fsmmshop.shop_name.new");
		}
		else{
			texts.get("title").transval("ui.fsmmshop.shop_name.owned", con.shop.oname);
		}
		if(con.shop.sell){
			texts.get("amount").transval("ui.fsmmshop.shop_viewer.stock", con.shop.stored());
		}
		else{
			int l = con.shop.limit();
			texts.get("amount").transval("ui.fsmmshop.shop_viewer.free", (l - con.shop.stored()), l);
		}
		texts.get("price").value(Config.getWorthAsString(con.shop.price));
		texts.get("balance").transval("ui.fsmmshop.shop_viewer.balance", Config.getWorthAsString(con.bal));
		buttons.get("act").text.transval(con.shop.sell ? "ui.fsmmshop.shop_viewer.buy" : "ui.fsmmshop.shop_viewer.sell");
	}

	@Override
	public void postdraw(float pticks, int mouseX, int mouseY){
		drawer.draw(gLeft + 10, gTop + 24, con.shop.stack);
	}

	@Override
	public void getTooltip(int mx, int my, List<String> list){
		if(texts.get("title").hovered()) list.add(texts.get("title").value());
		if(texts.get("amount").hovered()) list.add(texts.get("amount").value());
		if(texts.get("price").hovered()) list.add(texts.get("price").value());
		if(buttons.get("item").hovered()){
			list.add(con.shop.stack.getName());
			//todo stack info
			list.add(con.shop.stack.getID() + " | " + con.shop.stack.damage());
		}
	}

}
