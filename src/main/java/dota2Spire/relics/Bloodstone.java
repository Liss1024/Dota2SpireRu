package dota2Spire.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import dota2Spire.Dota2Spire;
import dota2Spire.util.StringUtil;
import dota2Spire.util.TextureLoader;

import static dota2Spire.Dota2Spire.makeRelicPath;

/**
 * 血精石
 * 回合开始时给1能量
 * 回合结束时未使用的能量转化为点数，战斗结束时失去所有点数回复相应血量
 */
public class Bloodstone extends CustomRelic {
    // ID, images, text.
    public static final String ID = Dota2Spire.makeID("Bloodstone");
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("Bloodstone.png"));
    private static final int _Energy = 1;

    public Bloodstone() {
        super(ID, IMG, RelicTier.BOSS, LandingSound.MAGICAL);
    }

    @Override
    public void onEquip() {
        this.counter = 0;
    }

    private void setCount(int count) {
        if (count <= 0) {
            this.counter = 0;
            stopPulse();
        } else {
            this.counter = count;
            beginLongPulse();
        }
    }

    @Override
    public void atTurnStart() {
        flash();
        this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        this.addToBot(new GainEnergyAction(_Energy));
    }

    @Override
    public void onPlayerEndTurn() {
        if (EnergyPanel.getCurrentEnergy() > 0) {
            setCount(this.counter + EnergyPanel.getCurrentEnergy());
        }
    }

    @Override
    public void onVictory() {
        if (AbstractDungeon.player.currentHealth > 0) {
            flash();
            AbstractDungeon.player.heal(this.counter);
            setCount(0);
        }
    }

    @Override
    public String getUpdatedDescription() {
        return StringUtil.format(DESCRIPTIONS[0]);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new Bloodstone();
    }
}
