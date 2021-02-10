package com.devmclovin.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

import java.util.List;

/**
 * This interface stands for a singular {@link UIEntry}.
 * <p>
 *     This class is an interface to be used by Developers in conjunction with the Bukkit/Spigot API {@link Bukkit}.
 *     <br>There are several methods in this class which help with the management/listening of Inventory events.
 * </p>
 *
 * @author VerduzcoTristan/McLovin
 */
public interface UIEntry {

    /**
     * Method to return the Material type of the Item.
     * <p>
     *     This method is used by {@link UIManager} to build the item with this specified Material.
     * </p>
     * @return {@link Material}
     */
    Material getType();

    /**
     * Method to return the Material date of the item.
     * <p>
     *     Although {@code @Deprecated} 1.7.X and 1.8.X functionality is <em>Experimentally supported</em>.
     * </p>
     * @see org.bukkit.inventory.ItemStack#ItemStack(Material, int, short)
     * @return
     */
    @Deprecated
    short getData();

    /**
     * Method to return the Display name of the Item.
     * <p>
     *     This can be {@code null}, the item name will default to the Minecraft name.
     *     <br>This is also automatically Color-translated from '&'.
     * </p>
     *
     * @return Display name of the Item.
     */
    String getName();

    /**
     * Method to return the Lore (Item Description Lines) of the Item.
     * <p>
     *     This is in the form of a String list.
     *     <br>This is also automatically Color-translated from '&'.
     * </p>
     *
     * @return String List of Description Lines.
     */
    List<String> getLore();

    /**
     * Method to return the amount of the item for the slot.
     * <p>
     *     Cannot be lower than 1 or greater than 64.
     * </p>
     *
     * @return Integer amount.
     */
    int getQuantity();

    /**
     * Method to return the slot of the item.
     * <p>
     *     Inventories provided by {@link Bukkit#createInventory(InventoryHolder, int, String)}
     *     begin at position 0 to 'n'.
     * </p>
     *
     * @return 0 - N (where N is the (size of the Inventory)-1) position.
     */
    int getSlot();

    /**
     * Method to handle the click event on the Inventory.
     * <p>
     *     This method is a bridge for Click events, {@link UIListeners}
     *     handles all Inventory Discovery and redirects Events to this method.
     * </p>
     *
     * @param event Instance of {@link InventoryClickEvent} from {@link Bukkit}.
     */
    void onClick(InventoryClickEvent event);

    /**
     * Method to handle/change item functionality on a timer.
     * <p>
     *     At every 'X' interval, this method is called to change/update/modify items in the inventory.
     * </p>
     * @see UIManager#openGUI(Player, UserInterface, long, long)
     */
    void onUpdate();

}
