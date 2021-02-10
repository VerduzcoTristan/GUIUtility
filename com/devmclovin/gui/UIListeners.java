package com.devmclovin.gui;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

/**
 * The UIListeners class handles all Inventory detection/bridge methodology.
 * <p>
 *     This class is on a per-UI level storing an instance of {@link UserInterface},
 *     {@link JavaPlugin} and {@link UIManager} to handle all functionality.
 *     <br>Both {@link InventoryClickEvent} and {@link InventoryCloseEvent} are handled in this class.
 * </p>
 *
 * @author VerduzcoTristan/McLovin
 */
public class UIListeners implements Listener {

    private final JavaPlugin plugin;
    private final UserInterface userInterface;
    private final UIManager manager;

    /**
     * Constructor to initialise a UIListeners instance.
     * <p>
     *     This constructor simply assigns different values in the class.
     *     <br>Varaibles assigned:
     *     <ol>
     *             {@link UIListeners#plugin},
     *             {@link UIListeners#userInterface},
     *             {@link UIListeners#manager}
     *     </ol>
     * </p>
     *
     * @param plugin {@link JavaPlugin} object.
     * @param userInterface {@link UserInterface} object.
     * @param manager {@link UIManager} object.
     */
    public UIListeners(JavaPlugin plugin, UserInterface userInterface, UIManager manager) {
        this.plugin = plugin;
        this.userInterface = userInterface;
        this.manager = manager;
    }

    /**
     * Method from the {@link Bukkit} Event API.
     * <p>
     *     This method handles the {@link InventoryClickEvent} event, 
     *     if the criterion is matched, the event instance is sent to a {@link UIEntry} object.
     * </p>
     * @see UIEntry#onClick(InventoryClickEvent)
     *
     * @param event {@link Bukkit}, {@link InventoryClickEvent}
     */
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getClickedInventory().equals(userInterface.getInventory())) {
            event.setCancelled(true);

            int slot = event.getSlot();
            for (UIEntry UIEntry : userInterface.getEntries()) {
                if (UIEntry.getSlot() == slot) {
                    UIEntry.onClick(event);
                }
            }
        }
    }

    /**
     * Method from the {@link Bukkit} Event API.
     * <p>
     *     This method handles the {@link InventoryCloseEvent} event,
     *     if the criterion is matched, {@link UserInterface#onClose()} is called and
     *     {@link UIManager#cancelTask(UUID)} is also called.
     * </p>
     *
     * @param event {@link Bukkit}, {@link InventoryCloseEvent}
     */
    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (event.getInventory().equals(userInterface.getInventory())) {
            userInterface.onClose();
            InventoryCloseEvent.getHandlerList().unregister(this);
            InventoryClickEvent.getHandlerList().unregister(this);
            manager.cancelTask(event.getPlayer().getUniqueId());
        }
    }

}
