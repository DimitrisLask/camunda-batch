package delegate;

import java.util.Map;

public abstract class Delegate {

    protected Map<String, Object> variables;

    public Delegate( Map<String, Object> variables){
        this.variables = variables;
        this.execute();
    }

    public abstract void execute();

}
