package game.xjl.Bean;

/**
 * 项目名称：GameApplication
 * 类描述：
 * 创建人：xjl
 * 创建时间：2016/10/11 11:34
 * 修改人：Administrator
 * 修改时间：2016/10/11 11:34
 * 修改备注：
 */
public class BonusBean {
    public String getBonusName() {
        return bonusName;
    }

    public void setBonusName(String bonusName) {
        this.bonusName = bonusName;
    }

    public float getBonusRates() {
        return bonusRates;
    }

    public void setBonusRates(float bonusRates) {
        this.bonusRates = bonusRates;
    }

    //中奖率
    private float bonusRates;
    //中奖名称
    private String bonusName;


}
