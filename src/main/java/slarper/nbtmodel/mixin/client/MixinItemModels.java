package slarper.nbtmodel.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.BakedModelManagerHelper;
import net.minecraft.client.render.item.ItemModels;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(ItemModels.class)
public abstract class MixinItemModels {
    @Shadow @Nullable public abstract BakedModel getModel(Item item);

    @Shadow public abstract BakedModelManager getModelManager();

    @Inject(
            method = "getModel(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/client/render/model/BakedModel;",
            at = @At("HEAD"),
            cancellable = true
    )
    private void getModelByNbt(ItemStack stack, CallbackInfoReturnable<BakedModel> cir){
        // if model is actually not specified, getString will return ""
        String model = stack.getOrCreateNbt().getString("Model");
        if (!model.equals("")){
            BakedModel bakedModel = null;
            try {
                Identifier id = new Identifier(model);
                // BakedModelManagerHelper::getModel ONLY provides extra models.
                // Other models must be specified with corresponding item id.
                if (Registry.ITEM.containsId(id)){
                    bakedModel = this.getModel(Registry.ITEM.get(id));
                } else {
                    bakedModel = BakedModelManagerHelper.getModel(this.getModelManager(), id);
                }
            } catch (InvalidIdentifierException e){
                // Don't log this exception
                // It's OK.
            }

            cir.setReturnValue(bakedModel == null? this.getModelManager().getMissingModel() : bakedModel);
        }
    }
}
