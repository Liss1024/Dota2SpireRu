package dota2Spire.potions;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import dota2Spire.Dota2Spire;
import dota2Spire.powers.HpRecovery;

public class HealingSalve extends AbstractPotion {

    public static final String POTION_ID = Dota2Spire.makeID("HealingSalve");
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);
    public static final String NAME = potionStrings.NAME;
    public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;

    public HealingSalve() {
        super(NAME, POTION_ID, PotionRarity.COMMON, PotionSize.SPHERE, PotionColor.GREEN);

        potency = getPotency();

        description = DESCRIPTIONS[0] + potency + DESCRIPTIONS[1];

        isThrown = false;

        tips.add(new PowerTip(name, description));
    }

    public void use(AbstractCreature target) {
        target = AbstractDungeon.player;
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            this.addToBot(new ApplyPowerAction(target, AbstractDungeon.player, new HpRecovery(target, potency), potency));
        }

    }

    @Override
    public AbstractPotion makeCopy() {
        return new HealingSalve();
    }

    @Override
    public int getPotency(final int potency) {
        return 6;
    }

    public void upgradePotion() {
        potency += 3;
        tips.clear();
        tips.add(new PowerTip(name, description));
    }
}
