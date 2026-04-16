package cn.onebound.sdk;

import cn.onebound.sdk.core.config.ClientConfig;
import cn.onebound.sdk.domestic.taobao.api.*;
import cn.onebound.sdk.domestic.ali1688.api.*;
import cn.onebound.sdk.domestic.jd.api.*;
import cn.onebound.sdk.domestic.pinduoduo.api.*;
import cn.onebound.sdk.domestic.micro.api.*;
import cn.onebound.sdk.overseas.shopee.api.*;
import cn.onebound.sdk.overseas.aliexpress.api.*;
import cn.onebound.sdk.overseas.temu.api.*;
import cn.onebound.sdk.overseas.tglobal.api.*;
import cn.onebound.sdk.overseas.lazada.api.*;
import cn.onebound.sdk.overseas.amazon.api.*;
import cn.onebound.sdk.overseas.ebay.api.*;
import cn.onebound.sdk.overseas.kgcg.api.*;
import cn.onebound.sdk.overseas.alibaba.api.*;
import cn.onebound.sdk.overseas.walgreens.api.*;
import cn.onebound.sdk.overseas.mic.api.*;
import cn.onebound.sdk.overseas.shop.api.*;
import cn.onebound.sdk.overseas.target.api.*;
import cn.onebound.sdk.overseas.wildberries.api.*;
import cn.onebound.sdk.overseas.daraz.api.*;
import cn.onebound.sdk.overseas.mercado.api.*;
import cn.onebound.sdk.overseas.dmm.api.*;
import cn.onebound.sdk.overseas.walmart.api.*;
import cn.onebound.sdk.overseas.jumia.api.*;
import cn.onebound.sdk.overseas.ozon.api.*;
import cn.onebound.sdk.hotel_travel.xiecheng.api.*;
import cn.onebound.sdk.hotel_travel.damai.api.*;
import cn.onebound.sdk.hotel_travel.ftx.api.*;
import cn.onebound.sdk.hotel_travel.youxia.api.*;
import cn.onebound.sdk.hotel_travel.huazhu.api.*;
import cn.onebound.sdk.hotel_travel.dongcheng.api.*;
import cn.onebound.sdk.hotel_travel.xinlimei.api.*;
import cn.onebound.sdk.hotel_travel.klook.api.*;
import cn.onebound.sdk.vertical.vip.api.*;
import cn.onebound.sdk.vertical.mogujie.api.*;
import cn.onebound.sdk.vertical.vancl.api.*;
import cn.onebound.sdk.vertical.alimama.api.*;
import cn.onebound.sdk.vertical.jumei.api.*;
import cn.onebound.sdk.vertical.hc360.api.*;
import cn.onebound.sdk.vertical.huobutou.api.*;
import cn.onebound.sdk.vertical.k3.api.*;
import cn.onebound.sdk.vertical.souhaohuo.api.*;
import cn.onebound.sdk.vertical.taobaoke.api.*;
import cn.onebound.sdk.vertical.kaola.api.*;
import cn.onebound.sdk.vertical.csg.api.*;
import cn.onebound.sdk.vertical.yhd.api.*;
import cn.onebound.sdk.vertical.ymatou.api.*;
import cn.onebound.sdk.vertical.smzdm.api.*;
import cn.onebound.sdk.vertical.yqzwd.api.*;
import cn.onebound.sdk.vertical.ylw.api.*;
import cn.onebound.sdk.vertical.fzw.api.*;
import cn.onebound.sdk.vertical.yiwugo.api.*;
import cn.onebound.sdk.vertical.vvic.api.*;
import cn.onebound.sdk.vertical.suning.api.*;
import cn.onebound.sdk.vertical.dewu.api.*;
import cn.onebound.sdk.vertical.xmz.api.*;
import cn.onebound.sdk.vertical.shihuo.api.*;
import cn.onebound.sdk.vertical.qx.api.*;
import cn.onebound.sdk.vertical.deli.api.*;
import cn.onebound.sdk.vertical.colipu.api.*;
import cn.onebound.sdk.hardware_industry.vipmro.api.*;
import cn.onebound.sdk.hardware_industry.jiancaiwang.api.*;
import cn.onebound.sdk.hardware_industry.huagw.api.*;
import cn.onebound.sdk.hardware_industry.ickey.api.*;
import cn.onebound.sdk.hardware_industry.zkw.api.*;
import cn.onebound.sdk.hardware_industry.china.api.*;
import cn.onebound.sdk.hardware_industry.zggk.api.*;
import cn.onebound.sdk.hardware_industry.schneider.api.*;
import cn.onebound.sdk.hardware_industry.dp123.api.*;
import cn.onebound.sdk.hardware_industry.misumi.api.*;
import cn.onebound.sdk.news_info.jmweb.api.*;
import cn.onebound.sdk.news_info.pbweb.api.*;
import cn.onebound.sdk.news_info.txxw.api.*;
import cn.onebound.sdk.news_info.zhw.api.*;
import cn.onebound.sdk.news_info.xjb.api.*;
import cn.onebound.sdk.social_media.weixin.api.*;
import cn.onebound.sdk.social_media.smallredbook.api.*;
import cn.onebound.sdk.social_media.hksp.api.*;
import cn.onebound.sdk.social_media.shortvideo.api.*;
import cn.onebound.sdk.social_media.ks.api.*;
import cn.onebound.sdk.social_media.bili.api.*;
import cn.onebound.sdk.social_media.tiktok.api.*;
import cn.onebound.sdk.social_media.youtube.api.*;
import cn.onebound.sdk.second_hand.ahs.api.*;
import cn.onebound.sdk.second_hand.goodfish.api.*;
import cn.onebound.sdk.second_hand.wuziw.api.*;
import cn.onebound.sdk.second_hand.zhuanzhuan.api.*;
import cn.onebound.sdk.enterprise.sqw.api.*;
import cn.onebound.sdk.enterprise.tyc.api.*;
import cn.onebound.sdk.enterprise.aqc.api.*;
import cn.onebound.sdk.online_bookstore.dangdang.api.*;
import cn.onebound.sdk.online_bookstore.kfz.api.*;
import cn.onebound.sdk.online_bookstore.cnbook.api.*;
import cn.onebound.sdk.bidding_info.jxsggzy.api.*;
import cn.onebound.sdk.bidding_info.zblh.api.*;
import cn.onebound.sdk.bidding_info.cgyzb.api.*;
import cn.onebound.sdk.bidding_info.bbw.api.*;
import cn.onebound.sdk.bidding_info.hbw.api.*;
import cn.onebound.sdk.bidding_info.czw.api.*;
import cn.onebound.sdk.bidding_info.zxw.api.*;
import cn.onebound.sdk.merchant_data.yhby.api.*;
import cn.onebound.sdk.merchant_data.yidaba.api.*;
import cn.onebound.sdk.merchant_data.ebdoor.api.*;
import cn.onebound.sdk.merchant_data.mkbl.api.*;
import cn.onebound.sdk.merchant_data.zqw.api.*;
import cn.onebound.sdk.merchant_data.swz007.api.*;
import cn.onebound.sdk.merchant_data.sole.api.*;
import cn.onebound.sdk.merchant_data.zhaosw.api.*;
import cn.onebound.sdk.merchant_data.b2b.api.*;
import cn.onebound.sdk.merchant_data.jmw.api.*;
import cn.onebound.sdk.merchant_data.jingcheng.api.*;

