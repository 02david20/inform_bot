package com.example.basicwebscrape.OilPrice;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class OilPrice {
    private static OilPrice oilPirce_instance = new OilPrice();
    private String oilprice;

    private OilPrice() {}

    public static OilPrice getOilPriceInstance() {
        return oilPirce_instance;
    }

    public String getOilPrice() {
        try {
            String url = "https://www.pvoil.com.vn/truyen-thong/tin-gia-xang-dau";
            Document doc = Jsoup.connect(url).get();
            Element table = doc.select("table").get(0);
            Elements allRows = table.select("tr");
            oilprice = allRows
                            .select("span")
                            .get(0)
                            .text() + "\n-----------------------------------\n";
            for (int i = 2; i < allRows.size(); ++i) {
                Element row = allRows.get(i);
                Elements col = row.select("td");
                setOilPrice(col);
            }
            return oilprice + "Nguồn: " + url;
        } catch (Exception e) {
            e.printStackTrace();
            return "Có lỗi xảy ra";
        }
    }

    private void setOilPrice(Elements col) {
        String idx = col.get(0).text();
        String name = col.get(1).text();
        String price = col.get(2).text();
        String deviation = col.get(3).text();
        oilprice += idx + ". " + "Tên mặt hàng: " + name + '\n' + "    " +
                    "Giá (VND/lít): " + price + '\n'+ "    " +
                    "Chênh lệch (tăng/giảm): " + deviation +
                    '\n' + "-----------------------------------" + '\n';
    }
}
