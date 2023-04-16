package dota2Spire.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.ShopRoom;
import dota2Spire.Dota2Spire;
import dota2Spire.cards.DagonPower;
import dota2Spire.util.StringUtil;
import dota2Spire.util.TextureLoader;

import static dota2Spire.Dota2Spire.makeRelicPath;

/**
 * 大根
 * 战斗开始时把1张达贡之神力洗入抽牌堆
 * 进入商店自动花费25块钱升级 最高5级
 * 达贡之神力 0费打10 消耗
 */
public class Dagon extends CustomRelic{
    // ID, images, text.
    public static final String ID = Dota2Spire.makeID("Dagon");
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("Dagon.png"));
    private static final int _upgradeGold = 25;
    private static final int _MaxLevel = 5;

    public Dagon() {
        super(ID, IMG, RelicTier.COMMON, LandingSound.MAGICAL);
    }

    @Override
    public void onEquip() {
        this.counter = 1;
    }

    @Override
    public void onEnterRoom(AbstractRoom room) {
        if (room instanceof ShopRoom) {
            flash();
            if (AbstractDungeon.player.gold > _upgradeGold && this.counter < _MaxLevel) {
                AbstractDungeon.player.loseGold(_upgradeGold);
                flash();
                this.counter++;
            }
        }
    }

    @Override
    public void atBattleStartPreDraw() {
        flash();
        addToBot(new MakeTempCardInDrawPileAction(new DagonPower(this.counter), 1, true, true));
    }

    @Override
    public String getUpdatedDescription() {
        return StringUtil.format(DESCRIPTIONS[0], _upgradeGold, _MaxLevel);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new Dagon();
    }
}