public class OneBoundClient {

    private final ClientConfig config;
    private final String platform;

    private TaobaoItemApi taobaoItemApi;
    private TaobaoSearchApi taobaoSearchApi;
    private TaobaoSellerApi taobaoSellerApi;
    private TaobaoReviewApi taobaoReviewApi;
    private TaobaoBuyerApi taobaoBuyerApi;
    private TaobaoSellerOpApi taobaoSellerOpApi;
    private TaobaoOtherApi taobaoOtherApi;

    private Ali1688ItemApi ali1688ItemApi;
    private Ali1688SearchApi ali1688SearchApi;
    private Ali1688SellerApi ali1688SellerApi;
    private Ali1688ReviewApi ali1688ReviewApi;
    private Ali1688OtherApi ali1688OtherApi;

    private JdItemApi jdItemApi;
    private JdSearchApi jdSearchApi;
    private JdOtherApi jdOtherApi;

    private PddItemApi pddItemApi;
    private PddSearchApi pddSearchApi;

    private MicroItemApi microItemApi;

    private ShopeeItemApi shopeeItemApi;

    private AliexpressItemApi aliexpressItemApi;

    private TemuItemApi temuItemApi;

    private TglobalItemApi tglobalItemApi;

    private LazadaItemApi lazadaItemApi;

    private AmazonItemApi amazonItemApi;

    private EbayItemApi ebayItemApi;

    private KgcgItemApi kgcgItemApi;

    private AlibabaItemApi alibabaItemApi;

    private WalgreensItemApi walgreensItemApi;

    private MicItemApi micItemApi;

    private ShopItemApi shopItemApi;

