package slarper.nbtmodel.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import slarper.nbtmodel.client.extra.ExtraModelLoader;

@Environment(EnvType.CLIENT)
public class NbtModelClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ExtraModelLoader.load();
    }
}
