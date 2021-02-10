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

/**
 * This class handles all Registration/Inventory management.
 * <p>
 *     {@link UserInterface} creation, event registration and Item slot updating is all initialised from this Class.
 * </p>
 *
 * @author VerduzcoTristan/McLovin
 */
public class UIManager {

    private final JavaPlugin plugin;
    private final HashMap<UUID, Integer> tasks;

    /**
     * Constructor to initialise a UIManager.
     * <p>
     *     This should be stored as an instance, do not recreate
     *     this everytime as this uses a {@link HashMap} to store {@link BukkitTask}s.
     * </p>
     *
     * @param plugin Plugin instance ('this' for example).
     */
    public UIManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.tasks = new HashMap<>();
    }

    /**
     * Method to open a graphical user interface for the specified Player.
     * <p>
     *     This method requires a {@link Player} object, this cannot be {@code null}.<br>
     *     The method also requires a {@link UserInterface} object to register/handle all methodology.
     * </p>
     *
     * @param player Player object.
     * @param userInterface UserInterface Object.
     */
    public void openGUI(Player player, UserInterface userInterface) {
        player.openInventory(setupInventory(userInterface));
        Bukkit.getPluginManager().registerEvents(new UIListeners(plugin, userInterface, this), plugin);
        userInterface.onOpen();

        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                for (UIEntry entry : userInterface.getEntries()) {
                    entry.onUpdate();
                    userInterface.getInventory().setItem(entry.getSlot(), getItem(entry, userInterface));
                }
            }
        }.runTaskTimer(plugin, 20, 20);

        this.tasks.put(player.getUniqueId(), task.getTaskId());
    }

    /**
     * Method to setup a {@link Bukkit} {@link Inventory}.
     * <p>
     *     This method uses all functionality from {@link UserInterface} to create the Inventory.
     *     This method also assigns each item to the slot they have setup in {@link UIEntry}.
     * </p>
     *
     * @param userInterface {@link UserInterface} object (not null).
     * @return {@link Inventory} instance.
     */
    private Inventory setupInventory(UserInterface userInterface) {
        Inventory inventory = Bukkit.createInventory(null, userInterface.getSize(), ChatColor.translateAlternateColorCodes('&', userInterface.getTitle()));
        userInterface.getEntries().forEach(UIEntry -> inventory.setItem(UIEntry.getSlot(), getItem(UIEntry, userInterface)));
        userInterface.setInventory(inventory);
        return userInterface.getInventory();
    }

    /**
     * Method to <em>get</em> an <em>Item</em>.
     * <p>
     *     This method uses all functionality from {@link UIEntry} to create a {@link Bukkit} {@link ItemStack}.
     * </p>
     *
     * @param UIEntry {@link UIEntry} object.
     * @param userInterface {@link UserInterface} object.
     * @return {@link ItemStack} object.
     */
    private ItemStack getItem(UIEntry UIEntry, UserInterface userInterface) {
        ItemStack item = new ItemStack(UIEntry.getType(), UIEntry.getQuantity());
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', UIEntry.getName()));
        meta.setLore(UIEntry.getLore());
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Method to cancel the automatic update task for a given player.
     * <p>
     *     This method uses the in-class {@link HashMap} to get the Task Id from the {@link Player} {@code UUID},
     *     This Id is then sent through to {@link Bukkit#getScheduler()} methodology to cancel the task.
     * </p>
     *
     * @param id {@link UUID} of the {@link Player}.
     */
    public void cancelTask(UUID id) {
        int task = this.tasks.get(id);
        Bukkit.getScheduler().cancelTask(task);
    }

}