    private TargetItemApi targetItemApi;

    private WildberriesItemApi wildberriesItemApi;

    private DarazItemApi darazItemApi;

    private MercadoItemApi mercadoItemApi;

    private DmmItemApi dmmItemApi;

    private WalmartItemApi walmartItemApi;

    private JumiaItemApi jumiaItemApi;

    private OzonItemApi ozonItemApi;

    private XiechengItemApi xiechengItemApi;

    private DamaiItemApi damaiItemApi;

    private FtxItemApi ftxItemApi;

    private YouxiaItemApi youxiaItemApi;

    private HuazhuItemApi huazhuItemApi;

    private DongchengItemApi dongchengItemApi;

    private XinlimeiItemApi xinlimeiItemApi;

    private KlookItemApi klookItemApi;

    private VipItemApi vipItemApi;

    private MogujieItemApi mogujieItemApi;

    private VanclItemApi vanclItemApi;

    private AlimamaItemApi alimamaItemApi;

    private JumeiItemApi jumeiItemApi;

    private Hc360ItemApi hc360ItemApi;

    private HuobutouItemApi huobutouItemApi;

    private K3ItemApi k3ItemApi;

    private SouhaohuoItemApi souhaohuoItemApi;

    private TaobaokeItemApi taobaokeItemApi;

    private KaolaItemApi kaolaItemApi;

    private CsgItemApi csgItemApi;

    private YhdItemApi yhdItemApi;

    private YmatouItemApi ymatouItemApi;

    private SmzdmItemApi smzdmItemApi;

    private YqzwdItemApi yqzwdItemApi;

    private YlwItemApi ylwItemApi;

    private FzwItemApi fzwItemApi;

    private YiwugoItemApi yiwugoItemApi;

    private VvicItemApi vvicItemApi;

    private SuningItemApi suningItemApi;

    private DewuItemApi dewuItemApi;

    private XmzItemApi xmzItemApi;

    private ShihuoItemApi shihuoItemApi;

    private QxItemApi qxItemApi;

    private DeliItemApi deliItemApi;

    private ColipuItemApi colipuItemApi;

    private VipmroItemApi vipmroItemApi;
    private JiancaiwangItemApi jiancaiwangItemApi;
    private HuagwItemApi huagwItemApi;
    private IkeyItemApi ikeyItemApi;
    private ZkwItemApi zkwItemApi;
    private ChinaItemApi chinaItemApi;
    private ZggkItemApi zggkItemApi;
    private SchneiderItemApi schneiderItemApi;
    private Dp123ItemApi dp123ItemApi;
    private MisumiItemApi misumiItemApi;

    private JmwebItemApi jmwebItemApi;
    private PbwebItemApi pbwebItemApi;
    private TxxwItemApi txxwItemApi;
    private ZhwItemApi zhwItemApi;
    private XjbItemApi xjbItemApi;

    private WeixinItemApi weixinItemApi;
    private SmallredbookItemApi smallredbookItemApi;
    private HkspItemApi hkspItemApi;
    private ShortvideoItemApi shortvideoItemApi;
    private KsItemApi ksItemApi;
    private BiliItemApi biliItemApi;
    private TiktokItemApi tiktokItemApi;
    private YoutubeItemApi youtubeItemApi;

    private AhsItemApi ahsItemApi;
    private GoodfishItemApi goodfishItemApi;
    private WuziwItemApi wuziwItemApi;
    private ZhuanzhuanItemApi zhuanzhuanItemApi;

    private SqwItemApi sqwItemApi;
    private TycItemApi tycItemApi;
    private AqcItemApi aqcItemApi;

    private DangdangItemApi dangdangItemApi;
    private KfzItemApi kfzItemApi;
    private CnbookItemApi cnbookItemApi;

    private JxsggzyItemApi jxsggzyItemApi;
    private ZblhItemApi zblhItemApi;
    private CgyzbItemApi cgyzbItemApi;
    private BbwItemApi bbwItemApi;
    private HbwItemApi hbwItemApi;
    private CzwItemApi czwItemApi;
    private ZxwItemApi zxwItemApi;

    private YhbyItemApi yhbyItemApi;
    private YidabaItemApi yidabaItemApi;
    private EbdoorItemApi ebdoorItemApi;
    private MkblItemApi mkblItemApi;
    private ZqwItemApi zqwItemApi;
    private Swz007ItemApi swz007ItemApi;
    private SoleItemApi soleItemApi;
    private ZhaoswItemApi zhaoswItemApi;
    private B2bItemApi b2bItemApi;
    private JmwItemApi jmwItemApi;
    private JingchengItemApi jingchengItemApi;

