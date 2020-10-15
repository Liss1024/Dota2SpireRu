package dota2Spire.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import dota2Spire.Dota2Spire;
import dota2Spire.powers.Invisibility;
import dota2Spire.powers.Lifesteal;
import dota2Spire.util.StringUtil;
import dota2Spire.util.TextureLoader;

import static dota2Spire.Dota2Spire.makeRelicPath;

public class ShadowBlade extends CustomRelic implements ClickableRelic {
    // ID, images, text.
    public static final String ID = Dota2Spire.makeID("ShadowBlade");
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("ShadowBlade.png"));
    //    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("placeholder_relic.png"));
    private boolean isPlayerTurn = false;

    private static final int _CD = 4;
    private static final int _BuffStack = 2;


    public ShadowBlade() {
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

        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                new Invisibility(AbstractDungeon.player, _BuffStack), _BuffStack));

        setCount(_CD);
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
        setCount(this.counter - 1);
    }

    @Override
    public void onPlayerEndTurn() {
        isPlayerTurn = false;
        stopPulse();
    }

    @Override
    public String getUpdatedDescription() {
        return StringUtil.format(DESCRIPTIONS[0], _BuffStack, _CD);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new ShadowBlade();
    }

}
