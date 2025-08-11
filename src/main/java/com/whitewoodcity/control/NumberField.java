package com.whitewoodcity.control;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

public class NumberField extends TextField {

  private double minValue, maxValue;

  public NumberField(int maxValue) {
    this(0, maxValue);
  }

  public NumberField(int minValue, int maxValue) {
    assert(minValue <= maxValue);
    this.minValue = minValue;
    this.maxValue = maxValue;

    this.setTextFormatter(new TextFormatter<>(c -> {
      var newText = c.getControlNewText();
      if (newText.trim().isEmpty()) {
        setText("0");
        return null;
      }if (newText.trim().equals(".")) {
        setText("0.");
        positionCaret(2);
        return null;
      }  else if (!validate(newText)) {
        setText(c.getControlText());
        return null;
      } else if (Double.parseDouble(newText) > this.maxValue) {
        setText(this.maxValue + "");
        return null;
      } else if (Double.parseDouble(newText) < this.minValue) {
        setText(this.minValue + "");
        return null;
      } else {
        return c;
      }
    }
    ));
  }

  protected boolean validate(String s) {
    try {
      Double.parseDouble(s);
      return true;
    } catch (Throwable throwable) {
      return false;
    }
  }

  public double getDouble(){
    return Double.parseDouble(getText());
  }

  public double getMinValue() {
    return minValue;
  }

  public void setMinValue(double minValue) {
    this.minValue = minValue;
  }

  public double getMaxValue() {
    return maxValue;
  }

  public void setMaxValue(double maxValue) {
    this.maxValue = maxValue;
  }
}