package com.exhibition.enumerate.venue;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum County {

    KEELUNG("KEELUNG", "10017", "基隆市"),
    TAIPEI("TAIPEI", "63000", "台北市"),
    NEW_TAIPEI("NEW_TAIPEI", "65000", "新北市"),
    TAOYUAN("TAOYUAN", "68000", "桃園市"),
    HSINCHU_CITY("HSINCHU_CITY", "10018", "新竹市"),
    HSINCHU_COUNTY("HSINCHU_COUNTY", "10004", "新竹縣"),
    MIAOLI("MIAOLI", "10005", "苗栗縣"),
    TAICHUNG("TAICHUNG", "66000", "台中市"),
    CHANGHUA("CHANGHUA", "10007", "彰化縣"),
    YUNLIN("YUNLIN", "10009", "雲林縣"),
    CHIAYI_CITY("CHIAYI_CITY", "10020", "嘉義市"),
    CHIAYI_COUNTY("CHIAYI_COUNTY", "10010", "嘉義縣"),
    TAINAN("TAINAN", "67000", "台南市"),
    KAOHSIUNG("KAOHSIUNG", "64000", "高雄市"),
    PINGTUNG("PINGTUNG", "10013", "屏東縣"),
    TAITUNG("TAITUNG", "10014", "台東縣"),
    HUALIEN("HUALIEN", "10015", "花蓮縣"),
    YILAN("YILAN", "10002", "宜蘭縣"),
    PENGHU("PENGHU", "10016", "澎湖縣"),
    KINMEN("KINMEN", "9020", "金門縣"),
    LIENCHIANG("LIENCHIANG", "9007", "連江縣");


    private final String value;
    private final String srcCode;
    private final String srcAbbreviation;

    public static County ofValue(String value) {
        for (County status : County.values()) {
            if (status.value.equals(value)) {
                return status;
            }
        }
        throw new EnumMappingException("County can't map with value " + value, County.class);
    }

    public static County ofSrcCode(String srcCode) {
        for (County status : County.values()) {
            if (status.srcCode.equals(srcCode)) {
                return status;
            }
        }
        throw new EnumMappingException("County can't map with srcCode " + srcCode, County.class);
    }

    public static County ofSrcAbbreviation(String srcAbbreviation) {
        for (County status : County.values()) {
            if (status.srcCode.equals(srcAbbreviation)) {
                return status;
            }
        }
        throw new EnumMappingException("County can't map with srcAbbreviation " + srcAbbreviation, County.class);
    }
}
