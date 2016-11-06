package tasks;

import org.tbot.internal.handlers.LogHandler;
import org.tbot.methods.Mouse;
import org.tbot.methods.Random;
import org.tbot.methods.Settings;
import org.tbot.methods.Time;
import org.tbot.methods.Widgets;
import org.tbot.util.Condition;
import org.tbot.wrappers.WidgetChild;

import data.WorldReader;

/**
 * @author Mocrosoft
 */
public class SwitchWorld {
	
	public static final int WORLD_SWITCHER_WIDGET = 69;
	public static final int WORLD_SWITCHER_CHILD = 7;
	
	private static final int SORT_WORLD_WIDGET = 10;
	private static final int CURRENT_WORLD_CHILD = 2;
	private static final int SCROLL_WIDGET = 15;
	private static final int SCROLLDOWN_CHILD = 5;
	private static final int SCROLLUP_CHILD = 4;
	
	private static final int WARNING_WIDGET = 219;
	private static final int YES_OPTION_CHILD = 0;
	
	private static final int LOGOUT_WIDGET = 182;
	private static final int WORLD_SWITCHER_BTN = 1;
	
	private static final int WORLD_SORT_SETTING = 477;
	
	private static int world = 1;
	
	/**
	 * @return
	 */
	public static int getWorld() {
		return world;
	}
	
	/**
	 * @param world
	 * @return
	 */
	public static boolean setWorld(int world) {
		LogHandler.log(isWorldSwitcherOpen());
		if (!isWorldSwitcherOpen()) {
			openWorldSwitcher();
			waitForWorldSwitcherInterface();
			return false;
		}
		if (!validateWorld(world)) {
			return false;
		} else {
			SwitchWorld.world = world;
			return true;
		}
	}
	
	
	
	/**
	 * Checks if the interface is visible
	 * @return
	 */
	private static boolean isWorldSwitcherOpen() {
		return Widgets.getWidget(WORLD_SWITCHER_WIDGET).isValid();
	}
	
	/**
	 * returns true if we are already in the world
	 * @param world
	 * @return
	 */
	private static boolean isInWorld(int world) {
		WidgetChild currentWorld = Widgets.getWidget(WORLD_SWITCHER_WIDGET, CURRENT_WORLD_CHILD);
		return currentWorld.getText().contains(Integer.toString(world));
	}
	
	/**
	 * Wait 3 seconds for the WorldSwitch interface to popup
	 * @return
	 */
	private static boolean waitForWorldSwitcherInterface() {
		Time.sleepUntil(new Condition() {
			@Override
			public boolean check() {
				return isWorldSwitcherOpen();
			}
		}, 3000);
		return isWorldSwitcherOpen();
	}
	
	/**
	 * Wait 10 seconds until we found the world
	 * @param i
	 * @param worldSwitcher
	 * @param scrollDown
	 * @return
	 */
	private static boolean waitUntilWorldFound(int i, WidgetChild worldSwitcher, WidgetChild scroll) {
		if (worldSwitcher != null && scroll != null) {
			Time.sleepUntil(new Condition() {
				@Override
				public boolean check() {
					if (worldSwitcher.getChild(i).getY() > Random.nextDouble(410, 420)) {
						Mouse.move(scroll.getChild(SCROLLDOWN_CHILD).getCenterLocation(), 3, 3);
						Mouse.hold(Random.nextInt(400, 600), true);
						Time.sleep(50, 150);
						return false;
					} else if (worldSwitcher.getChild(i).getY() < Random.nextInt(230, 240)) {
						Mouse.move(scroll.getChild(SCROLLUP_CHILD).getCenterLocation(), 3, 3);
						Mouse.hold(Random.nextInt(400, 600), true);
						Time.sleep(50, 150);
						return false;
					} else {
						return true;
					}
				}
			}, 10000);
			return !(worldSwitcher.getChild(i).getY() > 420 || worldSwitcher.getChild(i).getY() < 240);
		}
		return false;
	}
	
	/**
	 * Clicks the world on the world switch interface
	 * @param worldSwitcher
	 * @return
	 */
	private static boolean clickWorld(WidgetChild worldSwitcher) {
		return Mouse.click(worldSwitcher.getCenterLocation(), 5, 5, true);
	}
	
