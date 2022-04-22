package com.example.Vu;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class OilPrice {
    private String idx;
    private String name;
    private String price;
    private String deviation;
    private String oilprice;

    public String returnOilPrice(Document doc, String url) {
        Element table = doc.select("table").get(0);
        Elements allRows = table.select("tr");
        this.oilprice = allRows
                        .get(0)
                        .select("td")
                        .get(2)
                        .select("span")
                        .get(0)
                        .text() + "\n\n";
        for (int i = 2; i < allRows.size(); ++i) {
            Element row = allRows.get(i);
            Elements cols = row.select("td");
            setOilPrice(cols);
        }
        this.oilprice += "Nguồn: " + url;
        return this.oilprice;
    }

    private void setOilPrice(Elements cols) {
        this.idx = cols.get(0).text();
        this.name = cols.get(1).text();
        this.price = cols.get(2).text();
        this.deviation = cols.get(3).text();
        this.oilprice += idx + ". " + "Tên mặt hàng: " + name + '\n' + "    " +
                     "Giá (VND/lít): " + price + '\n'+ "    " +
                     "Chênh lệch (tăng/giảm): " + deviation +
                     '\n' + "-----------------" + '\n';
    }
}
