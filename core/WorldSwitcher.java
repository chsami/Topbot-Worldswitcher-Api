package core;

import java.util.Arrays;

import org.tbot.bot.TBot;
import org.tbot.internal.AbstractScript;
import org.tbot.internal.Manifest;
import org.tbot.internal.handlers.LogHandler;
import org.tbot.methods.Random;
import org.tbot.methods.Time;

import data.World;
import data.WorldSettings;
import tasks.SwitchWorld;

@Manifest(name="WorldSwitcher", authors="Mocrosoft", description="In game world switcher.")
public class WorldSwitcher extends AbstractScript {
	
	/* (non-Javadoc)
	 * @see org.tbot.concurrency.Task#onStart()
	 */
	@Override
	public boolean onStart() {
		WorldSettings.setWorldType(World.NON_MEMBERS);
		LogHandler.log("Thinking of a random world...");
		SwitchWorld.setRandomWorld();
		Time.sleep(1000, 1500);
		LogHandler.log("World set succesfully.");
		return true;
	}

	/* (non-Javadoc)
	 * @see org.tbot.internal.AbstractScript#loop()
	 */
	@Override
	public int loop() {
		if (SwitchWorld.switchWorld())
			TBot.getBot().getScriptHandler().stopScript();
		return Random.nextInt(100, 288);
	}

}
