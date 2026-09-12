package net.fexcraft.mod.fsmmshop;

import net.fexcraft.lib.frl.CompactModel;
import net.fexcraft.lib.frl.CompactModel.CompactGroup;
import net.fexcraft.lib.frl.CompactParserBEO;
import net.fexcraft.lib.frl.Polyhedron;

import java.io.InputStream;
import java.util.function.Supplier;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class ShopModel {

    public static CompactModel MODEL;
    public static Polyhedron top;
    public static Polyhedron bot;

    public static void init(Supplier<InputStream> stream){
		try{
			MODEL = CompactParserBEO.parse(stream.get(), 0.0625f);
			CompactGroup labels = MODEL.groups.remove("labels");
            bot = labels.polyhedrons.get(0);
            top = labels.polyhedrons.get(1);
	    }
		catch(Exception e){
			throw new RuntimeException(e);
		}
	}

}
