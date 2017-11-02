package com.suny.association.entity.po.baidulocation;

/**
 * Comments:   百度普通定位返回结果
 * @author :   孙建荣
 * Create Date: 2017/04/22 9:49
 */
public class GeneralLocationResult {

    /**
     * address : 地址
     * content :详细内容
     * status : 返回状态码
     */

    private String address;
    private ContentBean content;
    private int status;

    public String getAddress() {
        if (address == null) {
            return "未知地址";
        }
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ContentBean getContent() {
        return content;
    }

    public void setContent(ContentBean content) {
        this.content = content;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public class ContentBean {
        /**
         * address : 南昌市
         * address_detail : {"city":"南昌市","cityCode":131,"district":"","province":"南昌市","street":"","streetNumber":""}
         * point : {"x":"116.39564504","y":"39.92998578"}
         */

        private String address;
        private AddressDetailBean addressDetail;
        private PointBean point;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public AddressDetailBean getAddressDetail() {
            return addressDetail;
        }

        public void setAddressDetail(AddressDetailBean addressDetail) {
            this.addressDetail = addressDetail;
        }

        public PointBean getPoint() {
            return point;
        }

        public void setPoint(PointBean point) {
            this.point = point;
        }

        private class AddressDetailBean {
            /**
             *城市
             */
            private String city;
            /**
             * 城市代码
             */
            private int cityCode;
            /**
             *区县
             */
            private String district;
            /**
             *省份
             */
            private String province;
            /**
             * 街道
             * */
            private String street;
            /**
             *  门址
             *  */
            private String streetNumber;

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public int getCityCode() {
                return cityCode;
            }

            public void setCityCode(int cityCode) {
                this.cityCode = cityCode;
            }

            public String getDistrict() {
                return district;
            }

            public void setDistrict(String district) {
                this.district = district;
            }

            public String getProvince() {
                return province;
            }

            public void setProvince(String province) {
                this.province = province;
            }

            public String getStreet() {
                return street;
            }

            public void setStreet(String street) {
                this.street = street;
            }

            public String getStreetNumber() {
                return streetNumber;
            }

            public void setStreetNumber(String streetNumber) {
                this.streetNumber = streetNumber;
            }
        }

        /**
         * 城市中心点
         */
        private class PointBean {
            /**
             * x : 116.39564504
             * y : 39.92998578
             */

            private String x;
            private String y;

            public String getX() {
                return x;
            }

            public void setX(String x) {
                this.x = x;
            }

            public String getY() {
                return y;
            }

            public void setY(String y) {
                this.y = y;
            }
        }
    }
}
