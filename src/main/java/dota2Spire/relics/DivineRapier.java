package dota2Spire.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import dota2Spire.Dota2Spire;
import dota2Spire.util.StringUtil;
import dota2Spire.util.TextureLoader;

import static dota2Spire.Dota2Spire.makeRelicPath;

/**
 * 圣剑
 * 每隔2回合获得1易伤1脆弱
 */
public class DivineRapier extends CustomRelic {
    // ID, images, text.
    public static final String ID = Dota2Spire.makeID("DivineRapier");
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("DivineRapier.png"));
    private static final int _Damage = 6;
    private static final int _VulnerableStack = 1;
    private static final int _FrailStack = 1;
    private static final int _CD = 2;

    public DivineRapier() {
        super(ID, IMG, RelicTier.BOSS, LandingSound.MAGICAL);
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
    public void onEquip() {
        setCount(_CD);
    }

    @Override
    public void atTurnStart() {
        if (this.counter <= 0) {
            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                    new VulnerablePower(AbstractDungeon.player, _VulnerableStack, false)));
            addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                    new FrailPower(AbstractDungeon.player, _FrailStack, false)));
            setCount(_CD);
        } else {
            setCount(this.counter - 1);
        }
    }

    @Override
    public void atBattleStart() {

    }

    @Override
    public float atDamageModify(float damage, AbstractCard c) {
        if (c.type == AbstractCard.CardType.ATTACK) {
            return damage + _Damage;
        }
        return damage;
    }

    @Override
    public String getUpdatedDescription() {
        return StringUtil.format(DESCRIPTIONS[0], _Damage, _VulnerableStack, _FrailStack, _CD);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new DivineRapier();
    }
}
