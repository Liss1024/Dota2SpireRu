package dota2Spire.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import dota2Spire.Dota2Spire;
import dota2Spire.util.StringUtil;
import dota2Spire.util.TextureLoader;

import static dota2Spire.Dota2Spire.makeRelicPath;

/**
 * BKB
 * 回合开始时清除自身所有debuff
 * cd 5回合
 */
public class BlackKingBar extends CustomRelic {
    // ID, images, text.
    public static final String ID = Dota2Spire.makeID("BlackKingBar");
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("BlackKingBar.png"));
    private static final int _CD = 5;

    public BlackKingBar() {
        super(ID, IMG, RelicTier.UNCOMMON, LandingSound.MAGICAL);
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
            if (AbstractDungeon.getCurrRoom() == null || AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT) {
                return;
            }
            flash();
            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            int debuffCount = 0;
            for (AbstractPower power : AbstractDungeon.player.powers) {
                if (power.type == AbstractPower.PowerType.DEBUFF) {
                    debuffCount++;
                    addToBot(new RemoveSpecificPowerAction(AbstractDungeon.player, AbstractDungeon.player, power));
                }
            }
            if (debuffCount > 0) {
                setCount(_CD);
            }
        } else {
            setCount(this.counter - 1);
        }
    }

    @Override
    public String getUpdatedDescription() {
        return StringUtil.format(this.DESCRIPTIONS[0], _CD);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new BlackKingBar();
    }

}
