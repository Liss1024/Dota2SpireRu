package dota2Spire.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import dota2Spire.Dota2Spire;
import dota2Spire.powers.Lifesteal;
import dota2Spire.util.StringUtil;
import dota2Spire.util.TextureLoader;

import static dota2Spire.Dota2Spire.makeRelicPath;

/**
 * 疯脸
 * 获得1易伤
 * 获得20吸血
 * cd 4回合
 */
public class MaskOfMadness extends CustomRelic {
    // ID, images, text.
    public static final String ID = Dota2Spire.makeID("MaskOfMadness");
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("MaskOfMadness.png"));
    private static final int _LifestealStack = 20;
    private static final int _VulnerableStack = 1;
    private static final int _CD = 4;

    public MaskOfMadness() {
        super(ID, IMG, RelicTier.COMMON, LandingSound.MAGICAL);
    }

    @Override
    public void onEquip() {
        setCount(0);
    }

    private void setCount(int count) {
        if (count <= 0) {
            this.counter = -1;
            beginLongPulse();
        } else {
            this.counter = count;
            stopPulse();
        }
    }

    @Override
    public void atTurnStart() {
        if (this.counter <= 0) {
            flash();
            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));

            this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                    new Lifesteal(AbstractDungeon.player, _LifestealStack)));
            this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                    new VulnerablePower(AbstractDungeon.player, _VulnerableStack, false)));
            setCount(_CD);
        } else {
            setCount(this.counter - 1);
        }
    }

    @Override
    public String getUpdatedDescription() {
        return StringUtil.format(DESCRIPTIONS[0], _LifestealStack, _VulnerableStack, _CD);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new MaskOfMadness();
    }

}
