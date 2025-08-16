package com.whitewoodcity.control;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

public class NumberField extends TextField {
  private DoubleProperty value = new SimpleDoubleProperty();

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
      var oldText = c.getControlText();
      if (newText.trim().isEmpty()) {
        setText("0");
        value.set(0);
        positionCaret(1);
        return null;
      }if (newText.trim().equals(".")) {
        setText("0.");
        positionCaret(2);
        value.set(0);
        return null;
      }  else if (!validate(newText)) {
        setText(oldText);
        value.set(Double.parseDouble(oldText));
        return null;
      } else if (Double.parseDouble(newText) > this.maxValue) {
        setText(this.maxValue + "");
        positionCaret(getText().length());
        value.set(maxValue);
        return null;
      } else if (Double.parseDouble(newText) < this.minValue) {
        setText(this.minValue + "");
        positionCaret(getText().length());
        value.set(minValue);
        return null;
      } else {
        value.set(Double.parseDouble(newText));
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

  public double getValue() {
    return value.get();
  }

  public DoubleProperty valueProperty() {
    return value;
  }
}