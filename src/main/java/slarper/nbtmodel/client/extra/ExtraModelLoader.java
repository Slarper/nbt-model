package slarper.nbtmodel.client.extra;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;
import slarper.nbtmodel.NbtModel;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Environment(EnvType.CLIENT)
public class ExtraModelLoader {
    public final static Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static final String CONFIG_PATH = "nbtmodel:extra/models.json";

    public static void load(){
        ModelLoadingRegistry.INSTANCE.registerModelProvider(
                (manager, out) -> {
                    try {
                        List<Resource> resources = manager.getAllResources(new Identifier(CONFIG_PATH));
                        for (Resource resource : resources){
                            Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
                            ConfigObject config = GSON.fromJson(reader, ConfigObject.class);
                            for (String model : config.models){
                                NbtModel.LOGGER.info("loading extra model : " + model);
                                out.accept(new Identifier(model));
                            }
                        }
                    } catch (IOException e) {
                        NbtModel.LOGGER.error("Failed to find or parse models.json.",e);
                    } catch (InvalidIdentifierException e){
                        NbtModel.LOGGER.error("Failed to parse id",e);
                    }
                }
        );
    }
}
