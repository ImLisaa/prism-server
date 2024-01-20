package net.bytebridges.prism.server;

import net.hollowcube.minestom.extensions.ExtensionBootstrap;

public class PrismServerBootstrap {

    public static void main(String[] args) {

        var server = ExtensionBootstrap.init();
        new PrismServer(server);
    }
}