    public OneBoundClient(String platform, String key, String secret) {
        this.platform = platform.toLowerCase();
        this.config = new ClientConfig(key, secret);
        initApis();
    }

    public OneBoundClient(String platform, String key, String secret, String apiUrl) {
        this.platform = platform.toLowerCase();
        this.config = new ClientConfig(key, secret, apiUrl);
        initApis();
    }

    private void initApis() {
        switch (platform) {
            case "taobao":
                taobaoItemApi = new TaobaoItemApi(config);
                taobaoSearchApi = new TaobaoSearchApi(config);
                taobaoSellerApi = new TaobaoSellerApi(config);
                taobaoReviewApi = new TaobaoReviewApi(config);
                taobaoBuyerApi = new TaobaoBuyerApi(config);
                taobaoSellerOpApi = new TaobaoSellerOpApi(config);
                taobaoOtherApi = new TaobaoOtherApi(config);
                break;
            case "1688":
                ali1688ItemApi = new Ali1688ItemApi(config);
                ali1688SearchApi = new Ali1688SearchApi(config);
                ali1688SellerApi = new Ali1688SellerApi(config);
                ali1688ReviewApi = new Ali1688ReviewApi(config);
                ali1688OtherApi = new Ali1688OtherApi(config);
                break;
            case "jd":
                jdItemApi = new JdItemApi(config);
                jdSearchApi = new JdSearchApi(config);
                jdOtherApi = new JdOtherApi(config);
                break;
            case "pinduoduo":
                pddItemApi = new PddItemApi(config);
                pddSearchApi = new PddSearchApi(config);
                break;
            case "micro":
                microItemApi = new MicroItemApi(config);
                break;
            case "shopee":
                shopeeItemApi = new ShopeeItemApi(config);
                break;
            case "aliexpress":
                aliexpressItemApi = new AliexpressItemApi(config);
                break;
            case "temu":
                temuItemApi = new TemuItemApi(config);
                break;
            case "tglobal":
                tglobalItemApi = new TglobalItemApi(config);
                break;
            case "lazada":
                lazadaItemApi = new LazadaItemApi(config);
                break;
            case "amazon":
                amazonItemApi = new AmazonItemApi(config);
                break;
            case "ebay":
                ebayItemApi = new EbayItemApi(config);
                break;
            case "kgcg":
                kgcgItemApi = new KgcgItemApi(config);
                break;
            case "alibaba":
                alibabaItemApi = new AlibabaItemApi(config);
                break;
            case "walgreens":
                walgreensItemApi = new WalgreensItemApi(config);
                break;
            case "mic":
                micItemApi = new MicItemApi(config);
                break;
            case "shop":
                shopItemApi = new ShopItemApi(config);
                break;
            case "target":
                targetItemApi = new TargetItemApi(config);
                break;
            case "wildberries":
                wildberriesItemApi = new WildberriesItemApi(config);
                break;
            case "daraz":
                darazItemApi = new DarazItemApi(config);
                break;
            case "mercado":
                mercadoItemApi = new MercadoItemApi(config);
                break;
            case "dmm":
                dmmItemApi = new DmmItemApi(config);
                break;
            case "walmart":
                walmartItemApi = new WalmartItemApi(config);
                break;
            case "jumia":
                jumiaItemApi = new JumiaItemApi(config);
                break;
            case "ozon":
                ozonItemApi = new OzonItemApi(config);
                break;
            case "xiecheng":
                xiechengItemApi = new XiechengItemApi(config);
                break;
            case "damai":
                damaiItemApi = new DamaiItemApi(config);
                break;
            case "ftx":
                ftxItemApi = new FtxItemApi(config);
                break;
            case "youxia":
                youxiaItemApi = new YouxiaItemApi(config);
                break;
            case "huazhu":
                huazhuItemApi = new HuazhuItemApi(config);
                break;
            case "dongcheng":
                dongchengItemApi = new DongchengItemApi(config);
                break;
            case "xinlimei":
                xinlimeiItemApi = new XinlimeiItemApi(config);
                break;
            case "klook":
                klookItemApi = new KlookItemApi(config);
                break;
            case "vip":
                vipItemApi = new VipItemApi(config);
                break;
            case "mogujie":
                mogujieItemApi = new MogujieItemApi(config);
                break;
            case "vancl":
                vanclItemApi = new VanclItemApi(config);
                break;
            case "alimama":
                alimamaItemApi = new AlimamaItemApi(config);
                break;
            case "jumei":
                jumeiItemApi = new JumeiItemApi(config);
                break;
            case "hc360":
                hc360ItemApi = new Hc360ItemApi(config);
                break;
            case "huobutou":
                huobutouItemApi = new HuobutouItemApi(config);
                break;
            case "k3":
                k3ItemApi = new K3ItemApi(config);
                break;
            case "souhaohuo":
                souhaohuoItemApi = new SouhaohuoItemApi(config);
                break;
            case "taobaoke":
                taobaokeItemApi = new TaobaokeItemApi(config);
                break;
            case "kaola":
                kaolaItemApi = new KaolaItemApi(config);
                break;
            case "csg":
                csgItemApi = new CsgItemApi(config);
                break;
            case "yhd":
                yhdItemApi = new YhdItemApi(config);
                break;
            case "ymatou":
                ymatouItemApi = new YmatouItemApi(config);
                break;
            case "smzdm":
                smzdmItemApi = new SmzdmItemApi(config);
                break;
            case "yqzwd":
                yqzwdItemApi = new YqzwdItemApi(config);
                break;
            case "ylw":
                ylwItemApi = new YlwItemApi(config);
                break;
            case "fzw":
                fzwItemApi = new FzwItemApi(config);
                break;
            case "yiwugo":
                yiwugoItemApi = new YiwugoItemApi(config);
                break;
            case "vvic":
                vvicItemApi = new VvicItemApi(config);
                break;
            case "suning":
                suningItemApi = new SuningItemApi(config);
                break;
            case "dewu":
                dewuItemApi = new DewuItemApi(config);
                break;
            case "xmz":
                xmzItemApi = new XmzItemApi(config);
                break;
            case "shihuo":
                shihuoItemApi = new ShihuoItemApi(config);
                break;
            case "qx":
                qxItemApi = new QxItemApi(config);
                break;
            case "deli":
                deliItemApi = new DeliItemApi(config);
                break;
            case "colipu":
                colipuItemApi = new ColipuItemApi(config);
                break;
            case "vipmro":
                vipmroItemApi = new VipmroItemApi(config);
                break;
            case "jiancaiwang":
                jiancaiwangItemApi = new JiancaiwangItemApi(config);
                break;
            case "huagw":
                huagwItemApi = new HuagwItemApi(config);
                break;
            case "ickey":
                ikeyItemApi = new IkeyItemApi(config);
                break;
            case "zkw":
                zkwItemApi = new ZkwItemApi(config);
                break;
            case "china":
                chinaItemApi = new ChinaItemApi(config);
                break;
            case "zggk":
                zggkItemApi = new ZggkItemApi(config);
                break;
            case "schneider":
                schneiderItemApi = new SchneiderItemApi(config);
                break;
            case "dp123":
                dp123ItemApi = new Dp123ItemApi(config);
                break;
            case "misumi":
                misumiItemApi = new MisumiItemApi(config);
                break;
            case "jmweb":
                jmwebItemApi = new JmwebItemApi(config);
                break;
            case "pbweb":
                pbwebItemApi = new PbwebItemApi(config);
                break;
            case "txxw":
                txxwItemApi = new TxxwItemApi(config);
                break;
            case "zhw":
                zhwItemApi = new ZhwItemApi(config);
                break;
            case "xjb":
                xjbItemApi = new XjbItemApi(config);
                break;
            case "weixin":
                weixinItemApi = new WeixinItemApi(config);
                break;
            case "smallredbook":
                smallredbookItemApi = new SmallredbookItemApi(config);
                break;
            case "hksp":
                hkspItemApi = new HkspItemApi(config);
                break;
            case "shortvideo":
                shortvideoItemApi = new ShortvideoItemApi(config);
                break;
            case "ks":
                ksItemApi = new KsItemApi(config);
                break;
            case "bili":
                biliItemApi = new BiliItemApi(config);
                break;
            case "tiktok":
                tiktokItemApi = new TiktokItemApi(config);
                break;
            case "youtube":
                youtubeItemApi = new YoutubeItemApi(config);
                break;
            case "ahs":
                ahsItemApi = new AhsItemApi(config);
                break;
            case "goodfish":
                goodfishItemApi = new GoodfishItemApi(config);
                break;
            case "wuziw":
                wuziwItemApi = new WuziwItemApi(config);
                break;
            case "zhuanzhuan":
                zhuanzhuanItemApi = new ZhuanzhuanItemApi(config);
                break;
            case "sqw":
                sqwItemApi = new SqwItemApi(config);
                break;
            case "tyc":
                tycItemApi = new TycItemApi(config);
                break;
            case "aqc":
                aqcItemApi = new AqcItemApi(config);
                break;
            case "dangdang":
                dangdangItemApi = new DangdangItemApi(config);
                break;
            case "kfz":
                kfzItemApi = new KfzItemApi(config);
                break;
            case "cnbook":
                cnbookItemApi = new CnbookItemApi(config);
                break;
            case "jxsggzy":
                jxsggzyItemApi = new JxsggzyItemApi(config);
                break;
            case "zblh":
                zblhItemApi = new ZblhItemApi(config);
                break;
            case "cgyzb":
                cgyzbItemApi = new CgyzbItemApi(config);
                break;
            case "bbw":
                bbwItemApi = new BbwItemApi(config);
                break;
            case "hbw":
                hbwItemApi = new HbwItemApi(config);
                break;
            case "czw":
                czwItemApi = new CzwItemApi(config);
                break;
            case "zxw":
                zxwItemApi = new ZxwItemApi(config);
                break;
            case "yhby":
                yhbyItemApi = new YhbyItemApi(config);
                break;
            case "yidaba":
                yidabaItemApi = new YidabaItemApi(config);
                break;
            case "ebdoor":
                ebdoorItemApi = new EbdoorItemApi(config);
                break;
            case "mkbl":
                mkblItemApi = new MkblItemApi(config);
                break;
            case "zqw":
                zqwItemApi = new ZqwItemApi(config);
                break;
            case "007swz":
                swz007ItemApi = new Swz007ItemApi(config);
                break;
            case "sole":
                soleItemApi = new SoleItemApi(config);
                break;
            case "zhaosw":
                zhaoswItemApi = new ZhaoswItemApi(config);
                break;
            case "b2b":
                b2bItemApi = new B2bItemApi(config);
                break;
            case "jmw":
                jmwItemApi = new JmwItemApi(config);
                break;
            case "jingcheng":
                jingchengItemApi = new JingchengItemApi(config);
                break;
            default:
                throw new IllegalArgumentException("Unsupported platform: " + platform);
        }
    }

