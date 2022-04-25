package com.example.Vu;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class OilPrice {
    private static String oilprice;

    public static String returnOilPrice(Document doc, String url) {
        Element table = doc.select("table").get(0);
        Elements allRows = table.select("tr");
        oilprice = allRows
                        .select("span")
                        .get(0)
                        .text() + "\n-----------------------------------\n";
        for (int i = 2; i < allRows.size(); ++i) {
            Element row = allRows.get(i);
            Elements cols = row.select("td");
            setOilPrice(cols);
        }
        return oilprice + "Nguồn: " + url;
    }

    private static void setOilPrice(Elements cols) {
        String idx = cols.get(0).text();
        String name = cols.get(1).text();
        String price = cols.get(2).text();
        String deviation = cols.get(3).text();
        oilprice += idx + ". " + "Tên mặt hàng: " + name + '\n' + "    " +
                    "Giá (VND/lít): " + price + '\n'+ "    " +
                    "Chênh lệch (tăng/giảm): " + deviation +
                    '\n' + "-----------------------------------" + '\n';
    }
}
