package gui;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Entry
{
    public int slot;
    public int quantity;
    public String name;
    public Material material;
    public List<String> lore;

    public Entry( Material material, int slot)
    {
        this.slot = slot;
        this.quantity = 1;
        this.name = material.name();
        this.material = material;
        this.lore = new ArrayList<>();
    }

    public void onClick(InventoryClickEvent event)
    {
    }

    public static ItemStack buildEntry(Entry entry)
    {
        ItemStack item = new ItemStack(entry.material, entry.quantity);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', entry.name));
        meta.setLore(entry.lore);

        item.setItemMeta(meta);

        return item;
    }
}
