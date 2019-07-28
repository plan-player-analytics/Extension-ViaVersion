/*
    Copyright(c) 2019 Risto Lahtela (Rsl1122)

    The MIT License(MIT)

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files(the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and / or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions :
    The above copyright notice and this permission notice shall be included in
    all copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
    THE SOFTWARE.
*/
package com.djrapitops.extension;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import us.myles.ViaVersion.api.ViaAPI;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class ViaBukkitVersionListener implements Listener {

    private final ViaAPI viaAPI;
    private final ViaVersionStorage storage;

    private final Plugin plugin;

    ViaBukkitVersionListener(
            ViaAPI viaAPI,
            ViaVersionStorage storage
    ) {
        this.viaAPI = viaAPI;
        this.storage = storage;
        plugin = Bukkit.getPluginManager().getPlugin("Plan");
    }

    public void register() {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onJoin(PlayerJoinEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        int playerVersion = viaAPI.getPlayerVersion(uuid);

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                storage.storeProtocolVersion(uuid, playerVersion);
            } catch (ExecutionException ignored) {
                // Ignore
            }
        });
    }
}
