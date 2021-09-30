package gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class GUI
{
    //GUI properties
    public int size;
    public String title;
    public List<Entry> entries;
    public Inventory inventory;

    //GUI Constructor
    public GUI(int size, String title)
    {
        this.size = size;
        this.title = title;
        this.entries = new ArrayList<>();
    }

    //On GUI open
    public void onOpen(InventoryOpenEvent event)
    {
    }

    //On GUI close
    public void onClose(InventoryCloseEvent event)
    {
    }

    //Create the GUI's inventory
    public static GUI prepareInventory(GUI gui)
    {
        //Create new inventory with gui parameters
        Inventory inv = Bukkit.getServer().createInventory(null, gui.size,
                ChatColor.translateAlternateColorCodes('&', gui.title));
        //Loop through all entries
        for (Entry entry : gui.entries)
        {
            //Build and set item
            inv.setItem(entry.slot, Entry.buildEntry(entry));
        }
        gui.inventory = inv;
        return gui;
    }

    //Open a GUI for a player
    public static void openGUI(Player player, GUI gui, JavaPlugin plugin)
    {
        //register listeners
        plugin.getServer().getPluginManager().registerEvents(new GUIListeners(plugin, gui), plugin);
        //Prepare and open gui
        player.openInventory(prepareInventory(gui).inventory);
    }
}
