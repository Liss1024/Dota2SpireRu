package dota2Spire.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import dota2Spire.Dota2Spire;
import dota2Spire.powers.HpRecovery;
import dota2Spire.powers.MpRecovery;
import dota2Spire.util.StringUtil;
import dota2Spire.util.TextureLoader;

import static dota2Spire.Dota2Spire.makeRelicPath;

public class MagicBottle extends CustomRelic implements ClickableRelic {
    // ID, images, text.
    public static final String ID = Dota2Spire.makeID("MagicBottle");
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("MagicBottle.png"));
    //    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("placeholder_relic.png"));

    private boolean usedThisTurn = false;
    private boolean isPlayerTurn = false;

    private static final int _HpRecovery = 3;
    private static final int _MpRecovery = 2;
    private static final int _MaxPoint = 2;

    public MagicBottle() {
//        super(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.MAGICAL);
        super(ID, IMG, RelicTier.COMMON, LandingSound.MAGICAL);
    }

    @Override
    public void onEquip() {
        this.counter = _MaxPoint;
    }

    @Override
    public void onRightClick() {
        if (!isObtained || usedThisTurn || !isPlayerTurn || this.counter <= 0) {
            return;
        }

        if (AbstractDungeon.getCurrRoom() == null || AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT) {
            return;
        }
        flash();
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));

        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                new HpRecovery(AbstractDungeon.player, _HpRecovery), _HpRecovery));
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                new MpRecovery(AbstractDungeon.player, _MpRecovery), _MpRecovery));
        this.counter--;
        if (this.counter == 0) {
            stopPulse();
        }
    }

    @Override
    public void onEnterRestRoom() {
        flash();
        this.counter = _MaxPoint;
    }

    @Override
    public void atTurnStart() {
        usedThisTurn = false;
        isPlayerTurn = true;
        if (this.counter > 0) {
            beginLongPulse();
        }
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
        return StringUtil.format(DESCRIPTIONS[0], _MaxPoint, _HpRecovery, _MpRecovery);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new MagicBottle();
    }
}
