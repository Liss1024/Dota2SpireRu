package dota2Spire.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import dota2Spire.Dota2Spire;
import dota2Spire.util.StringUtil;
import dota2Spire.util.TextureLoader;

import static dota2Spire.Dota2Spire.makeRelicPath;

public class CrimsonGuard extends CustomRelic implements ClickableRelic {
    // ID, images, text.
    public static final String ID = Dota2Spire.makeID("CrimsonGuard");
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("CrimsonGuard.png"));
    //    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("placeholder_relic.png"));
    private boolean isPlayerTurn = false;
    private int effectRound = 0;

    private static final int _CD = 7;
    private static final int _Block = 8;
    private static final int _Continued = 3;

    public CrimsonGuard() {
//        super(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.MAGICAL);
        super(ID, IMG, RelicTier.UNCOMMON, LandingSound.MAGICAL);
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
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        addToBot(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, _Block));
        effectRound = _Continued;
        effectRound--;
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
        if (effectRound > 0) {
            effectRound--;
            flash();
            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            addToBot(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, _Block));
        }
        setCount(this.counter - 1);
    }

    @Override
    public void onPlayerEndTurn() {
        isPlayerTurn = false;
        if (effectRound > 0) {
            beginLongPulse();
        } else {
            stopPulse();
        }
    }

    @Override
    public void onVictory() {
        effectRound = 0;
    }

    @Override
    public String getUpdatedDescription() {
        return StringUtil.format(this.DESCRIPTIONS[0], _Continued, _Block, _CD);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new CrimsonGuard();
    }

}
