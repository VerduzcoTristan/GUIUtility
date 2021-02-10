package com.devmclovin.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
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
     * @param taskDelay Delay of the Update task.
     * @param taskPeriod Period of the Update task.
     * @see BukkitRunnable#runTaskTimer(Plugin, long, long)
     */
    public void openGUI(Player player, UserInterface userInterface, long taskDelay, long taskPeriod) {
        player.openInventory(setupInventory(userInterface));
        Bukkit.getPluginManager().registerEvents(new UIListeners(userInterface, this), plugin);
        userInterface.onOpen();
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                for (UIEntry entry : userInterface.getEntries()) {
                    entry.onUpdate();
                    userInterface.getInventory().setItem(entry.getSlot(), getItem(entry));
                }
            }
        }.runTaskTimer(plugin, taskDelay, taskPeriod);
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
     * @throws IllegalStateException if {@link UserInterface} is null.
     * @throws IllegalArgumentException if the {@link UserInterface#getSize()} is invalid.
     * @return {@link Inventory} instance.
     */
    private Inventory setupInventory(UserInterface userInterface)
            throws IllegalStateException, IllegalArgumentException {
        if (userInterface == null) {
            throw new IllegalStateException("UserInterface is null.");
        }
        if (isValidInventorySize(userInterface.getSize())) {
            throw new IllegalArgumentException("UserInterface size isn't 9, 18, 27, 36, 45 or 54.");
        }
        Inventory inventory = Bukkit.createInventory(
                null,
                userInterface.getSize(),
                ChatColor.translateAlternateColorCodes('&', userInterface.getTitle())
        );
        userInterface.getEntries().forEach(UIEntry -> inventory.setItem(UIEntry.getSlot(), getItem(UIEntry)));
        userInterface.setInventory(inventory);
        return userInterface.getInventory();
    }

    /**
     * Method to determine if an integer could be a valid Inventory size.
     * <p>
     *     Method uses basic math to determine if the Inventory size is valid for the Bukkit API.
     * </p>
     *
     * @param size Integer size to test.
     * @return {@code true} = is valid, {@code false} = is not valid.
     */
    private boolean isValidInventorySize(int size) {
        return size <= 54 && size >= 0 && size % 9 == 0;
    }

    /**
     * Method to <em>get</em> an <em>Item</em>.
     * <p>
     *     This method uses all functionality from {@link UIEntry} to create a {@link Bukkit} {@link ItemStack}.
     *     <br>This method uses deprecated methods to Experimentally support 1.8.X/1.7.X.
     * </p>
     *
     * @param entry {@link UIEntry} object.
     * @return {@link ItemStack} object.
     */
    @SuppressWarnings("deprecation")
    private ItemStack getItem(UIEntry entry) {
        ItemStack item;
        if (isExperimental()) {
            item = new ItemStack(entry.getType(), entry.getQuantity(), entry.getData());
        } else {
            item = new ItemStack(entry.getType(), entry.getQuantity());
        }
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', entry.getName()));
            meta.setLore(entry.getLore());
            item.setItemMeta(meta);
        }
        return item;
    }

    /**
     * Method to return if the Server Version is running an Experimentally supported Version.
     * <p>
     *     All versions of 1.8.X and 1.7.X will be supported by this API, but not officially.
     * </p>
     *
     * @return {@code true} = 1.7-1.8.X, {@code false} = 1.9.X+
     */
    private boolean isExperimental() {
        String a = Bukkit.getServer().getClass().getPackage().getName();
        String version = a.substring(a.lastIndexOf('.') + 1);
        return version.startsWith("v1_8_") || version.startsWith("v1_7_");
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
        int task = this.tasks.getOrDefault(id, -1);
        if (task == -1) return; // If the task isn't found.
        Bukkit.getScheduler().cancelTask(task);
    }

}
