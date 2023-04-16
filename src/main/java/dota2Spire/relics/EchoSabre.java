package dota2Spire.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import dota2Spire.Dota2Spire;
import dota2Spire.util.StringUtil;
import dota2Spire.util.TextureLoader;

import static dota2Spire.Dota2Spire.makeRelicPath;

/**
 * 回音战刃
 * 下一张攻击牌结算2次
 * CD 3回合
 */
public class EchoSabre extends CustomRelic {
    // ID, images, text.
    public static final String ID = Dota2Spire.makeID("EchoSabre");
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("EchoSabre.png"));

    private static final int _CD = 3;

    public EchoSabre() {
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
        if (card.type == AbstractCard.CardType.ATTACK) {
            AbstractMonster m = null;
            if (card.target != null)
                m = (AbstractMonster) action.target;
            this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            AbstractCard tmp = card.makeSameInstanceOf();
            tmp.current_x = card.current_x;
            tmp.current_y = card.current_y;
            tmp.target_x = (float) Settings.WIDTH / 2.0F - 300.0F * Settings.scale;
            tmp.target_y = (float) Settings.HEIGHT / 2.0F;
            tmp.applyPowers();
            tmp.purgeOnUse = true;
            AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(tmp, m, card.energyOnUse, true, true), true);
            flash();
            AbstractDungeon.player.hand.refreshHandLayout();
            setCount(_CD);
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
        return new EchoSabre();
    }
}
