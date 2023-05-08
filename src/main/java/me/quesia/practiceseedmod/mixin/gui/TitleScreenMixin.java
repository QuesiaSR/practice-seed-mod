package me.quesia.practiceseedmod.mixin.gui;

import me.quesia.practiceseedmod.PracticeSeedMod;
import me.quesia.practiceseedmod.gui.ModConfigScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {
    protected TitleScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "initWidgetsNormal", at = @At("TAIL"))
    private void addNewButton(int y, int spacingY, CallbackInfo ci) {
        int buttonWidth = 200;

        this.addButton(
                new ButtonWidget(
                        this.width / 2 - buttonWidth / 2,
                        y - spacingY,
                        buttonWidth,
                        20,
                        new LiteralText("Practice Seed Mod"),
                        b -> {
                            if (this.client != null) {
                                this.client.openScreen(new ModConfigScreen());
                            }
                        }
                )
        );
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void playNextSeed(CallbackInfo ci) {
        PracticeSeedMod.CURRENT_SEED = null;
        if (PracticeSeedMod.playNextSeed()) { ci.cancel(); }
        else { PracticeSeedMod.RUNNING = false; }
    }
}
