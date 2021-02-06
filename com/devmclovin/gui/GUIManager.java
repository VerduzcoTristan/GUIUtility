package com.devmclovin.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.UUID;

public class GUIManager
{
    private final JavaPlugin plugin;

    private HashMap<UUID, Integer> tasks;

    public GUIManager(JavaPlugin plugin)
    {
        this.plugin = plugin;
        this.tasks = new HashMap<>();
    }

    public void openGUI(Player player, GUI gui)
    {
        player.openInventory(setupInventory(gui));
        plugin.getServer().getPluginManager().registerEvents(new GUIListeners(plugin, gui, this), plugin);
        gui.onOpen();

        BukkitTask task = new BukkitRunnable()
        {
            @Override
            public void run()
            {
                for (Entry entry : gui.getEntries())
                {
                    entry.onUpdate();
                    gui.getInventory().setItem(entry.getSlot(), getItem(entry, gui));
                }
            }
        }.runTaskTimer(plugin, 20, 20);

        this.tasks.put(player.getUniqueId(), task.getTaskId());
    }

    private Inventory setupInventory(GUI gui)
    {
        Inventory inventory = Bukkit.createInventory(null, gui.getSize(),
                ChatColor.translateAlternateColorCodes('&', gui.getTitle()));

        for (Entry entry : gui.getEntries())
        {
            inventory.setItem(entry.getSlot(), getItem(entry, gui));
        }

        gui.setInventory(inventory);

        return gui.getInventory();
    }

    private ItemStack getItem(Entry entry, GUI gui)
    {
        ItemStack item = new ItemStack(entry.getType(), entry.getQuantity());
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', entry.getName()));
        meta.setLore(entry.getLore());

        item.setItemMeta(meta);

        return item;
    }

    public void cancelTask(UUID id)
    {
        int task = this.tasks.get(id);
        Bukkit.getServer().getScheduler().cancelTask(task);
    }
}
