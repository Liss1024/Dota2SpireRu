package dota2Spire.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.evacipated.cardcrawl.mod.stslib.relics.OnReceivePowerRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import dota2Spire.Dota2Spire;
import dota2Spire.util.StringUtil;
import dota2Spire.util.TextureLoader;

import static dota2Spire.Dota2Spire.makeRelicPath;

public class LotusOrb extends CustomRelic implements ClickableRelic, OnReceivePowerRelic {
    // ID, images, text.
    public static final String ID = Dota2Spire.makeID("LotusOrb");
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("LotusOrb.png"));
    //    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("placeholder_relic.png"));
    private boolean isPlayerTurn = false;
    private boolean onEffect = false;

    private static final int _CD = 3;

    public LotusOrb() {
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
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));

        onEffect = true;
        this.counter = -1;
    }

    @Override
    public boolean onReceivePower(AbstractPower power, AbstractCreature source) {
        if (onEffect && source != null && source != AbstractDungeon.player) {
            if (power.ID.equals("Weakened")) {
                flash();
                addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
                AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(source, AbstractDungeon.player, new WeakPower(source, power.amount, true), power.amount));
            }
            if (power.ID.equals("Vulnerable")) {
                flash();
                addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
                AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(source, AbstractDungeon.player, new VulnerablePower(source, power.amount, true), power.amount));
            }
        }
        return true;
    }

    private void setCount(int count) {
        if (count <= 0) {
            this.counter = 0;
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
            setCount(_CD - 1);
        } else {
            setCount(this.counter - 1);
        }
    }

    @Override
    public void onPlayerEndTurn() {
        isPlayerTurn = false;
        stopPulse();
    }

    @Override
    public String getUpdatedDescription() {
        return StringUtil.format(DESCRIPTIONS[0], _CD);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new LotusOrb();
    }

}
