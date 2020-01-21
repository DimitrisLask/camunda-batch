package delegate.definitions;

import delegate.Delegate;

import java.util.Map;
import java.util.Random;

public class CalculateValue extends Delegate {

    public CalculateValue(Map<String, Object> variables){
        super(variables);
    }

    @Override
    public void execute(){

        long amount = (long)variables.get("amount");
        long price = (long)variables.get("price");
        Random random = new Random();
        long newPrice = (long)random.nextInt((int) (15-price))+30;
        long newValue = amount*newPrice;

        variables.put("newValue", newValue);
    }
}
