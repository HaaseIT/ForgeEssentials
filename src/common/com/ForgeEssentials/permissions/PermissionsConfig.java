package com.ForgeEssentials.permissions;

import java.io.File;
import java.util.HashSet;

import net.minecraftforge.event.Event.Result;

import com.ForgeEssentials.core.ForgeEssentials;
import com.ForgeEssentials.core.config.Category;
import com.ForgeEssentials.core.config.Configuration;
import com.ForgeEssentials.core.config.Property;

public class PermissionsConfig
{
	public static File		permissionsFile	= new File(ForgeEssentials.FEDIR, "permissions.txt");

	public Configuration	config;

	public PermissionsConfig()
	{
		config = new Configuration(permissionsFile, true);

		// GLOBAL properties
		if (config.categories.containsKey("__GLOBAL__"))
		{
			config.addCustomCategoryComment("__GLOBAL__", "the last stop where permissions get checked before their defaults");

			if (config.categories.containsKey("__GLOBAL__.groups"))
				readGroupPerms(ZoneManager.GLOBAL, config.categories.get("__GLOBAL__.groups"), "__GLOBAL__");

			if (config.categories.containsKey("__GLOBAL__.players"))
				readPlayerPerms(ZoneManager.GLOBAL, config.categories.get("__GLOBAL__.players"), "__GLOBAL__");
		}
		writeGroupPerms(ZoneManager.GLOBAL, "__GLOBAL__");
		writePlayerPerms(ZoneManager.GLOBAL, "__GLOBAL__");

		// WorldZones
		for (Zone worldZone : ZoneManager.worldZoneMap.values())
		{
			if (config.categories.containsKey(worldZone.getZoneID()))
			{
				if (config.categories.containsKey(worldZone.getZoneID() + ".groups"))
					readGroupPerms(worldZone, config.categories.get(worldZone.getZoneID() + ".groups"), worldZone.getZoneID());

				if (config.categories.containsKey(worldZone.getZoneID() + ".players"))
					readPlayerPerms(ZoneManager.GLOBAL, config.categories.get(worldZone.getZoneID() + ".players"), worldZone.getZoneID());
			}
			writeGroupPerms(worldZone, worldZone.getZoneID());
			writePlayerPerms(worldZone, worldZone.getZoneID());
		}

		// all the other zones.
		for (Zone zone : ZoneManager.zoneMap.values())
		{
			if (config.categories.containsKey(zone.getZoneID()))
			{
				if (config.categories.containsKey(zone.getZoneID() + ".groups"))
					readGroupPerms(zone, config.categories.get(zone.getZoneID() + ".groups"), zone.getZoneID());

				if (config.categories.containsKey(zone.getZoneID() + ".players"))
					readPlayerPerms(ZoneManager.GLOBAL, config.categories.get(zone.getZoneID() + ".players"), zone.getZoneID());
			}
			writeGroupPerms(zone, zone.getZoneID());
			writePlayerPerms(zone, zone.getZoneID());
		}

		config.save();
	}

	/**
	 * 
	 * @param zone who's permissions to load from the file.
	 */
	public void forceLoadZone(Zone zone)
	{
		config.load();
		if (config.categories.containsKey(zone.getZoneID()))
		{
			if (config.categories.containsKey(zone.getZoneID() + ".groups"))
				readGroupPerms(zone, config.categories.get(zone.getZoneID() + ".groups"), zone.getZoneID());

			if (config.categories.containsKey(zone.getZoneID() + ".players"))
				readPlayerPerms(ZoneManager.GLOBAL, config.categories.get(zone.getZoneID() + ".players"), zone.getZoneID());
		}
	}

	/**
	 * 
	 * @param zone who's permissions to save to the file.
	 */
	public void forceSaveZone(Zone zone)
	{
		writeGroupPerms(zone, zone.getZoneID());
		writePlayerPerms(zone, zone.getZoneID());
		config.save();
	}

