package dota2Spire.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import dota2Spire.Dota2Spire;
import dota2Spire.util.StringUtil;
import dota2Spire.util.TextureLoader;

import static dota2Spire.Dota2Spire.makeRelicPath;

public class HeartOfTarrasque extends CustomRelic {
    // ID, images, text.
    public static final String ID = Dota2Spire.makeID("HeartOfTarrasque");
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("HeartOfTarrasque.png"));
//    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("placeholder_relic.png"));

    private static final int _MaxHP = 8;
    private static final int _HP = 4;
    private static final int _Round = 3;

    public HeartOfTarrasque() {
//        super(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.MAGICAL);
        super(ID, IMG, RelicTier.RARE, LandingSound.MAGICAL);
    }

    @Override
    public void onEquip() {
        setCount(3);
        AbstractDungeon.player.increaseMaxHp(_MaxHP, true);
    }

    @Override
    public void atBattleStart() {
        setCount(_Round);
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
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.type != DamageInfo.DamageType.HP_LOSS && damageAmount > 0) {
            setCount(_Round);
        }
        return super.onAttacked(info, damageAmount);
    }

    @Override
    public void atTurnStart() {
        if (this.counter == 0) {
            this.flash();
            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            AbstractDungeon.player.heal(_HP);
        }
    }

    @Override
    public void onVictory() {
        setCount(_Round);
    }

    @Override
    public void onPlayerEndTurn() {
        setCount(this.counter - 1);
    }

    @Override
    public String getUpdatedDescription() {
        return StringUtil.format(DESCRIPTIONS[0], _MaxHP, _Round, _HP);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new HeartOfTarrasque();
    }
}
