package data;

import org.tbot.internal.handlers.LogHandler;
import org.tbot.methods.Widgets;
import org.tbot.wrappers.WidgetChild;

import tasks.SwitchWorld;

public class WorldReader {
	
	private static final int EXCLUDED_WORLD = 45;
	
	public static int getWorldWidgetChildIndex(int world) {
		WidgetChild worldSwitcher = Widgets.getWidget(SwitchWorld.WORLD_SWITCHER_WIDGET, 7);
		int index = 1;
		for(int i = 2; i < worldSwitcher.getChildren().length; i+= 6) {
			if (worldSwitcher.getChild(i).getText().equals(Integer.toString(world))) {
				index = i;
				break;
			}
		}
		return index;
	}
	
	public static boolean isValidWorld(int world) {
		WidgetChild star;
		for (int i = 0; i < WorldSettings.getWorldType().length; i++) {
			star = Widgets.getWidget(SwitchWorld.WORLD_SWITCHER_WIDGET, SwitchWorld.WORLD_SWITCHER_CHILD);
			if (star != null && star.getChild(getWorldWidgetChildIndex(world) - 1) != null && star.getChild(getWorldWidgetChildIndex(world) - 1).getTextureID() == WorldSettings.getWorldType()[i].getTexture() && world != EXCLUDED_WORLD) {
				return true;
			}
		}
		return false;
	}

}
