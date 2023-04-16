package dota2Spire.powers;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import dota2Spire.Dota2Spire;
import dota2Spire.util.TextureLoader;

import static dota2Spire.Dota2Spire.makePowerPath;

public class MpRecovery extends AbstractPower {
    public static final String POWER_ID = Dota2Spire.makeID("MpRecovery");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public MpRecovery(final AbstractCreature owner, final int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;
        this.img = TextureLoader.getTexture(makePowerPath("MpRecovery.png"));

        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        flash();
        this.addToBot(new GainEnergyAction(1));
        changeAmount(this.amount - 1);
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.type != DamageInfo.DamageType.HP_LOSS && damageAmount > 10) {
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
