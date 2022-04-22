package com.example.Vu;

import org.jsoup.select.Elements;

public class OilPrice {
    private String idx;
    private String name;
    private String price;
    private String deviation;

    public void setOilPrice(Elements cols) {
        this.idx = cols.get(0).text();
        this.name = cols.get(1).text();
        this.price = cols.get(2).text();
        this.deviation = cols.get(3).text();
    }

    public String getOilPrice() {
        String ret = idx + ". " + "Tên mặt hàng: " + name + '\n' + "    " +
                     "Giá (VND/lít): " + price + '\n'+ "    " +
                     "Chênh lệch (tăng/giảm): " + deviation +
                     '\n' + "-----------------" + '\n';
        return ret;
    }
}
