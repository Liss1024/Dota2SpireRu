package dota2Spire.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import dota2Spire.Dota2Spire;
import dota2Spire.util.TextureLoader;

import static dota2Spire.Dota2Spire.makePowerPath;

public class Lifesteal extends AbstractPower {
    public static final String POWER_ID = Dota2Spire.makeID("Lifesteal");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public Lifesteal(final AbstractCreature owner, final int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;
        this.img = TextureLoader.getTexture(makePowerPath("Lifesteal.png"));

        updateDescription();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (target != null && info.type == DamageInfo.DamageType.NORMAL && this.amount > 0) {
            int heal = Math.round(damageAmount * this.amount / 100f);
            this.owner.heal(heal);
        }
        super.onAttack(info, damageAmount, target);
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

}
