package dota2Spire.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import dota2Spire.Dota2Spire;
import dota2Spire.util.StringUtil;
import dota2Spire.util.TextureLoader;

import static dota2Spire.Dota2Spire.makeRelicPath;

public class RefresherOrb extends CustomRelic implements ClickableRelic {
    // ID, images, text.
    public static final String ID = Dota2Spire.makeID("RefresherOrb");
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("RefresherOrb.png"));
    //    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("placeholder_relic.png"));

    private boolean isPlayerTurn = false;
    private boolean energyEnable = false;

    private static final int _Draw = 3;
    private static final int _CD = 5;

    public RefresherOrb() {
//        super(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.MAGICAL);
        super(ID, IMG, RelicTier.BOSS, LandingSound.MAGICAL);
    }

    private void setCount(int count) {
        this.counter = count;
        if (count <= 0) {
            this.counter = -1;
            flash();
            beginLongPulse();
            if (!energyEnable) {
                ++AbstractDungeon.player.energy.energyMaster;
                ++AbstractDungeon.player.energy.energy;
                energyEnable = true;
            }
        } else {
            if (energyEnable) {
                --AbstractDungeon.player.energy.energyMaster;
                --AbstractDungeon.player.energy.energy;
                energyEnable = false;
            }
        }
    }

    @Override
    public void onEquip() {
        setCount(0);
    }

    @Override
    public void onUnequip() {
        if (energyEnable) {
            --AbstractDungeon.player.energy.energyMaster;
            --AbstractDungeon.player.energy.energy;
            energyEnable = false;
        }
    }

    @Override
    public void onRightClick() {
        if (!isObtained || !isPlayerTurn || this.counter > 0) {
            return;
        }
        if (AbstractDungeon.getCurrRoom() == null || AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT) {
            return;
        }

        flash();
        this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));

        this.addToBot(new DrawCardAction(AbstractDungeon.player, _Draw));
        EnergyPanel.setEnergy(AbstractDungeon.player.energy.energyMaster);
        setCount(_CD);
        stopPulse();
    }

    @Override
    public void atBattleStart() {
        if (this.counter <= 0) {
            beginLongPulse();
        }
    }

    @Override
    public void atTurnStart() {
        isPlayerTurn = true;
        setCount(this.counter - 1);
    }

    @Override
    public void onPlayerEndTurn() {
        isPlayerTurn = false;
        stopPulse();
    }

    @Override
    public void onVictory() {
        stopPulse();
    }

    @Override
    public String getUpdatedDescription() {
        return StringUtil.format(DESCRIPTIONS[0], _Draw, _CD);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new RefresherOrb();
    }
}
