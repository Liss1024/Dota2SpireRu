package dota2Spire.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import dota2Spire.Dota2Spire;
import dota2Spire.util.StringUtil;
import dota2Spire.util.TextureLoader;

import static dota2Spire.Dota2Spire.makeRelicPath;

public class ArmletOfMordiggian extends CustomRelic implements ClickableRelic {
    // ID, images, text.
    public static final String ID = Dota2Spire.makeID("ArmletOfMordiggian");
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("ArmletOfMordiggian.png"));
    //    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("placeholder_relic.png"));
    private boolean isPlayerTurn = false;
    private boolean isActive = false;

    private static final int _Strength = 2;
    private static final int _HPLose = 2;

    public ArmletOfMordiggian() {
//        super(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.MAGICAL);
        super(ID, IMG, RelicTier.COMMON, LandingSound.MAGICAL);
    }

    @Override
    public void onRightClick() {
        if (!isObtained || !isPlayerTurn) {
            return;
        }

        if (AbstractDungeon.getCurrRoom() == null || AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT) {
            return;
        }
        flash();
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));

        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                new StrengthPower(AbstractDungeon.player, _Strength), _Strength));
        AbstractDungeon.player.damage(new DamageInfo(AbstractDungeon.player, _HPLose, DamageInfo.DamageType.HP_LOSS));

        setActive(true);
    }

    private void setActive(boolean isActive) {
        this.isActive = isActive;
        if (!isActive) {
            stopPulse();
        } else {
            beginLongPulse();
        }
    }

    @Override
    public void atTurnStart() {
        isPlayerTurn = true;
        if (isActive) {
            flash();
            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            AbstractDungeon.player.damage(new DamageInfo(AbstractDungeon.player, _HPLose, DamageInfo.DamageType.HP_LOSS));
        }
    }

    @Override
    public void onVictory() {
        setActive(false);
    }

    @Override
    public void onPlayerEndTurn() {
        isPlayerTurn = false;
    }

    @Override
    public String getUpdatedDescription() {
        return StringUtil.format(DESCRIPTIONS[0], _Strength, _HPLose);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new ArmletOfMordiggian();
    }

}
