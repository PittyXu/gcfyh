public class DataItem {

  private final int resultOrder;
  private int checkedOrder;
  private final String name;
  private final long num;

  private DataItem(int pOrder, String pName, long pNum) {
    resultOrder = pOrder;
    name = pName;
    num = pNum;
  }

  public int getResultOrder() {
    return resultOrder;
  }

  public String getName() {
    return name;
  }

  public long getNum() {
    return num;
  }

  public void setCheckedOrder(int pOrder) {
    checkedOrder = pOrder;
  }

  public int getCheckedOrder() {
    return checkedOrder;
  }

  public boolean isMatch() {
    return resultOrder == checkedOrder;
  }

  public static DataItem getItem(String line) {
    String trimLine = line.trim();
    if (trimLine.length() == 0) {
      return null;
    }

    StringBuilder order = new StringBuilder();
    StringBuilder name = new StringBuilder();
    StringBuilder num = new StringBuilder();
    for (int i = 0; i < trimLine.length(); i++) {
      char current = trimLine.charAt(i);
      if (Character.isDigit(current)) {
        if (name.length() > 0) {
          num.append(current);
        } else {
          order.append(current);
        }
      } else {
        if (i == 0) {
          return null;
        }
        name.append(current);
      }
    }

    return new DataItem(Integer.valueOf(order.toString().trim()), name.toString().trim(), Long.valueOf(num.toString().trim()));
  }
}
