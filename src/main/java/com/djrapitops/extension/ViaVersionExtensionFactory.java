/*
    Copyright(c) 2019 Risto Lahtela (AuroraLS3)

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

import com.djrapitops.plan.extension.Caller;
import com.djrapitops.plan.extension.DataExtension;

import java.util.Optional;

/**
 * Factory for DataExtension.
 *
 * @author AuroraLS3
 */
public class ViaVersionExtensionFactory {

    private ViaVersionExtension extension;

    private boolean isAvailable(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException | NoClassDefFoundError e) {
            return false;
        }
    }

    public Optional<DataExtension> createExtension() {
        try {
            extension = createNewExtension();
            return Optional.ofNullable(extension);
        } catch (IllegalStateException noSponge) {
            return Optional.empty();
        }
    }

    public void registerListener(Caller caller) {
        extension.getListener().register();
    }

    private ViaVersionExtension createNewExtension() {
        if (isAvailable("us.myles.ViaVersion.ViaVersionPlugin")) {
            return new ViaVersionBukkitExtension();
        }
        if (isAvailable("us.myles.ViaVersion.VelocityPlugin")) {
            return null;
        }
        if (isAvailable("us.myles.ViaVersion.BungeePlugin")) {
            return new ViaVersionBungeeExtension();
        }
        if (isAvailable("us.myles.ViaVersion.SpongePlugin")) {
            return new ViaVersionSpongeExtension();
        }
        return null;
    }
}