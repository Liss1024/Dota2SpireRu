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

public class OctarineCore extends CustomRelic {
    // ID, images, text.
    public static final String ID = Dota2Spire.makeID("OctarineCore");
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("OctarineCore.png"));
//    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("placeholder_relic.png"));

    private static final int _Count = 2;
    private boolean attack = false;

    public OctarineCore() {
//        super(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.MAGICAL);
        super(ID, IMG, RelicTier.RARE, LandingSound.MAGICAL);
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == AbstractCard.CardType.SKILL) {
            if (attack) {
                attack = false;
                this.counter = 1;
            } else {
                ++this.counter;
                if (this.counter == _Count) {
                    flash();
                    addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
                    CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                    for (AbstractCard abstractCard : AbstractDungeon.player.hand.group) {
                        if (abstractCard.type == AbstractCard.CardType.ATTACK && abstractCard.cost > 0 && abstractCard.costForTurn > 0 && !abstractCard.freeToPlay()) {
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
            }
        } else if (card.type == AbstractCard.CardType.ATTACK) {
            if (!attack) {
                attack = true;
                this.counter = 1;
            } else {
                ++this.counter;
                if (this.counter == _Count) {
                    flash();
                    addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
                    CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                    for (AbstractCard abstractCard : AbstractDungeon.player.hand.group) {
                        if (abstractCard.type == AbstractCard.CardType.SKILL && abstractCard.cost > 0 && abstractCard.costForTurn > 0 && !abstractCard.freeToPlay()) {
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
            }
        } else {
            this.counter = 0;
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
