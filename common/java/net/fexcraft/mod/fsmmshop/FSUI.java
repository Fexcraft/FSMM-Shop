package net.fexcraft.mod.fsmmshop;

import net.fexcraft.mod.fsmmshop.ui.ShopEditor;
import net.fexcraft.mod.fsmmshop.ui.ShopEditorCon;
import net.fexcraft.mod.uni.UniReg;
import net.fexcraft.mod.uni.ui.ContainerInterface;
import net.fexcraft.mod.uni.ui.UIKey;
import net.fexcraft.mod.uni.ui.UserInterface;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class FSUI {

	public static UIKey SHOP_EDITOR = new UIKey(0, "fsmmshop:editor");
	public static UIKey SHOP_VIEWER = new UIKey(1, "fsmmshop:viewer");

	public static void register(){
		UniReg.registerUI(SHOP_EDITOR, ShopEditor.class);
		UniReg.registerMenu(SHOP_EDITOR, "fsmmshop:uis/shop_editor", ShopEditorCon.class);
		UniReg.registerUI(SHOP_VIEWER, UserInterface.class);
		UniReg.registerMenu(SHOP_VIEWER, "fsmmshop:uis/shop_viewer", ContainerInterface.class);
	}

}
