package dota2Spire.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import dota2Spire.Dota2Spire;
import dota2Spire.util.StringUtil;
import dota2Spire.util.TextureLoader;

import static dota2Spire.Dota2Spire.makeRelicPath;

public class Bloodstone extends CustomRelic implements ClickableRelic {
    // ID, images, text.
    public static final String ID = Dota2Spire.makeID("Bloodstone");
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("Bloodstone.png"));
    //    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("placeholder_relic.png"));

    private boolean usedThisBattle = false;
    private boolean isPlayerTurn = false;
    private int loseHp = 0;

    private static final int _StartPoint = 5;
    private static final int _ThresholdPoint = 5;
    private static final int _HPPercent = 30;
    private static final int _HPHeal = 3;
    private static final int _Energy = 1;

    public Bloodstone() {
//        super(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.MAGICAL);
        super(ID, IMG, RelicTier.BOSS, LandingSound.MAGICAL);
    }

    private void setCount(int count) {
        if (count <= 0) {
            this.counter = 0;
            stopPulse();
        } else {
            this.counter = count;
        }
    }

    @Override
    public void onEquip() {
        setCount(_StartPoint);
    }

    @Override
    public void onRightClick() {
        if (!isObtained || usedThisBattle || !isPlayerTurn || this.counter <= 1) {
            return;
        }
        if (AbstractDungeon.getCurrRoom() == null || AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT) {
            return;
        }

        int e = this.counter / 2;
        if (e >= 1) {
            flash();
            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            addToBot(new GainEnergyAction(e));
            AbstractDungeon.player.heal(_HPHeal * e);
            setCount(this.counter - e);
            usedThisBattle = true;
            stopPulse();

        }
    }

    @Override
    public void onLoseHp(int damageAmount) {
        loseHp += damageAmount;
        int threshold = (int) (AbstractDungeon.player.maxHealth * _HPPercent / 100f);
        while (loseHp >= threshold) {
            loseHp -= threshold;
            setCount(this.counter - 1);
        }
    }

    @Override
    public void onMonsterDeath(AbstractMonster m) {
        if (!m.hasPower("Minion")) {
            setCount(this.counter + 1);
            flash();
        }
    }

    @Override
    public void atBattleStart() {
        usedThisBattle = false;
        setCount(this.counter);
        if (this.counter > 0) {
            beginLongPulse();
        }
    }

    @Override
    public void atTurnStart() {
        isPlayerTurn = true;
        if (this.counter >= _ThresholdPoint) {
            flash();
            this.addToBot(new GainEnergyAction(_Energy));
            if (!usedThisBattle){
                beginLongPulse();
            }
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
        return StringUtil.format(DESCRIPTIONS[0], _StartPoint, _ThresholdPoint, _HPPercent, _HPHeal);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new Bloodstone();
    }
}
