package dota2Spire.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.common.RemoveAllBlockAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import dota2Spire.Dota2Spire;
import dota2Spire.util.StringUtil;
import dota2Spire.util.TextureLoader;

import static dota2Spire.Dota2Spire.makeRelicPath;

/**
 * 散失
 * 下一张攻击牌打出时先清除敌方护甲
 * CD 3回合
 */
public class DiffusalBlade extends CustomRelic {
    // ID, images, text.
    public static final String ID = Dota2Spire.makeID("DiffusalBlade");
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("DiffusalBlade.png"));

    private static final int _CD = 4;

    public DiffusalBlade() {
        super(ID, IMG, RelicTier.COMMON, LandingSound.MAGICAL);
    }

    @Override
    public void onEquip() {
        setCount(0);
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
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (this.counter > 0) {
            return;
        }
        if (card.type == AbstractCard.CardType.ATTACK && card.target == AbstractCard.CardTarget.ENEMY) {
            AbstractMonster m = (AbstractMonster) action.target;
            if (m != null && m.currentBlock > 0) {
                this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
                this.addToTop(new RemoveAllBlockAction(m, AbstractDungeon.player));
                this.setCount(_CD);
            }
        }

    }

    @Override
    public void atTurnStart() {
        setCount(this.counter - 1);
    }

    @Override
    public String getUpdatedDescription() {
        return StringUtil.format(DESCRIPTIONS[0], _CD);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new DiffusalBlade();
    }
}