    public ClientConfig getConfig() {
        return config;
    }

    public String getPlatform() {
        return platform;
    }

    public TaobaoItemApi getTaobaoItemApi() {
        return taobaoItemApi;
    }

    public TaobaoSearchApi getTaobaoSearchApi() {
        return taobaoSearchApi;
    }

    public TaobaoSellerApi getTaobaoSellerApi() {
        return taobaoSellerApi;
    }

    public TaobaoReviewApi getTaobaoReviewApi() {
        return taobaoReviewApi;
    }

    public TaobaoBuyerApi getTaobaoBuyerApi() {
        return taobaoBuyerApi;
    }

    public TaobaoSellerOpApi getTaobaoSellerOpApi() {
        return taobaoSellerOpApi;
    }

    public TaobaoOtherApi getTaobaoOtherApi() {
        return taobaoOtherApi;
    }

    public Ali1688ItemApi getAli1688ItemApi() {
        return ali1688ItemApi;
    }

    public Ali1688SearchApi getAli1688SearchApi() {
        return ali1688SearchApi;
    }

    public Ali1688SellerApi getAli1688SellerApi() {
        return ali1688SellerApi;
    }

    public Ali1688ReviewApi getAli1688ReviewApi() {
        return ali1688ReviewApi;
    }

    public Ali1688OtherApi getAli1688OtherApi() {
        return ali1688OtherApi;
    }

