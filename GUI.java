package com.devmclovin.gui;

import org.bukkit.inventory.Inventory;

import java.util.List;

public interface GUI
{
    String getIdentifier();

    List<Entry> getEntries();
    int getSize();
    String getTitle();

    void onOpen();
    void onClose();

    Inventory getInventory();
    void setInventory(Inventory inventory);
}
