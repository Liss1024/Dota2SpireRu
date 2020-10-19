package dota2Spire.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ThornsPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import dota2Spire.Dota2Spire;
import dota2Spire.util.StringUtil;
import dota2Spire.util.TextureLoader;

import static dota2Spire.Dota2Spire.makeRelicPath;

public class BladeMail extends CustomRelic implements ClickableRelic {
    // ID, images, text.
    public static final String ID = Dota2Spire.makeID("BladeMail");
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("BladeMail.png"));
    //    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("placeholder_relic.png"));
    private boolean isPlayerTurn = false;
    private boolean onEffect = false;

    private static final int _Thorns = 8;
    private static final int _Block = 8;
    private static final int _CD = 5;

    public BladeMail() {
//        super(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.MAGICAL);
        super(ID, IMG, RelicTier.COMMON, LandingSound.MAGICAL);
    }

    @Override
    public void onEquip() {
        setCount(0);
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

        int thorns = 0;
        for (AbstractPower power : AbstractDungeon.player.powers) {
            if (ThornsPower.POWER_ID.equals(power.ID)) {
                thorns += power.amount;
            }
        }
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));

        addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                new ThornsPower(AbstractDungeon.player, thorns + _Thorns), thorns + _Thorns));
        addToBot(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, _Block));

        onEffect = true;
        setCount(_CD);
    }

    private void setCount(int count) {
        if (count <= 0) {
            this.counter = -1;
            flash();
            beginLongPulse();
        } else {
            this.counter = count;
            stopPulse();
        }
    }

    @Override
    public void atTurnStart() {
        isPlayerTurn = true;
        if (onEffect) {
            onEffect = false;
            this.addToBot(new RemoveSpecificPowerAction(AbstractDungeon.player, AbstractDungeon.player, ThornsPower.POWER_ID));
        }
        setCount(this.counter - 1);
    }

    @Override
    public void onPlayerEndTurn() {
        isPlayerTurn = false;
        stopPulse();
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