    public JdItemApi getJdItemApi() {
        return jdItemApi;
    }

    public JdSearchApi getJdSearchApi() {
        return jdSearchApi;
    }

    public JdOtherApi getJdOtherApi() {
        return jdOtherApi;
    }

    public PddItemApi getPddItemApi() {
        return pddItemApi;
    }

    public PddSearchApi getPddSearchApi() {
        return pddSearchApi;
    }

    public MicroItemApi getMicroItemApi() {
        return microItemApi;
    }

    public ShopeeItemApi getShopeeItemApi() {
        return shopeeItemApi;
    }

    public AliexpressItemApi getAliexpressItemApi() {
        return aliexpressItemApi;
    }

    public TemuItemApi getTemuItemApi() {
        return temuItemApi;
    }

    public TglobalItemApi getTglobalItemApi() {
        return tglobalItemApi;
    }

    public LazadaItemApi getLazadaItemApi() {
        return lazadaItemApi;
    }

    public AmazonItemApi getAmazonItemApi() {
        return amazonItemApi;
    }

    public EbayItemApi getEbayItemApi() {
        return ebayItemApi;
    }

    public KgcgItemApi getKgcgItemApi() {
        return kgcgItemApi;
    }

    public AlibabaItemApi getAlibabaItemApi() {
        return alibabaItemApi;
    }

