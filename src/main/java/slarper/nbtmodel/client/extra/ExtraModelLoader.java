package slarper.nbtmodel.client.extra;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
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

public class ExtraModelLoader {
    public final static Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static final String MODEL_CONFIG_PATH = "extra/models.json";

    public static void load(){
        ModelLoadingRegistry.INSTANCE.registerModelProvider(
                (manager, out) -> {
                    for (String namespace : manager.getAllNamespaces()){
                        try {
                            List<Resource> resources = manager.getAllResources(new Identifier(namespace,MODEL_CONFIG_PATH));
                            for (Resource resource : resources){
                                Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
                                ConfigObject config = GSON.fromJson(reader,ConfigObject.class);
                                for (String model : config.models){
                                    out.accept(new Identifier(model));
                                    NbtModel.LOGGER.info("loading extra model : " + model);
                                }
                            }
                        } catch (IOException e) {
                            // nothing. because can not find "extra/models.json" in most namespaces is normal.
                        } catch (NullPointerException e){
                            NbtModel.LOGGER.error("Failed to \"new\" an InputStreamReader",e);
                        } catch (JsonSyntaxException e){
                            NbtModel.LOGGER.error("Gson can not get valid json from namespace : " + namespace,e);
                        } catch (InvalidIdentifierException e){
                            NbtModel.LOGGER.error("Failed to \"new\" an Identifier",e);
                        }
                    }
                }
        );
    }
}
