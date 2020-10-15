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

import java.util.ArrayList;
import java.util.List;

import static dota2Spire.Dota2Spire.makeRelicPath;

public class Desolator extends CustomRelic {
    // ID, images, text.
    public static final String ID = Dota2Spire.makeID("Desolator");
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("Desolator.png"));
    //    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("placeholder_relic.png"));
    private List<Integer> ids;

    private static final int _BuffStack = 2;

    public Desolator() {
//        super(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.MAGICAL);
        super(ID, IMG, RelicTier.UNCOMMON, LandingSound.MAGICAL);
    }

    @Override
    public void onEquip() {
        ids = new ArrayList<>();
    }

    @Override
    public void atBattleStart() {
        if (ids == null) {
            ids = new ArrayList<>();
        } else {
            ids.clear();
        }
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == AbstractCard.CardType.ATTACK) {
            if (card.target == AbstractCard.CardTarget.ENEMY) {
                AbstractMonster m = (AbstractMonster) action.target;
                if (m != null) {
                    int hash = m.hashCode();
                    if (!ids.contains(hash)) {
                        flash();
                        addToBot(new RelicAboveCreatureAction(m, this));
                        addToBot(new ApplyPowerAction(m, AbstractDungeon.player, new VulnerablePower(m, _BuffStack, false)));
                        ids.add(hash);
                    }
                }
            } else if (card.target == AbstractCard.CardTarget.ALL_ENEMY) {
                for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                    int hash = m.hashCode();
                    if (!ids.contains(hash)) {
                        flash();
                        addToBot(new RelicAboveCreatureAction(m, this));
                        addToBot(new ApplyPowerAction(m, AbstractDungeon.player, new VulnerablePower(m, _BuffStack, false)));
                        ids.add(hash);
                    }
                }
            }
        }
    }

    @Override
    public String getUpdatedDescription() {
        return StringUtil.format(DESCRIPTIONS[0], _BuffStack);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new Desolator();
    }
}