    public WalgreensItemApi getWalgreensItemApi() {
        return walgreensItemApi;
    }

    public MicItemApi getMicItemApi() {
        return micItemApi;
    }

    public ShopItemApi getShopItemApi() {
        return shopItemApi;
    }

    public TargetItemApi getTargetItemApi() {
        return targetItemApi;
    }

    public WildberriesItemApi getWildberriesItemApi() {
        return wildberriesItemApi;
    }

    public DarazItemApi getDarazItemApi() {
        return darazItemApi;
    }

    public MercadoItemApi getMercadoItemApi() {
        return mercadoItemApi;
    }

    public DmmItemApi getDmmItemApi() {
        return dmmItemApi;
    }

    public WalmartItemApi getWalmartItemApi() {
        return walmartItemApi;
    }

    public JumiaItemApi getJumiaItemApi() {
        return jumiaItemApi;
    }

    public OzonItemApi getOzonItemApi() {
        return ozonItemApi;
    }

    public XiechengItemApi getXiechengItemApi() {
        return xiechengItemApi;
    }

    public DamaiItemApi getDamaiItemApi() {
        return damaiItemApi;
    }

    public FtxItemApi getFtxItemApi() {
        return ftxItemApi;
    }

    public YouxiaItemApi getYouxiaItemApi() {
        return youxiaItemApi;
    }

    public HuazhuItemApi getHuazhuItemApi() {
        return huazhuItemApi;
    }

    public DongchengItemApi getDongchengItemApi() {
        return dongchengItemApi;
    }

    public XinlimeiItemApi getXinlimeiItemApi() {
        return xinlimeiItemApi;
    }

    public KlookItemApi getKlookItemApi() {
        return klookItemApi;
    }

    public VipItemApi getVipItemApi() {
        return vipItemApi;
    }

    public MogujieItemApi getMogujieItemApi() {
        return mogujieItemApi;
    }

    public VanclItemApi getVanclItemApi() {
        return vanclItemApi;
    }

    public AlimamaItemApi getAlimamaItemApi() {
        return alimamaItemApi;
    }

    public JumeiItemApi getJumeiItemApi() {
        return jumeiItemApi;
    }

    public Hc360ItemApi getHc360ItemApi() {
        return hc360ItemApi;
    }

    public HuobutouItemApi getHuobutouItemApi() {
        return huobutouItemApi;
    }

    public K3ItemApi getK3ItemApi() {
        return k3ItemApi;
    }

    public SouhaohuoItemApi getSouhaohuoItemApi() {
        return souhaohuoItemApi;
    }

    public TaobaokeItemApi getTaobaokeItemApi() {
        return taobaokeItemApi;
    }

    public KaolaItemApi getKaolaItemApi() {
        return kaolaItemApi;
    }

    public CsgItemApi getCsgItemApi() {
        return csgItemApi;
    }

    public YhdItemApi getYhdItemApi() {
        return yhdItemApi;
    }

    public YmatouItemApi getYmatouItemApi() {
        return ymatouItemApi;
    }

    public SmzdmItemApi getSmzdmItemApi() {
        return smzdmItemApi;
    }

    public YqzwdItemApi getYqzwdItemApi() {
        return yqzwdItemApi;
    }

    public YlwItemApi getYlwItemApi() {
        return ylwItemApi;
    }

    public FzwItemApi getFzwItemApi() {
        return fzwItemApi;
    }

    public YiwugoItemApi getYiwugoItemApi() {
        return yiwugoItemApi;
    }

    public VvicItemApi getVvicItemApi() {
        return vvicItemApi;
    }

    public SuningItemApi getSuningItemApi() {
        return suningItemApi;
    }

    public DewuItemApi getDewuItemApi() {
        return dewuItemApi;
    }

    public XmzItemApi getXmzItemApi() {
        return xmzItemApi;
    }

    public ShihuoItemApi getShihuoItemApi() {
        return shihuoItemApi;
    }

    public QxItemApi getQxItemApi() {
        return qxItemApi;
    }

    public DeliItemApi getDeliItemApi() {
        return deliItemApi;
    }

    public ColipuItemApi getColipuItemApi() {
        return colipuItemApi;
    }

    public VipmroItemApi getVipmroItemApi() {
        return vipmroItemApi;
    }

    public JiancaiwangItemApi getJiancaiwangItemApi() {
        return jiancaiwangItemApi;
    }

