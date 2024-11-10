package me.lingbopro.rejoinnofalldamageisback.mixin;

import com.mojang.logging.LogUtils;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    @Unique
    private static final Logger LOGGER = LogUtils.getLogger();
    @Shadow
    public PlayerAbilities abilities;

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "damage(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/damage/DamageSource;F)Z", at = @At(value = "HEAD"),
            cancellable = true)
    public void damage(ServerWorld world, DamageSource source, float amount, CallbackInfoReturnable<Boolean> info) {
        LOGGER.debug("damage() called");
        if (this.abilities.invulnerable && source.isOf(DamageTypes.FALL)) {
            info.setReturnValue(false);
            info.cancel();
        }
    }

}
