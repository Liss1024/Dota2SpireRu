package dota2Spire.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import dota2Spire.Dota2Spire;
import dota2Spire.util.StringUtil;
import dota2Spire.util.TextureLoader;

import static dota2Spire.Dota2Spire.makeRelicPath;

public class OrchidMalevolence extends CustomRelic {
    // ID, images, text.
    public static final String ID = Dota2Spire.makeID("OrchidMalevolence");
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("OrchidMalevolence.png"));
//    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("placeholder_relic.png"));

    private static final int _VulnerableStack = 2;

    public OrchidMalevolence() {
//        super(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.MAGICAL);
        super(ID, IMG, RelicTier.UNCOMMON, LandingSound.MAGICAL);
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == AbstractCard.CardType.POWER) {
            AbstractMonster randomMonster = AbstractDungeon.getCurrRoom().monsters.getRandomMonster(true);
            if (randomMonster != null) {
                this.flash();
                addToBot(new RelicAboveCreatureAction(randomMonster, this));
                AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(randomMonster, AbstractDungeon.player, new VulnerablePower(randomMonster, _VulnerableStack, false), _VulnerableStack));
            }
        }

    }

    @Override
    public String getUpdatedDescription() {
        return StringUtil.format(DESCRIPTIONS[0], _VulnerableStack);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new OrchidMalevolence();
    }
}
