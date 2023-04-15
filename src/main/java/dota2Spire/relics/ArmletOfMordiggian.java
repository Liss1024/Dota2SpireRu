package dota2Spire.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import dota2Spire.Dota2Spire;
import dota2Spire.util.StringUtil;
import dota2Spire.util.TextureLoader;

import static dota2Spire.Dota2Spire.makeRelicPath;

/**
 * 臂章
 * 战斗开始时获得2点力量，每回合开始时受到2点伤害
 */
public class ArmletOfMordiggian extends CustomRelic {
    // ID, images, text.
    public static final String ID = Dota2Spire.makeID("ArmletOfMordiggian");
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("ArmletOfMordiggian.png"));

    private static final int _Strength = 2;
    private static final int _HPLose = 2;

    public ArmletOfMordiggian() {
        super(ID, IMG, RelicTier.COMMON, LandingSound.MAGICAL);
    }

    @Override
    public void atBattleStart() {
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, _Strength), _Strength));
    }

    @Override
    public void atTurnStart() {
        flash();
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        AbstractDungeon.player.damage(new DamageInfo(AbstractDungeon.player, _HPLose, DamageInfo.DamageType.HP_LOSS));
    }

    @Override
    public String getUpdatedDescription() {
        return StringUtil.format(DESCRIPTIONS[0], _Strength, _HPLose);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new ArmletOfMordiggian();
    }

}
