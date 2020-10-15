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

public class HpRecovery extends AbstractPower {
    public static final String POWER_ID = Dota2Spire.makeID("HpRecovery");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public HpRecovery(final AbstractCreature owner, final int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;
        this.img = TextureLoader.getTexture(makePowerPath("HpRecovery.png"));

        updateDescription();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        changeAmount(this.amount - 1);
        flash();
        if (this.owner.currentHealth > 0) {
            this.owner.heal(3);
        }
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.type != DamageInfo.DamageType.HP_LOSS && damageAmount > 0) {
            changeAmount(0);
        }
        return super.onAttacked(info, damageAmount);
    }

    private void changeAmount(int amount) {
        if (amount <= 0) {
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
        } else {
            this.amount = amount;
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

}
