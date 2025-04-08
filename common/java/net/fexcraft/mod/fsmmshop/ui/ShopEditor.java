package net.fexcraft.mod.fsmmshop.ui;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.mod.uni.ui.ContainerInterface;
import net.fexcraft.mod.uni.ui.UserInterface;

import java.util.List;

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
	public void predraw(float pticks, int mouseX, int mouseY) {
		buttons.get("sell").tx = con.shop.sell ? 31 : 101;
		buttons.get("buy").tx = con.shop.sell ? 101 : 31;
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

}
