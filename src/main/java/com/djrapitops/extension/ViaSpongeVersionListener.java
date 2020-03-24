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

import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.plugin.PluginContainer;
import us.myles.ViaVersion.api.ViaAPI;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class ViaSpongeVersionListener implements ViaListener {

    private final ViaAPI viaAPI;
    private final ViaVersionStorage storage;

    private final PluginContainer plugin;

    ViaSpongeVersionListener(
            ViaAPI viaAPI,
            ViaVersionStorage storage
    ) {
        this.viaAPI = viaAPI;
        this.storage = storage;
        plugin = Sponge.getPluginManager().getPlugin("Plan")
                .orElseThrow(IllegalStateException::new);
    }

    @Override
    public void register() {
        Sponge.getGame().getEventManager().registerListeners(plugin, this);
    }

    @Listener(order = Order.LAST)
    public void onJoin(ClientConnectionEvent.Join event) {
        UUID uuid = event.getTargetEntity().getUniqueId();
        int playerVersion = viaAPI.getPlayerVersion(uuid);

        Sponge.getGame().getScheduler().createTaskBuilder()
                .async()
                .execute(() -> {
                    try {
                        storage.storeProtocolVersion(uuid, playerVersion);
                    } catch (ExecutionException ignored) {
                        // Ignore
                    }
                })
                .submit(plugin);
    }
}
