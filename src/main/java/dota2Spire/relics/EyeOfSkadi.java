package dota2Spire.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import dota2Spire.Dota2Spire;
import dota2Spire.util.StringUtil;
import dota2Spire.util.TextureLoader;

import java.util.ArrayList;
import java.util.List;

import static dota2Spire.Dota2Spire.makeRelicPath;

/**
 * 冰眼
 * 开局获得1力量1敏捷
 * 对每个敌人第一次使用攻击牌时给与2虚弱
 */
public class EyeOfSkadi extends CustomRelic {
    // ID, images, text.
    public static final String ID = Dota2Spire.makeID("EyeOfSkadi");
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("EyeOfSkadi.png"));
    private List<Integer> ids;

    private static final int _BuffStack = 2;
    private static final int _StrengthStack = 1;
    private static final int _DexStack = 1;
    private static final int _MaxHP = 5;

    public EyeOfSkadi() {
        super(ID, IMG, RelicTier.RARE, LandingSound.MAGICAL);
    }

    @Override
    public void onEquip() {
        ids = new ArrayList<>();
        AbstractDungeon.player.increaseMaxHp(_MaxHP, true);
    }

    @Override
    public void atBattleStart() {
        if (ids == null) {
            ids = new ArrayList<>();
        } else {
            ids.clear();
        }
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, _StrengthStack), _StrengthStack));
        addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DexterityPower(AbstractDungeon.player, _DexStack), _DexStack));
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
                        addToBot(new ApplyPowerAction(m, AbstractDungeon.player, new WeakPower(m, _BuffStack, false)));
                        ids.add(hash);
                    }
                }
            } else if (card.target == AbstractCard.CardTarget.ALL_ENEMY) {
                for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                    int hash = m.hashCode();
                    if (!ids.contains(hash)) {
                        flash();
                        addToBot(new RelicAboveCreatureAction(m, this));
                        addToBot(new ApplyPowerAction(m, AbstractDungeon.player, new WeakPower(m, _BuffStack, false)));
                        ids.add(hash);
                    }
                }
            }
        }
    }

    @Override
    public String getUpdatedDescription() {
        return StringUtil.format(DESCRIPTIONS[0], _MaxHP, _StrengthStack, _DexStack, _BuffStack);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new EyeOfSkadi();
    }
}
