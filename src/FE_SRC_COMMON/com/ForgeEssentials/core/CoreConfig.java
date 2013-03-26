package com.ForgeEssentials.core;

import java.io.File;
import java.util.logging.Level;

import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;

import com.ForgeEssentials.core.compat.DuplicateCommandRemoval;
import com.ForgeEssentials.util.FunctionHelper;
import com.ForgeEssentials.util.MiscEventHandler;
import com.ForgeEssentials.util.OutputHandler;
import com.ForgeEssentials.util.TeleportCenter;

public class CoreConfig
{
	public static final File	mainconfig	= new File(ForgeEssentials.FEDIR, "main.cfg");

	public final Configuration	config;

	// this is designed so it will work for any class.
	public CoreConfig()
	{
		OutputHandler.finer("Loading configs");

		config = new Configuration(mainconfig, true);

		config.addCustomCategoryComment("Core", "Configure ForgeEssentials Core.");

		Property prop = config.get("Core", "versionCheck", true);
		prop.comment = "Check for newer versions of ForgeEssentials on load?";
		ForgeEssentials.verCheck = prop.getBoolean(true);

		prop = config.get("Core", "mcstats", true);
		prop.comment = "If you don't want to send feedback to MCstats, set to false. Optionally, use the opt-out setting located in PluginMetrics.cfg in your minecraft folder.";
		ForgeEssentials.mcstats = prop.getBoolean(true);
		
		prop = config.get("Core", "logLevel", ""+Level.OFF);
		prop.comment = "ForgeEssentials LogLevel. Valid values: OFF, FINE, FINER, FINEST, WARNING, SEVERE, ALL, OFF";
		OutputHandler.felog.setLevel(getLevel(prop.value));

		prop = config.get("Core", "modlistLocation", "modlist.txt");
		prop.comment = "Specify the file where the modlist will be written to. This path is relative to the ForgeEssentials folder.";
		ForgeEssentials.modlistLocation = prop.value;

		prop = config.get("general", "removeDuplicateCommands", true);
		prop.comment = "Remove commands from the list if they already exist outside of FE.";
		DuplicateCommandRemoval.removeDuplicateCommands = prop.getBoolean(true);

		// sanity check.

		prop = config.get("Core", "sanitychecks", true);
		prop.comment = "For advanced users only: If you wish to run FE on a bukkit server or on a server with Fihgu's mod (not recommended, not supported), set this to false.";
		ForgeEssentials.sanitycheck = prop.getBoolean(true);

		prop = config.get("Core.Misc", "tpWarmup", 5);
		prop.comment = "The amount of time you need to stand still to TP.";
		TeleportCenter.tpWarmup = prop.getInt(5);

		prop = config.get("Core.Misc", "tpCooldown", 5);
		prop.comment = "The amount of time you need to wait to TP again.";
		TeleportCenter.tpCooldown = prop.getInt(5);

		prop = config.get("Core.Misc", "MajoritySleep", true);
		prop.comment = "If +50% of players sleep, make it day.";
		MiscEventHandler.MajoritySleep = prop.getBoolean(true);

		config.addCustomCategoryComment("Core.VIP", "Permission \n" + PlayerTracker.PERMISSION);

		prop = config.get("Core.VIP", "KickForVIP", true);
		prop.comment = "Kick a player if not VIP and (playercount + VIP slots = total slots)";
		PlayerTracker.KickForVIP = prop.getBoolean(true);

		prop = config.get("Core.VIP", "VIPslots", 5);
		prop.comment = "Amount of space reserved for VIPs";
		PlayerTracker.VIPslots = prop.getInt();

		prop = config.get("Core.VIP", "kickMessage", "Sorry, this spot is for VIPs");
		prop.comment = "Message you get when you log in and no VIP space is available";
		PlayerTracker.kickMessage = prop.value;

		config.save();
	}
	
	private Level getLevel(String val)
	{
		if (val.equalsIgnoreCase("INFO"))
			return Level.INFO;
		else if (val.equalsIgnoreCase("WARNING"))
			return Level.WARNING;
		else if (val.equalsIgnoreCase("SEVERE"))
			return Level.SEVERE;
		else if (val.equalsIgnoreCase("FINE"))
			return Level.FINE;
		else if (val.equalsIgnoreCase("FINER"))
			return Level.FINER;
		else if (val.equalsIgnoreCase("FINEST"))
			return Level.FINEST;
		else if (val.equalsIgnoreCase("ALL"))
			return Level.ALL;
		else if (val.equalsIgnoreCase("OFF"))
			return Level.OFF;
		else
			return Level.INFO;
	}

	/**
	 * will overwrite the current physical file.
	 */
	public void forceSave()
	{
		config.save();

		config.get("general", "removeDuplicateCommands", true, "Remove commands from the list if they already exist outside of FE.").value = "" + DuplicateCommandRemoval.removeDuplicateCommands;
	}
}
