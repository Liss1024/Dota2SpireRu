package dota2Spire.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import dota2Spire.Dota2Spire;
import dota2Spire.powers.HpRecovery;
import dota2Spire.powers.MpRecovery;
import dota2Spire.util.StringUtil;
import dota2Spire.util.TextureLoader;

import static dota2Spire.Dota2Spire.makeRelicPath;

/**
 * 骨灰
 * 击杀敌人时获得1血量回复和1能量回复
 */
public class UrnOfShadows extends CustomRelic {
    // ID, images, text.
    public static final String ID = Dota2Spire.makeID("UrnOfShadows");
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("UrnOfShadows.png"));
    private static final int _HpRecovery = 1;
    private static final int _MpRecovery = 1;

    public UrnOfShadows() {
        super(ID, IMG, RelicTier.COMMON, LandingSound.MAGICAL);
    }

    @Override
    public void onMonsterDeath(AbstractMonster m) {
        if (!m.hasPower("Minion")) {
            flash();
            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                    new HpRecovery(AbstractDungeon.player, _HpRecovery)));
            addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                    new MpRecovery(AbstractDungeon.player, _MpRecovery)));
        }
    }

    @Override
    public String getUpdatedDescription() {
        return StringUtil.format(DESCRIPTIONS[0], _HpRecovery, _MpRecovery);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new UrnOfShadows();
    }
}
