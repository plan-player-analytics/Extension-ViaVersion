/*
    Copyright(c) 2019 AuroraLS3

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
package net.playeranalytics.extension.viaversion;

import com.djrapitops.plan.extension.CallEvents;
import com.djrapitops.plan.extension.DataExtension;
import com.djrapitops.plan.extension.annotation.PluginInfo;
import com.djrapitops.plan.extension.annotation.StringProvider;
import com.djrapitops.plan.extension.annotation.TableProvider;
import com.djrapitops.plan.extension.icon.Color;
import com.djrapitops.plan.extension.icon.Family;
import com.djrapitops.plan.extension.icon.Icon;
import com.djrapitops.plan.extension.table.Table;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;

import java.util.UUID;

/**
 * Template for ViaVersion.
 *
 * @author AuroraLS3
 */
@PluginInfo(name = "ViaVersion", iconName = "gamepad", iconFamily = Family.SOLID, color = Color.LIGHT_GREEN)
public abstract class ViaVersionExtension implements DataExtension {

    protected final ViaVersionStorage storage;

    public ViaVersionExtension(ViaVersionStorage storage) {
        this.storage = storage;
    }

    public abstract ViaListener getListener();

    @Override
    public CallEvents[] callExtensionMethodsOn() {
        return new CallEvents[]{
                CallEvents.PLAYER_JOIN,
                CallEvents.PLAYER_LEAVE,
                CallEvents.SERVER_PERIODICAL
        };
    }

    @StringProvider(
            text = "Last Join Version",
            description = "Version used last time the player joined",
            iconName = "signal",
            iconColor = Color.LIGHT_GREEN,
            showInPlayerTable = true
    )
    public String protocolVersion(UUID playerUUID) {
        return getProtocolVersionString(storage.getProtocolVersion(playerUUID));
    }

    private String getProtocolVersionString(int version) {
        return version != -1 ? ProtocolVersion.getProtocol(version).getName() : "Not Yet Known";
    }

    @TableProvider(tableColor = Color.LIGHT_GREEN)
    public Table protocolTable() {
        Table.Factory table = Table.builder()
                .columnOne("Version", Icon.called("signal").build())
                .columnTwo("Users", Icon.called("users").build());

        storage.getProtocolVersionCounts().entrySet()
                .stream()
                .sorted((one, two) -> Integer.compare(two.getValue(), one.getValue()))
                .forEach(entry -> table.addRow(getProtocolVersionString(entry.getKey()), entry.getValue()));

        return table.build();
    }
}