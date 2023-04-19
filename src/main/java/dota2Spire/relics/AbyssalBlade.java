package dota2Spire.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.powers.StunMonsterPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import dota2Spire.Dota2Spire;
import dota2Spire.util.StringUtil;
import dota2Spire.util.TextureLoader;

import static dota2Spire.Dota2Spire.makeRelicPath;

/**
 * 深渊之刃
 * 战斗开始时给2力量
 * 第10张攻击牌给敌人眩晕
 */
public class AbyssalBlade extends CustomRelic {
    // ID, images, text.
    public static final String ID = Dota2Spire.makeID("AbyssalBlade");
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("AbyssalBlade.png"));
    private static final int _StunNeed = 10;
    private static final int _StrengthStack = 2;

    public AbyssalBlade() {
        super(ID, IMG, RelicTier.RARE, LandingSound.MAGICAL);
        this.counter = 0;
    }

    @Override
    public void atBattleStart() {
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, _StrengthStack)));
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == AbstractCard.CardType.ATTACK) {
            ++this.counter;
            if (this.counter == _StunNeed) {
                if (card.target == AbstractCard.CardTarget.ENEMY) {
                    this.counter = 0;
                    this.flash();
                    AbstractMonster m = (AbstractMonster) action.target;
                    this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
                    this.addToBot(new ApplyPowerAction(m, AbstractDungeon.player, new StunMonsterPower(m, 1)));
                    this.stopPulse();
                } else {
                    --this.counter;
                }
            } else if (this.counter == 9) {
                this.beginLongPulse();
            }
        }
    }

    @Override
    public String getUpdatedDescription() {
        return StringUtil.format(DESCRIPTIONS[0], _StrengthStack, _StunNeed);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new AbyssalBlade();
    }
}
