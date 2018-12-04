import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class Main {

  public static void main(String[] args) throws Exception {
    String dataPath = "src/main/resources/data.pdf";
    // 读取PDF数据
    String content = readTextFromPdf(new File(dataPath));
    int total = 0;
    int seed = 0;
    List<DataItem> result = new ArrayList<>();
    // 解析PDF数据
    for (String line : content.split("\n")) {
      if (total <= 0 && line.contains("摇取")) {
        total = Integer.valueOf(line.split("摇取")[1].split("户")[0].trim());
      } else if (seed <= 0 && line.contains("随机种子号为")) {
        seed = Integer.valueOf(line.split("随机种子号为")[1].trim());
      } else {
        DataItem item = DataItem.getItem(line);
        if (null != item) {
          result.add(item);
        }
      }
    }

    System.out.println("摇号总数: " + total);
    System.out.println("随机种子: " + seed);
    System.out.println("数据大小: " + result.size());
    if (total != result.size()) {
      System.err.println("摇号总数和数据大小不匹配, 请检查是否有未解析或遗漏数据");
      for (int i = 0; i < result.size(); i++) {
        DataItem item = result.get(i);
        if (null != item && item.getResultOrder() != i + 1) {
          System.err.println("第" + (i + 1) + "条数据不匹配: " + item.getResultOrder() + ", " + item.getName() + ", "
            + item.getNum());
          return;
        } else if (null == item) {
          System.err.println("摇号总数和数据大小不匹配, 数据缺失");
          return;
        }
      }
    }

    // 按照num排序
    result.sort((o1, o2) -> {
      if (o1.getNum() > o2.getNum()) {
        return 1;
      } else if (o1.getNum() < o2.getNum()) {
        return -1;
      }
      return 0;
    });

    // 取.Net 随机数
    Random random = new Random(seed);
    List<DataItem> notMatch = new ArrayList<>();
    for (int i = 0; i < total; i++) {
      int order = random.Next(total);
      DataItem current = result.get(order);
      if (current.getCheckedOrder() <= 0) {
        current.setCheckedOrder(i + 1);
      } else {// 已经中过, 忽略
        i--;
      }
      if (!current.isMatch()) {
        notMatch.add(current);
      }
    }

    // 有不匹配的数据
    if (notMatch.size() > 0) {
      System.out.println("有" + notMatch.size() + "条未验证通过");
      for (DataItem item : notMatch) {
        System.out.println("摇号结果: " + item.getResultOrder() + ", " + item.getName() + ", "
          + item.getNum() + ", 检查结果: " + item.getCheckedOrder());
      }
    } else {
      System.out.println("全部验证通过!");
    }
  }

  public static String readTextFromPdf(File pdf) throws IOException {
    try (PDDocument document = PDDocument.load(pdf)) {
      PDFTextStripper stripper = new PDFTextStripper();
      return stripper.getText(document);
    }
  }
}


