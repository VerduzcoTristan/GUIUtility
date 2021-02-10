package com.devmclovin.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.List;

/**
 * The UserInterface class stands for a single Inventory definition.
 * <p>
 *     This class creates default class functionality to be used by both
 *     {@link UIManager} and {@link UIListeners} respectively.
 * </p>
 *
 * @author VerduzcoTristan/McLovin
 */
public interface UserInterface {

    /**
     * Method to return all {@link UIEntry} objects for the {@link Inventory}.
     * <p>
     *     This method can return an empty list.
     * </p>
     *
     * @return {@link List} of {@link UIEntry}s.
     */
    List<UIEntry> getEntries();

    /**
     * Method to return the size of the {@link Inventory}
     * <p>
     *     This method should return a number 9-54 and divisible by 9.
     * </p>
     *
     * @return 9-54
     */
    int getSize();

    /**
     * Method to return the Title of the {@link Inventory}
     * <p>
     *     This method uses {@link org.bukkit.ChatColor#translateAlternateColorCodes(char, String)}
     *     to translate colours automatically.
     * </p>
     *
     * @return String title (Automagical Colouring included).
     */
    String getTitle();

    /**
     * Void method which is called upon Open of the {@link Inventory}
     * <p>
     *     This method is a bridge from {@link UIManager#openGUI(Player, UserInterface, long, long)}.
     * </p>
     */
    void onOpen();

    /**
     * Void method which is called upon Close of the {@link Inventory}
     * <p>
     *     This method is a bridge from {@link UIListeners#onClose(InventoryCloseEvent)}.
     * </p>
     */
    void onClose();

    /**
     * Method to return the {@link Inventory} instance.
     * <p>
     *     When built, the {@link Inventory} object will be set using {@link UserInterface#setInventory(Inventory)}.
     *     <br>The Inventory is built by {@link UIManager#openGUI(Player, UserInterface, long, long)}.
     * </p>
     *
     * @return
     */
    Inventory getInventory();

    /**
     * Method to set the {@link Inventory} instance.
     * <p>
     *     This is called by {@link UIManager#openGUI(Player, UserInterface, long, long)}.
     *     <br>The instance should be saved on an in-class level,
     *     and then retrieved using {@link UserInterface#getInventory()}.
     * </p>
     *
     * @param inventory {@link Bukkit}, {@link Inventory}
     */
    void setInventory(Inventory inventory);

}