	/**
	 * Wait 2.5 seconds for a warning interface for hopping worlds
	 */
	private static void waitUntilWarning() {
		Time.sleepUntil(new Condition() {

			@Override
			public boolean check() {
				if (Widgets.getWidget(WARNING_WIDGET, YES_OPTION_CHILD).getChild(1) != null) {
					return Mouse.click(Widgets.getWidget(WARNING_WIDGET, YES_OPTION_CHILD).getChild(WORLD_SWITCHER_BTN).getCenterLocation(), 10, 5, true);
				}
				return false;
			}
			
		}, 2500);
	}
	
	/**
	 * Wait 10 seconds until we changed worlds
	 * @param world
	 * @return
	 */
	private static boolean waitUntilWorldChanged(int world) {
		WidgetChild currentWorld = Widgets.getWidget(WORLD_SWITCHER_WIDGET, 2);
		Time.sleepUntil(new Condition() {

			@Override
			public boolean check() {
				return currentWorld.getText().contains(Integer.toString(world));
			}
			
		}, 10000);
		return currentWorld.getText().contains(Integer.toString(world));
	}
	
	/**
	 * Returns true if we successfully hopped world
	 * @param world
	 * @return
	 */
	private static boolean findWorld() {
		WidgetChild worldSwitcher = Widgets.getWidget(WORLD_SWITCHER_WIDGET, WORLD_SWITCHER_CHILD);
		WidgetChild scroll = Widgets.getWidget(WORLD_SWITCHER_WIDGET, SCROLL_WIDGET);
		for(int i = 2; i < 420; i += 6) {
			if (worldSwitcher.getChild(i) != null && worldSwitcher.getChild(i).getText().equals(Integer.toString(getWorld()))) {
				LogHandler.log("Found world");
				if (waitUntilWorldFound(i, worldSwitcher, scroll)) {
					if (clickWorld(worldSwitcher.getChild(i))) {
						waitUntilWarning();
						if (waitUntilWorldChanged(getWorld()))
							return true;
					}
				}
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Sort worlds from low to high
	 * @return
	 */
	private static boolean organizeWorlds() {
		WidgetChild sortByWorldChild = Widgets.getWidget(WORLD_SWITCHER_WIDGET, SORT_WORLD_WIDGET).getChild(0);
		if (Settings.get(WORLD_SORT_SETTING) != 0 && sortByWorldChild != null) {
			if (Mouse.click(sortByWorldChild.getCenterLocation(), 1, 1, true)) {
				Time.sleepUntil(new Condition() {
					@Override
					public boolean check() {
						return Settings.get(WORLD_SORT_SETTING) == 0;
					}
				}, 5000);
			}
		}
		return Settings.get(WORLD_SORT_SETTING) == 0;
	}
	
	/**
	 * Checks for isValidWorld & isInWorld
	 * @param world
	 * @return
	 */
	private static boolean validateWorld(int world) {
		if (WorldReader.isValidWorld(world) && !isInWorld(world)) {
			return true;
		}
		return false;
	}
	
	/**
	 * returns a random world
	 * @return
	 */
	public static boolean setRandomWorld() {
		int world = Random.nextInt(1, 94);
		openWorldSwitcher();
		LogHandler.log(world);
		if (!setWorld(world)) {
			return setRandomWorld();
		} else {
			LogHandler.log("World is set to : " + world);
			return true;
		}
	}
	
	/**
	 * switching worlds, main method
	 * Checks if the world we are switching is a valid world & that the player is not in the world
	 * @param world
	 * @return
	 */
	public static boolean switchWorld() {
		if (isWorldSwitcherOpen()) {
			if (organizeWorlds()) {
				return findWorld();
			}
		} else {
			openWorldSwitcher();
			waitForWorldSwitcherInterface();
		}
		return false;
	}

	/**
	 * Clicks the logout tab en clicks the switch world button
	 */
	public static void openWorldSwitcher() {
		if (Widgets.getCurrentTab() != Widgets.TAB_LOGOUT) {
			Widgets.openTab(Widgets.TAB_LOGOUT);
			Time.sleep(600, 800);
		}
		if (Widgets.getWidget(LOGOUT_WIDGET, WORLD_SWITCHER_BTN).isVisible()) {
			Mouse.click(Widgets.getWidget(LOGOUT_WIDGET, WORLD_SWITCHER_BTN).getCenterLocation(), 10, 10, true);
			Time.sleep(1500, 2000);
		}
	}
}
