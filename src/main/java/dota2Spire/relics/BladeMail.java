package dota2Spire.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.ThornsPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import dota2Spire.Dota2Spire;
import dota2Spire.util.StringUtil;
import dota2Spire.util.TextureLoader;

import static dota2Spire.Dota2Spire.makeRelicPath;

/**
 * 刃甲
 * 回合结束时获得8护甲4荆棘
 * cd 5回合
 */
public class BladeMail extends CustomRelic{
    // ID, images, text.
    public static final String ID = Dota2Spire.makeID("BladeMail");
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("BladeMail.png"));

    private static final int _Thorns = 4;
    private static final int _Block = 8;
    private static final int _CD = 5;

    public BladeMail() {
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
        setCount(this.counter - 1);
    }

    @Override
    public void onPlayerEndTurn() {
        if (this.counter <= 0) {
            if (AbstractDungeon.getCurrRoom() == null || AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT) {
                return;
            }
            flash();

            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));

            addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                    new ThornsPower(AbstractDungeon.player, _Thorns)));

            addToBot(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, _Block));
            setCount(_CD);
            stopPulse();
        }
    }

    @Override
    public String getUpdatedDescription() {
        return StringUtil.format(DESCRIPTIONS[0], _Thorns, _Block, _CD);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new BladeMail();
    }

}
