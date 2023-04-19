package dota2Spire.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.unique.GamblingChipAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import dota2Spire.Dota2Spire;
import dota2Spire.util.StringUtil;
import dota2Spire.util.TextureLoader;

import static dota2Spire.Dota2Spire.makeRelicPath;

/**
 * 跳刀
 * 回合开始时丢弃任意张牌，然后抽相同数量张牌。
 * CD 4回合
 */
public class BlinkDagger extends CustomRelic {
    // ID, images, text.
    public static final String ID = Dota2Spire.makeID("BlinkDagger");
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("BlinkDagger.png"));

    private static final int _CD = 4;

    public BlinkDagger() {
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
    public void atTurnStartPostDraw() {
        if (this.counter <= 0) {
            setCount(_CD);
            this.flash();
            this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            this.addToBot(new GamblingChipAction(AbstractDungeon.player));
        } else {
            setCount(this.counter - 1);
        }
    }

    @Override
    public String getUpdatedDescription() {
        return StringUtil.format(DESCRIPTIONS[0], _CD);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new BlinkDagger();
    }
}