    public HuagwItemApi getHuagwItemApi() {
        return huagwItemApi;
    }

    public IkeyItemApi getIkeyItemApi() {
        return ikeyItemApi;
    }

    public ZkwItemApi getZkwItemApi() {
        return zkwItemApi;
    }

    public ChinaItemApi getChinaItemApi() {
        return chinaItemApi;
    }

    public ZggkItemApi getZggkItemApi() {
        return zggkItemApi;
    }

    public SchneiderItemApi getSchneiderItemApi() {
        return schneiderItemApi;
    }

    public Dp123ItemApi getDp123ItemApi() {
        return dp123ItemApi;
    }

    public MisumiItemApi getMisumiItemApi() {
        return misumiItemApi;
    }

    public JmwebItemApi getJmwebItemApi() {
        return jmwebItemApi;
    }

    public PbwebItemApi getPbwebItemApi() {
        return pbwebItemApi;
    }

    public TxxwItemApi getTxxwItemApi() {
        return txxwItemApi;
    }

    public ZhwItemApi getZhwItemApi() {
        return zhwItemApi;
    }

    public XjbItemApi getXjbItemApi() {
        return xjbItemApi;
    }

    public WeixinItemApi getWeixinItemApi() {
        return weixinItemApi;
    }

    public SmallredbookItemApi getSmallredbookItemApi() {
        return smallredbookItemApi;
    }

    public HkspItemApi getHkspItemApi() {
        return hkspItemApi;
    }

    public ShortvideoItemApi getShortvideoItemApi() {
        return shortvideoItemApi;
    }

    public KsItemApi getKsItemApi() {
        return ksItemApi;
    }

    public BiliItemApi getBiliItemApi() {
        return biliItemApi;
    }

    public TiktokItemApi getTiktokItemApi() {
        return tiktokItemApi;
    }

    public YoutubeItemApi getYoutubeItemApi() {
        return youtubeItemApi;
    }

    public AhsItemApi getAhsItemApi() {
        return ahsItemApi;
    }

    public GoodfishItemApi getGoodfishItemApi() {
        return goodfishItemApi;
    }

    public WuziwItemApi getWuziwItemApi() {
        return wuziwItemApi;
    }

    public ZhuanzhuanItemApi getZhuanzhuanItemApi() {
        return zhuanzhuanItemApi;
    }

    public SqwItemApi getSqwItemApi() {
        return sqwItemApi;
    }

    public TycItemApi getTycItemApi() {
        return tycItemApi;
    }

    public AqcItemApi getAqcItemApi() {
        return aqcItemApi;
    }

    public DangdangItemApi getDangdangItemApi() {
        return dangdangItemApi;
    }

    public KfzItemApi getKfzItemApi() {
        return kfzItemApi;
    }

    public CnbookItemApi getCnbookItemApi() {
        return cnbookItemApi;
    }

    public JxsggzyItemApi getJxsggzyItemApi() {
        return jxsggzyItemApi;
    }

    public ZblhItemApi getZblhItemApi() {
        return zblhItemApi;
    }

    public CgyzbItemApi getCgyzbItemApi() {
        return cgyzbItemApi;
    }

    public BbwItemApi getBbwItemApi() {
        return bbwItemApi;
    }

    public HbwItemApi getHbwItemApi() {
        return hbwItemApi;
    }

    public CzwItemApi getCzwItemApi() {
        return czwItemApi;
    }

    public ZxwItemApi getZxwItemApi() {
        return zxwItemApi;
    }

    public YhbyItemApi getYhbyItemApi() {
        return yhbyItemApi;
    }

    public YidabaItemApi getYidabaItemApi() {
        return yidabaItemApi;
    }

    public EbdoorItemApi getEbdoorItemApi() {
        return ebdoorItemApi;
    }

    public MkblItemApi getMkblItemApi() {
        return mkblItemApi;
    }

    public ZqwItemApi getZqwItemApi() {
        return zqwItemApi;
    }

    public Swz007ItemApi getSwz007ItemApi() {
        return swz007ItemApi;
    }

    public SoleItemApi getSoleItemApi() {
        return soleItemApi;
    }

    public ZhaoswItemApi getZhaoswItemApi() {
        return zhaoswItemApi;
    }

    public B2bItemApi getB2bItemApi() {
        return b2bItemApi;
    }

    public JmwItemApi getJmwItemApi() {
        return jmwItemApi;
    }

    public JingchengItemApi getJingchengItemApi() {
        return jingchengItemApi;
    }
}
