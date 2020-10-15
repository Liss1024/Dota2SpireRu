package dota2Spire.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import dota2Spire.Dota2Spire;
import dota2Spire.util.StringUtil;
import dota2Spire.util.TextureLoader;

import static dota2Spire.Dota2Spire.makeRelicPath;

public class DrumOfEndurance extends CustomRelic implements ClickableRelic {
    // ID, images, text.
    public static final String ID = Dota2Spire.makeID("DrumOfEndurance");
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("DrumOfEndurance.png"));
    //    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("placeholder_relic.png"));

    private boolean usedThisBattle = false;
    private boolean isPlayerTurn = false;

    private static final int _StartPoint = 5;
    private static final int _Str = 1;
    private static final int _Dex = 1;

    public DrumOfEndurance() {
//        super(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.MAGICAL);
        super(ID, IMG, RelicTier.COMMON, LandingSound.MAGICAL);
    }

    @Override
    public void onEquip() {
        this.counter = _StartPoint;
    }

    @Override
    public void onRightClick() {
        if (!isObtained || usedThisBattle || !isPlayerTurn || this.counter <= 0) {
            return;
        }

        if (AbstractDungeon.getCurrRoom() == null || AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT) {
            return;
        }
        flash();
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));

        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                new StrengthPower(AbstractDungeon.player, _Str), _Str));
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                new DexterityPower(AbstractDungeon.player, _Dex), _Dex));
        this.counter--;

        usedThisBattle = true;
        stopPulse();

    }

    @Override
    public void atBattleStart() {
        usedThisBattle = false;
        if (this.counter > 0) {
            beginLongPulse();
        }
    }

    @Override
    public void atTurnStart() {
        isPlayerTurn = true;
        if (this.counter > 0 && !usedThisBattle) {
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
        return StringUtil.format(DESCRIPTIONS[0], _StartPoint, _Str, _Dex);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new DrumOfEndurance();
    }
}
