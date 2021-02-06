package com.devmclovin.gui;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

public interface Entry
{
    Material getType();
    String getName();
    List<String> getLore();
    int getQuantity();

    int getSlot();

    void onClick(InventoryClickEvent event);

    void onUpdate();
}
