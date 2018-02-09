package engine;

import java.util.HashMap;

/**
 * These are essentially global variables accessible to the
 * entire engine/application. They can be in part default-initialized
 * via code, part overwritten by command line arguments, and part overwritten
 * by values read in from a file.
 *
 * @author Justin Hall
 */
public class ConsoleVariables {
    private HashMap<String, ConsoleVariable> _cvars = new HashMap<>();

    /**
     * Registers a console variable with the cvar system
     */
    public void registerVariable(ConsoleVariable cvar)
    {
        // If it already exists then do nothing except potentially
        // override the default value if it is different
        if (contains(cvar.getcvarName()))
        {
            find(cvar.getcvarName()).setDefault(cvar.getcvarDefault());
        }
        else _cvars.put(cvar.getcvarName(), cvar);
    }

    public void unregisterVariable(String cvar)
    {
        _cvars.remove(cvar);
    }

    public boolean contains(String cvar)
    {
        return _cvars.containsKey(cvar);
    }

    /**
     * Warning! This can return null!
     */
    public ConsoleVariable find(String cvar)
    {
        if (contains(cvar)) return _cvars.get(cvar);
        return null;
    }
}
