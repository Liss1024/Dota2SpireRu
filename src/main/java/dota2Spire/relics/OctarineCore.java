package dota2Spire.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import dota2Spire.Dota2Spire;
import dota2Spire.util.StringUtil;
import dota2Spire.util.TextureLoader;

import static dota2Spire.Dota2Spire.makeRelicPath;

/**
 * 玲珑心
 * 每连续使用2攻击牌手里随机一张技能牌免费
 * 每连续使用2技能牌手里随机一张攻击牌免费
 */
public class OctarineCore extends CustomRelic {
    // ID, images, text.
    public static final String ID = Dota2Spire.makeID("OctarineCore");
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("OctarineCore.png"));
    private static final int _Count = 2;
    private AbstractCard.CardType lastCardType = AbstractCard.CardType.ATTACK;

    public OctarineCore() {
        super(ID, IMG, RelicTier.RARE, LandingSound.MAGICAL);
    }

    @Override
    public void onEquip() {
        this.counter = 0;
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.type != AbstractCard.CardType.ATTACK && card.type != AbstractCard.CardType.SKILL) {
            return;
        }
        if (this.counter == 0) {
            lastCardType = card.type;
            this.counter++;
            return;
        }
        if (card.type == lastCardType) {
            this.counter++;
            if (this.counter == _Count) {
                flash();
                addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
                CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                AbstractCard.CardType freeType = card.type == AbstractCard.CardType.ATTACK ? AbstractCard.CardType.SKILL : AbstractCard.CardType.ATTACK;
                for (AbstractCard abstractCard : AbstractDungeon.player.hand.group) {
                    if (abstractCard.type == freeType && abstractCard.cost > 0 && abstractCard.costForTurn > 0 && !abstractCard.freeToPlay()) {
                        group.addToBottom(abstractCard);
                    }
                }
                if (!group.isEmpty()) {
                    AbstractCard randomCard = group.getRandomCard(true);
                    randomCard.freeToPlayOnce = true;
                    randomCard.superFlash(Color.GOLD);
                }
                this.counter = 0;
            }
        } else {
            lastCardType = card.type;
            this.counter = 1;
        }
    }

    @Override
    public void atTurnStart() {
        this.counter = 0;
    }

    @Override
    public String getUpdatedDescription() {
        return StringUtil.format(DESCRIPTIONS[0], _Count);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new OctarineCore();
    }
}