	public void loadAll()
	{
		config.load();

		// GLOBAL properties
		if (config.categories.containsKey("__GLOBAL__"))
		{
			config.addCustomCategoryComment("__GLOBAL__", "the last stop where permissions get checked before their defaults");

			if (config.categories.containsKey("__GLOBAL__.groups"))
				readGroupPerms(ZoneManager.GLOBAL, config.categories.get("__GLOBAL__.groups"), "__GLOBAL__");

			if (config.categories.containsKey("__GLOBAL__.players"))
				readPlayerPerms(ZoneManager.GLOBAL, config.categories.get("__GLOBAL__.players"), "__GLOBAL__");
		}

		// WorldZones
		for (Zone worldZone : ZoneManager.worldZoneMap.values())
			if (config.categories.containsKey(worldZone.getZoneID()))
			{
				if (config.categories.containsKey(worldZone.getZoneID() + ".groups"))
					readGroupPerms(worldZone, config.categories.get(worldZone.getZoneID() + ".groups"), worldZone.getZoneID());

				if (config.categories.containsKey(worldZone.getZoneID() + ".players"))
					readPlayerPerms(ZoneManager.GLOBAL, config.categories.get(worldZone.getZoneID() + ".players"), worldZone.getZoneID());
			}

		// all the other zones.
		for (Zone zone : ZoneManager.zoneMap.values())
			if (config.categories.containsKey(zone.getZoneID()))
			{
				if (config.categories.containsKey(zone.getZoneID() + ".groups"))
					readGroupPerms(zone, config.categories.get(zone.getZoneID() + ".groups"), zone.getZoneID());

				if (config.categories.containsKey(zone.getZoneID() + ".players"))
					readPlayerPerms(ZoneManager.GLOBAL, config.categories.get(zone.getZoneID() + ".players"), zone.getZoneID());
			}
	}

	public void saveAll()
	{
		// GLOBAL properties
		writeGroupPerms(ZoneManager.GLOBAL, "__GLOBAL__");
		writePlayerPerms(ZoneManager.GLOBAL, "__GLOBAL__");

		// WorldZones
		for (Zone worldZone : ZoneManager.worldZoneMap.values())
		{
			writeGroupPerms(worldZone, worldZone.getZoneID());
			writePlayerPerms(worldZone, worldZone.getZoneID());
		}

		// all the other zones.
		for (Zone zone : ZoneManager.zoneMap.values())
		{
			writeGroupPerms(zone, zone.getZoneID());
			writePlayerPerms(zone, zone.getZoneID());
		}

		config.save();
	}

	private void readGroupPerms(Zone zone, Category cat, String parentCat)
	{
		for (String group : ZoneManager.GLOBAL.getGroupsOverriden())
		{
			// write permissions.
			HashSet<Permission> list = ZoneManager.GLOBAL.groupOverrides.get(group);
			for (Permission perm : list)
				config.get(parentCat + ".groups." + group, perm.name, perm.allowed.equals(Result.ALLOW));

			// read permissions
			Category groupCat = config.categories.get(parentCat + ".groups." + group);

			HashSet<Permission> perms = zone.groupOverrides.get(group);
			if (perms == null)
				perms = new HashSet<Permission>();

			for (Property prop : groupCat.properties.values())
			{
				Permission newPerm = new Permission(prop.name, prop.getBoolean(true));
				PermissionChecker check = new Permission(prop.name, prop.getBoolean(true));

				if (perms.contains(check) && !perms.contains(newPerm))
					perms.remove(check);

				perms.add(newPerm);
			}
		}
	}

	private void readPlayerPerms(Zone zone, Category cat, String parentCat)
	{
		for (String player : zone.getPlayersOverriden())
		{
			// write permissions.
			HashSet<Permission> list = ZoneManager.GLOBAL.groupOverrides.get(player);
			for (Permission perm : list)
				config.get(parentCat + ".players." + player, perm.name, perm.allowed.equals(Result.ALLOW));

			// read permissions
			Category groupCat = config.categories.get(parentCat + ".players." + player);

			HashSet<Permission> perms = zone.groupOverrides.get(player);
			if (perms == null)
				perms = new HashSet<Permission>();

			for (Property prop : groupCat.properties.values())
			{
				Permission newPerm = new Permission(prop.name, prop.getBoolean(true));
				PermissionChecker check = new Permission(prop.name, prop.getBoolean(true));

				if (perms.contains(check) && !perms.contains(newPerm))
					perms.remove(check);

				perms.add(newPerm);
			}
		}
	}

	private void writePlayerPerms(Zone zone, String parentCat)
	{
		for (String player : zone.getPlayersOverriden())
		{
			HashSet<Permission> list = zone.groupOverrides.get(player);
			for (Permission perm : list)
				config.get(parentCat + ".players." + player, perm.name, perm.allowed.equals(Result.ALLOW));
		}
	}

	private void writeGroupPerms(Zone zone, String parentCat)
	{
		for (String group : zone.getGroupsOverriden())
		{
			HashSet<Permission> list = zone.groupOverrides.get(group);
			for (Permission perm : list)
				config.get(parentCat + ".groups." + group, perm.name, perm.allowed.equals(Result.ALLOW));
		}
	}

}
