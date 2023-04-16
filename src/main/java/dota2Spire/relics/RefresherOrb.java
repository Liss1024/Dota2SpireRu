package dota2Spire.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import dota2Spire.Dota2Spire;
import dota2Spire.util.StringUtil;
import dota2Spire.util.TextureLoader;

import static dota2Spire.Dota2Spire.makeRelicPath;

/**
 * 刷新球
 * 回合开始时给1能量
 * 回合结束时未使用的能量转化为下回合抽牌+
 */
public class RefresherOrb extends CustomRelic {
    // ID, images, text.
    public static final String ID = Dota2Spire.makeID("RefresherOrb");
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("RefresherOrb.png"));
    private static final int _Energy = 1;

    public RefresherOrb() {
        super(ID, IMG, RelicTier.BOSS, LandingSound.MAGICAL);
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
            flash();
            this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DrawCardNextTurnPower(AbstractDungeon.player, EnergyPanel.getCurrentEnergy())));
        }
    }

    @Override
    public String getUpdatedDescription() {
        return StringUtil.format(DESCRIPTIONS[0]);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new RefresherOrb();
    }
}
