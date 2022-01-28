package com.pvwvq.stat.inventories;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class StatInv implements Listener {

    Inventory inv;
    ArrayList<Integer> slots;
    ArrayList<ItemStack> glassPanes;

    public Inventory get() {
        return inv;
    }

    public StatInv() {

        this.inv = Bukkit.createInventory(null, 27, "Stat");
        Random random = new Random();
        ItemStack tempItem = new ItemStack(Material.OAK_BUTTON);

        for (int i = 0; i < 27; i++) {

            slots.add(i);

        }

        for (int i = 10; i < 17; i += 2) {

            slots.remove(slots.get(i));

        }

        glassPanes.add(new ItemStack(Material.WHITE_STAINED_GLASS_PANE));
        glassPanes.add(new ItemStack(Material.ORANGE_STAINED_GLASS_PANE));
        glassPanes.add(new ItemStack(Material.MAGENTA_STAINED_GLASS_PANE));
        glassPanes.add(new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE));
        glassPanes.add(new ItemStack(Material.YELLOW_STAINED_GLASS_PANE));
        glassPanes.add(new ItemStack(Material.LIME_STAINED_GLASS_PANE));
        glassPanes.add(new ItemStack(Material.PINK_STAINED_GLASS_PANE));

        for (int slot : slots) {

            inv.setItem(slot, glassPanes.get(random.nextInt(6)));

        }

        inv.setItem(10, getClickItem().get(0));
        inv.setItem(12, getClickItem().get(1));
        inv.setItem(14, getClickItem().get(2));
        inv.setItem(16, getClickItem().get(3));

        //Todo: set material of click item
        // - AD
        // - AP
        // - HP
        // - BR

    }

    @EventHandler
    private void onInventoryClick(InventoryClickEvent e) {

        if (!e.getInventory().equals(inv)) return;

        int slot = e.getSlot();

        if (slots.contains(e.getSlot())) {
            e.setCancelled(true);
            return;
        }

        Player p = (Player) e.getView().getPlayer();

        if (slot == 10) {


        }



    }

    private ArrayList<ItemStack> getClickItem() {

        ArrayList<ItemStack> itemStacks = new ArrayList<>();

        ItemStack AD = setName(new ItemStack(Material.RED_DYE), "AD");
        ItemStack AP = setName(new ItemStack(Material.BLUE_DYE), "AP");
        ItemStack HP = setName(new ItemStack(Material.LIME_DYE), "HP");
        ItemStack BR = setName(new ItemStack(Material.YELLOW_DYE), "BR");

        itemStacks.add(AD);
        itemStacks.add(AP);
        itemStacks.add(HP);
        itemStacks.add(BR);

        return itemStacks;

    }

    private ItemStack setName(ItemStack item, String name) {

        ItemMeta itemM = item.getItemMeta();

        itemM.setDisplayName(name);

        item.setItemMeta(itemM);

        return item;

    }
}
