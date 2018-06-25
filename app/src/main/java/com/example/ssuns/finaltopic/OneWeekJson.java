package com.example.ssuns.finaltopic;

import java.util.List;

public class OneWeekJson {

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
        private List<PoP12hBean> PoP12h;
        private List<TBean> T;
        private List<RHBean> RH;
        private List<WxBean> Wx;
        private List<MinTBean> MinT;
        private List<UVIBean> UVI;
        private List<WeatherDescriptionBean> WeatherDescription;
        private List<MaxTBean> MaxT;

        public List<PoP12hBean> getPoP12h() {
            return PoP12h;
        }

        public void setPoP12h(List<PoP12hBean> PoP12h) {
            this.PoP12h = PoP12h;
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

        public List<WxBean> getWx() {
            return Wx;
        }

        public void setWx(List<WxBean> Wx) {
            this.Wx = Wx;
        }

        public List<MinTBean> getMinT() {
            return MinT;
        }

        public void setMinT(List<MinTBean> MinT) {
            this.MinT = MinT;
        }

        public List<UVIBean> getUVI() {
            return UVI;
        }

        public void setUVI(List<UVIBean> UVI) {
            this.UVI = UVI;
        }

        public List<WeatherDescriptionBean> getWeatherDescription() {
            return WeatherDescription;
        }

        public void setWeatherDescription(List<WeatherDescriptionBean> WeatherDescription) {
            this.WeatherDescription = WeatherDescription;
        }

        public List<MaxTBean> getMaxT() {
            return MaxT;
        }

        public void setMaxT(List<MaxTBean> MaxT) {
            this.MaxT = MaxT;
        }

        public static class PoP12hBean {
            /**
             * time : 2018-06-15
             * value : 63
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
            /**
             * time : 2018-06-15
             * value : 25
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

        public static class RHBean {
            /**
             * time : 2018-06-15
             * value : 89
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

        public static class WxBean {
            /**
             * time : 2018-06-15
             * value : 陰短暫陣雨或雷雨
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

        public static class MinTBean {
            /**
             * time : 2018-06-15
             * value : 24
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

        public static class UVIBean {
            /**
             * time : 2018-06-15
             * value : 中量級
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
             * time : 2018-06-15
             * value : 多雲。降雨機率 20%。溫度攝氏24至28度。舒適。東北風 風速2級(每秒3公尺)。相對濕度89%。
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

        public static class MaxTBean {
            /**
             * time : 2018-06-15
             * value : 25
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
