package com.example.ssuns.finaltopic;

import java.util.List;

public class TwoDaysJson {

    private boolean success;
    private String locationName;
    private WeatherElementBean weatherElement;

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public WeatherElementBean getWeatherElement() {
        return weatherElement;
    }

    public void setWeatherElement(WeatherElementBean weatherElement) {
        this.weatherElement = weatherElement;
    }

    public static class WeatherElementBean {
        private List<WxBean> Wx;
        private List<ATBean> AT;
        private List<TBean> T;
        private List<RHBean> RH;
        private List<CIBean> CI;
        private List<WeatherDescriptionBean> WeatherDescription;
        private List<PoP6hBean> PoP6h;
        private List<WSBean> WS;
        private List<TdBean> Td;

        public List<WxBean> getWx() {
            return Wx;
        }

        public void setWx(List<WxBean> Wx) {
            this.Wx = Wx;
        }

        public List<ATBean> getAT() {
            return AT;
        }

        public void setAT(List<ATBean> AT) {
            this.AT = AT;
        }

        public List<TBean> getT() {
            return T;
        }

        public void setT(List<TBean> T) {
            this.T = T;
        }

        public List<RHBean> getRH() {
            return RH;
        }

        public void setRH(List<RHBean> RH) {
            this.RH = RH;
        }

        public List<CIBean> getCI() {
            return CI;
        }

        public void setCI(List<CIBean> CI) {
            this.CI = CI;
        }

        public List<WeatherDescriptionBean> getWeatherDescription() {
            return WeatherDescription;
        }

        public void setWeatherDescription(List<WeatherDescriptionBean> WeatherDescription) {
            this.WeatherDescription = WeatherDescription;
        }

        public List<PoP6hBean> getPoP6h() {
            return PoP6h;
        }

        public void setPoP6h(List<PoP6hBean> PoP6h) {
            this.PoP6h = PoP6h;
        }

        public List<WSBean> getWS() {
            return WS;
        }

        public void setWS(List<WSBean> WS) {
            this.WS = WS;
        }

        public List<TdBean> getTd() {
            return Td;
        }

        public void setTd(List<TdBean> Td) {
            this.Td = Td;
        }

        public static class WxBean {
            /**
             * time : 2018-06-14
             * value : 短暫陣雨或雷雨
             */

            private String time;
            private String value;

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }

        public static class ATBean {
            /**
             * time : 2018-06-14
             * value : 32
             */

            private String time;
            private int value;

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public int getValue() {
                return value;
            }

            public void setValue(int value) {
                this.value = value;
            }
        }

        public static class TBean {

            private String date;
            private String time;
            private int value;

            public void setDate(String date) {
                this.date = date;
            }

            public String getDate() {
                return date;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public int getValue() {
                return value;
            }

            public void setValue(int value) {
                this.value = value;
            }
        }

        public static class RHBean {
            /**
             * time : 2018-06-14
             * value : 91
             */

            private String time;
            private int value;

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public int getValue() {
                return value;
            }

            public void setValue(int value) {
                this.value = value;
            }
        }

        public static class CIBean {
            /**
             * time : 2018-06-14
             * value : 29, 悶熱
             */

            private String time;
            private String value;

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }

        public static class WeatherDescriptionBean {
            /**
             * time : 2018-06-14
             * value : 多雲。降雨機率 80%。溫度攝氏30度。悶熱。偏東風 平均風速1-2級(每秒3公尺)。相對濕度86%。
             */

            private String time;
            private String value;

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }

        public static class PoP6hBean {

            private String date;
            private String time;
            private int value;

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public int getValue() {
                return value;
            }

            public void setValue(int value) {
                this.value = value;
            }
        }

        public static class WSBean {
            /**
             * time : 2018-06-14
             * value : 4
             */

            private String time;
            private int value;

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public int getValue() {
                return value;
            }

            public void setValue(int value) {
                this.value = value;
            }
        }

        public static class TdBean {
            /**
             * time : 2018-06-14
             * value : 27
             */

            private String time;
            private int value;

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public int getValue() {
                return value;
            }

            public void setValue(int value) {
                this.value = value;
            }
        }
    }
}
