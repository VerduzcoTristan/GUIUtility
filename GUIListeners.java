package gui;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class GUIListeners implements Listener
{
    private final JavaPlugin plugin;
    private final GUI gui;

    public GUIListeners(JavaPlugin plugin, GUI gui)
    {
        this.plugin = plugin;
        this.gui = gui;
        System.out.println(0);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event)
    {
        System.out.println(1);
        if (event.getClickedInventory().equals(gui.inventory))
        {
            System.out.println(2);
            event.setCancelled(true);

            int clickedSlot = event.getSlot();
            for (Entry entry : gui.entries)
            {
                if (entry.slot == clickedSlot)
                {
                    entry.onClick(event);
                }
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event)
    {
        if (event.getInventory().equals(gui.inventory))
        {
            gui.onClose(event);
            InventoryCloseEvent.getHandlerList().unregister(this);
            InventoryClickEvent.getHandlerList().unregister(this);
        }
    }

    @EventHandler
    public void onOpen(InventoryOpenEvent event)
    {
        if (event.getInventory().equals(gui.inventory))
        {
            gui.onOpen(event);
        }
    }
}
