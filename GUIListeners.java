package com.devmclovin.gui;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class GUIListeners implements Listener
{
    private final JavaPlugin plugin;
    private final GUI gui;
    private final GUIManager manager;

    public GUIListeners(JavaPlugin plugin, GUI gui, GUIManager manager)
    {
        this.plugin = plugin;
        this.gui = gui;
        this.manager = manager;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event)
    {
        if (event.getClickedInventory().equals(gui.getInventory()))
        {
            event.setCancelled(true);

            int slot = event.getSlot();
            for (Entry entry : gui.getEntries())
            {
                if (entry.getSlot() == slot)
                {
                    entry.onClick(event);
                }
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event)
    {
        if (event.getInventory().equals(gui.getInventory()))
        {
            gui.onClose();
            InventoryCloseEvent.getHandlerList().unregister(this);
            InventoryClickEvent.getHandlerList().unregister(this);
            manager.cancelTask(event.getPlayer().getUniqueId());
        }
    }
}
