/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.tra.com.tsync.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author Mwagiru Kamoni
 */
@Entity
@Table(name = "ONLINE_ACTIVITY")
@NamedQueries({
    @NamedQuery(name = "OnlineActivity.findAll", query = "SELECT o FROM OnlineActivity o")})
public class OnlineActivity implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ONLINE_ACTIVITY_SEQ")
    @SequenceGenerator(sequenceName = "online_activity_seq", allocationSize = 1, name = "ONLINE_ACTIVITY_SEQ")
    @Column(name = "TRANSACTIONNUMBER")
    private BigDecimal transactionnumber;


    @Column(name = "FIELD000")
    private String field000;
    @Column(name = "FIELD001")
    private String field001;
    @Column(name = "FIELD002")
    private String field002;
    @Column(name = "FIELD003")
    private String field003;
    @Column(name = "FIELD004")
    private String field004;
    @Column(name = "FIELD005")
    private String field005;
    @Column(name = "FIELD006")
    private String field006;
    @Column(name = "FIELD007")
    private String field007;
    @Column(name = "FIELD008")
    private String field008;
    @Column(name = "FIELD009")
    private String field009;
    @Column(name = "FIELD010")
    private String field010;
    @Column(name = "FIELD011")
    private String field011;
    @Column(name = "FIELD012")
    private String field012;
    @Column(name = "FIELD013")
    private String field013;
    @Column(name = "FIELD014")
    private String field014;
    @Column(name = "FIELD015")
    private String field015;
    @Column(name = "FIELD016")
    private String field016;
    @Column(name = "FIELD017")
    private String field017;
    @Column(name = "FIELD018")
    private String field018;
    @Column(name = "FIELD019")
    private String field019;
    @Column(name = "FIELD020")
    private String field020;
    @Column(name = "FIELD021")
    private String field021;
    @Column(name = "FIELD022")
    private String field022;
    @Column(name = "FIELD023")
    private String field023;
    @Column(name = "FIELD024")
    private String field024;
    @Column(name = "FIELD025")
    private String field025;
    @Column(name = "FIELD026")
    private String field026;
    @Column(name = "FIELD027")
    private String field027;
    @Column(name = "FIELD028")
    private String field028;
    @Column(name = "FIELD029")
    private String field029;
    @Column(name = "FIELD030")
    private String field030;
    @Column(name = "FIELD031")
    private String field031;
    @Column(name = "FIELD032")
    private String field032;
    @Column(name = "FIELD033")
    private String field033;
    @Column(name = "FIELD034")
    private String field034;
    @Column(name = "FIELD035")
    private String field035;
    @Column(name = "FIELD036")
    private String field036;
    @Column(name = "FIELD037")
    private String field037;
    @Column(name = "FIELD038")
    private String field038;
    @Column(name = "FIELD039")
    private String field039;
    @Column(name = "FIELD040")
    private String field040;
    @Column(name = "FIELD041")
    private String field041;
    @Column(name = "FIELD042")
    private String field042;
    @Column(name = "FIELD043")
    private String field043;
    @Column(name = "FIELD044")
    private String field044;
    @Column(name = "FIELD045")
    private String field045;
    @Column(name = "FIELD046")
    private String field046;
    @Column(name = "FIELD047")
    private String field047;
    @Column(name = "FIELD048")
    private String field048;
    @Column(name = "FIELD049")
    private String field049;
    @Column(name = "FIELD050")
    private String field050;
    @Column(name = "FIELD051")
    private String field051;
    @Column(name = "FIELD052")
    private String field052;
    @Column(name = "FIELD053")
    private String field053;
    @Column(name = "FIELD054")
    private String field054;
    @Column(name = "FIELD055")
    private String field055;
    @Column(name = "FIELD056")
    private String field056;
    @Column(name = "FIELD057")
    private String field057;
    @Column(name = "FIELD058")
    private String field058;
    @Column(name = "FIELD059")
    private String field059;
    @Column(name = "FIELD060")
    private String field060;
    @Column(name = "FIELD061")
    private String field061;
    @Column(name = "FIELD062")
    private String field062;
    @Column(name = "FIELD063")
    private String field063;
    @Column(name = "FIELD064")
    private String field064;
    @Column(name = "FIELD065")
    private String field065;
    @Column(name = "FIELD066")
    private String field066;
    @Column(name = "FIELD067")
    private String field067;
    @Column(name = "FIELD068")
    private String field068;
    @Column(name = "FIELD069")
    private String field069;
    @Column(name = "FIELD070")
    private String field070;
    @Column(name = "FIELD071")
    private String field071;
    @Column(name = "FIELD072")
    private String field072;
    @Column(name = "FIELD073")
    private String field073;
    @Column(name = "FIELD074")
    private String field074;
    @Column(name = "FIELD075")
    private String field075;
    @Column(name = "FIELD076")
    private String field076;
    @Column(name = "FIELD077")
    private String field077;
    @Column(name = "FIELD078")
    private String field078;
    @Column(name = "FIELD079")
    private String field079;
    @Column(name = "FIELD080")
    private String field080;
    @Column(name = "FIELD081")
    private String field081;
    @Column(name = "FIELD082")
    private String field082;
    @Column(name = "FIELD083")
    private String field083;
    @Column(name = "FIELD084")
    private String field084;
    @Column(name = "FIELD085")
    private String field085;
    @Column(name = "FIELD086")
    private String field086;
    @Column(name = "FIELD087")
    private String field087;
    @Column(name = "FIELD088")
    private String field088;
    @Column(name = "FIELD089")
    private String field089;
    @Column(name = "FIELD090")
    private String field090;
    @Column(name = "FIELD091")
    private String field091;
    @Column(name = "FIELD092")
    private String field092;
    @Column(name = "FIELD093")
    private String field093;
    @Column(name = "FIELD094")
    private String field094;
    @Column(name = "FIELD095")
    private String field095;
    @Column(name = "FIELD096")
    private String field096;
    @Column(name = "FIELD097")
    private String field097;
    @Column(name = "FIELD098")
    private String field098;
    @Column(name = "FIELD099")
    private String field099;
    @Column(name = "FIELD100")
    private String field100;
    @Column(name = "FIELD101")
    private String field101;
    @Column(name = "FIELD102")
    private String field102;
    @Column(name = "FIELD103")
    private String field103;
    @Column(name = "FIELD104")
    private String field104;
    @Column(name = "FIELD105")
    private String field105;
    @Column(name = "FIELD106")
    private String field106;
    @Column(name = "FIELD107")
    private String field107;
    @Column(name = "FIELD108")
    private String field108;
    @Column(name = "FIELD109")
    private String field109;
    @Column(name = "FIELD110")
    private String field110;
    @Column(name = "FIELD111")
    private String field111;
    @Column(name = "FIELD112")
    private String field112;
    @Column(name = "FIELD113")
    private String field113;
    @Column(name = "FIELD114")
    private String field114;
    @Column(name = "FIELD115")
    private String field115;
    @Column(name = "FIELD116")
    private String field116;
    @Column(name = "FIELD117")
    private String field117;
    @Column(name = "FIELD118")
    private String field118;
    @Column(name = "FIELD119")
    private String field119;
    @Column(name = "FIELD120")
    private String field120;
    @Column(name = "FIELD121")
    private String field121;
    @Column(name = "FIELD122")
    private String field122;
    @Column(name = "FIELD123")
    private String field123;
    @Column(name = "FIELD124")
    private String field124;
    @Column(name = "FIELD125")
    private String field125;
    @Column(name = "FIELD126")
    private String field126;
    @Column(name = "FIELD127")
    private String field127;
    @Column(name = "FIELD128")
    private String field128;
    @Column(name = "INSERTTIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date inserttime;
    @Column(name = "STATUS")
    private String status;

    public OnlineActivity() {
    }

    public OnlineActivity(BigDecimal transactionnumber) {
        this.transactionnumber = transactionnumber;
    }

    public BigDecimal getTransactionnumber() {
        return transactionnumber;
    }

    public void setTransactionnumber(BigDecimal transactionnumber) {
        this.transactionnumber = transactionnumber;
    }

    public String getField000() {
        return field000;
    }

    public void setField000(String field000) {
        this.field000 = field000;
    }

    public String getField001() {
        return field001;
    }

    public void setField001(String field001) {
        this.field001 = field001;
    }

    public String getField002() {
        return field002;
    }

    public void setField002(String field002) {
        this.field002 = field002;
    }

    public String getField003() {
        return field003;
    }

    public void setField003(String field003) {
        this.field003 = field003;
    }

    public String getField004() {
        return field004;
    }

    public void setField004(String field004) {
        this.field004 = field004;
    }

    public String getField005() {
        return field005;
    }

    public void setField005(String field005) {
        this.field005 = field005;
    }

    public String getField006() {
        return field006;
    }

    public void setField006(String field006) {
        this.field006 = field006;
    }

    public String getField007() {
        return field007;
    }

    public void setField007(String field007) {
        this.field007 = field007;
    }

    public String getField008() {
        return field008;
    }

    public void setField008(String field008) {
        this.field008 = field008;
    }

    public String getField009() {
        return field009;
    }

    public void setField009(String field009) {
        this.field009 = field009;
    }

    public String getField010() {
        return field010;
    }

    public void setField010(String field010) {
        this.field010 = field010;
    }

    public String getField011() {
        return field011;
    }

    public void setField011(String field011) {
        this.field011 = field011;
    }

    public String getField012() {
        return field012;
    }

    public void setField012(String field012) {
        this.field012 = field012;
    }

    public String getField013() {
        return field013;
    }

    public void setField013(String field013) {
        this.field013 = field013;
    }

    public String getField014() {
        return field014;
    }

    public void setField014(String field014) {
        this.field014 = field014;
    }

    public String getField015() {
        return field015;
    }

    public void setField015(String field015) {
        this.field015 = field015;
    }

    public String getField016() {
        return field016;
    }

    public void setField016(String field016) {
        this.field016 = field016;
    }

    public String getField017() {
        return field017;
    }

    public void setField017(String field017) {
        this.field017 = field017;
    }

    public String getField018() {
        return field018;
    }

    public void setField018(String field018) {
        this.field018 = field018;
    }

    public String getField019() {
        return field019;
    }

    public void setField019(String field019) {
        this.field019 = field019;
    }

    public String getField020() {
        return field020;
    }

    public void setField020(String field020) {
        this.field020 = field020;
    }

    public String getField021() {
        return field021;
    }

    public void setField021(String field021) {
        this.field021 = field021;
    }

    public String getField022() {
        return field022;
    }

    public void setField022(String field022) {
        this.field022 = field022;
    }

    public String getField023() {
        return field023;
    }

    public void setField023(String field023) {
        this.field023 = field023;
    }

    public String getField024() {
        return field024;
    }

    public void setField024(String field024) {
        this.field024 = field024;
    }

    public String getField025() {
        return field025;
    }

    public void setField025(String field025) {
        this.field025 = field025;
    }

    public String getField026() {
        return field026;
    }

    public void setField026(String field026) {
        this.field026 = field026;
    }

    public String getField027() {
        return field027;
    }

    public void setField027(String field027) {
        this.field027 = field027;
    }

    public String getField028() {
        return field028;
    }

    public void setField028(String field028) {
        this.field028 = field028;
    }

    public String getField029() {
        return field029;
    }

    public void setField029(String field029) {
        this.field029 = field029;
    }

    public String getField030() {
        return field030;
    }

    public void setField030(String field030) {
        this.field030 = field030;
    }

    public String getField031() {
        return field031;
    }

    public void setField031(String field031) {
        this.field031 = field031;
    }

    public String getField032() {
        return field032;
    }

    public void setField032(String field032) {
        this.field032 = field032;
    }

    public String getField033() {
        return field033;
    }

    public void setField033(String field033) {
        this.field033 = field033;
    }

    public String getField034() {
        return field034;
    }

    public void setField034(String field034) {
        this.field034 = field034;
    }

    public String getField035() {
        return field035;
    }

    public void setField035(String field035) {
        this.field035 = field035;
    }

    public String getField036() {
        return field036;
    }

    public void setField036(String field036) {
        this.field036 = field036;
    }

    public String getField037() {
        return field037;
    }

    public void setField037(String field037) {
        this.field037 = field037;
    }

    public String getField038() {
        return field038;
    }

    public void setField038(String field038) {
        this.field038 = field038;
    }

    public String getField039() {
        return field039;
    }

    public void setField039(String field039) {
        this.field039 = field039;
    }

    public String getField040() {
        return field040;
    }

    public void setField040(String field040) {
        this.field040 = field040;
    }

    public String getField041() {
        return field041;
    }

    public void setField041(String field041) {
        this.field041 = field041;
    }

    public String getField042() {
        return field042;
    }

    public void setField042(String field042) {
        this.field042 = field042;
    }

    public String getField043() {
        return field043;
    }

    public void setField043(String field043) {
        this.field043 = field043;
    }

    public String getField044() {
        return field044;
    }

    public void setField044(String field044) {
        this.field044 = field044;
    }

    public String getField045() {
        return field045;
    }

    public void setField045(String field045) {
        this.field045 = field045;
    }

    public String getField046() {
        return field046;
    }

    public void setField046(String field046) {
        this.field046 = field046;
    }

    public String getField047() {
        return field047;
    }

    public void setField047(String field047) {
        this.field047 = field047;
    }

    public String getField048() {
        return field048;
    }

    public void setField048(String field048) {
        this.field048 = field048;
    }

    public String getField049() {
        return field049;
    }

    public void setField049(String field049) {
        this.field049 = field049;
    }

    public String getField050() {
        return field050;
    }

    public void setField050(String field050) {
        this.field050 = field050;
    }

    public String getField051() {
        return field051;
    }

    public void setField051(String field051) {
        this.field051 = field051;
    }

    public String getField052() {
        return field052;
    }

    public void setField052(String field052) {
        this.field052 = field052;
    }

    public String getField053() {
        return field053;
    }

    public void setField053(String field053) {
        this.field053 = field053;
    }

    public String getField054() {
        return field054;
    }

    public void setField054(String field054) {
        this.field054 = field054;
    }

    public String getField055() {
        return field055;
    }

    public void setField055(String field055) {
        this.field055 = field055;
    }

    public String getField056() {
        return field056;
    }

    public void setField056(String field056) {
        this.field056 = field056;
    }

    public String getField057() {
        return field057;
    }

    public void setField057(String field057) {
        this.field057 = field057;
    }

    public String getField058() {
        return field058;
    }

    public void setField058(String field058) {
        this.field058 = field058;
    }

    public String getField059() {
        return field059;
    }

    public void setField059(String field059) {
        this.field059 = field059;
    }

    public String getField060() {
        return field060;
    }

    public void setField060(String field060) {
        this.field060 = field060;
    }

    public String getField061() {
        return field061;
    }

    public void setField061(String field061) {
        this.field061 = field061;
    }

    public String getField062() {
        return field062;
    }

    public void setField062(String field062) {
        this.field062 = field062;
    }

    public String getField063() {
        return field063;
    }

    public void setField063(String field063) {
        this.field063 = field063;
    }

    public String getField064() {
        return field064;
    }

    public void setField064(String field064) {
        this.field064 = field064;
    }

    public String getField065() {
        return field065;
    }

    public void setField065(String field065) {
        this.field065 = field065;
    }

    public String getField066() {
        return field066;
    }

    public void setField066(String field066) {
        this.field066 = field066;
    }

    public String getField067() {
        return field067;
    }

    public void setField067(String field067) {
        this.field067 = field067;
    }

    public String getField068() {
        return field068;
    }

    public void setField068(String field068) {
        this.field068 = field068;
    }

    public String getField069() {
        return field069;
    }

    public void setField069(String field069) {
        this.field069 = field069;
    }

    public String getField070() {
        return field070;
    }

    public void setField070(String field070) {
        this.field070 = field070;
    }

    public String getField071() {
        return field071;
    }

    public void setField071(String field071) {
        this.field071 = field071;
    }

    public String getField072() {
        return field072;
    }

    public void setField072(String field072) {
        this.field072 = field072;
    }

    public String getField073() {
        return field073;
    }

    public void setField073(String field073) {
        this.field073 = field073;
    }

    public String getField074() {
        return field074;
    }

    public void setField074(String field074) {
        this.field074 = field074;
    }

    public String getField075() {
        return field075;
    }

    public void setField075(String field075) {
        this.field075 = field075;
    }

    public String getField076() {
        return field076;
    }

    public void setField076(String field076) {
        this.field076 = field076;
    }

    public String getField077() {
        return field077;
    }

    public void setField077(String field077) {
        this.field077 = field077;
    }

    public String getField078() {
        return field078;
    }

    public void setField078(String field078) {
        this.field078 = field078;
    }

    public String getField079() {
        return field079;
    }

    public void setField079(String field079) {
        this.field079 = field079;
    }

    public String getField080() {
        return field080;
    }

    public void setField080(String field080) {
        this.field080 = field080;
    }

    public String getField081() {
        return field081;
    }

    public void setField081(String field081) {
        this.field081 = field081;
    }

    public String getField082() {
        return field082;
    }

    public void setField082(String field082) {
        this.field082 = field082;
    }

    public String getField083() {
        return field083;
    }

    public void setField083(String field083) {
        this.field083 = field083;
    }

    public String getField084() {
        return field084;
    }

    public void setField084(String field084) {
        this.field084 = field084;
    }

    public String getField085() {
        return field085;
    }

    public void setField085(String field085) {
        this.field085 = field085;
    }

    public String getField086() {
        return field086;
    }

    public void setField086(String field086) {
        this.field086 = field086;
    }

    public String getField087() {
        return field087;
    }

    public void setField087(String field087) {
        this.field087 = field087;
    }

    public String getField088() {
        return field088;
    }

    public void setField088(String field088) {
        this.field088 = field088;
    }

    public String getField089() {
        return field089;
    }

    public void setField089(String field089) {
        this.field089 = field089;
    }

    public String getField090() {
        return field090;
    }

    public void setField090(String field090) {
        this.field090 = field090;
    }

    public String getField091() {
        return field091;
    }

    public void setField091(String field091) {
        this.field091 = field091;
    }

    public String getField092() {
        return field092;
    }

    public void setField092(String field092) {
        this.field092 = field092;
    }

    public String getField093() {
        return field093;
    }

    public void setField093(String field093) {
        this.field093 = field093;
    }

    public String getField094() {
        return field094;
    }

    public void setField094(String field094) {
        this.field094 = field094;
    }

    public String getField095() {
        return field095;
    }

    public void setField095(String field095) {
        this.field095 = field095;
    }

    public String getField096() {
        return field096;
    }

    public void setField096(String field096) {
        this.field096 = field096;
    }

    public String getField097() {
        return field097;
    }

    public void setField097(String field097) {
        this.field097 = field097;
    }

    public String getField098() {
        return field098;
    }

    public void setField098(String field098) {
        this.field098 = field098;
    }

    public String getField099() {
        return field099;
    }

    public void setField099(String field099) {
        this.field099 = field099;
    }

    public String getField100() {
        return field100;
    }

    public void setField100(String field100) {
        this.field100 = field100;
    }

    public String getField101() {
        return field101;
    }

    public void setField101(String field101) {
        this.field101 = field101;
    }

    public String getField102() {
        return field102;
    }

    public void setField102(String field102) {
        this.field102 = field102;
    }

    public String getField103() {
        return field103;
    }

    public void setField103(String field103) {
        this.field103 = field103;
    }

    public String getField104() {
        return field104;
    }

    public void setField104(String field104) {
        this.field104 = field104;
    }

    public String getField105() {
        return field105;
    }

    public void setField105(String field105) {
        this.field105 = field105;
    }

    public String getField106() {
        return field106;
    }

    public void setField106(String field106) {
        this.field106 = field106;
    }

    public String getField107() {
        return field107;
    }

    public void setField107(String field107) {
        this.field107 = field107;
    }

    public String getField108() {
        return field108;
    }

    public void setField108(String field108) {
        this.field108 = field108;
    }

    public String getField109() {
        return field109;
    }

    public void setField109(String field109) {
        this.field109 = field109;
    }

    public String getField110() {
        return field110;
    }

    public void setField110(String field110) {
        this.field110 = field110;
    }

    public String getField111() {
        return field111;
    }

    public void setField111(String field111) {
        this.field111 = field111;
    }

    public String getField112() {
        return field112;
    }

    public void setField112(String field112) {
        this.field112 = field112;
    }

    public String getField113() {
        return field113;
    }

    public void setField113(String field113) {
        this.field113 = field113;
    }

    public String getField114() {
        return field114;
    }

    public void setField114(String field114) {
        this.field114 = field114;
    }

    public String getField115() {
        return field115;
    }

    public void setField115(String field115) {
        this.field115 = field115;
    }

    public String getField116() {
        return field116;
    }

    public void setField116(String field116) {
        this.field116 = field116;
    }

    public String getField117() {
        return field117;
    }

    public void setField117(String field117) {
        this.field117 = field117;
    }

    public String getField118() {
        return field118;
    }

    public void setField118(String field118) {
        this.field118 = field118;
    }

    public String getField119() {
        return field119;
    }

    public void setField119(String field119) {
        this.field119 = field119;
    }

    public String getField120() {
        return field120;
    }

    public void setField120(String field120) {
        this.field120 = field120;
    }

    public String getField121() {
        return field121;
    }

    public void setField121(String field121) {
        this.field121 = field121;
    }

    public String getField122() {
        return field122;
    }

    public void setField122(String field122) {
        this.field122 = field122;
    }

    public String getField123() {
        return field123;
    }

    public void setField123(String field123) {
        this.field123 = field123;
    }

    public String getField124() {
        return field124;
    }

    public void setField124(String field124) {
        this.field124 = field124;
    }

    public String getField125() {
        return field125;
    }

    public void setField125(String field125) {
        this.field125 = field125;
    }

    public String getField126() {
        return field126;
    }

    public void setField126(String field126) {
        this.field126 = field126;
    }

    public String getField127() {
        return field127;
    }

    public void setField127(String field127) {
        this.field127 = field127;
    }

    public String getField128() {
        return field128;
    }

    public void setField128(String field128) {
        this.field128 = field128;
    }

    public Date getInserttime() {
        return inserttime;
    }

    public void setInserttime(Date inserttime) {
        this.inserttime = inserttime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (transactionnumber != null ? transactionnumber.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OnlineActivity)) {
            return false;
        }
        OnlineActivity other = (OnlineActivity) object;
        if ((this.transactionnumber == null && other.transactionnumber != null) || (this.transactionnumber != null && !this.transactionnumber.equals(other.transactionnumber))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.oracleufs.OnlineActivity[ transactionnumber=" + transactionnumber + " ]";
    }
    
}
