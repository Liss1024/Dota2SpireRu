package dota2Spire.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import dota2Spire.Dota2Spire;
import dota2Spire.util.TextureLoader;

import static dota2Spire.Dota2Spire.makePowerPath;

public class Invisibility extends AbstractPower {
    public static final String POWER_ID = Dota2Spire.makeID("Invisibility");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private boolean justAdded = true;

    public Invisibility(final AbstractCreature owner, final int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;
        this.img = TextureLoader.getTexture(makePowerPath("Invisibility.png"));
        justAdded = true;
        updateDescription();
    }

    private void changeAmount(int amount) {
        if (amount <= 0) {
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
        } else {
            this.amount = amount;
        }
    }

    @Override
    public void atStartOfTurn() {
        justAdded = false;
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (!justAdded) {
            changeAmount(this.amount - 1);
        }
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.type == DamageInfo.DamageType.NORMAL) {
            flash();
            return (int) Math.ceil(damageAmount / 2f);
        }
        return damageAmount;
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == AbstractCard.CardType.ATTACK) {
            changeAmount(0);
        }
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        return type == DamageInfo.DamageType.NORMAL ? damage + 4 : damage;
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

}
